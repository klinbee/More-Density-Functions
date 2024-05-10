package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorDivision(DensityFunction dividend, DensityFunction divisor, double maxOutput, double minOutput) implements DensityFunction {

    private static final MapCodec<FloorDivision> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("dividend").forGetter(FloorDivision::dividend), DensityFunction.CODEC.fieldOf("divisor").forGetter(FloorDivision::divisor), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(FloorDivision::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(FloorDivision::minOutput)).apply(instance, (FloorDivision::new)));
    public static final CodecHolder<FloorDivision> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        double initialDivisor = this.divisor.sample(pos);
        long dividend = Math.round(this.dividend.sample(pos));
        long divisor = Math.round(initialDivisor);

        if (divisor == 0) {
            return initialDivisor >= 0 ? maxOutput : minOutput;
        }

        long quotient = Math.floorDiv(dividend,divisor);
        if (quotient > this.maxOutput) {
            return maxOutput;
        }
        if (quotient < this.minOutput) {
            return minOutput;
        }
        return  quotient;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FloorDivision(this.dividend,this.divisor, this.maxOutput, this.minOutput));
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
