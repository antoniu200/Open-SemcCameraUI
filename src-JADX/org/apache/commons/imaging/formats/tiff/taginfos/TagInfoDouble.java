package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoDouble extends TagInfo {
    public TagInfoDouble(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.DOUBLE, i2, tiffDirectoryType);
    }

    public double[] getValue(ByteOrder byteOrder, byte[] bArr) {
        return ByteConversions.toDoubles(bArr, byteOrder);
    }

    public byte[] encodeValue(ByteOrder byteOrder, double... dArr) {
        return ByteConversions.toBytes(dArr, byteOrder);
    }
}
