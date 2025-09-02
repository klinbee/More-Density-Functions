package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import static com.klinbee.moredensityfunctions.MoreDensityFunctionsConstants.DENSITY_FUNCTION_ARRAY_CODEC;

public record GappedGridSquareSpiral(int xSize,
                                     int zSize,
                                     int spacing,
                                     DensityFunction[] gridCellArgs,
                                     DensityFunction oobArg)
        implements DensityFunction {

    private static final MapCodec<GappedGridSquareSpiral> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("x_size").forGetter(GappedGridSquareSpiral::xSize),
                    ExtraCodecs.POSITIVE_INT.fieldOf("z_size").forGetter(GappedGridSquareSpiral::zSize),
                    ExtraCodecs.POSITIVE_INT.fieldOf("spacing").orElse(1).forGetter(GappedGridSquareSpiral::spacing),
                    DENSITY_FUNCTION_ARRAY_CODEC.fieldOf("grid_cell_args").forGetter(GappedGridSquareSpiral::gridCellArgs),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("out_of_bounds_argument").orElse(DensityFunctions.constant(-1)).forGetter(GappedGridSquareSpiral::oobArg)
            ).apply(instance, GappedGridSquareSpiral::create)
    );

    public static final TypedCodec<GappedGridSquareSpiral> TYPED_CODEC = new TypedCodec<>("gapped_grid_square_spiral", KeyDispatchDataCodec.of(MAP_CODEC));

    private static GappedGridSquareSpiral create(int xSize,
                                                 int zSize,
                                                 int spacing,
                                                 DensityFunction[] gridCellArgs,
                                                 DensityFunction oobArg) {
        // We add 1 to spacing, because if we want spacing of 1 grid, we need to use a modulo 2, etc.
        return new GappedGridSquareSpiral(xSize, zSize, spacing + 1, gridCellArgs, oobArg);
    }


    @Override
    public double compute(DensityFunction.FunctionContext pos) {

        int gridX = StrictMath.floorDiv(pos.blockX(), xSize);
        int gridZ = StrictMath.floorDiv(pos.blockZ(), zSize);

        // Check if we're on a valid grid point
        if ((gridX % spacing != 0) || (gridZ % spacing != 0)) {
            return oobArg.compute(pos);
        }

        // Scale the grid positions
        int normalizedGridX = gridX / spacing;
        int normalizedGridZ = gridZ / spacing;

        int index = getSpiralIndex(normalizedGridX, normalizedGridZ);

        int numFunctions = gridCellArgs.length;

        // >= because array indices
        if (index >= numFunctions) {
            return oobArg.compute(pos);
        }

        DensityFunction arg = gridCellArgs[index];
        return arg.compute(pos);
    }

    private static int getSpiralIndex(int spiralX, int spiralZ) {
        // Index formula doesn't work for origin
        if (spiralX == 0 && spiralZ == 0) {
            return 0;
        }

        // Chebyshev distance from origin is the ring the point is in
        int ring = StrictMath.max(StrictMath.abs(spiralX), StrictMath.abs(spiralZ));

        // Each ring is 8 * ring, so sum of rings is 8 * sum of consecutive integers n * (n + 1) / 2
        // Therefore, end_index(ring) = 8 * (ring * (ring + 1)) / 2 -> 4 * ring * (ring + 1)
        // So starting index should be 1 + end_index(ring - 1) -> 1 + 4 * (ring - 1) * ring
        int index = 1 + 4 * ring * (ring - 1);

        int indexOffset = getOffsetFromStartIndex(spiralX, spiralZ, ring);

        return index + indexOffset;
    }

    private static int getOffsetFromStartIndex(int spiralX, int spiralZ, int ring) {

        int positionInRing;

        if (spiralX == ring && spiralZ >= -ring && spiralZ < ring) {
            // Right edge, moving up
            positionInRing = spiralZ + ring;
        } else if (spiralZ == ring && spiralX <= ring && spiralX > -ring) {
            // Top edge, moving left
            positionInRing = ring * 2 + (ring - spiralX);
        } else if (spiralX == -ring && spiralZ <= ring && spiralZ > -ring) {
            // Left edge, moving down
            positionInRing = ring * 4 + (ring - spiralZ);
        } else if (spiralZ == -ring && spiralX >= -ring && spiralX < ring) {
            // Bottom edge, moving right
            positionInRing = ring * 6 + (spiralX + ring);
        } else {
            throw new IllegalArgumentException("Invalid coordinate (" + spiralX + ", " + spiralZ + ") for spiral");
        }

        return positionInRing;
    }

    @Override
    public void fillArray(double[] densities, DensityFunction.ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(DensityFunction.Visitor visitor) {

        // `.mapAll()` cannot be applied to `gridCellArgs` or else, it will lag out Minecraft

        return visitor.apply(
                new GappedGridSquareSpiral(
                        xSize,
                        zSize,
                        spacing,
                        gridCellArgs,
                        oobArg.mapAll(visitor)

                )
        );
    }

    @Override
    public double minValue() {
        return StrictMath.min(oobArg.minValue(), 0.0D);
    }

    @Override
    public double maxValue() {
        return StrictMath.max(oobArg.maxValue(), 255.0D);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}
