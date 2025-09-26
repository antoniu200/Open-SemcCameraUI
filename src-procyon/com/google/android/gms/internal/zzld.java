// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;

public abstract class zzld<L> implements zzb<L>
{
    private final DataHolder zzabq;
    
    protected zzld(final DataHolder zzabq) {
        this.zzabq = zzabq;
    }
    
    protected abstract void zza(final L p0, final DataHolder p1);
    
    @Override
    public void zznN() {
        if (this.zzabq != null) {
            this.zzabq.close();
        }
    }
    
    @Override
    public final void zzq(final L l) {
        this.zza(l, this.zzabq);
    }
}
