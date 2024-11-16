package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public record ZClampedGradient(int fromZ, int toZ, double fromVal, double toVal) implements DensityFunction.Base {

    private static final MapCodec<ZClampedGradient> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.intRange(-30_000_000, 30_000_000).fieldOf("from_z").forGetter(ZClampedGradient::fromZ), Codec.intRange(-30_000_000, 30_000_000).fieldOf("to_z").forGetter(ZClampedGradient::toZ), DensityFunctionTypes.CONSTANT_RANGE.fieldOf("from_value").forGetter(ZClampedGradient::fromVal), DensityFunctionTypes.CONSTANT_RANGE.fieldOf("to_value").forGetter(ZClampedGradient::toVal)).apply(instance, (ZClampedGradient::new)));
    public static final CodecHolder<ZClampedGradient> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    @Override
    public double sample(NoisePos pos) {
        return MathHelper.clampedMap(pos.blockZ(), this.fromZ, this.toZ, this.fromVal, this.toVal);
    }

    @Override
    public double minValue() {
        return Math.min(this.fromVal,this.toVal);
    }

    @Override
    public double maxValue() {
        return Math.max(this.fromVal,this.toVal);
    }

    public int fromZ() {
        return this.fromZ;
    }

    public int toZ() {
        return toZ;
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
