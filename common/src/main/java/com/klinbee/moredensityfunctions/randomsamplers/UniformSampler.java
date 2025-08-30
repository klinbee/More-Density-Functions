package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record UniformSampler(double min,
                             double max,
                             double range)
        implements RandomSampler {

    private static final MapCodec<UniformSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min").forGetter(UniformSampler::min),
                    Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max").forGetter(UniformSampler::max)
            ).apply(instance, UniformSampler::create)
    );

    public static final TypedCodec<UniformSampler> TYPED_CODEC = new TypedCodec<>("uniform", KeyDispatchDataCodec.of(MAP_CODEC));

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

    public KeyDispatchDataCodec<? extends RandomSampler> codec() {
        return TYPED_CODEC.codec();
    }
}
