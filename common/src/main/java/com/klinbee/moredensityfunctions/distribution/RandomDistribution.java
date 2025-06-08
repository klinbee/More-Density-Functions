package com.klinbee.moredensityfunctions.distribution;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public interface RandomDistribution {

    @SuppressWarnings("unchecked")
    Codec<RandomDistribution> CODEC = ExtraCodecs.lazyInitializedCodec(() -> {
        var randomDistributionRegistry = BuiltInRegistries.REGISTRY.get(MoreDensityFunctionsConstants.RANDOM_DISTRIBUTION_TYPE.location());
        if (randomDistributionRegistry == null)
            throw new NullPointerException("Worldgen modifier registry does not exist yet!");
        return ((Registry<Codec<? extends RandomDistribution>>) randomDistributionRegistry).byNameCodec();
    }).dispatch(RandomDistribution::codec, Function.identity());

    default double getRandom(long hashedSeed) {
        return getSampler().sample(hashedSeed);
    }

    RandomSampler getSampler();

    Codec<? extends RandomDistribution> codec();
}
