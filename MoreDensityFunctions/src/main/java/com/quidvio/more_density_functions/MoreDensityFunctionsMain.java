package com.quidvio.more_density_functions;

import com.quidvio.more_density_functions.density_function_types.Sine;
import com.quidvio.more_density_functions.density_function_types.Sqrt;
import com.quidvio.more_density_functions.density_function_types.XClampedGradient;
import com.quidvio.more_density_functions.density_function_types.ZClampedGradient;
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
    }
}
