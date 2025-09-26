// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.RationalNumber;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoRational extends TagInfo
{
    public TagInfoRational(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.RATIONAL, n2, tiffDirectoryType);
    }
    
    public byte[] encodeValue(final ByteOrder byteOrder, final RationalNumber... array) {
        return ByteConversions.toBytes(array, byteOrder);
    }
    
    public RationalNumber[] getValue(final ByteOrder byteOrder, final byte[] array) {
        return ByteConversions.toRationals(array, byteOrder);
    }
}
