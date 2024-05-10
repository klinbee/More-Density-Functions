package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Subtract(DensityFunction arg1, DensityFunction arg2) implements DensityFunction {

    private static final MapCodec<Subtract> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("argument1").forGetter(Subtract::arg1), DensityFunction.CODEC.fieldOf("argument2").forGetter(Subtract::arg2)).apply(instance, (Subtract::new)));
    public static final CodecHolder<Subtract> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.arg1.sample(pos) - this.arg2.sample(pos);
    }

    @Override
    public void method_40470(double[] ds, class_6911 arg) {
        arg.method_40478(ds, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Subtract(this.arg1,this.arg2));
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
