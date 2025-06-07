package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;


public record Reciprocal(DensityFunction denominator, Optional<Double> maxOutputHolder, double maxOutput,
                         Optional<Double> minOutputHolder, double minOutput,
                         Optional<DensityFunction> errorArgHolder,
                         DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<Reciprocal> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Reciprocal::denominator),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Reciprocal::minOutputHolder),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Reciprocal::maxOutputHolder),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Reciprocal::errorArgHolder)
    ).apply(instance, (denominator, maxOutputHolder,
                       minOutputHolder, errorArgHolder) ->
            new Reciprocal(denominator,
                    maxOutputHolder, maxOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT),
                    minOutputHolder, minOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT),
                    errorArgHolder, errorArgHolder.orElse(DensityFunctions.constant(MoreDensityFunctionsConstants.DEFAULT_ERROR)))
    ));
    public static final KeyDispatchDataCodec<Reciprocal> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double denominatorValue = this.denominator.compute(pos);

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
        return visitor.apply(new Reciprocal(this.denominator, this.minOutputHolder, this.minOutput, this.maxOutputHolder, this.maxOutput, this.errorArgHolder, this.errorArg));
    }

    @Override
    public double minValue() {
        return Math.min(this.errorArg.minValue(), this.minOutput);
    }

    @Override
    public double maxValue() {
        return Math.max(this.errorArg.maxValue(), this.maxOutput);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
