// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class ScanlineFilterPaeth implements ScanlineFilter
{
    private final int bytesPerPixel;
    
    public ScanlineFilterPaeth(final int bytesPerPixel) {
        this.bytesPerPixel = bytesPerPixel;
    }
    
    private int paethPredictor(final int n, final int n2, final int n3) {
        final int n4 = n + n2 - n3;
        final int abs = Math.abs(n4 - n);
        final int abs2 = Math.abs(n4 - n2);
        final int abs3 = Math.abs(n4 - n3);
        if (abs <= abs2 && abs <= abs3) {
            return n;
        }
        if (abs2 <= abs3) {
            return n2;
        }
        return n3;
    }
    
    @Override
    public void unfilter(final byte[] array, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
        for (int i = 0; i < array.length; ++i) {
            final int n = i - this.bytesPerPixel;
            byte b;
            if (n >= 0) {
                b = array2[n];
            }
            else {
                b = 0;
            }
            byte b2;
            if (array3 != null) {
                b2 = array3[i];
            }
            else {
                b2 = 0;
            }
            byte b3;
            if (n >= 0 && array3 != null) {
                b3 = array3[n];
            }
            else {
                b3 = 0;
            }
            array2[i] = (byte)((array[i] + this.paethPredictor(b & 0xFF, b2 & 0xFF, b3 & 0xFF)) % 256);
        }
    }
}
