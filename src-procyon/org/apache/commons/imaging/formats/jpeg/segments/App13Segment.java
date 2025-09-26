// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;
import java.util.Map;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;

public class App13Segment extends AppnSegment
{
    protected final JpegImageParser parser;
    
    public App13Segment(final JpegImageParser parser, final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2, inputStream);
        this.parser = parser;
    }
    
    public App13Segment(final JpegImageParser jpegImageParser, final int n, final byte[] buf) throws IOException {
        this(jpegImageParser, n, buf.length, new ByteArrayInputStream(buf));
    }
    
    public boolean isPhotoshopJpegSegment() {
        return new IptcParser().isPhotoshopJpegSegment(this.getSegmentData());
    }
    
    public PhotoshopApp13Data parsePhotoshopSegment(final Map<String, Object> map) throws ImageReadException, IOException {
        if (!this.isPhotoshopJpegSegment()) {
            return null;
        }
        return new IptcParser().parsePhotoshopSegment(this.getSegmentData(), map);
    }
}
