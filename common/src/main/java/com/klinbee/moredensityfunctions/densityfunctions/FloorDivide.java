package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record FloorDivide(DensityFunction numerator,
                          DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<FloorDivide> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorDivide::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorDivide::denominator)
                    ).apply(instance, FloorDivide::new)
            );

    public static final KeyDispatchDataCodec<FloorDivide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "floor_div";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.floorDiv(Mth.floor(numerator.compute(pos)), Mth.floor(denominator.compute(pos)));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new FloorDivide(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    // TODO:
    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
