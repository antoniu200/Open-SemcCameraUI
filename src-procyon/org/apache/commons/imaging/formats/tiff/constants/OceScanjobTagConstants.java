// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class OceScanjobTagConstants
{
    public static final List<TagInfo> ALL_OCE_SCANJOB_TAGS;
    public static final TagInfoAscii EXIF_TAG_OCE_APPLICATION_SELECTOR;
    public static final TagInfoAscii EXIF_TAG_OCE_IDENTIFICATION_NUMBER;
    public static final TagInfoAscii EXIF_TAG_OCE_IMAGE_LOGIC_CHARACTERISTICS;
    public static final TagInfoAscii EXIF_TAG_OCE_SCANJOB_DESCRIPTION;
    
    static {
        EXIF_TAG_OCE_SCANJOB_DESCRIPTION = new TagInfoAscii("Oce Scanjob Description", 50215, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_OCE_APPLICATION_SELECTOR = new TagInfoAscii("Oce Application Selector", 50216, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_OCE_IDENTIFICATION_NUMBER = new TagInfoAscii("Oce Identification Number", 50217, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_OCE_IMAGE_LOGIC_CHARACTERISTICS = new TagInfoAscii("Oce ImageLogic Characteristics", 50218, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_OCE_SCANJOB_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(OceScanjobTagConstants.EXIF_TAG_OCE_SCANJOB_DESCRIPTION, OceScanjobTagConstants.EXIF_TAG_OCE_APPLICATION_SELECTOR, OceScanjobTagConstants.EXIF_TAG_OCE_IDENTIFICATION_NUMBER, OceScanjobTagConstants.EXIF_TAG_OCE_IMAGE_LOGIC_CHARACTERISTICS));
    }
    
    private OceScanjobTagConstants() {
    }
}
