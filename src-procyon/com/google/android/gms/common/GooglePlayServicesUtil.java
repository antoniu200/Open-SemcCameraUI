// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import android.os.Build;
import java.util.Iterator;
import android.content.pm.PackageInstaller$SessionInfo;
import android.app.AppOpsManager;
import android.os.UserManager;
import android.text.TextUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.os.Build$VERSION;
import android.app.Notification$Style;
import android.app.Notification$BigTextStyle;
import android.app.Notification$Builder;
import com.google.android.gms.common.internal.zzx;
import android.content.Intent;
import android.content.DialogInterface$OnClickListener;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.internal.zzg;
import android.app.AlertDialog$Builder;
import android.util.TypedValue;
import com.google.android.gms.internal.zzmx;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.internal.zzml;
import android.util.Log;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.zzd;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import android.net.Uri;
import java.util.NoSuchElementException;
import java.io.InputStream;
import java.util.Scanner;
import android.net.Uri$Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface$OnCancelListener;
import android.app.Dialog;
import android.app.Activity;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GooglePlayServicesUtil
{
    public static final String GMS_ERROR_DIALOG = "GooglePlayServicesErrorDialog";
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static boolean zzaal = false;
    public static boolean zzaam = false;
    private static int zzaan = -1;
    private static String zzaao;
    private static Integer zzaap;
    static final AtomicBoolean zzaaq;
    private static final AtomicBoolean zzaar;
    private static final Object zzpy;
    
    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = zzns();
        zzpy = new Object();
        zzaaq = new AtomicBoolean();
        zzaar = new AtomicBoolean();
    }
    
    private GooglePlayServicesUtil() {
    }
    
    @Deprecated
    public static Dialog getErrorDialog(final int n, final Activity activity, final int n2) {
        return getErrorDialog(n, activity, n2, null);
    }
    
    @Deprecated
    public static Dialog getErrorDialog(final int n, final Activity activity, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return zza(n, activity, null, n2, dialogInterface$OnCancelListener);
    }
    
    @Deprecated
    public static PendingIntent getErrorPendingIntent(final int n, final Context context, final int n2) {
        return GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(context, n, n2);
    }
    
    @Deprecated
    public static String getErrorString(final int n) {
        return ConnectionResult.getStatusString(n);
    }
    
    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(Context openInputStream) {
        final Uri build = new Uri$Builder().scheme("android.resource").authority("com.google.android.gms").appendPath("raw").appendPath("oss_notice").build();
        try {
            openInputStream = (Context)openInputStream.getContentResolver().openInputStream(build);
            try {
                return new Scanner((InputStream)openInputStream).useDelimiter("\\A").next();
            }
            catch (final NoSuchElementException ex) {
                if (openInputStream != null) {
                    ((InputStream)openInputStream).close();
                }
            }
            finally {
                if (openInputStream != null) {
                    ((InputStream)openInputStream).close();
                }
            }
            return null;
        }
        catch (final Exception ex2) {
            return null;
        }
    }
    
    public static Context getRemoteContext(Context packageContext) {
        try {
            packageContext = packageContext.createPackageContext("com.google.android.gms", 3);
            return packageContext;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    public static Resources getRemoteResource(final Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    @Deprecated
    public static int isGooglePlayServicesAvailable(final Context context) {
        if (zzd.zzaeK) {
            return 0;
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            context.getResources().getString(R.string.common_google_play_services_unknown_issue);
        }
        catch (final Throwable t) {
            Log.e("GooglePlayServicesUtil", "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.");
        }
        if (!"com.google.android.gms".equals(context.getPackageName())) {
            zzad(context);
        }
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo("com.google.android.gms", 64);
            final com.google.android.gms.common.zzd zznu = com.google.android.gms.common.zzd.zznu();
            Label_0179: {
                String s;
                if (!zzml.zzcb(packageInfo.versionCode) && !zzml.zzan(context)) {
                    try {
                        final zzc.zza zza = zznu.zza(packageManager.getPackageInfo("com.android.vending", 8256), zzc.zzbz.zzaak);
                        if (zza == null) {
                            Log.w("GooglePlayServicesUtil", "Google Play Store signature invalid.");
                            return 9;
                        }
                        if (zznu.zza(packageInfo, zza) == null) {
                            Log.w("GooglePlayServicesUtil", "Google Play services signature invalid.");
                            return 9;
                        }
                        break Label_0179;
                    }
                    catch (final PackageManager$NameNotFoundException ex) {
                        s = "Google Play Store is neither installed nor updating.";
                    }
                }
                else {
                    if (zznu.zza(packageInfo, zzc.zzbz.zzaak) != null) {
                        break Label_0179;
                    }
                    s = "Google Play services signature invalid.";
                }
                Log.w("GooglePlayServicesUtil", s);
                return 9;
            }
            if (zzml.zzca(packageInfo.versionCode) < zzml.zzca(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Google Play services out of date.  Requires ");
                sb.append(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                sb.append(" but found ");
                sb.append(packageInfo.versionCode);
                Log.w("GooglePlayServicesUtil", sb.toString());
                return 2;
            }
            ApplicationInfo applicationInfo;
            if ((applicationInfo = packageInfo.applicationInfo) == null) {
                try {
                    applicationInfo = packageManager.getApplicationInfo("com.google.android.gms", 0);
                }
                catch (final PackageManager$NameNotFoundException ex2) {
                    Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", (Throwable)ex2);
                    return 1;
                }
            }
            if (!applicationInfo.enabled) {
                return 3;
            }
            return 0;
        }
        catch (final PackageManager$NameNotFoundException ex3) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 1;
        }
    }
    
    @Deprecated
    public static boolean isUserRecoverableError(final int n) {
        if (n != 9) {
            switch (n) {
                default: {
                    return false;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return true;
    }
    
    @Deprecated
    public static boolean showErrorDialogFragment(final int n, final Activity activity, final int n2) {
        return showErrorDialogFragment(n, activity, n2, null);
    }
    
    @Deprecated
    public static boolean showErrorDialogFragment(final int n, final Activity activity, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return showErrorDialogFragment(n, activity, null, n2, dialogInterface$OnCancelListener);
    }
    
    public static boolean showErrorDialogFragment(final int n, final Activity activity, final Fragment fragment, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        final Dialog zza = zza(n, activity, fragment, n2, dialogInterface$OnCancelListener);
        if (zza == null) {
            return false;
        }
        zza(activity, dialogInterface$OnCancelListener, "GooglePlayServicesErrorDialog", zza);
        return true;
    }
    
    @Deprecated
    public static void showErrorNotification(final int n, final Context context) {
        int n2 = n;
        if (zzml.zzan(context) && (n2 = n) == 2) {
            n2 = 42;
        }
        if (!zzd(context, n2) && !zzf(context, n2)) {
            zza(n2, context);
            return;
        }
        zzae(context);
    }
    
    private static Dialog zza(final int n, final Activity activity, final Fragment fragment, final int n2, final DialogInterface$OnCancelListener onCancelListener) {
        final AlertDialog$Builder alertDialog$Builder = null;
        if (n == 0) {
            return null;
        }
        int n3 = n;
        if (zzml.zzan((Context)activity) && (n3 = n) == 2) {
            n3 = 42;
        }
        AlertDialog$Builder alertDialog$Builder2 = alertDialog$Builder;
        if (zzmx.zzqx()) {
            final TypedValue typedValue = new TypedValue();
            activity.getTheme().resolveAttribute(16843529, typedValue, true);
            alertDialog$Builder2 = alertDialog$Builder;
            if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                alertDialog$Builder2 = new AlertDialog$Builder((Context)activity, 5);
            }
        }
        AlertDialog$Builder alertDialog$Builder3;
        if ((alertDialog$Builder3 = alertDialog$Builder2) == null) {
            alertDialog$Builder3 = new AlertDialog$Builder((Context)activity);
        }
        alertDialog$Builder3.setMessage((CharSequence)zzg.zzc((Context)activity, n3, zzaf((Context)activity)));
        if (onCancelListener != null) {
            alertDialog$Builder3.setOnCancelListener(onCancelListener);
        }
        final Intent zza = GoogleApiAvailability.getInstance().zza((Context)activity, n3, "d");
        zzh zzh;
        if (fragment == null) {
            zzh = new zzh(activity, zza, n2);
        }
        else {
            zzh = new zzh(fragment, zza, n2);
        }
        final String zzh2 = zzg.zzh((Context)activity, n3);
        if (zzh2 != null) {
            alertDialog$Builder3.setPositiveButton((CharSequence)zzh2, (DialogInterface$OnClickListener)zzh);
        }
        final String zzg = com.google.android.gms.common.internal.zzg.zzg((Context)activity, n3);
        if (zzg != null) {
            alertDialog$Builder3.setTitle((CharSequence)zzg);
        }
        return (Dialog)alertDialog$Builder3.create();
    }
    
    private static void zza(final int n, final Context context) {
        zza(n, context, null);
    }
    
    private static void zza(int n, final Context context, final String s) {
        final Resources resources = context.getResources();
        final String zzaf = zzaf(context);
        String contentTitle;
        if ((contentTitle = zzg.zzi(context, n)) == null) {
            contentTitle = resources.getString(R.string.common_google_play_services_notification_ticker);
        }
        final String zzd = zzg.zzd(context, n, zzaf);
        final PendingIntent zza = GoogleApiAvailability.getInstance().zza(context, n, 0, "n");
        Notification notification;
        if (zzml.zzan(context)) {
            zzx.zzZ(zzmx.zzqy());
            final Notification$Builder setAutoCancel = new Notification$Builder(context).setSmallIcon(R.drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true);
            final Notification$BigTextStyle notification$BigTextStyle = new Notification$BigTextStyle();
            final StringBuilder sb = new StringBuilder();
            sb.append(contentTitle);
            sb.append(" ");
            sb.append(zzd);
            notification = setAutoCancel.setStyle((Notification$Style)notification$BigTextStyle.bigText((CharSequence)sb.toString())).addAction(R.drawable.common_full_open_on_phone, (CharSequence)resources.getString(R.string.common_open_on_phone), zza).build();
        }
        else {
            final String string = resources.getString(R.string.common_google_play_services_notification_ticker);
            if (zzmx.zzqu()) {
                final Notification$Builder setAutoCancel2 = new Notification$Builder(context).setSmallIcon(17301642).setContentTitle((CharSequence)contentTitle).setContentText((CharSequence)zzd).setContentIntent(zza).setTicker((CharSequence)string).setAutoCancel(true);
                if (zzmx.zzqC()) {
                    setAutoCancel2.setLocalOnly(true);
                }
                Notification notification2;
                if (zzmx.zzqy()) {
                    setAutoCancel2.setStyle((Notification$Style)new Notification$BigTextStyle().bigText((CharSequence)zzd));
                    notification2 = setAutoCancel2.build();
                }
                else {
                    notification2 = setAutoCancel2.getNotification();
                }
                notification = notification2;
                if (Build$VERSION.SDK_INT == 19) {
                    notification2.extras.putBoolean("android.support.localOnly", true);
                    notification = notification2;
                }
            }
            else {
                notification = new NotificationCompat.Builder(context).setSmallIcon(17301642).setTicker(string).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(zza).setContentTitle(contentTitle).setContentText(zzd).build();
            }
        }
        if (zzbk(n)) {
            n = 10436;
            GooglePlayServicesUtil.zzaaq.set(false);
        }
        else {
            n = 39789;
        }
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (s != null) {
            notificationManager.notify(s, n, notification);
            return;
        }
        notificationManager.notify(n, notification);
    }
    
    public static void zza(final Activity activity, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener, final String s, final Dialog dialog) {
        boolean b;
        try {
            b = (activity instanceof FragmentActivity);
        }
        catch (final NoClassDefFoundError noClassDefFoundError) {
            b = false;
        }
        if (b) {
            SupportErrorDialogFragment.newInstance(dialog, dialogInterface$OnCancelListener).show(((FragmentActivity)activity).getSupportFragmentManager(), s);
            return;
        }
        if (zzmx.zzqu()) {
            ErrorDialogFragment.newInstance(dialog, dialogInterface$OnCancelListener).show(activity.getFragmentManager(), s);
            return;
        }
        throw new RuntimeException("This Activity does not support Fragments.");
    }
    
    @Deprecated
    public static void zzaa(final Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        final int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (googlePlayServicesAvailable == 0) {
            return;
        }
        final Intent zza = GoogleApiAvailability.getInstance().zza(context, googlePlayServicesAvailable, "e");
        final StringBuilder sb = new StringBuilder();
        sb.append("GooglePlayServices not available due to error ");
        sb.append(googlePlayServicesAvailable);
        Log.e("GooglePlayServicesUtil", sb.toString());
        if (zza == null) {
            throw new GooglePlayServicesNotAvailableException(googlePlayServicesAvailable);
        }
        throw new GooglePlayServicesRepairableException(googlePlayServicesAvailable, "Google Play Services not available", zza);
    }
    
    @Deprecated
    public static void zzac(final Context context) {
        if (GooglePlayServicesUtil.zzaaq.getAndSet(true)) {
            return;
        }
        try {
            ((NotificationManager)context.getSystemService("notification")).cancel(10436);
        }
        catch (final SecurityException ex) {}
    }
    
    private static void zzad(final Context context) {
        if (GooglePlayServicesUtil.zzaar.get()) {
            return;
        }
        Object zzpy = GooglePlayServicesUtil.zzpy;
        synchronized (zzpy) {
            if (GooglePlayServicesUtil.zzaao == null) {
                GooglePlayServicesUtil.zzaao = context.getPackageName();
                try {
                    final Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                    if (metaData != null) {
                        GooglePlayServicesUtil.zzaap = metaData.getInt("com.google.android.gms.version");
                    }
                    else {
                        GooglePlayServicesUtil.zzaap = null;
                    }
                }
                catch (final PackageManager$NameNotFoundException ex) {
                    Log.wtf("GooglePlayServicesUtil", "This should never happen.", (Throwable)ex);
                }
            }
            else if (!GooglePlayServicesUtil.zzaao.equals(context.getPackageName())) {
                final StringBuilder sb = new StringBuilder();
                sb.append("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '");
                sb.append(GooglePlayServicesUtil.zzaao);
                sb.append("' and this call used package '");
                sb.append(context.getPackageName());
                sb.append("'.");
                throw new IllegalArgumentException(sb.toString());
            }
            final Integer zzaap = GooglePlayServicesUtil.zzaap;
            monitorexit(zzpy);
            if (zzaap == null) {
                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
            }
            if (zzaap != GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                zzpy = new StringBuilder();
                ((StringBuilder)zzpy).append("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ");
                ((StringBuilder)zzpy).append(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                ((StringBuilder)zzpy).append(" but");
                ((StringBuilder)zzpy).append(" found ");
                ((StringBuilder)zzpy).append(zzaap);
                ((StringBuilder)zzpy).append(".  You must have the");
                ((StringBuilder)zzpy).append(" following declaration within the <application> element: ");
                ((StringBuilder)zzpy).append("    <meta-data android:name=\"");
                ((StringBuilder)zzpy).append("com.google.android.gms.version");
                ((StringBuilder)zzpy).append("\" android:value=\"@integer/google_play_services_version\" />");
                throw new IllegalStateException(((StringBuilder)zzpy).toString());
            }
        }
    }
    
    private static void zzae(final Context context) {
        final zza zza = new zza(context);
        zza.sendMessageDelayed(zza.obtainMessage(1), 120000L);
    }
    
    public static String zzaf(final Context context) {
        String s;
        if (TextUtils.isEmpty((CharSequence)(s = context.getApplicationInfo().name))) {
            s = context.getPackageName();
            final PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            }
            catch (final PackageManager$NameNotFoundException ex) {
                applicationInfo = null;
            }
            if (applicationInfo != null) {
                s = packageManager.getApplicationLabel(applicationInfo).toString();
            }
        }
        return s;
    }
    
    public static boolean zzag(final Context context) {
        final PackageManager packageManager = context.getPackageManager();
        return zzmx.zzqD() && packageManager.hasSystemFeature("com.google.sidewinder");
    }
    
    public static boolean zzah(final Context context) {
        if (zzmx.zzqA()) {
            final Bundle applicationRestrictions = ((UserManager)context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (applicationRestrictions != null && "true".equals(applicationRestrictions.getString("restricted_profile"))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean zzb(final Context context, int i, final String s) {
        if (zzmx.zzqB()) {
            final AppOpsManager appOpsManager = (AppOpsManager)context.getSystemService("appops");
            try {
                appOpsManager.checkPackage(i, s);
                return true;
            }
            catch (final SecurityException ex) {
                return false;
            }
        }
        final String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (s != null && packagesForUid != null) {
            for (i = 0; i < packagesForUid.length; ++i) {
                if (s.equals(packagesForUid[i])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean zzb(final PackageManager packageManager) {
        synchronized (GooglePlayServicesUtil.zzpy) {
            final int zzaan = GooglePlayServicesUtil.zzaan;
            boolean b = false;
            if (zzaan == -1) {
                try {
                    if (com.google.android.gms.common.zzd.zznu().zza(packageManager.getPackageInfo("com.google.android.gms", 64), zzc.zzaad[1]) != null) {
                        GooglePlayServicesUtil.zzaan = 1;
                    }
                    else {
                        GooglePlayServicesUtil.zzaan = 0;
                    }
                }
                catch (final PackageManager$NameNotFoundException ex) {
                    GooglePlayServicesUtil.zzaan = 0;
                }
            }
            if (GooglePlayServicesUtil.zzaan != 0) {
                b = true;
            }
            return b;
        }
    }
    
    @Deprecated
    public static boolean zzb(final PackageManager packageManager, final String s) {
        return com.google.android.gms.common.zzd.zznu().zzb(packageManager, s);
    }
    
    @Deprecated
    public static Intent zzbj(final int n) {
        return GoogleApiAvailability.getInstance().zza(null, n, null);
    }
    
    private static boolean zzbk(final int n) {
        if (n != 18 && n != 42) {
            switch (n) {
                default: {
                    return false;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return true;
    }
    
    public static boolean zzc(final PackageManager packageManager) {
        return zzb(packageManager) || !zznt();
    }
    
    @Deprecated
    public static boolean zzd(final Context context, final int n) {
        return n == 18 || (n == 1 && zzj(context, "com.google.android.gms"));
    }
    
    public static boolean zze(final Context context, final int n) {
        return zzb(context, n, "com.google.android.gms") && zzb(context.getPackageManager(), "com.google.android.gms");
    }
    
    @Deprecated
    public static boolean zzf(final Context context, final int n) {
        return n == 9 && zzj(context, "com.android.vending");
    }
    
    static boolean zzj(final Context context, final String s) {
        if (zzmx.zzqD()) {
            final Iterator iterator = context.getPackageManager().getPackageInstaller().getAllSessions().iterator();
            while (iterator.hasNext()) {
                if (s.equals(((PackageInstaller$SessionInfo)iterator.next()).getAppPackageName())) {
                    return true;
                }
            }
        }
        if (zzah(context)) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationInfo(s, 8192).enabled;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    private static int zzns() {
        return 8115000;
    }
    
    public static boolean zznt() {
        if (GooglePlayServicesUtil.zzaal) {
            return GooglePlayServicesUtil.zzaam;
        }
        return "user".equals(Build.TYPE);
    }
    
    private static class zza extends Handler
    {
        private final Context zzqZ;
        
        zza(final Context context) {
            Looper looper;
            if (Looper.myLooper() == null) {
                looper = Looper.getMainLooper();
            }
            else {
                looper = Looper.myLooper();
            }
            super(looper);
            this.zzqZ = context.getApplicationContext();
        }
        
        public void handleMessage(final Message message) {
            if (message.what != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Don't know how to handle this message: ");
                sb.append(message.what);
                Log.w("GooglePlayServicesUtil", sb.toString());
                return;
            }
            final int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.zzqZ);
            if (GooglePlayServicesUtil.isUserRecoverableError(googlePlayServicesAvailable)) {
                zza(googlePlayServicesAvailable, this.zzqZ);
            }
        }
    }
}
