package com.klinbee.moredensityfunctions.registration;

import com.klinbee.moredensityfunctions.densityfunctions.*;
import com.klinbee.moredensityfunctions.randomsamplers.*;

public class CommonRegistrations {

    /// Registry IDs
    public static final String DENSITY_FUNCTIONS = "density_functions";
    public static final String RANDOM_SAMPLERS = "random_samplers";

    public static void registerCommon(RegistrationFunction registrar) {

        /// DensityFunctions
        registrar.register(DENSITY_FUNCTIONS, ArcCosine.NAME, ArcCosine.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, ArcSine.NAME, ArcSine.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, ArcTangent.NAME, ArcTangent.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Cache.NAME, Cache.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Ceil.NAME, Ceil.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Clamp.NAME, Clamp.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Cosine.NAME, Cosine.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Derivative.NAME, Derivative.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Divide.NAME, Divide.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, DotProduct.NAME, DotProduct.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Floor.NAME, Floor.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, FloorDivide.NAME, FloorDivide.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, FloorModulo.NAME, FloorModulo.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, GappedGridSquareSpiral.NAME, GappedGridSquareSpiral.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, GradientMagnitude.NAME, GradientMagnitude.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, IEEERemainder.NAME, IEEERemainder.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Log.NAME, Log.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Log2.NAME, Log2.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Log2Floor.NAME, Log2Floor.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, NaturalLog.NAME, NaturalLog.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Negate.NAME, Negate.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, PolarCoords.NAME, PolarCoords.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Power.NAME, Power.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Profiler.NAME, Profiler.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Reciprocal.NAME, Reciprocal.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Remainder.NAME, Remainder.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Round.NAME, Round.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Shift.NAME, Shift.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Sigmoid.NAME, Sigmoid.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Signum.NAME, Signum.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Sine.NAME, Sine.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, SingleChannelImageTessellation.NAME, SingleChannelImageTessellation.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, SquareRoot.NAME, SquareRoot.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Subtract.NAME, Subtract.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, Tangent.NAME, Tangent.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, ValueNoise.NAME, ValueNoise.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, VectorAngle.NAME, VectorAngle.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, XClampedGradient.NAME, XClampedGradient.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, XPos.NAME, XPos.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, YPos.NAME, YPos.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, ZClampedGradient.NAME, ZClampedGradient.CODEC.codec());
        registrar.register(DENSITY_FUNCTIONS, ZPos.NAME, ZPos.CODEC.codec());

        /// RandomSamplers
        registrar.register(RANDOM_SAMPLERS, BetaSampler.NAME, BetaSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, BinomialSampler.NAME, BinomialSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, ExponentialSampler.NAME, ExponentialSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, GammaSampler.NAME, GammaSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, GeometricSampler.NAME, GeometricSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, NormalSampler.NAME, NormalSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, PoissonSampler.NAME, PoissonSampler.CODEC.codec());
        registrar.register(RANDOM_SAMPLERS, UniformSampler.NAME, UniformSampler.CODEC.codec());
    }
}