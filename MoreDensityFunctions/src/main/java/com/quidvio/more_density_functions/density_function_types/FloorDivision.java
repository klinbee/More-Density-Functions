package com.quidvio.more_density_functions.density_function_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.quidvio.more_density_functions.MoreDensityFunctionsMain;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Optional;

public record FloorDivision(DensityFunction dividend, DensityFunction divisor, Optional<Double> maxOutput, Optional<Double> minOutput, Optional<DensityFunction> errorDf) implements DensityFunction {

    private static final MapCodec<FloorDivision> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DensityFunction.FUNCTION_CODEC.fieldOf("dividend").forGetter(FloorDivision::dividend), DensityFunction.FUNCTION_CODEC.fieldOf("divisor").forGetter(FloorDivision::divisor), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).optionalFieldOf("max_output").forGetter(FloorDivision::maxOutput), Codec.doubleRange(-Double.MAX_VALUE, Double.MAX_VALUE).optionalFieldOf("min_output").forGetter(FloorDivision::minOutput), DensityFunction.FUNCTION_CODEC.optionalFieldOf("error_output").forGetter(FloorDivision::errorDf)).apply(instance, (FloorDivision::new)));
    public static final CodecHolder<FloorDivision> CODEC = DensityFunctionTypes.holderOf(MAP_CODEC);

    @Override
    public double sample(NoisePos pos) {
        int divisorValue = (int)this.divisor.sample(pos);
        int dividendValue = (int)this.dividend.sample(pos);

        if (divisorValue == 0) {
            if (errorDf.isPresent()) {
                return this.errorDf.get().sample(pos);
            }
            return MoreDensityFunctionsMain.DEFAULT_ERROR;
        }

        double result = Math.floorDiv(dividendValue,divisorValue);


        if (result > this.maxOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MAX_OUTPUT)) {
            return this.maxOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MAX_OUTPUT);
        }

        if (result < this.minOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MIN_OUTPUT)) {
            return this.minOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MIN_OUTPUT);
        }

        return result;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities,this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        if (this.errorDf.isPresent()) {
            return visitor.apply(new FloorDivision(this.dividend.apply(visitor), this.divisor.apply(visitor), this.maxOutput, this.minOutput, Optional.of(this.errorDf.get().apply(visitor))));
        }
        return visitor.apply(new FloorDivision(this.dividend.apply(visitor), this.divisor.apply(visitor), this.maxOutput, this.minOutput, Optional.empty()));
    }

    @Override
    public DensityFunction dividend() {
        return dividend;
    }

    @Override
    public DensityFunction divisor() {
        return divisor;
    }

    @Override
    public Optional<DensityFunction> errorDf() {
        return this.errorDf;
    }

    @Override
    public double minValue() {
        if (errorDf.isPresent()) {
            return Math.min(this.errorDf.get().minValue(),this.minOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MIN_OUTPUT));
        }
        return Math.min(MoreDensityFunctionsMain.DEFAULT_ERROR,this.minOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MIN_OUTPUT));
    }

    @Override
    public double maxValue() {
        if (errorDf.isPresent()) {
            return Math.max(this.errorDf.get().maxValue(),this.maxOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MAX_OUTPUT));
        }
        return Math.max(MoreDensityFunctionsMain.DEFAULT_ERROR,this.maxOutput.orElse(MoreDensityFunctionsMain.DEFAULT_MAX_OUTPUT));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }
}
