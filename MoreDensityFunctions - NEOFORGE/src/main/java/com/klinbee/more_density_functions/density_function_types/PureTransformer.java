package com.klinbee.more_density_functions.density_function_types;

import net.minecraft.world.level.levelgen.DensityFunction;

// This is only necessary because AccessTransformers don't work properly in 1.20.6. in Neoforge.
// As of 1.21, access transformers work as intended and successfully make PureTransformers accessible.
// For now, the interface is implemented this way.
@Deprecated
public interface PureTransformer extends DensityFunction {
  DensityFunction input();

  @Override
  default double compute(DensityFunction.FunctionContext pContext) {
    return this.transform(this.input().compute(pContext));
  }

  @Override
  default void fillArray(double[] pArray, DensityFunction.ContextProvider pContextProvider) {
    this.input().fillArray(pArray, pContextProvider);

    for (int i = 0; i < pArray.length; i++) {
      pArray[i] = this.transform(pArray[i]);
    }
  }

  double transform(double pValue);
}