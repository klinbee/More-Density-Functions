package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Subtract(DensityFunction arg1,
                       DensityFunction arg2)
        implements DensityFunction {

    private static final MapCodec<Subtract> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(Subtract::arg1),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(Subtract::arg2)
                    ).apply(instance, Subtract::new)
            );

    public static final TypedCodec<Subtract> TYPED_CODEC = new TypedCodec<>("subtract", KeyDispatchDataCodec.of(MAP_CODEC));

    @Override
    public double compute(FunctionContext pos) {
        return arg1.compute(pos) - arg2.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Subtract(
                        arg1.mapAll(visitor),
                        arg2.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        return arg1.minValue() - arg2.maxValue();
    }

    @Override
    public double maxValue() {
        return arg1.maxValue() - arg2.minValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
