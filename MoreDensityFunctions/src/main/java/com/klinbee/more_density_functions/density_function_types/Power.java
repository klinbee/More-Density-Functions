package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.klinbee.more_density_functions.MoreDensityFunctionsMod;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

import java.util.Optional;

public record Power(DensityFunction base, DensityFunction exponent, Optional<Double> minOutput, Optional<Double> maxOutput, Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<Power> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("base").forGetter(Power::base), DensityFunction.FUNCTION_CODEC.fieldOf("exp").forGetter(Power::exponent), Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Power::minOutput), Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Power::maxOutput), DensityFunction.FUNCTION_CODEC.optionalFieldOf("error_output").forGetter(Power::errorDf)).apply(instance, (Power::new)));
    public static final CodecHolder<Power> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(NoisePos pos) {

        double base = this.base.sample(pos);
        double exponent = this.exponent.sample(pos);
        double result = Math.pow(base, exponent);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            if (errorDf.isPresent()) {
                return this.errorDf.get().sample(pos);
            }
            return MoreDensityFunctionsMod.DEFAULT_ERROR;
        }

        if (result > this.maxOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MAX_OUTPUT)) {
            return this.maxOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MAX_OUTPUT);
        }

        if (result < this.minOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MIN_OUTPUT)) {
            return this.minOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MIN_OUTPUT);
        }

        return result;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        if (this.errorDf.isPresent()) {
            return visitor.apply(new Power(this.base.apply(visitor), this.exponent.apply(visitor), this.minOutput, this.maxOutput, Optional.of(this.errorDf.get().apply(visitor))));
        }
        return visitor.apply(new Power(this.base.apply(visitor), this.exponent.apply(visitor), this.minOutput, this.maxOutput, Optional.empty()));
    }

    @Override
    public DensityFunction base() {
        return this.base;
    }

    @Override
    public DensityFunction exponent() {
        return this.exponent;
    }

    @Override
    public Optional<DensityFunction> errorDf() {
        return errorDf;
    }

    @Override
    public double minValue() {
        if (errorDf.isPresent()) {
            return Math.min(this.errorDf.get().minValue(),this.minOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MIN_OUTPUT));
        }
        return Math.min(MoreDensityFunctionsMod.DEFAULT_ERROR,this.minOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MIN_OUTPUT));
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(),this.maxOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MAX_OUTPUT));
        }
        return Math.max(MoreDensityFunctionsMod.DEFAULT_ERROR,this.maxOutput.orElse(MoreDensityFunctionsMod.DEFAULT_MAX_OUTPUT));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
