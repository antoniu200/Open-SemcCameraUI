// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDirectory;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class Rfc2301TagConstants
{
    public static final List<TagInfo> ALL_RFC_2301_TAGS;
    public static final int CLEAN_FAX_DATA_VALUE_CLEAN = 0;
    public static final int CLEAN_FAX_DATA_VALUE_REGENERATED = 1;
    public static final int CLEAN_FAX_DATA_VALUE_UNCLEAN = 2;
    public static final int CODING_METHODS_VALUE_T4_1D = 2;
    public static final int CODING_METHODS_VALUE_T4_2D = 4;
    public static final int CODING_METHODS_VALUE_T6 = 8;
    public static final int CODING_METHODS_VALUE_T81 = 32;
    public static final int CODING_METHODS_VALUE_T82_T43 = 64;
    public static final int CODING_METHODS_VALUE_T82_T85 = 16;
    public static final int FAX_PROFILE_VALUE_EXTENDED_B_AND_W_LOSSLESS_F = 2;
    public static final int FAX_PROFILE_VALUE_LOSSLESS_COLOR_AND_GRAYSCALE_L = 5;
    public static final int FAX_PROFILE_VALUE_LOSSLESS_JBIG_B_AND_W_J = 3;
    public static final int FAX_PROFILE_VALUE_LOSSY_COLOR_AND_GRAYSCALE_C = 4;
    public static final int FAX_PROFILE_VALUE_MINIMAL_B_AND_W_LOSSLESS_S = 1;
    public static final int FAX_PROFILE_VALUE_MIXED_RASTER_CONTENT_M = 6;
    public static final int FAX_PROFILE_VALUE_UNKNOWN = 0;
    public static final int PROFILE_TYPE_VALUE_GROUP_3_FAX = 1;
    public static final int PROFILE_TYPE_VALUE_UNSPECIFIED = 0;
    public static final TagInfoShortOrLong TIFF_TAG_BAD_FAX_LINES;
    public static final TagInfoShort TIFF_TAG_CLEAN_FAX_DATA;
    public static final TagInfoLong TIFF_TAG_CODING_METHODS;
    public static final TagInfoShortOrLong TIFF_TAG_CONSECUTIVE_BAD_FAX_LINES;
    public static final TagInfoRational TIFF_TAG_DECODE;
    public static final TagInfoShort TIFF_TAG_DEFAULT_IMAGE_COLOR;
    public static final TagInfoByte TIFF_TAG_FAX_PROFILE;
    public static final TagInfoDirectory TIFF_TAG_GLOBAL_PARAMETERS_IFD;
    public static final TagInfoShortOrLong TIFF_TAG_IMAGE_LAYER;
    public static final TagInfoByte TIFF_TAG_MODE_NUMBER;
    public static final TagInfoLong TIFF_TAG_PROFILE_TYPE;
    public static final TagInfoLong TIFF_TAG_STRIP_ROW_COUNTS;
    public static final TagInfoByte TIFF_TAG_VERSION_YEAR;
    
    static {
        TIFF_TAG_BAD_FAX_LINES = new TagInfoShortOrLong("BadFaxLines", 326, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_CLEAN_FAX_DATA = new TagInfoShort("CleanFaxData", 327, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_CONSECUTIVE_BAD_FAX_LINES = new TagInfoShortOrLong("ConsecutiveBadFaxLines", 328, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_GLOBAL_PARAMETERS_IFD = new TagInfoDirectory("GlobalParametersIFD", 400, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_PROFILE_TYPE = new TagInfoLong("ProfileType", 401, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_FAX_PROFILE = new TagInfoByte("FaxProfile", 402, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_CODING_METHODS = new TagInfoLong("CodingMethods", 403, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_VERSION_YEAR = new TagInfoByte("VersionYear", 404, 4, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_MODE_NUMBER = new TagInfoByte("ModeNumber", 405, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_DECODE = new TagInfoRational("Decode", 433, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_DEFAULT_IMAGE_COLOR = new TagInfoShort("DefaultImageColor", 434, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_STRIP_ROW_COUNTS = new TagInfoLong("StripRowCounts", 559, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_IMAGE_LAYER = new TagInfoShortOrLong("ImageLayer", 34732, 2, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_RFC_2301_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(Rfc2301TagConstants.TIFF_TAG_BAD_FAX_LINES, Rfc2301TagConstants.TIFF_TAG_CLEAN_FAX_DATA, Rfc2301TagConstants.TIFF_TAG_CONSECUTIVE_BAD_FAX_LINES, Rfc2301TagConstants.TIFF_TAG_GLOBAL_PARAMETERS_IFD, Rfc2301TagConstants.TIFF_TAG_PROFILE_TYPE, Rfc2301TagConstants.TIFF_TAG_FAX_PROFILE, Rfc2301TagConstants.TIFF_TAG_CODING_METHODS, Rfc2301TagConstants.TIFF_TAG_VERSION_YEAR, Rfc2301TagConstants.TIFF_TAG_MODE_NUMBER, Rfc2301TagConstants.TIFF_TAG_DECODE, Rfc2301TagConstants.TIFF_TAG_DEFAULT_IMAGE_COLOR, Rfc2301TagConstants.TIFF_TAG_STRIP_ROW_COUNTS, Rfc2301TagConstants.TIFF_TAG_IMAGE_LAYER));
    }
    
    private Rfc2301TagConstants() {
    }
}
