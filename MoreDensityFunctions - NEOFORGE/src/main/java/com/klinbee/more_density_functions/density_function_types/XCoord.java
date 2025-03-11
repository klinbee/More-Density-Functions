package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record XCoord() implements DensityFunction.SimpleFunction {

    public static final KeyDispatchDataCodec<XCoord> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new XCoord()));

    @Override
    public double compute(FunctionContext pContext) {
        return Math.min(Math.max(pContext.blockX(),minValue()), maxValue());
    }

    @Override
    public double minValue() {
        return -30_000_000;
    }

    @Override
    public double maxValue() {
        return 30_000_000;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction.SimpleFunction> codec() {
        return CODEC;
    }
}
