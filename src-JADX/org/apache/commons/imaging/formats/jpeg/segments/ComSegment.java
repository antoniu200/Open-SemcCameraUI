package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ComSegment extends GenericSegment {
    public ComSegment(int i, byte[] bArr) {
        super(i, bArr);
    }

    public ComSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2, inputStream);
    }

    public byte[] getComment() {
        return getSegmentData();
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        String segmentDataAsString;
        try {
            segmentDataAsString = getSegmentDataAsString("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            segmentDataAsString = "";
        }
        return "COM (" + segmentDataAsString + ")";
    }
}
