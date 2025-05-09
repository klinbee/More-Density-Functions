package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record Sigmoid(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<Sigmoid> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Sigmoid::arg)).apply(instance, (Sigmoid::new)));
    public static final KeyDispatchDataCodec<Sigmoid> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return 1.0D / (1.0D + StrictMath.exp(-density));
    }

    @Override
    public double compute(@NotNull FunctionContext pos) {
        return this.eval(arg.compute(pos));
    }

    @Override
    public void fillArray(double @NotNull [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities,this);
    }

    @Override
    public @NotNull DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Sigmoid(this.arg));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public double minValue() {
        return this.eval(arg.minValue());
    }

    @Override
    public double maxValue() {
        return this.eval(arg.maxValue());
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
