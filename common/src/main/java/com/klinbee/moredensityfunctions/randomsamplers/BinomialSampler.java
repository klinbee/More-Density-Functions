package com.klinbee.moredensityfunctions.randomsamplers;

public sealed interface BinomialSampler extends RandomSampler
        permits BinomialSampler.Direct, BinomialSampler.Poisson,
        BinomialSampler.Normal, BinomialSampler.Exponential {

    static BinomialSampler create(int trials, double probability) {

        if (trials < 30) {
            return new BinomialSampler.Direct(trials, probability);
        }
        double mean = trials * probability;

        if (mean < 10.0D) {
            PoissonSampler randPoisson = RandomSampler.buildPoisson(mean);
            return new BinomialSampler.Poisson(randPoisson);
        }

        double oppositeMean = trials * (1.0D - probability);

        if (mean >= 30.0D && oppositeMean >= 30.0D) {
            NormalSampler randNormal = NormalSampler.create(mean, StrictMath.sqrt(mean * (1.0D - probability)));
            return new BinomialSampler.Normal(trials, randNormal);
        }

        ExponentialSampler randExponential = ExponentialSampler.create(-StrictMath.log(1.0D - probability));
        return new BinomialSampler.Exponential(trials, randExponential);
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

    record Poisson(PoissonSampler randPoisson) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return randPoisson.sample(hashedSeed);
        }
    }

    record Normal(int trials, NormalSampler randNormal) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.min(trials, StrictMath.round(randNormal.sample(hashedSeed))));
        }
    }

    record Exponential(int trials, ExponentialSampler randExponential) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            int x = 0;
            double sum = 0.0D;
            while (sum <= trials) {
                sum += randExponential.sample(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                x++;
            }
            return x - 1;
        }
    }
}
