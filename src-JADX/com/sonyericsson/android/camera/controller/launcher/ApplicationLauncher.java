package com.sonyericsson.android.camera.controller.launcher;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.ExternalCameraAppSetting;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.controller.album.AlbumLauncher;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.SignatureUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ApplicationLauncher {
    private static String ACTION_EDIT_HIGH_FRAME_RATE = "com.sonymobile.moviecreator.intent.action.TIMESHIFT_VIDEO_EDITOR";
    public static final int BOKEH_AND_MONOCHROME = 3;
    public static final int BOKEH_ONLY = 1;
    private static final String DUAL_EFFECT_CLASS = "com.sonymobile.addoncamera.dualcameraeffect.ui.CameraActivity";
    private static final String DUAL_EFFECT_LAUNCH_MODE_BOKEH = "bokeh";
    private static final String DUAL_EFFECT_LAUNCH_MODE_CAMERA = "camera";
    private static final String DUAL_EFFECT_LAUNCH_MODE_KEY = "effect_mode";
    private static final String DUAL_EFFECT_LAUNCH_MODE_VIDEO = "video";
    private static final String DUAL_EFFECT_META_DATA = "com.sonymobile.addoncamera.dualcameraeffect.support_feature";
    private static final String DUAL_EFFECT_PACKAGE = "com.sonymobile.addoncamera.dualcameraeffect";
    public static final int MONOCHROME_ONLY = 2;
    private static final String PORTRAIT_SELFIE_CLASS = "com.arcsoft.camera.CameraActivity";
    private static final String PORTRAIT_SELFIE_PACKAGE = "com.sonymobile.addoncamera.portraitselfie";
    private static final String PORTRAIT_SELFIE_WIDE_ZOOM_TARGET_RATIO = "com.sonyericsson.android.camera.extra.WIDE_ZOOM_TARGET_RATIO";
    public static final String TAG = "ApplicationLauncher";

    public enum MonochromeType {
        MONOCHROME_PHOTO,
        MONOCHROME_VIDEO
    }

    private ApplicationLauncher() {
    }

    public static void launchAlbum(Activity activity, String str, Uri uri, int i, int i2) {
        AlbumLauncher.launchAlbum(activity, uri, str, i, i2 == 2);
    }

    public static void launchAlbum(Activity activity, String str, Uri uri, int i, int i2, boolean z) {
        AlbumLauncher.launchAlbum(activity, uri, str, i, i2 == 2, z);
    }

    public static void launchLocationSourceSettings(Activity activity) {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
            try {
                activity.startActivity(intent);
                if (CamLog.VERBOSE) {
                    CamLog.d(TAG, "launchLocationSourceSettings: " + intent);
                }
            } catch (ActivityNotFoundException e) {
                CamLog.e("launchLocationSourceSettings: failed.", e);
            }
        }
    }

    public static void launchSideSenseSettings(Activity activity) {
        Intent intent = new Intent("com.sonymobile.sidesenseapp.action.LAUNCH_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
            try {
                activity.startActivity(intent);
                if (CamLog.VERBOSE) {
                    CamLog.d(TAG, "launchSideSenseSettings: " + intent);
                }
            } catch (ActivityNotFoundException e) {
                CamLog.e("launchSideSenseSettings: failed.", e);
            }
        }
    }

    public static boolean isEditorAvailable(Context context, Uri uri, String str) {
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setDataAndType(uri, str);
        intent.setFlags(1);
        return CommonUtility.isActivityAvailable(context, intent);
    }

    public static boolean launchVideoEditor(Activity activity, StoreDataResult storeDataResult) {
        try {
            Uri uri = storeDataResult.uri;
            SavingRequest savingRequest = storeDataResult.savingRequest;
            String str = savingRequest != null ? savingRequest.common.mimeType : null;
            if (!MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME.equals(str) || !isEditorAvailable(activity, uri, savingRequest.common.mimeType)) {
                return false;
            }
            ActivityOptions activityOptionsMakeCustomAnimation = ActivityOptions.makeCustomAnimation(activity, R.anim.edit_activity_fade_in, R.anim.edit_activity_fade_out);
            Intent intent = new Intent(ACTION_EDIT_HIGH_FRAME_RATE);
            intent.setDataAndType(uri, str);
            intent.setFlags(3);
            if (!CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
                return false;
            }
            activity.startActivityForResult(intent, 14, activityOptionsMakeCustomAnimation.toBundle());
            return true;
        } catch (ActivityNotFoundException e) {
            CamLog.e("openReviewWindow: failed.", e);
            return false;
        }
    }

    private static Intent getDualCameraEffectIntent(int i, CapturingMode capturingMode) {
        Intent intent = new Intent();
        intent.setClassName(DUAL_EFFECT_PACKAGE, DUAL_EFFECT_CLASS);
        if (i == 16) {
            intent.putExtra(DUAL_EFFECT_LAUNCH_MODE_KEY, DUAL_EFFECT_LAUNCH_MODE_BOKEH);
        } else if (i == 17) {
            if (capturingMode == CapturingMode.VIDEO) {
                intent.putExtra(DUAL_EFFECT_LAUNCH_MODE_KEY, DUAL_EFFECT_LAUNCH_MODE_VIDEO);
            } else {
                intent.putExtra(DUAL_EFFECT_LAUNCH_MODE_KEY, DUAL_EFFECT_LAUNCH_MODE_CAMERA);
            }
        }
        return intent;
    }

    private static Intent getPortraitSelfieIntent(CameraActivity cameraActivity, UserSettings userSettings) {
        Intent intent = new Intent();
        intent.setClassName(PORTRAIT_SELFIE_PACKAGE, PORTRAIT_SELFIE_CLASS);
        if (PlatformCapability.isPrepared()) {
            intent.putExtra(PORTRAIT_SELFIE_WIDE_ZOOM_TARGET_RATIO, PlatformCapability.getWideZoomTargetRatio(CameraInfo.CameraId.FRONT));
        } else {
            CamLog.i("Platform capability is not prepared. Set 1.0 as default to PORTRAIT_SELFIE_WIDE_ZOOM_TARGET_RATIO");
            intent.putExtra(PORTRAIT_SELFIE_WIDE_ZOOM_TARGET_RATIO, 1.0f);
        }
        return intent;
    }

    public static boolean isPortraitSelfieAvailable(Context context) {
        Intent intent = new Intent();
        intent.setClassName(PORTRAIT_SELFIE_PACKAGE, PORTRAIT_SELFIE_CLASS);
        if (context.getPackageManager().resolveActivity(intent, 65536) == null) {
            return false;
        }
        return SignatureUtil.isAvailable(context, PORTRAIT_SELFIE_PACKAGE);
    }

    public static boolean isBokehSupported() {
        if (PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK)) {
            try {
                int i = CameraApplication.getContext().getPackageManager().getActivityInfo(new ComponentName(DUAL_EFFECT_PACKAGE, DUAL_EFFECT_CLASS), 128).metaData.getInt(DUAL_EFFECT_META_DATA, 0);
                if (i == 1 || i == 3) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                CamLog.e(TAG, "DualEffect Component : com.sonymobile.addoncamera.dualcameraeffect Not Found");
            }
        }
        return false;
    }

    public static boolean isMonochromeSupported() {
        if (PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK)) {
            try {
                switch (CameraApplication.getContext().getPackageManager().getActivityInfo(new ComponentName(DUAL_EFFECT_PACKAGE, DUAL_EFFECT_CLASS), 128).metaData.getInt(DUAL_EFFECT_META_DATA, 0)) {
                }
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                CamLog.e(TAG, "DualEffect Component : com.sonymobile.addoncamera.dualcameraeffect Not Found");
            }
        }
        return false;
    }

    public static void launchExternalCamera(CameraActivity cameraActivity, int i, UserSettings userSettings, CapturingMode capturingMode, boolean z) {
        ExternalCameraAppSetting.ShareSettingCategory shareSettingCategory;
        Intent dualCameraEffectIntent;
        Object intentValue;
        HashMap map = new HashMap();
        map.put(ExternalCameraAppSetting.DATA_STORAGE.intentKey, "internal");
        switch (i) {
            case 16:
                shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                dualCameraEffectIntent = getDualCameraEffectIntent(i, capturingMode);
                break;
            case 17:
                dualCameraEffectIntent = getDualCameraEffectIntent(i, capturingMode);
                if (capturingMode == CapturingMode.VIDEO) {
                    shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.VIDEO;
                    break;
                } else {
                    shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                    break;
                }
            case 18:
                shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                dualCameraEffectIntent = getPortraitSelfieIntent(cameraActivity, userSettings);
                break;
            default:
                throw new RuntimeException("The request code '" + i + "' is incorrect");
        }
        for (ExternalCameraAppSetting externalCameraAppSetting : ExternalCameraAppSetting.values()) {
            if (!map.containsKey(externalCameraAppSetting.intentKey) && externalCameraAppSetting.isShared(shareSettingCategory)) {
                if (i == 16 || i == 17) {
                    intentValue = externalCameraAppSetting.toIntentValue(userSettings.get(capturingMode, externalCameraAppSetting.key));
                } else {
                    intentValue = externalCameraAppSetting.toIntentValue(userSettings.get(externalCameraAppSetting.key));
                }
                if (intentValue.getClass().equals(Boolean.class)) {
                    dualCameraEffectIntent.putExtra(externalCameraAppSetting.intentKey, ((Boolean) intentValue).booleanValue());
                } else if (intentValue.getClass().equals(String.class)) {
                    dualCameraEffectIntent.putExtra(externalCameraAppSetting.intentKey, (String) intentValue);
                } else {
                    throw new RuntimeException("One of the external app values was neither a boolean nor a String. It was a " + intentValue.getClass() + ".");
                }
            }
        }
        if (!map.isEmpty()) {
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue().getClass().equals(Boolean.class)) {
                    dualCameraEffectIntent.putExtra((String) entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
                } else if (entry.getValue().getClass().equals(String.class)) {
                    dualCameraEffectIntent.putExtra((String) entry.getKey(), (String) entry.getValue());
                } else {
                    throw new RuntimeException("One of the Force settings values was neither a boolean nor a String. It was a " + entry.getClass() + ".");
                }
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("Launch external camera application");
            CamLog.d("  action:" + dualCameraEffectIntent.getAction());
            CamLog.d("  component:" + dualCameraEffectIntent.getComponent());
            CamLog.d("  extra:");
            for (String str : dualCameraEffectIntent.getExtras().keySet()) {
                CamLog.d("    " + str + ":" + dualCameraEffectIntent.getExtras().get(str));
            }
        }
        if (z) {
            cameraActivity.startActivityForResult(dualCameraEffectIntent, i);
        } else {
            cameraActivity.startActivity(dualCameraEffectIntent);
        }
        cameraActivity.overridePendingTransition(0, 0);
    }
}
