package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Log2Floor(DensityFunction arg,
                        double minOutput,
                        double maxOutput,
                        DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Log2Floor> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log2Floor::arg),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(Log2Floor::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(Log2Floor::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Log2Floor::errorArg)
                    ).apply(instance, Log2Floor::new)
            );

    public static final KeyDispatchDataCodec<Log2Floor> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "log2_floor";

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
        long bits = Double.doubleToLongBits(density);
        return (int) ((bits >>> 52) & 0x7FF) - 1023; // Exponent
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Log2Floor(
                        arg.mapAll(visitor),
                        minOutput,
                        maxOutput,
                        errorArg.mapAll(visitor)
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