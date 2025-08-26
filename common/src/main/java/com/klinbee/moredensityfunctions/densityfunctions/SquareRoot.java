package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record SquareRoot(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<SquareRoot> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(SquareRoot::arg)
                    ).apply(instance, SquareRoot::new)
            );
    public static final KeyDispatchDataCodec<SquareRoot> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "sqrt";

    private static double eval(double density) {
        return StrictMath.sqrt(density);
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
                new SquareRoot(
                        arg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double domainMinLocation = 0.0D;

        // Lower bound is above `domainMinLocation`, `minValue()` must be `eval(argMin)`
        double argMin = arg.minValue();

        if (argMin > domainMinLocation) {
            return eval(argMin);
        }

        // Upper bound is below `domainMinLocation`, `minValue()` must be `NaN`
        double argMax = arg.maxValue();

        if (argMax < domainMinLocation) {
            return Double.NaN;
        }

        // Range includes `domainMinLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(domainMinLocation)`
        return 0.0D;
    }

    @Override
    public double maxValue() {
        // Unlike `minValue()`, `maxValue()` can never be a result of `arg.minValue()`
        return eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
