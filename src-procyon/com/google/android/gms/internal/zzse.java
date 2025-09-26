// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzse
{
    protected volatile int zzbiv;
    
    public zzse() {
        this.zzbiv = -1;
    }
    
    public static final <T extends zzse> T zza(final T t, final byte[] array) throws zzsd {
        return zzb(t, array, 0, array.length);
    }
    
    public static final void zza(final zzse zzse, final byte[] array, final int n, final int n2) {
        try {
            final zzrx zzb = zzrx.zzb(array, n, n2);
            zzse.zza(zzb);
            zzb.zzFE();
        }
        catch (final IOException cause) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", cause);
        }
    }
    
    public static final <T extends zzse> T zzb(final T t, final byte[] array, final int n, final int n2) throws zzsd {
        try {
            final zzrw zza = zzrw.zza(array, n, n2);
            t.zzb(zza);
            zza.zzlz(0);
            return t;
        }
        catch (final IOException ex) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
        catch (final zzsd zzsd) {
            throw zzsd;
        }
    }
    
    public static final byte[] zzf(final zzse zzse) {
        final byte[] array = new byte[zzse.zzFR()];
        zza(zzse, array, 0, array.length);
        return array;
    }
    
    @Override
    public String toString() {
        return zzsf.zzg(this);
    }
    
    protected int zzB() {
        return 0;
    }
    
    public zzse zzFG() throws CloneNotSupportedException {
        return (zzse)super.clone();
    }
    
    public int zzFQ() {
        if (this.zzbiv < 0) {
            this.zzFR();
        }
        return this.zzbiv;
    }
    
    public int zzFR() {
        return this.zzbiv = this.zzB();
    }
    
    public void zza(final zzrx zzrx) throws IOException {
    }
    
    public abstract zzse zzb(final zzrw p0) throws IOException;
}
