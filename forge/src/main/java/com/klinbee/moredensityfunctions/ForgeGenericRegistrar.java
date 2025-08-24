package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.registration.RegistryKey;
import com.mojang.serialization.Codec;
import net.minecraftforge.registries.DeferredRegister;

import java.util.EnumMap;

public class ForgeGenericRegistrar {
    private static final EnumMap<RegistryKey, Object> registries = new EnumMap<>(RegistryKey.class);

    private ForgeGenericRegistrar() {
    }

    public static <T> void addRegistry(RegistryKey registryKey, DeferredRegister<Codec<? extends T>> deferredRegister) {
        registries.put(registryKey, deferredRegister);
    }

    @SuppressWarnings("unchecked")
    public static <T> void register(RegistryKey registryKey, String name, Codec<? extends T> codec) {
        DeferredRegister<Codec<? extends T>> deferredRegister =
                (DeferredRegister<Codec<? extends T>>) registries.get(registryKey);

        if (deferredRegister != null) {
            deferredRegister.register(name, () -> codec);
        } else {
            throw new IllegalArgumentException("No registry mapped for ID: " + registryKey);
        }
    }
}