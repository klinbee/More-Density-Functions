package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record FloorModulo(DensityFunction numerator,
                          DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<FloorModulo> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(FloorModulo::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(FloorModulo::denominator)
                    ).apply(instance, FloorModulo::new)
            );

    public static final KeyDispatchDataCodec<FloorModulo> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);


    public static final String NAME = "floor_mod";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.floorMod(Mth.floor(numerator.compute(pos)), Mth.floor(denominator.compute(pos)));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new FloorModulo(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        double floorMinDenom = Mth.floor(denominator.minValue());

        if (floorMinDenom < 0) {
            return floorMinDenom + 1;
        } else {
            return 0;
        }
    }

    @Override
    public double maxValue() {
        double floorMaxDenom = Mth.floor(denominator.maxValue());

        if (floorMaxDenom > 0) {
            return floorMaxDenom - 1;
        } else {
            return 0;
        }
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
