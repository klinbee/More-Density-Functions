package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

public interface NoiseDensityFunction extends DensityFunction {

    /// Extra Octaves CODEC
    record ExtraOctaves(int count,
                        double lacunarity,
                        double persistence,
                        double[] frequencies,
                        double[] amplitudes,
                        double maxAmplitude) {

        public static final Codec<ExtraOctaves> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                MoreDensityFunctionsConstants.NON_NEGATIVE_INT.fieldOf("count").forGetter(ExtraOctaves::count),
                                Codec.DOUBLE.fieldOf("lacunarity").forGetter(ExtraOctaves::lacunarity),
                                Codec.DOUBLE.fieldOf("persistence").forGetter(ExtraOctaves::persistence)
                        ).apply(instance, ExtraOctaves::create)
                );

        private static ExtraOctaves getDefault() {
            return new ExtraOctaves(0, 0.0D, 0.0D, null, null, 1.0D);
        }

        private static ExtraOctaves create(int count, double lacunarity, double persistence) {
            if (count == 0) { // Treats as non-existent when count is 0; count 0 is allowed for easier extraOctaves toggling
                return getDefault();
            }
            double[] amplitudes = computeNoiseRatios(count, persistence);
            double[] frequencies = computeNoiseRatios(count, lacunarity);

            double maxAmplitude = computeAmplitudesSum(count, persistence);

            return new ExtraOctaves(count, lacunarity, persistence, frequencies, amplitudes, maxAmplitude);
        }

        // To pre-compute amplitudes/frequencies based on extra octave count
        private static double[] computeNoiseRatios(int octaves, double ratio) {
            double[] ratios = new double[octaves];
            double currRatio = ratio;
            for (int i = 0; i < octaves; i++) {
                ratios[i] = currRatio;
                currRatio *= ratio;
            }
            return ratios;
        }

        // To pre-compute the sum of amplitudes using a geometric sum
        private static double computeAmplitudesSum(int count, double ratio) {
            if (ratio == 1.0D) return count;
            return ratio * (StrictMath.pow(ratio, count) - 1) / (ratio - 1);
        }

        public boolean isSingleOctave() {
            return count == 0;
        }
    }

    /// Getter Contracts for all NoiseDensityFunctions

    Optional<ExtraOctaves> extraOctavesHolder();
    ExtraOctaves extraOctaves();

    /// Evaluation Method Contract for all NoiseDensityFunctions
    double eval(int x, int y, int z);

    /// Default DensityFunction computation for all NoiseDensityFunctions
    @Override
    default double compute(FunctionContext context) {

        int x, y, z;
        x = context.blockX();
        y = context.blockY();
        z = context.blockZ();

        double noiseResult = eval(x, y, z);

        if (extraOctaves().isSingleOctave()) {
            return noiseResult;
        }

        double[] frequencies = extraOctaves().frequencies;
        double[] amplitudes = extraOctaves().amplitudes;
        for (int i = 0; i < frequencies.length; i++) {
            noiseResult += amplitudes[i] * eval(
                    Mth.floor(x * frequencies[i]),
                    Mth.floor(y * frequencies[i]),
                    Mth.floor(z * frequencies[i])
            );
        }
        return noiseResult;
    }

    /// Noise Utility Methods
    // To compute grid cell coordinates from regular coordinates
    static int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
    }

    /// DensityFunction Method Contract
    @Override
    default void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }
}
