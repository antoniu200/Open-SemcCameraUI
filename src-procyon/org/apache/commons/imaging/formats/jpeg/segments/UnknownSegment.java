// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;

public class UnknownSegment extends GenericSegment
{
    public UnknownSegment(final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2, inputStream);
    }
    
    public UnknownSegment(final int n, final byte[] array) {
        super(n, array);
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
}
