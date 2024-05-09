package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Modulo(DensityFunction dividend, DensityFunction divisor) implements DensityFunction {

    private static final MapCodec<Modulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("argument1").forGetter(Modulo::dividend), DensityFunction.CODEC.fieldOf("argument2").forGetter(Modulo::divisor)).apply(instance, (Modulo::new)));
    public static final CodecHolder<Modulo> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.dividend.sample(pos) % this.divisor.sample(pos);
    }

    @Override
    public void method_40470(double[] ds, class_6911 arg) {
        arg.method_40478(ds, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Modulo(this.dividend,this.divisor));
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
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
