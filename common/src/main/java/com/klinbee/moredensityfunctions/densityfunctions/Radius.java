package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Radius()
        implements DensityFunction {

    private static final MapCodec<Radius> MAP_CODEC = MapCodec.unit(new Radius());

    public static final KeyDispatchDataCodec<Radius> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "radius";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.sqrt((pos.blockX() * pos.blockX()) + (pos.blockZ() * pos.blockZ()));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Radius()
        );
    }

    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return Mth.SQRT_OF_TWO * MoreDensityFunctionsConstants.XZ_MAX_DOUBLE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
