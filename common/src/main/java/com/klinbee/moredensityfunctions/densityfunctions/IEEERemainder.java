package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record IEEERemainder(DensityFunction numerator,
                            DensityFunction denominator)
        implements DensityFunction {

    private static final MapCodec<IEEERemainder> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("numerator").forGetter(IEEERemainder::numerator),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("denominator").forGetter(IEEERemainder::denominator)
                    ).apply(instance, IEEERemainder::new)
            );

    public static final KeyDispatchDataCodec<IEEERemainder> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "ieee_rem";

    @Override
    public double compute(FunctionContext pos) {
        return StrictMath.IEEEremainder(numerator.compute(pos), denominator.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new IEEERemainder(
                        numerator.mapAll(visitor),
                        denominator.mapAll(visitor)
                )
        );
    }

    // Due to periodic nature, I'm using global min/max
    @Override
    public double minValue() {
        // If it is positive, the min is 0, if negative, the min is the denominator
        return StrictMath.min(0.0D, Math.nextUp(denominator.minValue()));
    }

    @Override
    public double maxValue() {
        // If it is positive, then the max is the denominator, if negative, the max is 0
        return StrictMath.max(0.0D, Math.nextDown(denominator.maxValue()));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
