package org.apache.commons.imaging.formats.icns;

import java.io.UnsupportedEncodingException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
enum IcnsType {
    ICNS_16x12_1BIT_IMAGE_AND_MASK("icm#", 16, 12, 1, true),
    ICNS_16x12_4BIT_IMAGE("icm4", 16, 12, 4, false),
    ICNS_16x12_8BIT_IMAGE("icm8", 16, 12, 8, false),
    ICNS_16x16_8BIT_MASK("s8mk", 16, 16, 8, true),
    ICNS_16x16_1BIT_IMAGE_AND_MASK("ics#", 16, 16, 1, true),
    ICNS_16x16_4BIT_IMAGE("ics4", 16, 16, 4, false),
    ICNS_16x16_8BIT_IMAGE("ics8", 16, 16, 8, false),
    ICNS_16x16_32BIT_IMAGE("is32", 16, 16, 32, false),
    ICNS_32x32_8BIT_MASK("l8mk", 32, 32, 8, true),
    ICNS_32x32_1BIT_IMAGE_AND_MASK("ICN#", 32, 32, 1, true),
    ICNS_32x32_4BIT_IMAGE("icl4", 32, 32, 4, false),
    ICNS_32x32_8BIT_IMAGE("icl8", 32, 32, 8, false),
    ICNS_32x32_32BIT_IMAGE("il32", 32, 32, 32, false),
    ICNS_48x48_8BIT_MASK("h8mk", 48, 48, 8, true),
    ICNS_48x48_1BIT_IMAGE_AND_MASK("ich#", 48, 48, 1, true),
    ICNS_48x48_4BIT_IMAGE("ich4", 48, 48, 4, false),
    ICNS_48x48_8BIT_IMAGE("ich8", 48, 48, 8, false),
    ICNS_48x48_32BIT_IMAGE("ih32", 48, 48, 32, false),
    ICNS_128x128_8BIT_MASK("t8mk", 128, 128, 8, true),
    ICNS_128x128_32BIT_IMAGE("it32", 128, 128, 32, false),
    ICNS_256x256_32BIT_ARGB_IMAGE("ic08", 256, 256, 32, false),
    ICNS_512x512_32BIT_ARGB_IMAGE("ic09", 512, 512, 32, false);

    private final int bitsPerPixel;
    private final boolean hasMask;
    private final int height;
    private final int type;
    private final int width;
    private static final IcnsType[] ALL_IMAGE_TYPES = {ICNS_16x12_1BIT_IMAGE_AND_MASK, ICNS_16x12_4BIT_IMAGE, ICNS_16x12_8BIT_IMAGE, ICNS_16x16_1BIT_IMAGE_AND_MASK, ICNS_16x16_4BIT_IMAGE, ICNS_16x16_8BIT_IMAGE, ICNS_16x16_32BIT_IMAGE, ICNS_32x32_1BIT_IMAGE_AND_MASK, ICNS_32x32_4BIT_IMAGE, ICNS_32x32_8BIT_IMAGE, ICNS_32x32_32BIT_IMAGE, ICNS_48x48_1BIT_IMAGE_AND_MASK, ICNS_48x48_4BIT_IMAGE, ICNS_48x48_8BIT_IMAGE, ICNS_48x48_32BIT_IMAGE, ICNS_128x128_32BIT_IMAGE, ICNS_256x256_32BIT_ARGB_IMAGE, ICNS_512x512_32BIT_ARGB_IMAGE};
    private static final IcnsType[] ALL_MASK_TYPES = {ICNS_16x12_1BIT_IMAGE_AND_MASK, ICNS_16x16_1BIT_IMAGE_AND_MASK, ICNS_16x16_8BIT_MASK, ICNS_32x32_1BIT_IMAGE_AND_MASK, ICNS_32x32_8BIT_MASK, ICNS_48x48_1BIT_IMAGE_AND_MASK, ICNS_48x48_8BIT_MASK, ICNS_128x128_8BIT_MASK};

    IcnsType(String str, int i, int i2, int i3, boolean z) {
        this.type = typeAsInt(str);
        this.width = i;
        this.height = i2;
        this.bitsPerPixel = i3;
        this.hasMask = z;
    }

    public int getType() {
        return this.type;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public boolean hasMask() {
        return this.hasMask;
    }

    @Override // java.lang.Enum
    public String toString() {
        return getClass().getName() + "[width=" + this.width + ",height=" + this.height + ",bpp=" + this.bitsPerPixel + ",hasMask=" + this.hasMask + "]";
    }

    public static IcnsType findAnyType(int i) {
        for (IcnsType icnsType : ALL_IMAGE_TYPES) {
            if (icnsType.getType() == i) {
                return icnsType;
            }
        }
        for (IcnsType icnsType2 : ALL_MASK_TYPES) {
            if (icnsType2.getType() == i) {
                return icnsType2;
            }
        }
        return null;
    }

    public static IcnsType findImageType(int i) {
        for (IcnsType icnsType : ALL_IMAGE_TYPES) {
            if (icnsType.getType() == i) {
                return icnsType;
            }
        }
        return null;
    }

    public static IcnsType find8BPPMaskType(IcnsType icnsType) {
        for (IcnsType icnsType2 : ALL_MASK_TYPES) {
            if (icnsType2.getBitsPerPixel() == 8 && icnsType2.getWidth() == icnsType.getWidth() && icnsType2.getHeight() == icnsType.getHeight()) {
                return icnsType2;
            }
        }
        return null;
    }

    public static IcnsType find1BPPMaskType(IcnsType icnsType) {
        for (IcnsType icnsType2 : ALL_MASK_TYPES) {
            if (icnsType2.getBitsPerPixel() == 1 && icnsType2.getWidth() == icnsType.getWidth() && icnsType2.getHeight() == icnsType.getHeight()) {
                return icnsType2;
            }
        }
        return null;
    }

    public static int typeAsInt(String str) throws UnsupportedEncodingException {
        try {
            byte[] bytes = str.getBytes("US-ASCII");
            if (bytes.length != 4) {
                throw new IllegalArgumentException("Invalid ICNS type");
            }
            return (bytes[3] & 255) | ((bytes[0] & 255) << 24) | ((bytes[1] & 255) << 16) | ((bytes[2] & 255) << 8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Your Java doesn't support US-ASCII", e);
        }
    }

    public static String describeType(int i) {
        try {
            return new String(new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)}, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Your Java doesn't support US-ASCII", e);
        }
    }
}
