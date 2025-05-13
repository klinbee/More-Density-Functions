package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class MoreDensityFunctionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"asin"), ArcSine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"acos"), ArcCosine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"atan"), ArcTangent.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"ceil"), Ceil.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"cos"), Cosine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"div"), Divide.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"floor"), Floor.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"floor_div"), FloorDivide.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"floor_mod"), FloorModulo.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"ieee_rem"), IEEERemainder.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"negate"), Negate.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"polar_coords"), PolarCoords.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"power"), Power.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"reciprocal"), Reciprocal.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"rem"), Remainder.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"round"), Round.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"shift_df"), ShiftDensityFunction.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"sigmoid"), Sigmoid.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"signum"), Signum.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"sine"), Sine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"sqrt"), SquareRoot.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"subtract"), Subtract.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"tan"), Tangent.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"vector_angle"), VectorAngle.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE, "x_clamped_gradient"), XClampedGradient.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"x"), XPos.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"y"), YPos.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"z_clamped_gradient"), ZClampedGradient.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(MoreDensityFunctionsConstants.MOD_NAMESPACE,"z"), ZPos.CODEC.codec());
        MoreDensityFunctionsCommon.init();
    }
}