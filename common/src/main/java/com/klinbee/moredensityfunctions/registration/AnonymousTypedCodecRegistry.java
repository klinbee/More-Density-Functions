package com.klinbee.moredensityfunctions.registration;

import com.mojang.serialization.Codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class AnonymousTypedCodecRegistry<T> {
    private final Map<String, Codec<? extends T>> registry = new ConcurrentHashMap<>();
    private final String registryType;

    public AnonymousTypedCodecRegistry(String registryType) {
        this.registryType = registryType;
    }

    public <U extends T> void register(AnonymousTypedCodec<U> anonCodec) {
        String type = anonCodec.type();
        if (registry.containsKey(type)) {
            throw new IllegalArgumentException("AnonymousRegistry " + registryType +
                    " type '" + type + "' is already registered!");
        }
        registry.put(type, anonCodec.codec());
    }

    public Codec<? extends T> getCodec(String type) {
        Codec<? extends T> codec = registry.get(type);
        if (codec == null) {
            throw new IllegalArgumentException("Unknown " + registryType + " type: " + type);
        }
        return codec;
    }

    public Codec<T> createDispatchCodec(Function<T, String> typeGetter) {
        return Codec.STRING.dispatch(
                "type",
                typeGetter,
                this::getCodec
        );
    }
}