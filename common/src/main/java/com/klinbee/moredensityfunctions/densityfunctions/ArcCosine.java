package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

/*
TODO: IMPROPER LIMITS/MIN/MAX
 */
public record ArcCosine(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<ArcCosine> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(ArcCosine::arg)).apply(instance, (ArcCosine::new)));
    public static final KeyDispatchDataCodec<ArcCosine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.acos(density);
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
        return visitor.apply(new ArcCosine(this.arg));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return StrictMath.PI;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
