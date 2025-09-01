package com.klinbee.moredensityfunctions.randomsamplers;

import com.klinbee.moredensityfunctions.registration.AnonymousTypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public sealed interface PoissonSampler
        extends RandomSampler {

    Codec<PoissonSampler> CODEC =
            RecordCodecBuilder.create((instance) ->
                    instance.group(
                            Codec.doubleRange(Double.MIN_NORMAL, Double.MAX_VALUE).fieldOf("lambda").forGetter(PoissonSampler::lambda)
                    ).apply(instance, PoissonSampler::create)
            );

    AnonymousTypedCodec<PoissonSampler> ANON_CODEC = new AnonymousTypedCodec<>("poisson", CODEC);

    static PoissonSampler create(double lambda) {
        if (lambda < 30.0D) {
            double expNegativeLambda = StrictMath.exp(-lambda);
            return new Knuth(lambda, expNegativeLambda);
        }
        return new Normal(lambda, NormalSampler.create(lambda, StrictMath.sqrt(lambda)));
    }

    double lambda();

    record Knuth(double lambda,
                 double expNegativeLambda)
            implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            double p = 1.0D;
            int k = 0;

            do {
                k++;
                p *= RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
            } while (p > expNegativeLambda);

            return k - 1.0D;
        }
    }

    record Normal(double lambda,
                  NormalSampler normalSampler)
            implements PoissonSampler {
        @Override
        public double sample(long hashedSeed) {
            return StrictMath.max(0.0D, StrictMath.round(normalSampler.sample(hashedSeed)));
        }
    }

    @Override
    default double minValue() {
        return 0.0D;
    }

    @Override
    default double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    default AnonymousTypedCodec<? extends RandomSampler> anonCodec() {
        return ANON_CODEC;
    }
}
