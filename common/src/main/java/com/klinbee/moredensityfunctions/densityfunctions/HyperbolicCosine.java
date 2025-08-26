package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record HyperbolicCosine(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<HyperbolicCosine> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(HyperbolicCosine::arg)
                    ).apply(instance, HyperbolicCosine::new)
            );

    public static final KeyDispatchDataCodec<HyperbolicCosine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "cosh";

    private static double eval(double density) {
        return StrictMath.cosh(density);
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
                new HyperbolicCosine(arg.mapAll(visitor))
        );
    }

    @Override
    public double minValue() {
        double globalMinLocation = 0.0D;

        // Lower bound is above `globalMinLocation`, `minValue()` must be `eval(argMin)`
        double argMin = arg.minValue();

        if (argMin > globalMinLocation) {
            return eval(argMin);
        }

        // Range includes `globalMinLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(globalMinLocation)`
        double argMax = arg.maxValue();

        if (argMax > globalMinLocation) {
            return 1.0D;

        }

        // Upper bound is below `globalMinLocation`, `minValue()` must be `eval(argMax)`
        return eval(argMax);
    }

    @Override
    public double maxValue() {
        // Since `eval()` is an even function increasing away from 0, `maxValue()` must be `eval()` of the larger absolute value
        return eval(Mth.absMax(arg.minValue(), arg.maxValue()));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
