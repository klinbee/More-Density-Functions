package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.klinbee.moredensityfunctions.distribution.*;

import com.mojang.serialization.Codec;
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

    public static final WritableRegistry<Codec<? extends RandomDistribution>> RANDOM_DISTRIBUTION_TYPE = FabricRegistryBuilder.createSimple(MoreDensityFunctionsConstants.RANDOM_DISTRIBUTION_TYPE).buildAndRegister();

    @Override
    public void onInitialize() {

        DynamicRegistries.register(MoreDensityFunctionsConstants.RANDOM_DISTRIBUTION, RandomDistribution.CODEC);

        registerRandomDistribution("beta", BetaDistribution.CODEC.codec());
        registerRandomDistribution("binomial", BinomialDistribution.CODEC.codec());
        registerRandomDistribution("exponential", ExponentialDistribution.CODEC.codec());
        registerRandomDistribution("gamma", GammaDistribution.CODEC.codec());
        registerRandomDistribution("geometric", GeometricDistribution.CODEC.codec());
        registerRandomDistribution("normal", NormalDistribution.CODEC.codec());
        registerRandomDistribution("poisson", PoissonDistribution.CODEC.codec());
        registerRandomDistribution("uniform", UniformDistribution.CODEC.codec());

        /// Density Functions ///
        registerDensityFunction("asin",ArcSine.CODEC.codec());
        registerDensityFunction("acos", ArcCosine.CODEC.codec());
        registerDensityFunction("atan", ArcTangent.CODEC.codec());
        registerDensityFunction("ceil", Ceil.CODEC.codec());
        registerDensityFunction("cellular_noise", CellularNoise.CODEC.codec());
        registerDensityFunction("cos", Cosine.CODEC.codec());
        registerDensityFunction("div", Divide.CODEC.codec());
        registerDensityFunction("floor", Floor.CODEC.codec());
        registerDensityFunction("floor_div", FloorDivide.CODEC.codec());
        registerDensityFunction("floor_mod", FloorModulo.CODEC.codec());
        registerDensityFunction("ieee_rem", IEEERemainder.CODEC.codec());
        registerDensityFunction("negate", Negate.CODEC.codec());
        registerDensityFunction("polar_coords", PolarCoords.CODEC.codec());
        registerDensityFunction("power", Power.CODEC.codec());
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
    public void registerDensityFunction(String name, Codec<? extends DensityFunction> codec) {
        ResourceLocation resourceLocation = new ResourceLocation(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
        ResourceKey<Codec<? extends DensityFunction>> resourceKey = ResourceKey.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE.key(), resourceLocation);
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE,resourceKey,codec);
    }

    /**
     * Registers RandomDistribution types
     * Method for ease of reading
     * Not generic, because Java hates that (makes it really annoying)
     *
     * @param name: name of the RandomDistribution type used for JSON
     * @param codec: the codec from the KeyDispatchDataCodec of the RandomDistribution class
     */
    public void registerRandomDistribution(String name, Codec<? extends RandomDistribution> codec) {
        ResourceLocation resourceLocation = new ResourceLocation(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
        ResourceKey<Codec<? extends RandomDistribution>> resourceKey = ResourceKey.create(RANDOM_DISTRIBUTION_TYPE.key(), resourceLocation);
        Registry.register(RANDOM_DISTRIBUTION_TYPE,resourceKey,codec);
    }

}