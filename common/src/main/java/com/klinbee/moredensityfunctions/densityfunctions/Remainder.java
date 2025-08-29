package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
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

    // Due to periodic nature, I'm using global min/max
    @Override
    public double minValue() {
        // The sign is the same as the numerator, if negative, the minimum is the negative absolute maximum of the denominator's range
        // Otherwise, it is simply 0
        return (numerator.minValue() < 0) ?
                -Mth.absMax(denominator.minValue(), denominator.maxValue()) :
                0.0D;
    }

    @Override
    public double maxValue() {
        // The sign is the same as the numerator, if positive, the maximum is the absolute maximum of the denominator's range
        // Otherwise, it is simply 0
        return (numerator.maxValue() > 0) ?
                Mth.absMax(denominator.minValue(), denominator.maxValue()) :
                0.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
