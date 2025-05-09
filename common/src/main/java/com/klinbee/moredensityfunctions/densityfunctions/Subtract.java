package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record Subtract(DensityFunction arg1, DensityFunction arg2) implements DensityFunction {
    private static final MapCodec<Subtract> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(Subtract::arg1), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(Subtract::arg2)).apply(instance, (Subtract::new)));
    public static final KeyDispatchDataCodec<Subtract> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return this.arg1.compute(pos) - this.arg2.compute(pos);
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Subtract(this.arg1, this.arg2));
    }

    @Override
    public DensityFunction arg1() {
        return arg1;
    }

    @Override
    public DensityFunction arg2() {
        return arg2;
    }

    @Override
    public double minValue() {
        return this.arg1.minValue() - this.arg2.maxValue();
    }

    @Override
    public double maxValue() {
        return this.arg1.maxValue() - this.arg2.minValue();
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
