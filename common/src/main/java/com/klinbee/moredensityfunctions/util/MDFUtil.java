package com.klinbee.moredensityfunctions.util;

public class MDFUtil {

    ///  Hash Function Constant Longs ///

    private static final long GOLDEN_RATIO = 0x9E3779B97F4A7C15L;

    /// Hashing RNG ///

    /**
     * Wicked Position Hashing Function
     * <p>
     * The entire logic of the RNG for MoreDFs hinges upon this hash.
     * (worldSeed, x, y, z, salt) -> random values, with avalanching properties
     * e.g. (seed, 0, 0, 0, salt) -> n and (seed, 0, 0, 1, salt) -> m
     * Where n and m have very little correlation, and are spread among the long range
     * <p>
     * This hash was found by optimizing and testing ~10 other candidate hash functions
     * The main optimization is packing xy and zsalt together into two 64-bit numbers
     * The prime constants are all also binary concatenated primes
     * E.g. The top and bottom 32-bits are prime, and so is the full 64-bit number
     * <p>
     * PLEASE let me know if this results in issues
     * I had a lot of trouble researching this and finding good candidates...
     * lol thanks ~Klinbee
     *
     * @param worldSeed The literal /seed, seed.
     * @param x         The x cell value input
     * @param y         The y cell value input
     * @param z         The z cell value input
     * @param salt      Randomization factor for different noises, defined by YOU, the user
     * @return The long seed value for the input position
     */
    public static long hashPosition(long worldSeed, int x, int y, int z, int salt) {
        // high 32-bits x, low 32-bits = y
        long xy = ((long)x << 32) | (y & 0xFFFFFFFFL);
        // high 32-bits = z, low 32-bits = salt
        long zsalt = ((long)z << 32) | (salt & 0xFFFFFFFFL);

        long hash = worldSeed;
        hash ^= xy * 5490034563487805519L;
        hash ^= zsalt * 8951897656766556691L;
        hash *= 6019079666967813221L;
        hash ^= hash << 19;

        return hash;
    }

    /**
     * Simple Mixing Function
     * <p>
     * The hashPosition function is great, so this can be kind of bad for efficiency
     * Besides, how many times are you going to be calling mix()s at the same position, seed, and salt?
     * Probably not that many!
     *
     * @param seed Current RNG seed
     * @return The next long RNG seed value
     */
    private static long mix(long seed) {
        seed *= GOLDEN_RATIO;
        seed ^= seed >>> 32;
        return seed;
    }

    /// RNG Functions ///

    /**
     * Returns a double purely by converting a long value into a double on the range (0, 1) (idk if inclusive/exclusive).
     * Relies upon well-mixed seeds being input
     *
     * @param seed Current RNG seed
     * @return double on the range (0,1)
     */
    public static double getDouble(long seed) {
        return (seed >>> 11) * 0x1.0p-53;
    }

    /**
     * Returns an integer in range [0, bound) without modulo bias
     * Same concept as getDouble()
     * Uses Lemire's nearly-divisionless method
     *
     * @param seed  Current RNG seed
     * @param bound Bound of the random int range
     * @return integer on the range [0, bound)
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

        return (int) (m >>> 32);
    }

    /**
     * Returns a Gaussian-distributed value using Box-Muller transform
     * Same concept as getDouble()
     *
     * @param seed Current RNG seed
     * @return The Gaussian-distributed double value
     */
    public static double getGaussian(long seed) {

        double u1 = getDouble(seed);
        double u2 = getDouble(mix(seed));

        // Avoid log(0)
        if (u1 < 1e-15) u1 = 1e-15;

        return StrictMath.sqrt(-2.0 * StrictMath.log(u1)) * StrictMath.cos(2.0 * StrictMath.PI * u2);
    }

    /// RNG Distribution Functions ///

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
        double y = MDFUtil.getGamma(beta, mix(seed + 1));
        return x / (x + y);
    }

    /// Standard Math Functions ///

    public static final double LOG2_E = 1.4426950408889634; // 1/ln(2)

    /**
     * StrictMath.floorDiv() but returns 0 if the denominator is 0
     *
     * @param numerator   int
     * @param denominator int
     * @return The integer division result, or 0
     */
    public static int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
    }
}
