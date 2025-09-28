package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.graphics.Rect;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolutionOptions {
    public static final String TAG = "ResolutionOptions";
    private final String mDefaultResolution;
    private final String mDefaultVideoSize;
    private final String[] mResolutionOptions;
    private final String[] mSuperiorAutoResolutionOptions;
    private final String[] mVideoSizeOptions;

    public ResolutionOptions() {
        this.mResolutionOptions = new String[0];
        this.mSuperiorAutoResolutionOptions = new String[0];
        this.mVideoSizeOptions = new String[0];
        this.mDefaultResolution = "";
        this.mDefaultVideoSize = "";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public ResolutionOptions(android.content.Context r7, java.lang.String r8, java.util.List<android.graphics.Rect> r9) {
        /*
            Method dump skipped, instructions count: 516
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.util.capability.ResolutionOptions.<init>(android.content.Context, java.lang.String, java.util.List):void");
    }

    public String[] getResolutionOptions() {
        return (String[]) this.mResolutionOptions.clone();
    }

    public String[] getSuperiorAutoResolutionOptions() {
        return (String[]) this.mSuperiorAutoResolutionOptions.clone();
    }

    public String[] getVideoSizeOptions() {
        return (String[]) this.mVideoSizeOptions.clone();
    }

    public String getDefaultResolution() {
        return this.mDefaultResolution;
    }

    public String getDefaultVideoSize() {
        return this.mDefaultVideoSize;
    }

    private boolean is4KVideoSizeSupported(Context context) {
        return context.getResources().getBoolean(R.bool.enable_4k_videosize);
    }

    private String[] getMaxResolutions(List<Rect> list) {
        Resolution resolutionFindResolution;
        Resolution resolution = null;
        Resolution resolution2 = null;
        for (Rect rect : list) {
            if (isAspectRatio4_3(rect)) {
                Resolution resolutionFindResolution2 = findResolution(rect);
                if (resolutionFindResolution2 != null && (resolution == null || rect.width() * rect.height() > resolution.getPictureRect().width() * resolution.getPictureRect().height())) {
                    resolution = resolutionFindResolution2;
                }
            } else if (isAspectRatio16_9(rect) && (resolutionFindResolution = findResolution(rect)) != null && (resolution2 == null || rect.width() * rect.height() > resolution2.getPictureRect().width() * resolution2.getPictureRect().height())) {
                resolution2 = resolutionFindResolution;
            }
        }
        if (resolution != null && resolution2 != null) {
            return new String[]{resolution.getValue(), resolution2.getValue()};
        }
        if (resolution != null) {
            return new String[]{resolution.getValue()};
        }
        if (resolution2 != null) {
            return new String[]{resolution2.getValue()};
        }
        return new String[]{Resolution.VGA.getValue()};
    }

    private Resolution findResolution(Rect rect) {
        for (Resolution resolution : Resolution.values()) {
            if (resolution.getPictureRect().width() == rect.width() && resolution.getPictureRect().height() == rect.height()) {
                return resolution;
            }
        }
        return null;
    }

    private static boolean isAspectRatio16_9(Rect rect) {
        return (rect == null || rect.width() == 0 || rect.height() == 0 || rect.width() * 9 != rect.height() * 16) ? false : true;
    }

    private static boolean isAspectRatio4_3(Rect rect) {
        return (rect == null || rect.width() == 0 || rect.height() == 0 || rect.width() * 3 != rect.height() * 4) ? false : true;
    }
}
