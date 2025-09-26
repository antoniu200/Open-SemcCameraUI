// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.itu_t4;

import java.io.OutputStream;

class BitArrayOutputStream extends OutputStream
{
    private byte[] buffer;
    private int bytesWritten;
    private int cache;
    private int cacheMask;
    
    public BitArrayOutputStream() {
        this.cacheMask = 128;
        this.buffer = new byte[16];
    }
    
    public BitArrayOutputStream(final int n) {
        this.cacheMask = 128;
        this.buffer = new byte[n];
    }
    
    private void writeByte(final int n) {
        if (this.bytesWritten >= this.buffer.length) {
            final byte[] buffer = new byte[this.buffer.length * 2];
            System.arraycopy(this.buffer, 0, buffer, 0, this.bytesWritten);
            this.buffer = buffer;
        }
        this.buffer[this.bytesWritten++] = (byte)n;
    }
    
    @Override
    public void close() {
        this.flush();
    }
    
    @Override
    public void flush() {
        if (this.cacheMask != 128) {
            this.writeByte(this.cache);
            this.cache = 0;
            this.cacheMask = 128;
        }
    }
    
    public int getBitsAvailableInCurrentByte() {
        int i = this.cacheMask;
        int n = 0;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    public int size() {
        return this.bytesWritten;
    }
    
    public byte[] toByteArray() {
        this.flush();
        if (this.bytesWritten == this.buffer.length) {
            return this.buffer;
        }
        final byte[] array = new byte[this.bytesWritten];
        System.arraycopy(this.buffer, 0, array, 0, this.bytesWritten);
        return array;
    }
    
    @Override
    public void write(final int n) {
        this.flush();
        this.writeByte(n);
    }
    
    public void writeBit(final int n) {
        if (n != 0) {
            this.cache |= this.cacheMask;
        }
        this.cacheMask >>>= 1;
        if (this.cacheMask == 0) {
            this.flush();
        }
    }
}
