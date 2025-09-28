package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoLongOrIFD extends TagInfo {
    public TagInfoLongOrIFD(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.LONG_OR_IFD, i2, tiffDirectoryType);
    }

    public TagInfoLongOrIFD(String str, int i, int i2, TiffDirectoryType tiffDirectoryType, boolean z) {
        super(str, i, FieldType.LONG_OR_IFD, i2, tiffDirectoryType, z);
    }

    public int[] getValue(ByteOrder byteOrder, byte[] bArr) {
        return ByteConversions.toInts(bArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, int... iArr) {
        return ByteConversions.toBytes(iArr, byteOrder);
    }
}
