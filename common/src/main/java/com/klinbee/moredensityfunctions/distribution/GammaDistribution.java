package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.GammaGenerator;
import com.klinbee.moredensityfunctions.randomgenerators.RandomGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record GammaDistribution(double shape, double scale,
                                GammaGenerator rand) implements RandomDistribution {
    private static final MapCodec<GammaDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("shape").forGetter(GammaDistribution::shape),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("scale").forGetter(GammaDistribution::scale)
    ).apply(instance, (shape, scale) -> new GammaDistribution(shape, scale, RandomGenerator.buildGamma(shape))));
    public static final KeyDispatchDataCodec<GammaDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomGenerator getRand() {
        return rand;
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

    public static MapCodec<GammaDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
