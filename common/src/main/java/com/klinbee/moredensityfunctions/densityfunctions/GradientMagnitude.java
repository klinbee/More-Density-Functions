package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public interface GradientMagnitude extends DensityFunction {
    MapCodec<GradientMagnitude> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(GradientMagnitude::arg),
            Codec.intRange(1, 30_000_000).optionalFieldOf("step_x").forGetter(GradientMagnitude::stepHolderX),
            Codec.intRange(1, 30_000_000).optionalFieldOf("step_y").forGetter(GradientMagnitude::stepHolderY),
            Codec.intRange(1, 30_000_000).optionalFieldOf("step_z").forGetter(GradientMagnitude::stepHolderZ)
    ).apply(instance, GradientMagnitude::create));
    KeyDispatchDataCodec<GradientMagnitude> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);
    
    DensityFunction arg();
    Optional<Integer> stepHolderX();
    Optional<Integer> stepHolderY();
    Optional<Integer> stepHolderZ();

    record BlockContext(int blockX, int blockY, int blockZ) implements FunctionContext { }
    
    static GradientMagnitude create(DensityFunction arg,
                                    Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ) {
        boolean xPresent, yPresent, zPresent;
        xPresent = stepHolderX.isPresent();
        yPresent = stepHolderY.isPresent();
        zPresent = stepHolderZ.isPresent();
        if (xPresent && yPresent && zPresent) {
            return new GradientXYZ(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderX.get(), stepHolderY.get(), stepHolderZ.get()
            );
        }
        if (xPresent && yPresent) {
            return new GradientXY(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderX.get(), stepHolderY.get()
            );
        }
        if (xPresent && zPresent) {
            return new GradientXZ(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderX.get(), stepHolderZ.get()
            );
        }
        if (yPresent && zPresent) {
            return new GradientYZ(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderY.get(), stepHolderZ.get()
            );
        }
        if (xPresent) {
            return new GradientX(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderX.get()
            );
        }
        if (yPresent) {
            return new GradientY(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderY.get()
            );
        }
        if (zPresent) {
            return new GradientZ(arg,
                    stepHolderX, stepHolderY, stepHolderZ,
                    stepHolderZ.get()
            );
        }
        throw new IllegalArgumentException("Directional Derivative must contain at least one directional component!");
    }

    record GradientX(DensityFunction arg,
                       Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                       int stepX
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepZ = new BlockContext(x+stepX,y,z);
            BlockContext backwardStepZ = new BlockContext(x-stepX,y,z);

            return (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepX);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientX(arg, stepHolderX, stepHolderY, stepHolderZ, stepX));
        }
    }

    record GradientY(DensityFunction arg,
                       Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                       int stepY
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepY = new BlockContext(x,y+stepY,z);
            BlockContext backwardStepY = new BlockContext(x,y-stepY,z);

            return (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientY(arg, stepHolderX, stepHolderY, stepHolderZ, stepY));
        }
    }

    record GradientZ(DensityFunction arg,
                       Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                       int stepZ
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepZ = new BlockContext(x,y,z+stepZ);
            BlockContext backwardStepZ = new BlockContext(x,y,z-stepZ);

            return (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientZ(arg, stepHolderX, stepHolderY, stepHolderZ, stepZ));
        }
    }

    record GradientXY(DensityFunction arg,
                        Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                        int stepX, int stepY
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;
            
            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();
            
            BlockContext forwardStepX, backwardStepX, forwardStepY, backwardStepY;
            
            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);

            double derivativeX, derivativeY;

            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);

            return StrictMath.sqrt(derivativeX * derivativeX
                    + derivativeY * derivativeY);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientXY(arg, stepHolderX, stepHolderY, stepHolderZ, stepX, stepY));
        }
    }

    record GradientXZ(DensityFunction arg,
                        Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                        int stepX, int stepZ
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepX, backwardStepX, forwardStepZ, backwardStepZ;

            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double derivativeX, derivativeZ;

            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return StrictMath.sqrt(derivativeX * derivativeX
                    + derivativeZ * derivativeZ);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientXZ(arg, stepHolderX, stepHolderY, stepHolderZ, stepX, stepZ));
        }
    }

    record GradientYZ(DensityFunction arg,
                        Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                        int stepY, int stepZ
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepY, backwardStepY, forwardStepZ, backwardStepZ;

            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double derivativeY, derivativeZ;

            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return StrictMath.sqrt(derivativeY * derivativeY
                    + derivativeZ * derivativeZ);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientYZ(arg, stepHolderX, stepHolderY, stepHolderZ, stepY, stepZ));
        }
    }

    record GradientXYZ(DensityFunction arg,
                         Optional<Integer> stepHolderX, Optional<Integer> stepHolderY, Optional<Integer> stepHolderZ,
                         int stepX, int stepY, int stepZ
    ) implements GradientMagnitude {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            BlockContext forwardStepX, backwardStepX, forwardStepY, backwardStepY, forwardStepZ, backwardStepZ;

            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double derivativeX, derivativeY, derivativeZ;
            
            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return StrictMath.sqrt(derivativeX * derivativeX
                    + derivativeY * derivativeY
                    + derivativeZ * derivativeZ);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new GradientXYZ(arg, stepHolderX, stepHolderY, stepHolderZ, stepX, stepY, stepZ));
        }
    }

    @Override
    default void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    default double minValue() {
        return -Double.MAX_VALUE;
    }

    @Override
    default double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    default KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
