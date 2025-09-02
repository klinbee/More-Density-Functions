package com.klinbee.moredensityfunctions;

import com.klinbee.moredensityfunctions.registration.RegistryKey;
import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;

public class FabricGenericRegistrar {

    private static final EnumMap<RegistryKey, Object> registries = new EnumMap<>(RegistryKey.class);

    private FabricGenericRegistrar() {
    }

    public static <T> void addRegistry(RegistryKey registryKey, Registry<MapCodec<? extends T>> registry) {
        registries.put(registryKey, registry);
    }

    @SuppressWarnings("unchecked")
    public static <T> void register(RegistryKey registryKey, TypedCodec<T> typedCodec) {
        Registry<MapCodec<? extends T>> registry = (Registry<MapCodec<? extends T>>) registries.get(registryKey);

        if (registry != null) {
            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(MoreDensityFunctionsConstants.MOD_NAMESPACE, typedCodec.type());
            ResourceKey<MapCodec<? extends T>> resourceKey = ResourceKey.create(registry.key(), resourceLocation);
            Registry.register(registry, resourceKey, typedCodec.codec().codec());
        } else {
            throw new IllegalArgumentException("No registry mapped for ID: " + registryKey);
        }
    }

}