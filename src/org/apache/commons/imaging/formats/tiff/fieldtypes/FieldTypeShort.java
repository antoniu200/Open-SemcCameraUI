package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.TiffField;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FieldTypeShort extends FieldType {
    public FieldTypeShort(int i, String str) {
        super(i, str, 2);
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public Object getValue(TiffField tiffField) {
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (tiffField.getCount() == 1) {
            return Short.valueOf(ByteConversions.toShort(byteArrayValue, tiffField.getByteOrder()));
        }
        return ByteConversions.toShorts(byteArrayValue, tiffField.getByteOrder());
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public byte[] writeData(Object obj, ByteOrder byteOrder) throws ImageWriteException {
        if (obj instanceof Short) {
            return ByteConversions.toBytes(((Short) obj).shortValue(), byteOrder);
        }
        if (obj instanceof short[]) {
            return ByteConversions.toBytes((short[]) obj, byteOrder);
        }
        if (obj instanceof Short[]) {
            Short[] shArr = (Short[]) obj;
            short[] sArr = new short[shArr.length];
            for (int i = 0; i < sArr.length; i++) {
                sArr[i] = shArr[i].shortValue();
            }
            return ByteConversions.toBytes(sArr, byteOrder);
        }
        throw new ImageWriteException("Invalid data", obj);
    }
}
