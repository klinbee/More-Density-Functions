package com.klinbee.moredensityfunctions.noisegenerators;

import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;

public sealed interface ValueNoiseGenerator extends NoiseGenerator
        permits ValueNoiseGenerator.ThreeD, ValueNoiseGenerator.TwoD {

    /// Contract to guarantee all ValueNoiseGenerators contain these getters from the record ///
    RandomSampler randSampler();
    int sizeX();
    int sizeY();
    int sizeZ();


    static ValueNoiseGenerator create(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt, InterpolationType interpType) {
        boolean isTwoD = sizeY == 0;

        if (isTwoD) {
            return switch (interpType) {
                case SMOOTHSTEP -> new ValueNoiseGenerator.TwoD.Smoothstep(randSampler, sizeX, sizeY, sizeZ, salt);
                case LINEAR -> new ValueNoiseGenerator.TwoD.Lerp(randSampler, sizeX, sizeY, sizeZ, salt);
                default -> new ValueNoiseGenerator.TwoD.None(randSampler, sizeX, sizeY, sizeZ, salt);
            };
        }
        return switch (interpType) {
            case SMOOTHSTEP -> new ValueNoiseGenerator.ThreeD.Smoothstep(randSampler, sizeX, sizeY, sizeZ, salt);
            case LINEAR -> new ValueNoiseGenerator.ThreeD.Lerp(randSampler, sizeX, sizeY, sizeZ, salt);
            default -> new ValueNoiseGenerator.ThreeD.None(randSampler, sizeX, sizeY, sizeZ, salt);
        };
    }

    sealed interface TwoD extends ValueNoiseGenerator
            permits TwoD.Lerp, TwoD.Smoothstep, TwoD.None {

        static double interpolate(RandomSampler randSampler, int sizeX, int sizeZ, int salt,
                                    int x, int z, double cellX, double cellZ) {
            int gridX0 = NoiseGenerator.safeFloorDiv(x, sizeX);
            int gridZ0 = NoiseGenerator.safeFloorDiv(z, sizeZ);
            int gridX1 = gridX0 + 1;
            int gridZ1 = gridZ0 + 1;

            long hash0 = RandomSampler.hashPosition(gridX0, 0, gridZ0, salt);
            long hash1 = RandomSampler.hashPosition(gridX1, 0, gridZ0, salt);
            long hash2 = RandomSampler.hashPosition(gridX0, 0, gridZ1, salt);
            long hash3 = RandomSampler.hashPosition(gridX1, 0, gridZ1, salt);

            double gridVal0 = randSampler.sample(hash0);
            double gridVal1 = randSampler.sample(hash1);
            double gridVal2 = randSampler.sample(hash2);
            double gridVal3 = randSampler.sample(hash3);

            double x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
            double x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;

            return x0 * (1 - cellZ) + x1 * cellZ;
        }

        record Lerp(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements TwoD {
            @Override
            public double evaluate(int x, int y, int z) {
                double cellX = NoiseGenerator.cellCoord(x, sizeX);
                double cellZ = NoiseGenerator.cellCoord(z, sizeZ);
                return TwoD.interpolate(randSampler, sizeX, sizeZ, salt, x, z, cellX, cellZ);
            }
        }

        record Smoothstep(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements TwoD {
            @Override
            public double evaluate(int x, int y, int z) {
                double cellX = NoiseGenerator.smoothCellCoord(x, sizeX);
                double cellZ = NoiseGenerator.smoothCellCoord(z, sizeZ);
                return TwoD.interpolate(randSampler, sizeX, sizeZ, salt, x, z, cellX, cellZ);
            }
        }
        
        record None(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements TwoD {
            @Override
            public double evaluate(int x, int y, int z) {
                int gridX0 = NoiseGenerator.safeFloorDiv(x, sizeX);
                int gridZ0 = NoiseGenerator.safeFloorDiv(z, sizeZ);
                long hash = RandomSampler.hashPosition(gridX0, 0, gridZ0, salt);
                return randSampler.sample(hash);
            }
        }
    }

    sealed interface ThreeD extends ValueNoiseGenerator
            permits ThreeD.Lerp, ThreeD.Smoothstep, ThreeD.None {

        static double interpolate(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt,
                                    int x, int y, int z, double cellX, double cellY, double cellZ) {
            int gridX0 = NoiseGenerator.safeFloorDiv(x, sizeX);
            int gridY0 = NoiseGenerator.safeFloorDiv(y, sizeY);
            int gridZ0 = NoiseGenerator.safeFloorDiv(z, sizeZ);
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

            double gridVal0 = randSampler.sample(hash0);
            double gridVal1 = randSampler.sample(hash1);
            double gridVal2 = randSampler.sample(hash2);
            double gridVal3 = randSampler.sample(hash3);
            double gridVal4 = randSampler.sample(hash4);
            double gridVal5 = randSampler.sample(hash5);
            double gridVal6 = randSampler.sample(hash6);
            double gridVal7 = randSampler.sample(hash7);

            double x0 = gridVal0 * (1 - cellX) + gridVal1 * cellX;
            double x1 = gridVal2 * (1 - cellX) + gridVal3 * cellX;
            double x2 = gridVal4 * (1 - cellX) + gridVal5 * cellX;
            double x3 = gridVal6 * (1 - cellX) + gridVal7 * cellX;

            double y0 = x0 * (1 - cellY) + x1 * cellY;
            double y1 = x2 * (1 - cellY) + x3 * cellY;

            return y0 * (1 - cellZ) + y1 * cellZ;
        }

        record Lerp(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements ThreeD {
            @Override
            public double evaluate(int x, int y, int z) {
                double cellX = NoiseGenerator.cellCoord(x, sizeX);
                double cellY = NoiseGenerator.cellCoord(y, sizeY);
                double cellZ = NoiseGenerator.cellCoord(z, sizeZ);
                return ThreeD.interpolate(randSampler, sizeX, sizeY, sizeZ, salt, x, y, z, cellX, cellY, cellZ);
            }
        }

        record Smoothstep(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements ThreeD {
            @Override
            public double evaluate(int x, int y, int z) {
                double cellX = NoiseGenerator.smoothCellCoord(x, sizeX);
                double cellY = NoiseGenerator.smoothCellCoord(y, sizeY);
                double cellZ = NoiseGenerator.smoothCellCoord(z, sizeZ);
                return ThreeD.interpolate(randSampler, sizeX, sizeY, sizeZ, salt, x, y, z, cellX, cellY, cellZ);
            }
        }

        record None(RandomSampler randSampler, int sizeX, int sizeY, int sizeZ, int salt) implements ThreeD {
            @Override
            public double evaluate(int x, int y, int z) {
                int gridX0 = NoiseGenerator.safeFloorDiv(x, sizeX);
                int gridY0 = NoiseGenerator.safeFloorDiv(y, sizeY);
                int gridZ0 = NoiseGenerator.safeFloorDiv(z, sizeZ);
                long hash = RandomSampler.hashPosition(gridX0, gridY0, gridZ0, salt);
                return randSampler.sample(hash);
            }
        }
    }
}
