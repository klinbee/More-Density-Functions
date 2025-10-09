package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record HyperbolicTangent(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<HyperbolicTangent> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(HyperbolicTangent::arg)
                    ).apply(instance, HyperbolicTangent::new)
            );

    public static final TypedCodec<HyperbolicTangent> TYPED_CODEC = new TypedCodec<>("tanh", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double density) {
        return MDFMath.htan(density);
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
                new HyperbolicTangent(arg.mapAll(visitor))
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
