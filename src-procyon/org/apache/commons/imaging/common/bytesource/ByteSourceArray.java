// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.bytesource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public class ByteSourceArray extends ByteSource
{
    private final byte[] bytes;
    
    public ByteSourceArray(final String s, final byte[] bytes) {
        super(s);
        this.bytes = bytes;
    }
    
    public ByteSourceArray(final byte[] bytes) {
        super(null);
        this.bytes = bytes;
    }
    
    @Override
    public byte[] getAll() throws IOException {
        return this.bytes;
    }
    
    @Override
    public byte[] getBlock(final long n, final int i) throws IOException {
        final int j = (int)n;
        if (j >= 0 && i >= 0) {
            final int n2 = j + i;
            if (n2 >= 0) {
                if (n2 <= this.bytes.length) {
                    final byte[] array = new byte[i];
                    System.arraycopy(this.bytes, j, array, 0, i);
                    return array;
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not read block (block start: ");
        sb.append(j);
        sb.append(", block length: ");
        sb.append(i);
        sb.append(", data length: ");
        sb.append(this.bytes.length);
        sb.append(").");
        throw new IOException(sb.toString());
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.bytes.length);
        sb.append(" byte array");
        return sb.toString();
    }
    
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }
    
    @Override
    public long getLength() {
        return this.bytes.length;
    }
}
