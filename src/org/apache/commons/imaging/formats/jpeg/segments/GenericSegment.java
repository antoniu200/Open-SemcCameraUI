package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class GenericSegment extends Segment {
    private final byte[] segmentData;

    public GenericSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2);
        this.segmentData = BinaryFunctions.readBytes("Segment Data", inputStream, i2, "Invalid Segment: insufficient data");
    }

    public GenericSegment(int i, byte[] bArr) {
        super(i, bArr.length);
        this.segmentData = bArr;
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public void dump(PrintWriter printWriter) {
        dump(printWriter, 0);
    }

    public void dump(PrintWriter printWriter, int i) {
        int i2;
        for (int i3 = 0; i3 < 50 && (i2 = i3 + i) < this.segmentData.length; i3++) {
            debugNumber(printWriter, "\t" + i2, this.segmentData[i2], 1);
        }
    }

    public byte[] getSegmentData() {
        return (byte[]) this.segmentData.clone();
    }

    protected byte getSegmentData(int i) {
        return this.segmentData[i];
    }

    public String getSegmentDataAsString(String str) throws UnsupportedEncodingException {
        return new String(this.segmentData, str);
    }
}
