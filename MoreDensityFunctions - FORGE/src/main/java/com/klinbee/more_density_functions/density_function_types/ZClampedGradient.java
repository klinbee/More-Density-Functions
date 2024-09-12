package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record ZClampedGradient(int fromZ, int toZ, double fromVal, double toVal) implements DensityFunction.SimpleFunction {

    private static final MapCodec<ZClampedGradient> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.intRange(-30_000_000, 30_000_000).fieldOf("from_z").forGetter(ZClampedGradient::fromZ), Codec.intRange(-30_000_000, 30_000_000).fieldOf("to_z").forGetter(ZClampedGradient::toZ), Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("from_value").forGetter(ZClampedGradient::fromVal), Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("to_value").forGetter(ZClampedGradient::toVal)).apply(instance, ZClampedGradient::new));
    public static final KeyDispatchDataCodec<ZClampedGradient> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(FunctionContext pContext) {
        return Mth.clampedMap(pContext.blockX(), this.fromZ, this.toZ, this.fromVal, this.toVal);
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new ZClampedGradient(this.fromZ, this.toZ, this.fromVal, this.toVal));
    }

    public int fromZ() {
        return this.fromZ;
    }

    public int toZ() {
        return this.toZ;
    }

    public double toVal() {
        return this.toVal;
    }

    public double fromVal() {
        return this.fromVal;
    }

    @Override
    public double minValue() {
        return Math.min(this.fromVal,this.toVal);
    }

    @Override
    public double maxValue() {
        return Math.max(this.fromVal,this.toVal);
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction.SimpleFunction> codec() {
        return CODEC;
    }
}
