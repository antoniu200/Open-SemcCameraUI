// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;

public class TagInfoDirectory extends TagInfoLong
{
    public TagInfoDirectory(final String s, final int n, final int n2, final TiffDirectoryType tiffDirectoryType) {
        super(s, n, n2, tiffDirectoryType, true);
    }
}
