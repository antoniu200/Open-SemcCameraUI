// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

public class JpegImageData extends DataElement
{
    public JpegImageData(final long n, final int n2, final byte[] array) {
        super(n, n2, array);
    }
    
    @Override
    public String getElementDescription(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Jpeg image data: ");
        sb.append(((DataElement)this).getDataLength());
        sb.append(" bytes");
        return sb.toString();
    }
}
