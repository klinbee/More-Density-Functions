package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;

public record Log(DensityFunction arg, DensityFunction base, Optional<Double> maxOutputHolder, double maxOutput,
                  Optional<Double> minOutputHolder, double minOutput, Optional<DensityFunction> errorArgHolder,
                  DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<Log> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log::arg),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Log::base),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Log::minOutputHolder),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Log::maxOutputHolder),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Log::errorArgHolder)
    ).apply(instance, (arg, base, maxOutputHolder,
                       minOutputHolder, errorArgHolder) ->
            new Log(arg, base,
                    maxOutputHolder, maxOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT),
                    minOutputHolder, minOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT),
                    errorArgHolder, errorArgHolder.orElse(DensityFunctions.constant(MoreDensityFunctionsConstants.DEFAULT_ERROR)))
    ));
    public static final KeyDispatchDataCodec<Log> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double argValue = this.arg.compute(pos);
        double baseValue = this.base.compute(pos);

        if (argValue <= 0.0D || baseValue <= 0.0D) {
            return errorArg.compute(pos);
        }
        if (baseValue == 1.0D) {
            return errorArg.compute(pos);
        }

        double result = StrictMath.log(argValue) / StrictMath.log(baseValue);

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Log(this.arg, this.base, this.minOutputHolder, this.minOutput, this.maxOutputHolder, this.maxOutput, this.errorArgHolder, this.errorArg));
    }

    @Override
    public double minValue() {
        return -Double.MAX_VALUE;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}