package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import android.support.v4.view.ViewCompat;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotometricInterpreterYCbCr extends PhotometricInterpreter {
    public PhotometricInterpreterYCbCr(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    public static int limit(int i, int i2, int i3) {
        return Math.min(i3, Math.max(i2, i));
    }

    public static int convertYCbCrtoRGB(int i, int i2, int i3) {
        double d = 1.164d * (i - 16.0d);
        double d2 = i3 - 128.0d;
        double d3 = i2 - 128.0d;
        return (limit((int) ((1.596d * d2) + d), 0, 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (limit((int) ((d - (0.813d * d2)) - (0.392d * d3)), 0, 255) << 8) | (limit((int) (d + (2.017d * d3)), 0, 255) << 0);
    }

    @Override // org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter
    public void interpretPixel(ImageBuilder imageBuilder, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        int i3 = iArr[0];
        double d = i3;
        double d2 = iArr[2] - 128.0d;
        double d3 = iArr[1] - 128.0d;
        imageBuilder.setRGB(i, i2, (limit((int) ((1.402d * d2) + d), 0, 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (limit((int) ((d - (0.34414d * d3)) - (0.71414d * d2)), 0, 255) << 8) | (limit((int) (d + (1.772d * d3)), 0, 255) << 0));
    }
}
