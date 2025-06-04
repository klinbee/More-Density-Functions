package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Optional;


public record FloorModulo(DensityFunction numerator, DensityFunction denominator,
                          Optional<DensityFunction> errorArgHolder, DensityFunction errorArg
) implements DensityFunction {
    private static final MapCodec<FloorModulo> MAP_CODEC = RecordCodecBuilder.mapCodec(
            (instance) -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorModulo::numerator),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorModulo::denominator),
                    DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(FloorModulo::errorArgHolder)
            ).apply(instance, (numerator, denominator, errorArgHolder) ->
                    new FloorModulo(numerator, denominator, errorArgHolder, errorArgHolder.orElse(DensityFunctions.zero()))
            ));
    public static final KeyDispatchDataCodec<FloorModulo> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        int numeratorValue = Mth.floor(this.numerator.compute(pos));
        int denominatorValue = Mth.floor(this.denominator.compute(pos));

        if (denominatorValue == 0) {
            return errorArg.compute(pos);
        }

        return StrictMath.floorMod(numeratorValue, denominatorValue);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FloorModulo(this.numerator, this.denominator, this.errorArgHolder, this.errorArg));
    }

    public DensityFunction numerator() {
        return numerator;
    }

    public DensityFunction denominator() {
        return denominator;
    }

    public Optional<DensityFunction> errorArgHolder() {
        return errorArgHolder;
    }

    @Override
    public double minValue() {
        double minError = errorArg.minValue();
        double minDenom = denominator.minValue();

        if (Mth.floor(minDenom) < 0) {
            // Worst case for negative: floorMod returns floor(minDenom) + 1
            return Math.min(minError, Mth.floor(minDenom) + 1);
        } else {
            // Worst case for positive: floorMod returns 0
            return Math.min(minError, 0);
        }
    }

    @Override
    public double maxValue() {
        double maxError = errorArg.maxValue();
        double maxDenom = denominator.maxValue();

        if (Mth.floor(maxDenom) > 0) {
            // Worst case for negative: floorMod returns floor(maxDenom)  - 1
            return Math.max(maxError, Mth.floor(maxDenom) - 1);
        } else {
            // Worst case for positive: floorMod returns 0
            return Math.max(maxError, 0);
        }
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
