package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Power(DensityFunction base,
                    DensityFunction exponent)
        implements DensityFunction {

    private static final MapCodec<Power> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Power::base),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("exponent").forGetter(Power::exponent)
                    ).apply(instance, Power::new)
            );

    public static final KeyDispatchDataCodec<Power> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "power";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.pow(base.compute(pos), exponent.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Power(
                        base.mapAll(visitor),
                        exponent.mapAll(visitor)
                )
        );
    }

    // TODO: help?
    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
