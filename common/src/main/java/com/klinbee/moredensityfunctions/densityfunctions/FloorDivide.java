package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public record FloorDivide(DensityFunction numerator, DensityFunction denominator, Optional<Double> maxOutput,
                          Optional<Double> minOutput, Optional<DensityFunction> argError) implements DensityFunction {
    private static final MapCodec<FloorDivide> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(FloorDivide::numerator), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("arg").forGetter(FloorDivide::denominator), Codec.DOUBLE.optionalFieldOf("min_output").forGetter(FloorDivide::minOutput), Codec.DOUBLE.optionalFieldOf("max_output").forGetter(FloorDivide::maxOutput), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(FloorDivide::argError)).apply(instance, (FloorDivide::new)));
    public static final KeyDispatchDataCodec<FloorDivide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        int numeratorValue = (int) StrictMath.floor(this.numerator.compute(pos));
        int denominatorValue = (int) StrictMath.floor(this.denominator.compute(pos));

        if (denominatorValue == 0) {
            if (argError.isPresent()) {
                return this.argError.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }

        double result = StrictMath.floorDiv(numeratorValue, denominatorValue);


        if (result > this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT)) {
            return this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT);
        }

        if (result < this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT)) {
            return this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT);
        }

        return result;
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FloorDivide(this.numerator, this.denominator, this.minOutput, this.maxOutput, this.argError));
    }

    public DensityFunction numerator() {
        return numerator;
    }

    public DensityFunction denominator() {
        return denominator;
    }

    @Override
    public Optional<Double> minOutput() {
        return minOutput;
    }

    @Override
    public Optional<Double> maxOutput() {
        return maxOutput;
    }

    @Override
    public Optional<DensityFunction> argError() {
        return argError;
    }

    @Override
    public double minValue() {
        if (argError.isPresent()) {
            return Math.min(this.argError.get().minValue(), this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT));
        }
        return Math.min(MoreDensityFunctionsConstants.DEFAULT_ERROR, this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT));
    }

    @Override
    public double maxValue() {
        if (argError.isPresent()) {
            return Math.max(this.argError.get().maxValue(), this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT));
        }
        return Math.max(MoreDensityFunctionsConstants.DEFAULT_ERROR, this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT));
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
