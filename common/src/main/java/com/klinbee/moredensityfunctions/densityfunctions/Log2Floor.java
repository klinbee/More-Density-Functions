package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Log2Floor(DensityFunction arg)
        implements DensityFunction {

    private static final MapCodec<Log2Floor> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Log2Floor::arg)
                    ).apply(instance, Log2Floor::new)
            );

    public static final TypedCodec<Log2Floor> TYPED_CODEC = new TypedCodec<>("log2_floor", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double density) {
        return MDFMath.log2Floor(density);
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
                new Log2Floor(
                        arg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double symmetricAsymptoteLocation = 0.0D;

        // Lower bound is above `symmetricAsymptoteLocation`, `minValue()` must be `eval(argMin)`
        double argMin = arg.minValue();

        if (argMin > symmetricAsymptoteLocation) {
            return eval(argMin);
        }

        // Upper bound is below `symmetricAsymptoteLocation`, `minValue()` must be `eval(argMax)`
        double argMax = arg.maxValue();

        if (argMax < symmetricAsymptoteLocation) {
            return eval(argMax);
        }

        // Range includes `symmetricAsymptoteLocation`, `minValue()` cannot be guaranteed, but could be as low as `eval(symmetricAsymptoteLocation)`
        return -1023;
    }

    @Override
    public double maxValue() {
        // Since `eval()` is an even function increasing away from 0, `maxValue()` must be `eval()` of the larger absolute value
        return eval(Mth.absMax(arg.minValue(), arg.maxValue()));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}