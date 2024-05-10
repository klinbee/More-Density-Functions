package com.quidvio.more_density_functions;

import com.quidvio.more_density_functions.density_function_types.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MoreDensityFunctionsMain implements ModInitializer {


    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","x_clamped_gradient"), XClampedGradient.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","z_clamped_gradient"), ZClampedGradient.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","sine"), Sine.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","sqrt"), Sqrt.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","power"), Power.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","floor"), Floor.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","ceil"), Ceil.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","round"), Round.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","floor_div"), FloorDivision.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","mod"), Modulo.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","reciprocal"), Reciprocal.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","shift_df"), ShiftFunction.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","negate"), Negation.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","subtract"), Subtract.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","x"), XCoord.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","y"), YCoord.CODEC.codec());
        Registry.register(Registry.REGISTRIES.DENSITY_FUNCTION_TYPE, new Identifier("more_dfs","z"), ZCoord.CODEC.codec());

    }

}
