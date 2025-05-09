package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record PolarCoords() implements DensityFunction {
    private static final MapCodec<PolarCoords> MAP_CODEC = MapCodec.unit(new PolarCoords());
    public static final KeyDispatchDataCodec<PolarCoords> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return StrictMath.atan2(pos.blockX(),pos.blockZ());
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new PolarCoords());
    }

    @Override
    public double minValue() {
        return -StrictMath.PI;
    }

    @Override
    public double maxValue() {
        return StrictMath.PI;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
