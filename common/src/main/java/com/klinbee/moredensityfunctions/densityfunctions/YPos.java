package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;


public record YPos() implements DensityFunction {
    private static final MapCodec<YPos> MAP_CODEC = MapCodec.unit(new YPos());
    public static final KeyDispatchDataCodec<YPos> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute( FunctionContext pos) {
        return pos.blockY();
    }

    @Override
    public void fillArray(double  [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public  DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new YPos());
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
    public  KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
