package com.klinbee.moredensityfunctions.distancemetrics;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodecRegistry;
import com.mojang.serialization.Codec;

public interface DistanceMetric {

    AnonymousTypedCodecRegistry<DistanceMetric> REGISTRY =
            new AnonymousTypedCodecRegistry<>("DistanceMetric");

    Codec<DistanceMetric> CODEC = REGISTRY.createDispatchCodec(
            distanceMetric -> distanceMetric.anonCodec().type()
    );

    double distance(double[] point1, double[] point2);

    double minValue(double[] minAbsDiffs);

    double maxValue(double[] maxAbsDiffs);

    AnonymousTypedCodec<? extends DistanceMetric> anonCodec();
}
