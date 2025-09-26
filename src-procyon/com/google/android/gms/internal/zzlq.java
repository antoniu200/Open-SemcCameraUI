// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.Status;
import android.util.Log;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.zzb;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.zze;
import com.google.android.gms.common.api.Result;

public class zzlq<R extends Result> extends zze<R> implements ResultCallback<R>
{
    private final Object zzabh;
    private zzb<? super R, ? extends Result> zzacY;
    private zzlq<? extends Result> zzacZ;
    private ResultCallbacks<? super R> zzada;
    private PendingResult<R> zzadb;
    
    private void zzd(final Result obj) {
        if (obj instanceof Releasable) {
            try {
                ((Releasable)obj).release();
            }
            catch (final RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to release ");
                sb.append(obj);
                Log.w("TransformedResultImpl", sb.toString(), (Throwable)ex);
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
    
    @Override
    public void onResult(final R r) {
        synchronized (this.zzabh) {
            if (r.getStatus().isSuccess()) {
                if (this.zzacY != null) {
                    final PendingResult<? extends Result> zza = this.zzacY.zza(r);
                    if (zza == null) {
                        this.zzx(new Status(13, "Transform returned null"));
                    }
                    else {
                        this.zzacZ.zza(zza);
                    }
                }
                else {
                    if (this.zzada != null) {
                        this.zzada.onSuccess(r);
                    }
                    return;
                }
            }
            else {
                this.zzx(r.getStatus());
            }
            this.zzd(r);
        }
    }
    
    public void zza(final PendingResult<?> zzadb) {
        synchronized (this.zzabh) {
            this.zzadb = (PendingResult<R>)zzadb;
            this.zzon();
        }
    }
    
    public void zzx(Status zzu) {
        synchronized (this.zzabh) {
            if (this.zzacY != null) {
                zzu = this.zzacY.zzu(zzu);
                zzx.zzb(zzu, "onFailure must not return null");
                this.zzacZ.zzx(zzu);
            }
            else if (this.zzada != null) {
                this.zzada.onFailure(zzu);
            }
        }
    }
}
