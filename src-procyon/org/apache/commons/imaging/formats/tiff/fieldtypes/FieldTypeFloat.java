// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeFloat extends FieldType
{
    public FieldTypeFloat(final int n, final String s) {
        super(n, s, 4);
    }
    
    @Override
    public Object getValue(final TiffField tiffField) {
        final byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1L) {
            return ByteConversions.toFloat(byteArrayValue, tiffField.getByteOrder());
        }
        return ByteConversions.toFloats(byteArrayValue, tiffField.getByteOrder());
    }
    
    @Override
    public byte[] writeData(final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (o instanceof Float) {
            return ByteConversions.toBytes((float)o, byteOrder);
        }
        if (o instanceof float[]) {
            return ByteConversions.toBytes((float[])o, byteOrder);
        }
        if (o instanceof Float[]) {
            final Float[] array = (Float[])o;
            final float[] array2 = new float[array.length];
            for (int i = 0; i < array2.length; ++i) {
                array2[i] = array[i];
            }
            return ByteConversions.toBytes(array2, byteOrder);
        }
        throw new ImageWriteException("Invalid data", o);
    }
}
