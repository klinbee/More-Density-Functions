package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;

public record Manhattan() implements DistanceMetric {

    public static final Codec<Manhattan> CODEC = Codec.unit(new Manhattan());

    public static final AnonymousTypedCodec<Manhattan> ANON_CODEC = new AnonymousTypedCodec<>("manhattan", CODEC);

    @Override
    public double distance(double[] point1, double[] point2) {
        double sum = 0.0D;

        for (int i = 0; i < point1.length; i++) {
            sum += StrictMath.abs(point2[i] - point1[i]);
        }
        return sum;
    }

    @Override
    public double minValue(double[] minAbsDiffs) {
        double sum = 0;
        for (double diff : minAbsDiffs) {
            sum += diff;
        }
        return sum;
    }

    @Override
    public double maxValue(double[] maxAbsDiffs) {
        double sum = 0;
        for (double diff : maxAbsDiffs) {
            sum += diff;
        }
        return sum;
    }

    @Override
    public AnonymousTypedCodec<? extends DistanceMetric> anonCodec() {
        return ANON_CODEC;
    }
}