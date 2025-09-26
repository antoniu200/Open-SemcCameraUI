// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;

public class PngChunkPhys extends PngChunk
{
    public final int pixelsPerUnitXAxis;
    public final int pixelsPerUnitYAxis;
    public final int unitSpecifier;
    
    public PngChunkPhys(final int n, final int n2, final int n3, final byte[] buf) throws IOException {
        super(n, n2, n3, buf);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        this.pixelsPerUnitXAxis = BinaryFunctions.read4Bytes("PixelsPerUnitXAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt", this.getByteOrder());
        this.pixelsPerUnitYAxis = BinaryFunctions.read4Bytes("PixelsPerUnitYAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt", this.getByteOrder());
        this.unitSpecifier = BinaryFunctions.readByte("Unit specifier", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt");
    }
}
