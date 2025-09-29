package com.google.android.gms.common;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatExtras;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzml;
import com.google.android.gms.internal.zzmx;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class GooglePlayServicesUtil {
    public static final String GMS_ERROR_DIALOG = "GooglePlayServicesErrorDialog";

    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static boolean zzaal = false;
    public static boolean zzaam = false;
    private static int zzaan = -1;
    private static String zzaao;
    private static Integer zzaap;

    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = zzns();
    private static final Object zzpy = new Object();
    static final AtomicBoolean zzaaq = new AtomicBoolean();
    private static final AtomicBoolean zzaar = new AtomicBoolean();

    private static class zza extends Handler {
        private final Context zzqZ;

        zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.zzqZ = context.getApplicationContext();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
            if (message.what != 1) {
                Log.w("GooglePlayServicesUtil", "Don't know how to handle this message: " + message.what);
                return;
            }
            int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.zzqZ);
            if (GooglePlayServicesUtil.isUserRecoverableError(iIsGooglePlayServicesAvailable)) {
                GooglePlayServicesUtil.zza(iIsGooglePlayServicesAvailable, this.zzqZ);
            }
        }
    }

    private GooglePlayServicesUtil() {
    }

    @Deprecated
    public static Dialog getErrorDialog(int i, Activity activity, int i2) {
        return getErrorDialog(i, activity, i2, null);
    }

    @Deprecated
    public static Dialog getErrorDialog(int i, Activity activity, int i2, DialogInterface.OnCancelListener onCancelListener) {
        return zza(i, activity, null, i2, onCancelListener);
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(context, i, i2);
    }

    @Deprecated
    public static String getErrorString(int i) {
        return ConnectionResult.getStatusString(i);
    }

    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(Context context) throws IOException {
        try {
            InputStream inputStreamOpenInputStream = context.getContentResolver().openInputStream(new Uri.Builder().scheme("android.resource").authority("com.google.android.gms").appendPath("raw").appendPath("oss_notice").build());
            try {
                try {
                    return new Scanner(inputStreamOpenInputStream).useDelimiter("\\A").next();
                } finally {
                    if (inputStreamOpenInputStream != null) {
                        inputStreamOpenInputStream.close();
                    }
                }
            } catch (NoSuchElementException unused) {
                if (inputStreamOpenInputStream != null) {
                    inputStreamOpenInputStream.close();
                }
                return null;
            }
        } catch (Exception unused2) {
        }
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    @java.lang.Deprecated
    public static int isGooglePlayServicesAvailable(android.content.Context ctx) {
        // If test flag says we're good, return SUCCESS (0)
        if (com.google.android.gms.common.internal.zzd.zzaeK) {
            return 0;
        }

        final android.content.pm.PackageManager pm = ctx.getPackageManager();

        // Try touching resources; log if missing (doesn't change result here)
        try {
            android.content.res.Resources res = ctx.getResources();
            res.getString(com.google.android.gms.R.string.common_google_play_services_unknown_issue);
        } catch (Throwable t) {
            android.util.Log.e("GooglePlayServicesUtil",
                "The Google Play services resources were not found. " +
                "Check your project configuration to ensure that the resources are included.");
        }

        // Validate manifest setup if not running inside GMS core
        if (!"com.google.android.gms".equals(ctx.getPackageName())) {
            zzad(ctx);
        }

        final int SUCCESS = 0;
        final int SERVICE_MISSING = 1;
        final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
        final int SERVICE_DISABLED = 3;
        final int SERVICE_INVALID = 9;

        final int SIGS = 64;      // PackageManager.GET_SIGNATURES (legacy)
        final int PLAY_STORE_FLAGS = 0x2040; // 8256

        final android.content.pm.PackageInfo gmsPkg;
        try {
            gmsPkg = pm.getPackageInfo("com.google.android.gms", SIGS);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return SERVICE_MISSING;
        }

        final com.google.android.gms.common.zzd verifier = com.google.android.gms.common.zzd.zznu();
        final boolean isTvBuildOrUpdating = com.google.android.gms.internal.zzml.zzcb(gmsPkg.versionCode)
                || com.google.android.gms.internal.zzml.zzan(ctx);

        if (!isTvBuildOrUpdating) {
            // Validate Play Store and then validate GMS with Play Store's signature
            try {
                android.content.pm.PackageInfo vending = pm.getPackageInfo("com.android.vending", PLAY_STORE_FLAGS);
                com.google.android.gms.common.zzc.zza vendingSig =
                        verifier.zza(vending, com.google.android.gms.common.zzc.zzbz.zzaak);
                if (vendingSig == null) {
                    android.util.Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
                    return SERVICE_INVALID;
                }
                com.google.android.gms.common.zzc.zza[] allow = new com.google.android.gms.common.zzc.zza[1];
                allow[0] = vendingSig;
                com.google.android.gms.common.zzc.zza gmsSig = verifier.zza(gmsPkg, allow);
                if (gmsSig == null) {
                    android.util.Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                    return SERVICE_INVALID;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.w("GooglePlayServicesUtil", "Google Play Store is neither installed nor updating.");
                return SERVICE_INVALID;
            }
        } else {
            // Directly validate GMS against known good signatures
            com.google.android.gms.common.zzc.zza gmsSig =
                    verifier.zza(gmsPkg, com.google.android.gms.common.zzc.zzbz.zzaak);
            if (gmsSig == null) {
                android.util.Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                return SERVICE_INVALID;
            }
        }

        // Version check (normalize with zzml.zzca)
        final int required = com.google.android.gms.internal.zzml
                .zzca(com.google.android.gms.common.GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        final int found = com.google.android.gms.internal.zzml.zzca(gmsPkg.versionCode);
        if (found < required) {
            android.util.Log.w("GooglePlayServicesUtil",
                    "Google Play services out of date.  Requires "
                    + com.google.android.gms.common.GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE
                    + " but found " + gmsPkg.versionCode);
            return SERVICE_VERSION_UPDATE_REQUIRED;
        }

        // Enabled check
        android.content.pm.ApplicationInfo ai = gmsPkg.applicationInfo;
        if (ai == null) {
            try {
                ai = pm.getApplicationInfo("com.google.android.gms", 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.wtf("GooglePlayServicesUtil",
                        "Google Play services missing when getting application info.", e);
                return SERVICE_MISSING;
            }
        }
        if (!ai.enabled) {
            return SERVICE_DISABLED;
        }
        return SUCCESS;
    }

    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        if (i == 9) {
            return true;
        }
        switch (i) {
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int i, Activity activity, int i2) {
        return showErrorDialogFragment(i, activity, i2, null);
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int i, Activity activity, int i2, DialogInterface.OnCancelListener onCancelListener) {
        return showErrorDialogFragment(i, activity, null, i2, onCancelListener);
    }

    public static boolean showErrorDialogFragment(int i, Activity activity, Fragment fragment, int i2, DialogInterface.OnCancelListener onCancelListener) {
        Dialog dialogZza = zza(i, activity, fragment, i2, onCancelListener);
        if (dialogZza == null) {
            return false;
        }
        zza(activity, onCancelListener, GMS_ERROR_DIALOG, dialogZza);
        return true;
    }

    @Deprecated
    public static void showErrorNotification(int i, Context context) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        if (zzml.zzan(context) && i == 2) {
            i = 42;
        }
        if (zzd(context, i) || zzf(context, i)) {
            zzae(context);
        } else {
            zza(i, context);
        }
    }

    private static Dialog zza(int i, Activity activity, Fragment fragment, int i2, DialogInterface.OnCancelListener onCancelListener) {
        AlertDialog.Builder builder = null;
        if (i == 0) {
            return null;
        }
        if (zzml.zzan(activity) && i == 2) {
            i = 42;
        }
        if (zzmx.zzqx()) {
            TypedValue typedValue = new TypedValue();
            activity.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
            if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                builder = new AlertDialog.Builder(activity, 5);
            }
        }
        if (builder == null) {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setMessage(zzg.zzc(activity, i, zzaf(activity)));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        Intent intentZza = GoogleApiAvailability.getInstance().zza(activity, i, "d");
        zzh zzhVar = fragment == null ? new zzh(activity, intentZza, i2) : new zzh(fragment, intentZza, i2);
        String strZzh = zzg.zzh(activity, i);
        if (strZzh != null) {
            builder.setPositiveButton(strZzh, zzhVar);
        }
        String strZzg = zzg.zzg(activity, i);
        if (strZzg != null) {
            builder.setTitle(strZzg);
        }
        return builder.create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void zza(int i, Context context) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        zza(i, context, null);
    }

    private static void zza(int i, Context context, String str) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        Notification notificationBuild;
        int i2;
        Resources resources = context.getResources();
        String strZzaf = zzaf(context);
        String strZzi = zzg.zzi(context, i);
        if (strZzi == null) {
            strZzi = resources.getString(com.google.android.gms.R.string.common_google_play_services_notification_ticker);
        }
        String strZzd = zzg.zzd(context, i, strZzaf);
        PendingIntent pendingIntentZza = GoogleApiAvailability.getInstance().zza(context, i, 0, "n");
        if (zzml.zzan(context)) {
            zzx.zzZ(zzmx.zzqy());
            notificationBuild = new Notification.Builder(context).setSmallIcon(com.google.android.gms.R.drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true).setStyle(new Notification.BigTextStyle().bigText(strZzi + " " + strZzd)).addAction(com.google.android.gms.R.drawable.common_full_open_on_phone, resources.getString(com.google.android.gms.R.string.common_open_on_phone), pendingIntentZza).build();
        } else {
            String string = resources.getString(com.google.android.gms.R.string.common_google_play_services_notification_ticker);
            if (zzmx.zzqu()) {
                Notification.Builder autoCancel = new Notification.Builder(context).setSmallIcon(R.drawable.stat_sys_warning).setContentTitle(strZzi).setContentText(strZzd).setContentIntent(pendingIntentZza).setTicker(string).setAutoCancel(true);
                if (zzmx.zzqC()) {
                    autoCancel.setLocalOnly(true);
                }
                if (zzmx.zzqy()) {
                    autoCancel.setStyle(new Notification.BigTextStyle().bigText(strZzd));
                    notificationBuild = autoCancel.build();
                } else {
                    notificationBuild = autoCancel.getNotification();
                }
                if (Build.VERSION.SDK_INT == 19) {
                    notificationBuild.extras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
                }
            } else {
                notificationBuild = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.stat_sys_warning).setTicker(string).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntentZza).setContentTitle(strZzi).setContentText(strZzd).build();
            }
        }
        if (zzbk(i)) {
            i2 = 10436;
            zzaaq.set(false);
        } else {
            i2 = 39789;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (str != null) {
            notificationManager.notify(str, i2, notificationBuild);
        } else {
            notificationManager.notify(i2, notificationBuild);
        }
    }

    public static void zza(Activity activity, DialogInterface.OnCancelListener onCancelListener, String str, Dialog dialog) {
        boolean z;
        try {
            z = activity instanceof FragmentActivity;
        } catch (NoClassDefFoundError unused) {
            z = false;
        }
        if (z) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else {
            if (!zzmx.zzqu()) {
                throw new RuntimeException("This Activity does not support Fragments.");
            }
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        }
    }

    @Deprecated
    public static void zzaa(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        int iIsGooglePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (iIsGooglePlayServicesAvailable != 0) {
            Intent intentZza = GoogleApiAvailability.getInstance().zza(context, iIsGooglePlayServicesAvailable, "e");
            Log.e("GooglePlayServicesUtil", "GooglePlayServices not available due to error " + iIsGooglePlayServicesAvailable);
            if (intentZza != null) {
                throw new GooglePlayServicesRepairableException(iIsGooglePlayServicesAvailable, "Google Play Services not available", intentZza);
            }
            throw new GooglePlayServicesNotAvailableException(iIsGooglePlayServicesAvailable);
        }
    }

    @Deprecated
    public static void zzac(Context context) {
        if (zzaaq.getAndSet(true)) {
            return;
        }
        try {
            ((NotificationManager) context.getSystemService("notification")).cancel(10436);
        } catch (SecurityException unused) {
        }
    }

    private static void zzad(Context context) {
        Integer num;
        if (zzaar.get()) {
            return;
        }
        synchronized (zzpy) {
            if (zzaao == null) {
                zzaao = context.getPackageName();
                try {
                    Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                    if (bundle != null) {
                        zzaap = Integer.valueOf(bundle.getInt("com.google.android.gms.version"));
                    } else {
                        zzaap = null;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.wtf("GooglePlayServicesUtil", "This should never happen.", e);
                }
            } else if (!zzaao.equals(context.getPackageName())) {
                throw new IllegalArgumentException("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '" + zzaao + "' and this call used package '" + context.getPackageName() + "'.");
            }
            num = zzaap;
        }
        if (num == null) {
            throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
        }
        if (num.intValue() != GOOGLE_PLAY_SERVICES_VERSION_CODE) {
            throw new IllegalStateException("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected " + GOOGLE_PLAY_SERVICES_VERSION_CODE + " but found " + num + ".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
        }
    }

    private static void zzae(Context context) {
        zza zzaVar = new zza(context);
        zzaVar.sendMessageDelayed(zzaVar.obtainMessage(1), 120000L);
    }

    public static String zzaf(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo;
        String str = context.getApplicationInfo().name;
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        return applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : packageName;
    }

    public static boolean zzag(Context context) {
        return zzmx.zzqD() && context.getPackageManager().hasSystemFeature("com.google.sidewinder");
    }

    public static boolean zzah(Context context) {
        Bundle applicationRestrictions;
        return zzmx.zzqA() && (applicationRestrictions = ((UserManager) context.getSystemService("user")).getApplicationRestrictions(context.getPackageName())) != null && "true".equals(applicationRestrictions.getString("restricted_profile"));
    }

    public static boolean zzb(Context context, int i, String str) {
        if (zzmx.zzqB()) {
            try {
                ((AppOpsManager) context.getSystemService("appops")).checkPackage(i, str);
                return true;
            } catch (SecurityException unused) {
                return false;
            }
        }
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (str != null && packagesForUid != null) {
            for (String str2 : packagesForUid) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean zzb(android.content.Context ctx, int uid, String packageName) {
        if (com.google.android.gms.internal.zzmx.zzqB()) {
            Object svc = ctx.getSystemService("appops");
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) svc;
            try {
                appOps.checkPackage(uid, packageName);
                return true;
            } catch (java.lang.SecurityException se) {
                return false;
            }
        } else {
            android.content.pm.PackageManager pm = ctx.getPackageManager();
            String[] pkgs = pm.getPackagesForUid(uid);
            if (packageName != null && pkgs != null) {
                for (int i = 0; i < pkgs.length; i++) {
                    if (packageName.equals(pkgs[i])) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Deprecated
    public static boolean zzb(PackageManager packageManager, String str) {
        return zzd.zznu().zzb(packageManager, str);
    }

    @Deprecated
    public static Intent zzbj(int i) {
        return GoogleApiAvailability.getInstance().zza(null, i, null);
    }

    private static boolean zzbk(int i) {
        if (i == 18 || i == 42) {
            return true;
        }
        switch (i) {
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public static boolean zzc(PackageManager packageManager) {
        return zzb(packageManager) || !zznt();
    }

    @Deprecated
    public static boolean zzd(Context context, int i) {
        if (i == 18) {
            return true;
        }
        if (i == 1) {
            return zzj(context, "com.google.android.gms");
        }
        return false;
    }

    public static boolean zze(Context context, int i) {
        return zzb(context, i, "com.google.android.gms") && zzb(context.getPackageManager(), "com.google.android.gms");
    }

    @Deprecated
    public static boolean zzf(Context context, int i) {
        if (i == 9) {
            return zzj(context, GOOGLE_PLAY_STORE_PACKAGE);
        }
        return false;
    }

    static boolean zzj(Context context, String str) {
        if (zzmx.zzqD()) {
            Iterator<PackageInstaller.SessionInfo> it = context.getPackageManager().getPackageInstaller().getAllSessions().iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().getAppPackageName())) {
                    return true;
                }
            }
        }
        if (zzah(context)) {
            return false;
        }
        try {
            return context.getPackageManager().getApplicationInfo(str, 8192).enabled;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private static int zzns() {
        return 8115000;
    }

    public static boolean zznt() {
        return zzaal ? zzaam : "user".equals(Build.TYPE);
    }
}
