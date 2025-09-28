package com.sonyericsson.android.camera.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonymobile.help.HelpUtils;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HelpGuide {
    private static final String CATEGORY_CAPTURE = "Capture";
    private static final String HELP_APP_PKG_NAME = "com.sonymobile.support";
    public static final String HELP_CATEGORY_CAPTURE = "Capture";
    public static final String TAG = "HelpGuide";

    public static void startHelpApp(Context context) {
        try {
            context.startActivity(getHelpAppStartIntent(context));
        } catch (ActivityNotFoundException e) {
            CamLog.e("startResolvedActivity failed.", e);
        }
    }

    public static void startOnlineHelp(Context context) {
        try {
            context.startActivity(getOnlineHelpIntent(context));
        } catch (ActivityNotFoundException e) {
            CamLog.e("startOnlineHelp failed.", e);
        }
    }

    public static boolean isHelpAppAvailable(Context context) {
        Intent helpAppStartIntent = getHelpAppStartIntent(context);
        PackageManager packageManager = context.getPackageManager();
        try {
            if (packageManager.getApplicationInfo(HELP_APP_PKG_NAME, 0) == null) {
                return false;
            }
            ComponentName componentNameResolveActivity = helpAppStartIntent.resolveActivity(packageManager);
            if (componentNameResolveActivity != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("component:" + componentNameResolveActivity);
                }
                helpAppStartIntent.setComponent(componentNameResolveActivity);
            }
            return CommonUtility.isActivityAvailable(context, helpAppStartIntent);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e("Somc in-device help app not found.", e);
            return false;
        }
    }

    private static Intent getHelpAppStartIntent(Context context) {
        Uri.Builder builderAppendQueryParameter = HelpUtils.BASE_URI.buildUpon().appendQueryParameter("app", context.getPackageName()).appendQueryParameter("category", "Capture");
        HelpUtils.uriWithAddedVersionParameter(context, builderAppendQueryParameter);
        if (CamLog.VERBOSE) {
            CamLog.d("Uri:" + builderAppendQueryParameter.toString());
        }
        return new Intent("android.intent.action.VIEW", builderAppendQueryParameter.build());
    }

    private static Intent getOnlineHelpIntent(Context context) {
        if (context == null) {
            return null;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ResourceUtil.HTTPS_SCHEME);
        builder.authority("ids.indevice.sonymobile.com");
        builder.path("in-device/getSoftwareSupport.htm");
        builder.appendQueryParameter("sourceAppName", context.getPackageName());
        builder.appendQueryParameter("sourceAppVersion", getVersionName(context));
        builder.appendQueryParameter("sourceAppView", "Capture");
        builder.appendQueryParameter("androidVersion", Build.VERSION.RELEASE);
        builder.appendQueryParameter("manufacturer", Build.MANUFACTURER);
        builder.appendQueryParameter("model", Build.MODEL);
        builder.appendQueryParameter("locale", Locale.getDefault().toString());
        builder.appendQueryParameter("output", "html");
        if (CamLog.VERBOSE) {
            CamLog.d("Uri:" + builder.toString());
        }
        return new Intent("android.intent.action.VIEW", builder.build());
    }

    private static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
