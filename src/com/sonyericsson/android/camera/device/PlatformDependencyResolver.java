package com.sonyericsson.android.camera.device;

import android.content.Context;
import android.graphics.Rect;
import android.util.Size;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.capability.ResolutionDependence;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.device.SizeConstants;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PlatformDependencyResolver extends CommonPlatformDependencyResolver {
    public static final String TAG = "PlatformDependencyResolver";

    public static int getMaxPictureWidth(Context context, List<Rect> list) {
        boolean zIsDependOnAspect = ResolutionDependence.isDependOnAspect(context);
        int iWidth = 0;
        if (list != null) {
            int i = 0;
            for (Rect rect : list) {
                if (zIsDependOnAspect) {
                    int iWidth2 = rect.width() * rect.height();
                    if (i < iWidth2) {
                        iWidth = rect.width();
                        i = iWidth2;
                    }
                } else if (iWidth < rect.width()) {
                    iWidth = rect.width();
                }
            }
        }
        return iWidth;
    }

    public static VideoSize getDefaultVideoSize(CameraInfo.CameraId cameraId) {
        boolean z;
        boolean z2;
        List<VideoConfiguration> supportedVideoConfiguration = PlatformCapability.getSupportedVideoConfiguration(cameraId);
        boolean z3 = false;
        if (supportedVideoConfiguration != null) {
            z = false;
            z2 = false;
            for (VideoConfiguration videoConfiguration : supportedVideoConfiguration) {
                if (videoConfiguration.mWidth == 1920 && videoConfiguration.mHeight == 1080) {
                    z3 = true;
                }
                if (videoConfiguration.mWidth == 1280 && videoConfiguration.mHeight == 720) {
                    z = true;
                }
                if (videoConfiguration.mWidth == 640 && videoConfiguration.mHeight == 480) {
                    z2 = true;
                }
            }
        } else {
            z = false;
            z2 = false;
        }
        if (z3) {
            return VideoSize.FULL_HD;
        }
        if (z) {
            return VideoSize.HD;
        }
        if (z2) {
            return VideoSize.VGA;
        }
        return null;
    }

    public static String getDefaultFocusModeForFastCapturePhoto(CameraParameters cameraParameters, CameraInfo.CameraId cameraId) {
        List<String> supportedFocusModes = PlatformCapability.getSupportedFocusModes(cameraId);
        if (supportedFocusModes != null) {
            if (supportedFocusModes.contains(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                return CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE;
            }
            if (supportedFocusModes.contains("auto")) {
                return "auto";
            }
        }
        return cameraParameters.getFocusMode();
    }

    public static Rect getOptimalPreviewSize(CameraInfo.CameraId cameraId, int i, Rect rect) {
        if (CamLog.VERBOSE) {
            CamLog.d("E: Base rect(" + rect.width() + " x " + rect.height() + ")");
        }
        List<Rect> supportedPreviewSizes = PlatformCapability.getSupportedPreviewSizes(cameraId);
        if (i == 2) {
            Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
            if (preferredPreviewSizeForVideo.width() == 0 || preferredPreviewSizeForVideo.height() == 0) {
                preferredPreviewSizeForVideo = getPreferredPreviewSizeFromCaptureSize(rect);
                CamLog.w("preferredPreviewSize is invalid. Get preferredPreviewSize from videoSize: " + preferredPreviewSizeForVideo);
            }
            return getOptimalVideoPreviewRect(rect, preferredPreviewSizeForVideo, supportedPreviewSizes);
        }
        Rect preferredPreviewSizeForStill = PlatformCapability.getPreferredPreviewSizeForStill(cameraId);
        if (preferredPreviewSizeForStill == null) {
            preferredPreviewSizeForStill = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
        }
        if (preferredPreviewSizeForStill.width() == 0 || preferredPreviewSizeForStill.height() == 0) {
            preferredPreviewSizeForStill = getPreferredPreviewSizeFromCaptureSize(rect);
            CamLog.w("preferredPreviewSize is invalid. Get preferredPreviewSize from captureSize: " + preferredPreviewSizeForStill);
        }
        return getOptimalStillPreviewRect(rect, preferredPreviewSizeForStill, supportedPreviewSizes);
    }

    public static Size getSurfaceSize(Rect rect, boolean z) {
        if (checkAspectRatio(rect, 16, 9)) {
            if (z) {
                CamLog.d("getSurfaceSize: video HDR enable, return 1080");
                return new Size(SizeConstants.WIDTH_PREVIEW_FULL_HD, SizeConstants.HEIGHT_PREVIEW_FULL_HD);
            }
            return new Size(SizeConstants.WIDTH_PREVIEW_HD, SizeConstants.HEIGHT_PREVIEW_HD);
        }
        if (checkAspectRatio(rect, 4, 3)) {
            return new Size(960, SizeConstants.HEIGHT_PREVIEW_HD);
        }
        if (checkAspectRatio(rect, 1, 1)) {
            return new Size(SizeConstants.HEIGHT_PREVIEW_HD, SizeConstants.HEIGHT_PREVIEW_HD);
        }
        if (checkAspectRatio(rect, 11, 9)) {
            return new Size(176, 144);
        }
        throw new RuntimeException("The specified preview size is not supported. (" + rect.width() + SharedPrefsTranslator.CONNECTOR_CROSS + rect.height() + ")");
    }

    private static boolean checkAspectRatio(Rect rect, int i, int i2) {
        return rect.width() * i2 == rect.height() * i;
    }

    public static Rect getPreferredPreviewSizeFromCaptureSize(Rect rect) {
        Size surfaceSize = getSurfaceSize(rect, false);
        return new Rect(0, 0, surfaceSize.getWidth(), surfaceSize.getHeight());
    }
}
