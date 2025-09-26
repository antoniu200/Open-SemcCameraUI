package org.apache.commons.imaging.formats.png.scanlinefilters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ScanlineFilter {
    void unfilter(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException;
}
