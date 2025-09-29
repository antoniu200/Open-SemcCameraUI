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

    public ResolutionOptions(Context ctx, String modelCode, List<Rect> maxRects) {
        // Aspect dependency used in several branches
        final boolean dependOnAspect = ResolutionDependence.isDependOnAspect(ctx);

        // These will be set in each branch
        String[] resolutionOptions;
        String[] superiorAutoOptions;
        String[] videoSizeOptions;
        String   defaultResolution;
        String   defaultVideoSize;

        // Grouped exactly as in the fallback
        switch (modelCode) {
            case "SOI20BS0":
            case "SOI20BS2": {
                final int resArr      = 0x7f030001; // 2130903041
                final int supArr      = 0x7f030007; // 2130903047
                final int videoArr    = is4KVideoSizeSupported(ctx) ? 0x7f03000c : 0x7f03000f; // 2130903052/3055
                final int defResStr   = dependOnAspect ? 0x7f0f0352 : 0x7f0f035f;              // 2131690322/0335
                final int defVideoStr = 0x7f0f0362;                                           // 2131690338
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            case "SOS20FW0":
            case "SOI20BSA": {
                final int resArr      = 0x7f030002; // 2130903042
                final int supArr      = 0x7f030008; // 2130903048
                final int videoArr    = is4KVideoSizeSupported(ctx) ? 0x7f03000d : 0x7f030010; // 2130903053/3056
                final int defResStr   = dependOnAspect ? 0x7f0f0353 : 0x7f0f0360;              // 2131690323/0336
                final int defVideoStr = 0x7f0f0363;                                           // 2131690339
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            case "SOI13BS1":
            case "SEM13BS1": {
                final int resArr      = 0x7f030000; // 2130903040
                final int supArr      = 0x7f030006; // 2130903046
                final int videoArr    = 0x7f03000e; // 2130903054
                final int defResStr   = 0x7f0f0351; // 2131690321
                final int defVideoStr = 0x7f0f0361; // 2131690337
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            case "LGI08BS0":
            case "CHI08BS0": {
                final int resArr      = 0x7f030005; // 2130903045
                final int supArr      = 0x7f03000b; // 2130903051
                final int videoArr    = 0x7f030014; // 2130903060
                final int defResStr   = 0x7f0f0356; // 2131690326
                final int defVideoStr = 0x7f0f0368; // 2131690344
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            case "LGI05BN1":
            case "CHI05BN1": {
                final int resArr      = 0x7f030003; // 2130903043
                final int supArr      = 0x7f030009; // 2130903049
                final int videoArr    = 0x7f030012; // 2130903058
                final int defResStr   = 0x7f0f0354; // 2131690324
                final int defVideoStr = 0x7f0f0366; // 2131690342
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            case "LGI13BS0":
            case "MTM13BS0": {
                final int resArr      = 0x7f030004; // 2130903044
                final int supArr      = 0x7f03000a; // 2130903050
                final int videoArr    = 0x7f030013; // 2130903059
                final int defResStr   = 0x7f0f0355; // 2131690325
                final int defVideoStr = 0x7f0f0367; // 2131690343
                resolutionOptions     = ctx.getResources().getStringArray(resArr);
                superiorAutoOptions   = ctx.getResources().getStringArray(supArr);
                videoSizeOptions      = ctx.getResources().getStringArray(videoArr);
                defaultResolution     = ctx.getResources().getString(defResStr);
                defaultVideoSize      = ctx.getResources().getString(defVideoStr);
                break;
            }

            default: {
                // Use device-reported max sizes
                final String[] max = getMaxResolutions(maxRects);
                resolutionOptions   = max;
                superiorAutoOptions = max;

                // Default resolution selection
                if (max.length == 2) {
                    defaultResolution = dependOnAspect ? max[0] : max[1];
                } else {
                    defaultResolution = max.length > 0 ? max[0] : "";
                }

                // Generic video arrays / defaults
                videoSizeOptions = ctx.getResources().getStringArray(0x7f030011); // 2130903057
                defaultVideoSize = ctx.getResources().getString(0x7f0f0364);      // 2131690340
                break;
            }
        }

        // Commit to final fields
        this.mResolutionOptions           = resolutionOptions;
        this.mSuperiorAutoResolutionOptions = superiorAutoOptions;
        this.mVideoSizeOptions            = videoSizeOptions;
        this.mDefaultResolution           = defaultResolution;
        this.mDefaultVideoSize            = defaultVideoSize;
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
