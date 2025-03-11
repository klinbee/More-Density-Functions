package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record ShiftFunction(DensityFunction df, DensityFunction shiftx, DensityFunction shifty, DensityFunction shiftz) implements DensityFunction {

    private static final MapCodec<ShiftFunction> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(ShiftFunction::df), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(ShiftFunction::shiftx), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(ShiftFunction::shifty), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(ShiftFunction::shiftz)).apply(instance, ShiftFunction::new));
    public static final KeyDispatchDataCodec<ShiftFunction> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pContext) {
        return this.df.compute(new SinglePointContext(pContext.blockX() + (int)this.shiftx.compute(pContext), pContext.blockY() + (int)this.shifty.compute(pContext), pContext.blockZ() + (int)this.shiftz.compute(pContext)));
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new ShiftFunction(this.df, this.shiftx, this.shifty, this.shiftz));
    }

    public DensityFunction df() {
        return this.df;
    }

    public DensityFunction shiftx() {
        return this.shiftx;
    }

    public DensityFunction shifty() {
        return this.shifty;
    }

    public DensityFunction shiftz() {
        return this.shiftz;
    }

    @Override
    public double minValue() {
        return df.minValue();
    }

    @Override
    public double maxValue() {
        return df.maxValue();
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
