// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

public class zzc
{
    static int zzTo = 31;
    private int zzTp;
    
    public zzc() {
        this.zzTp = 1;
    }
    
    public zzc zzP(final boolean b) {
        this.zzTp = zzc.zzTo * this.zzTp + (b ? 1 : 0);
        return this;
    }
    
    public zzc zzl(final Object o) {
        final int zzTo = zzc.zzTo;
        final int zzTp = this.zzTp;
        int hashCode;
        if (o == null) {
            hashCode = 0;
        }
        else {
            hashCode = o.hashCode();
        }
        this.zzTp = zzTo * zzTp + hashCode;
        return this;
    }
    
    public int zzmd() {
        return this.zzTp;
    }
}
