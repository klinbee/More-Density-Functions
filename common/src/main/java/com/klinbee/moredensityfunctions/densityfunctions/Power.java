package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;

public record Power(DensityFunction base, DensityFunction exponent, Optional<Double> maxOutputHolder, double maxOutput,
                    Optional<Double> minOutputHolder, double minOutput, Optional<DensityFunction> errorArgHolder,
                    DensityFunction errorArg) implements DensityFunction {
    private static final MapCodec<Power> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base").forGetter(Power::base),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("exponent").forGetter(Power::exponent),
            Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Power::minOutputHolder),
            Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Power::maxOutputHolder),
            DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Power::errorArgHolder)
    ).apply(instance, (base, exponent, maxOutputHolder,
                       minOutputHolder, errorArgHolder) ->
            new Power(base, exponent,
                    maxOutputHolder, maxOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MAX_OUTPUT),
                    minOutputHolder, minOutputHolder.orElse(MoreDensityFunctionsConstants.DEFAULT_MIN_OUTPUT),
                    errorArgHolder, errorArgHolder.orElse(DensityFunctions.constant(MoreDensityFunctionsConstants.DEFAULT_ERROR)))
    ));
    public static final KeyDispatchDataCodec<Power> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        double exponentValue = this.exponent.compute(pos);
        double baseValue = this.base.compute(pos);

        if (exponentValue == 0.0D) {
            return 1.0D;
        }

        if (exponentValue == 1.0D) {
            return this.base.compute(pos);
        }

        double result = StrictMath.pow(baseValue, exponentValue);

        // Ain't no way I'm doin' all those cases, Power is messed up.
        if (Double.isNaN(result)) {
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
        return visitor.apply(new Power(this.base, this.exponent, this.minOutputHolder, this.minOutput, this.maxOutputHolder, this.maxOutput, this.errorArgHolder, this.errorArg));
    }

    public DensityFunction base() {
        return base;
    }

    public DensityFunction exponent() {
        return exponent;
    }

    public Optional<Double> minOutputHolder() {
        return minOutputHolder;
    }

    public Optional<Double> maxOutputHolder() {
        return maxOutputHolder;
    }

    public Optional<DensityFunction> errorArgHolder() {
        return errorArgHolder;
    }

    @Override
    public double minValue() {
        // This is not happening.
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        // This is not happening.
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
