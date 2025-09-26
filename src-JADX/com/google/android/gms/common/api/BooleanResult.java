package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BooleanResult implements Result {
    private final Status zzSC;
    private final boolean zzaaE;

    public BooleanResult(Status status, boolean z) {
        this.zzSC = (Status) zzx.zzb(status, "Status must not be null");
        this.zzaaE = z;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanResult)) {
            return false;
        }
        BooleanResult booleanResult = (BooleanResult) obj;
        return this.zzSC.equals(booleanResult.zzSC) && this.zzaaE == booleanResult.zzaaE;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.zzSC;
    }

    public boolean getValue() {
        return this.zzaaE;
    }

    public final int hashCode() {
        return (31 * (527 + this.zzSC.hashCode())) + (this.zzaaE ? 1 : 0);
    }
}
