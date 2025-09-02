package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

/**
 * The Contract for this record is that it should never be used for arrays of size > 1
 */
public record Linear() implements DistanceMetric {

    @Override
    public double distance(double[] point1, double[] point2) {
        return StrictMath.abs(point2[0] - point1[0]);
    }

    @Override
    public double minValue(double[] minAbsDiffs) {
        return minAbsDiffs[0];
    }

    @Override
    public double maxValue(double[] maxAbsDiffs) {
        return maxAbsDiffs[0];
    }

    @Override
    public AnonymousTypedCodec<? extends DistanceMetric> anonCodec() {
        throw new UnsupportedOperationException("More Density Functions: attempting to make linear distance metric via CODEC");
    }
}