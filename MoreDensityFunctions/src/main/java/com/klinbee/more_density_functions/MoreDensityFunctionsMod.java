package com.klinbee.more_density_functions;

import com.klinbee.more_density_functions.density_function_types.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MoreDensityFunctionsMod implements ModInitializer {

    public static final double DEFAULT_ERROR = 0.0;
    public static final double DEFAULT_MAX_OUTPUT = 1.0;
    public static final double DEFAULT_MIN_OUTPUT = -1.0;
    public static final String NAMESPACE = "more_dfs";

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE, "x_clamped_gradient"), XClampedGradient.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"z_clamped_gradient"), ZClampedGradient.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"sine"), Sine.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"sqrt"), Sqrt.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"power"), Power.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"floor"), Floor.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"ceil"), Ceil.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"round"), Round.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"signum"), Signum.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"div"), Division.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"floor_div"), FloorDivision.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"mod"), Modulo.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"floor_mod"), FloorModulo.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"reciprocal"), Reciprocal.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"shift_df"), ShiftFunction.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"negate"), Negation.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"subtract"), Subtract.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"x"), XCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"y"), YCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"z"), ZCoord.CODEC.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(NAMESPACE,"simplex_noise"), SimplexNoise.CODEC.codec());
    }
}
