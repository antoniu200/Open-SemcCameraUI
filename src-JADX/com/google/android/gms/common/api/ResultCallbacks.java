package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlc;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ResultCallbacks<R extends Result> implements ResultCallback<R> {
    public abstract void onFailure(Status status);

    @Override // com.google.android.gms.common.api.ResultCallback
    public final void onResult(R r) {
        Status status = r.getStatus();
        if (status.isSuccess()) {
            onSuccess(r);
        } else {
            onFailure(status);
            zzlc.zzd(r);
        }
    }

    public abstract void onSuccess(R r);
}
