package com.klinbee.more_density_functions;

import com.klinbee.more_density_functions.density_function_types.Ceil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoreDensityFunctions.MOD_ID)
public class MoreDensityFunctions
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "more_density_functions";

    public static final double DEFAULT_ERROR = 0.0;
    public static final double DEFAULT_MAX_OUTPUT = 1.0;
    public static final double DEFAULT_MIN_OUTPUT = -1.0;
    public static final String NAMESPACE = "more_dfs";

    public MoreDensityFunctions()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE, "x_clamped_gradient"), XClampedGradient.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"z_clamped_gradient"), ZClampedGradient.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"sine"), Sine.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"sqrt"), Sqrt.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"power"), Power.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"floor"), Floor.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"ceil"), Ceil.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"round"), Round.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"signum"), Signum.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"div"), Division.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"floor_div"), FloorDivision.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"mod"), Modulo.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"floor_mod"), FloorModulo.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"reciprocal"), Reciprocal.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"shift_df"), ShiftFunction.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"negate"), Negation.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"subtract"), Subtract.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"x"), XCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"y"), YCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"z"), ZCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new ResourceLocation(NAMESPACE,"simplex_noise"), SimplexNoise.CODEC.codec());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
