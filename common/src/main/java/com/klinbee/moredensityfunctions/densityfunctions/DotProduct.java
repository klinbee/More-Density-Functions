package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record DotProduct(DensityFunction arg1,
                         DensityFunction arg2,
                         int stepX,
                         int stepY,
                         int stepZ)
        implements DensityFunction {

    public static final MapCodec<DotProduct> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(DotProduct::arg1),
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(DotProduct::arg2),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_x").orElse(0).forGetter(DotProduct::stepX),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_y").orElse(0).forGetter(DotProduct::stepY),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step_z").orElse(0).forGetter(DotProduct::stepZ)
                    ).apply(instance, DotProduct::create)
            );

    public static final KeyDispatchDataCodec<DotProduct> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    public static final String NAME = "dot_product";

    private static DotProduct create(DensityFunction arg1,
                                     DensityFunction arg2,
                                     int stepX,
                                     int stepY,
                                     int stepZ) {
        if ((stepX | stepY | stepZ) == 0) {
            throw new IllegalArgumentException("Dot Product must contain at least one non-zero step component!");
        }
        return new DotProduct(arg1, arg2, stepX, stepY, stepZ);
    }

    private record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double gradX1 = 0.0D, gradY1 = 0.0D, gradZ1 = 0.0D;
        double gradX2 = 0.0D, gradY2 = 0.0D, gradZ2 = 0.0D;

        if (stepX != 0) {
            BlockContext posForwards = new BlockContext(x + stepX, y, z);
            BlockContext posBackwards = new BlockContext(x - stepX, y, z);

            gradX1 = (arg1.compute(posForwards) -
                    arg1.compute(posBackwards)) / (2.0D * stepX);

            gradX2 = (arg2.compute(posForwards) -
                    arg2.compute(posBackwards)) / (2.0D * stepX);
        }

        if (stepY != 0) {
            BlockContext posForwards = new BlockContext(x, y + stepY, z);
            BlockContext posBackwards = new BlockContext(x, y - stepY, z);

            gradY1 = (arg1.compute(posForwards) -
                    arg1.compute(posBackwards)) / (2.0D * stepY);

            gradY2 = (arg2.compute(posForwards) -
                    arg2.compute(posBackwards)) / (2.0D * stepY);
        }

        if (stepZ != 0) {
            BlockContext posForwards = new BlockContext(x, y, z + stepZ);
            BlockContext posBackwards = new BlockContext(x, y, z - stepZ);

            gradZ1 = (arg1.compute(posForwards) -
                    arg1.compute(posBackwards)) / (2.0D * stepZ);

            gradZ2 = (arg2.compute(posForwards) -
                    arg2.compute(posBackwards)) / (2.0D * stepZ);
        }

        return gradX1 * gradX2 + gradY1 * gradY2 + gradZ1 * gradZ2;
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new DotProduct(
                        arg1.mapAll(visitor),
                        arg2.mapAll(visitor),
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
        return -Double.MAX_VALUE;
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