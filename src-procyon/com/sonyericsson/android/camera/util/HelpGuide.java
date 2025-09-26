// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.Locale;
import android.os.Build;
import android.os.Build$VERSION;
import android.net.Uri$Builder;
import com.sonymobile.help.HelpUtils;
import android.content.Intent;
import android.content.Context;

public class HelpGuide
{
    private static final String CATEGORY_CAPTURE = "Capture";
    private static final String HELP_APP_PKG_NAME = "com.sonymobile.support";
    public static final String HELP_CATEGORY_CAPTURE = "Capture";
    public static final String TAG = "HelpGuide";
    
    private static Intent getHelpAppStartIntent(final Context context) {
        final Uri$Builder appendQueryParameter = HelpUtils.BASE_URI.buildUpon().appendQueryParameter("app", context.getPackageName()).appendQueryParameter("category", "Capture");
        HelpUtils.uriWithAddedVersionParameter(context, appendQueryParameter);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Uri:");
            sb.append(appendQueryParameter.toString());
            CamLog.d(sb.toString());
        }
        return new Intent("android.intent.action.VIEW", appendQueryParameter.build());
    }
    
    private static Intent getOnlineHelpIntent(final Context context) {
        if (context == null) {
            return null;
        }
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("https");
        uri$Builder.authority("ids.indevice.sonymobile.com");
        uri$Builder.path("in-device/getSoftwareSupport.htm");
        uri$Builder.appendQueryParameter("sourceAppName", context.getPackageName());
        uri$Builder.appendQueryParameter("sourceAppVersion", getVersionName(context));
        uri$Builder.appendQueryParameter("sourceAppView", "Capture");
        uri$Builder.appendQueryParameter("androidVersion", Build$VERSION.RELEASE);
        uri$Builder.appendQueryParameter("manufacturer", Build.MANUFACTURER);
        uri$Builder.appendQueryParameter("model", Build.MODEL);
        uri$Builder.appendQueryParameter("locale", Locale.getDefault().toString());
        uri$Builder.appendQueryParameter("output", "html");
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Uri:");
            sb.append(uri$Builder.toString());
            CamLog.d(sb.toString());
        }
        return new Intent("android.intent.action.VIEW", uri$Builder.build());
    }
    
    private static String getVersionName(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    public static boolean isHelpAppAvailable(final Context context) {
        final Intent helpAppStartIntent = getHelpAppStartIntent(context);
        final PackageManager packageManager = context.getPackageManager();
        boolean activityAvailable = false;
        try {
            if (packageManager.getApplicationInfo("com.sonymobile.support", 0) != null) {
                final ComponentName resolveActivity = helpAppStartIntent.resolveActivity(packageManager);
                if (resolveActivity != null) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("component:");
                        sb.append(resolveActivity);
                        CamLog.d(sb.toString());
                    }
                    helpAppStartIntent.setComponent(resolveActivity);
                }
                activityAvailable = CommonUtility.isActivityAvailable(context, helpAppStartIntent);
            }
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("Somc in-device help app not found.", (Throwable)ex);
            activityAvailable = activityAvailable;
        }
        return activityAvailable;
    }
    
    public static void startHelpApp(final Context context) {
        try {
            context.startActivity(getHelpAppStartIntent(context));
        }
        catch (final ActivityNotFoundException ex) {
            CamLog.e("startResolvedActivity failed.", (Throwable)ex);
        }
    }
    
    public static void startOnlineHelp(final Context context) {
        try {
            context.startActivity(getOnlineHelpIntent(context));
        }
        catch (final ActivityNotFoundException ex) {
            CamLog.e("startOnlineHelp failed.", (Throwable)ex);
        }
    }
}
