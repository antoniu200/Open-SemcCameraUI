package com.google.android.gms.common;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.ProgressBar;
import com.google.android.gms.common.internal.zzn;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleApiAvailability {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailability zzaab = new GoogleApiAvailability();

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzaab;
    }

    private String zzk(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("gcore_");
        sb.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        sb.append(UserSettingValueHolder.DELIMITER);
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
        }
        sb.append(UserSettingValueHolder.DELIMITER);
        if (context != null) {
            sb.append(context.getPackageName());
        }
        sb.append(UserSettingValueHolder.DELIMITER);
        if (context != null) {
            try {
                sb.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return sb.toString();
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2) {
        return GooglePlayServicesUtil.getErrorDialog(i, activity, i2);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        return GooglePlayServicesUtil.getErrorDialog(i, activity, i2, onCancelListener);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return zza(context, i, i2, null);
    }

    public final String getErrorString(int i) {
        return GooglePlayServicesUtil.getErrorString(i);
    }

    public String getOpenSourceSoftwareLicenseInfo(Context context) {
        return GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(context);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.zzd(context, iIsGooglePlayServicesAvailable)) {
            return 18;
        }
        return iIsGooglePlayServicesAvailable;
    }

    public final boolean isUserResolvableError(int i) {
        return GooglePlayServicesUtil.isUserRecoverableError(i);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2) {
        return GooglePlayServicesUtil.showErrorDialogFragment(i, activity, i2);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, DialogInterface.OnCancelListener onCancelListener) {
        return GooglePlayServicesUtil.showErrorDialogFragment(i, activity, i2, onCancelListener);
    }

    public void showErrorNotification(Context context, int i) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        GooglePlayServicesUtil.showErrorNotification(i, context);
    }

    public Dialog zza(Activity activity, DialogInterface.OnCancelListener onCancelListener) throws PackageManager.NameNotFoundException {
        ProgressBar progressBar = new ProgressBar(activity, null, R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(activity.getResources().getString(com.google.android.gms.R.string.common_google_play_services_updating_text, GooglePlayServicesUtil.zzaf(activity)));
        builder.setTitle(com.google.android.gms.R.string.common_google_play_services_updating_title);
        builder.setPositiveButton("", (DialogInterface.OnClickListener) null);
        AlertDialog alertDialogCreate = builder.create();
        GooglePlayServicesUtil.zza(activity, onCancelListener, "GooglePlayServicesUpdatingDialog", alertDialogCreate);
        return alertDialogCreate;
    }

    public PendingIntent zza(Context context, int i, int i2, String str) {
        Intent intentZza = zza(context, i, str);
        if (intentZza == null) {
            return null;
        }
        return PendingIntent.getActivity(context, i2, intentZza, 268435456);
    }

    public Intent zza(Context context, int i, String str) {
        if (i == 42) {
            return zzn.zzpo();
        }
        switch (i) {
            case 1:
            case 2:
                return zzn.zzw("com.google.android.gms", zzk(context, str));
            case 3:
                return zzn.zzco("com.google.android.gms");
            default:
                return null;
        }
    }

    public void zzab(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtil.zzaa(context);
    }

    public void zzac(Context context) {
        GooglePlayServicesUtil.zzac(context);
    }

    @Deprecated
    public Intent zzbi(int i) {
        return zza(null, i, null);
    }

    public boolean zzd(Context context, int i) {
        return GooglePlayServicesUtil.zzd(context, i);
    }

    public boolean zzj(Context context, String str) {
        return GooglePlayServicesUtil.zzj(context, str);
    }
}
