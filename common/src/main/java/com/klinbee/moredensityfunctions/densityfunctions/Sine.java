package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record Sine(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<Sine> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Sine::arg)).apply(instance, (Sine::new)));
    public static final KeyDispatchDataCodec<Sine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.sin(density);
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
        return visitor.apply(new Sine(this.arg));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public double minValue() {
        return -1;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
