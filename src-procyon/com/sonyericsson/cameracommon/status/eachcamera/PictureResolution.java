// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import android.graphics.Rect;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;

public class PictureResolution extends ResolutionValue implements EachCameraStatusValue
{
    public static final Rect DEFAULT_VALUE;
    public static final String KEY = "picture_resolution";
    private static int REQUIRED_PROVIDER_VERSION = 1;
    
    static {
        DEFAULT_VALUE = new Rect(0, 0, 0, 0);
    }
    
    public PictureResolution(final Rect rect) {
        super(rect);
    }
    
    @Override
    public String getKey() {
        return "picture_resolution";
    }
    
    @Override
    public int minRequiredVersion() {
        return PictureResolution.REQUIRED_PROVIDER_VERSION;
    }
}
