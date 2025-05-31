package com.klinbee.moredensityfunctions.util;

import com.klinbee.moredensityfunctions.distribution.DistributionFunction;

import java.util.Random;

public class MDFUtil {

    public static int binomial(int n, float p, long seed) {
        Random random = new Random(seed);
        int successes = 0;
        for (int i = 0; i < n; i++) {
            if (random.nextFloat() < p) {
                successes++;
            }
        }
        return successes;
    }

    public static int geometric(double p, long seed) {
        Random random = new Random(seed);
        return (int) Math.ceil(Math.log(1 - random.nextDouble()) / Math.log(1 - p));
    }

    public static double uniform(double min, double max, long seed) {
        Random random = new Random(seed);
        return min + (max - min) * random.nextDouble();
    }

    public static double normal(double mean, double stdDev, long seed) {
        Random random = new Random(seed);
        return mean + stdDev * random.nextGaussian();
    }

    public static double exponential(double lambda, long seed) {
        Random random = new Random(seed);
        return -Math.log(1 - random.nextDouble()) / lambda;
    }

    public static int poisson(double lambda, long seed) {
        Random random = new Random(seed);
        // Knuth's algorithm
        if (lambda < 30) {
            double L = Math.exp(-lambda);
            double p = 1.0;
            int k = 0;

            do {
                k++;
                p *= random.nextDouble();
            } while (p > L);

            return k - 1;
        }
        // Normal approximation
        else {
            return Math.max(0, (int) Math.round(DistributionFunction.normal(lambda, Math.sqrt(lambda), seed)));
        }
    }

    public static double gamma(double shape, long seed) {
        Random random = new Random(seed);
        if (shape < 1.0) {
            // Ahrens-Dieter method for shape < 1
            double c = (1.0 / shape);
            double d = ((1.0 - shape) * Math.pow(shape, (shape / (1.0 - shape))));
            double u, v, w, x, y, z;

            do {
                u = random.nextDouble();
                v = random.nextDouble();
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
                    x = random.nextGaussian();
                    v = 1.0 + c * x;
                } while (v <= 0);

                v = v * v * v;
                double u = random.nextDouble();

                if (u < 1.0 - 0.0331 * x * x * x * x) {
                    return d * v;
                }

                if (Math.log(u) < 0.5 * x * x + d * (1.0 - v + Math.log(v))) {
                    return d * v;
                }
            }
        }
    }

    public static double beta(double alpha, double beta, long seed) {
        double x = DistributionFunction.gamma(alpha, seed);
        double y = DistributionFunction.gamma(beta, seed);
        return x / (x + y);
    }

    public static long hashPosition(long worldSeed, int x, int y, int z, int salt) {
        long hash = worldSeed + salt * 0x517CC1B727220A95L;
        hash = hash * 0x9E3779B97F4A7C15L + x;
        hash = (hash ^ (hash >>> 30)) * 0xBF58476D1CE4E5B9L;
        hash = hash * 0x9E3779B97F4A7C15L + y;
        hash = (hash ^ (hash >>> 27)) * 0x94D049BB133111EBL;
        hash = hash * 0x9E3779B97F4A7C15L + z;
        hash = hash ^ (hash >>> 31);
        return hash;
    }
}
