package com.klinbee.moredensityfunctions.util;

import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/// Used by `WorleyNoise` for sampling offsets for the points within the Worley Cells
public record Jitter(RandomSampler samplerX,
                     RandomSampler samplerY,
                     RandomSampler samplerZ) {
    public static final Codec<Jitter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RandomSampler.CODEC.fieldOf("sampler_x").forGetter(Jitter::samplerX),
                    RandomSampler.CODEC.fieldOf("sampler_y").forGetter(Jitter::samplerY),
                    RandomSampler.CODEC.fieldOf("sampler_z").forGetter(Jitter::samplerZ)
            ).apply(instance, Jitter::new)
    );
}