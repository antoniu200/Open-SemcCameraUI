// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.icns;

final class Rle24Compression
{
    private Rle24Compression() {
    }
    
    public static byte[] decompress(int n, int n2, final byte[] array) {
        final int n3 = n * n2;
        final byte[] array2 = new byte[4 * n3];
        if (n >= 128 && n2 >= 128) {
            n = 4;
        }
        else {
            n = 0;
        }
        for (int i = 1; i <= 3; ++i) {
            n2 = 0;
            int j = n3;
            while (j > 0) {
                if ((array[n] & 0x80) != 0x0) {
                    final int n4 = (array[n] & 0xFF) - 125;
                    for (int k = 0; k < n4; ++k, ++n2) {
                        array2[n2 * 4 + i] = array[n + 1];
                    }
                    n += 2;
                    j -= n4;
                }
                else {
                    final int n5 = (array[n] & 0xFF) + 1;
                    ++n;
                    for (int l = 0; l < n5; ++l, ++n2, ++n) {
                        array2[n2 * 4 + i] = array[n];
                    }
                    j -= n5;
                }
            }
        }
        return array2;
    }
}
