package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScanlineFilterNone implements ScanlineFilter {
    @Override // org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter
    public void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
    }
}
