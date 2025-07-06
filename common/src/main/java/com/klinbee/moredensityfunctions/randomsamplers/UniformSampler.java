package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record UniformSampler(double min, double max, double range) implements RandomSampler {

    public static final MapCodec<UniformSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min").forGetter(UniformSampler::min),
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max").forGetter(UniformSampler::max)
            ).apply(instance, (min, max) -> {
                if (min > max) {
                    throw new IllegalArgumentException("Min must be less than max! min: " + min + " max: " + max);
                }
                return UniformSampler.create(min, max);
            }));


    public double minValue() {
        return min;
    }

    public double maxValue() {
        return max;
    }


    static UniformSampler create(double min, double max) {
        double range = max - min;
        return new UniformSampler(min, max, range);
    }

    @Override
    public double sample(long hashedSeed) {
        return min + range * RandomSampler.sampleDouble(hashedSeed);
    }

    public static final KeyDispatchDataCodec<UniformSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<UniformSampler> getMapCodec() {
        return MAP_CODEC;
    }

    public MapCodec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
