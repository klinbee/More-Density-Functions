package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record XClampedGradient(int fromX, int toX, double fromValue, double toValue) implements DensityFunction {
    private static final MapCodec<XClampedGradient> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.intRange(MoreDensityFunctionsConstants.MIN_POS_INT, MoreDensityFunctionsConstants.MAX_POS_INT).fieldOf("from_x").forGetter(XClampedGradient::fromX), Codec.intRange(MoreDensityFunctionsConstants.MIN_POS_INT, MoreDensityFunctionsConstants.MAX_POS_INT).fieldOf("to_x").forGetter(XClampedGradient::toX), Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("from_val").forGetter(XClampedGradient::fromValue), Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("to_val").forGetter(XClampedGradient::toValue)).apply(instance, (XClampedGradient::new)));
    public static final KeyDispatchDataCodec<XClampedGradient> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return Mth.clampedMap(pos.blockX(), this.fromX, this.toX, this.fromValue, this.toValue);
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities,this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new XClampedGradient(this.fromX, this.toX, this.fromValue, this.toValue));
    }

    public int fromX() {
        return fromX;
    }

    public int toX() {
        return toX;
    }

    public double fromValue() {
        return fromValue;
    }

    public double toValue() {
        return toValue;
    }

    @Override
    public double minValue() {
        return StrictMath.min(fromValue,toValue);
    }

    @Override
    public double maxValue() {
        return StrictMath.max(fromValue,toValue);
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
