package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Tangent(DensityFunction arg,
                      DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Tangent> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Tangent::arg),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Tangent::errorArg)
            ).apply(instance, Tangent::new)
    );

    public static final KeyDispatchDataCodec<Tangent> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double argValue = arg.compute(pos);
        double result = StrictMath.tan(argValue);

        if (!Double.isFinite(result)) {
            return errorArg.compute(pos);
        }

        return result;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Tangent(
                        arg,
                        errorArg
                )
        );
    }

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
        return CODEC;
    }
}
