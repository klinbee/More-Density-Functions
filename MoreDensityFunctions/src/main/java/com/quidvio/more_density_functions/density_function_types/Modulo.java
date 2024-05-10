package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Modulo(DensityFunction dividend, DensityFunction divisor) implements DensityFunction {

    private static final MapCodec<Modulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("dividend").forGetter(Modulo::dividend), DensityFunction.CODEC.fieldOf("divisor").forGetter(Modulo::divisor)).apply(instance, (Modulo::new)));
    public static final CodecHolder<Modulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.dividend.sample(pos) % this.divisor.sample(pos);
    }

    @Override
    public void applyEach(double[] densities, EachApplier applier) {
        applier.applyEach(densities,this);
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
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
