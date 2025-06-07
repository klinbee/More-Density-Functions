package com.klinbee.moredensityfunctions.randomgenerators;

public sealed interface BinomialSampler extends RandomSampler
        permits BinomialSampler.Direct, BinomialSampler.Poisson,
        BinomialSampler.Normal, BinomialSampler.Exponential {

    static BinomialSampler create(int numTrials, double probability) {

        if (numTrials < 30) {
            return new BinomialSampler.Direct(numTrials, probability);
        }
        double mean = numTrials * probability;

        if (mean < 10.0D) {
            PoissonSampler randPoisson = RandomSampler.buildPoisson(mean);
            return new BinomialSampler.Poisson(randPoisson);
        }

        double oppositeMean = numTrials * (1.0D - probability);

        if (mean >= 30.0D && oppositeMean >= 30.0D) {
            NormalSampler randNormal = NormalSampler.create(mean, StrictMath.sqrt(mean * (1.0D - probability)));
            return new BinomialSampler.Normal(numTrials, randNormal);
        }

        ExponentialSampler randExponential = ExponentialSampler.create(-StrictMath.log(1.0D - probability));
        return new BinomialSampler.Exponential(numTrials, randExponential);
    }

    record Direct(int numTrials, double probability) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            int successes = 0;
            for (int i = 0; i < numTrials; i++) {
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

    record Normal(int numTrials, NormalSampler randNormal) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.min(numTrials, StrictMath.round(randNormal.sample(hashedSeed))));
        }
    }

    record Exponential(int numTrials, ExponentialSampler randExponential) implements BinomialSampler {
        @Override
        public double sample(long hashedSeed) {
            int x = 0;
            double sum = 0.0D;
            while (sum <= numTrials) {
                sum += randExponential.sample(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                x++;
            }
            return x - 1;
        }
    }
}
