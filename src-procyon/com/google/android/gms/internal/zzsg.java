// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

final class zzsg
{
    final int tag;
    final byte[] zzbiw;
    
    zzsg(final int tag, final byte[] zzbiw) {
        this.tag = tag;
        this.zzbiw = zzbiw;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsg)) {
            return false;
        }
        final zzsg zzsg = (zzsg)o;
        return this.tag == zzsg.tag && Arrays.equals(this.zzbiw, zzsg.zzbiw);
    }
    
    @Override
    public int hashCode() {
        return 31 * (527 + this.tag) + Arrays.hashCode(this.zzbiw);
    }
    
    int zzB() {
        return 0 + zzrx.zzlO(this.tag) + this.zzbiw.length;
    }
    
    void zza(final zzrx zzrx) throws IOException {
        zzrx.zzlN(this.tag);
        zzrx.zzF(this.zzbiw);
    }
}
