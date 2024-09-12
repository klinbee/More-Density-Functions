package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.NotNull;

public record Sine(DensityFunction df) implements DensityFunctions.PureTransformer {

    private static final MapCodec<Sine> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Sine::df)).apply(instance, Sine::new));
    public static final KeyDispatchDataCodec<Sine> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public @NotNull DensityFunction input() {
        return this.df;
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    @Override
    public double transform(double pValue) {
        return Math.sin(pValue);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new Sine(this.df));
    }

    public double minValue() {
        return -1;
    }

    public double maxValue() {
        return 1;
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunctions.PureTransformer> codec() {
        return CODEC;
    }
}
