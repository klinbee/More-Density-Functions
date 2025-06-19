package com.klinbee.moredensityfunctions.densityfunctions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;


public interface DirectionalDerivative extends DensityFunction {
    MapCodec<DirectionalDerivative> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(DirectionalDerivative::arg),
            DerivativeComponent.CODEC.optionalFieldOf("component_x").forGetter(DirectionalDerivative::componentHolderX),
            DerivativeComponent.CODEC.optionalFieldOf("component_y").forGetter(DirectionalDerivative::componentHolderY),
            DerivativeComponent.CODEC.optionalFieldOf("component_z").forGetter(DirectionalDerivative::componentHolderZ)
    ).apply(instance, DirectionalDerivative::create));
    KeyDispatchDataCodec<DirectionalDerivative> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    /// Derivative Component CODEC ///
    record DerivativeComponent(int step, DensityFunction direction) {
        public static final Codec<DerivativeComponent> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(1, 30_000_000).fieldOf("step").forGetter(DerivativeComponent::step),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("direction").forGetter(DerivativeComponent::direction)
                ).apply(instance, DerivativeComponent::new)
        );
    }
    
    DensityFunction arg();
    Optional<DerivativeComponent> componentHolderX();
    Optional<DerivativeComponent> componentHolderY();
    Optional<DerivativeComponent> componentHolderZ();

    record BlockContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext { }
    
    static DirectionalDerivative create(DensityFunction arg,
                                        Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ) {
        boolean xPresent, yPresent, zPresent;
        xPresent = componentHolderX.isPresent();
        yPresent = componentHolderY.isPresent();
        zPresent = componentHolderZ.isPresent();
        if (xPresent && yPresent && zPresent) {
            return new DerivativeXYZ(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderX.get(), componentHolderY.get(), componentHolderZ.get()
            );
        }
        if (xPresent && yPresent) {
            return new DerivativeXY(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderX.get(), componentHolderY.get()
            );
        }
        if (xPresent && zPresent) {
            return new DerivativeXZ(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderX.get(), componentHolderZ.get()
            );
        }
        if (yPresent && zPresent) {
            return new DerivativeYZ(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderY.get(), componentHolderZ.get()
            );
        }
        if (xPresent) {
            return new DerivativeX(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderX.get()
            );
        }
        if (yPresent) {
            return new DerivativeY(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderY.get()
            );
        }
        if (zPresent) {
            return new DerivativeZ(arg,
                    componentHolderX, componentHolderY, componentHolderZ,
                    componentHolderZ.get()
            );
        }
        throw new IllegalArgumentException("Directional Derivative must contain at least one directional component!");
    }

    record DerivativeX(DensityFunction arg,
                       Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                       DerivativeComponent componentX
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            int stepX = componentX.step;

            BlockContext forwardStepX = new BlockContext(x+stepX,y,z);
            BlockContext backwardStepX = new BlockContext(x-stepX,y,z);

            int directionVecX = (int) StrictMath.signum(componentX.direction.compute(pos));

            double derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);

            return directionVecX * derivativeX;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeX(arg, componentHolderX, componentHolderY, componentHolderZ, componentX));
        }
    }

    record DerivativeY(DensityFunction arg,
                       Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                       DerivativeComponent componentY
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            int stepY = componentY.step;

            BlockContext forwardStepY = new BlockContext(x,y+stepY,z);
            BlockContext backwardStepY = new BlockContext(x,y-stepY,z);

            int directionVecY = (int) StrictMath.signum(componentY.direction.compute(pos));

            double derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);

            return directionVecY * derivativeY;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeY(arg, componentHolderX, componentHolderY, componentHolderZ, componentY));
        }
    }

    record DerivativeZ(DensityFunction arg,
                       Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                       DerivativeComponent componentZ
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();

            int stepZ = componentZ.step;

            BlockContext forwardStepZ = new BlockContext(x,y,z+stepZ);
            BlockContext backwardStepZ = new BlockContext(x,y,z-stepZ);

            int directionVecZ = (int) StrictMath.signum(componentZ.direction.compute(pos));

            double derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return directionVecZ * derivativeZ;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeZ(arg, componentHolderX, componentHolderY, componentHolderZ, componentZ));
        }
    }

    record DerivativeXY(DensityFunction arg,
                        Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                        DerivativeComponent componentX, DerivativeComponent componentY
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;
            int stepX, stepY;
            
            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();
            stepX = componentX.step; stepY = componentY.step;
            
            BlockContext forwardStepX, backwardStepX, forwardStepY, backwardStepY;
            
            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);
            
            double directionMagnitude, directionVecX, directionVecY;
            double directionVecNormX, directionVecNormY, derivativeX, derivativeY;
            
            directionVecX = componentX.direction.compute(pos);
            directionVecY = componentY.direction.compute(pos);

            directionMagnitude = StrictMath.sqrt(directionVecX * directionVecX
                    + directionVecY * directionVecY);

            directionVecNormX = directionVecX / directionMagnitude;
            directionVecNormY = directionVecY / directionMagnitude;

            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);

            return directionVecNormX * derivativeX
                    + directionVecNormY * derivativeY;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeXY(arg, componentHolderX, componentHolderY, componentHolderZ, componentX, componentY));
        }
    }

    record DerivativeXZ(DensityFunction arg,
                        Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                        DerivativeComponent componentX, DerivativeComponent componentZ
    ) implements DirectionalDerivative {

        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;
            int stepX, stepZ;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();
            stepX = componentX.step; stepZ = componentZ.step;

            BlockContext forwardStepX, backwardStepX, forwardStepZ, backwardStepZ;

            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double directionMagnitude, directionVecX, directionVecZ;
            double directionVecNormX, directionVecNormZ, derivativeX, derivativeZ;

            directionVecX = componentX.direction.compute(pos);
            directionVecZ = componentZ.direction.compute(pos);

            directionMagnitude = StrictMath.sqrt(directionVecX * directionVecX
                    + directionVecZ * directionVecZ);

            directionVecNormX = directionVecX / directionMagnitude;
            directionVecNormZ = directionVecZ / directionMagnitude;

            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return directionVecNormX * derivativeX
                    + directionVecNormZ * derivativeZ;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeXZ(arg, componentHolderX, componentHolderY, componentHolderZ, componentX, componentZ));
        }
    }

    record DerivativeYZ(DensityFunction arg,
                        Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                        DerivativeComponent componentY, DerivativeComponent componentZ
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;
            int stepY, stepZ;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();
            stepY = componentY.step; stepZ = componentZ.step;

            BlockContext forwardStepY, backwardStepY, forwardStepZ, backwardStepZ;

            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double directionMagnitude, directionVecY, directionVecZ;
            double directionVecNormY, directionVecNormZ, derivativeY, derivativeZ;

            directionVecY = componentY.direction.compute(pos);
            directionVecZ = componentZ.direction.compute(pos);

            directionMagnitude = StrictMath.sqrt(directionVecY * directionVecY
                    + directionVecZ * directionVecZ);

            directionVecNormY = directionVecY / directionMagnitude;
            directionVecNormZ = directionVecZ / directionMagnitude;

            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return directionVecNormY * derivativeY
                    + directionVecNormZ * derivativeZ;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeYZ(arg, componentHolderX, componentHolderY, componentHolderZ, componentY, componentZ));
        }
    }

    record DerivativeXYZ(DensityFunction arg,
                         Optional<DerivativeComponent> componentHolderX, Optional<DerivativeComponent> componentHolderY, Optional<DerivativeComponent> componentHolderZ,
                         DerivativeComponent componentX, DerivativeComponent componentY, DerivativeComponent componentZ
    ) implements DirectionalDerivative {
        @Override
        public double compute(FunctionContext pos) {
            int x, y, z;
            int stepX, stepY, stepZ;

            x = pos.blockX(); y= pos.blockY(); z = pos.blockZ();
            stepX = componentX.step; stepY = componentY.step; stepZ = componentZ.step;

            BlockContext forwardStepX, backwardStepX, forwardStepY, backwardStepY, forwardStepZ, backwardStepZ;

            forwardStepX = new BlockContext(x+stepX,y,z);
            backwardStepX = new BlockContext(x-stepX,y,z);
            forwardStepY = new BlockContext(x,y+stepY,z);
            backwardStepY = new BlockContext(x,y-stepY,z);
            forwardStepZ = new BlockContext(x,y,z+stepZ);
            backwardStepZ = new BlockContext(x,y,z-stepZ);

            double directionMagnitude, directionVecX, directionVecY, directionVecZ;
            double directionVecNormX, directionVecNormY, directionVecNormZ, derivativeX, derivativeY, derivativeZ;

            directionVecX = componentX.direction.compute(pos);
            directionVecY = componentY.direction.compute(pos);
            directionVecZ = componentZ.direction.compute(pos);
            
            directionMagnitude = StrictMath.sqrt(directionVecX * directionVecX
                    + directionVecY * directionVecY
                    + directionVecZ * directionVecZ);
            
            directionVecNormX = directionVecX / directionMagnitude;
            directionVecNormY = directionVecY / directionMagnitude;
            directionVecNormZ = directionVecZ / directionMagnitude;
            
            derivativeX = (arg.compute(forwardStepX) - arg.compute(backwardStepX)) / (2*stepX);
            derivativeY = (arg.compute(forwardStepY) - arg.compute(backwardStepY)) / (2*stepY);
            derivativeZ = (arg.compute(forwardStepZ) - arg.compute(backwardStepZ)) / (2*stepZ);

            return directionVecNormX * derivativeX
                    + directionVecNormY * derivativeY
                    + directionVecNormZ * derivativeZ;
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new DirectionalDerivative.DerivativeXYZ(arg, componentHolderX, componentHolderY, componentHolderZ, componentX, componentY, componentZ));
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
