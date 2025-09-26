// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.bytesource;

import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.io.IOException;

public abstract class ByteSource
{
    protected final String filename;
    
    public ByteSource(final String filename) {
        this.filename = filename;
    }
    
    public abstract byte[] getAll() throws IOException;
    
    public byte[] getBlock(final int n, final int n2) throws IOException {
        return this.getBlock((long)n & 0xFFFFFFFFL, n2);
    }
    
    public abstract byte[] getBlock(final long p0, final int p1) throws IOException;
    
    public abstract String getDescription();
    
    public final String getFilename() {
        return this.filename;
    }
    
    public abstract InputStream getInputStream() throws IOException;
    
    public final InputStream getInputStream(final long n) throws IOException {
        InputStream inputStream2;
        try {
            final InputStream inputStream = this.getInputStream();
            try {
                BinaryFunctions.skipBytes(inputStream, n);
                return inputStream;
            }
            finally {}
        }
        finally {
            inputStream2 = null;
        }
        if (inputStream2 != null) {
            inputStream2.close();
        }
    }
    
    public abstract long getLength() throws IOException;
}
