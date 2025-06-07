package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.GeometricGenerator;
import com.klinbee.moredensityfunctions.randomgenerators.RandomGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record GeometricDistribution(double probability, GeometricGenerator rand) implements RandomDistribution {
    private static final MapCodec<GeometricDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, 1.0D).fieldOf("probability").forGetter(GeometricDistribution::probability)
    ).apply(instance, (probability) -> new GeometricDistribution(probability, RandomGenerator.buildGeometric(probability))));
    public static final KeyDispatchDataCodec<GeometricDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomGenerator getRand() {
        return rand;
    }

    @Override
    public double probability() {
        return probability;
    }

    public double minValue() {
        return 1.0D;
    }

    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<GeometricDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
