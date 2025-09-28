package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AppnSegment extends GenericSegment {
    public AppnSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2, inputStream);
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        return "APPN (APP" + (this.marker - 65504) + ") (" + getSegmentType() + ")";
    }
}
