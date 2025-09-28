package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScanlineFilterSub implements ScanlineFilter {
    private final int bytesPerPixel;

    public ScanlineFilterSub(int i) {
        this.bytesPerPixel = i;
    }

    @Override // org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i - this.bytesPerPixel;
            if (i2 >= 0) {
                bArr2[i] = (byte) ((bArr[i] + bArr2[i2]) % 256);
            } else {
                bArr2[i] = bArr[i];
            }
        }
    }
}
