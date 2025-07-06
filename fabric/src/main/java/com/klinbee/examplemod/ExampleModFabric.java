package com.klinbee.examplemod;

import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ExampleModCommon.init();
    }
}
