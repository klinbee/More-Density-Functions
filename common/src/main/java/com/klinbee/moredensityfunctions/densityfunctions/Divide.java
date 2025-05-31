package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record Divide(DensityFunction numerator, DensityFunction denominator, Optional<Double> maxOutput,
                     Optional<Double> minOutput, Optional<DensityFunction> errorArg) implements DensityFunction {
    private static final MapCodec<Divide> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Divide::numerator),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Divide::denominator),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Divide::minOutput),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Divide::maxOutput),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Divide::errorArg)
    ).apply(instance, (Divide::new)));
    public static final KeyDispatchDataCodec<Divide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double numeratorValue = this.numerator.compute(pos);
        double denominatorValue = this.denominator.compute(pos);

        if (denominatorValue == 0) {
            if (errorArg.isPresent()) {
                return this.errorArg.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }

        double result = numeratorValue / denominatorValue;


        if (result > this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT)) {
            return this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT);
        }

        if (result < this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT)) {
            return this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT);
        }

        return result;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Divide(this.numerator, this.denominator, this.minOutput, this.maxOutput, this.errorArg));
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

    public Optional<DensityFunction> errorArg() {
        return errorArg;
    }

    @Override
    public double minValue() {
        if (errorArg.isPresent()) {
            return Math.min(this.errorArg.get().minValue(), this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT));
        }
        return Math.min(MoreDensityFunctionsConstants.DEFAULT_ERROR, this.minOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT));
    }

    @Override
    public double maxValue() {
        if (errorArg.isPresent()) {
            return Math.max(this.errorArg.get().maxValue(), this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT));
        }
        return Math.max(MoreDensityFunctionsConstants.DEFAULT_ERROR, this.maxOutput.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
