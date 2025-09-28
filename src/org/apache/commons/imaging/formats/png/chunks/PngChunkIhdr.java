package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.png.InterlaceMethod;
import org.apache.commons.imaging.formats.png.PngColorType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkIhdr extends PngChunk {
    public final int bitDepth;
    public final int compressionMethod;
    public final int filterMethod;
    public final int height;
    public final InterlaceMethod interlaceMethod;
    public final PngColorType pngColorType;
    public final int width;

    public PngChunkIhdr(int i, int i2, int i3, byte[] bArr) throws IOException, ImageReadException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.width = BinaryFunctions.read4Bytes("Width", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt", getByteOrder());
        this.height = BinaryFunctions.read4Bytes("Height", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt", getByteOrder());
        this.bitDepth = BinaryFunctions.readByte("BitDepth", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        byte b = BinaryFunctions.readByte("ColorType", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.pngColorType = PngColorType.getColorType(b);
        if (this.pngColorType == null) {
            throw new ImageReadException("PNG: unknown color type: " + ((int) b));
        }
        this.compressionMethod = BinaryFunctions.readByte("CompressionMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.filterMethod = BinaryFunctions.readByte("FilterMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        byte b2 = BinaryFunctions.readByte("InterlaceMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        if (b2 < 0 && b2 >= InterlaceMethod.values().length) {
            throw new ImageReadException("PNG: unknown interlace method: " + ((int) b2));
        }
        this.interlaceMethod = InterlaceMethod.values()[b2];
    }
}
