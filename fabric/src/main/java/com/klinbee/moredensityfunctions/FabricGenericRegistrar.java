package com.klinbee.moredensityfunctions;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class FabricGenericRegistrar {
    private static final Map<String, Registry<?>> registryMap = new HashMap<>();

    private FabricGenericRegistrar() {}

    public static <T> void addRegistry(String registryId, Registry<Codec<? extends T>> registry) {
        registryMap.put(registryId, registry);
    }

    @SuppressWarnings("unchecked")
    public static <T> void register(String registryId, String name, Codec<? extends T> codec) {
        Registry<Codec<? extends T>> registry = (Registry<Codec<? extends T>>) registryMap.get(registryId);

        if (registry != null) {
            ResourceLocation resourceLocation = new ResourceLocation(MoreDensityFunctionsConstants.MOD_NAMESPACE, name);
            ResourceKey<Codec<? extends T>> resourceKey = ResourceKey.create(registry.key(), resourceLocation);
            Registry.register(registry, resourceKey, codec);
        } else {
            throw new IllegalArgumentException("No registry mapped for ID: " + registryId);
        }
    }
}