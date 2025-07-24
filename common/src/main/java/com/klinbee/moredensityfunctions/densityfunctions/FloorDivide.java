package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record FloorDivide(DensityFunction numerator,
                          DensityFunction denominator,
                          double minOutput,
                          double maxOutput,
                          DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<FloorDivide> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorDivide::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorDivide::denominator),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(FloorDivide::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(FloorDivide::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(FloorDivide::errorArg)
                    ).apply(instance, FloorDivide::new)
            );

    public static final KeyDispatchDataCodec<FloorDivide> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        int numeratorValue = Mth.floor(numerator.compute(pos));
        int denominatorValue = Mth.floor(denominator.compute(pos));

        if (denominatorValue == 0) {
            return errorArg.compute(pos);
        }

        double result = StrictMath.floorDiv(numeratorValue, denominatorValue);

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new FloorDivide(
                        numerator,
                        denominator,
                        minOutput,
                        maxOutput,
                        errorArg
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
