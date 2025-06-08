package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomsamplers.BetaSampler;
import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record BetaDistribution(double alpha, double beta, BetaSampler rand) implements RandomDistribution {
    private static final MapCodec<BetaDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("alpha").forGetter(BetaDistribution::alpha),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("beta").forGetter(BetaDistribution::beta)
    ).apply(instance, (alpha, beta) -> new BetaDistribution(alpha, beta, RandomSampler.buildBeta(alpha, beta))));
    public static final KeyDispatchDataCodec<BetaDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomSampler getRand() {
        return rand;
    }

    @Override
    public double alpha() {
        return alpha;
    }

    @Override
    public double beta() {
        return beta;
    }

    public double minValue() {
        return 0.0D;
    }

    public double maxValue() {
        return 1.0D;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<BetaDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
