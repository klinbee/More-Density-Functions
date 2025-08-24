package com.klinbee.moredensityfunctions.registration;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.klinbee.moredensityfunctions.randomsamplers.*;

import static com.klinbee.moredensityfunctions.registration.RegistryKey.DENSITY_FUNCTION;
import static com.klinbee.moredensityfunctions.registration.RegistryKey.RANDOM_SAMPLER;

public class CommonRegistrations {

    public static void registerCommon(RegistrationFunction registrar) {

        /// DensityFunctions
        registrar.register(DENSITY_FUNCTION, ArcCosine.NAME, ArcCosine.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, ArcSine.NAME, ArcSine.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, ArcTangent.NAME, ArcTangent.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Cache.NAME, Cache.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Ceil.NAME, Ceil.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Clamp.NAME, Clamp.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Cosine.NAME, Cosine.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Derivative.NAME, Derivative.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Distance.NAME, Distance.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Divide.NAME, Divide.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, DotProduct.NAME, DotProduct.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Floor.NAME, Floor.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, FloorDivide.NAME, FloorDivide.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, FloorModulo.NAME, FloorModulo.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, GappedGridSquareSpiral.NAME, GappedGridSquareSpiral.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, GradientMagnitude.NAME, GradientMagnitude.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, IEEERemainder.NAME, IEEERemainder.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Log.NAME, Log.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Log2.NAME, Log2.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Log2Floor.NAME, Log2Floor.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, NaturalLog.NAME, NaturalLog.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Negate.NAME, Negate.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, PolarCoords.NAME, PolarCoords.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Power.NAME, Power.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Profiler.NAME, Profiler.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Radius.NAME, Radius.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Radius3D.NAME, Radius3D.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Reciprocal.NAME, Reciprocal.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Remainder.NAME, Remainder.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Round.NAME, Round.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Shift.NAME, Shift.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Sigmoid.NAME, Sigmoid.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Signum.NAME, Signum.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Sine.NAME, Sine.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, SingleChannelImageTessellation.NAME, SingleChannelImageTessellation.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, SquareRoot.NAME, SquareRoot.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Subtract.NAME, Subtract.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, Tangent.NAME, Tangent.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, ValueNoise.NAME, ValueNoise.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, VectorAngle.NAME, VectorAngle.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, XClampedGradient.NAME, XClampedGradient.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, XPos.NAME, XPos.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, YPos.NAME, YPos.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, ZClampedGradient.NAME, ZClampedGradient.CODEC.codec());
        registrar.register(DENSITY_FUNCTION, ZPos.NAME, ZPos.CODEC.codec());

        /// RandomSamplers
        registrar.register(RANDOM_SAMPLER, BetaSampler.NAME, BetaSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, BinomialSampler.NAME, BinomialSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, ExponentialSampler.NAME, ExponentialSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, GammaSampler.NAME, GammaSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, GeometricSampler.NAME, GeometricSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, NormalSampler.NAME, NormalSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, PoissonSampler.NAME, PoissonSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLER, UniformSampler.NAME, UniformSampler.CODEC.codec());
    }
}