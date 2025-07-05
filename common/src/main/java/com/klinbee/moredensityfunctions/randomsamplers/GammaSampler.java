package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public sealed interface GammaSampler extends RandomSampler {

    MapCodec<GammaSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("shape").forGetter(GammaSampler::shape),
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("scale").forGetter(GammaSampler::scale)
            ).apply(instance, GammaSampler::create)
    );

    double shape();
    double scale();

    @Override
    default double minValue() {
        return 0.0D;
    }

    @Override
    default double maxValue() {
        return Double.MAX_VALUE;
    }

    static GammaSampler create(double shape, double scale) {
        if (shape < 1.0D) {
            double inverseShape = 1.0D / shape;
            return new GammaSampler.AhrensDieter(shape, scale, inverseShape);
        }
        double shapeMinusOneThird = shape - 1.0D / 3.0D;
        double inverseSqrtShape = 1.0D / StrictMath.sqrt(9.0D * shapeMinusOneThird);
        return new GammaSampler.MarsagliaTsang(shape, scale, shapeMinusOneThird, inverseSqrtShape);
    }

    record AhrensDieter(double shape, double scale, double inverseShape) implements GammaSampler {
        @Override
        public double sample(long hashedSeed) {
            double u, v, w, x, y, z;

            do {
                u = RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                v = RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                w = u * (1.0D - u);
                y = StrictMath.sqrt(inverseShape / w) * (u - 0.5D);
                x = shape + y;
                if (x >= 0.0D) {
                    z = 64.0D * w * w * w * v * v;
                    if (z <= 1.0D - 2.0D * y * y / x) {
                        break;
                    }
                    if (StrictMath.log(z) <= 2.0D * (shape * StrictMath.log(x / shape) - y)) {
                        break;
                    }
                }
            } while (true);

            return x * scale;
        }
    }

    record MarsagliaTsang(double shape, double scale, double shapeMinusOneThird, double inverseSqrtShape) implements GammaSampler {
        @Override
        public double sample(long hashedSeed) {
            while (true) {
                double x, v;
                do {
                    x = RandomSampler.sampleGaussian(hashedSeed);
                    hashedSeed = RandomSampler.mix(hashedSeed);
                    v = 1.0D + inverseSqrtShape * x;
                } while (v <= 0);

                v = v * v * v;
                double u = RandomSampler.sampleDouble(hashedSeed);

                if (u < 1.0D - 0.0331D * x * x * x * x) {
                    return shapeMinusOneThird * v * scale;
                }

                if (StrictMath.log(u) < 0.5D * x * x + shapeMinusOneThird * (1.0D - v + StrictMath.log(v))) {
                    return shapeMinusOneThird * v * scale;
                }

            }
        }
    }

    KeyDispatchDataCodec<GammaSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<GammaSampler> getMapCodec() {
        return MAP_CODEC;
    }

    default Codec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
