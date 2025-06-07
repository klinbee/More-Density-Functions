package com.klinbee.moredensityfunctions.randomgenerators;

public interface RandomGenerator {

    static BetaGenerator buildBeta(double alpha, double beta) {
        return BetaGenerator.create(alpha, beta);
    }

    static BinomialGenerator buildBinomial(int numTrials, double probability) {
        return BinomialGenerator.create(numTrials, probability);
    }

    static ExponentialGenerator buildExponential(double lambda) {
        return ExponentialGenerator.create(lambda);
    }

    static GammaGenerator buildGamma(double shape) {
        return GammaGenerator.create(shape);
    }

    static GeometricGenerator buildGeometric(double probability) {
        return GeometricGenerator.create(probability);
    }

    static NormalGenerator buildNormal(double shape, double scale) {
        return NormalGenerator.create(shape, scale);
    }

    static PoissonGenerator buildPoisson(double lambda) {
        return PoissonGenerator.create(lambda);
    }

    static UniformGenerator buildUniform(double min, double max) {
        return UniformGenerator.create(min, max);
    }

    double getRandom(long hashedSeed);

}
