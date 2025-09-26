// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;

public enum BaseLayoutPattern implements LayoutPattern
{
    private static final BaseLayoutPattern[] $VALUES;
    
    BURST_SHOOTING, 
    CAPTURE, 
    CLEAR, 
    FOCUS_DONE, 
    FOCUS_SEARCHING, 
    HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION, 
    MODE_CHANGING, 
    OVERLAY_CONTROL_SEEKING, 
    PAUSE_RECORDING, 
    PREVIEW, 
    PREVIEW_NO_RECORDING, 
    RECORDING, 
    SELFTIMER, 
    SETTING, 
    ZOOMING, 
    ZOOMING_IN_PAUSE_RECORDING, 
    ZOOMING_IN_RECORDING;
    
    static {
        $VALUES = new BaseLayoutPattern[] { BaseLayoutPattern.PREVIEW, BaseLayoutPattern.PREVIEW_NO_RECORDING, BaseLayoutPattern.MODE_CHANGING, BaseLayoutPattern.CLEAR, BaseLayoutPattern.ZOOMING, BaseLayoutPattern.ZOOMING_IN_RECORDING, BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING, BaseLayoutPattern.FOCUS_SEARCHING, BaseLayoutPattern.FOCUS_DONE, BaseLayoutPattern.CAPTURE, BaseLayoutPattern.BURST_SHOOTING, BaseLayoutPattern.RECORDING, BaseLayoutPattern.SETTING, BaseLayoutPattern.SELFTIMER, BaseLayoutPattern.PAUSE_RECORDING, BaseLayoutPattern.OVERLAY_CONTROL_SEEKING, BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION };
    }
}
