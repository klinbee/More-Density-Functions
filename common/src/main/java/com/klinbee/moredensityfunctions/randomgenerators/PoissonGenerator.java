package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.getDouble;
import static com.klinbee.moredensityfunctions.util.MDFUtil.mix;

public abstract class PoissonGenerator implements RandomGenerator {

    protected final double lambda;

    private PoissonGenerator(double lambda) {
        this.lambda = lambda;
    }

    static PoissonGenerator create(double lambda) {
        if (lambda < 30.0D) {
            return new Knuth(lambda);
        }
        return new Normal(lambda);
    }

    static class Knuth extends PoissonGenerator {
        private final double eNegativeLambda;

        Knuth(double lambda) {
            super(lambda);
            eNegativeLambda = StrictMath.exp(-lambda);

        }

        @Override
        public double getRandom(long hashedSeed) {
            double p = 1.0D;
            int k = 0;

            do {
                k++;
                p *= getDouble(hashedSeed);
                hashedSeed = mix(hashedSeed);
            } while (p > eNegativeLambda);

            return k - 1.0D;
        }
    }

    static class Normal extends PoissonGenerator {
        private final NormalGenerator randNormal;

        Normal(double lambda) {
            super(lambda);
            this.randNormal = RandomGenerator.buildNormal(lambda, StrictMath.sqrt(lambda));
        }

        @Override
        public double getRandom(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.round(randNormal.getRandom(hashedSeed)));
        }
    }
}
