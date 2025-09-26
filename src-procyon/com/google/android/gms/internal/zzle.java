// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Releasable;

public abstract class zzle implements Releasable, Result
{
    protected final Status zzSC;
    protected final DataHolder zzabq;
    
    protected zzle(final DataHolder zzabq, final Status zzSC) {
        this.zzSC = zzSC;
        this.zzabq = zzabq;
    }
    
    @Override
    public Status getStatus() {
        return this.zzSC;
    }
    
    @Override
    public void release() {
        if (this.zzabq != null) {
            this.zzabq.close();
        }
    }
}
