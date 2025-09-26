// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.nio.ByteOrder;

public class TiffHeader extends TiffElement
{
    public final ByteOrder byteOrder;
    public final long offsetToFirstIFD;
    public final int tiffVersion;
    
    public TiffHeader(final ByteOrder byteOrder, final int tiffVersion, final long offsetToFirstIFD) {
        super(0L, 8);
        this.byteOrder = byteOrder;
        this.tiffVersion = tiffVersion;
        this.offsetToFirstIFD = offsetToFirstIFD;
    }
    
    @Override
    public String getElementDescription(final boolean b) {
        if (b) {
            return null;
        }
        return "TIFF Header";
    }
}
