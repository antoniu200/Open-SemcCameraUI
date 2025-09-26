// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;

class PixelParserRgb extends PixelParserSimple
{
    private int bytecount;
    private int cachedBitCount;
    private int cachedByte;
    
    public PixelParserRgb(final BmpHeaderInfo bmpHeaderInfo, final byte[] array, final byte[] array2) {
        super(bmpHeaderInfo, array, array2);
    }
    
    @Override
    public int getNextRGB() throws ImageReadException, IOException {
        if (this.bhi.bitsPerPixel == 1 || this.bhi.bitsPerPixel == 4) {
            if (this.cachedBitCount < this.bhi.bitsPerPixel) {
                if (this.cachedBitCount != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected leftover bits: ");
                    sb.append(this.cachedBitCount);
                    sb.append("/");
                    sb.append(this.bhi.bitsPerPixel);
                    throw new ImageReadException(sb.toString());
                }
                this.cachedBitCount += 8;
                this.cachedByte = (this.imageData[this.bytecount] & 0xFF);
                ++this.bytecount;
            }
            final int bitsPerPixel = this.bhi.bitsPerPixel;
            final int cachedByte = this.cachedByte;
            final int bitsPerPixel2 = this.bhi.bitsPerPixel;
            this.cachedByte = (this.cachedByte << this.bhi.bitsPerPixel & 0xFF);
            this.cachedBitCount -= this.bhi.bitsPerPixel;
            return this.getColorTableRGB((1 << bitsPerPixel) - 1 & cachedByte >> 8 - bitsPerPixel2);
        }
        if (this.bhi.bitsPerPixel == 8) {
            final int colorTableRGB = this.getColorTableRGB(this.imageData[this.bytecount + 0] & 0xFF);
            ++this.bytecount;
            return colorTableRGB;
        }
        if (this.bhi.bitsPerPixel == 16) {
            final int read2Bytes = BinaryFunctions.read2Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            this.bytecount += 2;
            return (read2Bytes >> 10 & 0x1F) << 3 << 16 | 0xFF000000 | (read2Bytes >> 5 & 0x1F) << 3 << 8 | (read2Bytes >> 0 & 0x1F) << 3 << 0;
        }
        if (this.bhi.bitsPerPixel == 24) {
            final byte b = this.imageData[this.bytecount + 0];
            final byte b2 = this.imageData[this.bytecount + 1];
            final byte b3 = this.imageData[this.bytecount + 2];
            this.bytecount += 3;
            return (b & 0xFF) << 0 | ((b2 & 0xFF) << 8 | ((0xFF & b3) << 16 | 0xFF000000));
        }
        if (this.bhi.bitsPerPixel == 32) {
            final byte b4 = this.imageData[this.bytecount + 0];
            final byte b5 = this.imageData[this.bytecount + 1];
            final byte b6 = this.imageData[this.bytecount + 2];
            this.bytecount += 4;
            return (b4 & 0xFF) << 0 | ((b5 & 0xFF) << 8 | ((0xFF & b6) << 16 | 0xFF000000));
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown BitsPerPixel: ");
        sb2.append(this.bhi.bitsPerPixel);
        throw new ImageReadException(sb2.toString());
    }
    
    @Override
    public void newline() throws ImageReadException, IOException {
        this.cachedBitCount = 0;
        while (this.bytecount % 4 != 0) {
            BinaryFunctions.readByte("Pixel", this.is, "BMP Image Data");
            ++this.bytecount;
        }
    }
}
