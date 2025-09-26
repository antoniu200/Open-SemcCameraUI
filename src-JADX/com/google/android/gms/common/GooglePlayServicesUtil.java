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

    /* JADX WARN: Removed duplicated region for block: B:35:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b9  */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int isGooglePlayServicesAvailable(android.content.Context r7) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            boolean r0 = com.google.android.gms.common.internal.zzd.zzaeK
            r1 = 0
            if (r0 == 0) goto L6
            return r1
        L6:
            android.content.pm.PackageManager r0 = r7.getPackageManager()
            android.content.res.Resources r2 = r7.getResources()     // Catch: java.lang.Throwable -> L14
            int r3 = com.google.android.gms.R.string.common_google_play_services_unknown_issue     // Catch: java.lang.Throwable -> L14
            r2.getString(r3)     // Catch: java.lang.Throwable -> L14
            goto L1b
        L14:
            java.lang.String r2 = "GooglePlayServicesUtil"
            java.lang.String r3 = "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included."
            android.util.Log.e(r2, r3)
        L1b:
            java.lang.String r2 = "com.google.android.gms"
            java.lang.String r3 = r7.getPackageName()
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L2a
            zzad(r7)
        L2a:
            r2 = 1
            java.lang.String r3 = "com.google.android.gms"
            r4 = 64
            android.content.pm.PackageInfo r3 = r0.getPackageInfo(r3, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> Ld4
            com.google.android.gms.common.zzd r4 = com.google.android.gms.common.zzd.zznu()
            int r5 = r3.versionCode
            boolean r5 = com.google.android.gms.internal.zzml.zzcb(r5)
            r6 = 9
            if (r5 != 0) goto L7a
            boolean r7 = com.google.android.gms.internal.zzml.zzan(r7)
            if (r7 == 0) goto L48
            goto L7a
        L48:
            java.lang.String r7 = "com.android.vending"
            r5 = 8256(0x2040, float:1.1569E-41)
            android.content.pm.PackageInfo r7 = r0.getPackageInfo(r7, r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            com.google.android.gms.common.zzc$zza[] r5 = com.google.android.gms.common.zzc.zzbz.zzaak     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            com.google.android.gms.common.zzc$zza r7 = r4.zza(r7, r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            if (r7 != 0) goto L60
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play Store signature invalid."
            android.util.Log.w(r7, r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            return r6
        L60:
            com.google.android.gms.common.zzc$zza[] r5 = new com.google.android.gms.common.zzc.zza[r2]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            r5[r1] = r7     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            com.google.android.gms.common.zzc$zza r7 = r4.zza(r3, r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            if (r7 != 0) goto L87
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play services signature invalid."
            android.util.Log.w(r7, r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L72
            return r6
        L72:
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play Store is neither installed nor updating."
        L76:
            android.util.Log.w(r7, r0)
            return r6
        L7a:
            com.google.android.gms.common.zzc$zza[] r7 = com.google.android.gms.common.zzc.zzbz.zzaak
            com.google.android.gms.common.zzc$zza r7 = r4.zza(r3, r7)
            if (r7 != 0) goto L87
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play services signature invalid."
            goto L76
        L87:
            int r7 = com.google.android.gms.common.GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE
            int r7 = com.google.android.gms.internal.zzml.zzca(r7)
            int r4 = r3.versionCode
            int r4 = com.google.android.gms.internal.zzml.zzca(r4)
            if (r4 >= r7) goto Lb9
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Google Play services out of date.  Requires "
            r0.append(r1)
            int r1 = com.google.android.gms.common.GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE
            r0.append(r1)
            java.lang.String r1 = " but found "
            r0.append(r1)
            int r1 = r3.versionCode
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r7, r0)
            r7 = 2
            return r7
        Lb9:
            android.content.pm.ApplicationInfo r7 = r3.applicationInfo
            if (r7 != 0) goto Lcd
            java.lang.String r7 = "com.google.android.gms"
            android.content.pm.ApplicationInfo r7 = r0.getApplicationInfo(r7, r1)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> Lc4
            goto Lcd
        Lc4:
            r7 = move-exception
            java.lang.String r0 = "GooglePlayServicesUtil"
            java.lang.String r1 = "Google Play services missing when getting application info."
            android.util.Log.wtf(r0, r1, r7)
            return r2
        Lcd:
            boolean r7 = r7.enabled
            if (r7 != 0) goto Ld3
            r7 = 3
            return r7
        Ld3:
            return r1
        Ld4:
            java.lang.String r7 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play services is missing."
            android.util.Log.w(r7, r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(android.content.Context):int");
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

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean zzb(android.content.pm.PackageManager r6) {
        /*
            java.lang.Object r0 = com.google.android.gms.common.GooglePlayServicesUtil.zzpy
            monitor-enter(r0)
            int r1 = com.google.android.gms.common.GooglePlayServicesUtil.zzaan     // Catch: java.lang.Throwable -> L33
            r2 = -1
            r3 = 0
            r4 = 1
            if (r1 != r2) goto L2c
            java.lang.String r1 = "com.google.android.gms"
            r2 = 64
            android.content.pm.PackageInfo r6 = r6.getPackageInfo(r1, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            com.google.android.gms.common.zzd r1 = com.google.android.gms.common.zzd.zznu()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            com.google.android.gms.common.zzc$zza[] r2 = new com.google.android.gms.common.zzc.zza[r4]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            com.google.android.gms.common.zzc$zza[] r5 = com.google.android.gms.common.zzc.zzaad     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            r5 = r5[r4]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            r2[r3] = r5     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            com.google.android.gms.common.zzc$zza r6 = r1.zza(r6, r2)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            if (r6 == 0) goto L27
            com.google.android.gms.common.GooglePlayServicesUtil.zzaan = r4     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            goto L2c
        L27:
            com.google.android.gms.common.GooglePlayServicesUtil.zzaan = r3     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L2a java.lang.Throwable -> L33
            goto L2c
        L2a:
            com.google.android.gms.common.GooglePlayServicesUtil.zzaan = r3     // Catch: java.lang.Throwable -> L33
        L2c:
            int r6 = com.google.android.gms.common.GooglePlayServicesUtil.zzaan     // Catch: java.lang.Throwable -> L33
            if (r6 == 0) goto L31
            r3 = r4
        L31:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L33
            return r3
        L33:
            r6 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L33
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.zzb(android.content.pm.PackageManager):boolean");
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
