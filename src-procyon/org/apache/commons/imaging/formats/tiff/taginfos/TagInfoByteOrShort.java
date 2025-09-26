// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.common.ByteConversions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoByteOrShort extends TagInfo
{
    public TagInfoByteOrShort(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.BYTE_OR_SHORT, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final byte... array) {
        return array;
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final short... array) {
        return ByteConversions.toBytes(array, byteOrder);
    }
}
