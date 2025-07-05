//package com.klinbee.moredensityfunctions.densityfunctions;
//
// TODO: WIP
//import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
//import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.util.KeyDispatchDataCodec;
//import net.minecraft.util.StringRepresentable;
//import net.minecraft.world.level.levelgen.DensityFunction;
//
//import java.util.Optional;
//
//public record WorleyNoise(Interpolator interpolator, Optional<Integer> saltHolder,
//                          Optional<ExtraOctaves> extraOctavesHolder, boolean singleOctave,
//                          double[] frequencies, double[] amplitudes,
//                          double minValue, double maxValue
//) implements NoiseDensityFunction {
//
//    private static final MapCodec<WorleyNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
//            RandomSampler.CODEC.fieldOf("sampler").forGetter(df -> df.interpolator().randomSampler()),
//            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(df -> df.interpolator().sizeX()),
//            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(df -> df.interpolator().sizeY()),
//            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(df -> df.interpolator().sizeZ()),
//            Interpolator.Type.CODEC.fieldOf("interpolation").forGetter(df -> df.interpolator().interpolation()),
//            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(WorleyNoise::saltHolder),
//            ExtraOctaves.CODEC.optionalFieldOf("extra_octaves").forGetter(WorleyNoise::extraOctavesHolder)
//    ).apply(instance, WorleyNoise::create));
//
//    /// Evaluates the noise based on the ValueNoise.Interpolator instance ///
//    public double evaluate3DWorley(int x, int y, int z) {
//        // divide into grid cells
//        int gridX = NoiseDensityFunction.safeFloorDiv(x, sizeX);
//        int gridY = NoiseDensityFunction.safeFloorDiv(y, sizeY);
//        int gridZ = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
//        double minDistance = Double.MAX_VALUE;
//        int closestCellX, closestCellY, closestCellZ;
//        for (int dx = -1; dx <= 1; dx++) {
//            for (int dy = -1; dy <= 1; dy++) {
//                for (int dz = -1; dz <= 1; dz++) {
//                    int sampleGridX = gridX + dx;
//                    int sampleGridY = gridY + dy;
//                    int sampleGridZ = gridZ + dz;
//
//                    long sampleHash = RandomSampler.hashPosition(sampleGridX, sampleGridY, sampleGridZ, salt);
//                    int samplePointX = sampleGridX * sizeX + (randomSamplerX.sample(sampleHash) % sizeX);
//                    int samplePointY = sampleGridY * sizeY + (randomSamplerY.sample(sampleHash) % sizeY);
//                    int samplePointZ = sampleGridZ * sizeZ + (randomSamplerZ.sample(sampleHash) % sizeZ);
//
//                    double currDistance = distance3D(x, y, z, samplePointX, samplePointY, samplePointZ);
//                    if (currDistance < minDistance) {
//                        minDistance = currDistance;
//                        closestCellX = sampleGridX;
//                        closestCellY = sampleGridY;
//                        closestCellZ = sampleGridZ;
//                    }
//                }
//            }
//        }
//        return minDistance;
//    }
//
//    public double evaluate3DVoronoi(int x, int y, int z) {
//        // divide into grid cells
//        int gridX = NoiseDensityFunction.safeFloorDiv(x, sizeX);
//        int gridY = NoiseDensityFunction.safeFloorDiv(y, sizeY);
//        int gridZ = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
//        double minDistance = Double.MAX_VALUE;
//        int closestCellX, closestCellY, closestCellZ;
//        for (int dx = -1; dx <= 1; dx++) {
//            for (int dy = -1; dy <= 1; dy++) {
//                for (int dz = -1; dz <= 1; dz++) {
//                    int sampleGridX = gridX + dx;
//                    int sampleGridY = gridY + dy;
//                    int sampleGridZ = gridZ + dz;
//
//                    long sampleHash = RandomSampler.hashPosition(sampleGridX, sampleGridY, sampleGridZ, salt);
//                    int samplePointX = sampleGridX * sizeX + (randomSamplerX.sample(sampleHash) % sizeX);
//                    int samplePointY = sampleGridY * sizeY + (randomSamplerY.sample(sampleHash) % sizeY);
//                    int samplePointZ = sampleGridZ * sizeZ + (randomSamplerZ.sample(sampleHash) % sizeZ);
//
//                    double currDistance = distance3D(x, y, z, samplePointX, samplePointY, samplePointZ);
//                    if (currDistance < minDistance) {
//                        minDistance = currDistance;
//                        closestCellX = sampleGridX;
//                        closestCellY = sampleGridY;
//                        closestCellZ = sampleGridZ;
//                    }
//                }
//            }
//        }
//        long valueSampleHash = RandomSampler.hashPosition(closestCellX, closestCellY, closestCellZ, salt);
//        return voronoiSampler.sample(valueSampleHash);
//    }
//
//    private static double distance3D(int x1, int y1, int z1, int x2, int y2, int z2) {
//        return StrictMath.sqrt((x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) + (z2-z1) * (z2-z1));
//    }
//
//    private static double distance2D(int x1, int y1, int x2, int y2) {
//        return StrictMath.sqrt((x2-x1) * (x2-x1) + (y2-y1) * (y2-y1));
//    }
//
//    /// Getter Contracts (Fulfilled by record) ///
//    // double minValue();
//    // double maxValue();
//
//    /// ValueNoise Builder ///
//    static WorleyNoise create(RandomSampler randomSampler, int sizeX, int sizeY, int sizeZ,
//                              Interpolator.Type interpolation, Optional<Integer> saltHolder, Optional<ExtraOctaves> extraOctavesHolder) {
//
//        int salt = saltHolder.orElse(0);
//
//        double minValue = randomSampler.minValue();
//        double maxValue = randomSampler.maxValue();
//
//        ExtraOctaves extraOctaves = extraOctavesHolder.orElse(null);
//
//        boolean isTwoD = sizeY == 0;
//
//        Interpolator interpolator = Interpolator.create(interpolation, isTwoD, sizeX, sizeY, sizeZ, randomSampler, salt);
//
//        if (extraOctaves == null || extraOctaves.count() == 0) {
//            return new WorleyNoise(
//                    interpolator, saltHolder,
//                    extraOctavesHolder, true,
//                    null, null,
//                    minValue, maxValue);
//        }
//
//        double[] amplitudes = NoiseDensityFunction.computeNoiseRatios(extraOctaves.count(), extraOctaves.persistence());
//        double[] frequencies = NoiseDensityFunction.computeNoiseRatios(extraOctaves.count(), extraOctaves.lacunarity());
//
//        double amplitudeSum = 0.0D;
//        for (double amplitude : amplitudes) {
//            amplitudeSum += amplitude;
//        }
//        minValue = minValue * amplitudeSum;
//        maxValue = maxValue * amplitudeSum;
//
//        return new WorleyNoise(interpolator, saltHolder,
//                extraOctavesHolder, false,
//                amplitudes, frequencies,
//                minValue, maxValue);
//    }
//
//    /// DensityFunction Method Contracts ///
//    @Override
//    public DensityFunction mapAll(Visitor visitor) {
//        return visitor.apply(new WorleyNoise(interpolator, saltHolder, extraOctavesHolder, singleOctave, frequencies, amplitudes, minValue, maxValue));
//    }
//
//    public static KeyDispatchDataCodec<WorleyNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
//
//    public static MapCodec<WorleyNoise> getMapCodec() {
//        return MAP_CODEC;
//    }
//
//    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
//        return CODEC;
//    }
//
//    @Override
//    double evaluate(int x, int y, int z) {
//        return 0;
//    }
//}
