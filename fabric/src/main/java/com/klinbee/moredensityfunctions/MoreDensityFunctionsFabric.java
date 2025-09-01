package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.registration.CommonRegistrations;
import com.klinbee.moredensityfunctions.registration.RegistryKey;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;

public class MoreDensityFunctionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        /// Register through CommonRegistrations
        FabricGenericRegistrar.addRegistry(RegistryKey.DENSITY_FUNCTION, BuiltInRegistries.DENSITY_FUNCTION_TYPE);

        CommonRegistrations.registerCommon(FabricGenericRegistrar::register);
    }

}