package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.color.ColorConversions;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotometricInterpreterCmyk extends PhotometricInterpreter {
    public PhotometricInterpreterCmyk(int i, int[] iArr, int i2, int i3, int i4) {
        super(i, iArr, i2, i3, i4);
    }

    @Override // org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter
    public void interpretPixel(ImageBuilder imageBuilder, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        imageBuilder.setRGB(i, i2, ColorConversions.convertCMYKtoRGB(iArr[0], iArr[1], iArr[2], iArr[3]));
    }
}
