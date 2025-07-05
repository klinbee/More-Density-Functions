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

        DynamicRegistries.register(MoreDensityFunctionsConstants.RANDOM_SAMPLER, RandomSampler.CODEC);

        ///  Random Distributions ///
        registerRandomSampler("beta", BetaSampler.CODEC.codec());
        registerRandomSampler("binomial", BinomialSampler.CODEC.codec());
        registerRandomSampler("exponential", ExponentialSampler.CODEC.codec());
        registerRandomSampler("gamma", GammaSampler.CODEC.codec());
        registerRandomSampler("geometric", GeometricSampler.CODEC.codec());
        registerRandomSampler("normal", NormalSampler.CODEC.codec());
        registerRandomSampler("poisson", PoissonSampler.CODEC.codec());
        registerRandomSampler("uniform", UniformSampler.CODEC.codec());

        /// Density Functions ///
        registerDensityFunction("acos", ArcCosine.CODEC.codec());
        registerDensityFunction("asin",ArcSine.CODEC.codec());
        registerDensityFunction("atan", ArcTangent.CODEC.codec());
        registerDensityFunction("ceil", Ceil.CODEC.codec());
        registerDensityFunction("clamp", Clamp.CODEC.codec());
        registerDensityFunction("cos", Cosine.CODEC.codec());
        registerDensityFunction("derivative", DirectionalDerivative.CODEC.codec());
        registerDensityFunction("div", Divide.CODEC.codec());
        registerDensityFunction("floor", Floor.CODEC.codec());
        registerDensityFunction("floor_div", FloorDivide.CODEC.codec());
        registerDensityFunction("floor_mod", FloorModulo.CODEC.codec());
        registerDensityFunction("gradient_magnitude", GradientMagnitude.CODEC.codec());
        registerDensityFunction("ieee_rem", IEEERemainder.CODEC.codec());
        registerDensityFunction("log", Log.CODEC.codec());
        registerDensityFunction("log2", Log2.CODEC.codec());
        registerDensityFunction("log2_floor", Log2Floor.CODEC.codec());
        registerDensityFunction("ln", NaturalLog.CODEC.codec());
        registerDensityFunction("negate", Negate.CODEC.codec());
        registerDensityFunction("polar_coords", PolarCoords.CODEC.codec());
        registerDensityFunction("power", Power.CODEC.codec());
        registerDensityFunction("profiler", Profiler.CODEC.codec());
        registerDensityFunction("reciprocal", Reciprocal.CODEC.codec());
        registerDensityFunction("rem", Remainder.CODEC.codec());
        registerDensityFunction("round", Round.CODEC.codec());
        registerDensityFunction("shift_df", ShiftDensityFunction.CODEC.codec());
        registerDensityFunction("sigmoid", Sigmoid.CODEC.codec());
        registerDensityFunction("signum", Signum.CODEC.codec());
        registerDensityFunction("sine", Sine.CODEC.codec());
        registerDensityFunction("sqrt", SquareRoot.CODEC.codec());
        registerDensityFunction("subtract", Subtract.CODEC.codec());
        registerDensityFunction("tan", Tangent.CODEC.codec());
        registerDensityFunction("value_noise", ValueNoise.CODEC.codec());
        registerDensityFunction("vector_angle", VectorAngle.CODEC.codec());
        registerDensityFunction( "x_clamped_gradient", XClampedGradient.CODEC.codec());
        registerDensityFunction("x", XPos.CODEC.codec());
        registerDensityFunction("y", YPos.CODEC.codec());
        registerDensityFunction("z_clamped_gradient", ZClampedGradient.CODEC.codec());
        registerDensityFunction("z", ZPos.CODEC.codec());

        /// Unused
        MoreDensityFunctionsCommon.init();
    }

    /**
     * Registers DensityFunction types
     * Method for ease of reading
     * Not generic, because Java hates that (makes it really annoying)
     *
     * @param name: name of the DensityFunction type used for JSON
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
     * @param name: name of the RandomSampler type used for JSON
     * @param codec: the codec from the KeyDispatchDataCodec of the RandomSampler class
     */
    public void registerRandomSampler(String name, MapCodec<? extends RandomSampler> codec) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
        ResourceKey<MapCodec<? extends RandomSampler>> resourceKey = ResourceKey.create(RANDOM_SAMPLER_TYPE.key(), resourceLocation);
        Registry.register(RANDOM_SAMPLER_TYPE,resourceKey,codec);
    }

}