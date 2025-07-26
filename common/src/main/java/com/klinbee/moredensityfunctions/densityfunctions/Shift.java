package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Shift(DensityFunction arg,
                    DensityFunction shiftX,
                    DensityFunction shiftY,
                    DensityFunction shiftZ)
        implements DensityFunction {

    private static final MapCodec<Shift> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Shift::arg),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(Shift::shiftX),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(Shift::shiftY),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(Shift::shiftZ)
                    ).apply(instance, Shift::new)
            );

    public static final KeyDispatchDataCodec<Shift> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        return arg.compute(new FunctionContext() {
            @Override
            public int blockX() {
                return pos.blockX() + (int) shiftX.compute(pos);
            }

            @Override
            public int blockY() {
                return pos.blockY() + (int) shiftY.compute(pos);
            }

            @Override
            public int blockZ() {
                return pos.blockZ() + (int) shiftZ.compute(pos);
            }
        });
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Shift(
                        arg.mapAll(visitor),
                        shiftX.mapAll(visitor),
                        shiftY.mapAll(visitor),
                        shiftZ.mapAll(visitor)
                )
        );
    }

    @Override
    public double minValue() {
        return arg.minValue();
    }

    @Override
    public double maxValue() {
        return arg.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
