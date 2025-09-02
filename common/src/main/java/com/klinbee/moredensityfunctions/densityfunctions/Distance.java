package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.distancemetrics.DistanceMetric;
import com.klinbee.moredensityfunctions.distancemetrics.Linear;
import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Arrays;

import static com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants.DENSITY_FUNCTION_ARRAY_CODEC;

public record Distance(DistanceMetric distanceMetric,
                       DensityFunction[] point1,
                       DensityFunction[] point2)
        implements DensityFunction {

    private static final MapCodec<Distance> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DistanceMetric.CODEC.fieldOf("distance_metric").forGetter(Distance::distanceMetric),
                            DENSITY_FUNCTION_ARRAY_CODEC.fieldOf("point1").forGetter(Distance::point1),
                            DENSITY_FUNCTION_ARRAY_CODEC.fieldOf("point2").forGetter(Distance::point2)
                    ).apply(instance, Distance::create)
            );

    public static final TypedCodec<Distance> TYPED_CODEC = new TypedCodec<>("distance", KeyDispatchDataCodec.of(MAP_CODEC));

    private static Distance create(DistanceMetric distanceMetric,
                                   DensityFunction[] point1,
                                   DensityFunction[] point2) {
        // Both points empty
        if (point1.length == 0 && point2.length == 0) {
            throw new IllegalArgumentException("Distance points' dimension must be above 0!");
        }

        // Other point is empty (auto-complete N-Dimensional origin)
        if (point1.length == 0) {
            point1 = new DensityFunction[point2.length];
            Arrays.fill(point1, DensityFunctions.constant(0));
        }

        if (point2.length == 0) {
            point2 = new DensityFunction[point1.length];
            Arrays.fill(point2, DensityFunctions.constant(0));
        }

        // Point dimension mismatch
        if (point1.length != point2.length) {
            throw new IllegalArgumentException("Distance points must have the same dimension!");
        }

        // All 1-Dimensional distances are linear
        if (point1.length == 1) {
            return new Distance(new Linear(), point1, point2);
        }
        return new Distance(distanceMetric, point1, point2);
    }

    @Override
    public double compute(FunctionContext pos) {
        double[] values1 = new double[point1.length];
        double[] values2 = new double[point2.length];
        for (int i = 0; i < point1.length; i++) {
            values1[i] = point1[i].compute(pos);
            values2[i] = point2[i].compute(pos);
        }
        return distanceMetric.distance(values1, values2);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {

        for (int i = 0; i < point1.length; i++) {
            point1[i].mapAll(visitor);
            point2[i].mapAll(visitor);
        }

        return visitor.apply(
                new Distance(
                        distanceMetric,
                        point1,
                        point2
                )
        );
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    private double[] minAbsDiffs() {
        double[] minAbsDiffs = new double[point1.length];

        for (int i = 0; i < point1.length; i++) {
            double p1Min = point1[i].minValue();
            double p1Max = point1[i].maxValue();
            double p2Min = point2[i].minValue();
            double p2Max = point2[i].maxValue();

            double diffMin = p2Min - p1Max;
            double diffMax = p2Max - p1Min;

            // For absolute difference, we need to consider if 0 is in the range
            if (diffMin <= 0 && diffMax >= 0) {
                // Range crosses zero, so minimum absolute difference is 0
                minAbsDiffs[i] = 0;
            } else {
                // Range doesn't cross zero, minimum is the smaller absolute value
                minAbsDiffs[i] = StrictMath.min(StrictMath.abs(diffMin), StrictMath.abs(diffMax));
            }
        }

        return minAbsDiffs;
    }

    private double[] maxAbsDiffs() {
        double[] maxAbsDiffs = new double[point1.length];

        for (int i = 0; i < point1.length; i++) {
            double p1Min = point1[i].minValue();
            double p1Max = point1[i].maxValue();
            double p2Min = point2[i].minValue();
            double p2Max = point2[i].maxValue();

            double diffMin = p2Min - p1Max;
            double diffMax = p2Max - p1Min;

            // Maximum absolute difference is always the larger absolute value
            maxAbsDiffs[i] = StrictMath.max(StrictMath.abs(diffMin), StrictMath.abs(diffMax));
        }

        return maxAbsDiffs;
    }

    @Override
    public double minValue() {
        return distanceMetric.minValue(minAbsDiffs());
    }

    @Override
    public double maxValue() {
        return distanceMetric.maxValue(maxAbsDiffs());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
