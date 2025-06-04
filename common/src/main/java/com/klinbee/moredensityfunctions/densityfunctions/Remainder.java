package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;


public record Remainder(DensityFunction numerator, DensityFunction denominator,
                        Optional<DensityFunction> errorArgHolder, DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<Remainder> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Remainder::numerator),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Remainder::denominator),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Remainder::errorArgHolder)
    ).apply(instance, (numerator, denominator, errorArgHolder) ->
            new Remainder(numerator, denominator, errorArgHolder, errorArgHolder.orElse(DensityFunctions.zero()))
    ));
    public static final KeyDispatchDataCodec<Remainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double numeratorValue = this.numerator.compute(pos);
        double denominatorValue = this.denominator.compute(pos);

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
        return visitor.apply(new Remainder(this.numerator, this.denominator, this.errorArgHolder, this.errorArg));
    }

    public DensityFunction numerator() {
        return numerator;
    }

    public DensityFunction denominator() {
        return denominator;
    }

    public Optional<DensityFunction> errorArgHolder() {
        return errorArgHolder;
    }

    @Override
    public double minValue() {
        double minError = errorArg.minValue();
        double minDenom = denominator.minValue();
        double maxDenom = denominator.maxValue();

        // Most negative possible: -|y|/2 where |y| is maximized

        double largestMagnitude = Math.max(Math.abs(minDenom), Math.abs(maxDenom));

        // Conservative: assume both error and mathematical minimum are possible
        return Math.min(minError, -largestMagnitude / 2);
    }

    @Override
    public double maxValue() {
        double maxError = errorArg.maxValue();
        double minDenom = denominator.minValue();
        double maxDenom = denominator.maxValue();

        // Most positive possible: |y|/2 where |y| is maximized
        double largestMagnitude = Math.max(Math.abs(minDenom), Math.abs(maxDenom));

        // Conservative: assume both error and mathematical maximum are possible
        return Math.max(maxError, largestMagnitude / 2);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
