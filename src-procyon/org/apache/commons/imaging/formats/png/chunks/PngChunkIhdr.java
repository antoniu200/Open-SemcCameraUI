// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.formats.png.PngColorType;
import org.apache.commons.imaging.formats.png.InterlaceMethod;

public class PngChunkIhdr extends PngChunk
{
    public final int bitDepth;
    public final int compressionMethod;
    public final int filterMethod;
    public final int height;
    public final InterlaceMethod interlaceMethod;
    public final PngColorType pngColorType;
    public final int width;
    
    public PngChunkIhdr(int n, final int n2, final int n3, final byte[] buf) throws ImageReadException, IOException {
        super(n, n2, n3, buf);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        this.width = BinaryFunctions.read4Bytes("Width", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt", this.getByteOrder());
        this.height = BinaryFunctions.read4Bytes("Height", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt", this.getByteOrder());
        this.bitDepth = BinaryFunctions.readByte("BitDepth", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        n = BinaryFunctions.readByte("ColorType", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.pngColorType = PngColorType.getColorType(n);
        if (this.pngColorType == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG: unknown color type: ");
            sb.append(n);
            throw new ImageReadException(sb.toString());
        }
        this.compressionMethod = BinaryFunctions.readByte("CompressionMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        this.filterMethod = BinaryFunctions.readByte("FilterMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        n = BinaryFunctions.readByte("InterlaceMethod", byteArrayInputStream, "Not a Valid Png File: IHDR Corrupt");
        if (n < 0 && n >= InterlaceMethod.values().length) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("PNG: unknown interlace method: ");
            sb2.append(n);
            throw new ImageReadException(sb2.toString());
        }
        this.interlaceMethod = InterlaceMethod.values()[n];
    }
}
