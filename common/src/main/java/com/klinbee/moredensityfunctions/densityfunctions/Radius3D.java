package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Radius3D()
        implements DensityFunction {

    private static final MapCodec<Radius3D> MAP_CODEC = MapCodec.unit(new Radius3D());

    public static final KeyDispatchDataCodec<Radius3D> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "radius_3d";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.sqrt((pos.blockX() * pos.blockX()) + (pos.blockY() * pos.blockY()) + (pos.blockZ() * pos.blockZ()));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Radius3D()
        );
    }

    @Override
    public double minValue() {
        return 0.0D;
    }

    // The actual maximum is *very* slightly higher than this, but XZ_MAX_DOUBLE > normal Minecraft Range so this doesn't matter
    @Override
    public double maxValue() {
        return Mth.SQRT_OF_TWO * MoreDensityFunctionsConstants.XZ_MAX_DOUBLE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
