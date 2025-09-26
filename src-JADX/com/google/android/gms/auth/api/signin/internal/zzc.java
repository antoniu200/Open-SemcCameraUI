package com.google.android.gms.auth.api.signin.internal;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzc {
    static int zzTo = 31;
    private int zzTp = 1;

    public zzc zzP(boolean z) {
        this.zzTp = (zzTo * this.zzTp) + (z ? 1 : 0);
        return this;
    }

    public zzc zzl(Object obj) {
        this.zzTp = (zzTo * this.zzTp) + (obj == null ? 0 : obj.hashCode());
        return this;
    }

    public int zzmd() {
        return this.zzTp;
    }
}
