package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzse {
    protected volatile int zzbiv = -1;

    public static final <T extends zzse> T zza(T t, byte[] bArr) throws zzsd {
        return (T) zzb(t, bArr, 0, bArr.length);
    }

    public static final void zza(zzse zzseVar, byte[] bArr, int i, int i2) {
        try {
            zzrx zzrxVarZzb = zzrx.zzb(bArr, i, i2);
            zzseVar.zza(zzrxVarZzb);
            zzrxVarZzb.zzFE();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final <T extends zzse> T zzb(T t, byte[] bArr, int i, int i2) throws zzsd {
        try {
            zzrw zzrwVarZza = zzrw.zza(bArr, i, i2);
            t.zzb(zzrwVarZza);
            zzrwVarZza.zzlz(0);
            return t;
        } catch (zzsd e) {
            throw e;
        } catch (IOException unused) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public static final byte[] zzf(zzse zzseVar) {
        byte[] bArr = new byte[zzseVar.zzFR()];
        zza(zzseVar, bArr, 0, bArr.length);
        return bArr;
    }

    public String toString() {
        return zzsf.zzg(this);
    }

    protected int zzB() {
        return 0;
    }

    @Override // 
    /* renamed from: zzFG, reason: merged with bridge method [inline-methods] */
    public zzse clone() throws CloneNotSupportedException {
        return (zzse) super.clone();
    }

    public int zzFQ() {
        if (this.zzbiv < 0) {
            zzFR();
        }
        return this.zzbiv;
    }

    public int zzFR() {
        int iZzB = zzB();
        this.zzbiv = iZzB;
        return iZzB;
    }

    public void zza(zzrx zzrxVar) throws IOException {
    }

    public abstract zzse zzb(zzrw zzrwVar) throws IOException;
}
