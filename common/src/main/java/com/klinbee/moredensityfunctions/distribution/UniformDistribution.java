package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.RandomGenerator;
import com.klinbee.moredensityfunctions.randomgenerators.UniformGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;


public record UniformDistribution(double min, double max, UniformGenerator rand) implements RandomDistribution {
    private static final MapCodec<UniformDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min").forGetter(UniformDistribution::min),
            Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max").forGetter(UniformDistribution::max)
    ).apply(instance, (min, max) -> {
        if (min > max) {
            throw new IllegalArgumentException("Min must be less than max!");
        }
        return new UniformDistribution(min, max, RandomGenerator.buildUniform(min, max));
    }));
    public static final KeyDispatchDataCodec<UniformDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomGenerator getRand() {
        return rand;
    }

    public double minValue() {
        return min;
    }

    public double maxValue() {
        return max;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<UniformDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
