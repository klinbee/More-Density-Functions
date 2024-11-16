package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record ShiftFunction(DensityFunction df, DensityFunction shiftx, DensityFunction shifty, DensityFunction shiftz) implements DensityFunction {

    private static final MapCodec<ShiftFunction> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(ShiftFunction::df), DensityFunction.FUNCTION_CODEC.fieldOf("shift_x").forGetter(ShiftFunction::shiftx), DensityFunction.FUNCTION_CODEC.fieldOf("shift_y").forGetter(ShiftFunction::shifty), DensityFunction.FUNCTION_CODEC.fieldOf("shift_z").forGetter(ShiftFunction::shiftz)).apply(instance, (ShiftFunction::new)));
    public static final CodecHolder<ShiftFunction> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {
        return this.df.sample(new UnblendedNoisePos(pos.blockX() + (int)this.shiftx.sample(pos),pos.blockY() + (int)this.shifty.sample(pos),pos.blockZ() + (int)this.shiftz.sample(pos)));
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }
    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new ShiftFunction(this.df.apply(visitor), this.shiftx.apply(visitor), this.shifty.apply(visitor), this.shiftz.apply(visitor)));
    }

    @Override
    public DensityFunction shiftx() {
        return shiftx;
    }

    @Override
    public DensityFunction shifty() {
        return shifty;
    }

    @Override
    public DensityFunction shiftz() {
        return shiftz;
    }

    @Override
    public DensityFunction df() {
        return df;
    }

    @Override
    public double minValue() {
        return df.minValue();
    }

    @Override
    public double maxValue() {
        return df.maxValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
