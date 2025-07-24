package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDensityFunctionsConstants {

    /// Mod Info
    public static final String MOD_ID = "moredensityfunctions";
    public static final String MOD_NAME = "MoreDensityFunctions";
    public static final String MOD_NAMESPACE = "moredfs";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    /// Constant Values
    public static final double MAX_COORD_DOUBLE = 30_000_000D;
    public static final double MIN_COORD_DOUBLE = -MAX_COORD_DOUBLE;
    public static final int MAX_COORD_INT = 30_000_000;
    public static final int MIN_COORD_INT = -MAX_COORD_INT;

    /// Useful Codecs
    public static final Codec<Integer> COORD_CODEC_INT = Codec.intRange(MIN_COORD_INT, MAX_COORD_INT);
    public static final Codec<Integer> NON_NEGATIVE_INT = Codec.intRange(0, Integer.MAX_VALUE);
    public static final Codec<Integer> POSITIVE_INT = Codec.intRange(1, Integer.MAX_VALUE);

    /// ResourceKeys
    public static final ResourceKey<Registry<RandomSampler>> RANDOM_SAMPLER = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_NAMESPACE, "random_sampler"));
    public static final ResourceKey<Registry<MapCodec<? extends RandomSampler>>> RANDOM_SAMPLER_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_NAMESPACE, "random_sampler_type"));
}