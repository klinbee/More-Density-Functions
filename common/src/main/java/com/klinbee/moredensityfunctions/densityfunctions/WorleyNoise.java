package com.klinbee.moredensityfunctions.densityfunctions;


import com.klinbee.moredensityfunctions.distancemetrics.DistanceMetric;
import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.DistanceType;
import com.klinbee.moredensityfunctions.util.ExtraOctaves;
import com.klinbee.moredensityfunctions.util.Jitter;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record WorleyNoise(int sizeX,
                          int sizeY,
                          int sizeZ,
                          Jitter jitter,
                          DistanceMetric distanceMetric,
                          DistanceType distanceType,
                          boolean exact,
                          ExtraOctaves extraOctaves,
                          int salt)
        implements NoiseDensityFunction {

    private static final MapCodec<WorleyNoise> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_x").forGetter(WorleyNoise::sizeX),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_y").forGetter(WorleyNoise::sizeY),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_z").forGetter(WorleyNoise::sizeZ),
                    Jitter.CODEC.fieldOf("jitter").forGetter(WorleyNoise::jitter),
                    DistanceMetric.CODEC.fieldOf("distance_metric").forGetter(WorleyNoise::distanceMetric),
                    DistanceType.CODEC.fieldOf("distance_type").forGetter(WorleyNoise::distanceType),
                    Codec.BOOL.fieldOf("exact").forGetter(WorleyNoise::exact),
                    ExtraOctaves.CODEC.fieldOf("extra_octaves").orElse(ExtraOctaves.getDefault()).forGetter(WorleyNoise::extraOctaves),
                    Codec.INT.fieldOf("salt").orElse(0).forGetter(WorleyNoise::salt)
            ).apply(instance, WorleyNoise::new)
    );
    public static final TypedCodec<WorleyNoise> TYPED_CODEC = new TypedCodec<>("value_noise", KeyDispatchDataCodec.of(MAP_CODEC));

    public WorleyNoise(int sizeX,
                       int sizeY,
                       int sizeZ,
                       Jitter jitter,
                       DistanceMetric distanceMetric,
                       DistanceType distanceType,
                       boolean exact,
                       ExtraOctaves extraOctaves,
                       int salt) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.jitter = jitter;
        this.distanceMetric = distanceMetric;
        this.distanceType = distanceType;
        this.exact = exact;
        this.extraOctaves = extraOctaves.finalizedWithSalt(salt);
        this.salt = salt;
    }

    @Override
    public double eval(int x, int y, int z) {
        boolean is2D = sizeY == 0;
        // exact = 2 neighbors = 5x5x5, otherwise = 1 neighbor = 3x3x3
        int neighbors = exact ?
                2 :
                1;

        if (is2D) {
            if (distanceType == DistanceType.F1) {
                return evaluate2DWorleyF1(x, z, neighbors);
            } else {
                return evaluate2DWorleyF2(x, z, neighbors);
            }
        } else {
            if (distanceType == DistanceType.F1) {
                return evaluate3DWorleyF1(x, y, z, neighbors);
            } else {
                return evaluate3DWorleyF2(x, y, z, neighbors);
            }
        }
    }

    private double evaluate2DWorleyF1(int x, int z, int neighbors) {
        // divide into grid cells
        int gridX = MDFMath.safeFloorDiv(x, sizeX);
        int gridZ = MDFMath.safeFloorDiv(z, sizeZ);

        double minDistance = Double.MAX_VALUE;

        // avoid new alloc
        double[] currPos = new double[]{x, 0, z};
        double[] samplePos = new double[3];

        for (int dx = -neighbors; dx <= neighbors; dx++) {
            for (int dz = -neighbors; dz <= neighbors; dz++) {

                // Skip corners on 5x5
                if (neighbors == 2 && ((dx | dz) == 2 || (dx | dz) == -2)) {
                    continue;
                }

                int neighborGridCellX = gridX + dx;
                int neighborGridCellZ = gridZ + dz;

                long sampleHash = RandomSampler.hashPosition(neighborGridCellX, 0, neighborGridCellZ, salt);

                double centerX = neighborGridCellX * sizeX + sizeX * 0.5;
                double centerZ = neighborGridCellZ * sizeZ + sizeZ * 0.5;

                double jitterX = MDFMath.modulo(jitter.samplerX().sample(sampleHash), sizeX) - sizeX * 0.5;
                double jitterY = MDFMath.modulo(jitter.samplerY().sample(sampleHash), sizeY);
                double jitterZ = MDFMath.modulo(jitter.samplerZ().sample(sampleHash), sizeZ) - sizeZ * 0.5;

                samplePos[0] = centerX + jitterX;
                samplePos[1] = jitterY;
                samplePos[2] = centerZ + jitterZ;

                double currDistance = distanceMetric.distance(
                        currPos,
                        samplePos
                );

                if (currDistance < minDistance) {
                    minDistance = currDistance;
                }
            }
        }

        return minDistance;
    }

    private double evaluate3DWorleyF1(int x, int y, int z, int neighbors) {
        // divide into grid cells
        int gridX = MDFMath.safeFloorDiv(x, sizeX);
        int gridY = MDFMath.safeFloorDiv(y, sizeY);
        int gridZ = MDFMath.safeFloorDiv(z, sizeZ);

        double minDistance = Double.MAX_VALUE;

        // avoid new alloc
        double[] currPos = new double[]{x, y, z};
        double[] samplePos = new double[3];

        for (int dx = -neighbors; dx <= neighbors; dx++) {
            for (int dy = -neighbors; dy <= neighbors; dy++) {
                for (int dz = -neighbors; dz <= neighbors; dz++) {

                    // Skip corners on 5x5
                    if (neighbors == 2 && ((dx | dy | dz) == 2 || (dx | dy | dz) == -2)) {
                        continue;
                    }

                    int neighborGridCellX = gridX + dx;
                    int neighborGridCellY = gridY + dy;
                    int neighborGridCellZ = gridZ + dz;

                    long sampleHash = RandomSampler.hashPosition(neighborGridCellX, neighborGridCellY, neighborGridCellZ, salt);

                    double centerX = neighborGridCellX * sizeX + sizeX * 0.5;
                    double centerY = neighborGridCellY * sizeY + sizeY * 0.5;
                    double centerZ = neighborGridCellZ * sizeZ + sizeZ * 0.5;

                    double jitterX = MDFMath.modulo(jitter.samplerX().sample(sampleHash), sizeX) - sizeX * 0.5;
                    double jitterY = MDFMath.modulo(jitter.samplerY().sample(sampleHash), sizeY) - sizeY * 0.5;
                    double jitterZ = MDFMath.modulo(jitter.samplerZ().sample(sampleHash), sizeZ) - sizeZ * 0.5;

                    samplePos[0] = centerX + jitterX;
                    samplePos[1] = centerY + jitterY;
                    samplePos[2] = centerZ + jitterZ;

                    double currDistance = distanceMetric.distance(
                            currPos,
                            samplePos
                    );

                    if (currDistance < minDistance) {
                        minDistance = currDistance;
                    }
                }
            }
        }

        return minDistance;
    }

    private double evaluate2DWorleyF2(int x, int z, int neighbors) {
        // divide into grid cells
        int gridX = MDFMath.safeFloorDiv(x, sizeX);
        int gridZ = MDFMath.safeFloorDiv(z, sizeZ);

        // For F1 and F2 `DistanceType`
        double minDistance = Double.MAX_VALUE;
        double secondMinDistance = Double.MAX_VALUE;

        // avoid new alloc
        double[] currPos = new double[]{x, 0, z};
        double[] samplePos = new double[3];

        for (int dx = -neighbors; dx <= neighbors; dx++) {
            for (int dz = -neighbors; dz <= neighbors; dz++) {

                // Skip corners on 5x5
                if (neighbors == 2 && ((dx | dz) == 2 || (dx | dz) == -2)) {
                    continue;
                }

                int neighborGridCellX = gridX + dx;
                int neighborGridCellZ = gridZ + dz;

                long sampleHash = RandomSampler.hashPosition(neighborGridCellX, 0, neighborGridCellZ, salt);

                double centerX = neighborGridCellX * sizeX + sizeX * 0.5;
                double centerZ = neighborGridCellZ * sizeZ + sizeZ * 0.5;

                double jitterX = MDFMath.modulo(jitter.samplerX().sample(sampleHash), sizeX) - sizeX * 0.5;
                double jitterY = MDFMath.modulo(jitter.samplerY().sample(sampleHash), sizeY);
                double jitterZ = MDFMath.modulo(jitter.samplerZ().sample(sampleHash), sizeZ) - sizeZ * 0.5;

                samplePos[0] = centerX + jitterX;
                samplePos[1] = jitterY;
                samplePos[2] = centerZ + jitterZ;

                double currDistance = distanceMetric.distance(
                        currPos,
                        samplePos
                );

                // For F1 and F2 `DistanceType`
                if (currDistance < minDistance) {
                    secondMinDistance = minDistance;
                    minDistance = currDistance;
                } else if (currDistance < secondMinDistance) {
                    secondMinDistance = currDistance;
                }
            }
        }

        return switch (distanceType) {
            case F2 -> secondMinDistance;
            case F2_ADD_F1 -> secondMinDistance + minDistance;
            case F2_SUB_F1 -> secondMinDistance - minDistance;
            case F2_MUL_F1 -> secondMinDistance * minDistance;
            case F2_DIV_F1 -> secondMinDistance / minDistance; // double = safe div
            default -> throw new AssertionError("MoreDFs attempted to use F1 DistanceType with F2 calculation.");
        };
    }

    private double evaluate3DWorleyF2(int x, int y, int z, int neighbors) {
        // divide into grid cells
        int gridX = MDFMath.safeFloorDiv(x, sizeX);
        int gridY = MDFMath.safeFloorDiv(y, sizeY);
        int gridZ = MDFMath.safeFloorDiv(z, sizeZ);

        // For F1 and F2 `DistanceType`
        double minDistance = Double.MAX_VALUE;
        double secondMinDistance = Double.MAX_VALUE;

        // avoid new alloc
        double[] currPos = new double[]{x, y, z};
        double[] samplePos = new double[3];

        for (int dx = -neighbors; dx <= neighbors; dx++) {
            for (int dy = -neighbors; dy <= neighbors; dy++) {
                for (int dz = -neighbors; dz <= neighbors; dz++) {

                    // Skip corners on 5x5
                    if (neighbors == 2 && ((dx | dy | dz) == 2 || (dx | dy | dz) == -2)) {
                        continue;
                    }

                    int neighborGridCellX = gridX + dx;
                    int neighborGridCellY = gridY + dy;
                    int neighborGridCellZ = gridZ + dz;

                    long sampleHash = RandomSampler.hashPosition(neighborGridCellX, neighborGridCellY, neighborGridCellZ, salt);

                    double centerX = neighborGridCellX * sizeX + sizeX * 0.5;
                    double centerY = neighborGridCellY * sizeY + sizeY * 0.5;
                    double centerZ = neighborGridCellZ * sizeZ + sizeZ * 0.5;

                    double jitterX = MDFMath.modulo(jitter.samplerX().sample(sampleHash), sizeX) - sizeX * 0.5;
                    double jitterY = MDFMath.modulo(jitter.samplerY().sample(sampleHash), sizeY) - sizeY * 0.5;
                    double jitterZ = MDFMath.modulo(jitter.samplerZ().sample(sampleHash), sizeZ) - sizeZ * 0.5;

                    samplePos[0] = centerX + jitterX;
                    samplePos[1] = centerY + jitterY;
                    samplePos[2] = centerZ + jitterZ;

                    double currDistance = distanceMetric.distance(
                            currPos,
                            samplePos
                    );

                    // For F1 and F2 `DistanceType`
                    if (currDistance < minDistance) {
                        secondMinDistance = minDistance;
                        minDistance = currDistance;
                    } else if (currDistance < secondMinDistance) {
                        secondMinDistance = currDistance;
                    }
                }
            }
        }

        return switch (distanceType) {
            case F2 -> secondMinDistance;
            case F2_ADD_F1 -> secondMinDistance + minDistance;
            case F2_SUB_F1 -> secondMinDistance - minDistance;
            case F2_MUL_F1 -> secondMinDistance * minDistance;
            case F2_DIV_F1 -> secondMinDistance / minDistance; // double = safe div
            default -> throw new AssertionError("MoreDFs attempted to use F1 DistanceType with F2 calculation.");
        };
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new WorleyNoise(
                        sizeX,
                        sizeY,
                        sizeZ,
                        jitter,
                        distanceMetric,
                        distanceType,
                        exact,
                        extraOctaves,
                        salt
                )
        );
    }

    // Due to `DistanceMetric`, `DistanceType`, and `jitter`, these are conservative estimates
    @Override
    public double minValue() {
        return switch (distanceType) {
            case F1 -> 0.0D; // Lowest is when on the point
            case F2 -> 0.0D; // Lowest is when the two points are touching
            case F2_SUB_F1 -> 0.0D; // Same situation as above
            case F2_ADD_F1 -> 0.0D; // Same situation as above
            case F2_MUL_F1 -> 0.0D; // Neither negative, 0 dist for A | B = 0 = N * 0 = 0.0D
            case F2_DIV_F1 -> 1.0D; // Equidistant = N/N = 1.0D
        };
    }

    @Override
    public double maxValue() {
        return switch (distanceType) {
            case F1 ->
                    extraOctaves.maxAmplitude() * distanceMetric().maxValue(new double[]{sizeX, sizeY, sizeZ}); // Highest is cell point diagonal to current location
            case F2 ->
                    extraOctaves.maxAmplitude() * 2 * distanceMetric().maxValue(new double[]{sizeX, sizeY, sizeZ}); // Highest is neighbor cell point diagonal to current location
            case F2_SUB_F1 ->
                    extraOctaves.maxAmplitude() * 2 * distanceMetric().maxValue(new double[]{sizeX, sizeY, sizeZ}); // Highest is cell point on current location and neighbor cell point is diagonal to current location
            case F2_ADD_F1 ->
                    extraOctaves.maxAmplitude() * 2 * distanceMetric().maxValue(new double[]{sizeX, sizeY, sizeZ}); // Highest is cell point diagonal to current location and neighbor cell point is diagonal to current location
            case F2_MUL_F1 -> { // Same logic as above
                double dist = extraOctaves.maxAmplitude() * distanceMetric().maxValue(new double[]{sizeX, sizeY, sizeZ});
                yield dist * dist;
            }
            case F2_DIV_F1 -> Double.POSITIVE_INFINITY; // Potentially Unbounded
        };
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}