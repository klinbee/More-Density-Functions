package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsCommon;
import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.klinbee.moredensityfunctions.randomgenerators.RandomSampler;
import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public record CellularNoise(RandomDistribution distribution,
                            int sizeX, int sizeY, int sizeZ,
                            Optional<Integer> saltHolder, int salt
) implements DensityFunction {
    private static final MapCodec<CellularNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            RandomDistribution.CODEC.fieldOf("distribution").forGetter(CellularNoise::distribution),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_x").forGetter(CellularNoise::sizeX),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_y").forGetter(CellularNoise::sizeY),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("size_z").forGetter(CellularNoise::sizeZ),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.optionalFieldOf("saltHolder").forGetter(CellularNoise::saltHolder)
    ).apply(instance, (distribution, sizeX, sizeY, sizeZ, saltHolder) ->
            new CellularNoise(distribution, sizeX, sizeY, sizeZ, saltHolder, saltHolder.orElse(0))));
    public static final KeyDispatchDataCodec<CellularNoise> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
    private static long worldSeed;
    private static boolean worldSeedInitialized;

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

        int x = MDFUtil.safeFloorDiv(context.blockX(), sizeX);
        int y = MDFUtil.safeFloorDiv(context.blockY(), sizeY);
        int z = MDFUtil.safeFloorDiv(context.blockZ(), sizeZ);
        long hash = RandomSampler.hashPosition(worldSeed, x, y, z, salt);

        return distribution.getRandom(hash);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new CellularNoise(this.distribution, this.sizeX, this.sizeY, this.sizeZ, this.saltHolder, this.salt));
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

    public Optional<Integer> saltHolder() {
        return saltHolder;
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
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
