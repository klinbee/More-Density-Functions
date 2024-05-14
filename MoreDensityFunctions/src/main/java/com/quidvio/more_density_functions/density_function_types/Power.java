package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Power(DensityFunction base, DensityFunction exponent, double minOutput, double maxOutput,
    DensityFunction errorDf) implements DensityFunction {

  private static final MapCodec<Power> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
      .group(DensityFunction.FUNCTION_CODEC.fieldOf("base").forGetter(Power::base),
          DensityFunction.FUNCTION_CODEC.fieldOf("exp").forGetter(Power::exponent),
          Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min_output").forGetter(Power::minOutput),
          Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max_output").forGetter(Power::maxOutput),
          DensityFunction.FUNCTION_CODEC.fieldOf("error_output").forGetter(Power::errorDf))
      .apply(instance, (Power::new)));
  public static final CodecHolder<Power> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

  @Override
  public double sample(NoisePos pos) {
    double base = this.base.sample(pos);
    double exponent = this.exponent.sample(pos);
    double result = Math.pow(base, exponent);

    if (Double.isNaN(result) || Double.isInfinite(result)) {
      return this.errorDf.sample(pos);
    }

    if (result < this.minOutput) {
      return this.minOutput;
    }

    if (result > this.maxOutput) {
      return this.maxOutput;
    }

    return result;
  }

  @Override
  public void fill(double[] densities, EachApplier applier) {
    applier.fill(densities, this);
  }

  @Override
  public DensityFunction apply(DensityFunctionVisitor visitor) {
    return visitor.apply(new Power(this.base.apply(visitor), this.exponent.apply(visitor), this.minOutput,
        this.maxOutput, this.errorDf.apply(visitor)));
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
  public DensityFunction errorDf() {
    return this.errorDf;
  }

  @Override
  public double minValue() {
    return Math.min(this.errorDf.minValue(), this.minOutput);
  }

  @Override
  public double maxValue() {
    return Math.max(this.errorDf.maxValue(), this.maxOutput);
  }

  @Override
  public CodecHolder<? extends DensityFunction> getCodecHolder() {
    return CODEC;
  }
}
