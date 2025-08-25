package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record OrElse(DensityFunction arg,
                     DensityFunction fallback)
        implements DensityFunction {

    private static final MapCodec<OrElse> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(OrElse::arg),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("fallback").forGetter(OrElse::fallback)
                    ).apply(instance, OrElse::new)
            );

    public static final KeyDispatchDataCodec<OrElse> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "or_else";

    @Override
    public double compute(FunctionContext pos) {
        double result = arg.compute(pos);
        return Double.isFinite(result) ?
                result :
                fallback.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new OrElse(
                        arg.mapAll(visitor),
                        fallback.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        return StrictMath.min(arg.minValue(), fallback.minValue());
    }

    @Override
    public double maxValue() {
        return StrictMath.max(arg.maxValue(), fallback.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
