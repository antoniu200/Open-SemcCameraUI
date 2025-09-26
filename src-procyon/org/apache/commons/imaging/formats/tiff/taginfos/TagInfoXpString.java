// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoXpString extends TagInfo
{
    public TagInfoXpString(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.BYTE, n2, tiffDirectoryType);
    }
    
    @Override
    public byte[] encodeValue(final FieldType fieldType, final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        if (!(o instanceof String)) {
            throw new ImageWriteException("Text value not String", o);
        }
        final String s = (String)o;
        try {
            return s.getBytes("UTF-16LE");
        }
        catch (final UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    @Override
    public String getValue(final TiffField tiffField) throws ImageReadException {
        if (tiffField.getFieldType() != FieldType.BYTE) {
            throw new ImageReadException("Text field not encoded as bytes.");
        }
        try {
            return new String(tiffField.getByteArrayValue(), "UTF-16LE");
        }
        catch (final UnsupportedEncodingException ex) {
            return null;
        }
    }
}
