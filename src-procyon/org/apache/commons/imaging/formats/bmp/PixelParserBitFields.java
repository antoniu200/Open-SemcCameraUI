// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.nio.ByteOrder;

class PixelParserBitFields extends PixelParserSimple
{
    private final int alphaMask;
    private final int alphaShift;
    private final int blueMask;
    private final int blueShift;
    private int bytecount;
    private final int greenMask;
    private final int greenShift;
    private final int redMask;
    private final int redShift;
    
    public PixelParserBitFields(final BmpHeaderInfo bmpHeaderInfo, final byte[] array, final byte[] array2) {
        super(bmpHeaderInfo, array, array2);
        this.redMask = bmpHeaderInfo.redMask;
        this.greenMask = bmpHeaderInfo.greenMask;
        this.blueMask = bmpHeaderInfo.blueMask;
        this.alphaMask = bmpHeaderInfo.alphaMask;
        this.redShift = this.getMaskShift(this.redMask);
        this.greenShift = this.getMaskShift(this.greenMask);
        this.blueShift = this.getMaskShift(this.blueMask);
        int maskShift;
        if (this.alphaMask != 0) {
            maskShift = this.getMaskShift(this.alphaMask);
        }
        else {
            maskShift = 0;
        }
        this.alphaShift = maskShift;
    }
    
    private int getMaskShift(int n) {
        final int n2 = 0;
        int n3 = 0;
        int n4;
        int n5;
        while (true) {
            n4 = n2;
            n5 = n;
            if ((0x1 & n) != 0x0) {
                break;
            }
            n = (n >> 1 & Integer.MAX_VALUE);
            ++n3;
        }
        while (0x1 & n5) {
            n5 = (n5 >> 1 & Integer.MAX_VALUE);
            ++n4;
        }
        return n3 - (8 - n4);
    }
    
    @Override
    public int getNextRGB() throws ImageReadException, IOException {
        final int bitsPerPixel = this.bhi.bitsPerPixel;
        int n = 255;
        int n2;
        if (bitsPerPixel == 8) {
            n2 = (this.imageData[this.bytecount + 0] & 0xFF);
            ++this.bytecount;
        }
        else if (this.bhi.bitsPerPixel == 24) {
            n2 = BinaryFunctions.read3Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 3;
        }
        else if (this.bhi.bitsPerPixel == 32) {
            n2 = BinaryFunctions.read4Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 4;
        }
        else {
            if (this.bhi.bitsPerPixel != 16) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown BitsPerPixel: ");
                sb.append(this.bhi.bitsPerPixel);
                throw new ImageReadException(sb.toString());
            }
            n2 = BinaryFunctions.read2Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 2;
        }
        final int n3 = this.redMask & n2;
        final int n4 = this.greenMask & n2;
        final int n5 = this.blueMask & n2;
        if (this.alphaMask != 0) {
            n = (this.alphaMask & n2);
        }
        int n6;
        if (this.redShift >= 0) {
            n6 = n3 >> this.redShift;
        }
        else {
            n6 = n3 << -this.redShift;
        }
        int n7;
        if (this.greenShift >= 0) {
            n7 = n4 >> this.greenShift;
        }
        else {
            n7 = n4 << -this.greenShift;
        }
        int n8;
        if (this.blueShift >= 0) {
            n8 = n5 >> this.blueShift;
        }
        else {
            n8 = n5 << -this.blueShift;
        }
        int n9;
        if (this.alphaShift >= 0) {
            n9 = n >> this.alphaShift;
        }
        else {
            n9 = n << -this.alphaShift;
        }
        return n9 << 24 | n6 << 16 | n7 << 8 | n8 << 0;
    }
    
    @Override
    public void newline() throws ImageReadException, IOException {
        while (this.bytecount % 4 != 0) {
            BinaryFunctions.readByte("Pixel", this.is, "BMP Image Data");
            ++this.bytecount;
        }
    }
}
