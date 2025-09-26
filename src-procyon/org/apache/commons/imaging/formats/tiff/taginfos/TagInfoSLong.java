// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.common.ByteConversions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoSLong extends TagInfo
{
    public TagInfoSLong(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.SLONG, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final int... array) {
        return ByteConversions.toBytes(array, byteOrder);
    }
    
    public int[] getValue(final ByteOrder byteOrder, final byte[] array) {
        return ByteConversions.toInts(array, byteOrder);
    }
}
