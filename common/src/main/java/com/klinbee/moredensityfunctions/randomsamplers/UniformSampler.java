package com.klinbee.moredensityfunctions.randomsamplers;

public record UniformSampler(double min, double range) implements RandomSampler {

    static UniformSampler create(double min, double max) {
        double range = max - min;
        return new UniformSampler(min, range);
    }

    @Override
    public double sample(long hashedSeed) {
        return min + range * RandomSampler.sampleDouble(hashedSeed);
    }
}
