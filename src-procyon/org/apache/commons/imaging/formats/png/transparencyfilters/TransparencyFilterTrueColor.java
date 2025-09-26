// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.transparencyfilters;

import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;

public class TransparencyFilterTrueColor extends TransparencyFilter
{
    private final int transparentColor;
    
    public TransparencyFilterTrueColor(final byte[] buf) throws IOException {
        super(buf);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        this.transparentColor = ((BinaryFunctions.read2Bytes("transparentRed", byteArrayInputStream, "tRNS: Missing transparentColor", this.getByteOrder()) & 0xFF) << 16 | (BinaryFunctions.read2Bytes("transparentGreen", byteArrayInputStream, "tRNS: Missing transparentColor", this.getByteOrder()) & 0xFF) << 8 | (BinaryFunctions.read2Bytes("transparentBlue", byteArrayInputStream, "tRNS: Missing transparentColor", this.getByteOrder()) & 0xFF) << 0);
    }
    
    @Override
    public int filter(final int n, final int n2) throws ImageReadException, IOException {
        if ((0xFFFFFF & n) == this.transparentColor) {
            return 0;
        }
        return n;
    }
}
