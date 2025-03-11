package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.jetbrains.annotations.NotNull;

public record SimplexNoiseFunction(double xzScale, double yScale) implements DensityFunction {

    private static final MapCodec<SimplexNoiseFunction> DATA_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.DOUBLE.fieldOf("xz_scale").forGetter(SimplexNoiseFunction::xzScale), Codec.DOUBLE.fieldOf("y_scale").forGetter(SimplexNoiseFunction::yScale)).apply(instance, SimplexNoiseFunction::new));
    public static final KeyDispatchDataCodec<SimplexNoiseFunction> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);

    private static final RandomSource random = new LegacyRandomSource(0L);
    private static final SimplexNoise noise = new SimplexNoise(random);

    @Override
    public double compute(FunctionContext pContext) {
        return noise.getValue(pContext.blockX() * this.xzScale,pContext.blockY() * this.yScale, pContext.blockZ() * this.xzScale);
    }

    public void fillArray(double @NotNull [] pArray, DensityFunction.ContextProvider pContextProvider) {
        pContextProvider.fillAllDirectly(pArray, this);
    }

    public @NotNull DensityFunction mapAll(DensityFunction.Visitor pVisitor) {
        return pVisitor.apply(new SimplexNoiseFunction(this.xzScale, this.yScale));
    }

    public double xzScale() {
        return this.xzScale;
    }

    public double yScale() {
        return this.yScale;
    }


    @Override
    public double minValue() {
        return -1;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    public @NotNull KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}

