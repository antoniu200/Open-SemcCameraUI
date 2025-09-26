// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.imaging.common.BinaryConstant;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoUndefined;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoFloat;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

public final class MicrosoftHdPhotoTagConstants
{
    public static final List<TagInfo> ALL_MICROSOFT_HD_PHOTO_TAGS;
    public static final int ALPHA_DATA_DISCARD_VALUE_FLEXBITS_DISCARDED = 1;
    public static final int ALPHA_DATA_DISCARD_VALUE_FULL_RESOLUTION = 0;
    public static final int ALPHA_DATA_DISCARD_VALUE_HIGHPASS_AND_LOW_PASS_FREQUENCY_DATA_DISCARDED = 3;
    public static final int ALPHA_DATA_DISCARD_VALUE_HIGH_PASS_FREQUENCY_DATA_DISCARDED = 2;
    public static final TagInfoLong EXIF_TAG_ALPHA_BYTE_COUNT;
    public static final TagInfoByte EXIF_TAG_ALPHA_DATA_DISCARD;
    public static final TagInfoLong EXIF_TAG_ALPHA_OFFSET;
    public static final TagInfoFloat EXIF_TAG_HEIGHT_RESOLUTION;
    public static final TagInfoLong EXIF_TAG_IMAGE_BYTE_COUNT;
    public static final TagInfoByte EXIF_TAG_IMAGE_DATA_DISCARD;
    public static final TagInfoLong EXIF_TAG_IMAGE_HEIGHT;
    public static final TagInfoLong EXIF_TAG_IMAGE_OFFSET;
    public static final TagInfoLong EXIF_TAG_IMAGE_TYPE;
    public static final TagInfoLong EXIF_TAG_IMAGE_WIDTH;
    public static final TagInfoUndefined EXIF_TAG_PADDING;
    public static final TagInfoByte EXIF_TAG_PIXEL_FORMAT;
    public static final TagInfoLong EXIF_TAG_TRANSFOMATION;
    public static final TagInfoLong EXIF_TAG_UNCOMPRESSED;
    public static final TagInfoFloat EXIF_TAG_WIDTH_RESOLUTION;
    public static final int IMAGE_DATA_DISCARD_VALUE_FLEXBITS_DISCARDED = 1;
    public static final int IMAGE_DATA_DISCARD_VALUE_FULL_RESOLUTION = 0;
    public static final int IMAGE_DATA_DISCARD_VALUE_HIGHPASS_AND_LOW_PASS_FREQUENCY_DATA_DISCARDED = 3;
    public static final int IMAGE_DATA_DISCARD_VALUE_HIGH_PASS_FREQUENCY_DATA_DISCARDED = 2;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_112_BIT_6_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_112_BIT_7_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_7_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_8_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_PRGBA_FLOAT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_RGBA_FIXED_POINT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_RGBA_FLOAT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_128_BIT_RGB_FLOAT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_144_BIT_8_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_16_BIT_BGR555;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_16_BIT_BGR565;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_16_BIT_GRAY;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_24_BIT_3_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_24_BIT_BGR;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_24_BIT_RGB;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_3_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_4_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_BGR;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_BGR101010;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_BGRA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_CMYK;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_GRAY_FIXED_POINT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_GRAY_FLOAT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_PBGRA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_32_BIT_RGBE;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_40_BIT_4_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_40_BIT_5_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_40_BIT_CMYK_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_3_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_5_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_6_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_RGB;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_RGB_FIXED_POINT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_48_BIT_RGB_HALF;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_56_BIT_6_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_56_BIT_7_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_3_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_4_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_7_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_8_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_CMYK;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_PRGBA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_RGBA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_RGBA_FIXED_POINT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_64_BIT_RGBA_HALF;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_72_BIT_8_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_80_BIT_4_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_80_BIT_5_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_80_BIT_CMYK_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_8_BIT_GRAY;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_96_BIT_5_CHANNELS_ALPHA;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_96_BIT_6_CHANNELS;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_96_BIT_RGB_FIXED_POINT;
    public static final BinaryConstant PIXEL_FORMAT_VALUE_BLACK_AND_WHITE;
    public static final int TRANSFOMATION_VALUE_HORIZONTAL_NORMAL = 0;
    public static final int TRANSFOMATION_VALUE_MIRROR_HORIZONTAL = 2;
    public static final int TRANSFOMATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_270_CW = 6;
    public static final int TRANSFOMATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_90_CW = 5;
    public static final int TRANSFOMATION_VALUE_MIRROR_VERTICAL = 1;
    public static final int TRANSFOMATION_VALUE_ROTATE_180 = 3;
    public static final int TRANSFOMATION_VALUE_ROTATE_270_CW = 7;
    public static final int TRANSFOMATION_VALUE_ROTATE_90_CW = 4;
    public static final int UNCOMPRESSED_VALUE_NO = 0;
    public static final int UNCOMPRESSED_VALUE_YES = 1;
    
    static {
        EXIF_TAG_PIXEL_FORMAT = new TagInfoByte("PixelFormat", 48129, 16, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        PIXEL_FORMAT_VALUE_BLACK_AND_WHITE = createMicrosoftHdPhotoGuidEndingWith((byte)5);
        PIXEL_FORMAT_VALUE_8_BIT_GRAY = createMicrosoftHdPhotoGuidEndingWith((byte)8);
        PIXEL_FORMAT_VALUE_16_BIT_BGR555 = createMicrosoftHdPhotoGuidEndingWith((byte)9);
        PIXEL_FORMAT_VALUE_16_BIT_BGR565 = createMicrosoftHdPhotoGuidEndingWith((byte)10);
        PIXEL_FORMAT_VALUE_16_BIT_GRAY = createMicrosoftHdPhotoGuidEndingWith((byte)11);
        PIXEL_FORMAT_VALUE_24_BIT_BGR = createMicrosoftHdPhotoGuidEndingWith((byte)12);
        PIXEL_FORMAT_VALUE_24_BIT_RGB = createMicrosoftHdPhotoGuidEndingWith((byte)13);
        PIXEL_FORMAT_VALUE_32_BIT_BGR = createMicrosoftHdPhotoGuidEndingWith((byte)14);
        PIXEL_FORMAT_VALUE_32_BIT_BGRA = createMicrosoftHdPhotoGuidEndingWith((byte)15);
        PIXEL_FORMAT_VALUE_32_BIT_PBGRA = createMicrosoftHdPhotoGuidEndingWith((byte)16);
        PIXEL_FORMAT_VALUE_32_BIT_GRAY_FLOAT = createMicrosoftHdPhotoGuidEndingWith((byte)17);
        PIXEL_FORMAT_VALUE_48_BIT_RGB_FIXED_POINT = createMicrosoftHdPhotoGuidEndingWith((byte)18);
        PIXEL_FORMAT_VALUE_32_BIT_BGR101010 = createMicrosoftHdPhotoGuidEndingWith((byte)19);
        PIXEL_FORMAT_VALUE_48_BIT_RGB = createMicrosoftHdPhotoGuidEndingWith((byte)21);
        PIXEL_FORMAT_VALUE_64_BIT_RGBA = createMicrosoftHdPhotoGuidEndingWith((byte)22);
        PIXEL_FORMAT_VALUE_64_BIT_PRGBA = createMicrosoftHdPhotoGuidEndingWith((byte)23);
        PIXEL_FORMAT_VALUE_96_BIT_RGB_FIXED_POINT = createMicrosoftHdPhotoGuidEndingWith((byte)24);
        PIXEL_FORMAT_VALUE_128_BIT_RGBA_FLOAT = createMicrosoftHdPhotoGuidEndingWith((byte)25);
        PIXEL_FORMAT_VALUE_128_BIT_PRGBA_FLOAT = createMicrosoftHdPhotoGuidEndingWith((byte)26);
        PIXEL_FORMAT_VALUE_128_BIT_RGB_FLOAT = createMicrosoftHdPhotoGuidEndingWith((byte)27);
        PIXEL_FORMAT_VALUE_32_BIT_CMYK = createMicrosoftHdPhotoGuidEndingWith((byte)28);
        PIXEL_FORMAT_VALUE_64_BIT_RGBA_FIXED_POINT = createMicrosoftHdPhotoGuidEndingWith((byte)29);
        PIXEL_FORMAT_VALUE_128_BIT_RGBA_FIXED_POINT = createMicrosoftHdPhotoGuidEndingWith((byte)30);
        PIXEL_FORMAT_VALUE_64_BIT_CMYK = createMicrosoftHdPhotoGuidEndingWith((byte)31);
        PIXEL_FORMAT_VALUE_24_BIT_3_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)32);
        PIXEL_FORMAT_VALUE_32_BIT_4_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)33);
        PIXEL_FORMAT_VALUE_40_BIT_5_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)34);
        PIXEL_FORMAT_VALUE_48_BIT_6_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)35);
        PIXEL_FORMAT_VALUE_56_BIT_7_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)36);
        PIXEL_FORMAT_VALUE_64_BIT_8_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)37);
        PIXEL_FORMAT_VALUE_48_BIT_3_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)38);
        PIXEL_FORMAT_VALUE_64_BIT_4_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)39);
        PIXEL_FORMAT_VALUE_80_BIT_5_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)40);
        PIXEL_FORMAT_VALUE_96_BIT_6_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)41);
        PIXEL_FORMAT_VALUE_112_BIT_7_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)42);
        PIXEL_FORMAT_VALUE_128_BIT_8_CHANNELS = createMicrosoftHdPhotoGuidEndingWith((byte)43);
        PIXEL_FORMAT_VALUE_40_BIT_CMYK_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)44);
        PIXEL_FORMAT_VALUE_80_BIT_CMYK_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)45);
        PIXEL_FORMAT_VALUE_32_BIT_3_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)46);
        PIXEL_FORMAT_VALUE_40_BIT_4_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)47);
        PIXEL_FORMAT_VALUE_48_BIT_5_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)48);
        PIXEL_FORMAT_VALUE_56_BIT_6_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)49);
        PIXEL_FORMAT_VALUE_64_BIT_7_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)50);
        PIXEL_FORMAT_VALUE_72_BIT_8_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)51);
        PIXEL_FORMAT_VALUE_64_BIT_3_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)52);
        PIXEL_FORMAT_VALUE_80_BIT_4_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)53);
        PIXEL_FORMAT_VALUE_96_BIT_5_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)54);
        PIXEL_FORMAT_VALUE_112_BIT_6_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)55);
        PIXEL_FORMAT_VALUE_128_BIT_7_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)56);
        PIXEL_FORMAT_VALUE_144_BIT_8_CHANNELS_ALPHA = createMicrosoftHdPhotoGuidEndingWith((byte)57);
        PIXEL_FORMAT_VALUE_64_BIT_RGBA_HALF = createMicrosoftHdPhotoGuidEndingWith((byte)58);
        PIXEL_FORMAT_VALUE_48_BIT_RGB_HALF = createMicrosoftHdPhotoGuidEndingWith((byte)59);
        PIXEL_FORMAT_VALUE_32_BIT_RGBE = createMicrosoftHdPhotoGuidEndingWith((byte)61);
        PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF = createMicrosoftHdPhotoGuidEndingWith((byte)62);
        PIXEL_FORMAT_VALUE_32_BIT_GRAY_FIXED_POINT = createMicrosoftHdPhotoGuidEndingWith((byte)63);
        EXIF_TAG_TRANSFOMATION = new TagInfoLong("Transfomation", 48130, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_UNCOMPRESSED = new TagInfoLong("Uncompressed", 48131, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_TYPE = new TagInfoLong("ImageType", 48132, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_WIDTH = new TagInfoLong("ImageWidth", 48256, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_HEIGHT = new TagInfoLong("ImageHeight", 48257, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_WIDTH_RESOLUTION = new TagInfoFloat("WidthResolution", 48258, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_HEIGHT_RESOLUTION = new TagInfoFloat("HeightResolution", 48259, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_OFFSET = new TagInfoLong("ImageOffset", 48320, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_BYTE_COUNT = new TagInfoLong("ImageByteCount", 48321, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_ALPHA_OFFSET = new TagInfoLong("AlphaOffset", 48322, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_ALPHA_BYTE_COUNT = new TagInfoLong("AlphaByteCount", 48323, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_IMAGE_DATA_DISCARD = new TagInfoByte("ImageDataDiscard", 48324, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_ALPHA_DATA_DISCARD = new TagInfoByte("AlphaDataDiscard", 48325, 1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        EXIF_TAG_PADDING = new TagInfoUndefined("Padding", 59932, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
        ALL_MICROSOFT_HD_PHOTO_TAGS = Collections.unmodifiableList((List<? extends TagInfo>)Arrays.asList(MicrosoftHdPhotoTagConstants.EXIF_TAG_PIXEL_FORMAT, MicrosoftHdPhotoTagConstants.EXIF_TAG_TRANSFOMATION, MicrosoftHdPhotoTagConstants.EXIF_TAG_UNCOMPRESSED, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_TYPE, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_WIDTH, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_HEIGHT, MicrosoftHdPhotoTagConstants.EXIF_TAG_WIDTH_RESOLUTION, MicrosoftHdPhotoTagConstants.EXIF_TAG_HEIGHT_RESOLUTION, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_OFFSET, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_BYTE_COUNT, MicrosoftHdPhotoTagConstants.EXIF_TAG_ALPHA_OFFSET, MicrosoftHdPhotoTagConstants.EXIF_TAG_ALPHA_BYTE_COUNT, MicrosoftHdPhotoTagConstants.EXIF_TAG_IMAGE_DATA_DISCARD, MicrosoftHdPhotoTagConstants.EXIF_TAG_ALPHA_DATA_DISCARD, MicrosoftHdPhotoTagConstants.EXIF_TAG_PADDING));
    }
    
    private MicrosoftHdPhotoTagConstants() {
    }
    
    private static BinaryConstant createMicrosoftHdPhotoGuidEndingWith(final byte b) {
        return new BinaryConstant(new byte[] { 36, -61, -35, 111, 3, 78, -2, 75, -79, -123, 61, 119, 118, -115, -55, b });
    }
}
