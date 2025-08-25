package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Divide(DensityFunction numerator,
                     DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<Divide> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Divide::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Divide::denominator)
                    ).apply(instance, Divide::new)
            );

    public static final KeyDispatchDataCodec<Divide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "div";

    @Override
    public double compute(FunctionContext pos) {
        return numerator.compute(pos) / denominator.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Divide(
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
