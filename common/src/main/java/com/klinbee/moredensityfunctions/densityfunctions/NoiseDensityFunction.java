package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

public interface NoiseDensityFunction extends DensityFunction {

    /// Extra Octaves CODEC ///
    record ExtraOctaves(int count, double lacunarity, double persistence) {
        public static final Codec<ExtraOctaves> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("count").forGetter(ExtraOctaves::count),
                        Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lacunarity").forGetter(ExtraOctaves::lacunarity),
                        Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("persistence").forGetter(ExtraOctaves::persistence)
                ).apply(instance, ExtraOctaves::new)
        );
    }

    /// Getter Contracts for all NoiseDensityFunctions ///

    Optional<ExtraOctaves> extraOctavesHolder();

    boolean singleOctave();

    double[] frequencies();

    double[] amplitudes();

    /// Evaluation Method Contract for all NoiseDensityFunctions ///
    double evaluate(int x, int y, int z);

    /// Default DensityFunction computation for all NoiseDensityFunctions ///
    @Override
    default double compute(FunctionContext context) {

        int x, y, z;
        x = context.blockX();
        y = context.blockY();
        z = context.blockZ();

        double noiseResult = this.evaluate(x, y, z);

        if (this.singleOctave()) {
            return noiseResult;
        }

        double[] frequencies = this.frequencies();
        double[] amplitudes = this.amplitudes();
        for (int i = 0; i < frequencies.length; i++) {
            noiseResult += amplitudes[i] * this.evaluate(
                    Mth.floor(x * frequencies[i]),
                    Mth.floor(y * frequencies[i]),
                    Mth.floor(z * frequencies[i])
            );
        }
        return noiseResult;
    }

    /// Noise Utility Methods ///
    static int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
    }

    static double[] computeNoiseRatios(int octaves, double ratio) {
        double[] ratios = new double[octaves];
        double baseRatio = ratio;
        for (int i = 0; i < octaves; i++) {
            ratios[i] = ratio;
            ratio *= baseRatio;
        }
        return ratios;
    }

    /// DensityFunction Method Contract ///
    @Override
    default void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }
}
