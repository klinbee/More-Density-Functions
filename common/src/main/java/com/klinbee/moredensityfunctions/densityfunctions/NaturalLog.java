package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record NaturalLog(DensityFunction arg,
                         double minOutput,
                         double maxOutput,
                         DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<NaturalLog> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(NaturalLog::arg),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(NaturalLog::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(NaturalLog::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(NaturalLog::errorArg)
                    ).apply(instance, NaturalLog::new)
            );

    public static final KeyDispatchDataCodec<NaturalLog> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double argValue = arg.compute(pos);

        if (argValue <= 0.0D) {
            return errorArg.compute(pos);
        }

        double result = eval(argValue);

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    private static double eval(double density) {
        return StrictMath.log(density);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new NaturalLog(
                        arg,
                        minOutput,
                        maxOutput,
                        errorArg
                )
        );
    }

    @Override
    public double minValue() {
        return arg.minValue() <= 0 ? errorArg.minValue() : eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        return arg.maxValue() <= 0 ? errorArg.maxValue() : eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}