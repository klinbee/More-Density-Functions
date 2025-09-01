package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;

public record Chebyshev() implements DistanceMetric {

    public static final Codec<Chebyshev> CODEC = Codec.unit(new Chebyshev());

    public static final AnonymousTypedCodec<Chebyshev> ANON_CODEC = new AnonymousTypedCodec<>("chebyshev", CODEC);

    static {
        REGISTRY.register(ANON_CODEC);
    }

    @Override
    public double distance(double[] point1, double[] point2) {
        double maxDistance = 0.0D;

        for (int i = 0; i < point1.length; i++) {
            double distance = StrictMath.abs(point2[i] - point1[i]);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        return maxDistance;
    }

    @Override
    public double minValue(double[] minAbsDiffs) {
        double min = Double.POSITIVE_INFINITY;
        for (double diff : minAbsDiffs) {
            if (diff < min) min = diff;
        }
        return min == Double.POSITIVE_INFINITY ? 0 : min;
    }

    @Override
    public double maxValue(double[] maxAbsDiffs) {
        double max = 0;
        for (double diff : maxAbsDiffs) {
            if (diff > max) max = diff;
        }
        return max;
    }

    @Override
    public AnonymousTypedCodec<? extends DistanceMetric> anonCodec() {
        return ANON_CODEC;
    }
}