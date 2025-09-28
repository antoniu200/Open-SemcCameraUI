package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoShortOrLong extends TagInfo {
    public TagInfoShortOrLong(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.SHORT_OR_LONG, i2, tiffDirectoryType, false);
    }

    public TagInfoShortOrLong(String str, int i, int i2, TiffDirectoryType tiffDirectoryType, boolean z) {
        super(str, i, FieldType.SHORT_OR_LONG, i2, tiffDirectoryType, z);
    }

    public byte[] encodeValue(ByteOrder byteOrder, short... sArr) {
        return ByteConversions.toBytes(sArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, int... iArr) {
        return ByteConversions.toBytes(iArr, byteOrder);
    }
}
