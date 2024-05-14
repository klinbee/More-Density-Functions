package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorModulo(DensityFunction dividend, DensityFunction divisor,
                          DensityFunction errorDf) implements DensityFunction {

    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(FloorModulo::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(FloorModulo::divisor), DensityFunction.FUNCTION_CODEC.fieldOf("error_output").forGetter(FloorModulo::errorDf)).apply(instance, (FloorModulo::new)));
    public static final CodecHolder<FloorModulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {

        int dividendValue = (int) this.dividend.sample(pos);
        int divisorValue = (int) this.divisor.sample(pos);

        if (divisorValue == 0) {
            return this.errorDf.sample(pos);
        }

        return Math.floorMod(dividendValue, divisorValue);
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FloorModulo(this.dividend.apply(visitor), this.divisor.apply(visitor), this.errorDf.apply(visitor)));
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
        return Math.min(this.errorDf.minValue(),-Math.abs(this.divisor.maxValue()));
    }

    @Override
    public double maxValue() {
        return Math.max(this.errorDf.maxValue(),Math.abs(this.divisor.maxValue()));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
