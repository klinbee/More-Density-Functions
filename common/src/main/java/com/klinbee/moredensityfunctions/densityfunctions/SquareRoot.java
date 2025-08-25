package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SquareRoot(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<SquareRoot> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(SquareRoot::arg)
                    ).apply(instance, SquareRoot::new)
            );
    public static final KeyDispatchDataCodec<SquareRoot> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "sqrt";

    private static double eval(double density) {
        return StrictMath.sqrt(density);
    }

    @Override
    public double compute(FunctionContext pos) {
        return eval(arg.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new SquareRoot(
                        arg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        if (arg.minValue() < 0) { // Result would be NaN, but this is technically the lower bound besides that
            return 0;
        }
        return eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        if (arg.maxValue() < 0) { // Result would be NaN, but this is technically the lower bound besides that
            return 0;
        }
        return eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
