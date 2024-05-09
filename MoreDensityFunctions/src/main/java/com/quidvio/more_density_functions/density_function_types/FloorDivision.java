package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorDivision(DensityFunction arg1, DensityFunction arg2, double maxOutput, double minOutput) implements DensityFunction {

    private static final MapCodec<FloorDivision> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("argument_1").forGetter(FloorDivision::arg1), DensityFunction.CODEC.fieldOf("argument_2").forGetter(FloorDivision::arg2), Codec.doubleRange(Double.MIN_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(FloorDivision::maxOutput), Codec.doubleRange(Double.MIN_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(FloorDivision::minOutput)).apply(instance, (FloorDivision::new)));
    public static final CodecHolder<FloorDivision> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        double initialDivisor = this.arg2.sample(pos);
        long dividend = Math.round(this.arg1.sample(pos));
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
    public void method_40470(double[] ds, class_6911 arg) {
        arg.method_40478(ds, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FloorDivision(this.arg1,this.arg2, this.maxOutput, this.minOutput));
    }

    @Override
    public DensityFunction arg1() {
        return arg1;
    }

    @Override
    public DensityFunction arg2() {
        return arg2;
    }

    @Override
    public double minValue() {
        return Math.min(arg1.minValue(),arg2.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(arg1.maxValue(),arg2.maxValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
