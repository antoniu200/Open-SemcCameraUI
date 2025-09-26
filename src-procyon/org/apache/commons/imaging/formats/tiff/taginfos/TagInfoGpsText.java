// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public final class TagInfoGpsText extends TagInfo
{
    private static final TextEncoding[] TEXT_ENCODINGS;
    private static final TextEncoding TEXT_ENCODING_ASCII;
    private static final TextEncoding TEXT_ENCODING_JIS;
    private static final TextEncoding TEXT_ENCODING_UNDEFINED;
    private static final TextEncoding TEXT_ENCODING_UNICODE_BE;
    private static final TextEncoding TEXT_ENCODING_UNICODE_LE;
    
    static {
        TEXT_ENCODING_ASCII = new TextEncoding(new byte[] { 65, 83, 67, 73, 73, 0, 0, 0 }, "US-ASCII");
        TEXT_ENCODING_JIS = new TextEncoding(new byte[] { 74, 73, 83, 0, 0, 0, 0, 0 }, "JIS");
        TEXT_ENCODING_UNICODE_LE = new TextEncoding(new byte[] { 85, 78, 73, 67, 79, 68, 69, 0 }, "UTF-16LE");
        TEXT_ENCODING_UNICODE_BE = new TextEncoding(new byte[] { 85, 78, 73, 67, 79, 68, 69, 0 }, "UTF-16BE");
        TEXT_ENCODING_UNDEFINED = new TextEncoding(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, "ISO-8859-1");
        TEXT_ENCODINGS = new TextEncoding[] { TagInfoGpsText.TEXT_ENCODING_ASCII, TagInfoGpsText.TEXT_ENCODING_JIS, TagInfoGpsText.TEXT_ENCODING_UNICODE_LE, TagInfoGpsText.TEXT_ENCODING_UNICODE_BE, TagInfoGpsText.TEXT_ENCODING_UNDEFINED };
    }
    
    public TagInfoGpsText(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.UNDEFINED, n2, tiffDirectoryType);
    }
    
    @Override
    public byte[] encodeValue(final FieldType fieldType, final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (!(o instanceof String)) {
            throw new ImageWriteException("GPS text value not String", o);
        }
        final String anObject = (String)o;
        try {
            final byte[] bytes = anObject.getBytes(TagInfoGpsText.TEXT_ENCODING_ASCII.encodingName);
            if (new String(bytes, TagInfoGpsText.TEXT_ENCODING_ASCII.encodingName).equals(anObject)) {
                final byte[] array = new byte[bytes.length + TagInfoGpsText.TEXT_ENCODING_ASCII.prefix.length];
                System.arraycopy(TagInfoGpsText.TEXT_ENCODING_ASCII.prefix, 0, array, 0, TagInfoGpsText.TEXT_ENCODING_ASCII.prefix.length);
                System.arraycopy(bytes, 0, array, TagInfoGpsText.TEXT_ENCODING_ASCII.prefix.length, bytes.length);
                return array;
            }
            TextEncoding textEncoding;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                textEncoding = TagInfoGpsText.TEXT_ENCODING_UNICODE_BE;
            }
            else {
                textEncoding = TagInfoGpsText.TEXT_ENCODING_UNICODE_LE;
            }
            final byte[] bytes2 = anObject.getBytes(textEncoding.encodingName);
            final byte[] array2 = new byte[bytes2.length + textEncoding.prefix.length];
            System.arraycopy(textEncoding.prefix, 0, array2, 0, textEncoding.prefix.length);
            System.arraycopy(bytes2, 0, array2, textEncoding.prefix.length, bytes2.length);
            return array2;
        }
        catch (final UnsupportedEncodingException ex) {
            throw new ImageWriteException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public String getValue(final TiffField tiffField) throws ImageReadException {
        if (tiffField.getFieldType() != FieldType.ASCII) {
            Label_0275: {
                if (tiffField.getFieldType() != FieldType.UNDEFINED) {
                    if (tiffField.getFieldType() != FieldType.BYTE) {
                        break Label_0275;
                    }
                }
                final byte[] byteArrayValue = tiffField.getByteArrayValue();
                if (byteArrayValue.length < 8) {
                    try {
                        return new String(byteArrayValue, "US-ASCII");
                    }
                    catch (final UnsupportedEncodingException ex) {
                        throw new ImageReadException("GPS text field missing encoding prefix.", ex);
                    }
                }
                for (final TextEncoding textEncoding : TagInfoGpsText.TEXT_ENCODINGS) {
                    if (BinaryFunctions.compareBytes(byteArrayValue, 0, textEncoding.prefix, 0, textEncoding.prefix.length)) {
                        try {
                            final String s = new String(byteArrayValue, textEncoding.prefix.length, byteArrayValue.length - textEncoding.prefix.length, textEncoding.encodingName);
                            final byte[] bytes = s.getBytes(textEncoding.encodingName);
                            if (BinaryFunctions.compareBytes(byteArrayValue, textEncoding.prefix.length, bytes, 0, bytes.length)) {
                                return s;
                            }
                        }
                        catch (final UnsupportedEncodingException ex2) {
                            throw new ImageReadException(ex2.getMessage(), ex2);
                        }
                    }
                }
                try {
                    return new String(byteArrayValue, "US-ASCII");
                }
                catch (final UnsupportedEncodingException ex3) {
                    throw new ImageReadException("Unknown GPS text encoding prefix.", ex3);
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("entry.type: ");
            sb.append(tiffField.getFieldType());
            Debug.debug(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("entry.directoryType: ");
            sb2.append(tiffField.getDirectoryType());
            Debug.debug(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("entry.type: ");
            sb3.append(tiffField.getDescriptionWithoutValue());
            Debug.debug(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("entry.type: ");
            sb4.append(tiffField.getFieldType());
            Debug.debug(sb4.toString());
            throw new ImageReadException("GPS text field not encoded as bytes.");
        }
        final Object value = FieldType.ASCII.getValue(tiffField);
        if (value instanceof String) {
            return (String)value;
        }
        if (value instanceof String[]) {
            return ((String[])value)[0];
        }
        throw new ImageReadException("Unexpected ASCII type decoded");
    }
    
    @Override
    public boolean isText() {
        return true;
    }
    
    private static final class TextEncoding
    {
        public final String encodingName;
        final byte[] prefix;
        
        public TextEncoding(final byte[] prefix, final String encodingName) {
            this.prefix = prefix;
            this.encodingName = encodingName;
        }
    }
}
