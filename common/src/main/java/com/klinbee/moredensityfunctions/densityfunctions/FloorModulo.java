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

    private static double eval(double numerator, double denominator) {
        // Similar to `StrictMath.floorMod()` but for doubles, doesn't throw errors
        return denominator < 0 ?
                Math.floor((-numerator % -denominator - denominator) % -denominator) - 1 :
                Math.floor((numerator % denominator + denominator) % denominator);
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
                new FloorModulo(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    // TODO
    @Override
    public double minValue() {
        return -1.0D;
    }

    @Override
    public double maxValue() {
        return 1.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
