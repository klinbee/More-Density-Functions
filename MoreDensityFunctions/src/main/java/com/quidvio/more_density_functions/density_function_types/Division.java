package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Division(DensityFunction dividend, DensityFunction divisor, double maxOutput, double minOutput, double errorValue) implements DensityFunction {

    private static final MapCodec<Division> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(Division::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(Division::divisor), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(Division::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(Division::minOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("error_value").forGetter(Division::errorValue)).apply(instance, (Division::new)));
    public static final CodecHolder<Division> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        double divisorValue = this.divisor.sample(pos);
        double dividendValue = this.dividend.sample(pos);

        if (divisorValue == 0) {
            return this.errorValue;
        }
        
        double result = dividendValue / divisorValue;
        
        if (result > this.maxOutput) {
            return this.maxOutput;
        }
        
        if (result < this.minOutput) {
            return this.minOutput;
        }
        
        return result;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Division(this.dividend.apply(visitor), this.divisor.apply(visitor), this.maxOutput, this.minOutput, this.errorValue));
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
    public double errorValue() {
        return errorValue;
    }

    @Override
    public double minValue() {
        return Math.min(dividend.minValue(),divisor.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(dividend.maxValue(),divisor.maxValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
