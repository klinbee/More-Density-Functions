package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorModulo(DensityFunction dividend, DensityFunction divisor,
                          double errorVal) implements DensityFunction {

    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(FloorModulo::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(FloorModulo::divisor), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("error_value").forGetter(FloorModulo::errorVal)).apply(instance, (FloorModulo::new)));
    public static final CodecHolder<FloorModulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {

        int dividendValue = (int) this.dividend.sample(pos);
        int divisorValue = (int) this.divisor.sample(pos);

        if (divisorValue == 0) {
            return this.errorVal;
        }

        return Math.floorMod(dividendValue, divisorValue);
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FloorModulo(this.dividend.apply(visitor), this.divisor.apply(visitor), this.errorVal));
    }

    @Override
    public DensityFunction dividend() {
        return dividend;
    }

    @Override
    public DensityFunction divisor() {
        return divisor;
    }

    @Override
    public double minValue() {
        return Math.min(dividend.minValue(), divisor.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(dividend.maxValue(), divisor.maxValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
