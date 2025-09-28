package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzb<R extends Result, S extends Result> {
    public abstract PendingResult<S> zza(R r);

    public Status zzu(Status status) {
        return status;
    }
}
