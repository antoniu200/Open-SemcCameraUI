package com.sonyericsson.android.camera.parameter.dependency;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DependencyCheckUtil {
    public static boolean isIntelligentActiveAvailable(@NonNull CameraInfo.CameraId cameraId, @NonNull VideoSize videoSize, @NonNull VideoHdr videoHdr) {
        if (videoHdr == VideoHdr.HDR_ON) {
            return false;
        }
        return VideoStabilizer.isIntelligentActiveSupported(cameraId, videoSize);
    }

    public static boolean isFaceDetectionAvailable(@NonNull CapturingMode capturingMode, @NonNull VideoSize videoSize, @NonNull VideoHdr videoHdr) {
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            return false;
        }
        if (capturingMode == CapturingMode.VIDEO || capturingMode == CapturingMode.FRONT_VIDEO) {
            return (videoSize.is4KVideo() || videoHdr == VideoHdr.HDR_ON) ? false : true;
        }
        return true;
    }

    public static boolean isFusionAvailableOnStill(@NonNull CameraInfo.CameraId cameraId, @NonNull Resolution resolution, @NonNull Hdr hdr) {
        if (hdr == Hdr.HDR_ON) {
            return false;
        }
        return PlatformCapability.isFusionSupportedWith(cameraId, resolution);
    }

    public static boolean isFusionAvailableOnVideo(@NonNull CameraInfo.CameraId cameraId, @NonNull VideoSize videoSize, @NonNull VideoHdr videoHdr) {
        if (videoHdr == VideoHdr.HDR_ON) {
            return false;
        }
        return PlatformCapability.isFusionSupportedWith(cameraId, videoSize);
    }
}
