package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Modulo(DensityFunction dividend, DensityFunction divisor, double errorVal) implements DensityFunction {

    private static final MapCodec<Modulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(Modulo::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(Modulo::divisor), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("error_value").forGetter(Modulo::errorVal)).apply(instance, (Modulo::new)));
    public static final CodecHolder<Modulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {

        int dividendValue = (int) this.dividend.sample(pos);
        int divisorValue = (int) this.divisor.sample(pos);

        if (divisorValue == 0) {
            return this.errorVal;
        }

        return dividendValue % divisorValue;
    }

    @Override
    public void applyEach(double[] densities, EachApplier applier) {
        applier.applyEach(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Modulo(this.dividend.apply(visitor), this.divisor.apply(visitor), this.errorVal));
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
