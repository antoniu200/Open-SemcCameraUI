package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class BatchResult implements Result {
    private final Status zzSC;
    private final PendingResult<?>[] zzaaB;

    BatchResult(Status status, PendingResult<?>[] pendingResultArr) {
        this.zzSC = status;
        this.zzaaB = pendingResultArr;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.zzSC;
    }

    public <R extends Result> R take(BatchResultToken<R> batchResultToken) {
        zzx.zzb(batchResultToken.mId < this.zzaaB.length, "The result token does not belong to this batch");
        return (R) this.zzaaB[batchResultToken.mId].await(0L, TimeUnit.MILLISECONDS);
    }
}
