// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

public interface ScanlineFilter
{
    void unfilter(final byte[] p0, final byte[] p1, final byte[] p2) throws ImageReadException, IOException;
}
