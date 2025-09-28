package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoShortOrRational extends TagInfo {
    public TagInfoShortOrRational(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.SHORT_OR_RATIONAL, i2, tiffDirectoryType, false);
    }

    public byte[] encodeValue(ByteOrder byteOrder, short... sArr) {
        return ByteConversions.toBytes(sArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, RationalNumber... rationalNumberArr) {
        return ByteConversions.toBytes(rationalNumberArr, byteOrder);
    }
}
