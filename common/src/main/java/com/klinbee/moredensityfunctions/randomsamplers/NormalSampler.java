package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NormalSampler(double mean,
                            double stdDev)
        implements RandomSampler {

    private static final Codec<NormalSampler> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("mean").forGetter(NormalSampler::mean),
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("std_dev").forGetter(NormalSampler::stdDev)
            ).apply(instance, NormalSampler::create)
    );

    public static final AnonymousTypedCodec<NormalSampler> ANON_CODEC = new AnonymousTypedCodec<>("normal", CODEC);

    static {
        REGISTRY.register(ANON_CODEC);
    }

    public static NormalSampler create(double mean, double stdDev) {
        return new NormalSampler(mean, stdDev);
    }

    @Override
    public double sample(long hashedSeed) {
        return mean + stdDev * RandomSampler.sampleGaussian(hashedSeed);
    }

    @Override
    public double minValue() {
        return -Double.MAX_VALUE;
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
