package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record Euclidean() implements DistanceMetric {

    public static final MapCodec<Euclidean> CODEC = MapCodec.unit(new Euclidean());

    public static final AnonymousTypedCodec<Euclidean> ANON_CODEC = new AnonymousTypedCodec<>("euclidean", CODEC);

    @Override
    public double distance(double[] point1, double[] point2) {
        double sum = 0.0D;

        for (int i = 0; i < point1.length; i++) {
            sum += (point2[i] - point1[i]) * (point2[i] - point1[i]);
        }
        return StrictMath.sqrt(sum);
    }

    @Override
    public double minValue(double[] minAbsDiffs) {
        double sum = 0.0D;
        for (double diff : minAbsDiffs) {
            sum += diff * diff;
        }
        return StrictMath.sqrt(sum);
    }

    @Override
    public double maxValue(double[] maxAbsDiffs) {
        double sum = 0.0D;
        for (double diff : maxAbsDiffs) {
            sum += diff * diff;
        }
        return StrictMath.sqrt(sum);
    }

    @Override
    public AnonymousTypedCodec<? extends DistanceMetric> anonCodec() {
        return ANON_CODEC;
    }
}