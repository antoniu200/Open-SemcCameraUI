package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import android.support.v4.view.ViewCompat;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotometricInterpreterPalette extends PhotometricInterpreter {
    private final int[] indexColorMap;

    public PhotometricInterpreterPalette(int i, int[] iArr, int i2, int i3, int i4, int[] iArr2) {
        super(i, iArr, i2, i3, i4);
        int bitsPerSample = 1 << getBitsPerSample(0);
        this.indexColorMap = new int[bitsPerSample];
        for (int i5 = 0; i5 < bitsPerSample; i5++) {
            int i6 = (iArr2[i5] >> 8) & 255;
            int i7 = (iArr2[i5 + bitsPerSample] >> 8) & 255;
            this.indexColorMap[i5] = (i6 << 16) | ViewCompat.MEASURED_STATE_MASK | (i7 << 8) | ((iArr2[(2 * bitsPerSample) + i5] >> 8) & 255);
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter
    public void interpretPixel(ImageBuilder imageBuilder, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        imageBuilder.setRGB(i, i2, this.indexColorMap[iArr[0]]);
    }
}
