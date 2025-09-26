// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.io.InputStream;

public class App2Segment extends AppnSegment implements Comparable<App2Segment>
{
    public final int curMarker;
    private final byte[] iccBytes;
    public final int numMarkers;
    
    public App2Segment(final int n, final int n2, final InputStream inputStream) throws ImageReadException, IOException {
        super(n, n2, inputStream);
        if (BinaryFunctions.startsWith(this.getSegmentData(), JpegConstants.ICC_PROFILE_LABEL)) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.getSegmentData());
            BinaryFunctions.readAndVerifyBytes(byteArrayInputStream, JpegConstants.ICC_PROFILE_LABEL, "Not a Valid App2 Segment: missing ICC Profile label");
            this.curMarker = BinaryFunctions.readByte("curMarker", byteArrayInputStream, "Not a valid App2 Marker");
            this.numMarkers = BinaryFunctions.readByte("numMarkers", byteArrayInputStream, "Not a valid App2 Marker");
            this.iccBytes = BinaryFunctions.readBytes("App2 Data", byteArrayInputStream, n2 - JpegConstants.ICC_PROFILE_LABEL.size() - 2, "Invalid App2 Segment: insufficient data");
        }
        else {
            this.curMarker = -1;
            this.numMarkers = -1;
            this.iccBytes = null;
        }
    }
    
    public App2Segment(final int n, final byte[] buf) throws ImageReadException, IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    @Override
    public int compareTo(final App2Segment app2Segment) {
        return this.curMarker - app2Segment.curMarker;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof App2Segment;
        boolean b2 = false;
        if (b) {
            if (this.curMarker == ((App2Segment)o).curMarker) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    public byte[] getIccBytes() {
        return this.iccBytes;
    }
    
    @Override
    public int hashCode() {
        return this.curMarker;
    }
}
