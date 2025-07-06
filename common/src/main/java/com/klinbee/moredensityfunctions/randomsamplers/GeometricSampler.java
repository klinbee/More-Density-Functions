package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;

public record GeometricSampler(double probability, double inverseLog1p) implements RandomSampler {

    public static final MapCodec<GeometricSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, 1.0D).fieldOf("probability").forGetter(GeometricSampler::probability)
            ).apply(instance, GeometricSampler::create)
    );

    static GeometricSampler create(double probability) {
        double inverseLog1p = 1.0D / StrictMath.log(1.0D - probability);
        return new GeometricSampler(probability, inverseLog1p);
    }

    @Override
    public double sample(long hashedSeed) {
        return Mth.ceil(inverseLog1p * StrictMath.log(1.0D - RandomSampler.sampleDouble(hashedSeed)));
    }

    @Override
    public double minValue() {
        return 1.0D;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    public static final KeyDispatchDataCodec<GeometricSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static MapCodec<GeometricSampler> getMapCodec() {
        return MAP_CODEC;
    }

    public MapCodec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
