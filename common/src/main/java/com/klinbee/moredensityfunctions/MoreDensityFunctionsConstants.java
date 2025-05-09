package com.klinbee.moredensityfunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreDensityFunctionsConstants {

	public static final String MOD_ID = "moredensityfunctions";
	public static final String MOD_NAME = "MoreDensityFunctions";
	public static final String MOD_NAMESPACE = "moredfs";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	// Density Function Defaults
	public static final double DEFAULT_ERROR = 0.0D;
	public static final double DEFAULT_MAX_OUTPUT = 1.0D;
	public static final double DEFAULT_MIN_OUTPUT = -1.0D;
	public static final double MIN_POS_DOUBLE = -30_000_000D;
	public static final double MAX_POS_DOUBLE = 30_000_000D;
	public static final int MIN_POS_INT = -30_000_000;
	public static final int MAX_POS_INT = 30_000_000;
}