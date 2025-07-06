package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record ExponentialSampler(double lambda, double negativeInverseLambda) implements RandomSampler {

    public static final MapCodec<ExponentialSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(ExponentialSampler::lambda)
            ).apply(instance, ExponentialSampler::create)
    );

    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    static ExponentialSampler create(double lambda) {
        double negativeInverseLambda = -1.0D / lambda;
        return new ExponentialSampler(lambda, negativeInverseLambda);
    }

    @Override
    public double sample(long hashedSeed) {
        return negativeInverseLambda * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed));
    }

    public static KeyDispatchDataCodec<ExponentialSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<ExponentialSampler> getMapCodec() {
        return MAP_CODEC;
    }

    public MapCodec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
