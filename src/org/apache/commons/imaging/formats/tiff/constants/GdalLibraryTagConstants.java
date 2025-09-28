package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class GdalLibraryTagConstants {
    public static final TagInfoAscii EXIF_TAG_GDAL_METADATA = new TagInfoAscii("GDALMetadata", 42112, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_GDAL_NO_DATA = new TagInfoAscii("GDALNoData", 42113, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_GDAL_LIBRARY_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_GDAL_METADATA, EXIF_TAG_GDAL_NO_DATA));

    private GdalLibraryTagConstants() {
    }
}
