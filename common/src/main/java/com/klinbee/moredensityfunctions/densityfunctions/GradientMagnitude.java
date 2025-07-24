package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

public record GradientMagnitude(DensityFunction arg,
                                Optional<Integer> stepHolderX,
                                Optional<Integer> stepHolderY,
                                Optional<Integer> stepHolderZ)
        implements DensityFunction {

    public static final MapCodec<GradientMagnitude> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(GradientMagnitude::arg),
                            MoreDensityFunctionsConstants.POSITIVE_INT.optionalFieldOf("step_x").forGetter(GradientMagnitude::stepHolderX),
                            MoreDensityFunctionsConstants.POSITIVE_INT.optionalFieldOf("step_y").forGetter(GradientMagnitude::stepHolderY),
                            MoreDensityFunctionsConstants.POSITIVE_INT.optionalFieldOf("step_z").forGetter(GradientMagnitude::stepHolderZ)
                    ).apply(instance, GradientMagnitude::create)
            );

    public static final KeyDispatchDataCodec<GradientMagnitude> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    private static GradientMagnitude create(DensityFunction arg,
                                            Optional<Integer> stepHolderX,
                                            Optional<Integer> stepHolderY,
                                            Optional<Integer> stepHolderZ) {
        if (stepHolderX.isEmpty() && stepHolderY.isEmpty() && stepHolderZ.isEmpty()) {
            throw new IllegalArgumentException("Gradient Magnitude must contain at least one valid step component!");
        }
        return new GradientMagnitude(arg, stepHolderX, stepHolderY, stepHolderZ);
    }

    private record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double gradX = 0.0D, gradY = 0.0D, gradZ = 0.0D;

        if (stepHolderX.isPresent()) {
            int stepX = stepHolderX.get();
            gradX = (arg.compute(new BlockContext(x + stepX, y, z)) -
                    arg.compute(new BlockContext(x - stepX, y, z))) / (2.0D * stepX);
        }

        if (stepHolderY.isPresent()) {
            int stepY = stepHolderY.get();
            gradY = (arg.compute(new BlockContext(x, y + stepY, z)) -
                    arg.compute(new BlockContext(x, y - stepY, z))) / (2.0D * stepY);
        }

        if (stepHolderZ.isPresent()) {
            int stepZ = stepHolderZ.get();
            gradZ = (arg.compute(new BlockContext(x, y, z + stepZ)) -
                    arg.compute(new BlockContext(x, y, z - stepZ))) / (2.0D * stepZ);
        }

        // Single step case short-circuits
        if (stepHolderY.isEmpty() && stepHolderZ.isEmpty()) {
            return StrictMath.abs(gradX);
        }
        if (stepHolderX.isEmpty() && stepHolderZ.isEmpty()) {
            return StrictMath.abs(gradY);
        }
        if (stepHolderX.isEmpty() && stepHolderY.isEmpty()) {
            return StrictMath.abs(gradZ);
        }

        return StrictMath.sqrt(gradX * gradX + gradY * gradY + gradZ * gradZ);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new GradientMagnitude(
                        arg,
                        stepHolderX,
                        stepHolderY,
                        stepHolderZ
                )
        );
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
