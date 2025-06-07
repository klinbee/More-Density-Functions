package com.klinbee.moredensityfunctions.randomgenerators;

public sealed interface PoissonSampler extends RandomSampler {

    static PoissonSampler create(double lambda) {
        if (lambda < 30.0D) {
            double expNegativeLambda = StrictMath.exp(-lambda);
            return new Knuth(expNegativeLambda);
        }
        NormalSampler randNormal = RandomSampler.buildNormal(lambda, StrictMath.sqrt(lambda));
        return new Normal(randNormal);
    }

    record Knuth(double expNegativeLambda) implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            double p = 1.0D;
            int k = 0;

            do {
                k++;
                p *= RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
            } while (p > expNegativeLambda);

            return k - 1.0D;
        }
    }

    record Normal(NormalSampler randNormal) implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.round(randNormal.sample(hashedSeed)));
        }
    }
}
