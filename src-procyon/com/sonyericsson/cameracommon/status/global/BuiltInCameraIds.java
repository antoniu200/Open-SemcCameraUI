// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.global;

import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;
import com.sonyericsson.cameracommon.status.CameraIdArrayValue;

public class BuiltInCameraIds extends CameraIdArrayValue implements GlobalCameraStatusValue
{
    public static final CameraInfo.CameraId[] DEFAULT_VALUE;
    public static final String KEY = "built_in_camera_ids";
    private static int REQUIRED_PROVIDER_VERSION = 10;
    
    static {
        DEFAULT_VALUE = new CameraInfo.CameraId[0];
    }
    
    public BuiltInCameraIds(final CameraInfo.CameraId... array) {
        super(array);
    }
    
    @Override
    public String getKey() {
        return "built_in_camera_ids";
    }
    
    @Override
    public int minRequiredVersion() {
        return BuiltInCameraIds.REQUIRED_PROVIDER_VERSION;
    }
}
