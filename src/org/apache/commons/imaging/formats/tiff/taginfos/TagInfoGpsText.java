package org.apache.commons.imaging.formats.tiff.taginfos;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TagInfoGpsText extends TagInfo {
    private static final TextEncoding TEXT_ENCODING_ASCII = new TextEncoding(new byte[]{65, 83, 67, 73, 73, 0, 0, 0}, "US-ASCII");
    private static final TextEncoding TEXT_ENCODING_JIS = new TextEncoding(new byte[]{74, 73, 83, 0, 0, 0, 0, 0}, "JIS");
    private static final TextEncoding TEXT_ENCODING_UNICODE_LE = new TextEncoding(new byte[]{85, 78, 73, 67, 79, 68, 69, 0}, "UTF-16LE");
    private static final TextEncoding TEXT_ENCODING_UNICODE_BE = new TextEncoding(new byte[]{85, 78, 73, 67, 79, 68, 69, 0}, "UTF-16BE");
    private static final TextEncoding TEXT_ENCODING_UNDEFINED = new TextEncoding(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, "ISO-8859-1");
    private static final TextEncoding[] TEXT_ENCODINGS = {TEXT_ENCODING_ASCII, TEXT_ENCODING_JIS, TEXT_ENCODING_UNICODE_LE, TEXT_ENCODING_UNICODE_BE, TEXT_ENCODING_UNDEFINED};

    @Override // org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
    public boolean isText() {
        return true;
    }

    public TagInfoGpsText(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.UNDEFINED, i2, tiffDirectoryType);
    }

    private static final class TextEncoding {
        public final String encodingName;
        final byte[] prefix;

        public TextEncoding(byte[] bArr, String str) {
            this.prefix = bArr;
            this.encodingName = str;
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
    public byte[] encodeValue(FieldType fieldType, Object obj, ByteOrder byteOrder) throws ImageWriteException, UnsupportedEncodingException {
        TextEncoding textEncoding;
        if (!(obj instanceof String)) {
            throw new ImageWriteException("GPS text value not String", obj);
        }
        String str = (String) obj;
        try {
            byte[] bytes = str.getBytes(TEXT_ENCODING_ASCII.encodingName);
            if (new String(bytes, TEXT_ENCODING_ASCII.encodingName).equals(str)) {
                byte[] bArr = new byte[bytes.length + TEXT_ENCODING_ASCII.prefix.length];
                System.arraycopy(TEXT_ENCODING_ASCII.prefix, 0, bArr, 0, TEXT_ENCODING_ASCII.prefix.length);
                System.arraycopy(bytes, 0, bArr, TEXT_ENCODING_ASCII.prefix.length, bytes.length);
                return bArr;
            }
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                textEncoding = TEXT_ENCODING_UNICODE_BE;
            } else {
                textEncoding = TEXT_ENCODING_UNICODE_LE;
            }
            byte[] bytes2 = str.getBytes(textEncoding.encodingName);
            byte[] bArr2 = new byte[bytes2.length + textEncoding.prefix.length];
            System.arraycopy(textEncoding.prefix, 0, bArr2, 0, textEncoding.prefix.length);
            System.arraycopy(bytes2, 0, bArr2, textEncoding.prefix.length, bytes2.length);
            return bArr2;
        } catch (UnsupportedEncodingException e) {
            throw new ImageWriteException(e.getMessage(), (Throwable) e);
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
    public String getValue(TiffField tiffField) throws UnsupportedEncodingException, ImageReadException {
        if (tiffField.getFieldType() == FieldType.ASCII) {
            Object value = FieldType.ASCII.getValue(tiffField);
            if (value instanceof String) {
                return (String) value;
            }
            if (value instanceof String[]) {
                return ((String[]) value)[0];
            }
            throw new ImageReadException("Unexpected ASCII type decoded");
        }
        if (tiffField.getFieldType() != FieldType.UNDEFINED && tiffField.getFieldType() != FieldType.BYTE) {
            Debug.debug("entry.type: " + tiffField.getFieldType());
            Debug.debug("entry.directoryType: " + tiffField.getDirectoryType());
            Debug.debug("entry.type: " + tiffField.getDescriptionWithoutValue());
            Debug.debug("entry.type: " + tiffField.getFieldType());
            throw new ImageReadException("GPS text field not encoded as bytes.");
        }
        byte[] byteArrayValue = tiffField.getByteArrayValue();
        if (byteArrayValue.length < 8) {
            try {
                return new String(byteArrayValue, "US-ASCII");
            } catch (UnsupportedEncodingException e) {
                throw new ImageReadException("GPS text field missing encoding prefix.", e);
            }
        }
        for (TextEncoding textEncoding : TEXT_ENCODINGS) {
            if (BinaryFunctions.compareBytes(byteArrayValue, 0, textEncoding.prefix, 0, textEncoding.prefix.length)) {
                try {
                    String str = new String(byteArrayValue, textEncoding.prefix.length, byteArrayValue.length - textEncoding.prefix.length, textEncoding.encodingName);
                    byte[] bytes = str.getBytes(textEncoding.encodingName);
                    if (BinaryFunctions.compareBytes(byteArrayValue, textEncoding.prefix.length, bytes, 0, bytes.length)) {
                        return str;
                    }
                } catch (UnsupportedEncodingException e2) {
                    throw new ImageReadException(e2.getMessage(), e2);
                }
            }
        }
        try {
            return new String(byteArrayValue, "US-ASCII");
        } catch (UnsupportedEncodingException e3) {
            throw new ImageReadException("Unknown GPS text encoding prefix.", e3);
        }
    }
}
