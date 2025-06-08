package com.klinbee.moredensityfunctions.randomsamplers;

import net.minecraft.util.Mth;

public record GeometricSampler(double inverseLog1p) implements RandomSampler {

    static GeometricSampler create(double probability) {
        double inverseLog1p = 1.0D / StrictMath.log(1.0D - probability);
        return new GeometricSampler(inverseLog1p);
    }

    @Override
    public double sample(long hashedSeed) {
        return Mth.ceil(inverseLog1p * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed)));
    }
}
