// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class ScanlineFilterAverage implements ScanlineFilter
{
    private final int bytesPerPixel;
    
    public ScanlineFilterAverage(final int bytesPerPixel) {
        this.bytesPerPixel = bytesPerPixel;
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
            array2[i] = (byte)((array[i] + ((b & 0xFF) + (b2 & 0xFF)) / 2) % 256);
        }
    }
}
