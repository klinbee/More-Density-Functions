package com.klinbee.moredensityfunctions.densityfunctions;

import com.klinbee.moredensityfunctions.registration.TypedCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.zip.InflaterInputStream;

public record SingleChannelImageTessellation(int xSize,
                                             int zSize,
                                             byte[] inflatedFrameData)
        implements DensityFunction {

    private static final MapCodec<SingleChannelImageTessellation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("x_size").forGetter(SingleChannelImageTessellation::xSize),
                    ExtraCodecs.POSITIVE_INT.fieldOf("z_size").forGetter(SingleChannelImageTessellation::zSize),
                    ExtraCodecs.BASE64_STRING.fieldOf("deflated_frame_data").forGetter(SingleChannelImageTessellation::inflatedFrameData)
            ).apply(instance, SingleChannelImageTessellation::create)
    );

    public static final TypedCodec<SingleChannelImageTessellation> TYPED_CODEC = new TypedCodec<>("single_channel_image_tessellation", KeyDispatchDataCodec.of(MAP_CODEC));

    private static SingleChannelImageTessellation create(int xSize,
                                                         int zSize,
                                                         byte[] deflatedFrameData) {
        byte[] inflatedBytes;
        try {
            var inflaterStream = new InflaterInputStream(new ByteArrayInputStream(deflatedFrameData));
            inflatedBytes = inflaterStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Error: More Density Functions failed to decompress frame data", e);
        }
        return new SingleChannelImageTessellation(xSize, zSize, inflatedBytes);
    }

    @Override
    public double compute(FunctionContext pos) {
        int x = pos.blockX();
        int z = pos.blockZ();
        int arrayPos = StrictMath.floorMod(x, xSize) + StrictMath.floorMod(z, zSize) * xSize;

        // Conversion from signed 8-bit int (what `byte` is in java) to an unsigned 8-bit int
        return inflatedFrameData[arrayPos] & 0xFF;
    }

    @Override
    public void fillArray(double[] densities, DensityFunction.ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(DensityFunction.Visitor visitor) {
        return visitor.apply(
                new SingleChannelImageTessellation(
                        xSize,
                        zSize,
                        inflatedFrameData
                )
        );
    }

    // Min and Max u8 Values
    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return 255.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return TYPED_CODEC.codec();
    }
}

