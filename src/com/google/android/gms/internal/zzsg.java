package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class zzsg {
    final int tag;
    final byte[] zzbiw;

    zzsg(int i, byte[] bArr) {
        this.tag = i;
        this.zzbiw = bArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsg)) {
            return false;
        }
        zzsg zzsgVar = (zzsg) obj;
        return this.tag == zzsgVar.tag && Arrays.equals(this.zzbiw, zzsgVar.zzbiw);
    }

    public int hashCode() {
        return (31 * (527 + this.tag)) + Arrays.hashCode(this.zzbiw);
    }

    int zzB() {
        return 0 + zzrx.zzlO(this.tag) + this.zzbiw.length;
    }

    void zza(zzrx zzrxVar) throws IOException {
        zzrxVar.zzlN(this.tag);
        zzrxVar.zzF(this.zzbiw);
    }
}
