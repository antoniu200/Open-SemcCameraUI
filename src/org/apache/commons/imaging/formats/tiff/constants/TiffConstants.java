package org.apache.commons.imaging.formats.tiff.constants;

import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TiffConstants {
    public static final ByteOrder DEFAULT_TIFF_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    public static final String PARAM_KEY_LZW_COMPRESSION_BLOCK_SIZE = "PARAM_KEY_LZW_COMPRESSION_BLOCK_SIZE";
    public static final String PARAM_KEY_SUBIMAGE_HEIGHT = "SUBIMAGE_HEIGHT";
    public static final String PARAM_KEY_SUBIMAGE_WIDTH = "SUBIMAGE_WIDTH";
    public static final String PARAM_KEY_SUBIMAGE_X = "SUBIMAGE_X";
    public static final String PARAM_KEY_SUBIMAGE_Y = "SUBIMAGE_Y";
    public static final String PARAM_KEY_T4_OPTIONS = "T4_OPTIONS";
    public static final String PARAM_KEY_T6_OPTIONS = "T6_OPTIONS";
    public static final int TIFF_COMPRESSION_CCITT_1D = 2;
    public static final int TIFF_COMPRESSION_CCITT_GROUP_3 = 3;
    public static final int TIFF_COMPRESSION_CCITT_GROUP_4 = 4;
    public static final int TIFF_COMPRESSION_JPEG = 6;
    public static final int TIFF_COMPRESSION_LZW = 5;
    public static final int TIFF_COMPRESSION_PACKBITS = 32773;
    public static final int TIFF_COMPRESSION_UNCOMPRESSED = 1;
    public static final int TIFF_COMPRESSION_UNCOMPRESSED_1 = 1;
    public static final int TIFF_COMPRESSION_UNCOMPRESSED_2 = 32771;
    public static final int TIFF_DIRECTORY_FOOTER_LENGTH = 4;
    public static final int TIFF_DIRECTORY_HEADER_LENGTH = 2;
    public static final int TIFF_ENTRY_LENGTH = 12;
    public static final int TIFF_ENTRY_MAX_VALUE_LENGTH = 4;
    public static final int TIFF_FLAG_T4_OPTIONS_2D = 1;
    public static final int TIFF_FLAG_T4_OPTIONS_FILL = 4;
    public static final int TIFF_FLAG_T4_OPTIONS_UNCOMPRESSED_MODE = 2;
    public static final int TIFF_FLAG_T6_OPTIONS_UNCOMPRESSED_MODE = 2;
    public static final int TIFF_HEADER_SIZE = 8;
    public static final int TIFF_LZW_COMPRESSION_BLOCK_SIZE_LARGE = 65536;
    public static final int TIFF_LZW_COMPRESSION_BLOCK_SIZE_MEDIUM = 32768;

    private TiffConstants() {
    }
}
