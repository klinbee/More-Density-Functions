package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsCommon;
import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record ValueNoise(RandomDistribution distribution, int sizeX, int sizeY, int sizeZ,
                         Optional<Integer> octaves, Optional<Double> lacunarity,
                         Optional<Double> persistence, boolean useSmoothstep, Optional<Integer> salt
) implements DensityFunction {
    private static final MapCodec<ValueNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomDistribution.CODEC.fieldOf("distribution").forGetter(ValueNoise::distribution),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(ValueNoise::sizeX),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(ValueNoise::sizeY),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(ValueNoise::sizeZ),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("octaves").forGetter(ValueNoise::octaves),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).optionalFieldOf("lacunarity").forGetter(ValueNoise::lacunarity),
            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).optionalFieldOf("persistence").forGetter(ValueNoise::persistence),
            Codec.BOOL.fieldOf("use_smoothstep").forGetter(ValueNoise::useSmoothstep),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(ValueNoise::salt)
    ).apply(instance, (ValueNoise::new)));
    public static final KeyDispatchDataCodec<ValueNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public ValueNoise(RandomDistribution distribution, int sizeX, int sizeY, int sizeZ, Optional<Integer> octaves, Optional<Double> lacunarity, Optional<Double> persistence, boolean useSmoothstep, Optional<Integer> salt) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.persistence = persistence;
        this.useSmoothstep = useSmoothstep;
        this.salt = salt;
        this.distribution = distribution;
    }

    @Override
    public double compute(FunctionContext context) {

        long worldSeed;
        try {
            worldSeed = MoreDensityFunctionsCommon.getWorldSeed();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        int funcSalt = salt.orElse(0);

        int coordX, coordY, coordZ;
        coordX = context.blockX();
        coordY = context.blockY();
        coordZ = context.blockZ();

        if (useSmoothstep) {
            double noiseResult = evalSmoothValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, sizeX, sizeY, sizeZ);
            if (octaves.isEmpty() || lacunarity.isEmpty() || persistence().isEmpty()) {
                return noiseResult;
            }
            double baseLacunarity, currLacunarity, basePersistence, currPersistence;
            baseLacunarity = lacunarity.get();
            basePersistence = persistence.get();
            currLacunarity = baseLacunarity;
            currPersistence = basePersistence;

            int currSizeX, currSizeY, currSizeZ;
            currSizeX = (int) StrictMath.floor(sizeX * currLacunarity);
            currSizeY = (int) StrictMath.floor(sizeY * currLacunarity);
            currSizeZ = (int) StrictMath.floor(sizeZ * currLacunarity);

            noiseResult += currPersistence * evalSmoothValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, currSizeX, currSizeY, currSizeZ);

            for (int i = 1; i < octaves.get(); i++) {
                currLacunarity *= baseLacunarity;
                currPersistence *= basePersistence;
                currSizeX = (int) StrictMath.floor(sizeX * currLacunarity);
                currSizeY = (int) StrictMath.floor(sizeY * currLacunarity);
                currSizeZ = (int) StrictMath.floor(sizeZ * currLacunarity);
                noiseResult += currPersistence * evalSmoothValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, currSizeX, currSizeY, currSizeZ);
            }

            return noiseResult;
        }
        double noiseResult = evalLerpValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, sizeX, sizeY, sizeZ);
        if (octaves.isEmpty() || lacunarity.isEmpty() || persistence().isEmpty()) {
            return noiseResult;
        }
        double baseLacunarity, currLacunarity, basePersistence, currPersistence;
        baseLacunarity = lacunarity.get();
        basePersistence = persistence.get();
        currLacunarity = baseLacunarity;
        currPersistence = basePersistence;

        int currSizeX, currSizeY, currSizeZ;
        currSizeX = (int) StrictMath.floor(sizeX * currLacunarity);
        currSizeY = (int) StrictMath.floor(sizeY * currLacunarity);
        currSizeZ = (int) StrictMath.floor(sizeZ * currLacunarity);

        noiseResult += currPersistence * evalLerpValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, currSizeX, currSizeY, currSizeZ);

        for (int i = 1; i < octaves.get(); i++) {
            currLacunarity *= baseLacunarity;
            currPersistence *= basePersistence;
            currSizeX = (int) StrictMath.floor(sizeX * currLacunarity);
            currSizeY = (int) StrictMath.floor(sizeY * currLacunarity);
            currSizeZ = (int) StrictMath.floor(sizeZ * currLacunarity);
            noiseResult += currPersistence * evalLerpValueNoise(worldSeed, funcSalt, coordX, coordY, coordZ, currSizeX, currSizeY, currSizeZ);
        }

        return noiseResult;
    }

    public double lerp(double a, double b, double t) {
        return a * (1 - t) + b * t;
    }

    public double smoothstep(double t) {
        return t * t * (3.0D - 2.0D * t);
    }

    public double trilerp(double gridVal0, double gridVal1,
                          double gridVal2, double gridVal3,
                          double gridVal4, double gridVal5,
                          double gridVal6, double gridVal7,
                          double cellX, double cellY, double cellZ) {

        double x0, x1, x2, x3, y0, y1;

        x0 = lerp(gridVal0, gridVal1, cellX);
        x1 = lerp(gridVal2, gridVal3, cellX);
        x2 = lerp(gridVal4, gridVal5, cellX);
        x3 = lerp(gridVal6, gridVal7, cellX);

        y0 = lerp(x0, x1, cellY);
        y1 = lerp(x2, x3, cellY);

        return lerp(y0, y1, cellZ);
    }

    public double smoothTrilerp(double gridVal0, double gridVal1,
                          double gridVal2, double gridVal3,
                          double gridVal4, double gridVal5,
                          double gridVal6, double gridVal7,
                          double cellX, double cellY, double cellZ) {

        double x0, x1, x2, x3, y0, y1;

        cellX = smoothstep(cellX);
        cellY = smoothstep(cellY);
        cellZ = smoothstep(cellZ);

        x0 = lerp(gridVal0, gridVal1, cellX);
        x1 = lerp(gridVal2, gridVal3, cellX);
        x2 = lerp(gridVal4, gridVal5, cellX);
        x3 = lerp(gridVal6, gridVal7, cellX);

        y0 = lerp(x0, x1, cellY);
        y1 = lerp(x2, x3, cellY);

        return lerp(y0, y1, cellZ);
    }

    public double evalLerpValueNoise(long worldSeed, int funcSalt, int coordX, int coordY, int coordZ, int sizeX, int sizeY, int sizeZ) {
        int gridX0, gridY0, gridZ0, gridX1, gridY1, gridZ1;
        gridX0 = safeFloorDiv(coordX, sizeX);
        gridY0 = safeFloorDiv(coordY, sizeY);
        gridZ0 = safeFloorDiv(coordZ, sizeZ);
        gridX1 = gridX0 + 1;
        gridY1 = gridY0 + 1;
        gridZ1 = gridZ0 + 1;

        double cellX, cellY, cellZ;
        cellX = cellCoord(coordX, sizeX);
        cellY = cellCoord(coordY, sizeY);
        cellZ = cellCoord(coordZ, sizeZ);

        long hash0, hash1, hash2, hash3, hash4, hash5, hash6, hash7;
        hash0 = MDFUtil.hashPosition(worldSeed, gridX0, gridY0, gridZ0, funcSalt);
        hash1 = MDFUtil.hashPosition(worldSeed, gridX1, gridY0, gridZ0, funcSalt);
        hash2 = MDFUtil.hashPosition(worldSeed, gridX0, gridY1, gridZ0, funcSalt);
        hash3 = MDFUtil.hashPosition(worldSeed, gridX1, gridY1, gridZ0, funcSalt);
        hash4 = MDFUtil.hashPosition(worldSeed, gridX0, gridY0, gridZ1, funcSalt);
        hash5 = MDFUtil.hashPosition(worldSeed, gridX1, gridY0, gridZ1, funcSalt);
        hash6 = MDFUtil.hashPosition(worldSeed, gridX0, gridY1, gridZ1, funcSalt);
        hash7 = MDFUtil.hashPosition(worldSeed, gridX1, gridY1, gridZ1, funcSalt);

        double gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7;
        gridVal0 = distribution.getRandom(hash0);
        gridVal1 = distribution.getRandom(hash1);
        gridVal2 = distribution.getRandom(hash2);
        gridVal3 = distribution.getRandom(hash3);
        gridVal4 = distribution.getRandom(hash4);
        gridVal5 = distribution.getRandom(hash5);
        gridVal6 = distribution.getRandom(hash6);
        gridVal7 = distribution.getRandom(hash7);

        return trilerp(gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7, cellX, cellY, cellZ);
    }

    public double evalSmoothValueNoise(long worldSeed, int funcSalt, int coordX, int coordY, int coordZ, int sizeX, int sizeY, int sizeZ) {
        int gridX0, gridY0, gridZ0, gridX1, gridY1, gridZ1;
        gridX0 = safeFloorDiv(coordX, sizeX);
        gridY0 = safeFloorDiv(coordY, sizeY);
        gridZ0 = safeFloorDiv(coordZ, sizeZ);
        gridX1 = gridX0 + 1;
        gridY1 = gridY0 + 1;
        gridZ1 = gridZ0 + 1;

        double cellX, cellY, cellZ;
        cellX = cellCoord(coordX, sizeX);
        cellY = cellCoord(coordY, sizeY);
        cellZ = cellCoord(coordZ, sizeZ);

        long hash0, hash1, hash2, hash3, hash4, hash5, hash6, hash7;
        hash0 = MDFUtil.hashPosition(worldSeed, gridX0, gridY0, gridZ0, funcSalt);
        hash1 = MDFUtil.hashPosition(worldSeed, gridX1, gridY0, gridZ0, funcSalt);
        hash2 = MDFUtil.hashPosition(worldSeed, gridX0, gridY1, gridZ0, funcSalt);
        hash3 = MDFUtil.hashPosition(worldSeed, gridX1, gridY1, gridZ0, funcSalt);
        hash4 = MDFUtil.hashPosition(worldSeed, gridX0, gridY0, gridZ1, funcSalt);
        hash5 = MDFUtil.hashPosition(worldSeed, gridX1, gridY0, gridZ1, funcSalt);
        hash6 = MDFUtil.hashPosition(worldSeed, gridX0, gridY1, gridZ1, funcSalt);
        hash7 = MDFUtil.hashPosition(worldSeed, gridX1, gridY1, gridZ1, funcSalt);

        double gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7;
        gridVal0 = distribution.getRandom(hash0);
        gridVal1 = distribution.getRandom(hash1);
        gridVal2 = distribution.getRandom(hash2);
        gridVal3 = distribution.getRandom(hash3);
        gridVal4 = distribution.getRandom(hash4);
        gridVal5 = distribution.getRandom(hash5);
        gridVal6 = distribution.getRandom(hash6);
        gridVal7 = distribution.getRandom(hash7);

        return smoothTrilerp(gridVal0, gridVal1, gridVal2, gridVal3, gridVal4, gridVal5, gridVal6, gridVal7, cellX, cellY, cellZ);
    }

    /**
     * No divide by 0, easy way of making it 2D.
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
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
        return visitor.apply(new ValueNoise(this.distribution, this.sizeX, this.sizeY, this.sizeZ, this.octaves, this.lacunarity, this.persistence, this.useSmoothstep, this.salt));
    }

    @Override
    public RandomDistribution distribution() {
        return distribution;
    }

    @Override
    public int sizeX() {
        return sizeX;
    }

    @Override
    public int sizeY() {
        return sizeY;
    }

    @Override
    public int sizeZ() {
        return sizeZ;
    }

    @Override
    public Optional<Integer> octaves() {
        return octaves;
    }

    @Override
    public Optional<Double> lacunarity() {
        return lacunarity;
    }

    @Override
    public Optional<Double> persistence() {
        return persistence;
    }

    @Override
    public Optional<Integer> salt() {
        return salt;
    }

    @Override
    public double minValue() {
        return this.distribution.minValue();
    }

    @Override
    public double maxValue() {
        return this.distribution.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
