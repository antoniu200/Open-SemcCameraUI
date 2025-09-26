// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoByte extends TagInfo
{
    public TagInfoByte(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.BYTE, n2, tiffDirectoryType);
    }
    
    public TagInfoByte(final String s, final int n, final List<FieldType> list, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, list, n2, tiffDirectoryType);
    }
    
    public TagInfoByte(final String s, final int n, final FieldType fieldType, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, fieldType, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final byte... array) {
        return array;
    }
}
