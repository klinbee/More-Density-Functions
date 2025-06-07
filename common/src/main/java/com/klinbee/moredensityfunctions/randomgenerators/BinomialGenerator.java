package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.*;

public abstract class BinomialGenerator implements RandomGenerator {

    protected final int numTrials;
    protected final double probability;
    protected final double mean;

    BinomialGenerator(int numTrials, double probability) {
        this.numTrials = numTrials;
        this.probability = probability;
        this.mean = numTrials * probability;
    }

    static BinomialGenerator create(int numTrials, double probability) {
        if (numTrials < 30) {
            return new BinomialGenerator.Direct(numTrials, probability);
        }
        double mean = numTrials * probability;

        if (mean < 10.0D) {
            return new BinomialGenerator.Poisson(numTrials, probability);
        }
        double qrobability = 1.0D - probability;
        double oppositeMean = numTrials * qrobability;
        if (mean >= 30.0D && oppositeMean >= 30.0D) {
            return new BinomialGenerator.Normal(numTrials, probability);
        }
        return new BinomialGenerator.Exponential(numTrials, probability);
    }

    static class Direct extends BinomialGenerator {

        Direct(int numTrials, double probability) {
            super(numTrials, probability);
        }

        @Override
        public double getRandom(long hashedSeed) {
            int successes = 0;
            for (int i = 0; i < numTrials; i++) {
                if (getDouble(hashedSeed) < probability) {
                    successes++;
                }
                hashedSeed = mix(hashedSeed);
            }
            return successes;
        }
    }

    static class Poisson extends BinomialGenerator {
        private final PoissonGenerator randPoisson;

        Poisson(int numTrials, double probability) {
            super(numTrials, probability);
            this.randPoisson = PoissonGenerator.create(mean);
        }

        @Override
        public double getRandom(long hashedSeed) {
            return randPoisson.getRandom(hashedSeed);
        }
    }

    static class Normal extends BinomialGenerator {
        private final NormalGenerator randNormal;

        Normal(int numTrials, double probability) {
            super(numTrials, probability);
            this.randNormal = NormalGenerator.create(mean, StrictMath.sqrt(mean * (1.0D - probability)));
        }

        @Override
        public double getRandom(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.min(numTrials, StrictMath.round(randNormal.getRandom(hashedSeed))));
        }
    }

    static class Exponential extends BinomialGenerator {
        private final ExponentialGenerator randExponential;

        Exponential(int numTrials, double probability) {
            super(numTrials, probability);
            this.randExponential = ExponentialGenerator.create(-StrictMath.log(1.0D - probability));
        }

        @Override
        public double getRandom(long hashedSeed) {
            int x = 0;
            double sum = 0.0D;
            while (sum <= numTrials) {
                sum += randExponential.getRandom(hashedSeed);
                hashedSeed = mix(hashedSeed);
                x++;
            }
            return x - 1;
        }
    }
}
