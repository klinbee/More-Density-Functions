package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.concurrent.ConcurrentHashMap;

public record Resolver(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<Resolver> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Resolver::arg)
                    ).apply(instance, Resolver::new)
            );

    public static final TypedCodec<Resolver> TYPED_CODEC = new TypedCodec<>("resolver", KeyDispatchDataCodec.of(MAP_CODEC));

    private static final ConcurrentHashMap<Integer, DensityFunction> RESOLUTION_CACHE = new ConcurrentHashMap<>();

    private static DensityFunction unwrapHolder(DensityFunction df) {
        return df instanceof DensityFunctions.HolderHolder ?
                ((DensityFunctions.HolderHolder) df).function().value() :
                df;
    }

    @Override
    public double compute(FunctionContext pos) {
        return RESOLUTION_CACHE.get(arg.hashCode()).compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        int argHash = arg.hashCode();
        DensityFunction visited = RESOLUTION_CACHE.get(argHash);
        if (visited == null) {
            visited = visitor.apply(arg.mapAll(visitor)).mapAll(Resolver::unwrapHolder);
            RESOLUTION_CACHE.put(argHash, visited);
        }
        return visited;
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
        return TYPED_CODEC.codec();
    }
}
