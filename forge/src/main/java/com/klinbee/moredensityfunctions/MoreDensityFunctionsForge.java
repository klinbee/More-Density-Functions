package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
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

@Mod(MoreDensityFunctionsConstants.MOD_ID)
public class MoreDensityFunctionsForge {

    private static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTIONS = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, MoreDensityFunctionsConstants.MOD_NAMESPACE);
    public static final RegistryObject<Codec<? extends DensityFunction>> ASIN = DENSITY_FUNCTIONS.register("asin", ArcSine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ACOS = DENSITY_FUNCTIONS.register("acos", ArcCosine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ATAN = DENSITY_FUNCTIONS.register("atan", ArcTangent.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> CEIL = DENSITY_FUNCTIONS.register("ceil", Ceil.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> COS = DENSITY_FUNCTIONS.register("cos", Cosine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> DIV = DENSITY_FUNCTIONS.register("div", Divide.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR = DENSITY_FUNCTIONS.register("floor", Floor.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_DIV = DENSITY_FUNCTIONS.register("floor_div", FloorDivide.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_MOD = DENSITY_FUNCTIONS.register("floor_mod", FloorModulo.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> IEEE_REM = DENSITY_FUNCTIONS.register("ieee_rem", IEEERemainder.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> NEGATE = DENSITY_FUNCTIONS.register("negate", Negate.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POLAR_COORDS = DENSITY_FUNCTIONS.register("polar_coords", PolarCoords.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POWER = DENSITY_FUNCTIONS.register("power", Power.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> RECIPROCAL = DENSITY_FUNCTIONS.register("reciprocal", Reciprocal.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> REM = DENSITY_FUNCTIONS.register("rem", Remainder.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ROUND = DENSITY_FUNCTIONS.register("round", Round.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SHIFT_DF = DENSITY_FUNCTIONS.register("shift_df", ShiftDensityFunction.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SIGMOID = DENSITY_FUNCTIONS.register("sigmoid", Sigmoid.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SIGNUM = DENSITY_FUNCTIONS.register("signum", Signum.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SINE = DENSITY_FUNCTIONS.register("sine", Sine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SQRT = DENSITY_FUNCTIONS.register("sqrt", SquareRoot.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SUBTRACT = DENSITY_FUNCTIONS.register("subtract", Subtract.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> TAN = DENSITY_FUNCTIONS.register("tan", Tangent.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> VEC_ANGLE = DENSITY_FUNCTIONS.register("vector_angle", VectorAngle.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> XGRAD = DENSITY_FUNCTIONS.register("x_clamped_gradient", XClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> X_POS = DENSITY_FUNCTIONS.register("x", XPos.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Y_POS = DENSITY_FUNCTIONS.register("y", YPos.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Z_GRAD = DENSITY_FUNCTIONS.register("z_clamped_gradient", ZClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Z_POS = DENSITY_FUNCTIONS.register("z", ZPos.CODEC::codec);

    public MoreDensityFunctionsForge() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        DENSITY_FUNCTIONS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MoreDensityFunctionsConstants.LOG.info("Hello Forge world!");
        MoreDensityFunctionsCommon.init();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MoreDensityFunctionsConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}