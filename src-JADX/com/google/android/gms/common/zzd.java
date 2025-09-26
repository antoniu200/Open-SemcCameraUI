package com.google.android.gms.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.zzc;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzd {
    private static final zzd zzaas = new zzd();

    private zzd() {
    }

    private boolean zza(PackageInfo packageInfo, boolean z) {
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return false;
        }
        zzc.zzb zzbVar = new zzc.zzb(packageInfo.signatures[0].toByteArray());
        if ((z ? zzc.zznp() : zzc.zznq()).contains(zzbVar)) {
            return true;
        }
        if (Log.isLoggable("GoogleSignatureVerifier", 2)) {
            Log.v("GoogleSignatureVerifier", "Signature not valid.  Found: \n" + Base64.encodeToString(zzbVar.getBytes(), 0));
        }
        return false;
    }

    public static zzd zznu() {
        return zzaas;
    }

    zzc.zza zza(PackageInfo packageInfo, zzc.zza... zzaVarArr) {
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzc.zzb zzbVar = new zzc.zzb(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < zzaVarArr.length; i++) {
            if (zzaVarArr[i].equals(zzbVar)) {
                return zzaVarArr[i];
            }
        }
        if (Log.isLoggable("GoogleSignatureVerifier", 2)) {
            Log.v("GoogleSignatureVerifier", "Signature not valid.  Found: \n" + Base64.encodeToString(zzbVar.getBytes(), 0));
        }
        return null;
    }

    public boolean zza(PackageManager packageManager, PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (GooglePlayServicesUtil.zzc(packageManager)) {
            return zza(packageInfo, true);
        }
        boolean zZza = zza(packageInfo, false);
        if (!zZza && zza(packageInfo, true)) {
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return zZza;
    }

    public boolean zzb(PackageManager packageManager, String str) {
        try {
            return zza(packageManager, packageManager.getPackageInfo(str, 64));
        } catch (PackageManager.NameNotFoundException unused) {
            if (!Log.isLoggable("GoogleSignatureVerifier", 3)) {
                return false;
            }
            Log.d("GoogleSignatureVerifier", "Package manager can't find package " + str + ", defaulting to false");
            return false;
        }
    }
}
