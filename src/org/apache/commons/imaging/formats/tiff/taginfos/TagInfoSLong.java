package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoSLong extends TagInfo {
    public TagInfoSLong(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.SLONG, i2, tiffDirectoryType);
    }

    public int[] getValue(ByteOrder byteOrder, byte[] bArr) {
        return ByteConversions.toInts(bArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, int... iArr) {
        return ByteConversions.toBytes(iArr, byteOrder);
    }
}
