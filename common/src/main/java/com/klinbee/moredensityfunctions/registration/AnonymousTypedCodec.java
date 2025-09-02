package com.klinbee.moredensityfunctions.registration;

import com.mojang.serialization.MapCodec;

public record AnonymousTypedCodec<T>(String type, MapCodec<T> codec) {
}