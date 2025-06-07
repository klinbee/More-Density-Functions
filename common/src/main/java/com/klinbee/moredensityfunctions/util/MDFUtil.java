package com.klinbee.moredensityfunctions.util;

public class MDFUtil {

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
