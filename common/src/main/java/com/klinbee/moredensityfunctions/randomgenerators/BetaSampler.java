package com.klinbee.moredensityfunctions.randomgenerators;

public record BetaSampler(GammaSampler alphaGen, GammaSampler betaGen) implements RandomSampler {

    static BetaSampler create(double alpha, double beta) {
        return new BetaSampler(RandomSampler.buildGamma(alpha), RandomSampler.buildGamma(beta));
    }

    @Override
    public double sample(long hashedSeed) {
        double x = alphaGen.sample(hashedSeed);
        double y = betaGen.sample(RandomSampler.mix(hashedSeed + 1L));
        return x / (x + y);
    }
}
