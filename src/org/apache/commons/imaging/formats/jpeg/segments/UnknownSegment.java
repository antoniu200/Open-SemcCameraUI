package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UnknownSegment extends GenericSegment {
    public UnknownSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2, inputStream);
    }

    public UnknownSegment(int i, byte[] bArr) {
        super(i, bArr);
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        return "Unknown (" + getSegmentType() + ")";
    }
}
