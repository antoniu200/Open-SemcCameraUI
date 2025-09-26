package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class HylaFaxTagConstants {
    public static final TagInfoLong EXIF_TAG_FAX_RECV_PARAMS = new TagInfoLong("FaxRecvParams", 34908, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_FAX_SUB_ADDRESS = new TagInfoAscii("FaxSubAddress", 34909, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoLong EXIF_TAG_FAX_RECV_TIME = new TagInfoLong("FaxRecvTime", 34910, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_FAX_DCS = new TagInfoAscii("FaxDCS", 34911, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_HYLAFAX_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_FAX_RECV_PARAMS, EXIF_TAG_FAX_SUB_ADDRESS, EXIF_TAG_FAX_RECV_TIME, EXIF_TAG_FAX_DCS));

    private HylaFaxTagConstants() {
    }
}
