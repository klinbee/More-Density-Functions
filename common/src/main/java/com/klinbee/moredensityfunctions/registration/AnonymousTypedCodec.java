package com.klinbee.moredensityfunctions.registration;

import com.mojang.serialization.Codec;

public record AnonymousTypedCodec<T>(String type, Codec<T> codec) {
}