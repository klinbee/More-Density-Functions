package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Log(DensityFunction arg,
                  DensityFunction base,
                  double minOutput,
                  double maxOutput,
                  DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Log> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log::arg),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Log::base),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(Log::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(Log::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Log::errorArg)
                    ).apply(instance, Log::new)
            );

    public static final KeyDispatchDataCodec<Log> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "log";

    @Override
    public double compute(FunctionContext pos) {
        double argValue = arg.compute(pos);
        double baseValue = base.compute(pos);

        if (argValue <= 0.0D || baseValue <= 0.0D) {
            return errorArg.compute(pos);
        }
        if (baseValue == 1.0D) {
            return errorArg.compute(pos);
        }

        double result = StrictMath.log(argValue) / StrictMath.log(baseValue);

        return StrictMath.max(StrictMath.min(result, maxOutput), minOutput);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Log(
                        arg.mapAll(visitor),
                        base.mapAll(visitor),
                        minOutput,
                        maxOutput,
                        errorArg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double argMin = arg.minValue();

        double baseMin = base.minValue();
        double baseMax = base.maxValue();

        if (argMin <= 0.0 || baseMin <= 0.0 || (baseMin <= 1.0 && baseMax >= 1.0)) {
            return StrictMath.min(minOutput, errorArg.minValue());
        }

        return minOutput;
    }

    @Override
    public double maxValue() {
        double argMin = arg.minValue();

        double baseMin = base.minValue();
        double baseMax = base.maxValue();

        if (argMin <= 0.0 || baseMin <= 0.0 || (baseMin <= 1.0 && baseMax >= 1.0)) {
            return StrictMath.max(maxOutput, errorArg.maxValue());
        }

        return maxOutput;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}