package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record NaturalLog(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<NaturalLog> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(NaturalLog::arg)
                    ).apply(instance, NaturalLog::new)
            );

    public static final KeyDispatchDataCodec<NaturalLog> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "ln";

    @Override
    public double compute(FunctionContext pos) {
        return eval(arg.compute(pos));
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
                        arg.mapAll(visitor)
                )
        );
    }

    //TODO: I think this is right??
    @Override
    public double minValue() {
        return arg.minValue() <= 0 ? Double.NEGATIVE_INFINITY : eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        return arg.maxValue() <= 0 ? Double.POSITIVE_INFINITY : eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}