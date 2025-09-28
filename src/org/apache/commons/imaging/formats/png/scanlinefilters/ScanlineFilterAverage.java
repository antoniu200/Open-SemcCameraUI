package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScanlineFilterAverage implements ScanlineFilter {
    private final int bytesPerPixel;

    public ScanlineFilterAverage(int i) {
        this.bytesPerPixel = i;
    }

    @Override // org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i - this.bytesPerPixel;
            bArr2[i] = (byte) ((bArr[i] + ((((i2 >= 0 ? bArr2[i2] : (byte) 0) & 255) + ((bArr3 != null ? bArr3[i] : (byte) 0) & 255)) / 2)) % 256);
        }
    }
}
