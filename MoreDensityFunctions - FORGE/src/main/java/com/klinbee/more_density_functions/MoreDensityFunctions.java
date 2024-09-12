package com.klinbee.more_density_functions;

import com.klinbee.more_density_functions.density_function_types.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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

    private static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTIONS = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, NAMESPACE);

    public static final RegistryObject<Codec<? extends DensityFunction>> X_CLAMPED_GRADIENT = DENSITY_FUNCTIONS.register( "x_clamped_gradient", XClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Z_CLAMPED_GRADIENT = DENSITY_FUNCTIONS.register("z_clamped_gradient", ZClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SINE = DENSITY_FUNCTIONS.register("sine", Sine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SQRT = DENSITY_FUNCTIONS.register("sqrt", Sqrt.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POWER = DENSITY_FUNCTIONS.register("power", Power.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR = DENSITY_FUNCTIONS.register("floor", Floor.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> CEIL = DENSITY_FUNCTIONS.register("ceil", Ceil.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ROUND = DENSITY_FUNCTIONS.register("round", Round.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SIGNUM = DENSITY_FUNCTIONS.register("signum", Signum.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> DIV = DENSITY_FUNCTIONS.register("div", Division.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_DIV = DENSITY_FUNCTIONS.register("floor_div", FloorDivision.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> MOD = DENSITY_FUNCTIONS.register("mod", Modulo.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_MOD = DENSITY_FUNCTIONS.register("floor_mod", FloorModulo.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> RECIPROCAL = DENSITY_FUNCTIONS.register("reciprocal", Reciprocal.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SHIFT_DF = DENSITY_FUNCTIONS.register("shift_df", ShiftFunction.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> NEGATE = DENSITY_FUNCTIONS.register("negate", Negation.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SUBTRACT = DENSITY_FUNCTIONS.register("subtract", Subtract.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> XCOORD = DENSITY_FUNCTIONS.register("x", XCoord.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> YCOORD = DENSITY_FUNCTIONS.register("y", YCoord.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ZCOORD = DENSITY_FUNCTIONS.register("z", ZCoord.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SIMPLEX_NOISE = DENSITY_FUNCTIONS.register("simplex_noise", SimplexNoiseFunction.CODEC::codec);



    public MoreDensityFunctions()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        DENSITY_FUNCTIONS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

//        DENSITY_FUNCTIONS.register( "x_clamped_gradient", XClampedGradient.CODEC::codec);
//        DENSITY_FUNCTIONS.register("z_clamped_gradient", ZClampedGradient.CODEC::codec);
//        DENSITY_FUNCTIONS.register("sine", Sine.CODEC::codec);
//        DENSITY_FUNCTIONS.register("sqrt", Sqrt.CODEC::codec);
//        DENSITY_FUNCTIONS.register("power", Power.CODEC::codec);
//        DENSITY_FUNCTIONS.register("floor", Floor.CODEC::codec);
//        DENSITY_FUNCTIONS.register("ceil", Ceil.CODEC::codec);
//        DENSITY_FUNCTIONS.register("round", Round.CODEC::codec);
//        DENSITY_FUNCTIONS.register("signum", Signum.CODEC::codec);
//        DENSITY_FUNCTIONS.register("div", Division.CODEC::codec);
//        DENSITY_FUNCTIONS.register("floor_div", FloorDivision.CODEC::codec);
//        DENSITY_FUNCTIONS.register("mod", Modulo.CODEC::codec);
//        DENSITY_FUNCTIONS.register("floor_mod", FloorModulo.CODEC::codec);
//        DENSITY_FUNCTIONS.register("reciprocal", Reciprocal.CODEC::codec);
//        DENSITY_FUNCTIONS.register("shift_df", ShiftFunction.CODEC::codec);
//        DENSITY_FUNCTIONS.register("negate", Negation.CODEC::codec);
//        DENSITY_FUNCTIONS.register("subtract", Subtract.CODEC::codec);
//        DENSITY_FUNCTIONS.register("x", XCoord.CODEC::codec);
//        DENSITY_FUNCTIONS.register("y", YCoord.CODEC::codec);
//        DENSITY_FUNCTIONS.register("z", ZCoord.CODEC::codec);
//        DENSITY_FUNCTIONS.register("simplex_noise", SimplexNoiseFunction.CODEC::codec);
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
