package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class App14Segment extends AppnSegment {
    public static final int ADOBE_COLOR_TRANSFORM_UNKNOWN = 0;
    public static final int ADOBE_COLOR_TRANSFORM_YCCK = 2;
    public static final int ADOBE_COLOR_TRANSFORM_YCbCr = 1;
    private static final byte[] ADOBE_PREFIX;

    static {
        byte[] bytes;
        try {
            bytes = "Adobe".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException unused) {
            bytes = null;
        }
        ADOBE_PREFIX = bytes;
    }

    public App14Segment(int i, byte[] bArr) throws IOException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public App14Segment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2, inputStream);
    }

    public boolean isAdobeJpegSegment() {
        return BinaryFunctions.startsWith(getSegmentData(), ADOBE_PREFIX);
    }

    public int getAdobeColorTransform() {
        return getSegmentData(11) & 255;
    }
}
