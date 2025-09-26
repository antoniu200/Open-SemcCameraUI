// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Api;
import android.os.RemoteException;
import android.content.Context;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.proxy.ProxyApi;

abstract class zzkl extends zza<ProxyApi.ProxyResult, zzki>
{
    public zzkl(final GoogleApiClient googleApiClient) {
        super(Auth.zzRE, googleApiClient);
    }
    
    protected abstract void zza(final Context p0, final zzkk p1) throws RemoteException;
    
    protected final void zza(final zzki zzki) throws RemoteException {
        this.zza(zzki.getContext(), zzki.zzpc());
    }
    
    protected ProxyApi.ProxyResult zzj(final Status status) {
        return new zzkn(status);
    }
}
