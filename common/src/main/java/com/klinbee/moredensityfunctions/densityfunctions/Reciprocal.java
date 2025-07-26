package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Reciprocal(DensityFunction denominator,
                         double minOutput,
                         double maxOutput,
                         DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Reciprocal> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Reciprocal::denominator),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(Reciprocal::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(Reciprocal::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Reciprocal::errorArg)
                    ).apply(instance, Reciprocal::new)
            );

    public static final KeyDispatchDataCodec<Reciprocal> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double denominatorValue = denominator.compute(pos);

        if (denominatorValue == 0) {
            return errorArg.compute(pos);
        }

        double result = 1.0D / denominatorValue;

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Reciprocal(
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
