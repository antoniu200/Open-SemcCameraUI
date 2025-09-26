// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class App14Segment extends AppnSegment
{
    public static final int ADOBE_COLOR_TRANSFORM_UNKNOWN = 0;
    public static final int ADOBE_COLOR_TRANSFORM_YCCK = 2;
    public static final int ADOBE_COLOR_TRANSFORM_YCbCr = 1;
    private static final byte[] ADOBE_PREFIX;
    
    static {
        byte[] bytes;
        try {
            bytes = "Adobe".getBytes("US-ASCII");
        }
        catch (final UnsupportedEncodingException ex) {
            bytes = null;
        }
        ADOBE_PREFIX = bytes;
    }
    
    public App14Segment(final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2, inputStream);
    }
    
    public App14Segment(final int n, final byte[] buf) throws IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    public int getAdobeColorTransform() {
        return this.getSegmentData(11) & 0xFF;
    }
    
    public boolean isAdobeJpegSegment() {
        return BinaryFunctions.startsWith(this.getSegmentData(), App14Segment.ADOBE_PREFIX);
    }
}
