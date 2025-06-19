package com.klinbee.moredensityfunctions.densityfunctions;


import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

public record WorleyNoise(Interpolator interpolator, Optional<Integer> saltHolder,
                          Optional<ExtraOctaves> extraOctavesHolder, boolean singleOctave,
                          double[] frequencies, double[] amplitudes,
                          double minValue, double maxValue
) implements NoiseDensityFunction {

    private static final MapCodec<WorleyNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomSampler.CODEC.fieldOf("sampler").forGetter(df -> df.interpolator().randomSampler()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(df -> df.interpolator().sizeX()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(df -> df.interpolator().sizeY()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(df -> df.interpolator().sizeZ()),
            Interpolator.Type.CODEC.fieldOf("interpolation").forGetter(df -> df.interpolator().interpolation()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(WorleyNoise::saltHolder),
            ExtraOctaves.CODEC.optionalFieldOf("extra_octaves").forGetter(WorleyNoise::extraOctavesHolder)
    ).apply(instance, WorleyNoise::create));

    /// Evaluates the noise based on the ValueNoise.Interpolator instance ///
    @Override
    public double evaluate(int x, int y, int z) {
        // divide into grid cells
        int gridX = NoiseDensityFunction.safeFloorDiv(x, sizeX);
        int gridY = NoiseDensityFunction.safeFloorDiv(y, sizeY);
        int gridZ = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
        // randomize sub-cell point based on jitter
        // offset from gridxyz by jitter, modulo?
        long hashCell = RandomSampler.hashPosition(gridX, gridY, gridZ, salt);
        int pointX = gridX + (randomSamplerX.sample(hashCell) % sizeX);
        int pointY = gridY + (randomSamplerY.sample(hashCell) % sizeY);
        int pointZ = gridZ + (randomSamplerZ.sample(hashCell) % sizeZ);
        // optionally: add values to points based on voronoi_sampler
        // use distance_metric to calculate values for position
        // e.g. compute nearby cells first, then find closest point in cell...
        // optionally: more steps based on edge/F2/etc.

        return 0.0D;
    }

    /// Getter Contracts (Fulfilled by record) ///
    // double minValue();
    // double maxValue();

    /// ValueNoise Builder ///
    static WorleyNoise create(RandomSampler randomSampler, int sizeX, int sizeY, int sizeZ,
                              Interpolator.Type interpolation, Optional<Integer> saltHolder, Optional<ExtraOctaves> extraOctavesHolder) {

        int salt = saltHolder.orElse(0);

        double minValue = randomSampler.minValue();
        double maxValue = randomSampler.maxValue();

        ExtraOctaves extraOctaves = extraOctavesHolder.orElse(null);

        boolean isTwoD = sizeY == 0;

        Interpolator interpolator = Interpolator.create(interpolation, isTwoD, sizeX, sizeY, sizeZ, randomSampler, salt);

        if (extraOctaves == null || extraOctaves.count() == 0) {
            return new WorleyNoise(
                    interpolator, saltHolder,
                    extraOctavesHolder, true,
                    null, null,
                    minValue, maxValue);
        }

        double[] amplitudes = NoiseDensityFunction.computeNoiseRatios(extraOctaves.count(), extraOctaves.persistence());
        double[] frequencies = NoiseDensityFunction.computeNoiseRatios(extraOctaves.count(), extraOctaves.lacunarity());

        double amplitudeSum = 0.0D;
        for (double amplitude : amplitudes) {
            amplitudeSum += amplitude;
        }
        minValue = minValue * amplitudeSum;
        maxValue = maxValue * amplitudeSum;

        return new WorleyNoise(interpolator, saltHolder,
                extraOctavesHolder, false,
                amplitudes, frequencies,
                minValue, maxValue);
    }

    /// DensityFunction Method Contracts ///
    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new WorleyNoise(interpolator, saltHolder, extraOctavesHolder, singleOctave, frequencies, amplitudes, minValue, maxValue));
    }

    public static KeyDispatchDataCodec<WorleyNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static MapCodec<WorleyNoise> getMapCodec() {
        return MAP_CODEC;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
