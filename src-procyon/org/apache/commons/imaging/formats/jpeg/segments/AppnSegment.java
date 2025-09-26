// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;

public class AppnSegment extends GenericSegment
{
    public AppnSegment(final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2, inputStream);
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("APPN (APP");
        sb.append(this.marker - 65504);
        sb.append(") (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
}
