// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public class DependencyCheckUtil
{
    public static boolean isFaceDetectionAvailable(@NonNull final CapturingMode capturingMode, @NonNull final VideoSize videoSize, @NonNull final VideoHdr videoHdr) {
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            return false;
        }
        if (capturingMode == CapturingMode.VIDEO || capturingMode == CapturingMode.FRONT_VIDEO) {
            if (videoSize.is4KVideo()) {
                return false;
            }
            if (videoHdr == VideoHdr.HDR_ON) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isFusionAvailableOnStill(@NonNull final CameraInfo.CameraId cameraId, @NonNull final Resolution resolution, @NonNull final Hdr hdr) {
        return hdr != Hdr.HDR_ON && PlatformCapability.isFusionSupportedWith(cameraId, resolution);
    }
    
    public static boolean isFusionAvailableOnVideo(@NonNull final CameraInfo.CameraId cameraId, @NonNull final VideoSize videoSize, @NonNull final VideoHdr videoHdr) {
        return videoHdr != VideoHdr.HDR_ON && PlatformCapability.isFusionSupportedWith(cameraId, videoSize);
    }
    
    public static boolean isIntelligentActiveAvailable(@NonNull final CameraInfo.CameraId cameraId, @NonNull final VideoSize videoSize, @NonNull final VideoHdr videoHdr) {
        return videoHdr != VideoHdr.HDR_ON && VideoStabilizer.isIntelligentActiveSupported(cameraId, videoSize);
    }
}
