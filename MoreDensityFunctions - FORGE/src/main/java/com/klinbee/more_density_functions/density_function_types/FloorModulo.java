package com.klinbee.more_density_functions.density_function_types;

import com.klinbee.more_density_functions.MoreDensityFunctions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record FloorModulo(DensityFunction dividend, DensityFunction divisor, Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<FloorModulo> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("dividend").forGetter(FloorModulo::dividend), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("divisor").forGetter(FloorModulo::divisor), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_output").forGetter(FloorModulo::errorDf)).apply(instance, FloorModulo::new));
    public static final KeyDispatchDataCodec<FloorModulo> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

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

        return Math.floorMod(dividendValue, divisorValue);
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new FloorModulo(this.dividend, this.divisor, this.errorDf));
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
            return Math.min(this.errorDf.get().minValue(),MoreDensityFunctions.DEFAULT_MIN_OUTPUT);
        }
        return Math.min(MoreDensityFunctions.DEFAULT_ERROR,MoreDensityFunctions.DEFAULT_MIN_OUTPUT);
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(),MoreDensityFunctions.DEFAULT_MAX_OUTPUT);
        }
        return Math.max(MoreDensityFunctions.DEFAULT_ERROR,MoreDensityFunctions.DEFAULT_MAX_OUTPUT);
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
