package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class WangTagConstants {
    public static final TagInfoByte EXIF_TAG_WANG_ANNOTATION = new TagInfoByte("WangAnnotation", 32932, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_WANG_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_WANG_ANNOTATION));

    private WangTagConstants() {
    }
}
