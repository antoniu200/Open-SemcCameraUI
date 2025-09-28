package org.apache.commons.imaging.formats.png;

import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BitParser {
    private final int bitDepth;
    private final int bitsPerPixel;
    private final byte[] bytes;

    public BitParser(byte[] bArr, int i, int i2) {
        this.bytes = bArr;
        this.bitsPerPixel = i;
        this.bitDepth = i2;
    }

    public int getSample(int i, int i2) throws ImageReadException {
        int i3 = this.bitsPerPixel * i;
        int i4 = ((i2 * this.bitDepth) + i3) >> 3;
        if (this.bitDepth == 8) {
            return this.bytes[i4] & 255;
        }
        if (this.bitDepth < 8) {
            return ((1 << this.bitDepth) - 1) & ((this.bytes[i4] & 255) >> (8 - ((i3 & 7) + this.bitDepth)));
        }
        if (this.bitDepth == 16) {
            return (this.bytes[i4 + 1] & 255) | ((this.bytes[i4] & 255) << 8);
        }
        throw new ImageReadException("PNG: bad BitDepth: " + this.bitDepth);
    }

    public int getSampleAsByte(int i, int i2) throws ImageReadException {
        int sample = getSample(i, i2);
        int i3 = 8 - this.bitDepth;
        if (i3 > 0) {
            sample = (sample * 255) / ((1 << this.bitDepth) - 1);
        } else if (i3 < 0) {
            sample >>= -i3;
        }
        return 255 & sample;
    }
}
