package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record Profiler(DensityFunction arg, int iterations, int warmup, int trials) implements DensityFunction {
    private static final MapCodec<Profiler> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Profiler::arg),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("iterations").forGetter(Profiler::iterations),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("warm_up").forGetter(Profiler::warmup),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("trials").forGetter(Profiler::trials)
    ).apply(instance, Profiler::new));
    public static final KeyDispatchDataCodec<Profiler> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
    private static final Set<Profiler> inactiveProfilers = ConcurrentHashMap.newKeySet();

    @Override
    public double compute(FunctionContext pos) {
        if (inactiveProfilers.contains(this)) {
            return arg.compute(pos);
        }

        System.out.println("\nBeginning Profile of Density Function: " + this);
        List<Long> warmupTimes = new ArrayList<>();
        List<Long> iterationTimes = new ArrayList<>();
        for (int t = 0; t < trials; t++) {
            if (t > 0) System.gc();
            long startTime = System.nanoTime();
            for (int i = 0; i < warmup; i++) {
                arg.compute(pos);
            }
            long warmupFinish = System.nanoTime();
            warmupTimes.add(warmupFinish - startTime);

            for (int j = 0; j < iterations; j++) {
                arg.compute(pos);
            }
            long iterationFinish = System.nanoTime();
            iterationTimes.add(iterationFinish - warmupFinish);
        }

        double avgWarmup = warmupTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double avgIteration = iterationTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);

        System.out.printf("Profile of %d Trials Complete: \nAvg. Warm-up Time: %.3fms\nAvg. Profile Time: %.3fms\nSum: %.3f\n",
                trials,
                avgWarmup / 1_000_000.0D,
                avgIteration / 1_000_000.0D
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
        return visitor.apply(new Profiler(this.arg, this.iterations, this.warmup, this.trials));
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
