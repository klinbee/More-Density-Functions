package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public record GeometricSampler(double probability,
                               double inverseLog1p)
        implements RandomSampler {

    private static final Codec<GeometricSampler> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, 1.0D).fieldOf("probability").forGetter(GeometricSampler::probability)
            ).apply(instance, GeometricSampler::create)
    );

    public static final AnonymousTypedCodec<GeometricSampler> ANON_CODEC = new AnonymousTypedCodec<>("geometric", CODEC);

    static {
        REGISTRY.register(ANON_CODEC);
    }

    public static GeometricSampler create(double probability) {
        double inverseLog1p = 1.0D / StrictMath.log(1.0D - probability);
        return new GeometricSampler(probability, inverseLog1p);
    }

    @Override
    public double sample(long hashedSeed) {
        return Mth.ceil(inverseLog1p * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed)));
    }

    @Override
    public double minValue() {
        return 1.0D;
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
