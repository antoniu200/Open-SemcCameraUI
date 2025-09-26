package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.TiffField;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FieldTypeRational extends FieldType {
    public FieldTypeRational(int i, String str) {
        super(i, str, 8);
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public Object getValue(TiffField tiffField) {
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1) {
            return ByteConversions.toRational(byteArrayValue, tiffField.getByteOrder());
        }
        return ByteConversions.toRationals(byteArrayValue, tiffField.getByteOrder());
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public byte[] writeData(Object obj, ByteOrder byteOrder) throws ImageWriteException {
        if (obj instanceof RationalNumber) {
            return ByteConversions.toBytes((RationalNumber) obj, byteOrder);
        }
        if (obj instanceof RationalNumber[]) {
            return ByteConversions.toBytes((RationalNumber[]) obj, byteOrder);
        }
        if (obj instanceof Number) {
            return ByteConversions.toBytes(RationalNumber.valueOf(((Number) obj).doubleValue()), byteOrder);
        }
        int i = 0;
        if (obj instanceof Number[]) {
            Number[] numberArr = (Number[]) obj;
            RationalNumber[] rationalNumberArr = new RationalNumber[numberArr.length];
            while (i < numberArr.length) {
                rationalNumberArr[i] = RationalNumber.valueOf(numberArr[i].doubleValue());
                i++;
            }
            return ByteConversions.toBytes(rationalNumberArr, byteOrder);
        }
        if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            RationalNumber[] rationalNumberArr2 = new RationalNumber[dArr.length];
            while (i < dArr.length) {
                rationalNumberArr2[i] = RationalNumber.valueOf(dArr[i]);
                i++;
            }
            return ByteConversions.toBytes(rationalNumberArr2, byteOrder);
        }
        throw new ImageWriteException("Invalid data", obj);
    }
}
