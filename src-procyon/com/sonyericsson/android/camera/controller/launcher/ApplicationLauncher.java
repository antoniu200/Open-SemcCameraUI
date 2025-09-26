// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller.launcher;

import com.sonyericsson.cameracommon.storage.SavingRequest;
import android.app.ActivityOptions;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import android.content.ActivityNotFoundException;
import java.util.Iterator;
import java.util.Map;
import com.sonyericsson.android.camera.ExternalCameraAppSetting;
import java.util.HashMap;
import com.sonyericsson.android.camera.controller.album.AlbumLauncher;
import android.app.Activity;
import com.sonyericsson.android.camera.util.SignatureUtil;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.net.Uri;
import android.content.Context;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.ComponentName;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.CameraActivity;
import android.content.Intent;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public final class ApplicationLauncher
{
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
    
    private ApplicationLauncher() {
    }
    
    private static Intent getDualCameraEffectIntent(final int n, final CapturingMode capturingMode) {
        final Intent intent = new Intent();
        intent.setClassName("com.sonymobile.addoncamera.dualcameraeffect", "com.sonymobile.addoncamera.dualcameraeffect.ui.CameraActivity");
        if (n == 16) {
            intent.putExtra("effect_mode", "bokeh");
        }
        else if (n == 17) {
            if (capturingMode == CapturingMode.VIDEO) {
                intent.putExtra("effect_mode", "video");
            }
            else {
                intent.putExtra("effect_mode", "camera");
            }
        }
        return intent;
    }
    
    private static Intent getPortraitSelfieIntent(final CameraActivity cameraActivity, final UserSettings userSettings) {
        final Intent intent = new Intent();
        intent.setClassName("com.sonymobile.addoncamera.portraitselfie", "com.arcsoft.camera.CameraActivity");
        if (PlatformCapability.isPrepared()) {
            intent.putExtra("com.sonyericsson.android.camera.extra.WIDE_ZOOM_TARGET_RATIO", PlatformCapability.getWideZoomTargetRatio(CameraInfo.CameraId.FRONT));
        }
        else {
            CamLog.i("Platform capability is not prepared. Set 1.0 as default to PORTRAIT_SELFIE_WIDE_ZOOM_TARGET_RATIO");
            intent.putExtra("com.sonyericsson.android.camera.extra.WIDE_ZOOM_TARGET_RATIO", 1.0f);
        }
        return intent;
    }
    
    public static boolean isBokehSupported() {
        if (PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK)) {
            try {
                final int int1 = CameraApplication.getContext().getPackageManager().getActivityInfo(new ComponentName("com.sonymobile.addoncamera.dualcameraeffect", "com.sonymobile.addoncamera.dualcameraeffect.ui.CameraActivity"), 128).metaData.getInt("com.sonymobile.addoncamera.dualcameraeffect.support_feature", 0);
                if (int1 == 1) {
                    return true;
                }
                if (int1 == 3) {
                    return true;
                }
            }
            catch (final PackageManager$NameNotFoundException ex) {
                CamLog.e("ApplicationLauncher", "DualEffect Component : com.sonymobile.addoncamera.dualcameraeffect Not Found");
            }
        }
        return false;
    }
    
    public static boolean isEditorAvailable(final Context context, final Uri uri, final String s) {
        final Intent intent = new Intent("android.intent.action.EDIT");
        intent.setDataAndType(uri, s);
        intent.setFlags(1);
        return CommonUtility.isActivityAvailable(context, intent);
    }
    
    public static boolean isMonochromeSupported() {
        if (PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK)) {
            try {
                switch (CameraApplication.getContext().getPackageManager().getActivityInfo(new ComponentName("com.sonymobile.addoncamera.dualcameraeffect", "com.sonymobile.addoncamera.dualcameraeffect.ui.CameraActivity"), 128).metaData.getInt("com.sonymobile.addoncamera.dualcameraeffect.support_feature", 0)) {
                    case 3: {
                        return true;
                    }
                    case 2: {
                        return true;
                    }
                }
            }
            catch (final PackageManager$NameNotFoundException ex) {
                CamLog.e("ApplicationLauncher", "DualEffect Component : com.sonymobile.addoncamera.dualcameraeffect Not Found");
            }
        }
        return false;
    }
    
    public static boolean isPortraitSelfieAvailable(final Context context) {
        final Intent intent = new Intent();
        intent.setClassName("com.sonymobile.addoncamera.portraitselfie", "com.arcsoft.camera.CameraActivity");
        return context.getPackageManager().resolveActivity(intent, 65536) != null && SignatureUtil.isAvailable(context, "com.sonymobile.addoncamera.portraitselfie");
    }
    
    public static void launchAlbum(final Activity activity, final String s, final Uri uri, final int n, final int n2) {
        AlbumLauncher.launchAlbum(activity, uri, s, n, n2 == 2);
    }
    
    public static void launchAlbum(final Activity activity, final String s, final Uri uri, final int n, final int n2, final boolean b) {
        AlbumLauncher.launchAlbum(activity, uri, s, n, n2 == 2, b);
    }
    
    public static void launchExternalCamera(final CameraActivity cameraActivity, final int i, final UserSettings userSettings, final CapturingMode capturingMode, final boolean b) {
        final HashMap hashMap = new HashMap();
        hashMap.put(ExternalCameraAppSetting.DATA_STORAGE.intentKey, "internal");
        ExternalCameraAppSetting.ShareSettingCategory shareSettingCategory = null;
        Intent intent = null;
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("The request code '");
                sb.append(i);
                sb.append("' is incorrect");
                throw new RuntimeException(sb.toString());
            }
            case 18: {
                shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                intent = getPortraitSelfieIntent(cameraActivity, userSettings);
                break;
            }
            case 17: {
                intent = getDualCameraEffectIntent(i, capturingMode);
                if (capturingMode == CapturingMode.VIDEO) {
                    shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.VIDEO;
                    break;
                }
                shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                break;
            }
            case 16: {
                shareSettingCategory = ExternalCameraAppSetting.ShareSettingCategory.PHOTO;
                intent = getDualCameraEffectIntent(i, capturingMode);
                break;
            }
        }
        for (final ExternalCameraAppSetting externalCameraAppSetting : ExternalCameraAppSetting.values()) {
            if (!hashMap.containsKey(externalCameraAppSetting.intentKey) && externalCameraAppSetting.isShared(shareSettingCategory)) {
                Object o;
                if (i != 16 && i != 17) {
                    o = externalCameraAppSetting.toIntentValue(userSettings.get(externalCameraAppSetting.key));
                }
                else {
                    o = externalCameraAppSetting.toIntentValue(userSettings.get(capturingMode, externalCameraAppSetting.key));
                }
                if (((Boolean)o).getClass().equals(Boolean.class)) {
                    intent.putExtra(externalCameraAppSetting.intentKey, (boolean)o);
                }
                else {
                    if (!((Boolean)o).getClass().equals(String.class)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("One of the external app values was neither a boolean nor a String. It was a ");
                        sb2.append(((String)o).getClass());
                        sb2.append(".");
                        throw new RuntimeException(sb2.toString());
                    }
                    intent.putExtra(externalCameraAppSetting.intentKey, (String)o);
                }
            }
        }
        if (!hashMap.isEmpty()) {
            for (final Map.Entry<K, Object> entry : hashMap.entrySet()) {
                if (entry.getValue().getClass().equals(Boolean.class)) {
                    intent.putExtra((String)entry.getKey(), (boolean)entry.getValue());
                }
                else {
                    if (!entry.getValue().getClass().equals(String.class)) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("One of the Force settings values was neither a boolean nor a String. It was a ");
                        sb3.append(entry.getClass());
                        sb3.append(".");
                        throw new RuntimeException(sb3.toString());
                    }
                    intent.putExtra((String)entry.getKey(), (String)entry.getValue());
                }
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("Launch external camera application");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  action:");
            sb4.append(intent.getAction());
            CamLog.d(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  component:");
            sb5.append(intent.getComponent());
            CamLog.d(sb5.toString());
            CamLog.d("  extra:");
            for (final String str : intent.getExtras().keySet()) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("    ");
                sb6.append(str);
                sb6.append(":");
                sb6.append(intent.getExtras().get(str));
                CamLog.d(sb6.toString());
            }
        }
        if (b) {
            cameraActivity.startActivityForResult(intent, i);
        }
        else {
            cameraActivity.startActivity(intent);
        }
        cameraActivity.overridePendingTransition(0, 0);
    }
    
    public static void launchLocationSourceSettings(final Activity activity) {
        final Intent obj = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        obj.addCategory("android.intent.category.DEFAULT");
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), obj)) {
            try {
                activity.startActivity(obj);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("launchLocationSourceSettings: ");
                    sb.append(obj);
                    CamLog.d("ApplicationLauncher", sb.toString());
                }
            }
            catch (final ActivityNotFoundException ex) {
                CamLog.e("launchLocationSourceSettings: failed.", (Throwable)ex);
            }
        }
    }
    
    public static void launchSideSenseSettings(final Activity activity) {
        final Intent obj = new Intent("com.sonymobile.sidesenseapp.action.LAUNCH_SETTINGS");
        obj.addCategory("android.intent.category.DEFAULT");
        if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), obj)) {
            try {
                activity.startActivity(obj);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("launchSideSenseSettings: ");
                    sb.append(obj);
                    CamLog.d("ApplicationLauncher", sb.toString());
                }
            }
            catch (final ActivityNotFoundException ex) {
                CamLog.e("launchSideSenseSettings: failed.", (Throwable)ex);
            }
        }
    }
    
    public static boolean launchVideoEditor(final Activity activity, final StoreDataResult storeDataResult) {
        try {
            final Uri uri = storeDataResult.uri;
            final SavingRequest savingRequest = storeDataResult.savingRequest;
            String mimeType = null;
            if (savingRequest != null) {
                mimeType = savingRequest.common.mimeType;
            }
            if ("video/mp4".equals(mimeType) && isEditorAvailable((Context)activity, uri, savingRequest.common.mimeType)) {
                final ActivityOptions customAnimation = ActivityOptions.makeCustomAnimation((Context)activity, 2130771983, 2130771984);
                final Intent intent = new Intent(ApplicationLauncher.ACTION_EDIT_HIGH_FRAME_RATE);
                intent.setDataAndType(uri, mimeType);
                intent.setFlags(3);
                if (CommonUtility.isActivityAvailable(activity.getApplicationContext(), intent)) {
                    activity.startActivityForResult(intent, 14, customAnimation.toBundle());
                    return true;
                }
            }
        }
        catch (final ActivityNotFoundException ex) {
            CamLog.e("openReviewWindow: failed.", (Throwable)ex);
        }
        return false;
    }
    
    public enum MonochromeType
    {
        private static final MonochromeType[] $VALUES;
        
        MONOCHROME_PHOTO, 
        MONOCHROME_VIDEO;
        
        static {
            $VALUES = new MonochromeType[] { MonochromeType.MONOCHROME_PHOTO, MonochromeType.MONOCHROME_VIDEO };
        }
    }
}
