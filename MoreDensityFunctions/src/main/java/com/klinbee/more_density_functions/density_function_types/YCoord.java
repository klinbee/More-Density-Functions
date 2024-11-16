package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record YCoord() implements DensityFunction.Base {

    public static final CodecHolder<YCoord> CODEC = CodecHolder.of(MapCodec.unit(new YCoord()));

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return Math.min(Math.max(pos.blockY(),minValue()), maxValue());
    }

    @Override
    public double minValue() {
        return -2032;
    }

    @Override
    public double maxValue() {
        return 2032;
    }

    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
