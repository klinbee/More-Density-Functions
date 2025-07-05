package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;


public record ZClampedGradient(int fromZ, int toZ, double fromValue, double toValue) implements DensityFunction {
    private static final MapCodec<ZClampedGradient> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("from_z").forGetter(ZClampedGradient::fromZ),
            MoreDensityFunctionsConstants.COORD_CODEC_INT.fieldOf("to_z").forGetter(ZClampedGradient::toZ),
            Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("from_value").forGetter(ZClampedGradient::fromValue),
            Codec.doubleRange(-1000000.0D, 1000000.0D).fieldOf("to_value").forGetter(ZClampedGradient::toValue)
    ).apply(instance, (ZClampedGradient::new)));
    public static final KeyDispatchDataCodec<ZClampedGradient> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public double compute(FunctionContext pos) {
        return Mth.clampedMap(pos.blockZ(), this.fromZ, this.toZ, this.fromValue, this.toValue);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new ZClampedGradient(this.fromZ, this.toZ, this.fromValue, this.toValue));
    }

    @Override
    public double minValue() {
        return StrictMath.min(fromValue, toValue);
    }

    @Override
    public double maxValue() {
        return StrictMath.max(fromValue, toValue);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
