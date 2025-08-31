package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record Resolver(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<Resolver> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Resolver::arg)
                    ).apply(instance, Resolver::create)
            );

    public static final TypedCodec<Resolver> TYPED_CODEC = new TypedCodec<>("resolver", KeyDispatchDataCodec.of(MAP_CODEC));

    private static Resolver create(DensityFunction arg) {
        return new Resolver(resolve(arg));
    }

    private static DensityFunction resolve(DensityFunction df) {
        DensityFunction unwrapped = unwrapHolder(df);
        return unwrapped.mapAll(Resolver::resolve);
    }

    private static DensityFunction unwrapHolder(DensityFunction df) {
        return df instanceof DensityFunctions.HolderHolder ?
                unwrapHolder(df) :
                df;
    }

    @Override
    public double compute(FunctionContext pos) {
        return arg.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Resolver(arg.mapAll(visitor))
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
        return TYPED_CODEC.codec();
    }
}
