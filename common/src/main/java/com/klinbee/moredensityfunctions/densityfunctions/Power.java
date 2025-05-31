package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

/*
TODO: Make this function's minValue()/maxValue() functions more accurate...
 */
public record Power(DensityFunction base, DensityFunction exponent, Optional<Double> maxOutput,
                    Optional<Double> minOutput, Optional<DensityFunction> errorArg) implements DensityFunction {
    private static final MapCodec<Power> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Power::base),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("exponent").forGetter(Power::exponent),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Power::minOutput),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Power::maxOutput),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Power::errorArg)
    ).apply(instance, (Power::new)));
    public static final KeyDispatchDataCodec<Power> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double exponentValue = this.exponent.compute(pos);
        double baseValue = this.base.compute(pos);

        if (exponentValue == 0.0D) {
            return 1.0D;
        }

        if (exponentValue == 1.0D) {
            return this.base.compute(pos);
        }

        double result = StrictMath.pow(baseValue, exponentValue);

        // Ain't no way I doin' all those cases, Power is messed up.
        if (Double.isNaN(result)) {
            if (errorArg().isPresent()) {
                return errorArg.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }


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
        return visitor.apply(new Power(this.base, this.exponent, this.minOutput, this.maxOutput, this.errorArg));
    }

    public DensityFunction base() {
        return base;
    }

    public DensityFunction exponent() {
        return exponent;
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
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
