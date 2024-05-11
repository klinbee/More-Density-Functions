package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Reciprocal(DensityFunction df, double maxOutput, double minOutput) implements DensityFunctionTypes.Unary {

    private static final MapCodec<Reciprocal> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(Reciprocal::df), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(Reciprocal::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(Reciprocal::minOutput)).apply(instance, (Reciprocal::new)));
    public static final CodecHolder<Reciprocal> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    @Override
    public DensityFunction input() {
        return this.df;
    }

    @Override
    public double apply(double density) {
        if (density == 0) {
            return maxOutput;
        }
        double output = 1 / density;
        if (output > maxOutput) {
            return maxOutput;
        }
        if (output < minOutput) {
            return minOutput;
        }

        return output;
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
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
