package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoFloat extends TagInfo {
    public TagInfoFloat(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.FLOAT, i2, tiffDirectoryType);
    }

    public float[] getValue(ByteOrder byteOrder, byte[] bArr) {
        return ByteConversions.toFloats(bArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, float... fArr) {
        return ByteConversions.toBytes(fArr, byteOrder);
    }
}
