package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;


public record Cosine(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<Cosine> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Cosine::arg)).apply(instance, (Cosine::new)));
    public static final KeyDispatchDataCodec<Cosine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.cos(density);
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
        return visitor.apply(new Cosine(this.arg));
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
