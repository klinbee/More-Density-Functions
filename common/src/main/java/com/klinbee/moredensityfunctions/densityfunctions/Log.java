package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Log(DensityFunction arg,
                  DensityFunction base)
        implements DensityFunction {

    private static final MapCodec<Log> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log::arg),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Log::base)
                    ).apply(instance, Log::new)
            );

    public static final KeyDispatchDataCodec<Log> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "log";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.log(arg.compute(pos)) / StrictMath.log(base.compute(pos));
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
                        base.mapAll(visitor)
                )
        );
    }

    // TODO: base it off of base & max input; also use 0/1/negative cases
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