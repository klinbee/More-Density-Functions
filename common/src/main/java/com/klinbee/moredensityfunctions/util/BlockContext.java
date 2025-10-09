package com.klinbee.moredensityfunctions.util;

import net.minecraft.world.level.levelgen.DensityFunction;

public record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
}