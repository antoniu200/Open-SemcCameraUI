// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import android.graphics.Rect;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;

public class PreviewResolution extends ResolutionValue implements EachCameraStatusValue
{
    public static final Rect DEFAULT_VALUE;
    public static final String KEY = "preview_resolution";
    private static int REQUIRED_PROVIDER_VERSION = 1;
    
    static {
        DEFAULT_VALUE = new Rect(0, 0, 0, 0);
    }
    
    public PreviewResolution(final Rect rect) {
        super(rect);
    }
    
    @Override
    public String getKey() {
        return "preview_resolution";
    }
    
    @Override
    public int minRequiredVersion() {
        return PreviewResolution.REQUIRED_PROVIDER_VERSION;
    }
}
