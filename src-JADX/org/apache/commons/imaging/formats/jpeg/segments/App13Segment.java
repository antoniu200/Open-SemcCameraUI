package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class App13Segment extends AppnSegment {
    protected final JpegImageParser parser;

    public App13Segment(JpegImageParser jpegImageParser, int i, byte[] bArr) throws IOException {
        this(jpegImageParser, i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public App13Segment(JpegImageParser jpegImageParser, int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2, inputStream);
        this.parser = jpegImageParser;
    }

    public boolean isPhotoshopJpegSegment() {
        return new IptcParser().isPhotoshopJpegSegment(getSegmentData());
    }

    public PhotoshopApp13Data parsePhotoshopSegment(Map<String, Object> map) throws IOException, ImageReadException {
        if (isPhotoshopJpegSegment()) {
            return new IptcParser().parsePhotoshopSegment(getSegmentData(), map);
        }
        return null;
    }
}
