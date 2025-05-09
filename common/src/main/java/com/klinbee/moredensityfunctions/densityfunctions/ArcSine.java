package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

/*
TODO: IMPROPER LIMITS/MIN/MAX
 */
public record ArcSine(DensityFunction arg) implements DensityFunction {
    private static final MapCodec<ArcSine> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(ArcSine::arg)).apply(instance, (ArcSine::new)));
    public static final KeyDispatchDataCodec<ArcSine> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public double eval(double density) {
        return StrictMath.acos(density);
    }

    @Override
    public double compute( FunctionContext pos) {
        return this.eval(arg.compute(pos));
    }

    @Override
    public void fillArray(double  [] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities,this);
    }

    @Override
    public  DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ArcSine(this.arg));
    }

    public DensityFunction arg() {
        return arg;
    }

    @Override
    public double minValue() {
        return -StrictMath.PI/2.0D;
    }

    @Override
    public double maxValue() {
        return StrictMath.PI/2.0D;
    }

    @Override
    public  KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
