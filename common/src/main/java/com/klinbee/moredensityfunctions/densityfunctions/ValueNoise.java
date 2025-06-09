package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.klinbee.moredensityfunctions.noisegenerators.NoiseGenerator;
import com.klinbee.moredensityfunctions.noisegenerators.ValueNoiseGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record ValueNoise(ValueNoiseGenerator noiseGenerator, NoiseGenerator.InterpolationType interpType,
                         Optional<Integer> saltHolder,
                         Optional<NoiseGenerator.ExtraOctaves> extraOctavesHolder, boolean singleOctave,
                         double[] frequencies, double[] amplitudes,
                         double minValue, double maxValue
) implements DensityFunction {
    private static final MapCodec<ValueNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomDistribution.CODEC.fieldOf("distribution").forGetter(df -> df.noiseGenerator.distribution()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(df -> df.noiseGenerator.sizeX()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(df -> df.noiseGenerator.sizeY()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(df -> df.noiseGenerator.sizeZ()),
            NoiseGenerator.InterpolationType.CODEC.fieldOf("interpolation").forGetter(ValueNoise::interpType),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(ValueNoise::saltHolder),
            NoiseGenerator.ExtraOctaves.CODEC.optionalFieldOf("extra_octaves").forGetter(ValueNoise::extraOctavesHolder)
    ).apply(instance, (distribution, sizeX, sizeY, sizeZ, interpType, saltHolder, extraOctavesHolder) -> {
        int salt = saltHolder.orElse(0);
        ValueNoiseGenerator valueNoiseGenerator = NoiseGenerator.buildValueNoise(distribution, sizeX, sizeY, sizeZ, salt, interpType);

        double minValue = valueNoiseGenerator.distribution().minValue();
        double maxValue = valueNoiseGenerator.distribution().maxValue();

        NoiseGenerator.ExtraOctaves extraOctaves = extraOctavesHolder.orElse(null);

        if (extraOctaves == null || extraOctaves.count() == 0) {
            return new ValueNoise(valueNoiseGenerator, interpType, saltHolder, extraOctavesHolder, true, null, null, minValue, maxValue);
        }

        double[] amplitudes = NoiseGenerator.computeNoiseRatios(extraOctaves.count(), extraOctaves.persistence());
        double[] frequencies = NoiseGenerator.computeNoiseRatios(extraOctaves.count(), extraOctaves.lacunarity());

        double amplitudeSum = 0.0D;
        for (double amplitude : amplitudes) {
            amplitudeSum += amplitude;
        }
        minValue = minValue * amplitudeSum;
        maxValue = maxValue * amplitudeSum;
        return new ValueNoise(valueNoiseGenerator, interpType, saltHolder, extraOctavesHolder, false, frequencies, amplitudes, minValue, maxValue);
    }));
    public static final KeyDispatchDataCodec<ValueNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext context) {

        int x, y, z;
        x = context.blockX();
        y = context.blockY();
        z = context.blockZ();

        double noiseResult = noiseGenerator.evaluate(x, y, z);

        if (singleOctave) {
            return noiseResult;
        }

        for (int i = 0; i < frequencies.length; i++) {
            noiseResult += amplitudes[i] * noiseGenerator.evaluate(
                    Mth.floor(x * frequencies[i]),
                    Mth.floor(y * frequencies[i]),
                    Mth.floor(z * frequencies[i])
            );
        }
        return noiseResult;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ValueNoise(this.noiseGenerator, this.interpType, this.saltHolder, this.extraOctavesHolder, this.singleOctave, this.frequencies, this.amplitudes, this.minValue, this.maxValue));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
