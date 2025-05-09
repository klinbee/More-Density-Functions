package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record ShiftDensityFunction(DensityFunction arg, DensityFunction shiftX, DensityFunction shiftY,
                                   DensityFunction shiftZ) implements DensityFunction {
    private static final MapCodec<ShiftDensityFunction> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(ShiftDensityFunction::arg), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(ShiftDensityFunction::shiftX), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(ShiftDensityFunction::shiftY), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(ShiftDensityFunction::shiftZ)).apply(instance, (ShiftDensityFunction::new)));
    public static final KeyDispatchDataCodec<ShiftDensityFunction> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return arg.compute(new FunctionContext() {
            @Override
            public int blockX() {
                return pos.blockX() + (int)shiftX.compute(pos);
            }

            @Override
            public int blockY() {
                return pos.blockY() + (int)shiftY.compute(pos);
            }

            @Override
            public int blockZ() {
                return pos.blockZ() + (int)shiftZ.compute(pos);
            }
        });
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ShiftDensityFunction(this.arg, this.shiftX, this.shiftY, this.shiftZ));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public DensityFunction shiftX() {
        return shiftX;
    }

    @Override
    public DensityFunction shiftY() {
        return shiftY;
    }

    @Override
    public DensityFunction shiftZ() {
        return shiftZ;
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
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
