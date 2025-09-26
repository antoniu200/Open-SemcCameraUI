// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import org.apache.commons.imaging.ImageReadException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;

public class PackBits
{
    private int findNextDuplicate(final byte[] array, int i) {
        if (i >= array.length) {
            return -1;
        }
        byte b = array[i];
        ++i;
        while (i < array.length) {
            final byte b2 = array[i];
            if (b2 == b) {
                return i - 1;
            }
            ++i;
            b = b2;
        }
        return -1;
    }
    
    private int findRunLength(final byte[] array, final int n) {
        byte b;
        int n2;
        for (b = array[n], n2 = n + 1; n2 < array.length && array[n2] == b; ++n2) {}
        return n2 - n;
    }
    
    public byte[] compress(byte[] byteArray) throws IOException {
        Closeable closeable;
        try {
            final FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream(byteArray.length * 2);
            int i = 0;
            try {
                while (i < byteArray.length) {
                    final int nextDuplicate = this.findNextDuplicate(byteArray, i);
                    if (nextDuplicate == i) {
                        final int min = Math.min(this.findRunLength(byteArray, nextDuplicate), 128);
                        fastByteArrayOutputStream.write(-(min - 1));
                        fastByteArrayOutputStream.write(byteArray[i]);
                        i += min;
                    }
                    else {
                        final int n = nextDuplicate - i;
                        int n2 = nextDuplicate;
                        int a = n;
                        if (nextDuplicate > 0) {
                            final int runLength = this.findRunLength(byteArray, nextDuplicate);
                            n2 = nextDuplicate;
                            a = n;
                            if (runLength < 3) {
                                final int n3 = i + n + runLength;
                                final int nextDuplicate2 = this.findNextDuplicate(byteArray, n3);
                                n2 = nextDuplicate;
                                a = n;
                                if (nextDuplicate2 != n3) {
                                    a = nextDuplicate2 - i;
                                    n2 = nextDuplicate2;
                                }
                            }
                        }
                        if (n2 < 0) {
                            a = byteArray.length - i;
                        }
                        final int min2 = Math.min(a, 128);
                        fastByteArrayOutputStream.write(min2 - 1);
                        for (int j = 0; j < min2; ++j) {
                            fastByteArrayOutputStream.write(byteArray[i]);
                            ++i;
                        }
                    }
                }
                byteArray = fastByteArrayOutputStream.toByteArray();
                IoUtils.closeQuietly(true, fastByteArrayOutputStream);
                return byteArray;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    public byte[] decompress(final byte[] array, final int i) throws ImageReadException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int j = 0;
        int k = 0;
        while (j < i) {
            if (k >= array.length) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Tiff: Unpack bits source exhausted: ");
                sb.append(k);
                sb.append(", done + ");
                sb.append(j);
                sb.append(", expected + ");
                sb.append(i);
                throw new ImageReadException(sb.toString());
            }
            int n = k + 1;
            final byte l = array[k];
            int n5;
            int n6;
            if (l >= 0 && l <= 127) {
                final int n2 = l + 1;
                final int n3 = j + n2;
                int n4 = 0;
                while (true) {
                    n5 = n3;
                    n6 = n;
                    if (n4 >= n2) {
                        break;
                    }
                    byteArrayOutputStream.write(array[n]);
                    ++n4;
                    ++n;
                }
            }
            else if (l >= -127 && l <= -1) {
                final int n7 = n + 1;
                final byte b = array[n];
                final int n8 = -l + 1;
                final int n9 = j + n8;
                int n10 = 0;
                while (true) {
                    n5 = n9;
                    n6 = n7;
                    if (n10 >= n8) {
                        break;
                    }
                    byteArrayOutputStream.write(b);
                    ++n10;
                }
            }
            else {
                if (l == -128) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Packbits: ");
                    sb2.append(l);
                    throw new ImageReadException(sb2.toString());
                }
                k = n;
                continue;
            }
            final int n11 = n6;
            j = n5;
            k = n11;
        }
        return byteArrayOutputStream.toByteArray();
    }
}
