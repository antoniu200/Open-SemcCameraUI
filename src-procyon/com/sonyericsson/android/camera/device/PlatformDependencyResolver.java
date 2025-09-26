// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.util.Size;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.ResolutionDependence;
import android.content.Context;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import java.util.List;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;

public class PlatformDependencyResolver extends CommonPlatformDependencyResolver
{
    public static final String TAG = "PlatformDependencyResolver";
    
    private static boolean checkAspectRatio(final Rect rect, final int n, final int n2) {
        return rect.width() * n2 == rect.height() * n;
    }
    
    public static String getDefaultFocusModeForFastCapturePhoto(final CameraParameters cameraParameters, final CameraInfo.CameraId cameraId) {
        final List<String> supportedFocusModes = PlatformCapability.getSupportedFocusModes(cameraId);
        if (supportedFocusModes != null) {
            if (supportedFocusModes.contains("continuous-picture")) {
                return "continuous-picture";
            }
            if (supportedFocusModes.contains("auto")) {
                return "auto";
            }
        }
        return cameraParameters.getFocusMode();
    }
    
    public static VideoSize getDefaultVideoSize(final CameraInfo.CameraId cameraId) {
        final List<VideoConfiguration> supportedVideoConfiguration = PlatformCapability.getSupportedVideoConfiguration(cameraId);
        int n = 0;
        int n2 = 0;
        int n5;
        int n6;
        if (supportedVideoConfiguration != null) {
            final Iterator<VideoConfiguration> iterator = supportedVideoConfiguration.iterator();
            int n3 = 0;
            int n4 = 0;
            while (true) {
                n = n2;
                n5 = n3;
                n6 = n4;
                if (!iterator.hasNext()) {
                    break;
                }
                final VideoConfiguration videoConfiguration = iterator.next();
                int n7 = n2;
                if (videoConfiguration.mWidth == 1920) {
                    n7 = n2;
                    if (videoConfiguration.mHeight == 1080) {
                        n7 = 1;
                    }
                }
                int n8 = n3;
                if (videoConfiguration.mWidth == 1280) {
                    n8 = n3;
                    if (videoConfiguration.mHeight == 720) {
                        n8 = 1;
                    }
                }
                n2 = n7;
                n3 = n8;
                if (videoConfiguration.mWidth != 640) {
                    continue;
                }
                n2 = n7;
                n3 = n8;
                if (videoConfiguration.mHeight != 480) {
                    continue;
                }
                n4 = 1;
                n2 = n7;
                n3 = n8;
            }
        }
        else {
            n5 = 0;
            n6 = 0;
        }
        if (n != 0) {
            return VideoSize.FULL_HD;
        }
        if (n5 != 0) {
            return VideoSize.HD;
        }
        if (n6 != 0) {
            return VideoSize.VGA;
        }
        return null;
    }
    
    public static int getMaxPictureWidth(final Context context, final List<Rect> list) {
        final boolean dependOnAspect = ResolutionDependence.isDependOnAspect(context);
        int n = 0;
        int n2 = 0;
        if (list != null) {
            final Iterator<Rect> iterator = list.iterator();
            int n3 = 0;
            while (true) {
                n = n2;
                if (!iterator.hasNext()) {
                    break;
                }
                final Rect rect = iterator.next();
                if (dependOnAspect) {
                    final int n4 = rect.width() * rect.height();
                    if (n3 >= n4) {
                        continue;
                    }
                    n2 = rect.width();
                    n3 = n4;
                }
                else {
                    if (n2 >= rect.width()) {
                        continue;
                    }
                    n2 = rect.width();
                }
            }
        }
        return n;
    }
    
    public static Rect getOptimalPreviewSize(final CameraInfo.CameraId cameraId, final int n, final Rect rect) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("E: Base rect(");
            sb.append(rect.width());
            sb.append(" x ");
            sb.append(rect.height());
            sb.append(")");
            CamLog.d(sb.toString());
        }
        final List<Rect> supportedPreviewSizes = PlatformCapability.getSupportedPreviewSizes(cameraId);
        if (n == 2) {
            final Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
            if (preferredPreviewSizeForVideo.width() != 0) {
                final Rect preferredPreviewSizeFromCaptureSize = preferredPreviewSizeForVideo;
                if (preferredPreviewSizeForVideo.height() != 0) {
                    return CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(rect, preferredPreviewSizeFromCaptureSize, supportedPreviewSizes);
                }
            }
            final Rect preferredPreviewSizeFromCaptureSize = getPreferredPreviewSizeFromCaptureSize(rect);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("preferredPreviewSize is invalid. Get preferredPreviewSize from videoSize: ");
            sb2.append(preferredPreviewSizeFromCaptureSize);
            CamLog.w(sb2.toString());
            return CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(rect, preferredPreviewSizeFromCaptureSize, supportedPreviewSizes);
        }
        Rect rect2;
        if ((rect2 = PlatformCapability.getPreferredPreviewSizeForStill(cameraId)) == null) {
            rect2 = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
        }
        if (rect2.width() != 0) {
            final Rect preferredPreviewSizeFromCaptureSize2 = rect2;
            if (rect2.height() != 0) {
                return CommonPlatformDependencyResolver.getOptimalStillPreviewRect(rect, preferredPreviewSizeFromCaptureSize2, supportedPreviewSizes);
            }
        }
        final Rect preferredPreviewSizeFromCaptureSize2 = getPreferredPreviewSizeFromCaptureSize(rect);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("preferredPreviewSize is invalid. Get preferredPreviewSize from captureSize: ");
        sb3.append(preferredPreviewSizeFromCaptureSize2);
        CamLog.w(sb3.toString());
        return CommonPlatformDependencyResolver.getOptimalStillPreviewRect(rect, preferredPreviewSizeFromCaptureSize2, supportedPreviewSizes);
    }
    
    public static Rect getPreferredPreviewSizeFromCaptureSize(final Rect rect) {
        final Size surfaceSize = getSurfaceSize(rect, false);
        return new Rect(0, 0, surfaceSize.getWidth(), surfaceSize.getHeight());
    }
    
    public static Size getSurfaceSize(final Rect rect, final boolean b) {
        if (checkAspectRatio(rect, 16, 9)) {
            if (b) {
                CamLog.d("getSurfaceSize: video HDR enable, return 1080");
                return new Size(1920, 1080);
            }
            return new Size(1280, 720);
        }
        else {
            if (checkAspectRatio(rect, 4, 3)) {
                return new Size(960, 720);
            }
            if (checkAspectRatio(rect, 1, 1)) {
                return new Size(720, 720);
            }
            if (checkAspectRatio(rect, 11, 9)) {
                return new Size(176, 144);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The specified preview size is not supported. (");
            sb.append(rect.width());
            sb.append("x");
            sb.append(rect.height());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
    }
}
