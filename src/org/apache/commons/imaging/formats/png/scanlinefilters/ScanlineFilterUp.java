package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScanlineFilterUp implements ScanlineFilter {
    @Override // org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        for (int i = 0; i < bArr.length; i++) {
            if (bArr3 != null) {
                bArr2[i] = (byte) ((bArr[i] + bArr3[i]) % 256);
            } else {
                bArr2[i] = bArr[i];
            }
        }
    }
}
