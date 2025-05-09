package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record FloorModulo(DensityFunction numerator, DensityFunction denominator,
                          Optional<DensityFunction> argError) implements DensityFunction {
    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorModulo::numerator), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorModulo::denominator), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(FloorModulo::argError)).apply(instance, (FloorModulo::new)));
    public static final KeyDispatchDataCodec<FloorModulo> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute( FunctionContext pos) {
        int numeratorValue = (int) StrictMath.floor(this.numerator.compute(pos));
        int denominatorValue = (int) StrictMath.floor(this.denominator.compute(pos));

        if (denominatorValue == 0) {
            if (argError.isPresent()) {
                return this.argError.get().compute(pos);
            }
            return MoreDensityFunctionsConstants.DEFAULT_ERROR;
        }

        return StrictMath.floorMod(numeratorValue, denominatorValue);
    }

    @Override
    public void fillArray(double  [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public  DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FloorModulo(this.numerator, this.denominator, this.argError));
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
