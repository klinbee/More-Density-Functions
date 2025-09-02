package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public sealed interface BinomialSampler
        extends RandomSampler {

    Codec<BinomialSampler> CODEC =
            RecordCodecBuilder.create((instance) ->
                    instance.group(
                            Codec.intRange(0, 1_000_000).fieldOf("trials").forGetter(BinomialSampler::trials),
                            Codec.doubleRange(0.0D, 1.0D).fieldOf("probability").forGetter(BinomialSampler::probability)
                    ).apply(instance, BinomialSampler::create)
            );

    AnonymousTypedCodec<BinomialSampler> ANON_CODEC = new AnonymousTypedCodec<>("binomial", CODEC);

    static BinomialSampler create(int trials, double probability) {

        if (trials < 30) {
            return new Direct(trials, probability);
        }
        double mean = trials * probability;

        if (mean < 10.0D) {
            PoissonSampler randPoisson = PoissonSampler.create(mean);
            return new Poisson(trials, probability, randPoisson);
        }

        double oppositeMean = trials * (1.0D - probability);

        if (mean >= 30.0D && oppositeMean >= 30.0D) {
            NormalSampler normalSampler = NormalSampler.create(mean, StrictMath.sqrt(mean * (1.0D - probability)));
            return new Normal(trials, probability, normalSampler);
        }

        ExponentialSampler exponentialSampler = ExponentialSampler.create(-StrictMath.log(1.0D - probability));
        return new Exponential(trials, probability, exponentialSampler);
    }

    int trials();

    double probability();

    record Direct(int trials,
                  double probability)
            implements BinomialSampler {
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

    record Poisson(int trials,
                   double probability,
                   PoissonSampler randPoisson)
            implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return randPoisson.sample(hashedSeed);
        }
    }

    record Normal(int trials,
                  double probability,
                  NormalSampler normalSampler)
            implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.min(trials, StrictMath.round(normalSampler.sample(hashedSeed))));
        }
    }

    record Exponential(int trials,
                       double probability,
                       ExponentialSampler exponentialSampler)
            implements BinomialSampler {
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

    @Override
    default double minValue() {
        return 0.0D;
    }

    @Override
    default double maxValue() {
        return trials();
    }

    @Override
    default AnonymousTypedCodec<? extends RandomSampler> anonCodec() {
        return ANON_CODEC;
    }
}
