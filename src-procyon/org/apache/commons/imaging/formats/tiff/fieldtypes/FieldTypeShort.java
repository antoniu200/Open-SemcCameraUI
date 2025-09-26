// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeShort extends FieldType
{
    public FieldTypeShort(final int n, final String s) {
        super(n, s, 2);
    }
    
    @Override
    public Object getValue(final TiffField tiffField) {
        final byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1L) {
            return ByteConversions.toShort(byteArrayValue, tiffField.getByteOrder());
        }
        return ByteConversions.toShorts(byteArrayValue, tiffField.getByteOrder());
    }
    
    @Override
    public byte[] writeData(final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (o instanceof Short) {
            return ByteConversions.toBytes((short)o, byteOrder);
        }
        if (o instanceof short[]) {
            return ByteConversions.toBytes((short[])o, byteOrder);
        }
        if (o instanceof Short[]) {
            final Short[] array = (Short[])o;
            final short[] array2 = new short[array.length];
            for (int i = 0; i < array2.length; ++i) {
                array2[i] = array[i];
            }
            return ByteConversions.toBytes(array2, byteOrder);
        }
        throw new ImageWriteException("Invalid data", o);
    }
}
