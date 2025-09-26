// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.decoder;

final class YCbCrConverter
{
    private static final int[] BLUES;
    private static final int[] GREENS1;
    private static final int[] GREENS2;
    private static final int[] REDS;
    
    static {
        REDS = new int[65536];
        BLUES = new int[65536];
        GREENS1 = new int[65536];
        GREENS2 = new int[131072];
        for (int i = 0; i < 256; ++i) {
            for (int j = 0; j < 256; ++j) {
                int n;
                if ((n = fastRound(1.402f * (j - 128)) + i) < 0) {
                    n = 0;
                }
                int n2;
                if ((n2 = n) > 255) {
                    n2 = 255;
                }
                YCbCrConverter.REDS[j << 8 | i] = n2 << 16;
            }
        }
        for (int k = 0; k < 256; ++k) {
            for (int l = 0; l < 256; ++l) {
                int n3;
                if ((n3 = fastRound(1.772f * (l - 128)) + k) < 0) {
                    n3 = 0;
                }
                int n4;
                if ((n4 = n3) > 255) {
                    n4 = 255;
                }
                YCbCrConverter.BLUES[l << 8 | k] = n4;
            }
        }
        for (int n5 = 0; n5 < 256; ++n5) {
            for (int n6 = 0; n6 < 256; ++n6) {
                YCbCrConverter.GREENS1[n5 << 8 | n6] = fastRound(0.34414f * (n5 - 128) + 0.71414f * (n6 - 128)) + 135;
            }
        }
        for (int n7 = 0; n7 < 256; ++n7) {
            for (int n8 = 0; n8 < 270; ++n8) {
                final int n9 = n7 - (n8 - 135);
                int n10;
                if (n9 < 0) {
                    n10 = 0;
                }
                else if ((n10 = n9) > 255) {
                    n10 = 255;
                }
                YCbCrConverter.GREENS2[n8 << 8 | n7] = n10 << 8;
            }
        }
    }
    
    private YCbCrConverter() {
    }
    
    public static int convertYCbCrToRGB(final int n, int n2, int n3) {
        final int n4 = YCbCrConverter.REDS[n3 << 8 | n];
        final int[] greens1 = YCbCrConverter.GREENS1;
        n2 <<= 8;
        n3 = greens1[n3 | n2];
        n3 = YCbCrConverter.GREENS2[n3 << 8 | n];
        return YCbCrConverter.BLUES[n | n2] | (n4 | n3);
    }
    
    private static int fastRound(final float n) {
        return (int)(n + 0.5f);
    }
}
