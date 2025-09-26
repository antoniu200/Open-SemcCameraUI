// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.net.Uri;
import android.database.CharArrayBuffer;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

public abstract class zzc
{
    protected final DataHolder zzabq;
    protected int zzadl;
    private int zzadm;
    
    public zzc(final DataHolder dataHolder, final int n) {
        this.zzabq = zzx.zzw(dataHolder);
        this.zzbr(n);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof zzc;
        boolean b3;
        final boolean b2 = b3 = false;
        if (b) {
            final zzc zzc = (zzc)o;
            b3 = b2;
            if (zzw.equal(zzc.zzadl, this.zzadl)) {
                b3 = b2;
                if (zzw.equal(zzc.zzadm, this.zzadm)) {
                    b3 = b2;
                    if (zzc.zzabq == this.zzabq) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    protected boolean getBoolean(final String s) {
        return this.zzabq.zze(s, this.zzadl, this.zzadm);
    }
    
    protected byte[] getByteArray(final String s) {
        return this.zzabq.zzg(s, this.zzadl, this.zzadm);
    }
    
    protected float getFloat(final String s) {
        return this.zzabq.zzf(s, this.zzadl, this.zzadm);
    }
    
    protected int getInteger(final String s) {
        return this.zzabq.zzc(s, this.zzadl, this.zzadm);
    }
    
    protected long getLong(final String s) {
        return this.zzabq.zzb(s, this.zzadl, this.zzadm);
    }
    
    protected String getString(final String s) {
        return this.zzabq.zzd(s, this.zzadl, this.zzadm);
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.zzadl, this.zzadm, this.zzabq);
    }
    
    public boolean isDataValid() {
        return this.zzabq.isClosed() ^ true;
    }
    
    protected void zza(final String s, final CharArrayBuffer charArrayBuffer) {
        this.zzabq.zza(s, this.zzadl, this.zzadm, charArrayBuffer);
    }
    
    protected void zzbr(final int zzadl) {
        zzx.zzZ(zzadl >= 0 && zzadl < this.zzabq.getCount());
        this.zzadl = zzadl;
        this.zzadm = this.zzabq.zzbt(this.zzadl);
    }
    
    public boolean zzce(final String s) {
        return this.zzabq.zzce(s);
    }
    
    protected Uri zzcf(final String s) {
        return this.zzabq.zzh(s, this.zzadl, this.zzadm);
    }
    
    protected boolean zzcg(final String s) {
        return this.zzabq.zzi(s, this.zzadl, this.zzadm);
    }
    
    protected int zzou() {
        return this.zzadl;
    }
}
