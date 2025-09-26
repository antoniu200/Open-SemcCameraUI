// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoAsciiOrByte extends TagInfo
{
    public TagInfoAsciiOrByte(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, FieldType.ASCII_OR_BYTE, n2, tiffDirectoryType, false);
    }
}
