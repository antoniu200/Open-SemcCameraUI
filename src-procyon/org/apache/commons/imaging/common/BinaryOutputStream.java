// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.io.IOException;
import java.nio.ByteOrder;
import java.io.OutputStream;

public class BinaryOutputStream extends OutputStream
{
    private ByteOrder byteOrder;
    private int count;
    private boolean debug;
    private final OutputStream os;
    
    public BinaryOutputStream(final OutputStream os) {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.os = os;
    }
    
    public BinaryOutputStream(final OutputStream os, final ByteOrder byteOrder) {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.byteOrder = byteOrder;
        this.os = os;
    }
    
    @Override
    public void close() throws IOException {
        this.os.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.os.flush();
    }
    
    public int getByteCount() {
        return this.count;
    }
    
    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }
    
    public final boolean getDebug() {
        return this.debug;
    }
    
    protected void setByteOrder(final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }
    
    public final void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.os.write(n);
        ++this.count;
    }
    
    @Override
    public final void write(final byte[] b) throws IOException {
        this.os.write(b, 0, b.length);
        this.count += b.length;
    }
    
    @Override
    public final void write(final byte[] b, final int off, final int len) throws IOException {
        this.os.write(b, off, len);
        this.count += len;
    }
    
    public final void write2Bytes(final int n) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.write(n >> 8 & 0xFF);
            this.write(n & 0xFF);
        }
        else {
            this.write(0xFF & n);
            this.write(n >> 8 & 0xFF);
        }
    }
    
    public final void write3Bytes(final int n) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.write(n >> 16 & 0xFF);
            this.write(n >> 8 & 0xFF);
            this.write(n & 0xFF);
        }
        else {
            this.write(0xFF & n);
            this.write(n >> 8 & 0xFF);
            this.write(n >> 16 & 0xFF);
        }
    }
    
    public final void write4Bytes(final int n) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.write(n >> 24 & 0xFF);
            this.write(n >> 16 & 0xFF);
            this.write(n >> 8 & 0xFF);
            this.write(n & 0xFF);
        }
        else {
            this.write(0xFF & n);
            this.write(n >> 8 & 0xFF);
            this.write(n >> 16 & 0xFF);
            this.write(n >> 24 & 0xFF);
        }
    }
}
