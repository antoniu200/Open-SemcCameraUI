package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PixelParserBitFields extends PixelParserSimple {
    private final int alphaMask;
    private final int alphaShift;
    private final int blueMask;
    private final int blueShift;
    private int bytecount;
    private final int greenMask;
    private final int greenShift;
    private final int redMask;
    private final int redShift;

    private int getMaskShift(int i) {
        int i2 = 0;
        int i3 = 0;
        while ((1 & i) == 0) {
            i = (i >> 1) & Integer.MAX_VALUE;
            i3++;
        }
        while ((1 & i) == 1) {
            i = (i >> 1) & Integer.MAX_VALUE;
            i2++;
        }
        return i3 - (8 - i2);
    }

    public PixelParserBitFields(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
        this.redMask = bmpHeaderInfo.redMask;
        this.greenMask = bmpHeaderInfo.greenMask;
        this.blueMask = bmpHeaderInfo.blueMask;
        this.alphaMask = bmpHeaderInfo.alphaMask;
        this.redShift = getMaskShift(this.redMask);
        this.greenShift = getMaskShift(this.greenMask);
        this.blueShift = getMaskShift(this.blueMask);
        this.alphaShift = this.alphaMask != 0 ? getMaskShift(this.alphaMask) : 0;
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParserSimple
    public int getNextRGB() throws IOException, ImageReadException {
        int i;
        if (this.bhi.bitsPerPixel == 8) {
            i = this.imageData[this.bytecount + 0] & 255;
            this.bytecount++;
        } else if (this.bhi.bitsPerPixel == 24) {
            i = BinaryFunctions.read3Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 3;
        } else if (this.bhi.bitsPerPixel == 32) {
            i = BinaryFunctions.read4Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 4;
        } else if (this.bhi.bitsPerPixel == 16) {
            i = BinaryFunctions.read2Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 2;
        } else {
            throw new ImageReadException("Unknown BitsPerPixel: " + this.bhi.bitsPerPixel);
        }
        int i2 = this.redMask & i;
        int i3 = this.greenMask & i;
        int i4 = this.blueMask & i;
        int i5 = this.alphaMask != 0 ? this.alphaMask & i : 255;
        return ((this.alphaShift >= 0 ? i5 >> this.alphaShift : i5 << (-this.alphaShift)) << 24) | ((this.redShift >= 0 ? i2 >> this.redShift : i2 << (-this.redShift)) << 16) | ((this.greenShift >= 0 ? i3 >> this.greenShift : i3 << (-this.greenShift)) << 8) | ((this.blueShift >= 0 ? i4 >> this.blueShift : i4 << (-this.blueShift)) << 0);
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParserSimple
    public void newline() throws IOException, ImageReadException {
        while (this.bytecount % 4 != 0) {
            BinaryFunctions.readByte("Pixel", this.is, "BMP Image Data");
            this.bytecount++;
        }
    }
}
