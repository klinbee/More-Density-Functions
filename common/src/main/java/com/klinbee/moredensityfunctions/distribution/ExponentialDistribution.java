package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomsamplers.ExponentialSampler;
import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record ExponentialDistribution(double lambda, ExponentialSampler sampler) implements RandomDistribution {
    private static final MapCodec<ExponentialDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(ExponentialDistribution::lambda)
    ).apply(instance, (lambda) -> new ExponentialDistribution(lambda, RandomSampler.buildExponential(lambda))));
    public static final KeyDispatchDataCodec<ExponentialDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomSampler getSampler() {
        return sampler;
    }

    public double minValue() {
        return 0.0D;
    }

    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<ExponentialDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
