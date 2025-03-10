package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record Subtract(DensityFunction arg1, DensityFunction arg2) implements DensityFunction {

    private static final MapCodec<Subtract> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(Subtract::arg1), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(Subtract::arg2)).apply(instance, Subtract::new));
    public static final KeyDispatchDataCodec<Subtract> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pContext) {
        return this.arg1.compute(pContext) - this.arg2.compute(pContext);
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new Subtract(this.arg1, this.arg2));
    }

    public DensityFunction arg1() {
        return this.arg1;
    }

    public DensityFunction arg2() {
        return this.arg2;
    }

    @Override
    public double minValue() {
        return this.arg1.minValue() - this.arg2.maxValue();
    }

    @Override
    public double maxValue() {
        return this.arg1.maxValue() - this.arg2.minValue();
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}