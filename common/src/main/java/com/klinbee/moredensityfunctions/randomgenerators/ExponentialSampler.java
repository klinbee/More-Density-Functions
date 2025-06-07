package com.klinbee.moredensityfunctions.randomgenerators;

public record ExponentialSampler(double negativeInverseLambda) implements RandomSampler {

    static ExponentialSampler create(double lambda) {
        double negativeInverseLambda = -1.0D / lambda;
        return new ExponentialSampler(negativeInverseLambda);
    }

    @Override
    public double sample(long hashedSeed) {
        return negativeInverseLambda * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed));
    }
}
