package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Reciprocal(DensityFunction df, double maxOutput, double minOutput) implements DensityFunctionTypes.class_6932 {

    private static final MapCodec<Reciprocal> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.CODEC.fieldOf("input").forGetter(Reciprocal::df), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(Reciprocal::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(Reciprocal::minOutput)).apply(instance, (Reciprocal::new)));
    public static final CodecHolder<Reciprocal> CODEC  = DensityFunctionTypes.method_41065(MAP_CODEC);

    @Override
    public DensityFunction input() {
        return this.df;
    }

    @Override
    public double apply(double density) {
        if (density == 0) {
            return 0;
        }

        double output = 1/density;

        if (output > maxOutput) {
            return maxOutput;
        }
        if (output < minOutput) {
            return minOutput;
        }

        return 1/density;
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return new Reciprocal(this.df.apply(visitor), this.maxOutput, this.minOutput);
    }

    @Override
    public double minValue() {
        return this.df.minValue();
    }

    @Override
    public double maxValue() {
        return this.df.maxValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodec() {
        return CODEC;
    }
}
