package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record HyperbolicSine(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<HyperbolicSine> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(HyperbolicSine::arg)
                    ).apply(instance, HyperbolicSine::new)
            );

    public static final KeyDispatchDataCodec<HyperbolicSine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "sinh";

    private static double eval(double density) {
        return StrictMath.sinh(density);
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
                new HyperbolicSine(arg.mapAll(visitor))
        );
    }

    @Override
    public double minValue() {
        return eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        return eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
