// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Api;

public final class zzqx implements Optional
{
    public static final zzqx zzaUZ;
    private final boolean zzTi;
    private final boolean zzTk;
    private final String zzTl;
    private final boolean zzaVa;
    private final GoogleApiClient.ServerAuthCodeCallbacks zzaVb;
    private final boolean zzaVc;
    
    static {
        zzaUZ = new zza().zzCi();
    }
    
    private zzqx(final boolean zzaVa, final boolean zzTi, final String zzTl, final GoogleApiClient.ServerAuthCodeCallbacks zzaVb, final boolean zzaVc, final boolean zzTk) {
        this.zzaVa = zzaVa;
        this.zzTi = zzTi;
        this.zzTl = zzTl;
        this.zzaVb = zzaVb;
        this.zzaVc = zzaVc;
        this.zzTk = zzTk;
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
    
    public static final class zza
    {
        private String zzaSe;
        private boolean zzaVd;
        private boolean zzaVe;
        private GoogleApiClient.ServerAuthCodeCallbacks zzaVf;
        private boolean zzaVg;
        private boolean zzaVh;
        
        private String zzet(final String anObject) {
            zzx.zzw(anObject);
            zzx.zzb(this.zzaSe == null || this.zzaSe.equals(anObject), (Object)"two different server client ids provided");
            return anObject;
        }
        
        public zzqx zzCi() {
            return new zzqx(this.zzaVd, this.zzaVe, this.zzaSe, this.zzaVf, this.zzaVg, this.zzaVh, null);
        }
        
        public zza zza(final String s, final GoogleApiClient.ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzaVd = true;
            this.zzaVe = true;
            this.zzaSe = this.zzet(s);
            this.zzaVf = zzx.zzw(serverAuthCodeCallbacks);
            return this;
        }
    }
}
