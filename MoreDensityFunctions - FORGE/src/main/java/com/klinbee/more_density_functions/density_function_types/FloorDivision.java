package com.klinbee.more_density_functions.density_function_types;

import com.klinbee.more_density_functions.MoreDensityFunctions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record FloorDivision(DensityFunction dividend, DensityFunction divisor, Optional<Double> maxOutput, Optional<Double> minOutput, Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<Modulo> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Modulo::dividend), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("exponent").forGetter(Modulo::divisor), Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Modulo::maxOutput), Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Modulo::minOutput), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_output").forGetter(Modulo::errorDf)).apply(instance, Modulo::new));
    public static final KeyDispatchDataCodec<Modulo> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pContext) {
        int divisorValue = (int)this.divisor.compute(pContext);
        int dividendValue = (int)this.dividend.compute(pContext);;

        if (divisorValue == 0) {
            if (errorDf.isPresent()) {
                return this.errorDf.get().compute(pContext);
            }
            return MoreDensityFunctions.DEFAULT_ERROR;
        }

        double result = Math.floorDiv(dividendValue, divisorValue);


        if (result > this.maxOutput.orElse(MoreDensityFunctions.DEFAULT_MAX_OUTPUT)) {
            return this.maxOutput.orElse(MoreDensityFunctions.DEFAULT_MAX_OUTPUT);
        }

        if (result < this.minOutput.orElse(MoreDensityFunctions.DEFAULT_MIN_OUTPUT)) {
            return this.minOutput.orElse(MoreDensityFunctions.DEFAULT_MIN_OUTPUT);
        }

        return result;
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new Modulo(this.dividend, this.divisor, this.maxOutput, this.minOutput, this.errorDf));
    }

    public DensityFunction dividend() {
        return this.dividend;
    }

    public DensityFunction divisor() {
        return this.divisor;
    }

    public Optional<DensityFunction> errorDf() {
        return errorDf;
    }

    @Override
    public double minValue() {
        if (errorDf.isPresent()) {
            return Math.min(this.errorDf.get().minValue(),this.minOutput.orElse(MoreDensityFunctions.DEFAULT_MIN_OUTPUT));
        }
        return Math.min(MoreDensityFunctions.DEFAULT_ERROR,this.minOutput.orElse(MoreDensityFunctions.DEFAULT_MIN_OUTPUT));
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(),this.maxOutput.orElse(MoreDensityFunctions.DEFAULT_MAX_OUTPUT));
        }
        return Math.max(MoreDensityFunctions.DEFAULT_ERROR,this.maxOutput.orElse(MoreDensityFunctions.DEFAULT_MAX_OUTPUT));
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
