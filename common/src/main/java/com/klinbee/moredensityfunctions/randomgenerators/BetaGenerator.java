package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.mix;

public class BetaGenerator implements RandomGenerator {
    private final GammaGenerator alphaGen;
    private final GammaGenerator betaGen;

    BetaGenerator(double alpha, double beta) {
        this.alphaGen = RandomGenerator.buildGamma(alpha);
        this.betaGen = RandomGenerator.buildGamma(beta);
    }

    static BetaGenerator create(double alpha, double beta) {
        return new BetaGenerator(alpha, beta);
    }

    @Override
    public double getRandom(long hashedSeed) {
        double x = alphaGen.getRandom(hashedSeed);
        double y = betaGen.getRandom(mix(hashedSeed + 1L));
        return x / (x + y);
    }
}
