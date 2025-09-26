// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import org.apache.commons.imaging.ImageReadException;

class BitParser
{
    private final int bitDepth;
    private final int bitsPerPixel;
    private final byte[] bytes;
    
    public BitParser(final byte[] bytes, final int bitsPerPixel, final int bitDepth) {
        this.bytes = bytes;
        this.bitsPerPixel = bitsPerPixel;
        this.bitDepth = bitDepth;
    }
    
    public int getSample(int n, int bitDepth) throws ImageReadException {
        n *= this.bitsPerPixel;
        bitDepth = bitDepth * this.bitDepth + n >> 3;
        if (this.bitDepth == 8) {
            return this.bytes[bitDepth] & 0xFF;
        }
        if (this.bitDepth < 8) {
            final byte b = this.bytes[bitDepth];
            bitDepth = this.bitDepth;
            return (1 << this.bitDepth) - 1 & (b & 0xFF) >> 8 - ((n & 0x7) + bitDepth);
        }
        if (this.bitDepth == 16) {
            n = this.bytes[bitDepth];
            return (this.bytes[bitDepth + 1] & 0xFF) | (n & 0xFF) << 8;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("PNG: bad BitDepth: ");
        sb.append(this.bitDepth);
        throw new ImageReadException(sb.toString());
    }
    
    public int getSampleAsByte(int n, int sample) throws ImageReadException {
        sample = this.getSample(n, sample);
        final int n2 = 8 - this.bitDepth;
        if (n2 > 0) {
            n = sample * 255 / ((1 << this.bitDepth) - 1);
        }
        else {
            n = sample;
            if (n2 < 0) {
                n = sample >> -n2;
            }
        }
        return 0xFF & n;
    }
}
