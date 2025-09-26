// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.research.parameters;

public enum Screen
{
    private static final Screen[] $VALUES;
    
    APPS_UI, 
    CREATIVE_EFFECT, 
    FACE_IN, 
    FOUR_K_VIDEO, 
    MANUAL_FRONT, 
    MANUAL_MAIN, 
    MULTI_CAMERA, 
    PANORAMA, 
    SLOW_MOTION, 
    SOUND_PHOTO, 
    SUPERIOR_AUTO_FRONT, 
    SUPERIOR_AUTO_MAIN, 
    TIMESHIFT_BURST, 
    TIMESHIFT_VIDEO, 
    VIDEO_FRONT, 
    VIDEO_MAIN;
    
    static {
        $VALUES = new Screen[] { Screen.SUPERIOR_AUTO_MAIN, Screen.SUPERIOR_AUTO_FRONT, Screen.MANUAL_MAIN, Screen.MANUAL_FRONT, Screen.VIDEO_MAIN, Screen.VIDEO_FRONT, Screen.SLOW_MOTION, Screen.PANORAMA, Screen.TIMESHIFT_BURST, Screen.FOUR_K_VIDEO, Screen.TIMESHIFT_VIDEO, Screen.CREATIVE_EFFECT, Screen.SOUND_PHOTO, Screen.MULTI_CAMERA, Screen.FACE_IN, Screen.APPS_UI };
    }
}
