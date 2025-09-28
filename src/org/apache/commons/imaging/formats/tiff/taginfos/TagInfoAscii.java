package org.apache.commons.imaging.formats.tiff.taginfos;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfoAscii extends TagInfo {
    public TagInfoAscii(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.ASCII, i2, tiffDirectoryType);
    }

    public String[] getValue(ByteOrder byteOrder, byte[] bArr) {
        int i;
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length - 1; i3++) {
            if (bArr[i3] == 0) {
                i2++;
            }
        }
        String[] strArr = new String[i2 + 1];
        strArr[0] = "";
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < bArr.length; i6++) {
            if (bArr[i6] == 0) {
                try {
                    String str = new String(bArr, i4, i6 - i4, "UTF-8");
                    i = i5 + 1;
                    try {
                        strArr[i5] = str;
                    } catch (UnsupportedEncodingException unused) {
                    }
                } catch (UnsupportedEncodingException unused2) {
                    i = i5;
                }
                i5 = i;
                i4 = i6 + 1;
            }
        }
        if (i4 < bArr.length) {
            try {
                strArr[i5] = new String(bArr, i4, bArr.length - i4, "UTF-8");
            } catch (UnsupportedEncodingException unused3) {
            }
        }
        return strArr;
    }

    public byte[] encodeValue(ByteOrder byteOrder, String... strArr) throws ImageWriteException {
        return FieldType.ASCII.writeData(strArr, byteOrder);
    }
}
