// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.transparencyfilters;

import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;

public class TransparencyFilterGrayscale extends TransparencyFilter
{
    private final int transparentColor;
    
    public TransparencyFilterGrayscale(final byte[] buf) throws IOException {
        super(buf);
        this.transparentColor = BinaryFunctions.read2Bytes("transparentColor", new ByteArrayInputStream(buf), "tRNS: Missing transparentColor", this.getByteOrder());
    }
    
    @Override
    public int filter(final int n, final int n2) throws ImageReadException, IOException {
        if (n2 != this.transparentColor) {
            return n;
        }
        return 0;
    }
}
