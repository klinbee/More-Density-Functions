package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;

/**
 * The Contract for this record is that it will never be used for arrays of size > 1
 */
public record Linear() implements DistanceMetric {

    public static final Codec<Linear> CODEC = Codec.unit(new Linear());

    public static final AnonymousTypedCodec<Linear> ANON_CODEC = new AnonymousTypedCodec<>("linear", CODEC);

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
        return ANON_CODEC;
    }
}