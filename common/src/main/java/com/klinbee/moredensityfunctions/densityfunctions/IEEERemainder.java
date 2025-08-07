package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record IEEERemainder(DensityFunction numerator,
                            DensityFunction denominator,
                            DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<IEEERemainder> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(IEEERemainder::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(IEEERemainder::denominator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(IEEERemainder::errorArg)
                    ).apply(instance, IEEERemainder::new)
            );

    public static final KeyDispatchDataCodec<IEEERemainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "ieee_rem";

    @Override
    public double compute(FunctionContext pos) {
        double numeratorValue = numerator.compute(pos);
        double denominatorValue = denominator.compute(pos);

        if (denominatorValue == 0.0D) {
            return errorArg.compute(pos);
        }

        return StrictMath.IEEEremainder(numeratorValue, denominatorValue);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new IEEERemainder(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor),
                        errorArg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double errorMin = errorArg.minValue();

        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();

        // Most negative possible: -|y|/2 where |y| is maximized

        double largestMagnitude = Math.max(Math.abs(denomMin), Math.abs(denomMax));

        // Conservative: assume both error and mathematical minimum are possible
        return Math.min(errorMin, -largestMagnitude / 2.0D);
    }

    @Override
    public double maxValue() {
        double errorMax = errorArg.maxValue();

        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();

        // Most positive possible: |y|/2 where |y| is maximized
        double largestMagnitude = Math.max(Math.abs(denomMin), Math.abs(denomMax));

        // Conservative: assume both error and mathematical maximum are possible
        return Math.max(errorMax, largestMagnitude / 2.0D);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
