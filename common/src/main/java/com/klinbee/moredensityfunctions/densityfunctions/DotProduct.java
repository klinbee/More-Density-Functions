package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.BlockContext;
import com.klinbee.moredensityfunctions.util.MDFMath;
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

    public static final TypedCodec<DotProduct> TYPED_CODEC = new TypedCodec<>("dot_product", KeyDispatchDataCodec.of(MAP_CODEC));

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

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double gradX1 = 0.0D, gradY1 = 0.0D, gradZ1 = 0.0D;
        double gradX2 = 0.0D, gradY2 = 0.0D, gradZ2 = 0.0D;

        if (stepX != 0) {
            BlockContext posForwards = new BlockContext(x + stepX, y, z);
            BlockContext posBackwards = new BlockContext(x - stepX, y, z);

            gradX1 = MDFMath.centralDifference(arg1.compute(posForwards),
                    arg1.compute(posBackwards), stepX);

            gradX2 = MDFMath.centralDifference(arg2.compute(posForwards),
                    arg2.compute(posBackwards), stepX);
        }

        if (stepY != 0) {
            BlockContext posForwards = new BlockContext(x, y + stepY, z);
            BlockContext posBackwards = new BlockContext(x, y - stepY, z);

            gradY1 = MDFMath.centralDifference(arg1.compute(posForwards),
                    arg1.compute(posBackwards), stepY);

            gradY2 = MDFMath.centralDifference(arg2.compute(posForwards),
                    arg2.compute(posBackwards), stepY);
        }

        if (stepZ != 0) {
            BlockContext posForwards = new BlockContext(x, y, z + stepZ);
            BlockContext posBackwards = new BlockContext(x, y, z - stepZ);

            gradZ1 = MDFMath.centralDifference(arg1.compute(posForwards),
                    arg1.compute(posBackwards), stepZ);

            gradZ2 = MDFMath.centralDifference(arg2.compute(posForwards),
                    arg2.compute(posBackwards), stepZ);
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

        double minGradX1 = 0.0D, minGradY1 = 0.0D, minGradZ1 = 0.0D;
        double minGradX2 = 0.0D, minGradY2 = 0.0D, minGradZ2 = 0.0D;

        if (stepX != 0) {
            minGradX1 = MDFMath.centralDifference(arg1.minValue(),
                    arg1.maxValue(), stepX);

            minGradX2 = MDFMath.centralDifference(arg2.minValue(),
                    arg2.maxValue(), stepX);
        }

        if (stepY != 0) {
            minGradY1 = MDFMath.centralDifference(arg1.minValue(),
                    arg1.maxValue(), stepY);

            minGradY2 = MDFMath.centralDifference(arg2.minValue(),
                    arg2.maxValue(), stepY);
        }

        if (stepZ != 0) {
            minGradZ1 = MDFMath.centralDifference(arg1.minValue(),
                    arg1.maxValue(), stepZ);

            minGradZ2 = MDFMath.centralDifference(arg2.minValue(),
                    arg2.maxValue(), stepZ);
        }

        return minGradX1 * minGradX2 + minGradY1 * minGradY2 + minGradZ1 * minGradZ2;
    }

    @Override
    public double maxValue() {

        double maxGradX1 = 0.0D, maxGradY1 = 0.0D, maxGradZ1 = 0.0D;
        double maxGradX2 = 0.0D, maxGradY2 = 0.0D, maxGradZ2 = 0.0D;

        if (stepX != 0) {
            maxGradX1 = MDFMath.centralDifference(arg1.maxValue(),
                    arg1.minValue(), stepX);

            maxGradX2 = MDFMath.centralDifference(arg2.maxValue(),
                    arg2.minValue(), stepX);
        }

        if (stepY != 0) {
            maxGradY1 = MDFMath.centralDifference(arg1.maxValue(),
                    arg1.minValue(), stepY);

            maxGradY2 = MDFMath.centralDifference(arg2.maxValue(),
                    arg2.minValue(), stepY);
        }

        if (stepZ != 0) {
            maxGradZ1 = MDFMath.centralDifference(arg1.maxValue(),
                    arg1.minValue(), stepZ);

            maxGradZ2 = MDFMath.centralDifference(arg2.maxValue(),
                    arg2.minValue(), stepZ);
        }

        return maxGradX1 * maxGradX2 + maxGradY1 * maxGradY2 + maxGradZ1 * maxGradZ2;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}