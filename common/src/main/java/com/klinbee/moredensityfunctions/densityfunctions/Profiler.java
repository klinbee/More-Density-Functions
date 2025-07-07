package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record Profiler(DensityFunction arg, int iterations, int warmup) implements DensityFunction {
    private static final MapCodec<Profiler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Profiler::arg),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("iterations").forGetter(Profiler::iterations),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("warm_up").forGetter(Profiler::warmup)
    ).apply(instance, Profiler::new));
    public static final KeyDispatchDataCodec<Profiler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
    private static final Set<Profiler> inactiveProfilers = ConcurrentHashMap.newKeySet();

    @Override
    public double compute(FunctionContext pos) {
        if (inactiveProfilers.contains(this)) {
            return arg.compute(pos);
        }

        System.out.println("\nBeginning Profile of Density Function: " + this);
        long[] warmUpTimes = new long[warmup];
        long[] iterationTimes = new long[iterations];
        for (int i = 0; i < warmup; i++) {
            long startWarmUp = System.nanoTime();
            arg.compute(pos);
            long warmupFinish = System.nanoTime();
            warmUpTimes[i] = warmupFinish - startWarmUp;
        }
        System.gc();
        for (int i = 0; i < iterations; i++) {
            long startIteration = System.nanoTime();
            arg.compute(pos);
            long iterationFinish = System.nanoTime();
            iterationTimes[i] = iterationFinish - startIteration;
        }

        double avgWarmUpTime = Arrays.stream(warmUpTimes).average().orElse(0.0);
        double avgIterationTime = Arrays.stream(iterationTimes).average().orElse(0.0);

        System.out.printf("Profile of %,d iterations:" +
                        "\nAvg. Time per Compute (warm-up stage): %.3fns" +
                        "\nAvg. Time per Compute (profile stage): %.3fns" +
                        "\nProfile Time: %.3fs" +
                        "\n",
                iterations,
                avgWarmUpTime,
                avgIterationTime,
                (avgWarmUpTime * warmup + avgIterationTime * iterations) / (1_000_000_000.0D)
        );

        inactiveProfilers.add(this);
        return arg.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new Profiler(this.arg, this.iterations, this.warmup));
    }

    @Override
    public double minValue() {
        return arg.minValue();
    }

    @Override
    public double maxValue() {
        return arg.maxValue();
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
