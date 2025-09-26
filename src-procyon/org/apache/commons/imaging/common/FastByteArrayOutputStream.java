// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.io.IOException;
import java.io.OutputStream;

class FastByteArrayOutputStream extends OutputStream
{
    private final byte[] bytes;
    private int count;
    
    public FastByteArrayOutputStream(final int n) {
        this.bytes = new byte[n];
    }
    
    public int getBytesWritten() {
        return this.count;
    }
    
    public byte[] toByteArray() {
        if (this.count < this.bytes.length) {
            final byte[] array = new byte[this.count];
            System.arraycopy(this.bytes, 0, array, 0, this.count);
            return array;
        }
        return this.bytes;
    }
    
    @Override
    public void write(final int n) throws IOException {
        if (this.count >= this.bytes.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Write exceeded expected length (");
            sb.append(this.count);
            sb.append(", ");
            sb.append(this.bytes.length);
            sb.append(")");
            throw new IOException(sb.toString());
        }
        this.bytes[this.count] = (byte)n;
        ++this.count;
    }
}
