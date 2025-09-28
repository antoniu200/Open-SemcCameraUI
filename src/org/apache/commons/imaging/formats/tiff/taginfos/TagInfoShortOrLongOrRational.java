package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoShortOrLongOrRational extends TagInfo {
    public TagInfoShortOrLongOrRational(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.SHORT_OR_LONG_OR_RATIONAL, i2, tiffDirectoryType);
    }

    public byte[] encodeValue(ByteOrder byteOrder, short... sArr) {
        return ByteConversions.toBytes(sArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, int... iArr) {
        return ByteConversions.toBytes(iArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, RationalNumber... rationalNumberArr) {
        return ByteConversions.toBytes(rationalNumberArr, byteOrder);
    }
}
