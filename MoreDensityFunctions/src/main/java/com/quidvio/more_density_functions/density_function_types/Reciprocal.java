package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Reciprocal(DensityFunction df, double maxOutput, double minOutput,
                         DensityFunction errorDf) implements DensityFunction {

    private static final MapCodec<Reciprocal> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("input").forGetter(Reciprocal::df), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(Reciprocal::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(Reciprocal::minOutput), DensityFunction.FUNCTION_CODEC.fieldOf("error_output").forGetter(Reciprocal::errorDf)).apply(instance, (Reciprocal::new)));
    public static final CodecHolder<Reciprocal> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    public DensityFunction input() {
        return this.df;
    }


    /**
     * {@return the density value for the given block position}
     *
     * @param pos the block position
     */
    @Override
    public double sample(NoisePos pos) {

        double input = this.df.sample(pos);

        if (input == 0) {
            return this.errorDf.sample(pos);
        }

        double result = 1 / input;

        if (result > this.maxOutput) {
            return this.maxOutput;
        }
        if (result < this.minOutput) {
            return this.minOutput;
        }

        return result;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);

    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return new Reciprocal(this.df.apply(visitor), this.maxOutput, this.minOutput, this.errorDf.apply(visitor));
    }

    @Override
    public DensityFunction errorDf() {
        return this.errorDf;
    }

    @Override
    public double minValue() {
        return Math.min(this.errorDf.minValue(), this.minOutput);
    }

    @Override
    public double maxValue() {
        return Math.max(this.errorDf.maxValue(),this.maxOutput);
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
