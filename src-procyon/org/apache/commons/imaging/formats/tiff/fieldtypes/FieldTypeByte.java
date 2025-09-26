// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeByte extends FieldType
{
    public FieldTypeByte(final int n, final String s) {
        super(n, s, 1);
    }
    
    @Override
    public Object getValue(final TiffField tiffField) {
        final byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1L) {
            return byteArrayValue[0];
        }
        return byteArrayValue;
    }
    
    @Override
    public byte[] writeData(final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (o instanceof Byte) {
            return new byte[] { (byte)o };
        }
        if (o instanceof byte[]) {
            return (byte[])o;
        }
        throw new ImageWriteException("Invalid data", o);
    }
}
