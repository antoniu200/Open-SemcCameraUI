// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder;

public enum DefaultLayoutPattern implements LayoutPattern
{
    private static final DefaultLayoutPattern[] $VALUES;
    
    BURST_SHOOTING, 
    CAPTURE, 
    CLEAR, 
    FOCUS_DONE, 
    FOCUS_SEARCHING, 
    MODE_SELECTOR, 
    PAUSE_RECORDING, 
    PREVIEW, 
    RECORDING, 
    SELFTIMER, 
    SEMIAUTO_SEEKING, 
    SETTING, 
    ZOOMING;
    
    static {
        $VALUES = new DefaultLayoutPattern[] { DefaultLayoutPattern.PREVIEW, DefaultLayoutPattern.CLEAR, DefaultLayoutPattern.ZOOMING, DefaultLayoutPattern.FOCUS_SEARCHING, DefaultLayoutPattern.FOCUS_DONE, DefaultLayoutPattern.CAPTURE, DefaultLayoutPattern.BURST_SHOOTING, DefaultLayoutPattern.RECORDING, DefaultLayoutPattern.MODE_SELECTOR, DefaultLayoutPattern.SETTING, DefaultLayoutPattern.SELFTIMER, DefaultLayoutPattern.PAUSE_RECORDING, DefaultLayoutPattern.SEMIAUTO_SEEKING };
    }
}
