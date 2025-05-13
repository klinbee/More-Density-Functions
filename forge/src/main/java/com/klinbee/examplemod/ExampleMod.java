package com.klinbee.examplemod;

import net.minecraftforge.fml.common.Mod;

@Mod(ExampleModConstants.MOD_ID)
public class ExampleMod {
    
    public ExampleMod() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ExampleModConstants.LOG.info("Hello Forge world!");
        ExampleModCommon.init();
        
    }
}