package com.google.android.gms.internal;

import android.os.SystemClock;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzmp implements zzmn {
    private static zzmp zzaik;

    public static synchronized zzmn zzqt() {
        if (zzaik == null) {
            zzaik = new zzmp();
        }
        return zzaik;
    }

    @Override // com.google.android.gms.internal.zzmn
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.google.android.gms.internal.zzmn
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.google.android.gms.internal.zzmn
    public long nanoTime() {
        return System.nanoTime();
    }
}
