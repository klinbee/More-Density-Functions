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
    public static final RegistryObject<Codec<? extends DensityFunction>> ACOS = DENSITY_FUNCTIONS.register("acos", ArcCosine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ASIN = DENSITY_FUNCTIONS.register("asin", ArcSine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> ATAN = DENSITY_FUNCTIONS.register("atan", ArcTangent.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> CEIL = DENSITY_FUNCTIONS.register("ceil", Ceil.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> CLAMP = DENSITY_FUNCTIONS.register("clamp", Clamp.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> COS = DENSITY_FUNCTIONS.register("cos", Cosine.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> DERIVATIVE = DENSITY_FUNCTIONS.register("derivative", DirectionalDerivative.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> DIV = DENSITY_FUNCTIONS.register("div", Divide.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR = DENSITY_FUNCTIONS.register("floor", Floor.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_DIV = DENSITY_FUNCTIONS.register("floor_div", FloorDivide.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOOR_MOD = DENSITY_FUNCTIONS.register("floor_mod", FloorModulo.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> GRADIENT_MAGNITUDE = DENSITY_FUNCTIONS.register("gradient_magnitude", GradientMagnitude.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> IEEE_REM = DENSITY_FUNCTIONS.register("ieee_rem", IEEERemainder.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> LOG = DENSITY_FUNCTIONS.register("log", Log.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> LOG_2 = DENSITY_FUNCTIONS.register("log_2", Log2.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> LOG_2FLOOR = DENSITY_FUNCTIONS.register("log2_floor", Log2Floor.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> NATURAL_LOG = DENSITY_FUNCTIONS.register("ln", NaturalLog.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> NEGATE = DENSITY_FUNCTIONS.register("negate", Negate.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POLAR_COORDS = DENSITY_FUNCTIONS.register("polar_coords", PolarCoords.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POWER = DENSITY_FUNCTIONS.register("power", Power.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> PROFILER = DENSITY_FUNCTIONS.register("profiler", Profiler.CODEC::codec);
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
    public static final RegistryObject<Codec<? extends DensityFunction>> VALUE_NOISE = DENSITY_FUNCTIONS.register("value_noise", ValueNoise.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> VEC_ANGLE = DENSITY_FUNCTIONS.register("vector_angle", VectorAngle.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> XGRAD = DENSITY_FUNCTIONS.register("x_clamped_gradient", XClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> X_POS = DENSITY_FUNCTIONS.register("x", XPos.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Y_POS = DENSITY_FUNCTIONS.register("y", YPos.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Z_GRAD = DENSITY_FUNCTIONS.register("z_clamped_gradient", ZClampedGradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> Z_POS = DENSITY_FUNCTIONS.register("z", ZPos.CODEC::codec);

    public MoreDensityFunctionsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        DENSITY_FUNCTIONS.register(modEventBus);
        MoreDensityFunctionsConstants.LOG.info("Registered {} density functions", DENSITY_FUNCTIONS.getEntries().size());

        MinecraftForge.EVENT_BUS.register(this);
        MoreDensityFunctionsCommon.init();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MoreDensityFunctionsConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}