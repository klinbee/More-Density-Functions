package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record XClampedGradient(int fromX, int toX, double fromVal, double toVal) implements DensityFunction.Base {

    private static final MapCodec<XClampedGradient> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.intRange(-30_000_000, 30_000_000).fieldOf("from_x").forGetter(XClampedGradient::fromX), Codec.intRange(-30_000_000, 30_000_000).fieldOf("to_x").forGetter(XClampedGradient::toX), DensityFunctionTypes.CONSTANT_RANGE.fieldOf("from_value").forGetter(XClampedGradient::fromVal), DensityFunctionTypes.CONSTANT_RANGE.fieldOf("to_value").forGetter(XClampedGradient::toVal)).apply(instance, (XClampedGradient::new)));
    public static final CodecHolder<XClampedGradient> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);


    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return MathHelper.clampedMap(pos.blockX(), this.fromX, this.toX, this.fromVal, this.toVal);
    }

    @Override
    public double minValue() {
        return Math.min(this.fromVal,this.toVal);
    }

    @Override
    public double maxValue() {
        return Math.max(this.fromVal,this.toVal);
    }

    public int fromX() {
        return this.fromX;
    }

    public int toX() {
        return toX;
    }

    public double toVal() {
        return toVal;
    }

    public double fromVal() {
        return fromVal;
    }

    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
