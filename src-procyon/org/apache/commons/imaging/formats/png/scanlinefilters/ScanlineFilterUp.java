// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class ScanlineFilterUp implements ScanlineFilter
{
    @Override
    public void unfilter(final byte[] array, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
        for (int i = 0; i < array.length; ++i) {
            if (array3 != null) {
                array2[i] = (byte)((array[i] + array3[i]) % 256);
            }
            else {
                array2[i] = array[i];
            }
        }
    }
}
