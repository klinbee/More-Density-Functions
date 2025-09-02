package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record FloorDivide(DensityFunction numerator,
                          DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<FloorDivide> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorDivide::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorDivide::denominator)
                    ).apply(instance, FloorDivide::new)
            );

    public static final TypedCodec<FloorDivide> TYPED_CODEC = new TypedCodec<>("floor_div", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double numerator, double denominator) {
        return Mth.floor(numerator / denominator);
    }

    @Override
    public double compute(FunctionContext pos) {
        return eval(numerator.compute(pos), denominator.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new FloorDivide(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double asymptoteLocation = 0.0D;

        // Lower bound is above `asymptoteLocation`, `minValue()` must be `eval(numMin, denomMax)`
        double denomMin = denominator.minValue();
        double denomMax = denominator.maxValue();
        double numMin = numerator.minValue();


        if (denomMin > asymptoteLocation) {
            return eval(numMin, denomMax);
        }

        // Upper bound is below `asymptoteLocation`, `minValue()` must be `eval(numMax, denomMax)`
        double numMax = numerator.maxValue();

        if (denomMax < asymptoteLocation) {
            return eval(numMax, denomMax);
        }

        // Range includes `asymptoteLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(asymptoteLocation)`
        return Double.NaN;
    }

    @Override
    public double maxValue() {
        double asymptoteLocation = 0.0D;

        // Lower bound is above `asymptoteLocation`, `maxValue()` must be `eval(numMax, denomMin)`
        double denomMin = denominator.minValue();
        double numMax = numerator.maxValue();


        if (denomMin > asymptoteLocation) {
            return eval(numMax, denomMin);
        }

        // Upper bound is below `asymptoteLocation`, `maxValue()` must be `eval(numMin, denomMin)`
        double denomMax = denominator.maxValue();
        double numMin = numerator.minValue();

        if (denomMax < asymptoteLocation) {
            return eval(numMin, denomMin);
        }

        // Range includes `asymptoteLocation`, `maxValue()` cannot be guaranteed, but could be as high as `eval(asymptoteLocation)`
        return Double.NaN;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
