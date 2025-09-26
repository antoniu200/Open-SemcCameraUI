package org.apache.commons.imaging.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class BinaryFunctions {
    public static int charsToQuad(char c, char c2, char c3, char c4) {
        return ((c & 255) << 24) | ((c2 & 255) << 16) | ((255 & c3) << 8) | ((255 & c4) << 0);
    }

    private BinaryFunctions() {
    }

    public static boolean startsWith(byte[] bArr, byte[] bArr2) {
        if (bArr2 == null || bArr == null || bArr2.length > bArr.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr2[i] != bArr[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWith(byte[] bArr, BinaryConstant binaryConstant) {
        if (bArr == null || bArr.length < binaryConstant.size()) {
            return false;
        }
        for (int i = 0; i < binaryConstant.size(); i++) {
            if (bArr[i] != binaryConstant.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static byte readByte(String str, InputStream inputStream, String str2) throws IOException {
        int i = inputStream.read();
        if (i < 0) {
            throw new IOException(str2);
        }
        return (byte) (i & 255);
    }

    public static byte[] readBytes(String str, InputStream inputStream, int i) throws IOException {
        return readBytes(str, inputStream, i, str + " could not be read.");
    }

    public static byte[] readBytes(String str, InputStream inputStream, int i, String str2) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int i3 = inputStream.read(bArr, i2, i - i2);
            if (i3 < 0) {
                throw new IOException(str2 + " count: " + i3 + " read: " + i2 + " length: " + i);
            }
            i2 += i3;
        }
        return bArr;
    }

    public static byte[] readBytes(InputStream inputStream, int i) throws IOException {
        return readBytes("", inputStream, i, "Unexpected EOF");
    }

    public static void readAndVerifyBytes(InputStream inputStream, byte[] bArr, String str) throws IOException, ImageReadException {
        for (byte b : bArr) {
            int i = inputStream.read();
            byte b2 = (byte) (255 & i);
            if (i < 0) {
                throw new ImageReadException("Unexpected EOF.");
            }
            if (b2 != b) {
                throw new ImageReadException(str);
            }
        }
    }

    public static void readAndVerifyBytes(InputStream inputStream, BinaryConstant binaryConstant, String str) throws IOException, ImageReadException {
        for (int i = 0; i < binaryConstant.size(); i++) {
            int i2 = inputStream.read();
            byte b = (byte) (255 & i2);
            if (i2 < 0) {
                throw new ImageReadException("Unexpected EOF.");
            }
            if (b != binaryConstant.get(i)) {
                throw new ImageReadException(str);
            }
        }
    }

    public static void skipBytes(InputStream inputStream, long j, String str) throws IOException {
        long j2 = 0;
        while (j != j2) {
            long jSkip = inputStream.skip(j - j2);
            if (jSkip < 1) {
                throw new IOException(str + " (" + jSkip + ")");
            }
            j2 += jSkip;
        }
    }

    public static byte[] remainingBytes(String str, byte[] bArr, int i) {
        return slice(bArr, i, bArr.length - i);
    }

    public static byte[] slice(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public static byte[] head(byte[] bArr, int i) {
        if (i > bArr.length) {
            i = bArr.length;
        }
        return slice(bArr, 0, i);
    }

    public static boolean compareBytes(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static int read4Bytes(String str, InputStream inputStream, String str2, ByteOrder byteOrder) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        int i4 = inputStream.read();
        if ((i | i2 | i3 | i4) < 0) {
            throw new IOException(str2);
        }
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return (i << 24) | (i2 << 16) | (i3 << 8) | (i4 << 0);
        }
        return (i << 0) | (i4 << 24) | (i3 << 16) | (i2 << 8);
    }

    public static int read3Bytes(String str, InputStream inputStream, String str2, ByteOrder byteOrder) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        if ((i | i2 | i3) < 0) {
            throw new IOException(str2);
        }
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return (i << 16) | (i2 << 8) | (i3 << 0);
        }
        return (i << 0) | (i3 << 16) | (i2 << 8);
    }

    public static int read2Bytes(String str, InputStream inputStream, String str2, ByteOrder byteOrder) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        if ((i | i2) < 0) {
            throw new IOException(str2);
        }
        return byteOrder == ByteOrder.BIG_ENDIAN ? (i << 8) | i2 : i | (i2 << 8);
    }

    public static void printCharQuad(String str, int i) {
        System.out.println(str + ": '" + ((char) ((i >> 24) & 255)) + ((char) ((i >> 16) & 255)) + ((char) ((i >> 8) & 255)) + ((char) ((i >> 0) & 255)) + "'");
    }

    public static void printCharQuad(PrintWriter printWriter, String str, int i) {
        printWriter.println(str + ": '" + ((char) ((i >> 24) & 255)) + ((char) ((i >> 16) & 255)) + ((char) ((i >> 8) & 255)) + ((char) ((i >> 0) & 255)) + "'");
    }

    public static void printByteBits(String str, byte b) {
        System.out.println(str + ": '" + Integer.toBinaryString(255 & b));
    }

    public static int findNull(byte[] bArr) {
        return findNull(bArr, 0);
    }

    public static int findNull(byte[] bArr, int i) {
        while (i < bArr.length) {
            if (bArr[i] == 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static byte[] getRAFBytes(RandomAccessFile randomAccessFile, long j, int i, String str) throws IOException {
        byte[] bArr = new byte[i];
        randomAccessFile.seek(j);
        int i2 = 0;
        while (i2 < i) {
            int i3 = randomAccessFile.read(bArr, i2, i - i2);
            if (i3 < 0) {
                throw new IOException(str);
            }
            i2 += i3;
        }
        return bArr;
    }

    public static void skipBytes(InputStream inputStream, long j) throws IOException {
        skipBytes(inputStream, j, "Couldn't skip bytes");
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int i = inputStream.read(bArr);
            if (i <= 0) {
                return;
            } else {
                outputStream.write(bArr, 0, i);
            }
        }
    }

    public static byte[] getStreamBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStreamToStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
