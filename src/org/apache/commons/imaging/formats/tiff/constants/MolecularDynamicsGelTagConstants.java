package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class MolecularDynamicsGelTagConstants {
    public static final TagInfoLong EXIF_TAG_MD_FILE_TAG = new TagInfoLong("MD FileTag", 33445, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoRational EXIF_TAG_MD_SCALE_PIXEL = new TagInfoRational("MD ScalePixel", 33446, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoShort EXIF_TAG_MD_COLOR_TABLE = new TagInfoShort("MD ColorTable", 33447, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_MD_LAB_NAME = new TagInfoAscii("MD LabName", 33448, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_MD_SAMPLE_INFO = new TagInfoAscii("MD SampleInfo", 33449, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_MD_PREP_DATE = new TagInfoAscii("MD PrepDate", 33450, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_MD_PREP_TIME = new TagInfoAscii("MD PrepTime", 33451, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_MD_FILE_UNITS = new TagInfoAscii("MD FileUnits", 33452, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_MOLECULAR_DYNAMICS_GEL_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_MD_FILE_TAG, EXIF_TAG_MD_SCALE_PIXEL, EXIF_TAG_MD_COLOR_TABLE, EXIF_TAG_MD_LAB_NAME, EXIF_TAG_MD_SAMPLE_INFO, EXIF_TAG_MD_PREP_DATE, EXIF_TAG_MD_PREP_TIME, EXIF_TAG_MD_FILE_UNITS));

    private MolecularDynamicsGelTagConstants() {
    }
}
