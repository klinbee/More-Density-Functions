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

public record ValueNoise(Interpolator interpolator, Optional<Integer> saltHolder,
                         Optional<ExtraOctaves> extraOctavesHolder, boolean singleOctave,
                         double[] frequencies, double[] amplitudes,
                         double minValue, double maxValue
) implements NoiseDensityFunction {

    private static final MapCodec<ValueNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomSampler.CODEC.fieldOf("sampler").forGetter(df -> df.interpolator().randomSampler()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(df -> df.interpolator().sizeX()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(df -> df.interpolator().sizeY()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(df -> df.interpolator().sizeZ()),
            Interpolator.Type.CODEC.fieldOf("interpolation").forGetter(df -> df.interpolator().interpolation()),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(ValueNoise::saltHolder),
            NoiseDensityFunction.ExtraOctaves.CODEC.optionalFieldOf("extra_octaves").forGetter(ValueNoise::extraOctavesHolder)
    ).apply(instance, ValueNoise::create));

    /// Evaluates the noise based on the ValueNoise.Interpolator instance ///
    @Override
    public double evaluate(int x, int y, int z) {
        return interpolator.interpolate(x, y, z);
    }

    /// Getter Contracts (Fulfilled by record) ///
    // double minValue();
    // double maxValue();

    /// Interpolator Types ///
    sealed interface Interpolator
            permits Interpolator.None2D, Interpolator.None3D,
            Interpolator.Lerp2D, Interpolator.Lerp3D {

        /// Getter Contracts ///
        Interpolator.Type interpolation();

        int sizeX();

        int sizeY();

        int sizeZ();

        RandomSampler randomSampler();

        /// ValueNoise.Type Enum CODEC ///

        enum Type implements StringRepresentable {
            NONE("none"),
            LERP("lerp"),
            SMOOTHSTEP("smoothstep");

            public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

            private final String name;

            Type(String name) {
                this.name = name;
            }

            @Override
            public String getSerializedName() {
                return this.name;
            }
        }

        /// Interpolation Builder ///
        static Interpolator create(Interpolator.Type interpolation, boolean is2D,
                                   int sizeX, int sizeY, int sizeZ,
                                   RandomSampler randomSampler, int salt) {
            if (is2D) {
                return switch (interpolation) {
                    case NONE -> new Interpolator.None2D(interpolation, sizeX, sizeY, sizeZ, randomSampler, salt);
                    case LERP ->
                            new Interpolator.Lerp2D(interpolation, Interpolator::calcCellCoord, sizeX, sizeY, sizeZ, randomSampler, salt);
                    case SMOOTHSTEP ->
                            new Interpolator.Lerp2D(interpolation, Interpolator::calcCellCoordSmoothstep, sizeX, sizeY, sizeZ, randomSampler, salt);
                };
            }
            return switch (interpolation) {
                case NONE -> new Interpolator.None3D(interpolation, sizeX, sizeY, sizeZ, randomSampler, salt);
                case LERP ->
                        new Interpolator.Lerp3D(interpolation, Interpolator::calcCellCoord, sizeX, sizeY, sizeZ, randomSampler, salt);
                case SMOOTHSTEP ->
                        new Interpolator.Lerp3D(interpolation, Interpolator::calcCellCoordSmoothstep, sizeX, sizeY, sizeZ, randomSampler, salt);
            };
        }

        /// Cell Calculation Methods (Lerp/Smoothstep) ///
        @FunctionalInterface
        interface CellCalculator {
            double calculate(int coord, int size);
        }

        static double calcCellCoord(int coord, int size) {
            if (size == 0) {
                return 0.0D;
            }
            return ((double) StrictMath.floorMod(coord, size)) / size;
        }

        static double calcCellCoordSmoothstep(int coord, int size) {
            double cellCoord = calcCellCoord(coord, size);
            return cellCoord * cellCoord * (3.0D - 2.0D * cellCoord);
        }

        /// Interpolate Method Contract ///
        double interpolate(int x, int y, int z);

        /// Interpolator Implementations ///
        // 2D Raw Value Cells
        record None2D(Interpolator.Type interpolation,
                      int sizeX, int sizeY, int sizeZ,
                      RandomSampler randomSampler, int salt) implements Interpolator {
            @Override
            public double interpolate(int x, int y, int z) {
                int gridX0 = NoiseDensityFunction.safeFloorDiv(x, sizeX);
                int gridZ0 = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
                long hash = RandomSampler.hashPosition(gridX0, 0, gridZ0, salt);
                return randomSampler.sample(hash);
            }
        }

        // 3D Raw Value Cells
        record None3D(Interpolator.Type interpolation,
                      int sizeX, int sizeY, int sizeZ,
                      RandomSampler randomSampler, int salt) implements Interpolator {
            @Override
            public double interpolate(int x, int y, int z) {
                int gridX0 = NoiseDensityFunction.safeFloorDiv(x, sizeX);
                int gridY0 = NoiseDensityFunction.safeFloorDiv(y, sizeY);
                int gridZ0 = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
                long hash = RandomSampler.hashPosition(gridX0, gridY0, gridZ0, salt);
                return randomSampler.sample(hash);
            }
        }

        // Bilinear Interpolation of Value Cells
        // Smoothstep optionally done through CellCalculator
        record Lerp2D(Interpolator.Type interpolation, CellCalculator cellCalculator,
                      int sizeX, int sizeY, int sizeZ,
                      RandomSampler randomSampler, int salt) implements Interpolator {
            @Override
            public double interpolate(int x, int y, int z) {
                // Default or Smoothstep
                double cellX = cellCalculator.calculate(x, sizeX);
                double cellZ = cellCalculator.calculate(z, sizeZ);

                int gridX0 = NoiseDensityFunction.safeFloorDiv(x, sizeX);
                int gridZ0 = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
                int gridX1 = gridX0 + 1;
                int gridZ1 = gridZ0 + 1;

                long hash0 = RandomSampler.hashPosition(gridX0, 0, gridZ0, salt);
                long hash1 = RandomSampler.hashPosition(gridX1, 0, gridZ0, salt);
                long hash2 = RandomSampler.hashPosition(gridX0, 0, gridZ1, salt);
                long hash3 = RandomSampler.hashPosition(gridX1, 0, gridZ1, salt);

                double gridVal0 = randomSampler.sample(hash0);
                double gridVal1 = randomSampler.sample(hash1);
                double gridVal2 = randomSampler.sample(hash2);
                double gridVal3 = randomSampler.sample(hash3);

                double x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
                double x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;

                return x0 * (1 - cellZ) + x1 * cellZ;
            }
        }

        // Trilinear Interpolation of Value Cells
        // Smoothstep optionally done through CellCalculator
        record Lerp3D(Interpolator.Type interpolation, CellCalculator cellCalculator,
                      int sizeX, int sizeY, int sizeZ,
                      RandomSampler randomSampler, int salt) implements Interpolator {
            @Override
            public double interpolate(int x, int y, int z) {
                // Default or Smoothstep
                double cellX = cellCalculator.calculate(x, sizeX);
                double cellY = cellCalculator.calculate(y, sizeY);
                double cellZ = cellCalculator.calculate(z, sizeZ);

                int gridX0 = NoiseDensityFunction.safeFloorDiv(x, sizeX);
                int gridY0 = NoiseDensityFunction.safeFloorDiv(y, sizeY);
                int gridZ0 = NoiseDensityFunction.safeFloorDiv(z, sizeZ);
                int gridX1 = gridX0 + 1;
                int gridY1 = gridY0 + 1;
                int gridZ1 = gridZ0 + 1;

                long hash0 = RandomSampler.hashPosition(gridX0, gridY0, gridZ0, salt);
                long hash1 = RandomSampler.hashPosition(gridX1, gridY0, gridZ0, salt);
                long hash2 = RandomSampler.hashPosition(gridX0, gridY1, gridZ0, salt);
                long hash3 = RandomSampler.hashPosition(gridX1, gridY1, gridZ0, salt);
                long hash4 = RandomSampler.hashPosition(gridX0, gridY0, gridZ1, salt);
                long hash5 = RandomSampler.hashPosition(gridX1, gridY0, gridZ1, salt);
                long hash6 = RandomSampler.hashPosition(gridX0, gridY1, gridZ1, salt);
                long hash7 = RandomSampler.hashPosition(gridX1, gridY1, gridZ1, salt);

                double gridVal0 = randomSampler.sample(hash0);
                double gridVal1 = randomSampler.sample(hash1);
                double gridVal2 = randomSampler.sample(hash2);
                double gridVal3 = randomSampler.sample(hash3);
                double gridVal4 = randomSampler.sample(hash4);
                double gridVal5 = randomSampler.sample(hash5);
                double gridVal6 = randomSampler.sample(hash6);
                double gridVal7 = randomSampler.sample(hash7);

                double x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
                double x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;
                double x2 = gridVal4 * (1 - cellX) + gridVal5 * cellX;
                double x3 = gridVal6 * (1 - cellX) + gridVal7 * cellX;

                double y0 = x0 * (1 - cellY) + x1 * cellY;
                double y1 = x2 * (1 - cellY) + x3 * cellY;

                return y0 * (1 - cellZ) + y1 * cellZ;
            }
        }
    }

    /// ValueNoise Builder ///
    static ValueNoise create(RandomSampler randomSampler, int sizeX, int sizeY, int sizeZ,
                             Interpolator.Type interpolation, Optional<Integer> saltHolder, Optional<ExtraOctaves> extraOctavesHolder) {

        int salt = saltHolder.orElse(0);

        double minValue = randomSampler.minValue();
        double maxValue = randomSampler.maxValue();

        ExtraOctaves extraOctaves = extraOctavesHolder.orElse(null);

        boolean isTwoD = sizeY == 0;

        Interpolator interpolator = Interpolator.create(interpolation, isTwoD, sizeX, sizeY, sizeZ, randomSampler, salt);

        if (extraOctaves == null || extraOctaves.count() == 0) {
            return new ValueNoise(
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

        return new ValueNoise(interpolator, saltHolder,
                extraOctavesHolder, false,
                amplitudes, frequencies,
                minValue, maxValue);
    }

    /// DensityFunction Method Contracts ///
    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ValueNoise(interpolator, saltHolder, extraOctavesHolder, singleOctave, frequencies, amplitudes, minValue, maxValue));
    }

    public static KeyDispatchDataCodec<ValueNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static MapCodec<ValueNoise> getMapCodec() {
        return MAP_CODEC;
    }

    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
