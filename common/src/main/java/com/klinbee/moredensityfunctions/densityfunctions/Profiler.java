package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public record Profiler(DensityFunction arg,
                       int iterations,
                       int warmUp)
        implements DensityFunction {

    private static final MapCodec<Profiler> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Profiler::arg),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("iterations").forGetter(Profiler::iterations),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("warm_up").forGetter(Profiler::warmUp)
            ).apply(instance, Profiler::new)
    );

    public static final TypedCodec<Profiler> TYPED_CODEC = new TypedCodec<>("profiler", KeyDispatchDataCodec.of(MAP_CODEC));

    private static final Set<DensityFunction> inactiveProfilers = Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));

    @Override
    public double compute(FunctionContext pos) {
        if (inactiveProfilers.contains(arg)) {
            return arg.compute(pos);
        }

        System.out.println("\nBeginning Profile of Density Function: " + arg);

        long[] warmUpTimes = new long[warmUp];
        long[] iterationTimes = new long[iterations];

        for (int i = 0; i < warmUp; i++) {
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

        double avgWarmUpTime = Arrays.stream(warmUpTimes).average().orElse(0.0D);
        double avgIterationTime = Arrays.stream(iterationTimes).average().orElse(0.0D);

        System.out.printf("Profile of %,d iterations:" +
                        "\nAvg. Time per Compute (warm-up stage): %,.3fns" +
                        "\nAvg. Time per Compute (profile stage): %,.3fns" +
                        "\nProfile Time: %,.3fs" +
                        "\n",
                iterations,
                avgWarmUpTime,
                avgIterationTime,
                (avgWarmUpTime * warmUp + avgIterationTime * iterations) / (1_000_000_000.0D));

        inactiveProfilers.add(arg);
        return arg.compute(pos);
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        if (!inactiveProfilers.contains(arg)) {
            inactiveProfilers.add(arg);
            return new Profiler(
                    arg.mapAll(visitor),
                    iterations,
                    warmUp
            );
        }

        return visitor.apply(
                arg.mapAll(visitor)
        );
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
        return TYPED_CODEC.codec();
    }
}
