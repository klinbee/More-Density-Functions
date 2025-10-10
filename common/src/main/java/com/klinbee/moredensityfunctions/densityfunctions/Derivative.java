package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.klinbee.moredensityfunctions.util.BlockContext;
import com.klinbee.moredensityfunctions.util.MDFMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record Derivative(DensityFunction arg,
                         DerivativeComponent componentX,
                         DerivativeComponent componentY,
                         DerivativeComponent componentZ)
        implements DensityFunction {

    private static final MapCodec<Derivative> MAP_CODEC =
            RecordCodecBuilder.mapCodec((instance) ->
                    instance.group(
                            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(Derivative::arg),
                            DerivativeComponent.CODEC.fieldOf("component_x").orElse(DerivativeComponent.NONE).forGetter(Derivative::componentX),
                            DerivativeComponent.CODEC.fieldOf("component_y").orElse(DerivativeComponent.NONE).forGetter(Derivative::componentY),
                            DerivativeComponent.CODEC.fieldOf("component_z").orElse(DerivativeComponent.NONE).forGetter(Derivative::componentZ)
                    ).apply(instance, Derivative::create)
            );

    public static final TypedCodec<Derivative> TYPED_CODEC = new TypedCodec<>("derivative", KeyDispatchDataCodec.of(MAP_CODEC));

    private static Derivative create(DensityFunction arg,
                                     DerivativeComponent componentX,
                                     DerivativeComponent componentY,
                                     DerivativeComponent componentZ) {
        if ((componentX.step | componentY.step | componentZ.step) == 0) {
            throw new IllegalArgumentException("Derivative must contain at least one non-trivial directional component!");
        }
        return new Derivative(arg, componentX, componentY, componentZ);
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX(), y = pos.blockY(), z = pos.blockZ();

        double dirX = 0.0D, dirY = 0.0D, dirZ = 0.0D;
        double gradX = 0.0D, gradY = 0.0D, gradZ = 0.0D;

        if (componentX.step != 0) {
            BlockContext posForwards = new BlockContext(x + componentX.step, y, z);
            BlockContext posBackwards = new BlockContext(x - componentX.step, y, z);

            dirX = componentX.direction.compute(pos);
            gradX = MDFMath.centralDifference(arg.compute(posForwards),
                    arg.compute(posBackwards), componentX.step);
        }

        if (componentY.step != 0) {
            BlockContext posForwards = new BlockContext(x, y + componentY.step, z);
            BlockContext posBackwards = new BlockContext(x, y - componentY.step, z);

            dirY = componentY.direction.compute(pos);
            gradY = MDFMath.centralDifference(arg.compute(posForwards),
                    arg.compute(posBackwards), componentY.step);
        }

        if (componentZ.step != 0) {
            BlockContext posForwards = new BlockContext(x, y, z + componentZ.step);
            BlockContext posBackwards = new BlockContext(x, y, z - componentZ.step);

            dirZ = componentZ.direction.compute(pos);
            gradZ = MDFMath.centralDifference(arg.compute(posForwards),
                    arg.compute(posBackwards), componentZ.step);
        }

        double magnitude = MDFMath.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        return magnitude == 0.0D ? 0.0D : (dirX * gradX + dirY * gradY + dirZ * gradZ) / magnitude;
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        componentX.direction.mapAll(visitor);
        componentY.direction.mapAll(visitor);
        componentZ.direction.mapAll(visitor);
        return visitor.apply(
                new Derivative(
                        arg.mapAll(visitor),
                        componentX,
                        componentY,
                        componentZ
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
        return TYPED_CODEC.codec();
    }

    /// Derivative Component codec
    private record DerivativeComponent(int step, DensityFunction direction) {
        static final Codec<DerivativeComponent> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("step").forGetter(DerivativeComponent::step),
                                DensityFunction.HOLDER_HELPER_CODEC.fieldOf("direction").forGetter(DerivativeComponent::direction)
                        ).apply(instance, DerivativeComponent::new)
                );
        static final DerivativeComponent NONE = new DerivativeComponent(0, DensityFunctions.constant(0));
    }
}