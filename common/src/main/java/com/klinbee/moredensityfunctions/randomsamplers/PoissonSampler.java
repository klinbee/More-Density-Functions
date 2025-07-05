package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public sealed interface PoissonSampler extends RandomSampler {

    MapCodec<PoissonSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(PoissonSampler::lambda)
            ).apply(instance, PoissonSampler::create)
    );

    double lambda();

    @Override
    default double minValue() {
        return 0.0D;
    }

    @Override
    default double maxValue() {
        return Double.MAX_VALUE;
    }

    static PoissonSampler create(double lambda) {
        if (lambda < 30.0D) {
            double expNegativeLambda = StrictMath.exp(-lambda);
            return new Knuth(lambda, expNegativeLambda);
        }
        return new Normal(lambda, RandomSampler.buildNormal(lambda, StrictMath.sqrt(lambda)));
    }

    record Knuth(double lambda, double expNegativeLambda) implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            double p = 1.0D;
            int k = 0;

            do {
                k++;
                p *= RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
            } while (p > expNegativeLambda);

            return k - 1.0D;
        }
    }

    record Normal(double lambda, NormalSampler normalSampler) implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.round(normalSampler.sample(hashedSeed)));
        }
    }

    KeyDispatchDataCodec<PoissonSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<PoissonSampler> getMapCodec() {
        return MAP_CODEC;
    }

    default MapCodec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
