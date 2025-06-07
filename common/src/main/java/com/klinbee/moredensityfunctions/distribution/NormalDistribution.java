package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.NormalGenerator;
import com.klinbee.moredensityfunctions.randomgenerators.RandomGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record NormalDistribution(double mean, double stdDev, NormalGenerator rand) implements RandomDistribution {
    private static final MapCodec<NormalDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("mean").forGetter(NormalDistribution::mean),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("std_dev").forGetter(NormalDistribution::stdDev)
    ).apply(instance, (mean, stdDev) -> new NormalDistribution(mean, stdDev, RandomGenerator.buildNormal(mean, stdDev))));
    public static final KeyDispatchDataCodec<NormalDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomGenerator getRand() {
        return rand;
    }

    public double minValue() {
        return -Double.MAX_VALUE;
    }

    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<NormalDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
