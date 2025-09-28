package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzqx implements Api.ApiOptions.Optional {
    public static final zzqx zzaUZ = new zza().zzCi();
    private final boolean zzTi;
    private final boolean zzTk;
    private final String zzTl;
    private final boolean zzaVa;
    private final GoogleApiClient.ServerAuthCodeCallbacks zzaVb;
    private final boolean zzaVc;

    public static final class zza {
        private String zzaSe;
        private boolean zzaVd;
        private boolean zzaVe;
        private GoogleApiClient.ServerAuthCodeCallbacks zzaVf;
        private boolean zzaVg;
        private boolean zzaVh;

        private String zzet(String str) {
            zzx.zzw(str);
            zzx.zzb(this.zzaSe == null || this.zzaSe.equals(str), "two different server client ids provided");
            return str;
        }

        public zzqx zzCi() {
            return new zzqx(this.zzaVd, this.zzaVe, this.zzaSe, this.zzaVf, this.zzaVg, this.zzaVh);
        }

        public zza zza(String str, GoogleApiClient.ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzaVd = true;
            this.zzaVe = true;
            this.zzaSe = zzet(str);
            this.zzaVf = (GoogleApiClient.ServerAuthCodeCallbacks) zzx.zzw(serverAuthCodeCallbacks);
            return this;
        }
    }

    private zzqx(boolean z, boolean z2, String str, GoogleApiClient.ServerAuthCodeCallbacks serverAuthCodeCallbacks, boolean z3, boolean z4) {
        this.zzaVa = z;
        this.zzTi = z2;
        this.zzTl = str;
        this.zzaVb = serverAuthCodeCallbacks;
        this.zzaVc = z3;
        this.zzTk = z4;
    }

    public boolean zzCf() {
        return this.zzaVa;
    }

    public GoogleApiClient.ServerAuthCodeCallbacks zzCg() {
        return this.zzaVb;
    }

    public boolean zzCh() {
        return this.zzaVc;
    }

    public boolean zzlY() {
        return this.zzTi;
    }

    public boolean zzma() {
        return this.zzTk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
