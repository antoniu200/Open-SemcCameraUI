// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class GeoTiffTagConstants
{
    public static final List<TagInfo> ALL_GEO_TIFF_TAGS;
    public static final TagInfoAscii EXIF_TAG_GEO_ASCII_PARAMS_TAG;
    public static final TagInfoDouble EXIF_TAG_GEO_DOUBLE_PARAMS_TAG;
    public static final TagInfoShort EXIF_TAG_GEO_KEY_DIRECTORY_TAG;
    public static final TagInfoDouble EXIF_TAG_INTERGRAPH_MATRIX_TAG;
    public static final TagInfoDouble EXIF_TAG_MODEL_PIXEL_SCALE_TAG;
    public static final TagInfoDouble EXIF_TAG_MODEL_TIEPOINT_TAG;
    public static final TagInfoDouble EXIF_TAG_MODEL_TRANSFORMATION_TAG;
    
    static {
        EXIF_TAG_MODEL_PIXEL_SCALE_TAG = new TagInfoDouble("ModelPixelScaleTag", 33550, 3, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_INTERGRAPH_MATRIX_TAG = new TagInfoDouble("IntergraphMatrixTag", 33920, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_MODEL_TIEPOINT_TAG = new TagInfoDouble("ModelTiepointTag", 33922, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_MODEL_TRANSFORMATION_TAG = new TagInfoDouble("ModelTransformationTag", 34264, 16, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_GEO_KEY_DIRECTORY_TAG = new TagInfoShort("GeoKeyDirectoryTag", 34735, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_GEO_DOUBLE_PARAMS_TAG = new TagInfoDouble("GeoDoubleParamsTag", 34736, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_GEO_ASCII_PARAMS_TAG = new TagInfoAscii("GeoAsciiParamsTag", 34737, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_GEO_TIFF_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(GeoTiffTagConstants.EXIF_TAG_MODEL_PIXEL_SCALE_TAG, GeoTiffTagConstants.EXIF_TAG_INTERGRAPH_MATRIX_TAG, GeoTiffTagConstants.EXIF_TAG_MODEL_TIEPOINT_TAG, GeoTiffTagConstants.EXIF_TAG_MODEL_TRANSFORMATION_TAG, GeoTiffTagConstants.EXIF_TAG_GEO_KEY_DIRECTORY_TAG, GeoTiffTagConstants.EXIF_TAG_GEO_DOUBLE_PARAMS_TAG, GeoTiffTagConstants.EXIF_TAG_GEO_ASCII_PARAMS_TAG));
    }
    
    private GeoTiffTagConstants() {
    }
}
