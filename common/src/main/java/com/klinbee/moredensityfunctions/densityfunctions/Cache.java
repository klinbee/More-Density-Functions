package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

public record Cache(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<Cache> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Cache::arg)
                    ).apply(instance, Cache::new)
            );

    public static final KeyDispatchDataCodec<Cache> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "cache";

    private static final Map<DensityFunction, Long> DF_LAST_POS = Collections.synchronizedMap(new IdentityHashMap<>());
    private static final Map<DensityFunction, Double> DF_LAST_VALUE = Collections.synchronizedMap(new IdentityHashMap<>());

    @Override
    public double compute(FunctionContext pos) {
        int i = pos.blockX();
        int j = pos.blockZ();
        long k = ChunkPos.asLong(i, j);

        Long lastPos = DF_LAST_POS.get(arg);
        if (lastPos != null && lastPos == k) {
            return DF_LAST_VALUE.get(arg);
        } else {
            double result = arg.compute(pos);
            DF_LAST_POS.put(arg, k);
            DF_LAST_VALUE.put(arg, result);
            return result;
        }
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Cache(arg.mapAll(visitor))
        );
    }

    @Override
    public double minValue() {
        return arg.minValue();
    }

    @Override
    public double maxValue() {
        return arg.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
