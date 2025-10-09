package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.util.ExtraOctaves;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public interface NoiseDensityFunction extends DensityFunction {

    /// Getter Contracts for all NoiseDensityFunctions

    // All noises must have extra octaves
    ExtraOctaves extraOctaves();

    // All noises must have a salt
    int salt();

    /// Evaluation Method Contract for all NoiseDensityFunctions; used by `compute()`
    double eval(int x, int y, int z);

    /// Default DensityFunction computation for all NoiseDensityFunctions
    @Override
    default double compute(FunctionContext context) {

        int x, y, z;
        x = context.blockX();
        y = context.blockY();
        z = context.blockZ();

        ExtraOctaves extraOctaves = extraOctaves();

        double noiseResult = eval(x, y, z);

        if (extraOctaves.isSingleOctave()) {
            return noiseResult;
        }

        double[] frequencies = extraOctaves.frequencies();
        double[] amplitudes = extraOctaves.amplitudes();
        int[] offsetsX = extraOctaves.offsetsX();
        int[] offsetsY = extraOctaves.offsetsY();
        int[] offsetsZ = extraOctaves.offsetsZ();

        for (int i = 0; i < frequencies.length; i++) {
            noiseResult += amplitudes[i] * eval(
                    Mth.floor((x + offsetsX[i]) * frequencies[i]),
                    Mth.floor((y + offsetsY[i]) * frequencies[i]),
                    Mth.floor((z + offsetsZ[i]) * frequencies[i])
            );
        }
        return noiseResult;
    }

    /// Noise Utility Methods

    /// DensityFunction Method Contract
    @Override
    default void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }
}
