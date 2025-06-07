package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.getDouble;

public class ExponentialGenerator implements RandomGenerator {

    private final double negativeInverseLambda;

    ExponentialGenerator(double lambda) {
        this.negativeInverseLambda = -1.0D / lambda;
    }

    public static ExponentialGenerator create(double lambda) {
        return new ExponentialGenerator(lambda);
    }

    @Override
    public double getRandom(long hashedSeed) {
        return negativeInverseLambda * StrictMath.log(1.0D - getDouble(hashedSeed));
    }
}
