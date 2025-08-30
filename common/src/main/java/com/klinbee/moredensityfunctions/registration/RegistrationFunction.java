package com.klinbee.moredensityfunctions.registration;

@FunctionalInterface
public interface RegistrationFunction {
    <T> void register(RegistryKey registryKey, TypedCodec<T> typedCodec);
}
    