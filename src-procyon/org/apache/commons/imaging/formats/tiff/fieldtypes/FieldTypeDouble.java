// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeDouble extends FieldType
{
    public FieldTypeDouble(final int n, final String s) {
        super(n, s, 8);
    }
    
    @Override
    public Object getValue(final TiffField tiffField) {
        final byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1L) {
            return ByteConversions.toDouble(byteArrayValue, tiffField.getByteOrder());
        }
        return ByteConversions.toDoubles(byteArrayValue, tiffField.getByteOrder());
    }
    
    @Override
    public byte[] writeData(final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (o instanceof Double) {
            return ByteConversions.toBytes((double)o, byteOrder);
        }
        if (o instanceof double[]) {
            return ByteConversions.toBytes((double[])o, byteOrder);
        }
        if (o instanceof Double[]) {
            final Double[] array = (Double[])o;
            final double[] array2 = new double[array.length];
            for (int i = 0; i < array2.length; ++i) {
                array2[i] = array[i];
            }
            return ByteConversions.toBytes(array2, byteOrder);
        }
        throw new ImageWriteException("Invalid data", o);
    }
}
