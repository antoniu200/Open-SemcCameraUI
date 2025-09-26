// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import java.util.concurrent.TimeUnit;
import com.google.android.gms.common.internal.zzx;

public final class BatchResult implements Result
{
    private final Status zzSC;
    private final PendingResult<?>[] zzaaB;
    
    BatchResult(final Status zzSC, final PendingResult<?>[] zzaaB) {
        this.zzSC = zzSC;
        this.zzaaB = zzaaB;
    }
    
    @Override
    public Status getStatus() {
        return this.zzSC;
    }
    
    public <R extends Result> R take(final BatchResultToken<R> batchResultToken) {
        zzx.zzb(batchResultToken.mId < this.zzaaB.length, (Object)"The result token does not belong to this batch");
        return (R)this.zzaaB[batchResultToken.mId].await(0L, TimeUnit.MILLISECONDS);
    }
}
