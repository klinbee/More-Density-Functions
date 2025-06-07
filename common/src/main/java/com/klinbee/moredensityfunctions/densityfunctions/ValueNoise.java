package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsCommon;
import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.klinbee.moredensityfunctions.randomgenerators.RandomSampler;
import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record ValueNoise(RandomDistribution distribution, int sizeX, int sizeY, int sizeZ,
                         Optional<Integer> octaveHolder, Optional<Double> lacunarityHolder, double[] phases,
                         Optional<Double> persistenceHolder, double[] amplitudes, boolean useSmoothstep,
                         Optional<Integer> saltHolder, int salt
) implements DensityFunction {
    private static final MapCodec<ValueNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomDistribution.CODEC.fieldOf("distribution").forGetter(ValueNoise::distribution),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(ValueNoise::sizeX),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(ValueNoise::sizeY),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(ValueNoise::sizeZ),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("octaveHolder").forGetter(ValueNoise::octaveHolder),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).optionalFieldOf("lacunarityHolder").forGetter(ValueNoise::lacunarityHolder),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).optionalFieldOf("persistenceHolder").forGetter(ValueNoise::persistenceHolder),
            Codec.BOOL.fieldOf("use_smoothstep").forGetter(ValueNoise::useSmoothstep),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("saltHolder").forGetter(ValueNoise::saltHolder)
    ).apply(instance, (distribution, sizeX, sizeY, sizeZ, octaves, lacunarity, persistence, useSmoothstep, saltHolder) -> {
        int salt = saltHolder.orElse(0);
        int octave = octaves.orElse(0);

        if (octave == 0 || persistence.isEmpty() || lacunarity.isEmpty()) {
            return new ValueNoise(distribution, sizeX, sizeY, sizeZ, octaves, lacunarity, null,
                    persistence, null, useSmoothstep, saltHolder, salt);
        }

        double[] amplitudes = computeNoiseRatios(octave, persistence.get());
        double[] phases = computeNoiseRatios(octave, lacunarity.get());
        return new ValueNoise(distribution, sizeX, sizeY, sizeZ, octaves, lacunarity, phases,
                persistence, amplitudes, useSmoothstep, saltHolder, salt);
    }));
    public static final KeyDispatchDataCodec<ValueNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
    private static long worldSeed;
    private static boolean worldSeedInitialized;

    public static double[] computeNoiseRatios(int octaves, double ratio) {
        double[] ratios = new double[octaves];
        double baseRatio = ratio;
        for (int i = 0; i < octaves; i++) {
            ratios[i] = ratio;
            ratio *= baseRatio;
        }
        return ratios;
    }

    @Override
    public double compute(FunctionContext context) {
        if (!worldSeedInitialized) {
            try {
                worldSeed = MoreDensityFunctionsCommon.getWorldSeed();
                worldSeedInitialized = true;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        int coordX, coordY, coordZ;
        coordX = context.blockX();
        coordY = context.blockY();
        coordZ = context.blockZ();

        if (useSmoothstep) {
            double noiseResult = evalSmoothValueNoise(worldSeed, salt, coordX, coordY, coordZ, sizeX, sizeY, sizeZ);
            if (phases == null) {
                return noiseResult;
            }
            for (int i = 0; i < phases.length; i++) {
                noiseResult += amplitudes[i] * evalSmoothValueNoise(
                        worldSeed, salt, coordX, coordY, coordZ,
                        Mth.floor(sizeX * phases[i]),
                        Mth.floor(sizeY * phases[i]),
                        Mth.floor(sizeZ * phases[i]));
            }

            return noiseResult;
        }
        double noiseResult = evalLerpValueNoise(worldSeed, salt, coordX, coordY, coordZ, sizeX, sizeY, sizeZ);
        if (phases == null) {
            return noiseResult;
        }
        for (int i = 0; i < phases.length; i++) {
            noiseResult += amplitudes[i] * evalLerpValueNoise(
                    worldSeed, salt, coordX, coordY, coordZ,
                    Mth.floor(sizeX * phases[i]),
                    Mth.floor(sizeY * phases[i]),
                    Mth.floor(sizeZ * phases[i]));
        }

        return noiseResult;
    }

    public double evalLerpValueNoise(long worldSeed, int funcSalt, int coordX, int coordY, int coordZ, int sizeX, int sizeY, int sizeZ) {
        int gridX0, gridY0, gridZ0, gridX1, gridY1, gridZ1;
        gridX0 = MDFUtil.safeFloorDiv(coordX, sizeX);
        gridY0 = MDFUtil.safeFloorDiv(coordY, sizeY);
        gridZ0 = MDFUtil.safeFloorDiv(coordZ, sizeZ);
        gridX1 = gridX0 + 1;
        gridY1 = gridY0 + 1;
        gridZ1 = gridZ0 + 1;

        double cellX, cellY, cellZ;
        cellX = cellCoord(coordX, sizeX);
        cellY = cellCoord(coordY, sizeY);
        cellZ = cellCoord(coordZ, sizeZ);

        long hash0, hash1, hash2, hash3, hash4, hash5, hash6, hash7;
        hash0 = RandomSampler.hashPosition(worldSeed, gridX0, gridY0, gridZ0, funcSalt);
        hash1 = RandomSampler.hashPosition(worldSeed, gridX1, gridY0, gridZ0, funcSalt);
        hash2 = RandomSampler.hashPosition(worldSeed, gridX0, gridY1, gridZ0, funcSalt);
        hash3 = RandomSampler.hashPosition(worldSeed, gridX1, gridY1, gridZ0, funcSalt);
        hash4 = RandomSampler.hashPosition(worldSeed, gridX0, gridY0, gridZ1, funcSalt);
        hash5 = RandomSampler.hashPosition(worldSeed, gridX1, gridY0, gridZ1, funcSalt);
        hash6 = RandomSampler.hashPosition(worldSeed, gridX0, gridY1, gridZ1, funcSalt);
        hash7 = RandomSampler.hashPosition(worldSeed, gridX1, gridY1, gridZ1, funcSalt);

        double gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7;
        gridVal0 = distribution.getRandom(hash0);
        gridVal1 = distribution.getRandom(hash1);
        gridVal2 = distribution.getRandom(hash2);
        gridVal3 = distribution.getRandom(hash3);
        gridVal4 = distribution.getRandom(hash4);
        gridVal5 = distribution.getRandom(hash5);
        gridVal6 = distribution.getRandom(hash6);
        gridVal7 = distribution.getRandom(hash7);

        double x0, x1, x2, x3, y0, y1;

        x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
        x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;
        x2 = gridVal4 * (1 - cellX) + gridVal5 * cellX;
        x3 = gridVal6 * (1 - cellX) + gridVal7 * cellX;

        y0 = x0 * (1 - cellY) + x1 * cellY;
        y1 = x2 * (1 - cellY) + x3 * cellY;

        return y0 * (1 - cellZ) + y1 * cellZ;
    }

    public double evalSmoothValueNoise(long worldSeed, int funcSalt, int coordX, int coordY, int coordZ, int sizeX, int sizeY, int sizeZ) {
        int gridX0, gridY0, gridZ0, gridX1, gridY1, gridZ1;
        gridX0 = MDFUtil.safeFloorDiv(coordX, sizeX);
        gridY0 = MDFUtil.safeFloorDiv(coordY, sizeY);
        gridZ0 = MDFUtil.safeFloorDiv(coordZ, sizeZ);
        gridX1 = gridX0 + 1;
        gridY1 = gridY0 + 1;
        gridZ1 = gridZ0 + 1;

        double cellX, cellY, cellZ;
        cellX = cellCoord(coordX, sizeX);
        cellY = cellCoord(coordY, sizeY);
        cellZ = cellCoord(coordZ, sizeZ);

        /// Smooth Step ///
        cellX = cellX * cellX * (3.0D - 2.0D * cellX);
        cellY = cellY * cellY * (3.0D - 2.0D * cellY);
        cellZ = cellZ * cellZ * (3.0D - 2.0D * cellZ);

        long hash0, hash1, hash2, hash3, hash4, hash5, hash6, hash7;

        hash0 = RandomSampler.hashPosition(worldSeed, gridX0, gridY0, gridZ0, funcSalt);
        hash1 = RandomSampler.hashPosition(worldSeed, gridX1, gridY0, gridZ0, funcSalt);
        hash2 = RandomSampler.hashPosition(worldSeed, gridX0, gridY1, gridZ0, funcSalt);
        hash3 = RandomSampler.hashPosition(worldSeed, gridX1, gridY1, gridZ0, funcSalt);
        hash4 = RandomSampler.hashPosition(worldSeed, gridX0, gridY0, gridZ1, funcSalt);
        hash5 = RandomSampler.hashPosition(worldSeed, gridX1, gridY0, gridZ1, funcSalt);
        hash6 = RandomSampler.hashPosition(worldSeed, gridX0, gridY1, gridZ1, funcSalt);
        hash7 = RandomSampler.hashPosition(worldSeed, gridX1, gridY1, gridZ1, funcSalt);

        double gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7;

        gridVal0 = distribution.getRandom(hash0);
        gridVal1 = distribution.getRandom(hash1);
        gridVal2 = distribution.getRandom(hash2);
        gridVal3 = distribution.getRandom(hash3);
        gridVal4 = distribution.getRandom(hash4);
        gridVal5 = distribution.getRandom(hash5);
        gridVal6 = distribution.getRandom(hash6);
        gridVal7 = distribution.getRandom(hash7);

        /// Trilinear Interpolation ///
        double x0, x1, x2, x3, y0, y1;

        x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
        x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;
        x2 = gridVal4 * (1 - cellX) + gridVal5 * cellX;
        x3 = gridVal6 * (1 - cellX) + gridVal7 * cellX;

        y0 = x0 * (1 - cellY) + x1 * cellY;
        y1 = x2 * (1 - cellY) + x3 * cellY;

        return y0 * (1 - cellZ) + y1 * cellZ;
    }

    public double cellCoord(int coord, int size) {
        if (size == 0) {
            return 0.0D;
        }
        return ((double) StrictMath.floorMod(coord, size)) / size;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ValueNoise(this.distribution, this.sizeX, this.sizeY, this.sizeZ, this.octaveHolder, this.lacunarityHolder, this.phases, this.persistenceHolder, this.amplitudes, this.useSmoothstep, this.saltHolder, this.salt));
    }

    @Override
    public double minValue() {

        double minimum = this.distribution.minValue();
        double amplitudeSum = 0.0D;

        if (amplitudes != null) {
            for (double amplitude : amplitudes) {
                amplitudeSum += amplitude;
            }
            minimum += amplitudeSum * minimum;
        }
        return minimum;
    }

    @Override
    public double maxValue() {
        double maximum = this.distribution.minValue();
        double amplitudeSum = 0.0D;

        if (amplitudes != null) {
            for (double amplitude : amplitudes) {
                amplitudeSum += amplitude;
            }
            maximum += amplitudeSum * maximum;
        }
        return maximum;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
