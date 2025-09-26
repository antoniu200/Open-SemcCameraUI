// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class Tiff4TagConstants
{
    public static final List<TagInfo> ALL_TIFF_4_TAGS;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_00001 = 5;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_0001 = 4;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_001 = 3;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_01 = 2;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_1 = 1;
    public static final TagInfoShort TIFF_TAG_COLOR_RESPONSE_UNIT;
    
    static {
        TIFF_TAG_COLOR_RESPONSE_UNIT = new TagInfoShort("ColorResponseUnit", 300, 1, TiffDirectoryType.TIFF_DIRECTORY_ROOT);
        ALL_TIFF_4_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(Tiff4TagConstants.TIFF_TAG_COLOR_RESPONSE_UNIT));
    }
    
    private Tiff4TagConstants() {
    }
}
