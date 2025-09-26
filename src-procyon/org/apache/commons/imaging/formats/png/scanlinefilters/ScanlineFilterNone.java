// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public class ScanlineFilterNone implements ScanlineFilter
{
    @Override
    public void unfilter(final byte[] array, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
        System.arraycopy(array, 0, array2, 0, array.length);
    }
}
