// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.transparencyfilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class TransparencyFilterIndexedColor extends TransparencyFilter
{
    public TransparencyFilterIndexedColor(final byte[] array) {
        super(array);
    }
    
    @Override
    public int filter(final int n, final int i) throws ImageReadException, IOException {
        final int length = this.getLength();
        if (i >= length) {
            return n;
        }
        if (i >= 0 && i <= length) {
            return (this.getByte(i) & 0xFF) << 24 | (n & 0xFFFFFF);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("TransparencyFilterIndexedColor index: ");
        sb.append(i);
        sb.append(", bytes.length: ");
        sb.append(length);
        throw new ImageReadException(sb.toString());
    }
}
