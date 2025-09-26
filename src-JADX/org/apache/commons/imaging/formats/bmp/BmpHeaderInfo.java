package org.apache.commons.imaging.formats.bmp;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BmpHeaderInfo {
    public final int alphaMask;
    public final int bitmapDataOffset;
    public final int bitmapDataSize;
    public final int bitmapHeaderSize;
    public final int bitsPerPixel;
    public final int blueMask;
    public final ColorSpace colorSpace;
    public final int colorSpaceType;
    public final int colorsImportant;
    public final int colorsUsed;
    public final int compression;
    public final int fileSize;
    public final int gammaBlue;
    public final int gammaGreen;
    public final int gammaRed;
    public final int greenMask;
    public final int hResolution;
    public final int height;
    public final byte identifier1;
    public final byte identifier2;
    public final int intent;
    public final int planes;
    public final int profileData;
    public final int profileSize;
    public final int redMask;
    public final int reserved;
    public final int reservedV5;
    public final int vResolution;
    public final int width;

    static class ColorSpaceCoordinate {
        int x;
        int y;
        int z;

        ColorSpaceCoordinate() {
        }
    }

    static class ColorSpace {
        ColorSpaceCoordinate blue;
        ColorSpaceCoordinate green;
        ColorSpaceCoordinate red;

        ColorSpace() {
        }
    }

    public BmpHeaderInfo(byte b, byte b2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, ColorSpace colorSpace, int i20, int i21, int i22, int i23, int i24, int i25, int i26) {
        this.identifier1 = b;
        this.identifier2 = b2;
        this.fileSize = i;
        this.reserved = i2;
        this.bitmapDataOffset = i3;
        this.bitmapHeaderSize = i4;
        this.width = i5;
        this.height = i6;
        this.planes = i7;
        this.bitsPerPixel = i8;
        this.compression = i9;
        this.bitmapDataSize = i10;
        this.hResolution = i11;
        this.vResolution = i12;
        this.colorsUsed = i13;
        this.colorsImportant = i14;
        this.redMask = i15;
        this.greenMask = i16;
        this.blueMask = i17;
        this.alphaMask = i18;
        this.colorSpaceType = i19;
        this.colorSpace = colorSpace;
        this.gammaRed = i20;
        this.gammaGreen = i21;
        this.gammaBlue = i22;
        this.intent = i23;
        this.profileData = i24;
        this.profileSize = i25;
        this.reservedV5 = i26;
    }
}
