package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record ArcSine(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<ArcSine> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(ArcSine::arg)
                    ).apply(instance, ArcSine::new)
            );

    public static final TypedCodec<ArcSine> TYPED_CODEC = new TypedCodec<>("asin", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double density) {
        return StrictMath.acos(density);
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
                new ArcSine(arg.mapAll(visitor))
        );
    }

    @Override
    public double minValue() {
        double rangeMinLocation = -1.0D;

        // Upper bound is below `rangeMinLocation`, `minValue()` must be `NaN`
        double argMax = arg.maxValue();

        if (argMax < rangeMinLocation) {
            return Double.NaN;
        }

        // Range includes `rangeMinLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(rangeMinLocation)`
        double argMin = arg.minValue();

        if (argMin < rangeMinLocation) {
            return -StrictMath.PI / 2.0D;
        }

        // Lower bound is above `rangeMinLocation`, `minValue()` must be `eval(argMin)`
        return eval(argMin);
    }

    @Override
    public double maxValue() {
        double rangeMaxLocation = 1.0D;

        // Lower bound is above `rangeMaxLocation`, `maxValue()` must be `NaN`
        double argMin = arg.minValue();

        if (argMin > rangeMaxLocation) {
            return Double.NaN;
        }

        // Range includes `rangeMaxLocation`, `maxValue()` cannot be guaranteed, but could be as low as `eval(rangeMaxLocation)`
        double argMax = arg.maxValue();

        if (argMax > rangeMaxLocation) {
            return StrictMath.PI / 2.0D;
        }

        // Upper bound is below `rangeMaxLocation`, `maxValue()` must be `eval(argMax)`
        return eval(argMax);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
