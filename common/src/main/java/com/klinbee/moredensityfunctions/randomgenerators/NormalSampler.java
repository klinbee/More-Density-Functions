package com.klinbee.moredensityfunctions.randomgenerators;

public record NormalSampler(double mean, double stdDev) implements RandomSampler {

    static NormalSampler create(double mean, double stdDev) {
        return new NormalSampler(mean, stdDev);
    }

    @Override
    public double sample(long hashedSeed) {
        return mean + stdDev * RandomSampler.sampleGaussian(hashedSeed);
    }
}
