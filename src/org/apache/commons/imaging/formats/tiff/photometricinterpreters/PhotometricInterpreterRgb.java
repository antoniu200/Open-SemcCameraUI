package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import android.support.v4.view.ViewCompat;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotometricInterpreterRgb extends PhotometricInterpreter {
    public PhotometricInterpreterRgb(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    @Override // org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter
    public void interpretPixel(ImageBuilder imageBuilder, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        int i3 = iArr[0];
        int i4 = iArr[1];
        imageBuilder.setRGB(i, i2, (iArr[2] << 0) | (i3 << 16) | ViewCompat.MEASURED_STATE_MASK | (i4 << 8));
    }
}
