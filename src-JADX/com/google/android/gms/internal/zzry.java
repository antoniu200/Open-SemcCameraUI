package com.google.android.gms.internal;

import com.google.android.gms.internal.zzry;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzry<M extends zzry<M>> extends zzse {
    protected zzsa zzbik;

    @Override // com.google.android.gms.internal.zzse
    protected int zzB() {
        if (this.zzbik == null) {
            return 0;
        }
        int iZzB = 0;
        for (int i = 0; i < this.zzbik.size(); i++) {
            iZzB += this.zzbik.zzlS(i).zzB();
        }
        return iZzB;
    }

    @Override // com.google.android.gms.internal.zzse
    /* renamed from: zzFF, reason: merged with bridge method [inline-methods] */
    public M clone() throws CloneNotSupportedException {
        M m = (M) super.clone();
        zzsc.zza(this, m);
        return m;
    }

    public final <T> T zza(zzrz<M, T> zzrzVar) {
        zzsb zzsbVarZzlR;
        if (this.zzbik == null || (zzsbVarZzlR = this.zzbik.zzlR(zzsh.zzlV(zzrzVar.tag))) == null) {
            return null;
        }
        return (T) zzsbVarZzlR.zzb(zzrzVar);
    }

    @Override // com.google.android.gms.internal.zzse
    public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbik == null) {
            return;
        }
        for (int i = 0; i < this.zzbik.size(); i++) {
            this.zzbik.zzlS(i).zza(zzrxVar);
        }
    }

    protected final boolean zza(zzrw zzrwVar, int i) throws IOException {
        int position = zzrwVar.getPosition();
        if (!zzrwVar.zzlA(i)) {
            return false;
        }
        int iZzlV = zzsh.zzlV(i);
        zzsg zzsgVar = new zzsg(i, zzrwVar.zzx(position, zzrwVar.getPosition() - position));
        zzsb zzsbVarZzlR = null;
        if (this.zzbik == null) {
            this.zzbik = new zzsa();
        } else {
            zzsbVarZzlR = this.zzbik.zzlR(iZzlV);
        }
        if (zzsbVarZzlR == null) {
            zzsbVarZzlR = new zzsb();
            this.zzbik.zza(iZzlV, zzsbVarZzlR);
        }
        zzsbVarZzlR.zza(zzsgVar);
        return true;
    }
}
