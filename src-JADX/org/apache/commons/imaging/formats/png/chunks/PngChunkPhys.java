package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkPhys extends PngChunk {
    public final int pixelsPerUnitXAxis;
    public final int pixelsPerUnitYAxis;
    public final int unitSpecifier;

    public PngChunkPhys(int i, int i2, int i3, byte[] bArr) throws IOException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        this.pixelsPerUnitXAxis = BinaryFunctions.read4Bytes("PixelsPerUnitXAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt", getByteOrder());
        this.pixelsPerUnitYAxis = BinaryFunctions.read4Bytes("PixelsPerUnitYAxis", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt", getByteOrder());
        this.unitSpecifier = BinaryFunctions.readByte("Unit specifier", byteArrayInputStream, "Not a Valid Png File: pHYs Corrupt");
    }
}
