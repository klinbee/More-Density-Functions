package com.klinbee.moredensityfunctions.noisegenerators;

import com.klinbee.moredensityfunctions.densityfunctions.ValueNoise;
import com.klinbee.moredensityfunctions.distribution.RandomDistribution;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

public interface NoiseGenerator {

    static ValueNoiseGenerator buildValueNoise(RandomDistribution distribution, int sizeX, int sizeY, int sizeZ, int salt, InterpolationType interpType) {
        return ValueNoiseGenerator.create(distribution, sizeX, sizeY, sizeZ, salt, interpType);
    }

    enum InterpolationType implements StringRepresentable {
        NONE("none"),
        LINEAR("lerp"),
        SMOOTHSTEP("smoothstep");

        public static final Codec<InterpolationType> CODEC = StringRepresentable.fromEnum(InterpolationType::values);

        private final String name;

        InterpolationType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    record ExtraOctaves(int count, double lacunarity, double persistence) {
        public static final Codec<ExtraOctaves> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("count").forGetter(ExtraOctaves::count),
                        Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lacunarity").forGetter(ExtraOctaves::lacunarity),
                        Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("persistence").forGetter(ExtraOctaves::persistence)
                ).apply(instance, ExtraOctaves::new)
        );
    }

    /**
     * Noise Generator Contract
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    double evaluate(int x, int y, int z);

    /// Noise Utility Methods ///

    static double cellCoord(int coord, int size) {
        if (size == 0) {
            return 0.0D;
        }
        return ((double) StrictMath.floorMod(coord, size)) / size;
    }

    static int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
    }

    static double smoothCellCoord(int coord, int size) {
        if (size == 0) {
            return 0.0D;
        }
        double cellCoord = ((double) StrictMath.floorMod(coord, size)) / size;
        return cellCoord * cellCoord * (3.0D - 2.0D * cellCoord);
    }

    static double[] computeNoiseRatios(int octaves, double ratio) {
        double[] ratios = new double[octaves];
        double baseRatio = ratio;
        for (int i = 0; i < octaves; i++) {
            ratios[i] = ratio;
            ratio *= baseRatio;
        }
        return ratios;
    }

}
