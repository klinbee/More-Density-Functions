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

    // I *for some reason*, need to do this
    public static final Supplier<IForgeRegistry<Codec<? extends RandomSampler>>>
            RANDOM_SAMPLER_REGISTRY_SUPPLIER = RANDOM_SAMPLERS.makeRegistry(() ->
            new RegistryBuilder<Codec<? extends RandomSampler>>()
                    .hasTags()
                    .disableSync()
                    .disableSaving()
    );

    public MoreDensityFunctionsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /// Register the Random Sampler Registry
        eventBus.addListener((DataPackRegistryEvent.NewRegistry event) ->
                event.dataPackRegistry(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC));

        /// Random Samplers
        RANDOM_SAMPLERS.register(BetaSampler.NAME, BetaSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(BinomialSampler.NAME, BinomialSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(ExponentialSampler.NAME, ExponentialSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(GammaSampler.NAME, GammaSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(GeometricSampler.NAME, GeometricSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(NormalSampler.NAME, NormalSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(PoissonSampler.NAME, PoissonSampler.CODEC::codec);
        RANDOM_SAMPLERS.register(UniformSampler.NAME, UniformSampler.CODEC::codec);

        /// Density Functions
        DENSITY_FUNCTIONS.register(ArcCosine.NAME, ArcCosine.CODEC::codec);
        DENSITY_FUNCTIONS.register(ArcSine.NAME, ArcSine.CODEC::codec);
        DENSITY_FUNCTIONS.register(ArcTangent.NAME, ArcTangent.CODEC::codec);
        DENSITY_FUNCTIONS.register(Cache.NAME, Cache.CODEC::codec);
        DENSITY_FUNCTIONS.register(Ceil.NAME, Ceil.CODEC::codec);
        DENSITY_FUNCTIONS.register(Clamp.NAME, Clamp.CODEC::codec);
        DENSITY_FUNCTIONS.register(Cosine.NAME, Cosine.CODEC::codec);
        DENSITY_FUNCTIONS.register(Derivative.NAME, Derivative.CODEC::codec);
        DENSITY_FUNCTIONS.register(Divide.NAME, Divide.CODEC::codec);
        DENSITY_FUNCTIONS.register(Floor.NAME, Floor.CODEC::codec);
        DENSITY_FUNCTIONS.register(FloorDivide.NAME, FloorDivide.CODEC::codec);
        DENSITY_FUNCTIONS.register(FloorModulo.NAME, FloorModulo.CODEC::codec);
        DENSITY_FUNCTIONS.register(GradientMagnitude.NAME, GradientMagnitude.CODEC::codec);
        DENSITY_FUNCTIONS.register(IEEERemainder.NAME, IEEERemainder.CODEC::codec);
        DENSITY_FUNCTIONS.register(Log.NAME, Log.CODEC::codec);
        DENSITY_FUNCTIONS.register(Log2.NAME, Log2.CODEC::codec);
        DENSITY_FUNCTIONS.register(Log2Floor.NAME, Log2Floor.CODEC::codec);
        DENSITY_FUNCTIONS.register(NaturalLog.NAME, NaturalLog.CODEC::codec);
        DENSITY_FUNCTIONS.register(Negate.NAME, Negate.CODEC::codec);
        DENSITY_FUNCTIONS.register(PolarCoords.NAME, PolarCoords.CODEC::codec);
        DENSITY_FUNCTIONS.register(Power.NAME, Power.CODEC::codec);
        DENSITY_FUNCTIONS.register(Profiler.NAME, Profiler.CODEC::codec);
        DENSITY_FUNCTIONS.register(Reciprocal.NAME, Reciprocal.CODEC::codec);
        DENSITY_FUNCTIONS.register(Remainder.NAME, Remainder.CODEC::codec);
        DENSITY_FUNCTIONS.register(Round.NAME, Round.CODEC::codec);
        DENSITY_FUNCTIONS.register(Shift.NAME, Shift.CODEC::codec);
        DENSITY_FUNCTIONS.register(Sigmoid.NAME, Sigmoid.CODEC::codec);
        DENSITY_FUNCTIONS.register(Signum.NAME, Signum.CODEC::codec);
        DENSITY_FUNCTIONS.register(Sine.NAME, Sine.CODEC::codec);
        DENSITY_FUNCTIONS.register(SquareRoot.NAME, SquareRoot.CODEC::codec);
        DENSITY_FUNCTIONS.register(Subtract.NAME, Subtract.CODEC::codec);
        DENSITY_FUNCTIONS.register(Tangent.NAME, Tangent.CODEC::codec);
        DENSITY_FUNCTIONS.register(ValueNoise.NAME, ValueNoise.CODEC::codec);
        DENSITY_FUNCTIONS.register(VectorAngle.NAME, VectorAngle.CODEC::codec);
        DENSITY_FUNCTIONS.register(XClampedGradient.NAME, XClampedGradient.CODEC::codec);
        DENSITY_FUNCTIONS.register(XPos.NAME, XPos.CODEC::codec);
        DENSITY_FUNCTIONS.register(YPos.NAME, YPos.CODEC::codec);
        DENSITY_FUNCTIONS.register(ZClampedGradient.NAME, ZClampedGradient.CODEC::codec);
        DENSITY_FUNCTIONS.register(ZPos.NAME, ZPos.CODEC::codec);

        /// Add registrations to eventBus
        DENSITY_FUNCTIONS.register(eventBus);
        RANDOM_SAMPLERS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}