package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.getGaussian;

public class NormalGenerator implements RandomGenerator {

    private final double mean;
    private final double stdDev;

    NormalGenerator(double mean, double stdDev) {
        this.mean = mean;
        this.stdDev = stdDev;
    }

    static NormalGenerator create(double mean, double stdDev) {
        return new NormalGenerator(mean, stdDev);
    }

    @Override
    public double getRandom(long hashedSeed) {
        return mean + stdDev * getGaussian(hashedSeed);
    }
}
