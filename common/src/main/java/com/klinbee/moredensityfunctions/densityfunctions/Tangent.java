package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/*
TODO: IMPROPER LIMITS/MIN/MAX
 */
public record Tangent(DensityFunction arg, Optional<DensityFunction> argError) implements DensityFunction {
    private static final MapCodec<Tangent> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Tangent::arg), DensityFunction.HOLDER_HELPER_CODEC.optionalFieldOf("error_argument").forGetter(Tangent::argError)).apply(instance, (Tangent::new)));
    public static final KeyDispatchDataCodec<Tangent> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.tan(density);
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
        return visitor.apply(new Tangent(this.arg, this.argError));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
