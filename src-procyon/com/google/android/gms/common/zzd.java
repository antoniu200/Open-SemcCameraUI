// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.PackageManager;
import java.util.Set;
import android.util.Base64;
import android.util.Log;
import android.content.pm.PackageInfo;

public class zzd
{
    private static final zzd zzaas;
    
    static {
        zzaas = new zzd();
    }
    
    private zzd() {
    }
    
    private boolean zza(final PackageInfo packageInfo, final boolean b) {
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return false;
        }
        final zzc.zzb zzb = new zzc.zzb(packageInfo.signatures[0].toByteArray());
        Set<zzc.zza> set;
        if (b) {
            set = zzc.zznp();
        }
        else {
            set = zzc.zznq();
        }
        if (set.contains(zzb)) {
            return true;
        }
        if (Log.isLoggable("GoogleSignatureVerifier", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Signature not valid.  Found: \n");
            sb.append(Base64.encodeToString(((zzc.zza)zzb).getBytes(), 0));
            Log.v("GoogleSignatureVerifier", sb.toString());
        }
        return false;
    }
    
    public static zzd zznu() {
        return zzd.zzaas;
    }
    
    zzc.zza zza(final PackageInfo packageInfo, final zzc.zza... array) {
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        final zzc.zzb zzb = new zzc.zzb(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(zzb)) {
                return array[i];
            }
        }
        if (Log.isLoggable("GoogleSignatureVerifier", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Signature not valid.  Found: \n");
            sb.append(Base64.encodeToString(((zzc.zza)zzb).getBytes(), 0));
            Log.v("GoogleSignatureVerifier", sb.toString());
        }
        return null;
    }
    
    public boolean zza(final PackageManager packageManager, final PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (GooglePlayServicesUtil.zzc(packageManager)) {
            return this.zza(packageInfo, true);
        }
        final boolean zza = this.zza(packageInfo, false);
        if (!zza && this.zza(packageInfo, true)) {
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return zza;
    }
    
    public boolean zzb(final PackageManager packageManager, final String str) {
        try {
            return this.zza(packageManager, packageManager.getPackageInfo(str, 64));
        }
        catch (final PackageManager$NameNotFoundException ex) {
            if (Log.isLoggable("GoogleSignatureVerifier", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Package manager can't find package ");
                sb.append(str);
                sb.append(", defaulting to false");
                Log.d("GoogleSignatureVerifier", sb.toString());
            }
            return false;
        }
    }
}
