// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class DcfTagConstants
{
    public static final List<TagInfo> ALL_DCF_TAGS;
    public static final int COLOR_SPACE_VALUE_ADOBE_RGB = 2;
    public static final int COLOR_SPACE_VALUE_SRGB = 1;
    public static final int COLOR_SPACE_VALUE_UNCALIBRATED = 65535;
    public static final TagInfoShort EXIF_TAG_COLOR_SPACE;
    public static final TagInfoAscii EXIF_TAG_RELATED_IMAGE_FILE_FORMAT;
    public static final TagInfoShortOrLong EXIF_TAG_RELATED_IMAGE_LENGTH;
    public static final TagInfoShortOrLong EXIF_TAG_RELATED_IMAGE_WIDTH;
    
    static {
        EXIF_TAG_RELATED_IMAGE_FILE_FORMAT = new TagInfoAscii("RelatedImageFileFormat", 4096, -1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
        EXIF_TAG_RELATED_IMAGE_WIDTH = new TagInfoShortOrLong("RelatedImageWidth", 4097, 1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
        EXIF_TAG_RELATED_IMAGE_LENGTH = new TagInfoShortOrLong("RelatedImageLength", 4098, 1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
        EXIF_TAG_COLOR_SPACE = new TagInfoShort("ColorSpace", 40961, 1, TiffDirectoryType.EXIF_DIRECTORY_EXIF_IFD);
        ALL_DCF_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(DcfTagConstants.EXIF_TAG_RELATED_IMAGE_FILE_FORMAT, DcfTagConstants.EXIF_TAG_RELATED_IMAGE_WIDTH, DcfTagConstants.EXIF_TAG_RELATED_IMAGE_LENGTH, DcfTagConstants.EXIF_TAG_COLOR_SPACE));
    }
    
    private DcfTagConstants() {
    }
}
