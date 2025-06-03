package com.klinbee.moredensityfunctions.util;

public class MDFUtil {


    public static int getBinomial(int n, double p, long seed) {
        int successes = 0;
        for (int i = 0; i < n; i++) {
            if (getDouble(seed) < p) {
                successes++;
            }
            seed = mix(seed);
        }
        return successes;
    }

    public static int getGeometric(double p, long seed) {
        return (int) Math.ceil(Math.log(1 - getDouble(seed)) / Math.log(1 - p));
    }

    public static double getUniform(double min, double max, long seed) {
        return min + (max - min) * getDouble(seed);
    }

    public static double getNormal(double mean, double stdDev, long seed) {
        return mean + stdDev * getGaussian(seed);
    }

    public static double getExponential(double lambda, long seed) {
        return -Math.log(1 - getDouble(seed)) / lambda;
    }

    public static int getPoisson(double lambda, long seed) {
        // Knuth's algorithm
        if (lambda < 30) {
            double L = Math.exp(-lambda);
            double p = 1.0;
            int k = 0;

            do {
                k++;
                p *= getDouble(seed);
                seed = mix(seed);
            } while (p > L);

            return k - 1;
        }
        // Normal approximation
        else {
            return Math.max(0, (int) Math.round(MDFUtil.getNormal(lambda, Math.sqrt(lambda), seed)));
        }
    }

    public static double getGamma(double shape, long seed) {
        if (shape < 1.0) {
            // Ahrens-Dieter method for shape < 1
            double c = (1.0 / shape);
            double u, v, w, x, y, z;

            do {
                u = getDouble(seed);
                seed = mix(seed);
                v = getDouble(seed);
                seed = mix(seed);
                w = u * (1.0 - u);
                y = Math.sqrt(c / w) * (u - 0.5);
                x = shape + y;
                if (x >= 0) {
                    z = 64.0 * w * w * w * v * v;
                    if (z <= 1.0 - 2.0 * y * y / x) {
                        break;
                    }
                    if (Math.log(z) <= 2.0 * (shape * Math.log(x / shape) - y)) {
                        break;
                    }
                }
            } while (true);

            return x;
        } else {
            // Marsaglia and Tsang method for shape >= 1
            double d = shape - 1.0 / 3.0;
            double c = 1.0 / Math.sqrt(9.0 * d);

            while (true) {
                double x, v;
                do {
                    x = getGaussian(seed);
                    seed = mix(seed);
                    v = 1.0 + c * x;
                } while (v <= 0);

                v = v * v * v;
                double u = getDouble(seed);

                if (u < 1.0 - 0.0331 * x * x * x * x) {
                    return d * v;
                }

                if (Math.log(u) < 0.5 * x * x + d * (1.0 - v + Math.log(v))) {
                    return d * v;
                }
            }
        }
    }

    public static double getBeta(double alpha, double beta, long seed) {
        double x = MDFUtil.getGamma(alpha, seed);
        double y = MDFUtil.getGamma(beta, mix(seed+1));
        return x / (x + y);
    }


    public static long hashPosition(long worldSeed, int x, int y, int z, int salt) {
        long GOLDEN_RATIO = 0x9E3779B97F4A7C15L;
        long hash = worldSeed ^
                ((long)x * GOLDEN_RATIO) ^
                ((long)y * (GOLDEN_RATIO << 1)) ^
                ((long)z * (GOLDEN_RATIO << 2)) ^
                ((long)salt * (GOLDEN_RATIO << 3));

        hash ^= hash >>> 32;
        hash *= GOLDEN_RATIO;
        hash ^= hash >>> 32;

        return hash;
    }

    public static double getDouble(long seed) {
        return (seed >>> 11) * 0x1.0p-53;
    }

    /**
     * Get an integer in range [0, bound) without modulo bias
     * Uses Lemire's nearly-divisionless method - fastest approach
     */
    public static int getInt(long seed, int bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        long x = (seed >>> 32) & 0xFFFFFFFFL;
        long m = x * bound;
        long l = m & 0xFFFFFFFFL;

        if (l < bound) {
            // Calculate threshold: (2^32) % bound
            long t = (1L << 32) % bound;
            while (l < t) {
                seed = mix(seed);
                x = (seed >>> 32) & 0xFFFFFFFFL;
                m = x * bound;
                l = m & 0xFFFFFFFFL;
            }
        }

        return (int)(m >>> 32);
    }

    /**
     * Get Gaussian-distributed value using Box-Muller transform
     */
    public static double getGaussian(long seed) {
        // Mix seed to get second value
        long seed2 = mix(seed);

        double u1 = getDouble(seed);
        double u2 = getDouble(seed2);

        // Avoid log(0)
        if (u1 < 1e-15) u1 = 1e-15;

        return StrictMath.sqrt(-2.0 * StrictMath.log(u1)) * StrictMath.cos(2.0 * StrictMath.PI * u2);
    }

    /**
     * Mix function to generate additional randomness when needed
     */
    private static long mix(long seed) {
        seed = (seed ^ (seed >>> 33)) * 0xFF51AFD7ED558CCDL;
        seed = (seed ^ (seed >>> 33)) * 0xC4CEB9FE1A85EC53L;
        return seed ^ (seed >>> 33);
    }
}
