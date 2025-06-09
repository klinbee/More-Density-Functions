package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public sealed interface BinomialSampler extends RandomSampler
        permits BinomialSampler.Direct, BinomialSampler.Poisson,
        BinomialSampler.Normal, BinomialSampler.Exponential {

    MapCodec<BinomialSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.intRange(0, 1_000_000).fieldOf("trials").forGetter(BinomialSampler::trials),
                    Codec.doubleRange(0.0D, 1.0D).fieldOf("probability").forGetter(BinomialSampler::probability)
            ).apply(instance, BinomialSampler::create)
    );

    int trials();
    double probability();

    @Override
    default double minValue() {
        return 0.0D;
    }

    @Override
    default double maxValue() {
        return Double.MAX_VALUE;
    }

    static BinomialSampler create(int trials, double probability) {

        if (trials < 30) {
            return new BinomialSampler.Direct(trials, probability);
        }
        double mean = trials * probability;

        if (mean < 10.0D) {
            PoissonSampler randPoisson = RandomSampler.buildPoisson(mean);
            return new BinomialSampler.Poisson(trials, probability, randPoisson);
        }

        double oppositeMean = trials * (1.0D - probability);

        if (mean >= 30.0D && oppositeMean >= 30.0D) {
            NormalSampler normalSampler = NormalSampler.create(mean, StrictMath.sqrt(mean * (1.0D - probability)));
            return new BinomialSampler.Normal(trials, probability, normalSampler);
        }

        ExponentialSampler exponentialSampler = ExponentialSampler.create(-StrictMath.log(1.0D - probability));
        return new BinomialSampler.Exponential(trials, probability, exponentialSampler);
    }

    record Direct(int trials, double probability) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            int successes = 0;
            for (int i = 0; i < trials; i++) {
                if (RandomSampler.sampleDouble(hashedSeed) < probability) {
                    successes++;
                }
                hashedSeed = RandomSampler.mix(hashedSeed);
            }
            return successes;
        }
    }

    record Poisson(int trials, double probability, PoissonSampler randPoisson) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return randPoisson.sample(hashedSeed);
        }
    }

    record Normal(int trials, double probability, NormalSampler normalSampler) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.min(trials, StrictMath.round(normalSampler.sample(hashedSeed))));
        }
    }

    record Exponential(int trials, double probability,
                       ExponentialSampler exponentialSampler) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            int x = 0;
            double sum = 0.0D;
            while (sum <= trials) {
                sum += exponentialSampler.sample(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                x++;
            }
            return x - 1;
        }
    }

    KeyDispatchDataCodec<BinomialSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<BinomialSampler> getMapCodec() {
        return MAP_CODEC;
    }

    default Codec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
