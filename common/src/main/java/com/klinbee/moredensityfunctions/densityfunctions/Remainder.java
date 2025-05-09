package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record Remainder(DensityFunction numerator, DensityFunction denominator, Optional<DensityFunction> argError) implements DensityFunction {
    private static final MapCodec<Remainder> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(Remainder::numerator), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(Remainder::denominator), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Remainder::argError)).apply(instance, (Remainder::new)));
    public static final KeyDispatchDataCodec<Remainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute( FunctionContext pos) {
        double numeratorValue = this.numerator.compute(pos);
        double denominatorValue = this.denominator.compute(pos);

        if (denominatorValue == 0) {
            if (argError.isPresent()) {
                return this.argError.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }

        return  numeratorValue % denominatorValue;
    }

    @Override
    public void fillArray(double  [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public  DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Remainder(this.numerator, this.denominator, this.argError));
    }

    public DensityFunction numerator() {
        return numerator;
    }

    public DensityFunction denominator() {
        return denominator;
    }

    @Override
    public Optional<DensityFunction> argError() {
        return argError;
    }

    @Override
    public double minValue() {
        if (argError.isPresent()) {
            return Math.min(this.argError.get().minValue(), Math.min(-Math.abs(this.denominator.minValue()), -Math.abs(this.denominator.maxValue())));
        }
        return Math.min(MoreDensityFunctionsConstants.DEFAULT_ERROR, Math.min(-Math.abs(this.denominator.minValue()), -Math.abs(this.denominator.maxValue())));
    }

    @Override
    public double maxValue() {
        if (argError.isPresent()) {
            return Math.max(this.argError.get().maxValue(), Math.max(Math.abs(this.denominator.minValue()), Math.abs(this.denominator.maxValue())));
        }
        return Math.max(MoreDensityFunctionsConstants.DEFAULT_ERROR, Math.max(Math.abs(this.denominator.minValue()), Math.abs(this.denominator.maxValue())));
    }

    @Override
    public  KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
