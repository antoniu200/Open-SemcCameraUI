// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import android.os.IInterface;

public class zzac<T extends IInterface> extends zzj<T>
{
    private final Api.zzd<T> zzagt;
    
    public zzac(final Context context, final Looper looper, final int n, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, final com.google.android.gms.common.internal.zzf zzf, final Api.zzd zzagt) {
        super(context, looper, n, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzagt = zzagt;
    }
    
    @Override
    protected T zzW(final IBinder binder) {
        return this.zzagt.zzW(binder);
    }
    
    @Override
    protected void zzc(final int n, final T t) {
        this.zzagt.zza(n, t);
    }
    
    @Override
    protected String zzfK() {
        return this.zzagt.zzfK();
    }
    
    @Override
    protected String zzfL() {
        return this.zzagt.zzfL();
    }
}
