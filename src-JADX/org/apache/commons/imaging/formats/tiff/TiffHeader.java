package org.apache.commons.imaging.formats.tiff;

import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffHeader extends TiffElement {
    public final ByteOrder byteOrder;
    public final long offsetToFirstIFD;
    public final int tiffVersion;

    @Override // org.apache.commons.imaging.formats.tiff.TiffElement
    public String getElementDescription(boolean z) {
        if (z) {
            return null;
        }
        return "TIFF Header";
    }

    public TiffHeader(ByteOrder byteOrder, int i, long j) {
        super(0L, 8);
        this.byteOrder = byteOrder;
        this.tiffVersion = i;
        this.offsetToFirstIFD = j;
    }
}
