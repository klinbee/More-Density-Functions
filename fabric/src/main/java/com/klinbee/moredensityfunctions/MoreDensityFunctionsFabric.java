package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.randomsamplers.*;
import com.klinbee.moredensityfunctions.registration.CommonRegistrations;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class MoreDensityFunctionsFabric implements ModInitializer {

    public static final WritableRegistry<Codec<? extends RandomSampler>> RANDOM_SAMPLER_TYPE = FabricRegistryBuilder.createSimple(MoreDensityFunctionsConstants.RANDOM_SAMPLER_TYPE).buildAndRegister();

    @Override
    public void onInitialize() {

        /// Register the RandomSampler Registry
        DynamicRegistries.register(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC);

        /// Register through CommonRegistrations
        FabricGenericRegistrar.addRegistry(CommonRegistrations.DENSITY_FUNCTIONS, BuiltInRegistries.DENSITY_FUNCTION_TYPE);
        FabricGenericRegistrar.addRegistry(CommonRegistrations.RANDOM_SAMPLERS, RANDOM_SAMPLER_TYPE);

        CommonRegistrations.registerCommon(FabricGenericRegistrar::register);
    }

}