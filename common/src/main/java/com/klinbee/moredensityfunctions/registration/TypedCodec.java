package com.klinbee.moredensityfunctions.registration;

import net.minecraft.util.KeyDispatchDataCodec;

public record TypedCodec<T>(String type, KeyDispatchDataCodec<T> codec) {
}
