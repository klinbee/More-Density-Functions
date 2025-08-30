package com.klinbee.moredensityfunctions.registration;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.klinbee.moredensityfunctions.randomsamplers.*;

import static com.klinbee.moredensityfunctions.registration.RegistryKey.DENSITY_FUNCTION;
import static com.klinbee.moredensityfunctions.registration.RegistryKey.RANDOM_SAMPLER;

public class CommonRegistrations {

    public static void registerCommon(RegistrationFunction registrar) {

        /// DensityFunctions
        registrar.register(DENSITY_FUNCTION, ArcCosine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, ArcSine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, ArcTangent.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Cache.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Ceil.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Clamp.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Cosine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Derivative.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Distance.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Divide.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, DotProduct.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Floor.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, FloorDivide.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, FloorModulo.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, GappedGridSquareSpiral.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, GradientMagnitude.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, HyperbolicCosine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, HyperbolicSine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, HyperbolicTangent.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, IEEERemainder.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Log.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Log2.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Log2Floor.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Modulo.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, NaturalLog.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Negate.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, OrElse.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, PolarCoords.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Power.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Profiler.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Radius.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Radius3D.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Reciprocal.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Remainder.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Round.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Shift.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Sigmoid.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Signum.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Sine.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, SingleChannelImageTessellation.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, SquareRoot.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Subtract.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, Tangent.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, ValueNoise.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, VectorAngle.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, XClampedGradient.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, XPos.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, YPos.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, ZClampedGradient.TYPED_CODEC);
        registrar.register(DENSITY_FUNCTION, ZPos.TYPED_CODEC);

        /// RandomSamplers
        registrar.register(RANDOM_SAMPLER, BetaSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, BinomialSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, ExponentialSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, GammaSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, GeometricSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, NormalSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, PoissonSampler.TYPED_CODEC);
        registrar.register(RANDOM_SAMPLER, UniformSampler.TYPED_CODEC);
    }
}