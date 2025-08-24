package com.klinbee.moredensityfunctions.registration;

import com.mojang.serialization.Codec;

@FunctionalInterface
public interface RegistrationFunction {
    <T> void register(RegistryKey registryKey, String name, Codec<? extends T> codec);
}
    