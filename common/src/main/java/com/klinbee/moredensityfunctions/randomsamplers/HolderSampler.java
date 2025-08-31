package com.klinbee.moredensityfunctions.randomsamplers;

import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;

public record HolderSampler(Holder<RandomSampler> samplerHolder) implements RandomSampler {

    @Override
    public double sample(long hashedSeed) {
        return samplerHolder.value().sample(hashedSeed);
    }

    @Override
    public double minValue() {
        return samplerHolder.value().minValue();
    }

    @Override
    public double maxValue() {
        return samplerHolder.value().maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends RandomSampler> codec() {
        throw new UnsupportedOperationException("Calling .codec() on HolderSampler");
    }
}
