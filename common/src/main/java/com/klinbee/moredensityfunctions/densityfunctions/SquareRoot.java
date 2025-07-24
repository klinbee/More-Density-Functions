package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SquareRoot(DensityFunction arg,
                         double minOutput,
                         double maxOutput,
                         DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<SquareRoot> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(SquareRoot::arg),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(SquareRoot::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(SquareRoot::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(SquareRoot::errorArg)
                    ).apply(instance, SquareRoot::new)
            );
    public static final KeyDispatchDataCodec<SquareRoot> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    private static double eval(double density) {
        return StrictMath.sqrt(density);
    }

    @Override
    public double compute(FunctionContext pos) {
        double discriminantValue = arg.compute(pos);

        if (discriminantValue < 0) {
            return errorArg.compute(pos);
        }

        return eval(discriminantValue);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new SquareRoot(
                        arg,
                        minOutput,
                        maxOutput,
                        errorArg
                )
        );
    }

    @Override
    public double minValue() {
        if (arg.minValue() < 0) {
            return errorArg.minValue();
        }
        return eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        if (arg.maxValue() < 0) {
            return errorArg.maxValue();
        }
        return eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
