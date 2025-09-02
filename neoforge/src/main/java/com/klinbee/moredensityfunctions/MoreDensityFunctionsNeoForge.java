package com.klinbee.moredensityfunctions;


import com.klinbee.moredensityfunctions.registration.CommonRegistrations;
import com.klinbee.moredensityfunctions.registration.RegistryKey;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(MoreDensityFunctionsConstants.MOD_ID)
public class MoreDensityFunctionsNeoForge {

    private static final DeferredRegister<MapCodec<? extends DensityFunction>>
            DENSITY_FUNCTIONS = DeferredRegister.create(
            BuiltInRegistries.DENSITY_FUNCTION_TYPE,
            MoreDensityFunctionsConstants.MOD_NAMESPACE
    );

    public MoreDensityFunctionsNeoForge(IEventBus eventBus) {
        /// Register through CommonRegistrations
        NeoForgeGenericRegistrar.addRegistry(RegistryKey.DENSITY_FUNCTION, DENSITY_FUNCTIONS);

        CommonRegistrations.registerCommon(NeoForgeGenericRegistrar::register);

        DENSITY_FUNCTIONS.register(eventBus);
    }
}