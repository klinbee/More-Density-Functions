package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.Optional;

public record Derivative(DensityFunction arg,
                         Optional<DerivativeComponent> componentHolderX,
                         Optional<DerivativeComponent> componentHolderY,
                         Optional<DerivativeComponent> componentHolderZ)
        implements DensityFunction {

    private static final MapCodec<Derivative> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Derivative::arg),
                            DerivativeComponent.CODEC.optionalFieldOf("component_x").forGetter(Derivative::componentHolderX),
                            DerivativeComponent.CODEC.optionalFieldOf("component_y").forGetter(Derivative::componentHolderY),
                            DerivativeComponent.CODEC.optionalFieldOf("component_z").forGetter(Derivative::componentHolderZ)
                    ).apply(instance, Derivative::create)
            );

    public static final KeyDispatchDataCodec<Derivative> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    private static Derivative create(DensityFunction arg,
                                     Optional<DerivativeComponent> componentHolderX,
                                     Optional<DerivativeComponent> componentHolderY,
                                     Optional<DerivativeComponent> componentHolderZ) {
        if (componentHolderX.isEmpty() && componentHolderY.isEmpty() && componentHolderZ.isEmpty()) {
            throw new IllegalArgumentException("Derivative must contain at least one valid directional component!");
        }
        return new Derivative(arg, componentHolderX, componentHolderY, componentHolderZ);
    }

    /// Derivative Component CODEC
    private record DerivativeComponent(int step, DensityFunction direction) {
        static final Codec<DerivativeComponent> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                MoreDensityFunctionsConstants.POSITIVE_INT.fieldOf("step").forGetter(DerivativeComponent::step),
                                DensityFunction.HOLDER_HELPER_CODEC.fieldOf("direction").forGetter(DerivativeComponent::direction)
                        ).apply(instance, DerivativeComponent::new)
                );
    }

    private record BlockContext(int blockX, int blockY, int blockZ) implements FunctionContext {
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double dirX = 0.0D, dirY = 0.0D, dirZ = 0.0D;
        double gradX = 0.0D, gradY = 0.0D, gradZ = 0.0D;

        if (componentHolderX.isPresent()) {
            var comp = componentHolderX.get();
            dirX = comp.direction.compute(pos);
            gradX = (arg.compute(new BlockContext(x + comp.step, y, z)) -
                    arg.compute(new BlockContext(x - comp.step, y, z))) / (2.0D * comp.step);
        }

        if (componentHolderY.isPresent()) {
            var comp = componentHolderY.get();
            dirY = comp.direction.compute(pos);
            gradY = (arg.compute(new BlockContext(x, y + comp.step, z)) -
                    arg.compute(new BlockContext(x, y - comp.step, z))) / (2.0D * comp.step);
        }

        if (componentHolderZ.isPresent()) {
            var comp = componentHolderZ.get();
            dirZ = comp.direction.compute(pos);
            gradZ = (arg.compute(new BlockContext(x, y, z + comp.step)) -
                    arg.compute(new BlockContext(x, y, z - comp.step))) / (2.0D * comp.step);
        }

        // Single component case short-circuits
        if (componentHolderY.isEmpty() && componentHolderZ.isEmpty()) {
            return StrictMath.signum(dirX) * gradX;
        }
        if (componentHolderX.isEmpty() && componentHolderZ.isEmpty()) {
            return StrictMath.signum(dirY) * gradY;
        }
        if (componentHolderX.isEmpty() && componentHolderY.isEmpty()) {
            return StrictMath.signum(dirZ) * gradZ;
        }

        double magnitude = StrictMath.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        return magnitude == 0.0D ? 0.0D : (dirX * gradX + dirY * gradY + dirZ * gradZ) / magnitude;
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(
                new Derivative(
                        arg.mapAll(visitor),
                        componentHolderX.map(comp -> new DerivativeComponent(
                                comp.step(),
                                comp.direction().mapAll(visitor)
                        )),
                        componentHolderY.map(comp -> new DerivativeComponent(
                                comp.step(),
                                comp.direction().mapAll(visitor)
                        )),
                        componentHolderZ.map(comp -> new DerivativeComponent(
                                comp.step(),
                                comp.direction().mapAll(visitor)
                        ))
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