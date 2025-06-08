package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Clamp(DensityFunction arg, double min, double max) implements DensityFunction {
    private static final MapCodec<Clamp> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Clamp::arg),
            Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("min").forGetter(Clamp::min),
            Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).fieldOf("max").forGetter(Clamp::max)
    ).apply(instance, (arg, min, max) -> {
        if (min > max) {
            throw new IllegalArgumentException("Min must be less than max!");
        }
        return new Clamp(arg, min, max);
    }));
    public static final KeyDispatchDataCodec<Clamp> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return Mth.clamp(density, min, max);
    }

    @Override
    public double compute(FunctionContext pos) {
        return this.eval(arg.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Clamp(this.arg, this.min, this.max));
    }

    @Override
    public double minValue() {
        return StrictMath.min(arg.minValue(), min);
    }

    @Override
    public double maxValue() {
        return StrictMath.max(arg.maxValue(), max);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
