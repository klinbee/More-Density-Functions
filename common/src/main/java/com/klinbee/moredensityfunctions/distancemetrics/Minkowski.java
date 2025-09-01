package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Minkowski(int p) implements DistanceMetric {

    public static final Codec<DistanceMetric> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("p").forGetter(m -> ((Minkowski) m).p)
            ).apply(instance, Minkowski::create)
    );

    public static final AnonymousTypedCodec<DistanceMetric> ANON_CODEC = new AnonymousTypedCodec<>("minkowski", CODEC);

    static {
        REGISTRY.register(ANON_CODEC);
    }

    private static DistanceMetric create(int p) {
        return switch (p) {
            case 0 -> new Manhattan();
            case 1 -> new Euclidean();
            default -> new Minkowski(p);
        };
    }

    @Override
    public double distance(double[] point1, double[] point2) {
        double sum = 0.0D;

        for (int i = 0; i < point1.length; i++) {
            sum += StrictMath.pow(StrictMath.abs(point2[i] - point1[i]), p);
        }
        return StrictMath.pow(sum, 1.0D / p);
    }

    @Override
    public double minValue(double[] minAbsDiffs) {
        double sum = 0;
        for (double diff : minAbsDiffs) {
            sum += StrictMath.pow(diff, p);
        }
        return StrictMath.pow(sum, 1.0D / p);
    }

    @Override
    public double maxValue(double[] maxAbsDiffs) {
        double sum = 0;
        for (double diff : maxAbsDiffs) {
            sum += StrictMath.pow(diff, p);
        }
        return StrictMath.pow(sum, 1.0D / p);
    }

    @Override
    public AnonymousTypedCodec<? extends DistanceMetric> anonCodec() {
        return ANON_CODEC;
    }
}