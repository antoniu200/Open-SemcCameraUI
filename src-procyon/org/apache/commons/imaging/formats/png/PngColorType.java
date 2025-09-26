// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import java.util.Arrays;

public enum PngColorType
{
    private static final PngColorType[] $VALUES;
    
    GREYSCALE(0, true, false, 1, new int[] { 1, 2, 4, 8, 16 }), 
    GREYSCALE_WITH_ALPHA(4, true, true, 2, new int[] { 8, 16 }), 
    INDEXED_COLOR(3, false, false, 1, new int[] { 1, 2, 4, 8 }), 
    TRUE_COLOR(2, false, false, 3, new int[] { 8, 16 }), 
    TRUE_COLOR_WITH_ALPHA(6, false, true, 4, new int[] { 8, 16 });
    
    private final int[] allowedBitDepths;
    private final boolean alpha;
    private final boolean greyscale;
    private final int samplesPerPixel;
    private final int value;
    
    static {
        $VALUES = new PngColorType[] { PngColorType.GREYSCALE, PngColorType.TRUE_COLOR, PngColorType.INDEXED_COLOR, PngColorType.GREYSCALE_WITH_ALPHA, PngColorType.TRUE_COLOR_WITH_ALPHA };
    }
    
    private PngColorType(final int value, final boolean greyscale, final boolean alpha, final int samplesPerPixel, final int[] allowedBitDepths) {
        this.value = value;
        this.greyscale = greyscale;
        this.alpha = alpha;
        this.samplesPerPixel = samplesPerPixel;
        this.allowedBitDepths = allowedBitDepths;
    }
    
    public static PngColorType getColorType(final int n) {
        for (final PngColorType pngColorType : values()) {
            if (pngColorType.value == n) {
                return pngColorType;
            }
        }
        return null;
    }
    
    static PngColorType getColorType(final boolean b, final boolean b2) {
        if (b2) {
            if (b) {
                return PngColorType.GREYSCALE_WITH_ALPHA;
            }
            return PngColorType.GREYSCALE;
        }
        else {
            if (b) {
                return PngColorType.TRUE_COLOR_WITH_ALPHA;
            }
            return PngColorType.TRUE_COLOR;
        }
    }
    
    int getSamplesPerPixel() {
        return this.samplesPerPixel;
    }
    
    int getValue() {
        return this.value;
    }
    
    boolean hasAlpha() {
        return this.alpha;
    }
    
    boolean isBitDepthAllowed(final int key) {
        return Arrays.binarySearch(this.allowedBitDepths, key) >= 0;
    }
    
    boolean isGreyscale() {
        return this.greyscale;
    }
}
