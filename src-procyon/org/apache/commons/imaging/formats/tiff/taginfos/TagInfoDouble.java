// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.common.ByteConversions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoDouble extends TagInfo
{
    public TagInfoDouble(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.DOUBLE, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final double... array) {
        return ByteConversions.toBytes(array, byteOrder);
    }
    
    public double[] getValue(final ByteOrder byteOrder, final byte[] array) {
        return ByteConversions.toDoubles(array, byteOrder);
    }
}
