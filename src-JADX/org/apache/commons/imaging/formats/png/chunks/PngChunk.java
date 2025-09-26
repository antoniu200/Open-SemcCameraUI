package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.BinaryFileParser;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunk extends BinaryFileParser {
    public final boolean ancillary;
    private final byte[] bytes;
    public final int chunkType;
    public final int crc;
    public final boolean isPrivate;
    public final int length;
    private final boolean[] propertyBits = new boolean[4];
    public final boolean reserved;
    public final boolean safeToCopy;

    public PngChunk(int i, int i2, int i3, byte[] bArr) {
        this.length = i;
        this.chunkType = i2;
        this.crc = i3;
        this.bytes = bArr;
        int i4 = 24;
        int i5 = 0;
        while (true) {
            boolean z = true;
            if (i5 < 4) {
                int i6 = 255 & (i2 >> i4);
                i4 -= 8;
                boolean[] zArr = this.propertyBits;
                if ((i6 & 32) <= 0) {
                    z = false;
                }
                zArr[i5] = z;
                i5++;
            } else {
                this.ancillary = this.propertyBits[0];
                this.isPrivate = this.propertyBits[1];
                this.reserved = this.propertyBits[2];
                this.safeToCopy = this.propertyBits[3];
                return;
            }
        }
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean[] getPropertyBits() {
        return (boolean[]) this.propertyBits.clone();
    }

    protected ByteArrayInputStream getDataStream() {
        return new ByteArrayInputStream(getBytes());
    }
}
