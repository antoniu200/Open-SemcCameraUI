package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoByteOrShort extends TagInfo {
    public byte[] encodeValue(ByteOrder byteOrder, byte... bArr) {
        return bArr;
    }

    public TagInfoByteOrShort(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.BYTE_OR_SHORT, i2, tiffDirectoryType);
    }

    public byte[] encodeValue(ByteOrder byteOrder, short... sArr) {
        return ByteConversions.toBytes(sArr, byteOrder);
    }
}
