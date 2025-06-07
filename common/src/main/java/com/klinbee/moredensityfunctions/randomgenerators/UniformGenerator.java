package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.getDouble;

public class UniformGenerator implements RandomGenerator {

    private final double min;
    private final double range;

    UniformGenerator(double min, double max) {
        this.min = min;
        this.range = max - min;
    }

    static UniformGenerator create(double min, double max) {
        return new UniformGenerator(min, max);
    }

    @Override
    public double getRandom(long hashedSeed) {
        return min + range * getDouble(hashedSeed);
    }
}
