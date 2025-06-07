package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.util.MDFUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;


public record Ceil(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<Ceil> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Ceil::arg)
    ).apply(instance, (Ceil::new)));
    public static final KeyDispatchDataCodec<Ceil> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return Mth.ceil(density);
    }

    @Override
    public double compute(DensityFunction.FunctionContext pos) {
        return this.eval(arg.compute(pos));
    }

    @Override
    public void fillArray(double[] densities, DensityFunction.ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
        return visitor.apply(new Ceil(this.arg));
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
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
