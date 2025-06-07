package com.klinbee.moredensityfunctions.randomgenerators;

import net.minecraft.util.Mth;

import static com.klinbee.moredensityfunctions.util.MDFUtil.getDouble;

public class GeometricGenerator implements RandomGenerator {

    private final double inverseLog1p;

    GeometricGenerator(double probability) {
        this.inverseLog1p = 1.0D / StrictMath.log(1.0D - probability);
    }

    static GeometricGenerator create(double lambda) {
        return new GeometricGenerator(lambda);
    }

    @Override
    public double getRandom(long hashedSeed) {
        return Mth.ceil(inverseLog1p * StrictMath.log(1.0D - getDouble(hashedSeed)));
    }
}
