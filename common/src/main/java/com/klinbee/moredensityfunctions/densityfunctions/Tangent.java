package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Tangent(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<Tangent> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Tangent::arg)
            ).apply(instance, Tangent::new)
    );

    public static final TypedCodec<Tangent> TYPED_CODEC = new TypedCodec<>("tan", KeyDispatchDataCodec.of(MAP_CODEC));

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.tan(arg.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Tangent(
                        arg.mapAll(visitor)
                )
        );
    }

    // Due to periodic nature, I'm using global min/max (though tangent never hits these)
    @Override
    public double minValue() {
        return -Double.MAX_VALUE;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
