package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Reciprocal(DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<Reciprocal> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Reciprocal::denominator)
                    ).apply(instance, Reciprocal::new)
            );

    public static final TypedCodec<Reciprocal> TYPED_CODEC = new TypedCodec<>("reciprocal", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double density) {
        return 1.0D / density;
    }

    @Override
    public double compute(FunctionContext pos) {
        return eval(denominator.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Reciprocal(
                        denominator.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double asymptoteLocation = 0.0D;

        // Lower bound is above `asymptoteLocation`, `minValue()` must be `eval(denomMax)`
        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();

        if (denomMin > asymptoteLocation) {
            return eval(denomMax);
        }

        // Upper bound is below `asymptoteLocation`, `minValue()` must be `eval(denomMax)`

        if (denomMax < asymptoteLocation) {
            return eval(denomMax);
        }

        // Range includes `asymptoteLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(asymptoteLocation)`
        return Double.NaN;
    }

    @Override
    public double maxValue() {
        double asymptoteLocation = 0.0D;

        // Lower bound is above `asymptoteLocation`, `maxValue()` must be `eval(denomMin)`
        double denomMin = denominator.minValue();

        if (denomMin > asymptoteLocation) {
            return eval(denomMin);
        }

        // Upper bound is below `asymptoteLocation`, `maxValue()` must be `eval(denomMin)`
        double denomMax = denominator.maxValue();

        if (denomMax < asymptoteLocation) {
            return eval(denomMin);
        }

        // Range includes `asymptoteLocation`, `maxValue()` cannot be guaranteed, but could be as high as `eval(asymptoteLocation)`
        return Double.NaN;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
