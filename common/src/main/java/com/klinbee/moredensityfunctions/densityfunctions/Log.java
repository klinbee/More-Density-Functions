package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Log(DensityFunction arg,
                  DensityFunction base)
        implements DensityFunction {

    private static final MapCodec<Log> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log::arg),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Log::base)
                    ).apply(instance, Log::new)
            );

    public static final TypedCodec<Log> TYPED_CODEC = new TypedCodec<>("log", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double arg, double base) {
        return MDFMath.log(arg, base);
    }

    private static BaseRange findBaseRange(double min, double max) {

        // Range is inside the (0, 1) set
        if ((min > 0.0D) && (min < 1.0D) && (max > 0.0D) && (max < 1.0D)) {
            return BaseRange.ZERO_TO_ONE_EXCLUSIVE;
        }

        // Range is inside the (1, +∞) set
        if (min > 1.0D) {
            return BaseRange.ONE_TO_INFINITY_EXCLUSIVE;
        }

        // Range is across sets
        return BaseRange.INVALID;
    }

    @Override
    public double compute(FunctionContext pos) {
        return eval(arg.compute(pos), base.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Log(
                        arg.mapAll(visitor),
                        base.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double asymptoteLocation = 0.0D;

        double baseMin = base.minValue();
        double baseMax = base.maxValue();

        return switch (findBaseRange(baseMin, baseMax)) {
            case ZERO_TO_ONE_EXCLUSIVE -> { // Inverted Log Logic
                // Since this function is decreasing, the upper bound will always be the minimum
                double argMax = arg.maxValue();

                // argMax in the set (0, 1) and (1, +∞) produce different logic for min, so doing this instead
                yield StrictMath.min(eval(argMax, baseMin), eval(argMax, baseMax));
            }
            case ONE_TO_INFINITY_EXCLUSIVE -> { // Normal Log Logic
                // Lower bound is above `asymptoteLocation`, `minValue()` must be `eval(argMin)`
                double argMin = arg.minValue();

                // argMin in the set (0, 1) and (1, +∞) produce different logic for min, so doing this instead
                if (argMin > asymptoteLocation) {
                    yield StrictMath.min(eval(argMin, baseMin), eval(argMin, baseMax));
                }

                // Upper bound is below `asymptoteLocation`, `minValue()` must be `NaN`
                double argMax = arg.maxValue();

                if (argMax < asymptoteLocation) {
                    yield Double.NaN;
                }

                // Range includes `asymptoteLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(asymptoteLocation)`
                yield Double.NEGATIVE_INFINITY;
            }
            case INVALID -> Double.NaN; // Cannot determine upper or lower bound
        };
    }

    @Override
    public double maxValue() {
        double asymptoteLocation = 0.0D;

        double baseMin = base.minValue();
        double baseMax = base.maxValue();

        return switch (findBaseRange(baseMin, baseMax)) {
            case ZERO_TO_ONE_EXCLUSIVE -> { // Inverted Log Logic
                // Lower bound is above `asymptoteLocation`, `maxValue()` must be `eval(argMin)`
                double argMin = arg.minValue();

                // argMin in the set (0, 1) and (1, +∞) produce different logic for max, so doing this instead
                if (argMin > asymptoteLocation) {
                    yield StrictMath.max(eval(argMin, baseMin), eval(argMin, baseMax));
                }

                // Upper bound is below `asymptoteLocation`, `maxValue()` must be `NaN`
                double argMax = arg.maxValue();

                if (argMax < asymptoteLocation) {
                    yield Double.NaN;
                }

                // Range includes `asymptoteLocation`, `maxValue()` cannot be guaranteed, but could be as high as `eval(asymptoteLocation)`
                yield Double.POSITIVE_INFINITY;
            }
            case ONE_TO_INFINITY_EXCLUSIVE -> { // Normal Log Logic
                // Since this function is increasing, the upper bound will always be the maximum
                double argMax = arg.maxValue();

                // argMax in the set (0, 1) and (1, +∞) produce different logic for max, so doing this instead
                yield StrictMath.min(eval(argMax, baseMin), eval(argMax, baseMax));
            }
            case INVALID -> Double.NaN; // Cannot determine upper or lower bound
        };
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }

    private enum BaseRange {
        ZERO_TO_ONE_EXCLUSIVE,
        ONE_TO_INFINITY_EXCLUSIVE,
        INVALID,
    }
}