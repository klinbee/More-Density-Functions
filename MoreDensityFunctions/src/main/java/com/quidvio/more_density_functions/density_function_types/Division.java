package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record Division(DensityFunction dividend, DensityFunction divisor, double maxOutput, double minOutput,
    DensityFunction errorDf) implements DensityFunction {

  private static final MapCodec<Division> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
      .group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(Division::dividend),
          DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(Division::divisor),
          Codec.DOUBLE.optionalFieldOf("max_output").forGetter(Division::maxOutput),
          Codec.DOUBLE.optionalFieldOf("min_output").forGetter(Division::minOutput),
          DensityFunction.FUNCTION_CODEC.optionalFieldOf("error_output").forGetter(Division::errorDf))
      .apply(instance, (Division::new)));
  public static final CodecHolder<Division> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

  if(divisorValue==0)
  {
    return this.errorDf.sample(pos);
  }

  double result = dividendValue / divisorValue;

  if(result>this.maxOutput)
  {
    return this.maxOutput;
  }

  if(result<this.minOutput)
  {
    return this.minOutput;
  }

  return result;

  @Override
  public void applyEach(double[] densities, EachApplier applier) {
    applier.applyEach(densities, this);
  }

  @Override
  public DensityFunction apply(DensityFunctionVisitor visitor) {
    return visitor.apply(new Division(this.dividend.apply(visitor), this.divisor.apply(visitor), this.maxOutput,
        this.minOutput, this.errorDf.apply(visitor)));
  }

  @Override
  public DensityFunction dividend() {
    return dividend;
  }

  @Override
  public DensityFunction divisor() {
    return divisor;
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
