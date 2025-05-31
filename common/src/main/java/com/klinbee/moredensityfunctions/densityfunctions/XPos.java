package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;


public record XPos() implements DensityFunction {
    private static final MapCodec<XPos> MAP_CODEC = MapCodec.unit(new XPos());
    public static final KeyDispatchDataCodec<XPos> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        return pos.blockX();
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new XPos());
    }

    @Override
    public double minValue() {
        return MoreDensityFunctionsConstants.MIN_COORD_DOUBLE;
    }

    @Override
    public double maxValue() {
        return MoreDensityFunctionsConstants.MAX_COORD_DOUBLE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
