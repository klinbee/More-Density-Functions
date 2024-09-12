package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record YCoord() implements DensityFunction.SimpleFunction {

    public static final KeyDispatchDataCodec<YCoord> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new YCoord()));

    @Override
    public double compute(FunctionContext pContext) {
        return Math.min(Math.max(pContext.blockY(),minValue()), maxValue());
    }

    @Override
    public double minValue() {
        return -2032;
    }

    @Override
    public double maxValue() {
        return 2032;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction.SimpleFunction> codec() {
        return CODEC;
    }
}
