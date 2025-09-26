// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import org.apache.commons.imaging.ImageReadException;
import java.nio.ByteOrder;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class BinaryFunctions
{
    private BinaryFunctions() {
    }
    
    public static int charsToQuad(final char c, final char c2, final char c3, final char c4) {
        return (c & '\u00ff') << 24 | (c2 & '\u00ff') << 16 | ('\u00ff' & c3) << 8 | ('\u00ff' & c4) << 0;
    }
    
    public static boolean compareBytes(final byte[] array, final int n, final byte[] array2, final int n2, final int n3) {
        if (array.length < n + n3) {
            return false;
        }
        if (array2.length < n2 + n3) {
            return false;
        }
        for (int i = 0; i < n3; ++i) {
            if (array[n + i] != array2[n2 + i]) {
                return false;
            }
        }
        return true;
    }
    
    public static void copyStreamToStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read <= 0) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    public static int findNull(final byte[] array) {
        return findNull(array, 0);
    }
    
    public static int findNull(final byte[] array, int i) {
        while (i < array.length) {
            if (array[i] == 0) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static byte[] getRAFBytes(final RandomAccessFile randomAccessFile, final long pos, final int n, final String message) throws IOException {
        final byte[] b = new byte[n];
        randomAccessFile.seek(pos);
        int read;
        for (int i = 0; i < n; i += read) {
            read = randomAccessFile.read(b, i, n - i);
            if (read < 0) {
                throw new IOException(message);
            }
        }
        return b;
    }
    
    public static byte[] getStreamBytes(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStreamToStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] head(final byte[] array, final int n) {
        int length = n;
        if (n > array.length) {
            length = array.length;
        }
        return slice(array, 0, length);
    }
    
    public static void printByteBits(final String str, final byte b) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": '");
        sb.append(Integer.toBinaryString(0xFF & b));
        out.println(sb.toString());
    }
    
    public static void printCharQuad(final PrintWriter printWriter, final String str, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": '");
        sb.append((char)(n >> 24 & 0xFF));
        sb.append((char)(n >> 16 & 0xFF));
        sb.append((char)(n >> 8 & 0xFF));
        sb.append((char)(n >> 0 & 0xFF));
        sb.append("'");
        printWriter.println(sb.toString());
    }
    
    public static void printCharQuad(final String str, final int n) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": '");
        sb.append((char)(n >> 24 & 0xFF));
        sb.append((char)(n >> 16 & 0xFF));
        sb.append((char)(n >> 8 & 0xFF));
        sb.append((char)(n >> 0 & 0xFF));
        sb.append("'");
        out.println(sb.toString());
    }
    
    public static int read2Bytes(final String s, final InputStream inputStream, final String message, final ByteOrder byteOrder) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if ((read | read2) < 0) {
            throw new IOException(message);
        }
        int n;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n = (read << 8 | read2);
        }
        else {
            n = (read | read2 << 8);
        }
        return n;
    }
    
    public static int read3Bytes(final String s, final InputStream inputStream, final String message, final ByteOrder byteOrder) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        if ((read | read2 | read3) < 0) {
            throw new IOException(message);
        }
        int n;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n = (read << 16 | read2 << 8 | read3 << 0);
        }
        else {
            n = (read << 0 | (read3 << 16 | read2 << 8));
        }
        return n;
    }
    
    public static int read4Bytes(final String s, final InputStream inputStream, final String message, final ByteOrder byteOrder) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        final int read4 = inputStream.read();
        if ((read | read2 | read3 | read4) < 0) {
            throw new IOException(message);
        }
        int n;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n = (read << 24 | read2 << 16 | read3 << 8 | read4 << 0);
        }
        else {
            n = (read << 0 | (read4 << 24 | read3 << 16 | read2 << 8));
        }
        return n;
    }
    
    public static void readAndVerifyBytes(final InputStream inputStream, final BinaryConstant binaryConstant, final String s) throws ImageReadException, IOException {
        for (int i = 0; i < binaryConstant.size(); ++i) {
            final int read = inputStream.read();
            final byte b = (byte)(0xFF & read);
            if (read < 0) {
                throw new ImageReadException("Unexpected EOF.");
            }
            if (b != binaryConstant.get(i)) {
                throw new ImageReadException(s);
            }
        }
    }
    
    public static void readAndVerifyBytes(final InputStream inputStream, final byte[] array, final String s) throws ImageReadException, IOException {
        for (final byte b : array) {
            final int read = inputStream.read();
            final byte b2 = (byte)(0xFF & read);
            if (read < 0) {
                throw new ImageReadException("Unexpected EOF.");
            }
            if (b2 != b) {
                throw new ImageReadException(s);
            }
        }
    }
    
    public static byte readByte(final String s, final InputStream inputStream, final String message) throws IOException {
        final int read = inputStream.read();
        if (read < 0) {
            throw new IOException(message);
        }
        return (byte)(read & 0xFF);
    }
    
    public static byte[] readBytes(final InputStream inputStream, final int n) throws IOException {
        return readBytes("", inputStream, n, "Unexpected EOF");
    }
    
    public static byte[] readBytes(final String str, final InputStream inputStream, final int n) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" could not be read.");
        return readBytes(str, inputStream, n, sb.toString());
    }
    
    public static byte[] readBytes(final String s, final InputStream inputStream, final int i, final String str) throws IOException {
        final byte[] b = new byte[i];
        int read;
        for (int j = 0; j < i; j += read) {
            read = inputStream.read(b, j, i - j);
            if (read < 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" count: ");
                sb.append(read);
                sb.append(" read: ");
                sb.append(j);
                sb.append(" length: ");
                sb.append(i);
                throw new IOException(sb.toString());
            }
        }
        return b;
    }
    
    public static byte[] remainingBytes(final String s, final byte[] array, final int n) {
        return slice(array, n, array.length - n);
    }
    
    public static void skipBytes(final InputStream inputStream, final long n) throws IOException {
        skipBytes(inputStream, n, "Couldn't skip bytes");
    }
    
    public static void skipBytes(final InputStream inputStream, final long n, final String str) throws IOException {
        long skip;
        for (long n2 = 0L; n != n2; n2 += skip) {
            skip = inputStream.skip(n - n2);
            if (skip < 1L) {
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" (");
                sb.append(skip);
                sb.append(")");
                throw new IOException(sb.toString());
            }
        }
    }
    
    public static byte[] slice(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    public static boolean startsWith(final byte[] array, final BinaryConstant binaryConstant) {
        if (array != null && array.length >= binaryConstant.size()) {
            for (int i = 0; i < binaryConstant.size(); ++i) {
                if (array[i] != binaryConstant.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean startsWith(final byte[] array, final byte[] array2) {
        if (array2 == null) {
            return false;
        }
        if (array == null) {
            return false;
        }
        if (array2.length > array.length) {
            return false;
        }
        for (int i = 0; i < array2.length; ++i) {
            if (array2[i] != array[i]) {
                return false;
            }
        }
        return true;
    }
}
