package com.klinbee.moredensityfunctions.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/// Interpolation codec
public enum Interpolation implements StringRepresentable {
    NONE("none"),
    LERP("lerp"),
    SMOOTHSTEP("smoothstep");

    private final String name;

    public static final Codec<Interpolation> CODEC = StringRepresentable.fromEnum(Interpolation::values);

    Interpolation(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}