package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.randomgenerators.BinomialSampler;
import com.klinbee.moredensityfunctions.randomgenerators.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record BinomialDistribution(int numIterations, double probability,
                                   BinomialSampler rand) implements RandomDistribution {
    private static final MapCodec<BinomialDistribution> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.intRange(0, 1_000_000).fieldOf("num_iterations").forGetter(BinomialDistribution::numIterations),
            Codec.doubleRange(0.0D, 1.0D).fieldOf("probability").forGetter(BinomialDistribution::probability)
    ).apply(instance, (numTrials, probability) -> new BinomialDistribution(numTrials, probability, RandomSampler.buildBinomial(numTrials, probability))));
    public static final KeyDispatchDataCodec<BinomialDistribution> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public RandomSampler getRand() {
        return rand;
    }

    @Override
    public int numIterations() {
        return numIterations;
    }

    @Override
    public double probability() {
        return probability;
    }

    public double minValue() {
        return 0.0D;
    }

    public double maxValue() {
        return numIterations;
    }

    @Override
    public Codec<? extends RandomDistribution> codec() {
        return CODEC.codec();
    }

    public static MapCodec<BinomialDistribution> getMapCodec() {
        return MAP_CODEC;
    }
}
