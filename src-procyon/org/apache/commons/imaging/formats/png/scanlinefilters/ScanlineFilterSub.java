// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class ScanlineFilterSub implements ScanlineFilter
{
    private final int bytesPerPixel;
    
    public ScanlineFilterSub(final int bytesPerPixel) {
        this.bytesPerPixel = bytesPerPixel;
    }
    
    @Override
    public void unfilter(final byte[] array, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
        for (int i = 0; i < array.length; ++i) {
            final int n = i - this.bytesPerPixel;
            if (n >= 0) {
                array2[i] = (byte)((array[i] + array2[n]) % 256);
            }
            else {
                array2[i] = array[i];
            }
        }
    }
}
