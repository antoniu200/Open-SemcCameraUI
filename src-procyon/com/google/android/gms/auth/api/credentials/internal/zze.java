// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import android.os.Bundle;
import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.internal.zzj;

public final class zze extends zzj<com.google.android.gms.auth.api.credentials.internal.zzh>
{
    private final Auth.AuthCredentialsOptions zzSJ;
    
    public zze(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final Auth.AuthCredentialsOptions zzSJ, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 68, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzSJ = zzSJ;
    }
    
    protected com.google.android.gms.auth.api.credentials.internal.zzh zzar(final IBinder binder) {
        return com.google.android.gms.auth.api.credentials.internal.zzh.zza.zzat(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.auth.api.credentials.service.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.auth.api.credentials.internal.ICredentialsService";
    }
    
    @Override
    protected Bundle zzly() {
        if (this.zzSJ == null) {
            return new Bundle();
        }
        return this.zzSJ.zzly();
    }
}
