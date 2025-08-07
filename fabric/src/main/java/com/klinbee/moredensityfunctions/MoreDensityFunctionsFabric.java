package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.densityfunctions.*;

import com.klinbee.moredensityfunctions.randomsamplers.*;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;

public class MoreDensityFunctionsFabric implements ModInitializer {

    public static final WritableRegistry<MapCodec<? extends RandomSampler>> RANDOM_SAMPLER_TYPE = FabricRegistryBuilder.createSimple(MoreDensityFunctionsConstants.RANDOM_SAMPLER_TYPE).buildAndRegister();

    @Override
    public void onInitialize() {

        /// Register the RandomSampler Registry
        DynamicRegistries.register(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC);

        ///  Random Distributions ///
        registerRandomSampler(BetaSampler.NAME, BetaSampler.CODEC.codec());
        registerRandomSampler(BinomialSampler.NAME, BinomialSampler.CODEC.codec());
        registerRandomSampler(ExponentialSampler.NAME, ExponentialSampler.CODEC.codec());
        registerRandomSampler(GammaSampler.NAME, GammaSampler.CODEC.codec());
        registerRandomSampler(GeometricSampler.NAME, GeometricSampler.CODEC.codec());
        registerRandomSampler(NormalSampler.NAME, NormalSampler.CODEC.codec());
        registerRandomSampler(PoissonSampler.NAME, PoissonSampler.CODEC.codec());
        registerRandomSampler(UniformSampler.NAME, UniformSampler.CODEC.codec());

        /// Density Functions ///
        registerDensityFunction(ArcCosine.NAME, ArcCosine.CODEC.codec());
        registerDensityFunction(ArcSine.NAME, ArcSine.CODEC.codec());
        registerDensityFunction(ArcTangent.NAME, ArcTangent.CODEC.codec());
        registerDensityFunction(Cache.NAME, Cache.CODEC.codec());
        registerDensityFunction(Ceil.NAME, Ceil.CODEC.codec());
        registerDensityFunction(Clamp.NAME, Clamp.CODEC.codec());
        registerDensityFunction(Cosine.NAME, Cosine.CODEC.codec());
        registerDensityFunction(Derivative.NAME, Derivative.CODEC.codec());
        registerDensityFunction(Divide.NAME, Divide.CODEC.codec());
        registerDensityFunction(Floor.NAME, Floor.CODEC.codec());
        registerDensityFunction(FloorDivide.NAME, FloorDivide.CODEC.codec());
        registerDensityFunction(FloorModulo.NAME, FloorModulo.CODEC.codec());
        registerDensityFunction(GradientMagnitude.NAME, GradientMagnitude.CODEC.codec());
        registerDensityFunction(IEEERemainder.NAME, IEEERemainder.CODEC.codec());
        registerDensityFunction(Log.NAME, Log.CODEC.codec());
        registerDensityFunction(Log2.NAME, Log2.CODEC.codec());
        registerDensityFunction(Log2Floor.NAME, Log2Floor.CODEC.codec());
        registerDensityFunction(NaturalLog.NAME, NaturalLog.CODEC.codec());
        registerDensityFunction(Negate.NAME, Negate.CODEC.codec());
        registerDensityFunction(PolarCoords.NAME, PolarCoords.CODEC.codec());
        registerDensityFunction(Power.NAME, Power.CODEC.codec());
        registerDensityFunction(Profiler.NAME, Profiler.CODEC.codec());
        registerDensityFunction(Reciprocal.NAME, Reciprocal.CODEC.codec());
        registerDensityFunction(Remainder.NAME, Remainder.CODEC.codec());
        registerDensityFunction(Round.NAME, Round.CODEC.codec());
        registerDensityFunction(Shift.NAME, Shift.CODEC.codec());
        registerDensityFunction(Sigmoid.NAME, Sigmoid.CODEC.codec());
        registerDensityFunction(Signum.NAME, Signum.CODEC.codec());
        registerDensityFunction(Sine.NAME, Sine.CODEC.codec());
        registerDensityFunction(SquareRoot.NAME, SquareRoot.CODEC.codec());
        registerDensityFunction(Subtract.NAME, Subtract.CODEC.codec());
        registerDensityFunction(Tangent.NAME, Tangent.CODEC.codec());
        registerDensityFunction(ValueNoise.NAME, ValueNoise.CODEC.codec());
        registerDensityFunction(VectorAngle.NAME, VectorAngle.CODEC.codec());
        registerDensityFunction(XClampedGradient.NAME, XClampedGradient.CODEC.codec());
        registerDensityFunction(XPos.NAME, XPos.CODEC.codec());
        registerDensityFunction(YPos.NAME, YPos.CODEC.codec());
        registerDensityFunction(ZClampedGradient.NAME, ZClampedGradient.CODEC.codec());
        registerDensityFunction(ZPos.NAME, ZPos.CODEC.codec());
    }

    /**
     * Registers DensityFunction types
     * Method for ease of reading
     * Not generic, because Java hates that (makes it really annoying)
     *
     * @param name:  name of the DensityFunction type used for JSON
     * @param codec: the codec from the KeyDispatchDataCodec of the DensityFunction class
     */
    public void registerDensityFunction(String name, MapCodec<? extends DensityFunction> codec) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
        ResourceKey<MapCodec<? extends DensityFunction>> resourceKey = ResourceKey.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE.key(), resourceLocation);
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE,resourceKey,codec);
    }

    /**
     * Registers RandomSampler types
     * Method for ease of reading
     * Not generic, because Java hates that (makes it really annoying)
     *
     * @param name:  name of the RandomSampler type used for JSON
     * @param codec: the codec from the KeyDispatchDataCodec of the RandomSampler class
     */
    public void registerRandomSampler(String name, MapCodec<? extends RandomSampler> codec) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
        ResourceKey<MapCodec<? extends RandomSampler>> resourceKey = ResourceKey.create(RANDOM_SAMPLER_TYPE.key(), resourceLocation);
        Registry.register(RANDOM_SAMPLER_TYPE,resourceKey,codec);
    }
}