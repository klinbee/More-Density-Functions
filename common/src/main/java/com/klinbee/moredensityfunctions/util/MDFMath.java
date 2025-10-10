package com.klinbee.moredensityfunctions.util;

import net.minecraft.util.Mth;

/// Stores my custom math functions used. Also, an internal API wrapper for consistent function usage in this code base
public final class MDFMath {

    /// My math functions
    // To compute grid cell coordinates from regular coordinates
    public static int safeFloorDiv(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return StrictMath.floorDiv(numerator, denominator);
    }

    public static int coordinateToIndex(int x1, int x1Length, int x2, int x2Length) {
        return MDFMath.floorModInt(x1, x1Length) + MDFMath.floorModInt(x2, x2Length) * x1;
    }

    public static int floorModInt(int numerator, int denominator) {
        return StrictMath.floorMod(numerator, denominator);
    }

    public static int floorDivInt(int numerator, int denominator) {
        return StrictMath.floorDiv(numerator, denominator);
    }

    public static double centralDifference(double forwards, double backwards, int step) {
        return (forwards - backwards) / (2.0D * step);
    }

    public static int maxInt(int i1, int i2) {
        return StrictMath.max(i1, i2);
    }

    public static int minInt(int i1, int i2) {
        return StrictMath.min(i1, i2);
    }

    public static int absInt(int i) {
        return StrictMath.abs(i);
    }

    public static double floorMod(double numerator, double denominator) {
        return MDFMath.floor(MDFMath.modulo(numerator, denominator));
    }

    public static double floorDiv(double numerator, double denominator) {
        return MDFMath.floor(numerator / denominator);
    }

    public static double log(double x, double base) {
        return MDFMath.ln(x / MDFMath.ln(base));
    }

    public static double log2(double x) {
        double inverse_ln_2 = 1.4426950408889634D;
        return MDFMath.ln(x) * inverse_ln_2;
    }

    public static double log2Floor(double x) {
        // Bit-Trick for Fast Log2Floor
        // Note: this produces 1024 for ±∞ or NaN, and -1023 for ±0 and sub-normals
        long bits = Double.doubleToLongBits(x);
        return (int) ((bits >>> 52) & 0x7FF) - 1023; // Exponent
    }

    public static double modulo(double numerator, double denominator) {
        return (numerator % denominator + denominator) % denominator;
    }

    public static double safeModulo(double numerator, double denominator) {
        if (denominator == 0.0D) {
            return 0.0D;
        }
        return (numerator % denominator + denominator) % denominator;
    }

    public static double euclideanDist2D(double x, double y) {
        return MDFMath.sqrt(x * x + y * y);
    }

    public static double euclideanDist3D(double x, double y, double z) {
        return MDFMath.sqrt(x * x + y * y + z * z);
    }

    public static int chebyshevDist2DInt(int i1, int i2) {
        return MDFMath.maxInt(MDFMath.absInt(i1), MDFMath.absInt(i2));
    }

    public static double inverse(double x) {
        return 1.0D / x;
    }

    public static double remainder(double numerator, double denominator) {
        return numerator % denominator;
    }

    public static double sigmoid(double x) {
        return 1.0D / (1.0D + MDFMath.eRaisedTo(-x));
    }

    /// API Wrappers
    public static double min(double x, double y) {
        return StrictMath.min(x, y);
    }

    public static double max(double x, double y) {
        return StrictMath.max(x, y);
    }

    public static double abs(double x) {
        return StrictMath.abs(x);
    }

    public static double sqrt(double x) {
        return StrictMath.sqrt(x);
    }

    public static double cbrt(double x) {
        return StrictMath.cbrt(x);
    }

    public static double cos(double x) {
        return StrictMath.cos(x);
    }

    public static double sin(double x) {
        return StrictMath.sin(x);
    }

    public static double tan(double x) {
        return StrictMath.tan(x);
    }

    public static double acos(double x) {
        return StrictMath.acos(x);
    }

    public static double asin(double x) {
        return StrictMath.asin(x);
    }

    public static double atan(double x) {
        return StrictMath.atan(x);
    }

    public static double hcos(double x) {
        return StrictMath.cosh(x);
    }

    public static double hsin(double x) {
        return StrictMath.sinh(x);
    }

    public static double htan(double x) {
        return StrictMath.tanh(x);
    }

    public static double atan2(double x, double y) {
        return StrictMath.atan2(x, y);
    }

    // This should be device-consistent, and is faster than StrictMath's
    public static double ceil(double x) {
        return Mth.ceil(x);
    }

    // This should be device-consistent, and is faster than StrictMath's
    public static double floor(double x) {
        return Mth.floor(x);
    }

    public static double round(double x) {
        return StrictMath.round(x);
    }

    // This should be device-consistent; no implementations in Math/StrictMath exist
    public static double clamp(double x, double min, double max) {
        return Mth.clamp(x, min, max);
    }

    public static double ln(double x) {
        return StrictMath.log(x);
    }

    public static double pow(double base, double exponent) {
        return StrictMath.pow(base, exponent);
    }

    public static double eRaisedTo(double exp) {
        return StrictMath.exp(exp);
    }

    public static double signum(double x) {
        return StrictMath.signum(x);
    }
}
