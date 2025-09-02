package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record CubeRoot(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<CubeRoot> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(CubeRoot::arg)
                    ).apply(instance, CubeRoot::new)
            );

    public static final TypedCodec<CubeRoot> TYPED_CODEC = new TypedCodec<>("cbrt", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double density) {
        return StrictMath.cbrt(density);
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
                new CubeRoot(
                        arg.mapAll(visitor)
                )
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
        return TYPED_CODEC.codec();
    }
}
