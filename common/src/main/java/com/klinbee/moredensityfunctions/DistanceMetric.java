package com.klinbee.moredensityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface DistanceMetric {

    Codec<DistanceMetric> CODEC = Codec.STRING.dispatch(
            "type",
            DistanceMetric::type,
            type -> switch (type) {
                case "linear" -> Linear.CODEC;
                case "euclidean" -> Euclidean.CODEC;
                case "manhattan" -> Manhattan.CODEC;
                case "chebyshev" -> Chebyshev.CODEC;
                case "minkowski" -> Minkowski.CODEC;
                default -> throw new IllegalArgumentException("Unknown distance metric type: " + type);
            }
    );

    double distance(double[] point1, double[] point2);

    String type();

    record Linear() implements DistanceMetric {

        public static final Codec<Linear> CODEC = Codec.unit(new Linear());

        @Override
        public String type() {
            return "linear";
        }

        @Override
        public double distance(double[] point1, double[] point2) {
            return StrictMath.abs(point2[0] - point1[0]);
        }
    }

    record Euclidean() implements DistanceMetric {

        public static final Codec<Euclidean> CODEC = Codec.unit(new Euclidean());

        @Override
        public String type() {
            return "euclidean";
        }

        @Override
        public double distance(double[] point1, double[] point2) {
            double sum = 0.0D;

            for (int i = 0; i < point1.length; i++) {
                sum += (point2[i] - point1[i]) * (point2[i] - point1[i]);
            }
            return StrictMath.sqrt(sum);
        }
    }

    record Manhattan() implements DistanceMetric {

        public static final Codec<Manhattan> CODEC = Codec.unit(new Manhattan());

        @Override
        public String type() {
            return "manhattan";
        }

        @Override
        public double distance(double[] point1, double[] point2) {
            double sum = 0.0D;

            for (int i = 0; i < point1.length; i++) {
                sum += StrictMath.abs(point2[i] - point1[i]);
            }
            return sum;
        }
    }

    record Chebyshev() implements DistanceMetric {

        public static final Codec<Chebyshev> CODEC = Codec.unit(new Chebyshev());

        @Override
        public String type() {
            return "chebyshev";
        }

        @Override
        public double distance(double[] point1, double[] point2) {
            double maxDistance = 0.0D;

            for (int i = 0; i < point1.length; i++) {
                double distance = StrictMath.abs(point2[i] - point1[i]);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
            return maxDistance;
        }
    }

    record Minkowski(int p) implements DistanceMetric {

        public static final Codec<DistanceMetric> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("p").forGetter(m -> ((Minkowski) m).p)
                ).apply(instance, Minkowski::create)
        );

        @Override
        public String type() {
            return "minkowski";
        }

        private static DistanceMetric create(int p) {
            return switch (p) {
                case 0 -> new Manhattan();
                case 1 -> new Euclidean();
                default -> new Minkowski(p);
            };
        }

        @Override
        public double distance(double[] point1, double[] point2) {
            double sum = 0.0D;

            for (int i = 0; i < point1.length; i++) {
                sum += StrictMath.pow(StrictMath.abs(point2[i] - point1[i]), p);
            }
            return StrictMath.pow(sum, 1.0D / p);
        }
    }
}
