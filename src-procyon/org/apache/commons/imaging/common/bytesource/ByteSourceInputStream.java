// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.bytesource;

import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class ByteSourceInputStream extends ByteSource
{
    private static final int BLOCK_SIZE = 1024;
    private CacheBlock cacheHead;
    private final InputStream is;
    private byte[] readBuffer;
    private long streamLength;
    
    public ByteSourceInputStream(final InputStream in, final String s) {
        super(s);
        this.streamLength = -1L;
        this.is = new BufferedInputStream(in);
    }
    
    private CacheBlock getFirstBlock() throws IOException {
        if (this.cacheHead == null) {
            this.cacheHead = this.readBlock();
        }
        return this.cacheHead;
    }
    
    private CacheBlock readBlock() throws IOException {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
        }
        final int read = this.is.read(this.readBuffer);
        if (read < 1) {
            return null;
        }
        if (read < 1024) {
            final byte[] array = new byte[read];
            System.arraycopy(this.readBuffer, 0, array, 0, read);
            return new CacheBlock(array);
        }
        final byte[] readBuffer = this.readBuffer;
        this.readBuffer = null;
        return new CacheBlock(readBuffer);
    }
    
    @Override
    public byte[] getAll() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (CacheBlock cacheBlock = this.getFirstBlock(); cacheBlock != null; cacheBlock = cacheBlock.getNext()) {
            byteArrayOutputStream.write(cacheBlock.bytes);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public byte[] getBlock(final long lng, final int i) throws IOException {
        if (lng >= 0L && i >= 0) {
            final long n = i + lng;
            if (n >= 0L) {
                if (n <= this.streamLength) {
                    final InputStream inputStream = this.getInputStream();
                    BinaryFunctions.skipBytes(inputStream, lng);
                    final byte[] b = new byte[i];
                    int off = 0;
                    int read;
                    do {
                        read = inputStream.read(b, off, b.length - off);
                        if (read < 1) {
                            throw new IOException("Could not read block.");
                        }
                    } while ((off += read) < i);
                    return b;
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not read block (block start: ");
        sb.append(lng);
        sb.append(", block length: ");
        sb.append(i);
        sb.append(", data length: ");
        sb.append(this.streamLength);
        sb.append(").");
        throw new IOException(sb.toString());
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Inputstream: '");
        sb.append(this.filename);
        sb.append("'");
        return sb.toString();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new CacheReadingInputStream();
    }
    
    @Override
    public long getLength() throws IOException {
        if (this.streamLength >= 0L) {
            return this.streamLength;
        }
        final InputStream inputStream = this.getInputStream();
        long streamLength = 0L;
        while (true) {
            final long skip = inputStream.skip(1024L);
            if (skip <= 0L) {
                break;
            }
            streamLength += skip;
        }
        return this.streamLength = streamLength;
    }
    
    private class CacheBlock
    {
        public final byte[] bytes;
        private CacheBlock next;
        final ByteSourceInputStream this$0;
        private boolean triedNext;
        
        public CacheBlock(final ByteSourceInputStream this$0, final byte[] bytes) {
            this.this$0 = this$0;
            this.bytes = bytes;
        }
        
        public CacheBlock getNext() throws IOException {
            if (this.next != null) {
                return this.next;
            }
            if (this.triedNext) {
                return null;
            }
            this.triedNext = true;
            return this.next = this.this$0.readBlock();
        }
    }
    
    private class CacheReadingInputStream extends InputStream
    {
        private CacheBlock block;
        private int blockIndex;
        private boolean readFirst;
        final ByteSourceInputStream this$0;
        
        private CacheReadingInputStream(final ByteSourceInputStream this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public int read() throws IOException {
            if (this.block == null) {
                if (this.readFirst) {
                    return -1;
                }
                this.block = this.this$0.getFirstBlock();
                this.readFirst = true;
            }
            if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            if (this.block == null) {
                return -1;
            }
            if (this.blockIndex >= this.block.bytes.length) {
                return -1;
            }
            return this.block.bytes[this.blockIndex++] & 0xFF;
        }
        
        @Override
        public int read(final byte[] array, final int n, int min) throws IOException {
            if (array == null) {
                throw new NullPointerException();
            }
            if (n >= 0 && n <= array.length && min >= 0) {
                final int n2 = n + min;
                if (n2 <= array.length) {
                    if (n2 >= 0) {
                        if (min == 0) {
                            return 0;
                        }
                        if (this.block == null) {
                            if (this.readFirst) {
                                return -1;
                            }
                            this.block = this.this$0.getFirstBlock();
                            this.readFirst = true;
                        }
                        if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                            this.block = this.block.getNext();
                            this.blockIndex = 0;
                        }
                        if (this.block == null) {
                            return -1;
                        }
                        if (this.blockIndex >= this.block.bytes.length) {
                            return -1;
                        }
                        min = Math.min(min, this.block.bytes.length - this.blockIndex);
                        System.arraycopy(this.block.bytes, this.blockIndex, array, n, min);
                        this.blockIndex += min;
                        return min;
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public long skip(final long n) throws IOException {
            if (n <= 0L) {
                return 0L;
            }
            long b;
            int min;
            for (b = n; b > 0L; b -= min) {
                if (this.block == null) {
                    if (this.readFirst) {
                        return -1L;
                    }
                    this.block = this.this$0.getFirstBlock();
                    this.readFirst = true;
                }
                if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                    this.block = this.block.getNext();
                    this.blockIndex = 0;
                }
                if (this.block == null) {
                    break;
                }
                if (this.blockIndex >= this.block.bytes.length) {
                    break;
                }
                min = Math.min((int)Math.min(1024L, b), this.block.bytes.length - this.blockIndex);
                this.blockIndex += min;
            }
            return n - b;
        }
    }
}
