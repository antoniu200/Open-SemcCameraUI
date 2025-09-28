package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FieldTypeLong extends FieldType {
    public FieldTypeLong(int i, String str) {
        super(i, str, 4);
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public Object getValue(TiffField tiffField) {
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1) {
            return Integer.valueOf(ByteConversions.toInt(byteArrayValue, tiffField.getByteOrder()));
        }
        return ByteConversions.toInts(byteArrayValue, tiffField.getByteOrder());
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public byte[] writeData(Object obj, ByteOrder byteOrder) throws ImageWriteException {
        if (obj instanceof Integer) {
            return ByteConversions.toBytes(((Integer) obj).intValue(), byteOrder);
        }
        if (obj instanceof int[]) {
            return ByteConversions.toBytes((int[]) obj, byteOrder);
        }
        if (obj instanceof Integer[]) {
            Integer[] numArr = (Integer[]) obj;
            int[] iArr = new int[numArr.length];
            for (int i = 0; i < iArr.length; i++) {
                iArr[i] = numArr[i].intValue();
            }
            return ByteConversions.toBytes(iArr, byteOrder);
        }
        throw new ImageWriteException("Invalid data", obj);
    }
}
