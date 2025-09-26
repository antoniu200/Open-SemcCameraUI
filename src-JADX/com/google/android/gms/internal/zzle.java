package com.google.android.gms.internal;

import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzle implements Releasable, Result {
    protected final Status zzSC;
    protected final DataHolder zzabq;

    protected zzle(DataHolder dataHolder, Status status) {
        this.zzSC = status;
        this.zzabq = dataHolder;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.zzSC;
    }

    @Override // com.google.android.gms.common.api.Releasable
    public void release() {
        if (this.zzabq != null) {
            this.zzabq.close();
        }
    }
}
