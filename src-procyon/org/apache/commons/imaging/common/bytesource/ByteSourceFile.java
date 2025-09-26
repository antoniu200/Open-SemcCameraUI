// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.bytesource;

import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.RandomAccessFile;
import java.io.IOException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ByteSourceFile extends ByteSource
{
    private final File file;
    
    public ByteSourceFile(final File file) {
        super(file.getName());
        this.file = file;
    }
    
    @Override
    public byte[] getAll() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Closeable closeable = null;
        Closeable closeable2;
        try {
            closeable = closeable;
            final FileInputStream in = new FileInputStream(this.file);
            try {
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = bufferedInputStream.read(array);
                    if (read <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                }
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                IoUtils.closeQuietly(true, bufferedInputStream);
                return byteArray;
            }
            finally {}
        }
        finally {
            closeable2 = closeable;
        }
        IoUtils.closeQuietly(false, closeable2);
    }
    
    @Override
    public byte[] getBlock(final long lng, final int i) throws IOException {
        Closeable closeable = null;
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
            Label_0080: {
                if (lng < 0L || i < 0) {
                    break Label_0080;
                }
                final long n = i + lng;
                if (n < 0L) {
                    break Label_0080;
                }
                try {
                    if (n > randomAccessFile.length()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Could not read block (block start: ");
                        sb.append(lng);
                        sb.append(", block length: ");
                        sb.append(i);
                        sb.append(", data length: ");
                        sb.append(randomAccessFile.length());
                        sb.append(").");
                        throw new IOException(sb.toString());
                    }
                    final byte[] rafBytes = BinaryFunctions.getRAFBytes(randomAccessFile, lng, i, "Could not read value from file");
                    IoUtils.closeQuietly(true, randomAccessFile);
                    return rafBytes;
                }
                finally {}
            }
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("File: '");
        sb.append(this.file.getAbsolutePath());
        sb.append("'");
        return sb.toString();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }
    
    @Override
    public long getLength() {
        return this.file.length();
    }
}
