package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class GeoTiffTagConstants {
    public static final TagInfoDouble EXIF_TAG_MODEL_PIXEL_SCALE_TAG = new TagInfoDouble("ModelPixelScaleTag", 33550, 3, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoDouble EXIF_TAG_INTERGRAPH_MATRIX_TAG = new TagInfoDouble("IntergraphMatrixTag", 33920, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoDouble EXIF_TAG_MODEL_TIEPOINT_TAG = new TagInfoDouble("ModelTiepointTag", 33922, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoDouble EXIF_TAG_MODEL_TRANSFORMATION_TAG = new TagInfoDouble("ModelTransformationTag", 34264, 16, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoShort EXIF_TAG_GEO_KEY_DIRECTORY_TAG = new TagInfoShort("GeoKeyDirectoryTag", 34735, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoDouble EXIF_TAG_GEO_DOUBLE_PARAMS_TAG = new TagInfoDouble("GeoDoubleParamsTag", 34736, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final TagInfoAscii EXIF_TAG_GEO_ASCII_PARAMS_TAG = new TagInfoAscii("GeoAsciiParamsTag", 34737, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    public static final List<TagInfo> ALL_GEO_TIFF_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_MODEL_PIXEL_SCALE_TAG, EXIF_TAG_INTERGRAPH_MATRIX_TAG, EXIF_TAG_MODEL_TIEPOINT_TAG, EXIF_TAG_MODEL_TRANSFORMATION_TAG, EXIF_TAG_GEO_KEY_DIRECTORY_TAG, EXIF_TAG_GEO_DOUBLE_PARAMS_TAG, EXIF_TAG_GEO_ASCII_PARAMS_TAG));

    private GeoTiffTagConstants() {
    }
}
