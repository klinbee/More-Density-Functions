package com.klinbee.moredensityfunctions.util;

import net.minecraft.world.level.levelgen.DensityFunction;

/// Used for mutating `FunctionContext` instances, like in `Derivative` or `Shift`
public record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
}