// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

public enum RectangleColor
{
    private static final RectangleColor[] $VALUES;
    
    AF_FAIL, 
    AF_SUCCESS, 
    NORMAL, 
    RECORDING;
    
    static {
        $VALUES = new RectangleColor[] { RectangleColor.NORMAL, RectangleColor.AF_SUCCESS, RectangleColor.AF_FAIL, RectangleColor.RECORDING };
    }
}
