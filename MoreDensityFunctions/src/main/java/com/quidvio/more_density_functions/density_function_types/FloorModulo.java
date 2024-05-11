package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record FloorModulo(DensityFunction dividend, DensityFunction divisor) implements DensityFunction {

    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("dividend").forGetter(FloorModulo::dividend), DensityFunction.CODEC.fieldOf("divisor").forGetter(FloorModulo::divisor)).apply(instance, (FloorModulo::new)));
    public static final CodecHolder<FloorModulo> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return Math.floorMod((int)this.dividend.sample(pos),(int)this.divisor.sample(pos));
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }
    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FloorModulo(this.dividend.apply(visitor), this.divisor.apply(visitor)));
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
