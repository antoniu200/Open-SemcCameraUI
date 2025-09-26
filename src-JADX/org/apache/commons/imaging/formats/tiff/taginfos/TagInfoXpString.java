package org.apache.commons.imaging.formats.tiff.taginfos;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoXpString extends TagInfo {
    public TagInfoXpString(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.BYTE, i2, tiffDirectoryType);
    }

    @Override // org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
    public byte[] encodeValue(FieldType fieldType, Object obj, ByteOrder byteOrder) throws ImageWriteException {
        if (!(obj instanceof String)) {
            throw new ImageWriteException("Text value not String", obj);
        }
        try {
            return ((String) obj).getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
    public String getValue(TiffField tiffField) throws ImageReadException {
        if (tiffField.getFieldType() != FieldType.BYTE) {
            throw new ImageReadException("Text field not encoded as bytes.");
        }
        try {
            return new String(tiffField.getByteArrayValue(), "UTF-16LE");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }
}
