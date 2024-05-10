package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Power(DensityFunction base, DensityFunction exponent) implements DensityFunction {

    private static final MapCodec<Power> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("base").forGetter(Power::base), DensityFunction.CODEC.fieldOf("exp").forGetter(Power::exponent)).apply(instance, (Power::new)));
    public static final CodecHolder<Power> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return Math.pow(this.base.sample(pos),this.exponent.sample(pos));
    }

    @Override
    public void method_40470(double[] ds, class_6911 arg) {
        arg.method_40478(ds, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Power(this.base,this.exponent));
    }

    @Override
    public DensityFunction base() {
        return base;
    }

    @Override
    public DensityFunction exponent() {
        return exponent;
    }

    @Override
    public double minValue() {
        return Math.min(base.minValue(),exponent.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(base.maxValue(),exponent.maxValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
