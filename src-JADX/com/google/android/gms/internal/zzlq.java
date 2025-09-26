package com.google.android.gms.internal;

import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zzb;
import com.google.android.gms.common.api.zze;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzlq<R extends Result> extends zze<R> implements ResultCallback<R> {
    private final Object zzabh;
    private zzb<? super R, ? extends Result> zzacY;
    private zzlq<? extends Result> zzacZ;
    private ResultCallbacks<? super R> zzada;
    private PendingResult<R> zzadb;

    private void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                Log.w("TransformedResultImpl", "Unable to release " + result, e);
            }
        }
    }

    private void zzon() {
        if (this.zzadb != null) {
            if (this.zzacY == null && this.zzada == null) {
                return;
            }
            this.zzadb.setResultCallback(this);
        }
    }

    @Override // com.google.android.gms.common.api.ResultCallback
    public void onResult(R r) {
        synchronized (this.zzabh) {
            if (!r.getStatus().isSuccess()) {
                zzx(r.getStatus());
            } else if (this.zzacY != null) {
                PendingResult<S> pendingResultZza = this.zzacY.zza(r);
                if (pendingResultZza == 0) {
                    zzx(new Status(13, "Transform returned null"));
                } else {
                    this.zzacZ.zza(pendingResultZza);
                }
            } else if (this.zzada != null) {
                this.zzada.onSuccess(r);
            }
            zzd(r);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void zza(PendingResult<?> pendingResult) {
        synchronized (this.zzabh) {
            this.zzadb = pendingResult;
            zzon();
        }
    }

    public void zzx(Status status) {
        synchronized (this.zzabh) {
            if (this.zzacY != null) {
                Status statusZzu = this.zzacY.zzu(status);
                zzx.zzb(statusZzu, "onFailure must not return null");
                this.zzacZ.zzx(statusZzu);
            } else if (this.zzada != null) {
                this.zzada.onFailure(status);
            }
        }
    }
}
