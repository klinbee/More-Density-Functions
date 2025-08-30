package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record GradientMagnitude(DensityFunction arg,
                                int stepX,
                                int stepY,
                                int stepZ)
        implements DensityFunction {

    public static final MapCodec<GradientMagnitude> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(GradientMagnitude::arg),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_x").orElse(0).forGetter(GradientMagnitude::stepX),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_y").orElse(0).forGetter(GradientMagnitude::stepY),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_z").orElse(0).forGetter(GradientMagnitude::stepZ)
                    ).apply(instance, GradientMagnitude::create)
            );

    public static final TypedCodec<GradientMagnitude> TYPED_CODEC = new TypedCodec<>("gradient_magnitude", KeyDispatchDataCodec.of(MAP_CODEC));

    private static GradientMagnitude create(DensityFunction arg,
                                            int stepX,
                                            int stepY,
                                            int stepZ) {
        if ((stepX | stepY | stepZ) == 0) {
            throw new IllegalArgumentException("Gradient Magnitude must contain at least one non-zero step component!");
        }
        return new GradientMagnitude(arg, stepX, stepY, stepZ);
    }

    private record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double gradX = 0.0D, gradY = 0.0D, gradZ = 0.0D;

        if (stepX != 0) {
            gradX = (arg.compute(new BlockContext(x + stepX, y, z)) -
                    arg.compute(new BlockContext(x - stepX, y, z))) / (2.0D * stepX);
        }

        if (stepY != 0) {
            gradY = (arg.compute(new BlockContext(x, y + stepY, z)) -
                    arg.compute(new BlockContext(x, y - stepY, z))) / (2.0D * stepY);
        }

        if (stepZ != 0) {
            gradZ = (arg.compute(new BlockContext(x, y, z + stepZ)) -
                    arg.compute(new BlockContext(x, y, z - stepZ))) / (2.0D * stepZ);
        }

        // Single step case short-circuits
        if (stepY == 0 && stepZ == 0) {
            return StrictMath.abs(gradX);
        }
        if (stepX == 0 && stepZ == 0) {
            return StrictMath.abs(gradY);
        }
        if (stepX == 0 && stepY == 0) {
            return StrictMath.abs(gradZ);
        }

        return StrictMath.sqrt(gradX * gradX + gradY * gradY + gradZ * gradZ);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new GradientMagnitude(
                        arg.mapAll(visitor),
                        stepX,
                        stepY,
                        stepZ
                )
        );
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public double minValue() {

        double minGradX = 0.0D, minGradY = 0.0D, minGradZ = 0.0D;

        if (stepX != 0) {
            minGradX = (arg.minValue() -
                    arg.maxValue()) / (2.0D * stepX);
        }

        if (stepY != 0) {
            minGradY = (arg.minValue() -
                    arg.maxValue()) / (2.0D * stepY);
        }

        if (stepZ != 0) {
            minGradZ = (arg.minValue() -
                    arg.maxValue()) / (2.0D * stepZ);
        }

        return minGradX * minGradX + minGradY * minGradY + minGradZ * minGradZ;
    }

    @Override
    public double maxValue() {

        double maxGradX = 0.0D, maxGradY = 0.0D, maxGradZ = 0.0D;

        if (stepX != 0) {
            maxGradX = (arg.maxValue() -
                    arg.minValue()) / (2.0D * stepX);
        }

        if (stepY != 0) {
            maxGradY = (arg.maxValue() -
                    arg.minValue()) / (2.0D * stepY);
        }

        if (stepZ != 0) {
            maxGradZ = (arg.maxValue() -
                    arg.minValue()) / (2.0D * stepZ);
        }

        return maxGradX * maxGradX + maxGradY * maxGradY + maxGradZ * maxGradZ;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
