package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Tiff4TagConstants {
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_00001 = 5;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_0001 = 4;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_001 = 3;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_01 = 2;
    public static final int COLOR_RESPONSE_UNIT_VALUE_0_1 = 1;
    public static final TagInfoShort TIFF_TAG_COLOR_RESPONSE_UNIT = new TagInfoShort("ColorResponseUnit", 300, 1, TiffDirectoryType.TIFF_DIRECTORY_ROOT);
    public static final List<TagInfo> ALL_TIFF_4_TAGS = Collections.unmodifiableList(Arrays.asList(TIFF_TAG_COLOR_RESPONSE_UNIT));

    private Tiff4TagConstants() {
    }
}
