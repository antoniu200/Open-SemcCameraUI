package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffField;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FieldTypeByte extends FieldType {
    public FieldTypeByte(int i, String str) {
        super(i, str, 1);
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public Object getValue(TiffField tiffField) {
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        return tiffField.getCount() == 1 ? Byte.valueOf(byteArrayValue[0]) : byteArrayValue;
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public byte[] writeData(Object obj, ByteOrder byteOrder) throws ImageWriteException {
        if (obj instanceof Byte) {
            return new byte[]{((Byte) obj).byteValue()};
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        throw new ImageWriteException("Invalid data", obj);
    }
}
