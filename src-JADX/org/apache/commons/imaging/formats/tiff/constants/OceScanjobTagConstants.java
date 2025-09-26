package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class OceScanjobTagConstants {
    public static final TagInfoAscii EXIF_TAG_OCE_SCANJOB_DESCRIPTION = new TagInfoAscii("Oce Scanjob Description", 50215, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_OCE_APPLICATION_SELECTOR = new TagInfoAscii("Oce Application Selector", 50216, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_OCE_IDENTIFICATION_NUMBER = new TagInfoAscii("Oce Identification Number", 50217, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_OCE_IMAGE_LOGIC_CHARACTERISTICS = new TagInfoAscii("Oce ImageLogic Characteristics", 50218, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_OCE_SCANJOB_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_OCE_SCANJOB_DESCRIPTION, EXIF_TAG_OCE_APPLICATION_SELECTOR, EXIF_TAG_OCE_IDENTIFICATION_NUMBER, EXIF_TAG_OCE_IMAGE_LOGIC_CHARACTERISTICS));

    private OceScanjobTagConstants() {
    }
}
