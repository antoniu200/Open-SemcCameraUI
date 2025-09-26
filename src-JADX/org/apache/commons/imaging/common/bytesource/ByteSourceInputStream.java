package org.apache.commons.imaging.common.bytesource;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ByteSourceInputStream extends ByteSource {
    private static final int BLOCK_SIZE = 1024;
    private CacheBlock cacheHead;
    private final InputStream is;
    private byte[] readBuffer;
    private long streamLength;

    public ByteSourceInputStream(InputStream inputStream, String str) {
        super(str);
        this.streamLength = -1L;
        this.is = new BufferedInputStream(inputStream);
    }

    private class CacheBlock {
        public final byte[] bytes;
        private CacheBlock next;
        private boolean triedNext;

        public CacheBlock(byte[] bArr) {
            this.bytes = bArr;
        }

        public CacheBlock getNext() throws IOException {
            if (this.next != null) {
                return this.next;
            }
            if (this.triedNext) {
                return null;
            }
            this.triedNext = true;
            this.next = ByteSourceInputStream.this.readBlock();
            return this.next;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CacheBlock readBlock() throws IOException {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
        }
        int i = this.is.read(this.readBuffer);
        if (i < 1) {
            return null;
        }
        if (i < 1024) {
            byte[] bArr = new byte[i];
            System.arraycopy(this.readBuffer, 0, bArr, 0, i);
            return new CacheBlock(bArr);
        }
        byte[] bArr2 = this.readBuffer;
        this.readBuffer = null;
        return new CacheBlock(bArr2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CacheBlock getFirstBlock() throws IOException {
        if (this.cacheHead == null) {
            this.cacheHead = readBlock();
        }
        return this.cacheHead;
    }

    private class CacheReadingInputStream extends InputStream {
        private CacheBlock block;
        private int blockIndex;
        private boolean readFirst;

        private CacheReadingInputStream() {
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.block == null) {
                if (this.readFirst) {
                    return -1;
                }
                this.block = ByteSourceInputStream.this.getFirstBlock();
                this.readFirst = true;
            }
            if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            if (this.block == null || this.blockIndex >= this.block.bytes.length) {
                return -1;
            }
            byte[] bArr = this.block.bytes;
            int i = this.blockIndex;
            this.blockIndex = i + 1;
            return bArr[i] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int i3;
            if (bArr == null) {
                throw new NullPointerException();
            }
            if (i < 0 || i > bArr.length || i2 < 0 || (i3 = i + i2) > bArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == 0) {
                return 0;
            }
            if (this.block == null) {
                if (this.readFirst) {
                    return -1;
                }
                this.block = ByteSourceInputStream.this.getFirstBlock();
                this.readFirst = true;
            }
            if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            if (this.block == null || this.blockIndex >= this.block.bytes.length) {
                return -1;
            }
            int iMin = Math.min(i2, this.block.bytes.length - this.blockIndex);
            System.arraycopy(this.block.bytes, this.blockIndex, bArr, i, iMin);
            this.blockIndex += iMin;
            return iMin;
        }

        @Override // java.io.InputStream
        public long skip(long j) throws IOException {
            if (j <= 0) {
                return 0L;
            }
            long j2 = j;
            while (j2 > 0) {
                if (this.block == null) {
                    if (this.readFirst) {
                        return -1L;
                    }
                    this.block = ByteSourceInputStream.this.getFirstBlock();
                    this.readFirst = true;
                }
                if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                    this.block = this.block.getNext();
                    this.blockIndex = 0;
                }
                if (this.block == null || this.blockIndex >= this.block.bytes.length) {
                    break;
                }
                int iMin = Math.min((int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID, j2), this.block.bytes.length - this.blockIndex);
                this.blockIndex += iMin;
                j2 -= iMin;
            }
            return j - j2;
        }
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public InputStream getInputStream() throws IOException {
        return new CacheReadingInputStream();
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public byte[] getBlock(long j, int i) throws IOException {
        if (j >= 0 && i >= 0) {
            long j2 = i + j;
            if (j2 >= 0 && j2 <= this.streamLength) {
                InputStream inputStream = getInputStream();
                BinaryFunctions.skipBytes(inputStream, j);
                byte[] bArr = new byte[i];
                int i2 = 0;
                do {
                    int i3 = inputStream.read(bArr, i2, bArr.length - i2);
                    if (i3 < 1) {
                        throw new IOException("Could not read block.");
                    }
                    i2 += i3;
                } while (i2 < i);
                return bArr;
            }
        }
        throw new IOException("Could not read block (block start: " + j + ", block length: " + i + ", data length: " + this.streamLength + ").");
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public long getLength() throws IOException {
        if (this.streamLength >= 0) {
            return this.streamLength;
        }
        InputStream inputStream = getInputStream();
        long j = 0;
        while (true) {
            long jSkip = inputStream.skip(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            if (jSkip <= 0) {
                this.streamLength = j;
                return j;
            }
            j += jSkip;
        }
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public byte[] getAll() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (CacheBlock firstBlock = getFirstBlock(); firstBlock != null; firstBlock = firstBlock.getNext()) {
            byteArrayOutputStream.write(firstBlock.bytes);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public String getDescription() {
        return "Inputstream: '" + this.filename + "'";
    }
}
