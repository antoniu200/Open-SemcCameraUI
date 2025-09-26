// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.ImageWriteException;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;

abstract class TiffOutputItem
{
    public static final long UNDEFINED_VALUE = -1L;
    private long offset;
    
    TiffOutputItem() {
        this.offset = -1L;
    }
    
    public abstract String getItemDescription();
    
    public abstract int getItemLength();
    
    protected long getOffset() {
        return this.offset;
    }
    
    protected void setOffset(final long offset) {
        this.offset = offset;
    }
    
    public abstract void writeItem(final BinaryOutputStream p0) throws IOException, ImageWriteException;
    
    public static class Value extends TiffOutputItem
    {
        private final byte[] bytes;
        private final String name;
        
        public Value(final String name, final byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }
        
        @Override
        public String getItemDescription() {
            return this.name;
        }
        
        @Override
        public int getItemLength() {
            return this.bytes.length;
        }
        
        public void updateValue(final byte[] array) throws ImageWriteException {
            if (this.bytes.length != array.length) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Updated data size mismatch: ");
                sb.append(this.bytes.length);
                sb.append(" vs. ");
                sb.append(array.length);
                throw new ImageWriteException(sb.toString());
            }
            System.arraycopy(array, 0, this.bytes, 0, array.length);
        }
        
        @Override
        public void writeItem(final BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
            binaryOutputStream.write(this.bytes);
        }
    }
}
