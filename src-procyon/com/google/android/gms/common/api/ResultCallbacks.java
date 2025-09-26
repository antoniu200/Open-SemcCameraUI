// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import com.google.android.gms.internal.zzlc;

public abstract class ResultCallbacks<R extends Result> implements ResultCallback<R>
{
    public abstract void onFailure(final Status p0);
    
    @Override
    public final void onResult(final R r) {
        final Status status = r.getStatus();
        if (status.isSuccess()) {
            this.onSuccess(r);
            return;
        }
        this.onFailure(status);
        zzlc.zzd(r);
    }
    
    public abstract void onSuccess(final R p0);
}
