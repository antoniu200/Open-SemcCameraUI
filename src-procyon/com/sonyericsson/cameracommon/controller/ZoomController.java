// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.controller;

public class ZoomController
{
    private static final float PINCH_ZOOM_COEFFICIENT = 0.2f;
    
    public static float getZoomValue(final float n, final float n2) {
        return n + n2 * 0.2f;
    }
}
