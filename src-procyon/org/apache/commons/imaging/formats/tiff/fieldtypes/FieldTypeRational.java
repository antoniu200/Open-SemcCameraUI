// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.RationalNumber;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeRational extends FieldType
{
    public FieldTypeRational(final int n, final String s) {
        super(n, s, 8);
    }
    
    @Override
    public Object getValue(final TiffField tiffField) {
        final byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1L) {
            return ByteConversions.toRational(byteArrayValue, tiffField.getByteOrder());
        }
        return ByteConversions.toRationals(byteArrayValue, tiffField.getByteOrder());
    }
    
    @Override
    public byte[] writeData(final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (o instanceof RationalNumber) {
            return ByteConversions.toBytes((RationalNumber)o, byteOrder);
        }
        if (o instanceof RationalNumber[]) {
            return ByteConversions.toBytes((RationalNumber[])o, byteOrder);
        }
        if (o instanceof Number) {
            return ByteConversions.toBytes(RationalNumber.valueOf(((Number)o).doubleValue()), byteOrder);
        }
        final boolean b = o instanceof Number[];
        int i = 0;
        final int n = 0;
        if (b) {
            final Number[] array = (Number[])o;
            final RationalNumber[] array2 = new RationalNumber[array.length];
            for (int j = n; j < array.length; ++j) {
                array2[j] = RationalNumber.valueOf(array[j].doubleValue());
            }
            return ByteConversions.toBytes(array2, byteOrder);
        }
        if (o instanceof double[]) {
            final double[] array3 = (double[])o;
            final RationalNumber[] array4 = new RationalNumber[array3.length];
            while (i < array3.length) {
                array4[i] = RationalNumber.valueOf(array3[i]);
                ++i;
            }
            return ByteConversions.toBytes(array4, byteOrder);
        }
        throw new ImageWriteException("Invalid data", o);
    }
}
