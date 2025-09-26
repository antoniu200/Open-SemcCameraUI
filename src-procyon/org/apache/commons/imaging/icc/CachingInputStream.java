// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class CachingInputStream extends InputStream
{
    private final ByteArrayOutputStream baos;
    private final InputStream is;
    
    public CachingInputStream(final InputStream is) {
        this.baos = new ByteArrayOutputStream();
        this.is = is;
    }
    
    @Override
    public int available() throws IOException {
        return this.is.available();
    }
    
    @Override
    public void close() throws IOException {
        this.is.close();
    }
    
    public byte[] getCache() {
        return this.baos.toByteArray();
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.is.read();
        this.baos.write(read);
        return read;
    }
}
