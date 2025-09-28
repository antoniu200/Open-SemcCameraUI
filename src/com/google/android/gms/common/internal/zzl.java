package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzl {
    private static final Object zzafW = new Object();
    private static zzl zzafX;

    public static zzl zzal(Context context) {
        synchronized (zzafW) {
            if (zzafX == null) {
                zzafX = new zzm(context.getApplicationContext());
            }
        }
        return zzafX;
    }

    public abstract boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str);

    public abstract boolean zza(String str, ServiceConnection serviceConnection, String str2);

    public abstract void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str);

    public abstract void zzb(String str, ServiceConnection serviceConnection, String str2);
}
