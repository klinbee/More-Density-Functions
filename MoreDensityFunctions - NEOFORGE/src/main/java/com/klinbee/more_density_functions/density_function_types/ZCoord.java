package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record ZCoord() implements DensityFunction.SimpleFunction {

    public static final KeyDispatchDataCodec<ZCoord> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new ZCoord()));

    @Override
    public double compute(FunctionContext pContext) {
        return Math.min(Math.max(pContext.blockZ(),minValue()), maxValue());
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
