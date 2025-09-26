// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import com.google.android.gms.common.internal.zzn;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface$OnClickListener;
import com.google.android.gms.R;
import android.view.View;
import android.app.AlertDialog$Builder;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.app.PendingIntent;
import android.content.DialogInterface$OnCancelListener;
import android.app.Dialog;
import android.app.Activity;
import android.content.pm.PackageManager$NameNotFoundException;
import android.text.TextUtils;
import android.content.Context;

public class GoogleApiAvailability
{
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailability zzaab;
    
    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        zzaab = new GoogleApiAvailability();
    }
    
    GoogleApiAvailability() {
    }
    
    public static GoogleApiAvailability getInstance() {
        return GoogleApiAvailability.zzaab;
    }
    
    private String zzk(final Context context, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("gcore_");
        sb.append(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        sb.append("-");
        if (!TextUtils.isEmpty((CharSequence)str)) {
            sb.append(str);
        }
        sb.append("-");
        if (context != null) {
            sb.append(context.getPackageName());
        }
        sb.append("-");
        Label_0094: {
            if (context == null) {
                break Label_0094;
            }
            try {
                sb.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                return sb.toString();
            }
            catch (final PackageManager$NameNotFoundException ex) {
                return sb.toString();
            }
        }
    }
    
    public Dialog getErrorDialog(final Activity activity, final int n, final int n2) {
        return GooglePlayServicesUtil.getErrorDialog(n, activity, n2);
    }
    
    public Dialog getErrorDialog(final Activity activity, final int n, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return GooglePlayServicesUtil.getErrorDialog(n, activity, n2, dialogInterface$OnCancelListener);
    }
    
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final int n, final int n2) {
        return this.zza(context, n, n2, null);
    }
    
    public final String getErrorString(final int n) {
        return GooglePlayServicesUtil.getErrorString(n);
    }
    
    public String getOpenSourceSoftwareLicenseInfo(final Context context) {
        return GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(context);
    }
    
    public int isGooglePlayServicesAvailable(final Context context) {
        int googlePlayServicesAvailable;
        if (GooglePlayServicesUtil.zzd(context, googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context))) {
            googlePlayServicesAvailable = 18;
        }
        return googlePlayServicesAvailable;
    }
    
    public final boolean isUserResolvableError(final int n) {
        return GooglePlayServicesUtil.isUserRecoverableError(n);
    }
    
    public boolean showErrorDialogFragment(final Activity activity, final int n, final int n2) {
        return GooglePlayServicesUtil.showErrorDialogFragment(n, activity, n2);
    }
    
    public boolean showErrorDialogFragment(final Activity activity, final int n, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return GooglePlayServicesUtil.showErrorDialogFragment(n, activity, n2, dialogInterface$OnCancelListener);
    }
    
    public void showErrorNotification(final Context context, final int n) {
        GooglePlayServicesUtil.showErrorNotification(n, context);
    }
    
    public Dialog zza(final Activity activity, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        final ProgressBar view = new ProgressBar((Context)activity, (AttributeSet)null, 16842874);
        view.setIndeterminate(true);
        view.setVisibility(0);
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
        alertDialog$Builder.setView((View)view);
        alertDialog$Builder.setMessage((CharSequence)activity.getResources().getString(R.string.common_google_play_services_updating_text, new Object[] { GooglePlayServicesUtil.zzaf((Context)activity) }));
        alertDialog$Builder.setTitle(R.string.common_google_play_services_updating_title);
        alertDialog$Builder.setPositiveButton((CharSequence)"", (DialogInterface$OnClickListener)null);
        final AlertDialog create = alertDialog$Builder.create();
        GooglePlayServicesUtil.zza(activity, dialogInterface$OnCancelListener, "GooglePlayServicesUpdatingDialog", (Dialog)create);
        return (Dialog)create;
    }
    
    public PendingIntent zza(final Context context, final int n, final int n2, final String s) {
        final Intent zza = this.zza(context, n, s);
        if (zza == null) {
            return null;
        }
        return PendingIntent.getActivity(context, n2, zza, 268435456);
    }
    
    public Intent zza(final Context context, final int n, final String s) {
        if (n == 42) {
            return zzn.zzpo();
        }
        switch (n) {
            default: {
                return null;
            }
            case 3: {
                return zzn.zzco("com.google.android.gms");
            }
            case 1:
            case 2: {
                return zzn.zzw("com.google.android.gms", this.zzk(context, s));
            }
        }
    }
    
    public void zzab(final Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtil.zzaa(context);
    }
    
    public void zzac(final Context context) {
        GooglePlayServicesUtil.zzac(context);
    }
    
    @Deprecated
    public Intent zzbi(final int n) {
        return this.zza(null, n, null);
    }
    
    public boolean zzd(final Context context, final int n) {
        return GooglePlayServicesUtil.zzd(context, n);
    }
    
    public boolean zzj(final Context context, final String s) {
        return GooglePlayServicesUtil.zzj(context, s);
    }
}
