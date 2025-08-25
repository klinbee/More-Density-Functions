package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Reciprocal(DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<Reciprocal> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Reciprocal::denominator)
                    ).apply(instance, Reciprocal::new)
            );

    public static final KeyDispatchDataCodec<Reciprocal> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "reciprocal";

    @Override
    public double compute(FunctionContext pos) {
        return 1.0D / denominator.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Reciprocal(
                        denominator.mapAll(visitor)
                )
        );
    }

    //TODO: hgelp?
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
