package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.klinbee.more_density_functions.MoreDensityFunctionsMod;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

import java.util.Optional;

public record FloorModulo(DensityFunction dividend, DensityFunction divisor, Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(FloorModulo::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(FloorModulo::divisor), DensityFunction.FUNCTION_CODEC.optionalFieldOf("error_output").forGetter(FloorModulo::errorDf)).apply(instance, (FloorModulo::new)));
    public static final CodecHolder<FloorModulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    @Override
    public double sample(NoisePos pos) {

        int dividendValue = (int) this.dividend.sample(pos);
        int divisorValue = (int) this.divisor.sample(pos);

        if (divisorValue == 0) {
            if (errorDf.isPresent()) {
                return this.errorDf.get().sample(pos);
            }
            return MoreDensityFunctionsMod.DEFAULT_ERROR;
        }

        return Math.floorMod(dividendValue, divisorValue);
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        if (this.errorDf.isPresent()) {
            return visitor.apply(new FloorModulo(this.dividend.apply(visitor), this.divisor.apply(visitor), Optional.of(this.errorDf.get().apply(visitor))));
        }
        return visitor.apply(new FloorModulo(this.dividend.apply(visitor), this.divisor.apply(visitor), Optional.empty()));
    }

    @Override
    public DensityFunction dividend() {
        return this.dividend;
    }

    @Override
    public DensityFunction divisor() {
        return this.divisor;
    }

    @Override
    public double minValue() {
        if (errorDf.isPresent()) {
            return Math.min(this.errorDf.get().minValue(), Math.min(-Math.abs(this.divisor.minValue()), -Math.abs(this.divisor.maxValue())));
        }
        return Math.min(MoreDensityFunctionsMod.DEFAULT_ERROR, Math.min(-Math.abs(this.divisor.minValue()), -Math.abs(this.divisor.maxValue())));
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(), Math.max(Math.abs(this.divisor.minValue()), Math.abs(this.divisor.maxValue())));
        }
        return Math.max(MoreDensityFunctionsMod.DEFAULT_ERROR, Math.max(Math.abs(this.divisor.minValue()), Math.abs(this.divisor.maxValue())));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
