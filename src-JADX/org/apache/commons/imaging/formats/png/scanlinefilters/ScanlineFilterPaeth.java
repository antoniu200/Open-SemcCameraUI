package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScanlineFilterPaeth implements ScanlineFilter {
    private final int bytesPerPixel;

    public ScanlineFilterPaeth(int i) {
        this.bytesPerPixel = i;
    }

    private int paethPredictor(int i, int i2, int i3) {
        int i4 = (i + i2) - i3;
        int iAbs = Math.abs(i4 - i);
        int iAbs2 = Math.abs(i4 - i2);
        int iAbs3 = Math.abs(i4 - i3);
        return (iAbs > iAbs2 || iAbs > iAbs3) ? iAbs2 <= iAbs3 ? i2 : i3 : i;
    }

    @Override // org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i - this.bytesPerPixel;
            bArr2[i] = (byte) ((bArr[i] + paethPredictor((i2 >= 0 ? bArr2[i2] : (byte) 0) & 255, (bArr3 != null ? bArr3[i] : (byte) 0) & 255, ((i2 < 0 || bArr3 == null) ? (byte) 0 : bArr3[i2]) & 255)) % 256);
        }
    }
}
