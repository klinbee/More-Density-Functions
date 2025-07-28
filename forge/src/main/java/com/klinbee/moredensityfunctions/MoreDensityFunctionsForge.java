package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.klinbee.moredensityfunctions.randomsamplers.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod(MoreDensityFunctionsConstants.MOD_ID)
public class MoreDensityFunctionsForge {

    private static final DeferredRegister<Codec<? extends DensityFunction>>
            DENSITY_FUNCTIONS = DeferredRegister.create(
            Registries.DENSITY_FUNCTION_TYPE,
            MoreDensityFunctionsConstants.MOD_NAMESPACE
    );
    private static final DeferredRegister<Codec<? extends RandomSampler>>
            RANDOM_SAMPLERS = DeferredRegister.create(
            MoreDensityFunctionsConstants.RANDOM_SAMPLER_TYPE,
            MoreDensityFunctionsConstants.MOD_NAMESPACE
    );

    public static final Supplier<IForgeRegistry<Codec<? extends RandomSampler>>>
            RANDOM_SAMPLER_REGISTRY_SUPPLIER = RANDOM_SAMPLERS.makeRegistry(
            () -> new RegistryBuilder<Codec<? extends RandomSampler>>()
                    .hasTags()
                    .disableSync()
                    .disableSaving()
    );

    public MoreDensityFunctionsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /// Register the Random Sampler Registry
        eventBus.addListener((DataPackRegistryEvent.NewRegistry event)
                -> event.dataPackRegistry(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC));

        /// Random Samplers
        RANDOM_SAMPLERS.register("beta", BetaSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("binomial", BinomialSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("exponential", ExponentialSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("gamma", GammaSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("geometric", GeometricSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("normal", NormalSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("poisson", PoissonSampler.CODEC::codec);
        RANDOM_SAMPLERS.register("uniform", UniformSampler.CODEC::codec);

        /// Density Functions
        DENSITY_FUNCTIONS.register("acos", ArcCosine.CODEC::codec);
        DENSITY_FUNCTIONS.register("asin", ArcSine.CODEC::codec);
        DENSITY_FUNCTIONS.register("atan", ArcTangent.CODEC::codec);
        DENSITY_FUNCTIONS.register("cache", Cache.CODEC::codec);
        DENSITY_FUNCTIONS.register("ceil", Ceil.CODEC::codec);
        DENSITY_FUNCTIONS.register("clamp", Clamp.CODEC::codec);
        DENSITY_FUNCTIONS.register("cos", Cosine.CODEC::codec);
        DENSITY_FUNCTIONS.register("derivative", Derivative.CODEC::codec);
        DENSITY_FUNCTIONS.register("div", Divide.CODEC::codec);
        DENSITY_FUNCTIONS.register("floor", Floor.CODEC::codec);
        DENSITY_FUNCTIONS.register("floor_div", FloorDivide.CODEC::codec);
        DENSITY_FUNCTIONS.register("floor_mod", FloorModulo.CODEC::codec);
        DENSITY_FUNCTIONS.register("gradient_magnitude", GradientMagnitude.CODEC::codec);
        DENSITY_FUNCTIONS.register("ieee_rem", IEEERemainder.CODEC::codec);
        DENSITY_FUNCTIONS.register("log", Log.CODEC::codec);
        DENSITY_FUNCTIONS.register("log2", Log2.CODEC::codec);
        DENSITY_FUNCTIONS.register("log2_floor", Log2Floor.CODEC::codec);
        DENSITY_FUNCTIONS.register("ln", NaturalLog.CODEC::codec);
        DENSITY_FUNCTIONS.register("negate", Negate.CODEC::codec);
        DENSITY_FUNCTIONS.register("polar_coords", PolarCoords.CODEC::codec);
        DENSITY_FUNCTIONS.register("power", Power.CODEC::codec);
        DENSITY_FUNCTIONS.register("profiler", Profiler.CODEC::codec);
        DENSITY_FUNCTIONS.register("reciprocal", Reciprocal.CODEC::codec);
        DENSITY_FUNCTIONS.register("rem", Remainder.CODEC::codec);
        DENSITY_FUNCTIONS.register("round", Round.CODEC::codec);
        DENSITY_FUNCTIONS.register("shift", Shift.CODEC::codec);
        DENSITY_FUNCTIONS.register("sigmoid", Sigmoid.CODEC::codec);
        DENSITY_FUNCTIONS.register("signum", Signum.CODEC::codec);
        DENSITY_FUNCTIONS.register("sine", Sine.CODEC::codec);
        DENSITY_FUNCTIONS.register("sqrt", SquareRoot.CODEC::codec);
        DENSITY_FUNCTIONS.register("subtract", Subtract.CODEC::codec);
        DENSITY_FUNCTIONS.register("tan", Tangent.CODEC::codec);
        DENSITY_FUNCTIONS.register("value_noise", ValueNoise.CODEC::codec);
        DENSITY_FUNCTIONS.register("vector_angle", VectorAngle.CODEC::codec);
        DENSITY_FUNCTIONS.register("x_clamped_gradient", XClampedGradient.CODEC::codec);
        DENSITY_FUNCTIONS.register("x", XPos.CODEC::codec);
        DENSITY_FUNCTIONS.register("y", YPos.CODEC::codec);
        DENSITY_FUNCTIONS.register("z_clamped_gradient", ZClampedGradient.CODEC::codec);
        DENSITY_FUNCTIONS.register("z", ZPos.CODEC::codec);

        /// Add registrations to eventBus
        DENSITY_FUNCTIONS.register(eventBus);
        RANDOM_SAMPLERS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}