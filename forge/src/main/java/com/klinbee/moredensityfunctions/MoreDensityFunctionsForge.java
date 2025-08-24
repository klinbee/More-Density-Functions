package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.klinbee.moredensityfunctions.registration.CommonRegistrations;
import com.klinbee.moredensityfunctions.registration.RegistryKey;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod(MoreDensityFunctionsConstants.MOD_ID)
public class MoreDensityFunctionsForge {

    private static final DeferredRegister<Codec<? extends DensityFunction>>
            DENSITY_FUNCTIONS = DeferredRegister.create(
            Registries.DENSITY_FUNCTION_TYPE,
            MoreDensityFunctionsConstants.MOD_NAMESPACE
    );
    private static final DeferredRegister<Codec<? extends RandomSampler>>
            RANDOM_SAMPLERS = DeferredRegister.create(
            MoreDensityFunctionsConstants.RANDOM_SAMPLER_TYPE,
            MoreDensityFunctionsConstants.MOD_NAMESPACE
    );

    // I *for some reason*, need to do this
    public static final Supplier<IForgeRegistry<Codec<? extends RandomSampler>>>
            RANDOM_SAMPLER_REGISTRY_SUPPLIER = RANDOM_SAMPLERS.makeRegistry(() ->
            new RegistryBuilder<Codec<? extends RandomSampler>>()
                    .hasTags()
                    .disableSync()
                    .disableSaving()
    );

    public MoreDensityFunctionsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /// Register the RandomSampler Registry
        eventBus.addListener((DataPackRegistryEvent.NewRegistry event) ->
                event.dataPackRegistry(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC));

        /// Register through CommonRegistrations
        ForgeGenericRegistrar.addRegistry(RegistryKey.DENSITY_FUNCTION, DENSITY_FUNCTIONS);
        ForgeGenericRegistrar.addRegistry(RegistryKey.RANDOM_SAMPLER, RANDOM_SAMPLERS);

        CommonRegistrations.registerCommon(ForgeGenericRegistrar::register);


        /// Add registrations to eventBus
        DENSITY_FUNCTIONS.register(eventBus);
        RANDOM_SAMPLERS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}