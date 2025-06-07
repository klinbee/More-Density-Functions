package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;

public record Log2(DensityFunction arg, Optional<Double> maxOutputHolder, double maxOutput,
                   Optional<Double> minOutputHolder, double minOutput, Optional<DensityFunction> errorArgHolder,
                   DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<Log2> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log2::arg),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Log2::minOutputHolder),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Log2::maxOutputHolder),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Log2::errorArgHolder)
    ).apply(instance, (arg, maxOutputHolder,
                       minOutputHolder, errorArgHolder) ->
            new Log2(arg,
                    maxOutputHolder, maxOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT),
                    minOutputHolder, minOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT),
                    errorArgHolder, errorArgHolder.orElse(DensityFunctions.constant(MoreDensityFunctionsConstants.DEFAULT_ERROR)))
    ));
    public static final KeyDispatchDataCodec<Log2> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double argValue = this.arg.compute(pos);

        if (argValue <= 0.0D) {
            return errorArg.compute(pos);
        }

        double result = this.eval(argValue);

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    public double eval(double density) {
        return StrictMath.log(density) * MDFUtil.LOG2_E;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Log2(this.arg, this.minOutputHolder, this.minOutput, this.maxOutputHolder, this.maxOutput, this.errorArgHolder, this.errorArg));
    }

    @Override
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
        return arg.minValue() <= 0 ? errorArg.minValue() : this.eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        return arg.maxValue() <= 0 ? errorArg.maxValue() : this.eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}