package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record PoissonDistribution(double lambda) implements RandomDistribution {
    private static final MapCodec<PoissonDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(PoissonDistribution::lambda)
    ).apply(instance, (PoissonDistribution::new)));
    public static final KeyDispatchDataCodec<PoissonDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double getRandom(long hashedSeed) {
        return MDFUtil.getPoisson(lambda, hashedSeed);
    }

    @Override
    public double lambda() {
        return lambda;
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

    public static MapCodec<PoissonDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
