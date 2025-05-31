package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsCommon;
import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record CellularNoise(int sizeX, int sizeY, int sizeZ, Optional<Integer> salt, RandomDistribution distribution) implements DensityFunction {
    private static final MapCodec<CellularNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(CellularNoise::sizeX),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(CellularNoise::sizeY),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(CellularNoise::sizeZ),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("salt").forGetter(CellularNoise::salt),
            RandomDistribution.CODEC.fieldOf("distribution").forGetter(CellularNoise::distribution)).apply(instance, (CellularNoise::new)));
    public static final KeyDispatchDataCodec<CellularNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public CellularNoise(int sizeX, int sizeY, int sizeZ, Optional<Integer> salt, RandomDistribution distribution) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.salt = salt;
        this.distribution = distribution;
    }

    @Override
    public double compute(FunctionContext context) {
        int x = safeFloorDiv(context.blockX(), sizeX);
        int y = safeFloorDiv(context.blockY(), sizeY);
        int z = safeFloorDiv(context.blockZ(), sizeZ);
        long hash;

        try {
            hash = MDFUtil.hashPosition(MoreDensityFunctionsCommon.getWorldSeed(), x, y, z, salt.orElse(0));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return distribution.getRandom(hash);
    }

    /**
     * No divide by 0, easy way of making it 2D.
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

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public  DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new CellularNoise(this.sizeX, this.sizeY, this.sizeZ, this.salt, this.distribution));
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
    public Optional<Integer> salt() {
        return salt;
    }

    @Override
    public RandomDistribution distribution() {
        return distribution;
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
    public  KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
