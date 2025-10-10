package com.klinbee.moredensityfunctions.densityfunctions;


import com.klinbee.moredensityfunctions.randomsamplers.RandomSampler;
import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.ExtraOctaves;
import com.klinbee.moredensityfunctions.util.Interpolation;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record ValueNoise(RandomSampler randomSampler,
                         int sizeX,
                         int sizeY,
                         int sizeZ,
                         Interpolation interpolation,
                         ExtraOctaves extraOctaves,
                         int salt)
        implements NoiseDensityFunction {

    private static final MapCodec<ValueNoise> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    RandomSampler.CODEC.fieldOf("sampler").forGetter(ValueNoise::randomSampler),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_x").forGetter(ValueNoise::sizeX),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_y").forGetter(ValueNoise::sizeY),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("size_z").forGetter(ValueNoise::sizeZ),
                    Interpolation.CODEC.fieldOf("interpolation").forGetter(ValueNoise::interpolation),
                    ExtraOctaves.CODEC.fieldOf("extra_octaves").orElse(ExtraOctaves.getDefault()).forGetter(ValueNoise::extraOctaves),
                    Codec.INT.fieldOf("salt").orElse(0).forGetter(ValueNoise::salt)
            ).apply(instance, ValueNoise::new)
    );
    public static final TypedCodec<ValueNoise> TYPED_CODEC = new TypedCodec<>("value_noise", KeyDispatchDataCodec.of(MAP_CODEC));

    public ValueNoise(RandomSampler randomSampler,
                      int sizeX,
                      int sizeY,
                      int sizeZ,
                      Interpolation interpolation,
                      ExtraOctaves extraOctaves,
                      int salt) {
        this.randomSampler = randomSampler;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.interpolation = interpolation;
        this.extraOctaves = extraOctaves.finalizedWithSalt(salt);
        this.salt = salt;
    }

    @Override
    public double eval(int x, int y, int z) {
        if (interpolation == Interpolation.NONE) {
            return computeNoise(x, y, z, salt);
        } else {
            // if 2D
            return sizeY == 0 ?
                    computeNoiseInterpolated2D(x, z, salt) :
                    computeNoiseInterpolated3D(x, y, z, salt);
        }
    }

    private double computeNoise(int x, int y, int z, int salt) {
        int gridX = MDFMath.safeFloorDiv(x, sizeX);
        int gridY = MDFMath.safeFloorDiv(y, sizeY);
        int gridZ = MDFMath.safeFloorDiv(z, sizeZ);

        long hash = RandomSampler.hashPosition(gridX, gridY, gridZ, salt);

        return randomSampler.sample(hash);
    }

    private double computeNoiseInterpolated2D(int x, int z, int salt) {
        double cellX = calculateCellCoord(x, sizeX);
        double cellZ = calculateCellCoord(z, sizeZ);

        int gridX0 = MDFMath.safeFloorDiv(x, sizeX);
        int gridZ0 = MDFMath.safeFloorDiv(z, sizeZ);
        int gridX1 = gridX0 + 1;
        int gridZ1 = gridZ0 + 1;

        double val00 = randomSampler.sample(RandomSampler.hashPosition(gridX0, 0, gridZ0, salt));
        double val10 = randomSampler.sample(RandomSampler.hashPosition(gridX1, 0, gridZ0, salt));
        double val01 = randomSampler.sample(RandomSampler.hashPosition(gridX0, 0, gridZ1, salt));
        double val11 = randomSampler.sample(RandomSampler.hashPosition(gridX1, 0, gridZ1, salt));

        double x0 = val00 * (1 - cellX) + val10 * cellX;
        double x1 = val01 * (1 - cellX) + val11 * cellX;

        return x0 * (1 - cellZ) + x1 * cellZ;
    }

    private double computeNoiseInterpolated3D(int x, int y, int z, int salt) {
        double cellX = calculateCellCoord(x, sizeX);
        double cellY = calculateCellCoord(y, sizeY);
        double cellZ = calculateCellCoord(z, sizeZ);

        int gridX0 = MDFMath.safeFloorDiv(x, sizeX);
        int gridY0 = MDFMath.safeFloorDiv(y, sizeY);
        int gridZ0 = MDFMath.safeFloorDiv(z, sizeZ);
        int gridX1 = gridX0 + 1;
        int gridY1 = gridY0 + 1;
        int gridZ1 = gridZ0 + 1;

        double val000 = randomSampler.sample(RandomSampler.hashPosition(gridX0, gridY0, gridZ0, salt));
        double val100 = randomSampler.sample(RandomSampler.hashPosition(gridX1, gridY0, gridZ0, salt));
        double val010 = randomSampler.sample(RandomSampler.hashPosition(gridX0, gridY1, gridZ0, salt));
        double val110 = randomSampler.sample(RandomSampler.hashPosition(gridX1, gridY1, gridZ0, salt));
        double val001 = randomSampler.sample(RandomSampler.hashPosition(gridX0, gridY0, gridZ1, salt));
        double val101 = randomSampler.sample(RandomSampler.hashPosition(gridX1, gridY0, gridZ1, salt));
        double val011 = randomSampler.sample(RandomSampler.hashPosition(gridX0, gridY1, gridZ1, salt));
        double val111 = randomSampler.sample(RandomSampler.hashPosition(gridX1, gridY1, gridZ1, salt));

        double x00 = val000 * (1 - cellX) + val100 * cellX;
        double x10 = val010 * (1 - cellX) + val110 * cellX;
        double x01 = val001 * (1 - cellX) + val101 * cellX;
        double x11 = val011 * (1 - cellX) + val111 * cellX;

        double y0 = x00 * (1 - cellY) + x10 * cellY;
        double y1 = x01 * (1 - cellY) + x11 * cellY;

        return y0 * (1 - cellZ) + y1 * cellZ;
    }

    private double calculateCellCoord(int coord, int size) {
        if (size == 0) {
            return 0.0D;
        }

        double cellCoord = ((double) StrictMath.floorMod(coord, size)) / size;

        if (interpolation == Interpolation.SMOOTHSTEP) {
            return cellCoord * cellCoord * (3.0 - 2.0 * cellCoord);
        }

        return cellCoord;
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new ValueNoise(
                        randomSampler,
                        sizeX,
                        sizeY,
                        sizeZ,
                        interpolation,
                        extraOctaves,
                        salt
                )
        );
    }

    // Add 1 to take the original in account
    @Override
    public double minValue() {
        double totalAmplitude = 1.0D + extraOctaves.maxAmplitude();
        return randomSampler.minValue() * totalAmplitude;
    }

    @Override
    public double maxValue() {
        double totalAmplitude = 1.0D + extraOctaves.maxAmplitude();
        return randomSampler.maxValue() * totalAmplitude;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}