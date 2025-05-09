package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record ZPos() implements DensityFunction {
    private static final MapCodec<ZPos> MAP_CODEC = MapCodec.unit(new ZPos());
    public static final KeyDispatchDataCodec<ZPos> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return pos.blockZ();
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities,this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ZPos());
    }

    @Override
    public double minValue() {
        return MoreDensityFunctionsConstants.MIN_POS_DOUBLE;
    }

    @Override
    public double maxValue() {
        return MoreDensityFunctionsConstants.MAX_POS_DOUBLE;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
