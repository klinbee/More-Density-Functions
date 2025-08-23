package com.klinbee.moredensityfunctions;

import com.mojang.serialization.Codec;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ForgeGenericRegistrar {
    private static final Map<String, DeferredRegister<?>> registryMap = new HashMap<>();

    private ForgeGenericRegistrar() {}

    public static <T> void addRegistry(String registryId, DeferredRegister<Codec<? extends T>> deferredRegister) {
        registryMap.put(registryId, deferredRegister);
    }

    @SuppressWarnings("unchecked")
    public static <T> void register(String registryId, String name, Codec<? extends T> codec) {
        DeferredRegister<Codec<? extends T>> deferredRegister =
                (DeferredRegister<Codec<? extends T>>) registryMap.get(registryId);

        if (deferredRegister != null) {
            deferredRegister.register(name, () -> codec);
        } else {
            throw new IllegalArgumentException("No registry mapped for ID: " + registryId);
        }
    }
}