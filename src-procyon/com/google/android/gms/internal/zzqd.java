// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.app.PendingIntent;
import com.google.android.gms.playlog.internal.LogEvent;
import android.view.View;
import com.google.android.gms.common.api.Api;
import java.util.Map;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import android.accounts.Account;
import com.google.android.gms.playlog.internal.zzd;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.Context;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import com.google.android.gms.playlog.internal.zzf;

@Deprecated
public class zzqd
{
    private final zzf zzaRE;
    private PlayLoggerContext zzaRF;
    
    public zzqd(final Context context, final int n, final String s, final String s2, final zza zza, final boolean b, final String s3) {
        final String packageName = context.getPackageName();
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            Log.wtf("PlayLogger", "This can't happen.", (Throwable)ex);
            versionCode = 0;
        }
        this.zzaRF = new PlayLoggerContext(packageName, versionCode, n, s, s2, b);
        this.zzaRE = new zzf(context, context.getMainLooper(), new zzd(zza), new com.google.android.gms.common.internal.zzf(null, null, null, 49, null, packageName, s3, null));
    }
    
    public void start() {
        this.zzaRE.start();
    }
    
    public void stop() {
        this.zzaRE.stop();
    }
    
    public void zza(final long n, final String s, final byte[] array, final String... array2) {
        this.zzaRE.zzb(this.zzaRF, new LogEvent(n, 0L, s, array, array2));
    }
    
    public void zzb(final String s, final byte[] array, final String... array2) {
        this.zza(System.currentTimeMillis(), s, array, array2);
    }
    
    public interface zza
    {
        void zzBr();
        
        void zzBs();
        
        void zzf(final PendingIntent p0);
    }
}
