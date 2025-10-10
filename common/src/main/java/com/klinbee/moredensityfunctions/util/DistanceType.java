package com.klinbee.moredensityfunctions.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/// Used in `WorleyNoise` to represent different Feature Types
public enum DistanceType implements StringRepresentable {
    F1("f1"),
    F2("f2"),
    F2_SUB_F1("f2-f1"),
    F2_ADD_F1("f2+f1"),
    F2_MUL_F1("f2*f1"),
    F2_DIV_F1("f2/f1");

    public static final Codec<DistanceType> CODEC = StringRepresentable.fromEnum(DistanceType::values);
    private final String name;

    DistanceType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}