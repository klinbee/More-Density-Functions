package com.klinbee.moredensityfunctions.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/// Used by `ValueNoise` for interpolating between values
public enum Interpolation implements StringRepresentable {
    NONE("none"),
    LERP("lerp"),
    SMOOTHSTEP("smoothstep");

    public static final Codec<Interpolation> CODEC = StringRepresentable.fromEnum(Interpolation::values);
    private final String name;

    Interpolation(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}