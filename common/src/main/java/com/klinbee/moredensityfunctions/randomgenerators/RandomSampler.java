package com.klinbee.moredensityfunctions.randomgenerators;

public interface RandomSampler {

    ///  Sampler Builders

    static BetaSampler buildBeta(double alpha, double beta) {
        return BetaSampler.create(alpha, beta);
    }

    static BinomialSampler buildBinomial(int numTrials, double probability) {
        return BinomialSampler.create(numTrials, probability);
    }

    static ExponentialSampler buildExponential(double lambda) {
        return ExponentialSampler.create(lambda);
    }

    static GammaSampler buildGamma(double shape) {
        return GammaSampler.create(shape);
    }

    static GeometricSampler buildGeometric(double probability) {
        return GeometricSampler.create(probability);
    }

    static NormalSampler buildNormal(double mean, double stdDev) {
        return NormalSampler.create(mean, stdDev);
    }

    static PoissonSampler buildPoisson(double lambda) {
        return PoissonSampler.create(lambda);
    }

    static UniformSampler buildUniform(double min, double max) {
        return UniformSampler.create(min, max);
    }

    /// Core Method

    /**
     * Samples a value from the Sampler's distribution using the given already hashedSeed.
     *
     * @param hashedSeed The seed being used for the current sample.
     * @return The double value sampled from the distribution.
     */
    double sample(long hashedSeed);

    /// Seed Randomization

    /**
     * <p>Wicked Salted 3D Positional Hashing Function
     *
     * <p>The entire logic of the RNG for MoreDFs hinges upon this Hash function.
     * The design goal is to ensure small changes in the worldSeed, x, y, z, and salt
     * result in large changes in the output hashedSeed value. This *needs* to be the case, otherwise the values will repeat in some way.
     *
     * <p> For example, {@code RandomSampler.sampleDouble(seed)} <i>literally</i> just converts the seed, which is a {@code long} value,
     * into a {@code double} on the range of (0,1). So even if the difference between output hashedSeed values is 1,000,000,
     * that is nothing compared to the size of a {@code long}, and you will see a similar output value. If the difference is only 1,
     * then there will virtually be no difference.
     *
     * <p> This hash was found by optimizing and testing ~10 other candidate hash functions.
     * The main optimization is packing xy and zsalt together into two {@code long}s, which saves multiplying 2 extra {@code int}s
     * converted to {@code long}s.
     * <p>The prime constants are binary concatenated primes. E.g. The top and bottom 32-bits and the {@code long} value itself are all primes.
     * <p>PLEASE let me know if this results in issues. I am not a number theory expert, just a nerd that likes programming and math.
     * Thank you!
     * @param worldSeed The literal /seed, seed.
     * @param x         The x cell value input
     * @param y         The y cell value input
     * @param z         The z cell value input
     * @param salt      Randomization factor for different noises, defined by YOU, the user
     * @return The long seed value for the input position
     */
    static long hashPosition(long worldSeed, int x, int y, int z, int salt) {
        // high 32-bits x, low 32-bits = y
        long xy = ((long) x << 32) | (y & 0xFFFFFFFFL);
        // high 32-bits = z, low 32-bits = salt
        long zsalt = ((long) z << 32) | (salt & 0xFFFFFFFFL);

        long seed = worldSeed;
        seed ^= xy * 5490034563487805519L;
        seed ^= zsalt * 8951897656766556691L;
        seed *= 6019079666967813221L;
        seed ^= seed << 19;

        return seed;
    }

    /**
     * <p>PCG-ish Mixing Function
     * <p>This is only called anytime the same position is queried for another random roll.
     * E.g. using {@code BinomialSampler.sample(seed)}.
     *
     * @param seed Current RNG seed
     * @return The next long RNG seed value
     */
    static long mix(long seed) {
        seed = seed * 6364136223846793005L + 1442695040888963407L; // PCG Constants
        long finalSeed = ((seed >>> (int)(seed >>> 59) + 5) ^ seed) * -4132994306676758123L;
        return finalSeed >>> 43 ^ finalSeed;
    }

    /// RNG Functions

    /**
     * <p>Returns a {@code double} purely by converting a long value into a double on the range [0, 1)
     * <p>Please read {@code hashPosition(...)} for more details on RNG implementation.
     * @param seed current rng seed
     * @return double on the range [0,1)
     */
    static double sampleDouble(long seed) {
        return (seed >>> 11) * 0x1.0p-53;
    }

    /**
     * <p>Returns an {@code integer} in range [0, bound)
     * <p>Uses Lemire's nearly-divisionless method to avoid modulo bias
     *
     * @param seed  current rng seed
     * @param bound bound of the random int range
     * @return integer on the range [0, bound)
     */
    static int sampleInt(long seed, int bound) {
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
     * <p>Returns a Gaussian-distributed {@code double} value
     * <p> Using a Box-Muller transform
     *
     * @param seed current rng seed
     * @return double gaussian-distributed value
     */
    static double sampleGaussian(long seed) {

        double u1 = RandomSampler.sampleDouble(seed);
        double u2 = RandomSampler.sampleDouble(mix(seed));

        // Avoid log(0)
        if (u1 < 1e-15) u1 = 1e-15;

        return StrictMath.sqrt(-2.0 * StrictMath.log(u1)) * StrictMath.cos(2.0 * StrictMath.PI * u2);
    }
}
