// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.common.ByteConversions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoShort extends TagInfo
{
    public TagInfoShort(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.SHORT, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final short... array) {
        return ByteConversions.toBytes(array, byteOrder);
    }
    
    public short[] getValue(final ByteOrder byteOrder, final byte[] array) {
        return ByteConversions.toShorts(array, byteOrder);
    }
}
