package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record NormalSampler(double mean, double stdDev) implements RandomSampler {

    public static final MapCodec<NormalSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("mean").forGetter(NormalSampler::mean),
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("std_dev").forGetter(NormalSampler::stdDev)
            ).apply(instance, NormalSampler::create)
    );

    static NormalSampler create(double mean, double stdDev) {
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

    public static final KeyDispatchDataCodec<NormalSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static MapCodec<NormalSampler> getMapCodec() {
        return MAP_CODEC;
    }

    public Codec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
