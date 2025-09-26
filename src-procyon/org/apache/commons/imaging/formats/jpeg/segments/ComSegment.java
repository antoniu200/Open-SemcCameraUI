// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;

public class ComSegment extends GenericSegment
{
    public ComSegment(final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2, inputStream);
    }
    
    public ComSegment(final int n, final byte[] array) {
        super(n, array);
    }
    
    public byte[] getComment() {
        return this.getSegmentData();
    }
    
    @Override
    public String getDescription() {
        String segmentDataAsString;
        try {
            segmentDataAsString = this.getSegmentDataAsString("UTF-8");
        }
        catch (final UnsupportedEncodingException ex) {
            segmentDataAsString = "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("COM (");
        sb.append(segmentDataAsString);
        sb.append(")");
        return sb.toString();
    }
}
