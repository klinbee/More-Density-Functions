package com.klinbee.examplemod;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ExampleModConstants.MOD_ID)
public class ExampleModNeoForge {

    public ExampleModNeoForge(IEventBus eventBus) {
        ExampleModConstants.LOG.info("Hello NeoForge world!");
        ExampleModCommon.init();

    }
}