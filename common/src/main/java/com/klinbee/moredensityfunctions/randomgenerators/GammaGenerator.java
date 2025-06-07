package com.klinbee.moredensityfunctions.randomgenerators;

import static com.klinbee.moredensityfunctions.util.MDFUtil.*;

public abstract class GammaGenerator implements RandomGenerator {

    protected final double shape;

    private GammaGenerator(double shape) {
        this.shape = shape;
    }

    static GammaGenerator create(double shape) {
        if (shape < 1.0D) {
            return new GammaGenerator.AhrensDieter(shape);
        }
        return new GammaGenerator.MarsagliaTsang(shape);
    }

    static class AhrensDieter extends GammaGenerator {
        private final double inverseShape;

        AhrensDieter(double shape) {
            super(shape);
            this.inverseShape = 1.0D / shape;
        }

        @Override
        public double getRandom(long hashedSeed) {
            double u, v, w, x, y, z;

            do {
                u = getDouble(hashedSeed);
                hashedSeed = mix(hashedSeed);
                v = getDouble(hashedSeed);
                hashedSeed = mix(hashedSeed);
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

    static class MarsagliaTsang extends GammaGenerator {
        private final double shapeMinusOneThird;
        private final double inverseSqrtShape;

        MarsagliaTsang(double shape) {
            super(shape);
            this.shapeMinusOneThird = shape - 1.0D / 3.0D;
            this.inverseSqrtShape = 1.0D / StrictMath.sqrt(9.0D * shapeMinusOneThird);
        }

        @Override
        public double getRandom(long hashedSeed) {
            while (true) {
                double x, v;
                do {
                    x = getGaussian(hashedSeed);
                    hashedSeed = mix(hashedSeed);
                    v = 1.0D + inverseSqrtShape * x;
                } while (v <= 0);

                v = v * v * v;
                double u = getDouble(hashedSeed);

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
