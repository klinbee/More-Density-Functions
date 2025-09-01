package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record UniformSampler(double min,
                             double max,
                             double range)
        implements RandomSampler {

    private static final Codec<UniformSampler> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min").forGetter(UniformSampler::min),
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max").forGetter(UniformSampler::max)
            ).apply(instance, UniformSampler::create)
    );

    public static final AnonymousTypedCodec<UniformSampler> ANON_CODEC = new AnonymousTypedCodec<>("uniform", CODEC);

    static {
        REGISTRY.register(ANON_CODEC);
    }

    public static UniformSampler create(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must be less than max! min: " + min + " max: " + max);
        }
        double range = max - min;
        return new UniformSampler(min, max, range);
    }

    @Override
    public double sample(long hashedSeed) {
        return min + range * RandomSampler.sampleDouble(hashedSeed);
    }

    public double minValue() {
        return min;
    }

    public double maxValue() {
        return max;
    }

    @Override
    public AnonymousTypedCodec<? extends RandomSampler> anonCodec() {
        return ANON_CODEC;
    }
}
