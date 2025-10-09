package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record PolarCoords()
        implements DensityFunction {

    private static final MapCodec<PolarCoords> MAP_CODEC = MapCodec.unit(new PolarCoords());

    public static final TypedCodec<PolarCoords> TYPED_CODEC = new TypedCodec<>("polar_coords", KeyDispatchDataCodec.of(MAP_CODEC));

    @Override
    public double compute(FunctionContext pos) {
        return MDFMath.atan2(pos.blockX(), pos.blockZ());
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new PolarCoords()
        );
    }

    @Override
    public double minValue() {
        return -StrictMath.PI;
    }

    @Override
    public double maxValue() {
        return StrictMath.PI;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
