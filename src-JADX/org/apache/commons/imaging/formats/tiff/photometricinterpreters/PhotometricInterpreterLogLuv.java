package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import android.support.v4.view.ViewCompat;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotometricInterpreterLogLuv extends PhotometricInterpreter {
    private float cube(float f) {
        return f * f * f;
    }

    public PhotometricInterpreterLogLuv(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    @Override // org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter
    public void interpretPixel(ImageBuilder imageBuilder, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        float f = (((iArr[0] * 100.0f) / 255.0f) + 16.0f) / 116.0f;
        float f2 = (((byte) iArr[1]) / 500.0f) + f;
        float f3 = f - (((byte) iArr[2]) / 200.0f);
        float fCube = cube(f2);
        float fCube2 = cube(f);
        float fCube3 = cube(f3);
        if (fCube2 <= 0.008856f) {
            fCube2 = (f - 0.13793103f) / 7.787f;
        }
        if (fCube <= 0.008856f) {
            fCube = (f2 - 0.13793103f) / 7.787f;
        }
        if (fCube3 <= 0.008856f) {
            fCube3 = (f3 - 0.13793103f) / 7.787f;
        }
        float f4 = (95.047f * fCube) / 100.0f;
        float f5 = (fCube2 * 100.0f) / 100.0f;
        float f6 = (108.883f * fCube3) / 100.0f;
        float f7 = (3.2406f * f4) + ((-1.5372f) * f5) + ((-0.4986f) * f6);
        float f8 = ((-0.9689f) * f4) + (1.8758f * f5) + (0.0415f * f6);
        float f9 = (f4 * 0.0557f) + (f5 * (-0.204f)) + (f6 * 1.057f);
        double d = f7;
        float fPow = d > 0.0031308d ? (((float) Math.pow(d, 0.4166666666666667d)) * 1.055f) - 0.055f : f7 * 12.92f;
        double d2 = f8;
        float fPow2 = d2 > 0.0031308d ? (((float) Math.pow(d2, 0.4166666666666667d)) * 1.055f) - 0.055f : 12.92f * f8;
        double d3 = f9;
        imageBuilder.setRGB(i, i2, (Math.min(255, Math.max(0, (int) (fPow * 255.0f))) << 16) | ViewCompat.MEASURED_STATE_MASK | (Math.min(255, Math.max(0, (int) (fPow2 * 255.0f))) << 8) | (Math.min(255, Math.max(0, (int) ((d3 > 0.0031308d ? (1.055f * ((float) Math.pow(d3, 0.4166666666666667d))) - 0.055f : 12.92f * f9) * 255.0f))) << 0));
    }
}
