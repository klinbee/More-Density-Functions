package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Remainder(DensityFunction numerator,
                        DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<Remainder> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Remainder::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Remainder::denominator)
                    ).apply(instance, Remainder::new)
            );

    public static final KeyDispatchDataCodec<Remainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "rem";

    @Override
    public double compute(FunctionContext pos) {
        return numerator.compute(pos) % denominator.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Remainder(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    //TODO: help
    @Override
    public double minValue() {
        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();

        // Most negative possible: -|y|/2 where |y| is maximized
        double largestMagnitude = Math.max(Math.abs(denomMin), Math.abs(denomMax));

        return -largestMagnitude / 2.0D;
    }

    @Override
    public double maxValue() {
        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();

        // Most positive possible: |y|/2 where |y| is maximized
        double largestMagnitude = Math.max(Math.abs(denomMin), Math.abs(denomMax));

        return largestMagnitude / 2.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
