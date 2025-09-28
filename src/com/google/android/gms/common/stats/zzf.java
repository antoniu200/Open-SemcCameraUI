package com.google.android.gms.common.stats;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzf {
    public static int zzahY = 0;
    public static int zzahZ = 1;

    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        return getTimeMillis() + "\t" + getEventType() + "\t" + zzqd() + zzqg();
    }

    public abstract long zzqd();

    public abstract String zzqg();
}
