package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MoreDensityFunctionsConstants {

    /// Mod Info
    public static final String MOD_ID = "moredensityfunctions";
    public static final String MOD_NAME = "MoreDensityFunctions";
    public static final String MOD_NAMESPACE = "moredfs";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    /// Constant Values
    public static final int XZ_MAX_INT = 33_554_431;
    public static final int XZ_MIN_INT = -33_554_432;
    public static final int Y_MAX_INT = 6143;
    public static final int Y_MIN_INT = -6144;
    public static final double XZ_MAX_DOUBLE = XZ_MAX_INT;
    public static final double XZ_MIN_DOUBLE = XZ_MIN_INT;
    public static final double Y_MAX_DOUBLE = Y_MAX_INT;
    public static final double Y_MIN_DOUBLE = Y_MIN_INT;

    /// Useful Codecs
    public static final Codec<Integer> COORD_CODEC_INT = Codec.intRange(XZ_MIN_INT, XZ_MAX_INT);
    public static final Codec<DensityFunction[]> DENSITY_FUNCTION_ARRAY_CODEC =
            DensityFunction.HOLDER_HELPER_CODEC.listOf()
                    .xmap(
                            list -> list.toArray(new DensityFunction[0]),
                            Arrays::asList
                    );

    /// ResourceKeys
    public static final ResourceKey<Registry<RandomSampler>> RANDOM_SAMPLER = ResourceKey.createRegistryKey(new ResourceLocation(MOD_NAMESPACE, "worldgen/random_sampler"));
    public static final ResourceKey<Registry<Codec<? extends RandomSampler>>> RANDOM_SAMPLER_TYPE = ResourceKey.createRegistryKey(new ResourceLocation(MOD_NAMESPACE, "worldgen/random_sampler_type"));
}