package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public record SquareRoot(DensityFunction arg, Optional<Double> maxOutput, Optional<Double> minOutput, Optional<DensityFunction> argError) implements DensityFunction {
    private static final MapCodec<SquareRoot> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("arg").forGetter(SquareRoot::arg), Codec.DOUBLE.optionalFieldOf("min_output").forGetter(SquareRoot::minOutput), Codec.DOUBLE.optionalFieldOf("max_output").forGetter(SquareRoot::maxOutput), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(SquareRoot::argError)).apply(instance, (SquareRoot::new)));
    public static final KeyDispatchDataCodec<SquareRoot> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.sqrt(density);
    }

    @Override
    public double compute(@NotNull FunctionContext pos) {
        double discriminantValue = this.arg.compute(pos);

        if (discriminantValue < 0) {
            if (argError.isPresent()) {
                return this.argError.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }

        return this.eval(discriminantValue);
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new SquareRoot(this.arg, this.minOutput, this.maxOutput, this.argError));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public Optional<Double> minOutput() {
        return minOutput;
    }

    @Override
    public Optional<Double> maxOutput() {
        return maxOutput;
    }

    @Override
    public Optional<DensityFunction> argError() {
        return argError;
    }

    @Override
    public double minValue() {
        if (this.arg.minValue() < 0) {
            if (argError.isPresent()) {
                return argError.get().minValue();
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }
        return this.eval(this.arg.minValue());
    }

    @Override
    public double maxValue() {
        if (this.arg.maxValue() < 0) {
            if (argError.isPresent()) {
                return argError.get().maxValue();
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }
        return this.eval(this.arg.maxValue());
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
