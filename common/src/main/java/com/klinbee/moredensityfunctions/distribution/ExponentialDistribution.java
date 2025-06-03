package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record ExponentialDistribution(double lambda) implements RandomDistribution {
    private static final MapCodec<ExponentialDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(ExponentialDistribution::lambda)
    ).apply(instance, (ExponentialDistribution::new)));
    public static final KeyDispatchDataCodec<ExponentialDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double getRandom(long hashedSeed) {
        return MDFUtil.getExponential(lambda, hashedSeed);
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

    public static MapCodec<ExponentialDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
