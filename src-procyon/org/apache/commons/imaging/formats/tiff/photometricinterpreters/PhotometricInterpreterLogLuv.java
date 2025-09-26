// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

public class PhotometricInterpreterLogLuv extends PhotometricInterpreter
{
    public PhotometricInterpreterLogLuv(final int n, final int[] array, final int n2, final int n3, final int n4) {
        super(n, array, n2, n3, n4);
    }
    
    private float cube(final float n) {
        return n * n * n;
    }
    
    @Override
    public void interpretPixel(final ImageBuilder imageBuilder, final int[] array, final int n, final int n2) throws ImageReadException, IOException {
        final int n3 = array[0];
        final byte b = (byte)array[1];
        final byte b2 = (byte)array[2];
        final float n4 = (n3 * 100.0f / 255.0f + 16.0f) / 116.0f;
        final float n5 = b / 500.0f + n4;
        final float n6 = n4 - b2 / 200.0f;
        float cube = this.cube(n5);
        float cube2 = this.cube(n4);
        float cube3 = this.cube(n6);
        if (cube2 <= 0.008856f) {
            cube2 = (n4 - 0.13793103f) / 7.787f;
        }
        if (cube <= 0.008856f) {
            cube = (n5 - 0.13793103f) / 7.787f;
        }
        if (cube3 <= 0.008856f) {
            cube3 = (n6 - 0.13793103f) / 7.787f;
        }
        final float n7 = 95.047f * cube / 100.0f;
        final float n8 = cube2 * 100.0f / 100.0f;
        final float n9 = 108.883f * cube3 / 100.0f;
        final float n10 = 3.2406f * n7 + -1.5372f * n8 + -0.4986f * n9;
        final float n11 = -0.9689f * n7 + 1.8758f * n8 + 0.0415f * n9;
        final float n12 = n7 * 0.0557f + n8 * -0.204f + n9 * 1.057f;
        final double a = n10;
        float n13;
        if (a > 0.0031308) {
            n13 = (float)Math.pow(a, 0.4166666666666667) * 1.055f - 0.055f;
        }
        else {
            n13 = n10 * 12.92f;
        }
        final double a2 = n11;
        float n14;
        if (a2 > 0.0031308) {
            n14 = (float)Math.pow(a2, 0.4166666666666667) * 1.055f - 0.055f;
        }
        else {
            n14 = 12.92f * n11;
        }
        final double a3 = n12;
        float n15;
        if (a3 > 0.0031308) {
            n15 = 1.055f * (float)Math.pow(a3, 0.4166666666666667) - 0.055f;
        }
        else {
            n15 = 12.92f * n12;
        }
        imageBuilder.setRGB(n, n2, Math.min(255, Math.max(0, (int)(n13 * 255.0f))) << 16 | 0xFF000000 | Math.min(255, Math.max(0, (int)(n14 * 255.0f))) << 8 | Math.min(255, Math.max(0, (int)(n15 * 255.0f))) << 0);
    }
}
