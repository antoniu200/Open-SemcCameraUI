// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class WangTagConstants
{
    public static final List<TagInfo> ALL_WANG_TAGS;
    public static final TagInfoByte EXIF_TAG_WANG_ANNOTATION;
    
    static {
        EXIF_TAG_WANG_ANNOTATION = new TagInfoByte("WangAnnotation", 32932, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_WANG_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(WangTagConstants.EXIF_TAG_WANG_ANNOTATION));
    }
    
    private WangTagConstants() {
    }
}
