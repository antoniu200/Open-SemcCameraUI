package org.apache.commons.imaging.formats.tiff.constants;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.InputDeviceCompat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DcfTagConstants {
    public static final int COLOR_SPACE_VALUE_ADOBE_RGB = 2;
    public static final int COLOR_SPACE_VALUE_SRGB = 1;
    public static final int COLOR_SPACE_VALUE_UNCALIBRATED = 65535;
    public static final TagInfoAscii EXIF_TAG_RELATED_IMAGE_FILE_FORMAT = new TagInfoAscii("RelatedImageFileFormat", 4096, -1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
    public static final TagInfoShortOrLong EXIF_TAG_RELATED_IMAGE_WIDTH = new TagInfoShortOrLong("RelatedImageWidth", FragmentTransaction.TRANSIT_FRAGMENT_OPEN, 1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
    public static final TagInfoShortOrLong EXIF_TAG_RELATED_IMAGE_LENGTH = new TagInfoShortOrLong("RelatedImageLength", InputDeviceCompat.SOURCE_TOUCHSCREEN, 1, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD);
    public static final TagInfoShort EXIF_TAG_COLOR_SPACE = new TagInfoShort("ColorSpace", 40961, 1, TiffDirectoryType.EXIF_DIRECTORY_EXIF_IFD);
    public static final List<TagInfo> ALL_DCF_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_RELATED_IMAGE_FILE_FORMAT, EXIF_TAG_RELATED_IMAGE_WIDTH, EXIF_TAG_RELATED_IMAGE_LENGTH, EXIF_TAG_COLOR_SPACE));

    private DcfTagConstants() {
    }
}
