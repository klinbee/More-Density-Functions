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
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"asin"), ArcSine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"acos"), ArcCosine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"atan"), ArcTangent.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"ceil"), Ceil.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"cos"), Cosine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"div"), Divide.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"floor"), Floor.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"floor_div"), FloorDivide.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"floor_mod"), FloorModulo.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"ieee_rem"), IEEERemainder.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"negate"), Negate.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"polar_coords"), PolarCoords.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"power"), Power.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"reciprocal"), Reciprocal.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"rem"), Remainder.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"round"), Round.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"shift_df"), ShiftDensityFunction.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"sigmoid"), Sigmoid.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"signum"), Signum.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"sine"), Sine.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"sqrt"), SquareRoot.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"subtract"), Subtract.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"tan"), Tangent.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"vector_angle"), VectorAngle.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID, "x_clamped_gradient"), XClampedGradient.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"x"), XPos.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"y"), YPos.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"z_clamped_gradient"), ZClampedGradient.CODEC.codec());
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.tryBuild(Constants.MOD_ID,"z"), ZPos.CODEC.codec());
        MoreDensityFunctionsCommon.init();
    }
}
