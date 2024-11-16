package com.klinbee.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record SimplexNoise(double xzScale, double yScale) implements DensityFunction {

    private static final MapCodec<SimplexNoise> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.DOUBLE.fieldOf("xz_scale").forGetter(SimplexNoise::xzScale),Codec.DOUBLE.fieldOf("y_scale").forGetter(SimplexNoise::yScale)).apply(instance, (SimplexNoise::new)));
    public static final CodecHolder<SimplexNoise> CODEC = CodecHolder.of(MAP_CODEC);

    private static final Random random = new CheckedRandom(0L);
    private static final SimplexNoiseSampler sampler = new SimplexNoiseSampler(random);

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }

    public DensityFunction apply(DensityFunction.DensityFunctionVisitor visitor) {
        return visitor.apply(new SimplexNoise(this.xzScale, this.yScale));
    }

    public double sample(DensityFunction.NoisePos pos) {
        return sampler.sample(pos.blockX() * this.xzScale,pos.blockY() * this.yScale, pos.blockZ() * this.xzScale);
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
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }

}
