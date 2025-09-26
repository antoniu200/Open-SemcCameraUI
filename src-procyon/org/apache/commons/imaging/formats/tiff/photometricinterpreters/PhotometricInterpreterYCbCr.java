// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

public class PhotometricInterpreterYCbCr extends PhotometricInterpreter
{
    public PhotometricInterpreterYCbCr(final int n, final int[] array, final int n2, final int n3, final int n4) {
        super(n, array, n2, n3, n4);
    }
    
    public static int convertYCbCrtoRGB(final int n, final int n2, final int n3) {
        final double n4 = 1.164 * (n - 16.0);
        final double n5 = n3 - 128.0;
        final double n6 = n2 - 128.0;
        return limit((int)(1.596 * n5 + n4), 0, 255) << 16 | 0xFF000000 | limit((int)(n4 - 0.813 * n5 - 0.392 * n6), 0, 255) << 8 | limit((int)(n4 + 2.017 * n6), 0, 255) << 0;
    }
    
    public static int limit(final int b, final int a, final int a2) {
        return Math.min(a2, Math.max(a, b));
    }
    
    @Override
    public void interpretPixel(final ImageBuilder imageBuilder, final int[] array, final int n, final int n2) throws ImageReadException, IOException {
        final int n3 = array[0];
        final int n4 = array[1];
        final int n5 = array[2];
        final double n6 = n3;
        final double n7 = n5 - 128.0;
        final double n8 = n4 - 128.0;
        imageBuilder.setRGB(n, n2, limit((int)(1.402 * n7 + n6), 0, 255) << 16 | 0xFF000000 | limit((int)(n6 - 0.34414 * n8 - 0.71414 * n7), 0, 255) << 8 | limit((int)(n6 + 1.772 * n8), 0, 255) << 0);
    }
}
