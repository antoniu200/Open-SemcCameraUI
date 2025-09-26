// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.icns;

import java.io.UnsupportedEncodingException;

enum IcnsType
{
    private static final IcnsType[] $VALUES;
    private static final IcnsType[] ALL_IMAGE_TYPES;
    private static final IcnsType[] ALL_MASK_TYPES;
    
    ICNS_128x128_32BIT_IMAGE("it32", 128, 128, 32, false), 
    ICNS_128x128_8BIT_MASK("t8mk", 128, 128, 8, true), 
    ICNS_16x12_1BIT_IMAGE_AND_MASK("icm#", 16, 12, 1, true), 
    ICNS_16x12_4BIT_IMAGE("icm4", 16, 12, 4, false), 
    ICNS_16x12_8BIT_IMAGE("icm8", 16, 12, 8, false), 
    ICNS_16x16_1BIT_IMAGE_AND_MASK("ics#", 16, 16, 1, true), 
    ICNS_16x16_32BIT_IMAGE("is32", 16, 16, 32, false), 
    ICNS_16x16_4BIT_IMAGE("ics4", 16, 16, 4, false), 
    ICNS_16x16_8BIT_IMAGE("ics8", 16, 16, 8, false), 
    ICNS_16x16_8BIT_MASK("s8mk", 16, 16, 8, true), 
    ICNS_256x256_32BIT_ARGB_IMAGE("ic08", 256, 256, 32, false), 
    ICNS_32x32_1BIT_IMAGE_AND_MASK("ICN#", 32, 32, 1, true), 
    ICNS_32x32_32BIT_IMAGE("il32", 32, 32, 32, false), 
    ICNS_32x32_4BIT_IMAGE("icl4", 32, 32, 4, false), 
    ICNS_32x32_8BIT_IMAGE("icl8", 32, 32, 8, false), 
    ICNS_32x32_8BIT_MASK("l8mk", 32, 32, 8, true), 
    ICNS_48x48_1BIT_IMAGE_AND_MASK("ich#", 48, 48, 1, true), 
    ICNS_48x48_32BIT_IMAGE("ih32", 48, 48, 32, false), 
    ICNS_48x48_4BIT_IMAGE("ich4", 48, 48, 4, false), 
    ICNS_48x48_8BIT_IMAGE("ich8", 48, 48, 8, false), 
    ICNS_48x48_8BIT_MASK("h8mk", 48, 48, 8, true), 
    ICNS_512x512_32BIT_ARGB_IMAGE("ic09", 512, 512, 32, false);
    
    private final int bitsPerPixel;
    private final boolean hasMask;
    private final int height;
    private final int type;
    private final int width;
    
    static {
        $VALUES = new IcnsType[] { IcnsType.ICNS_16x12_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x12_4BIT_IMAGE, IcnsType.ICNS_16x12_8BIT_IMAGE, IcnsType.ICNS_16x16_8BIT_MASK, IcnsType.ICNS_16x16_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x16_4BIT_IMAGE, IcnsType.ICNS_16x16_8BIT_IMAGE, IcnsType.ICNS_16x16_32BIT_IMAGE, IcnsType.ICNS_32x32_8BIT_MASK, IcnsType.ICNS_32x32_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_32x32_4BIT_IMAGE, IcnsType.ICNS_32x32_8BIT_IMAGE, IcnsType.ICNS_32x32_32BIT_IMAGE, IcnsType.ICNS_48x48_8BIT_MASK, IcnsType.ICNS_48x48_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_48x48_4BIT_IMAGE, IcnsType.ICNS_48x48_8BIT_IMAGE, IcnsType.ICNS_48x48_32BIT_IMAGE, IcnsType.ICNS_128x128_8BIT_MASK, IcnsType.ICNS_128x128_32BIT_IMAGE, IcnsType.ICNS_256x256_32BIT_ARGB_IMAGE, IcnsType.ICNS_512x512_32BIT_ARGB_IMAGE };
        ALL_IMAGE_TYPES = new IcnsType[] { IcnsType.ICNS_16x12_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x12_4BIT_IMAGE, IcnsType.ICNS_16x12_8BIT_IMAGE, IcnsType.ICNS_16x16_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x16_4BIT_IMAGE, IcnsType.ICNS_16x16_8BIT_IMAGE, IcnsType.ICNS_16x16_32BIT_IMAGE, IcnsType.ICNS_32x32_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_32x32_4BIT_IMAGE, IcnsType.ICNS_32x32_8BIT_IMAGE, IcnsType.ICNS_32x32_32BIT_IMAGE, IcnsType.ICNS_48x48_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_48x48_4BIT_IMAGE, IcnsType.ICNS_48x48_8BIT_IMAGE, IcnsType.ICNS_48x48_32BIT_IMAGE, IcnsType.ICNS_128x128_32BIT_IMAGE, IcnsType.ICNS_256x256_32BIT_ARGB_IMAGE, IcnsType.ICNS_512x512_32BIT_ARGB_IMAGE };
        ALL_MASK_TYPES = new IcnsType[] { IcnsType.ICNS_16x12_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x16_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_16x16_8BIT_MASK, IcnsType.ICNS_32x32_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_32x32_8BIT_MASK, IcnsType.ICNS_48x48_1BIT_IMAGE_AND_MASK, IcnsType.ICNS_48x48_8BIT_MASK, IcnsType.ICNS_128x128_8BIT_MASK };
    }
    
    private IcnsType(final String s, final int width, final int height, final int bitsPerPixel, final boolean hasMask) {
        this.type = typeAsInt(s);
        this.width = width;
        this.height = height;
        this.bitsPerPixel = bitsPerPixel;
        this.hasMask = hasMask;
    }
    
    public static String describeType(final int n) {
        final byte b = (byte)(n >> 24 & 0xFF);
        final byte b2 = (byte)(n >> 16 & 0xFF);
        final byte b3 = (byte)(n >> 8 & 0xFF);
        final byte b4 = (byte)(n & 0xFF);
        try {
            return new String(new byte[] { b, b2, b3, b4 }, "US-ASCII");
        }
        catch (final UnsupportedEncodingException cause) {
            throw new IllegalArgumentException("Your Java doesn't support US-ASCII", cause);
        }
    }
    
    public static IcnsType find1BPPMaskType(final IcnsType icnsType) {
        for (final IcnsType icnsType2 : IcnsType.ALL_MASK_TYPES) {
            if (icnsType2.getBitsPerPixel() == 1 && icnsType2.getWidth() == icnsType.getWidth() && icnsType2.getHeight() == icnsType.getHeight()) {
                return icnsType2;
            }
        }
        return null;
    }
    
    public static IcnsType find8BPPMaskType(final IcnsType icnsType) {
        for (final IcnsType icnsType2 : IcnsType.ALL_MASK_TYPES) {
            if (icnsType2.getBitsPerPixel() == 8 && icnsType2.getWidth() == icnsType.getWidth() && icnsType2.getHeight() == icnsType.getHeight()) {
                return icnsType2;
            }
        }
        return null;
    }
    
    public static IcnsType findAnyType(final int n) {
        final IcnsType[] all_IMAGE_TYPES = IcnsType.ALL_IMAGE_TYPES;
        final int length = all_IMAGE_TYPES.length;
        final int n2 = 0;
        for (final IcnsType icnsType : all_IMAGE_TYPES) {
            if (icnsType.getType() == n) {
                return icnsType;
            }
        }
        final IcnsType[] all_MASK_TYPES = IcnsType.ALL_MASK_TYPES;
        for (int length2 = all_MASK_TYPES.length, j = n2; j < length2; ++j) {
            final IcnsType icnsType2 = all_MASK_TYPES[j];
            if (icnsType2.getType() == n) {
                return icnsType2;
            }
        }
        return null;
    }
    
    public static IcnsType findImageType(final int n) {
        for (final IcnsType icnsType : IcnsType.ALL_IMAGE_TYPES) {
            if (icnsType.getType() == n) {
                return icnsType;
            }
        }
        return null;
    }
    
    public static int typeAsInt(final String s) {
        try {
            final byte[] bytes = s.getBytes("US-ASCII");
            if (bytes.length != 4) {
                throw new IllegalArgumentException("Invalid ICNS type");
            }
            return (bytes[3] & 0xFF) | ((bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8);
        }
        catch (final UnsupportedEncodingException cause) {
            throw new IllegalArgumentException("Your Java doesn't support US-ASCII", cause);
        }
    }
    
    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean hasMask() {
        return this.hasMask;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append("[");
        sb.append("width=");
        sb.append(this.width);
        sb.append(",");
        sb.append("height=");
        sb.append(this.height);
        sb.append(",");
        sb.append("bpp=");
        sb.append(this.bitsPerPixel);
        sb.append(",");
        sb.append("hasMask=");
        sb.append(this.hasMask);
        sb.append("]");
        return sb.toString();
    }
}
