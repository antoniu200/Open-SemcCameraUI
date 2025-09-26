// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.util.Comparator;

public abstract class TiffElement
{
    public static final Comparator<TiffElement> COMPARATOR;
    public final int length;
    public final long offset;
    
    static {
        COMPARATOR = new Comparator<TiffElement>() {
            @Override
            public int compare(final TiffElement tiffElement, final TiffElement tiffElement2) {
                if (tiffElement.offset < tiffElement2.offset) {
                    return -1;
                }
                if (tiffElement.offset > tiffElement2.offset) {
                    return 1;
                }
                return 0;
            }
        };
    }
    
    public TiffElement(final long offset, final int length) {
        this.offset = offset;
        this.length = length;
    }
    
    public String getElementDescription() {
        return this.getElementDescription(false);
    }
    
    public abstract String getElementDescription(final boolean p0);
    
    public abstract static class DataElement extends TiffElement
    {
        private final byte[] data;
        
        public DataElement(final long n, final int n2, final byte[] data) {
            super(n, n2);
            this.data = data;
        }
        
        public byte[] getData() {
            return this.data;
        }
        
        public int getDataLength() {
            return this.data.length;
        }
    }
    
    public static final class Stub extends TiffElement
    {
        public Stub(final long n, final int n2) {
            super(n, n2);
        }
        
        @Override
        public String getElementDescription(final boolean b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Element, offset: ");
            sb.append(this.offset);
            sb.append(", length: ");
            sb.append(this.length);
            sb.append(", last: ");
            sb.append(this.offset + this.length);
            return sb.toString();
        }
    }
}
