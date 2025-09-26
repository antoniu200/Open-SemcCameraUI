// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

class BmpHeaderInfo
{
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
    
    public BmpHeaderInfo(final byte identifier1, final byte identifier2, final int fileSize, final int reserved, final int bitmapDataOffset, final int bitmapHeaderSize, final int width, final int height, final int planes, final int bitsPerPixel, final int compression, final int bitmapDataSize, final int hResolution, final int vResolution, final int colorsUsed, final int colorsImportant, final int redMask, final int greenMask, final int blueMask, final int alphaMask, final int colorSpaceType, final ColorSpace colorSpace, final int gammaRed, final int gammaGreen, final int gammaBlue, final int intent, final int profileData, final int profileSize, final int reservedV5) {
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
        this.fileSize = fileSize;
        this.reserved = reserved;
        this.bitmapDataOffset = bitmapDataOffset;
        this.bitmapHeaderSize = bitmapHeaderSize;
        this.width = width;
        this.height = height;
        this.planes = planes;
        this.bitsPerPixel = bitsPerPixel;
        this.compression = compression;
        this.bitmapDataSize = bitmapDataSize;
        this.hResolution = hResolution;
        this.vResolution = vResolution;
        this.colorsUsed = colorsUsed;
        this.colorsImportant = colorsImportant;
        this.redMask = redMask;
        this.greenMask = greenMask;
        this.blueMask = blueMask;
        this.alphaMask = alphaMask;
        this.colorSpaceType = colorSpaceType;
        this.colorSpace = colorSpace;
        this.gammaRed = gammaRed;
        this.gammaGreen = gammaGreen;
        this.gammaBlue = gammaBlue;
        this.intent = intent;
        this.profileData = profileData;
        this.profileSize = profileSize;
        this.reservedV5 = reservedV5;
    }
    
    static class ColorSpace
    {
        ColorSpaceCoordinate blue;
        ColorSpaceCoordinate green;
        ColorSpaceCoordinate red;
    }
    
    static class ColorSpaceCoordinate
    {
        int x;
        int y;
        int z;
    }
}
