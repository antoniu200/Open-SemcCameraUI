package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffField;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FieldTypeAscii extends FieldType {
    public FieldTypeAscii(int i, String str) {
        super(i, str, 1);
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public Object getValue(TiffField tiffField) {
        int i;
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        int i2 = 1;
        for (int i3 = 0; i3 < byteArrayValue.length - 1; i3++) {
            if (byteArrayValue[i3] == 0) {
                i2++;
            }
        }
        String[] strArr = new String[i2];
        strArr[0] = "";
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < byteArrayValue.length; i6++) {
            if (byteArrayValue[i6] == 0) {
                try {
                    String str = new String(byteArrayValue, i4, i6 - i4, "UTF-8");
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
        if (i4 < byteArrayValue.length) {
            try {
                strArr[i5] = new String(byteArrayValue, i4, byteArrayValue.length - i4, "UTF-8");
            } catch (UnsupportedEncodingException unused3) {
            }
        }
        return strArr.length == 1 ? strArr[0] : strArr;
    }

    @Override // org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType
    public byte[] writeData(Object obj, ByteOrder byteOrder) throws ImageWriteException, UnsupportedEncodingException {
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            byte[] bArr2 = new byte[bArr.length + 1];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            bArr2[bArr2.length - 1] = 0;
            return bArr2;
        }
        if (obj instanceof String) {
            try {
                byte[] bytes = ((String) obj).getBytes("UTF-8");
                byte[] bArr3 = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, bArr3, 0, bytes.length);
                bArr3[bArr3.length - 1] = 0;
                return bArr3;
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Your Java doesn't support UTF-8", e);
            }
        }
        if (obj instanceof String[]) {
            String[] strArr = (String[]) obj;
            int length = 0;
            for (String str : strArr) {
                try {
                    length += str.getBytes("UTF-8").length + 1;
                } catch (UnsupportedEncodingException e2) {
                    throw new IllegalArgumentException("Your Java doesn't support UTF-8", e2);
                }
            }
            byte[] bArr4 = new byte[length];
            int length2 = 0;
            for (String str2 : strArr) {
                try {
                    byte[] bytes2 = str2.getBytes("UTF-8");
                    System.arraycopy(bytes2, 0, bArr4, length2, bytes2.length);
                    length2 += bytes2.length + 1;
                } catch (UnsupportedEncodingException e3) {
                    throw new IllegalArgumentException("Your Java doesn't support UTF-8", e3);
                }
            }
            return bArr4;
        }
        throw new ImageWriteException("Unknown data type: " + obj);
    }
}
