package com.klinbee.moredensityfunctions.randomsamplers;

public sealed interface GammaSampler extends RandomSampler
        permits GammaSampler.AhrensDieter, GammaSampler.MarsagliaTsang {

    static GammaSampler create(double shape) {
        if (shape < 1.0D) {
            double inverseShape = 1.0D / shape;
            return new GammaSampler.AhrensDieter(shape, inverseShape);
        }
        double shapeMinusOneThird = shape - 1.0D / 3.0D;
        double inverseSqrtShape = 1.0D / StrictMath.sqrt(9.0D * shapeMinusOneThird);
        return new GammaSampler.MarsagliaTsang(shapeMinusOneThird, inverseSqrtShape);
    }

    record AhrensDieter(double shape, double inverseShape) implements GammaSampler {
        @Override
        public double sample(long hashedSeed) {
            double u, v, w, x, y, z;

            do {
                u = RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                v = RandomSampler.sampleDouble(hashedSeed);
                hashedSeed = RandomSampler.mix(hashedSeed);
                w = u * (1.0D - u);
                y = StrictMath.sqrt(inverseShape / w) * (u - 0.5D);
                x = shape + y;
                if (x >= 0.0D) {
                    z = 64.0D * w * w * w * v * v;
                    if (z <= 1.0D - 2.0D * y * y / x) {
                        break;
                    }
                    if (StrictMath.log(z) <= 2.0D * (shape * StrictMath.log(x / shape) - y)) {
                        break;
                    }
                }
            } while (true);

            return x;
        }
    }

    record MarsagliaTsang(double shapeMinusOneThird, double inverseSqrtShape) implements GammaSampler {
        @Override
        public double sample(long hashedSeed) {
            while (true) {
                double x, v;
                do {
                    x = RandomSampler.sampleGaussian(hashedSeed);
                    hashedSeed = RandomSampler.mix(hashedSeed);
                    v = 1.0D + inverseSqrtShape * x;
                } while (v <= 0);

                v = v * v * v;
                double u = RandomSampler.sampleDouble(hashedSeed);

                if (u < 1.0D - 0.0331D * x * x * x * x) {
                    return shapeMinusOneThird * v;
                }

                if (StrictMath.log(u) < 0.5D * x * x + shapeMinusOneThird * (1.0D - v + StrictMath.log(v))) {
                    return shapeMinusOneThird * v;
                }

            }
        }
    }
}
