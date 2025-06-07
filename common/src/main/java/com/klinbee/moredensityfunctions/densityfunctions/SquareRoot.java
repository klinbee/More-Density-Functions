package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;


public record SquareRoot(DensityFunction arg, Optional<Double> maxOutputHolder, double maxOutput,
                         Optional<Double> minOutputHolder, double minOutput,
                         Optional<DensityFunction> errorArgHolder,
                         DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<SquareRoot> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(SquareRoot::arg),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(SquareRoot::minOutputHolder),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(SquareRoot::maxOutputHolder),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(SquareRoot::errorArgHolder)
    ).apply(instance, (arg, maxOutputHolder,
                       minOutputHolder, errorArgHolder) ->
            new SquareRoot(arg,
                    maxOutputHolder, maxOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT),
                    minOutputHolder, minOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT),
                    errorArgHolder, errorArgHolder.orElse(DensityFunctions.constant(MoreDensityFunctionsConstants.DEFAULT_ERROR)))
    ));
    public static final KeyDispatchDataCodec<SquareRoot> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.sqrt(density);
    }

    @Override
    public double compute(FunctionContext pos) {
        double discriminantValue = this.arg.compute(pos);

        if (discriminantValue < 0) {
            return errorArg.compute(pos);
        }

        return this.eval(discriminantValue);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new SquareRoot(this.arg, this.minOutputHolder, this.minOutput, this.maxOutputHolder, this.maxOutput, this.errorArgHolder, this.errorArg));
    }

    public DensityFunction arg() {
        return arg;
    }

    public Optional<Double> minOutputHolder() {
        return minOutputHolder;
    }

    public Optional<Double> maxOutputHolder() {
        return maxOutputHolder;
    }

    public Optional<DensityFunction> errorArgHolder() {
        return errorArgHolder;
    }

    @Override
    public double minValue() {
        if (this.arg.minValue() < 0) {
            return errorArg.minValue();
        }
        return this.eval(this.arg.minValue());
    }

    @Override
    public double maxValue() {
        if (this.arg.maxValue() < 0) {
            return errorArg.maxValue();
        }
        return this.eval(this.arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
