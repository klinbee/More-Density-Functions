package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDensityFunctionsConstants {

    public static final String MOD_ID = "moredensityfunctions";
    public static final String MOD_NAME = "MoreDensityFunctions";
    public static final String MOD_NAMESPACE = "moredfs";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // Density Function Defaults
    public static final double DEFAULT_ERROR = 0.0D;
    public static final double DEFAULT_MAX_OUTPUT = 1.0D;
    public static final double DEFAULT_MIN_OUTPUT = -1.0D;

    public static final double MAX_COORD_DOUBLE = 30_000_000D;
    public static final double MIN_COORD_DOUBLE = -MAX_COORD_DOUBLE;
    public static final int MAX_COORD_INT = 30_000_000;
    public static final int MIN_COORD_INT = -MAX_COORD_INT;

    public static final Codec<Integer> COORD_CODEC_INT = Codec.intRange(MoreDensityFunctionsConstants.MIN_COORD_INT, MoreDensityFunctionsConstants.MAX_COORD_INT);
    public static final Codec<Double> COORD_CODEC_DOUBLE = Codec.doubleRange(MoreDensityFunctionsConstants.MIN_COORD_DOUBLE, MoreDensityFunctionsConstants.MAX_COORD_DOUBLE);

    public static final ResourceKey<Registry<RandomDistribution>> RANDOM_DISTRIBUTION = ResourceKey.createRegistryKey(new ResourceLocation(MoreDensityFunctionsConstants.MOD_NAMESPACE, "random_distribution"));
    public static final ResourceKey<Registry<Codec<? extends RandomDistribution>>> RANDOM_DISTRIBUTION_TYPE = ResourceKey.createRegistryKey(new ResourceLocation(MoreDensityFunctionsConstants.MOD_NAMESPACE, "random_distribution_type"));
}