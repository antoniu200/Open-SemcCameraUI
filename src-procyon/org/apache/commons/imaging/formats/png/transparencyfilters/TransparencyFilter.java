// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.transparencyfilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFileParser;

public abstract class TransparencyFilter extends BinaryFileParser
{
    private final byte[] bytes;
    
    public TransparencyFilter(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    public abstract int filter(final int p0, final int p1) throws ImageReadException, IOException;
    
    public byte getByte(final int n) {
        return this.bytes[n];
    }
    
    public int getLength() {
        return this.bytes.length;
    }
}
