package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record NaturalLog(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<NaturalLog> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(NaturalLog::arg)
                    ).apply(instance, NaturalLog::new)
            );

    public static final TypedCodec<NaturalLog> TYPED_CODEC = new TypedCodec<>("ln", KeyDispatchDataCodec.of(MAP_CODEC));

    @Override
    public double compute(FunctionContext pos) {
        return eval(arg.compute(pos));
    }

    private static double eval(double density) {
        return StrictMath.log(density);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new NaturalLog(
                        arg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double asymptoteLocation = 0.0D;

        // Lower bound is above `asymptoteLocation`, `minValue()` must be `eval(argMin)`
        double argMin = arg.minValue();

        if (argMin > asymptoteLocation) {
            return eval(argMin);
        }

        // Upper bound is below `asymptoteLocation`, `minValue()` must be `NaN`
        double argMax = arg.maxValue();

        if (argMax < asymptoteLocation) {
            return Double.NaN;
        }

        // Range includes `asymptoteLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(asymptoteLocation)`
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        // Unlike `minValue()`, `maxValue()` can never be a result of `arg.minValue()`
        return eval(arg.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}