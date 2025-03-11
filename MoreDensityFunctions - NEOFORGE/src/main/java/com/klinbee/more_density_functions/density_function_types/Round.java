package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.NotNull;

public record Round(DensityFunction df) implements PureTransformer {

    private static final MapCodec<Round> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Round::df)).apply(instance, Round::new));
    public static final KeyDispatchDataCodec<Round> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public @NotNull DensityFunction input() {
        return this.df;
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    @Override
    public double transform(double pValue) {
        return Math.round(pValue);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new Round(this.df));
    }

    public double minValue() {
        return transform(this.df.minValue());
    }

    public double maxValue() {
        return transform(this.df.minValue());
    }

    public @NotNull KeyDispatchDataCodec<? extends PureTransformer> codec() {
        return CODEC;
    }
}
