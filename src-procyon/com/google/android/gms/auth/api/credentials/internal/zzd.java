// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import com.google.android.gms.common.api.Api;
import android.os.RemoteException;
import android.os.DeadObjectException;
import android.content.Context;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.common.api.Result;

abstract class zzd<R extends Result> extends zza<R, com.google.android.gms.auth.api.credentials.internal.zze>
{
    zzd(final GoogleApiClient googleApiClient) {
        super(Auth.zzRF, googleApiClient);
    }
    
    protected abstract void zza(final Context p0, final zzh p1) throws DeadObjectException, RemoteException;
    
    protected final void zza(final com.google.android.gms.auth.api.credentials.internal.zze zze) throws DeadObjectException, RemoteException {
        this.zza(zze.getContext(), zze.zzpc());
    }
}
