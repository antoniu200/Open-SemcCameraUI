// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;

public class PngChunkGama extends PngChunk
{
    public final int gamma;
    
    public PngChunkGama(final int n, final int n2, final int n3, final byte[] buf) throws IOException {
        super(n, n2, n3, buf);
        this.gamma = BinaryFunctions.read4Bytes("Gamma", new ByteArrayInputStream(buf), "Not a Valid Png File: gAMA Corrupt", this.getByteOrder());
    }
    
    public double getGamma() {
        return 1.0 / (this.gamma / 100000.0);
    }
}
