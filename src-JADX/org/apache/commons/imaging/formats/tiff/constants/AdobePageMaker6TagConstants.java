package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLongOrIFD;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class AdobePageMaker6TagConstants {
    public static final int INDEXED_VALUE_INDEXED = 1;
    public static final int INDEXED_VALUE_NOT_INDEXED = 0;
    public static final int OPIPROXY_VALUE_HIGHER_RESOLUTION_IMAGE_DOES_NOT_EXIST = 0;
    public static final int OPIPROXY_VALUE_HIGHER_RESOLUTION_IMAGE_EXISTS = 1;
    public static final TagInfoLongOrIFD TIFF_TAG_SUB_IFD = new TagInfoLongOrIFD("SubIFDs", 330, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN, true);
    public static final TagInfoByte TIFF_TAG_CLIP_PATH = new TagInfoByte("ClipPath", 343, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoLong TIFF_TAG_XCLIP_PATH_UNITS = new TagInfoLong("XClipPathUnits", 344, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoLong TIFF_TAG_YCLIP_PATH_UNITS = new TagInfoLong("YClipPathUnits", 345, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoShort TIFF_TAG_INDEXED = new TagInfoShort("Indexed", 346, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoShort TIFF_TAG_OPIPROXY = new TagInfoShort("OPIProxy", 351, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii TIFF_TAG_IMAGE_ID = new TagInfoAscii("ImageID", 32781, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_ADOBE_PAGEMAKER_6_TAGS = Collections.unmodifiableList(Arrays.asList(TIFF_TAG_SUB_IFD, TIFF_TAG_CLIP_PATH, TIFF_TAG_XCLIP_PATH_UNITS, TIFF_TAG_YCLIP_PATH_UNITS, TIFF_TAG_INDEXED, TIFF_TAG_OPIPROXY, TIFF_TAG_IMAGE_ID));

    private AdobePageMaker6TagConstants() {
    }
}
