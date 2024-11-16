package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Negation(DensityFunction df) implements DensityFunctionTypes.Unary {

    private static final MapCodec<Negation> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("argument").forGetter(Negation::df)).apply(instance, (Negation::new)));
    public static final CodecHolder<Negation> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    @Override
    public DensityFunction input() {
        return this.df;
    }

    @Override
    public double apply(double density) {
        return -density;
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return new Negation(this.df.apply(visitor));
    }

    @Override
    public double minValue() {
        return apply(this.df.maxValue());
    }

    @Override
    public double maxValue() {
        return apply(this.df.minValue());
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
