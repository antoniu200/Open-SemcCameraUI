// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

public abstract class zzb<R extends Result, S extends Result>
{
    public abstract PendingResult<S> zza(final R p0);
    
    public Status zzu(final Status status) {
        return status;
    }
}
