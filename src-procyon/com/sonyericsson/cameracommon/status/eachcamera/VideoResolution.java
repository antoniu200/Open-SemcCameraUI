// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import android.graphics.Rect;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;

public class VideoResolution extends ResolutionValue implements EachCameraStatusValue
{
    public static final Rect DEFAULT_VALUE;
    public static final String KEY = "video_resolution";
    private static int REQUIRED_PROVIDER_VERSION = 1;
    
    static {
        DEFAULT_VALUE = new Rect(0, 0, 0, 0);
    }
    
    public VideoResolution(final int n, final int n2) {
        super(n, n2);
    }
    
    public VideoResolution(final Rect rect) {
        super(rect);
    }
    
    @Override
    public String getKey() {
        return "video_resolution";
    }
    
    @Override
    public int minRequiredVersion() {
        return VideoResolution.REQUIRED_PROVIDER_VERSION;
    }
}
