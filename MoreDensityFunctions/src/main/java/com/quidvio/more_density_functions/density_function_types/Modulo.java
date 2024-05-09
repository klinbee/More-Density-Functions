package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Modulo(DensityFunction df, int divisor) implements DensityFunctionTypes.class_6932 {

    private static final MapCodec<Modulo> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("input").forGetter(Modulo::df), Codec.intRange(Integer.MIN_VALUE, Integer.MAX_VALUE).fieldOf("divisor").forGetter(Modulo::divisor)).apply(instance, (Modulo::new)));
    public static final CodecHolder<Modulo> CODEC = DensityFunctionTypes.method_41065(MAP_CODEC);

    @Override
    public DensityFunction input() {
        return this.df;
    }

    public int divisor() {
        return divisor;
    }

    @Override
    public double apply(double density) {
        return density % this.divisor;
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return new Modulo(this.df.apply(visitor), this.divisor);
    }

    @Override
    public double minValue() {
        return Double.MIN_VALUE;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
