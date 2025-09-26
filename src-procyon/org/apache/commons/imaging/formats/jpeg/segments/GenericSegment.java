// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;

public abstract class GenericSegment extends Segment
{
    private final byte[] segmentData;
    
    public GenericSegment(final int n, final int n2, final InputStream inputStream) throws IOException {
        super(n, n2);
        this.segmentData = BinaryFunctions.readBytes("Segment Data", inputStream, n2, "Invalid Segment: insufficient data");
    }
    
    public GenericSegment(final int n, final byte[] segmentData) {
        super(n, segmentData.length);
        this.segmentData = segmentData;
    }
    
    @Override
    public void dump(final PrintWriter printWriter) {
        this.dump(printWriter, 0);
    }
    
    public void dump(final PrintWriter printWriter, final int n) {
        for (int i = 0; i < 50; ++i) {
            final int j = i + n;
            if (j >= this.segmentData.length) {
                break;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("\t");
            sb.append(j);
            this.debugNumber(printWriter, sb.toString(), this.segmentData[j], 1);
        }
    }
    
    protected byte getSegmentData(final int n) {
        return this.segmentData[n];
    }
    
    public byte[] getSegmentData() {
        return this.segmentData.clone();
    }
    
    public String getSegmentDataAsString(final String charsetName) throws UnsupportedEncodingException {
        return new String(this.segmentData, charsetName);
    }
}
