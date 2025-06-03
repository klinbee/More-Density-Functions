package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record BetaDistribution(double alpha, double beta) implements RandomDistribution {
    private static final MapCodec<BetaDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("alpha").forGetter(BetaDistribution::alpha),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("beta").forGetter(BetaDistribution::beta)
    ).apply(instance, (BetaDistribution::new)));
    public static final KeyDispatchDataCodec<BetaDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double getRandom(long hashedSeed) {
        return MDFUtil.getBeta(alpha, beta, hashedSeed);
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
        return Double.MAX_VALUE;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<BetaDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
