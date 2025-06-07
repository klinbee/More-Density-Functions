package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.PoissonGenerator;
import com.klinbee.moredensityfunctions.randomgenerators.RandomGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record PoissonDistribution(double lambda, PoissonGenerator rand) implements RandomDistribution {
    private static final MapCodec<PoissonDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(PoissonDistribution::lambda)
    ).apply(instance, (lambda) -> new PoissonDistribution(lambda, RandomGenerator.buildPoisson(lambda))));
    public static final KeyDispatchDataCodec<PoissonDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);


    @Override
    public double lambda() {
        return lambda;
    }

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

    public static MapCodec<PoissonDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
