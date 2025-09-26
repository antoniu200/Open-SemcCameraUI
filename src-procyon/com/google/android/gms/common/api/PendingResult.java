// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import java.util.concurrent.TimeUnit;

public abstract class PendingResult<R extends Result>
{
    public abstract R await();
    
    public abstract R await(final long p0, final TimeUnit p1);
    
    public abstract void cancel();
    
    public abstract boolean isCanceled();
    
    public abstract void setResultCallback(final ResultCallback<? super R> p0);
    
    public abstract void setResultCallback(final ResultCallback<? super R> p0, final long p1, final TimeUnit p2);
    
    public void zza(final zza zza) {
        throw new UnsupportedOperationException();
    }
    
    public Integer zznF() {
        throw new UnsupportedOperationException();
    }
    
    public interface zza
    {
        void zzt(final Status p0);
    }
}
