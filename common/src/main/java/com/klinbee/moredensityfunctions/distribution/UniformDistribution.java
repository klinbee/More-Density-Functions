package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record UniformDistribution(double min, double max) implements RandomDistribution {
    private static final MapCodec<UniformDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(-Double.MAX_VALUE,Double.MAX_VALUE).fieldOf("min").forGetter(UniformDistribution::min),
            Codec.doubleRange(-Double.MAX_VALUE,Double.MAX_VALUE).fieldOf("max").forGetter(UniformDistribution::max)
    ).apply(instance, (UniformDistribution::new)));
    public static final KeyDispatchDataCodec<UniformDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double getRandom(long hashedSeed) {
        return MDFUtil.getUniform(min, max, hashedSeed);
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
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

    public static MapCodec<UniformDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
