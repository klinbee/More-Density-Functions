package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Subtract(DensityFunction arg1, DensityFunction arg2) implements DensityFunction {

    private static final MapCodec<Subtract> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("argument1").forGetter(Subtract::arg1), DensityFunction.FUNCTION_CODEC.fieldOf("argument2").forGetter(Subtract::arg2)).apply(instance, (Subtract::new)));
    public static final CodecHolder<Subtract> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.arg1.sample(pos) - this.arg2.sample(pos);
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }
    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new Subtract(this.arg1.apply(visitor), this.arg2.apply(visitor)));
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
        return this.arg1.minValue() - this.arg2.maxValue();
    }

    @Override
    public double maxValue() {
        return this.arg1.maxValue() - this.arg2.minValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
