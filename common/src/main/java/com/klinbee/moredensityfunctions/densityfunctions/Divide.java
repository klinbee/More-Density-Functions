package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Divide(DensityFunction numerator,
                     DensityFunction denominator,
                     double minOutput,
                     double maxOutput,
                     DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Divide> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Divide::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Divide::denominator),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(Divide::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(Divide::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Divide::errorArg)
                    ).apply(instance, Divide::new)
            );

    public static final KeyDispatchDataCodec<Divide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "div";

    @Override
    public double compute(FunctionContext pos) {
        double numeratorValue = numerator.compute(pos);
        double denominatorValue = denominator.compute(pos);

        if (denominatorValue == 0.0D) {
            return errorArg.compute(pos);
        }

        double result = numeratorValue / denominatorValue;

        return Math.max(Math.min(result, maxOutput), minOutput);
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
                        denominator.mapAll(visitor),
                        minOutput,
                        maxOutput,
                        errorArg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        return Math.min(errorArg.minValue(), minOutput);
    }

    @Override
    public double maxValue() {
        return Math.max(errorArg.maxValue(), maxOutput);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
