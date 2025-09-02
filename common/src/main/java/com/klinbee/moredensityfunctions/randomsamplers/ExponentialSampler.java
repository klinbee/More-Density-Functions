package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ExponentialSampler(double lambda,
                                 double negativeInverseLambda)
        implements RandomSampler {

    private static final MapCodec<ExponentialSampler> CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(ExponentialSampler::lambda)
                    ).apply(instance, ExponentialSampler::create)
            );

    public static final AnonymousTypedCodec<ExponentialSampler> ANON_CODEC = new AnonymousTypedCodec<>("exponential", CODEC);

    public static ExponentialSampler create(double lambda) {
        double negativeInverseLambda = -1.0D / lambda;
        return new ExponentialSampler(lambda, negativeInverseLambda);
    }

    @Override
    public double sample(long hashedSeed) {
        return negativeInverseLambda * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed));
    }

    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public AnonymousTypedCodec<? extends RandomSampler> anonCodec() {
        return ANON_CODEC;
    }
}
