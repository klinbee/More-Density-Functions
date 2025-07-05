package com.klinbee.moredensityfunctions.randomsamplers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public record BetaSampler(double alpha, double beta,
                          GammaSampler alphaGen, GammaSampler betaGen
) implements RandomSampler {

    public static final MapCodec<BetaSampler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("alpha").forGetter(BetaSampler::alpha),
                    Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("beta").forGetter(BetaSampler::beta)
            ).apply(instance, BetaSampler::create)
    );


    static BetaSampler create(double alpha, double beta) {
        return new BetaSampler(alpha, beta, RandomSampler.buildGamma(alpha, 1.0D), RandomSampler.buildGamma(beta, 1.0D));
    }

    public double minValue() {
        return 0.0D;
    }

    public double maxValue() {
        return 1.0D;
    }

    @Override
    public double sample(long hashedSeed) {
        double x = alphaGen.sample(hashedSeed);
        double y = betaGen.sample(RandomSampler.mix(hashedSeed + 1L));
        return x / (x + y);
    }

    public static KeyDispatchDataCodec<BetaSampler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    static MapCodec<BetaSampler> getMapCodec() {
        return MAP_CODEC;
    }

    public MapCodec<? extends RandomSampler> codec() {
        return CODEC.codec();
    }
}
