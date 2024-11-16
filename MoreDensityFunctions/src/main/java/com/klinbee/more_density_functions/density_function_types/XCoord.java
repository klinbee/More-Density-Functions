package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record XCoord() implements DensityFunction.Base {

    public static final CodecHolder<XCoord> CODEC = CodecHolder.of(MapCodec.unit(new XCoord()));

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return Math.min(Math.max(pos.blockX(),minValue()), maxValue());
    }

    @Override
    public double minValue() {
        return -30_000_000;
    }

    @Override
    public double maxValue() {
        return 30_000_000;
    }

    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}