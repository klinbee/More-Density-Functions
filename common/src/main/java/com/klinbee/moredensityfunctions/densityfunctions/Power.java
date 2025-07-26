package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Power(DensityFunction base,
                    DensityFunction exponent,
                    double minOutput,
                    double maxOutput,
                    DensityFunction errorArg)
        implements DensityFunction {

    private static final MapCodec<Power> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Power::base),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("exponent").forGetter(Power::exponent),
                            Codec.DOUBLE.fieldOf("min_output").forGetter(Power::minOutput),
                            Codec.DOUBLE.fieldOf("max_output").forGetter(Power::maxOutput),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("error_argument").forGetter(Power::errorArg)
                    ).apply(instance, Power::new)
            );

    public static final KeyDispatchDataCodec<Power> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double exponentValue = exponent.compute(pos);
        double baseValue = base.compute(pos);

        if (exponentValue == 0.0D) {
            return 1.0D;
        }

        if (exponentValue == 1.0D) {
            return base.compute(pos);
        }

        double result = StrictMath.pow(baseValue, exponentValue);

        // Ain't no way I'm doin' all those cases, Power is messed up.
        if (!Double.isFinite(result)) {
            return errorArg.compute(pos);
        }

        return Math.max(Math.min(result, maxOutput), minOutput);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Power(
                        base.mapAll(visitor),
                        exponent.mapAll(visitor),
                        minOutput,
                        maxOutput,
                        errorArg.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        return Math.min(errorArg.minValue(), minOutput);
    }

    @Override
    public double maxValue() {
        return Math.max(errorArg.maxValue(), maxOutput);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
