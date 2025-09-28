package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.playlog.internal.LogEvent;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import com.google.android.gms.playlog.internal.zzd;
import com.google.android.gms.playlog.internal.zzf;

@Deprecated
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzqd {
    private final zzf zzaRE;
    private PlayLoggerContext zzaRF;

    public interface zza {
        void zzBr();

        void zzBs();

        void zzf(PendingIntent pendingIntent);
    }

    public zzqd(Context context, int i, String str, String str2, zza zzaVar, boolean z, String str3) {
        int i2;
        String packageName = context.getPackageName();
        try {
            i2 = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf("PlayLogger", "This can't happen.", e);
            i2 = 0;
        }
        this.zzaRF = new PlayLoggerContext(packageName, i2, i, str, str2, z);
        this.zzaRE = new zzf(context, context.getMainLooper(), new zzd(zzaVar), new com.google.android.gms.common.internal.zzf(null, null, null, 49, null, packageName, str3, null));
    }

    public void start() {
        this.zzaRE.start();
    }

    public void stop() {
        this.zzaRE.stop();
    }

    public void zza(long j, String str, byte[] bArr, String... strArr) {
        this.zzaRE.zzb(this.zzaRF, new LogEvent(j, 0L, str, bArr, strArr));
    }

    public void zzb(String str, byte[] bArr, String... strArr) {
        zza(System.currentTimeMillis(), str, bArr, strArr);
    }
}
