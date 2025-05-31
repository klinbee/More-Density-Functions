package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;


public record VectorAngle(DensityFunction arg1, DensityFunction arg2) implements DensityFunction {
    private static final MapCodec<VectorAngle> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(VectorAngle::arg1),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(VectorAngle::arg2)
    ).apply(instance, (VectorAngle::new)));
    public static final KeyDispatchDataCodec<VectorAngle> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.atan2(this.arg1.compute(pos), this.arg2.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new VectorAngle(this.arg1, this.arg2));
    }

    @Override
    public DensityFunction arg1() {
        return arg1;
    }

    @Override
    public DensityFunction arg2() {
        return arg2;
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
        return CODEC;
    }
}
