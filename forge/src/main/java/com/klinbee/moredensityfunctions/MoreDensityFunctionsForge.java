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

    public MoreDensityFunctionsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /// Register through CommonRegistrations
        ForgeGenericRegistrar.addRegistry(RegistryKey.DENSITY_FUNCTION, DENSITY_FUNCTIONS);

        CommonRegistrations.registerCommon(ForgeGenericRegistrar::register);


        /// Add registrations to eventBus
        DENSITY_FUNCTIONS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

}