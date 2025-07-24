package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Remainder(DensityFunction numerator,
                        DensityFunction denominator,
                        DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Remainder> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Remainder::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Remainder::denominator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Remainder::errorArg)
                    ).apply(instance, Remainder::new)
            );

    public static final KeyDispatchDataCodec<Remainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double numeratorValue = numerator.compute(pos);
        double denominatorValue = denominator.compute(pos);

        if (denominatorValue == 0) {
            return errorArg.compute(pos);
        }

        return numeratorValue % denominatorValue;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Remainder(
                        numerator,
                        denominator,
                        errorArg
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
