package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.MDFMath;
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

    public static final TypedCodec<FloorModulo> TYPED_CODEC = new TypedCodec<>("floor_mod", KeyDispatchDataCodec.of(MAP_CODEC));

    private static double eval(double numerator, double denominator) {
        return MDFMath.floorMod(numerator, denominator);
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

    // Due to periodic nature, I'm using global min/max
    @Override
    public double minValue() {
        // If it is positive, the min is 0, if negative, the min is the denominator
        return StrictMath.min(0.0D, Mth.floor(Math.nextUp(denominator.minValue())));
    }

    @Override
    public double maxValue() {
        // If it is positive, then the max is the denominator, if negative, the max is 0
        return StrictMath.max(0.0D, Mth.floor(Math.nextDown(denominator.maxValue())));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
