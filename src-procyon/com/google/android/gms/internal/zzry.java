// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzry<M extends zzry<M>> extends zzse
{
    protected zzsa zzbik;
    
    @Override
    protected int zzB() {
        final zzsa zzbik = this.zzbik;
        int n = 0;
        int n3;
        if (zzbik != null) {
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (n >= this.zzbik.size()) {
                    break;
                }
                n2 += this.zzbik.zzlS(n).zzB();
                ++n;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    public M zzFF() throws CloneNotSupportedException {
        final zzry zzry = (zzry)super.zzFG();
        zzsc.zza(this, zzry);
        return (M)zzry;
    }
    
    public final <T> T zza(final zzrz<M, T> zzrz) {
        if (this.zzbik == null) {
            return null;
        }
        final zzsb zzlR = this.zzbik.zzlR(zzsh.zzlV(zzrz.tag));
        if (zzlR == null) {
            return null;
        }
        return zzlR.zzb(zzrz);
    }
    
    @Override
    public void zza(final zzrx zzrx) throws IOException {
        if (this.zzbik == null) {
            return;
        }
        for (int i = 0; i < this.zzbik.size(); ++i) {
            this.zzbik.zzlS(i).zza(zzrx);
        }
    }
    
    protected final boolean zza(final zzrw zzrw, final int n) throws IOException {
        final int position = zzrw.getPosition();
        if (!zzrw.zzlA(n)) {
            return false;
        }
        final int zzlV = zzsh.zzlV(n);
        final zzsg zzsg = new zzsg(n, zzrw.zzx(position, zzrw.getPosition() - position));
        zzsb zzlR = null;
        if (this.zzbik == null) {
            this.zzbik = new zzsa();
        }
        else {
            zzlR = this.zzbik.zzlR(zzlV);
        }
        zzsb zzsb = zzlR;
        if (zzlR == null) {
            zzsb = new zzsb();
            this.zzbik.zza(zzlV, zzsb);
        }
        zzsb.zza(zzsg);
        return true;
    }
}
