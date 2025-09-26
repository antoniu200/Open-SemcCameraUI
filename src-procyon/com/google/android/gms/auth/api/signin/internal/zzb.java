// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.zzj;

public class zzb extends zzj<com.google.android.gms.auth.api.signin.internal.zze>
{
    private final GoogleSignInConfig zzTn;
    
    public zzb(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, GoogleSignInConfig zzTh, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 91, zzf, connectionCallbacks, onConnectionFailedListener);
        if (zzTh == null) {
            zzTh = GoogleSignInConfig.zzTh;
        }
        this.zzTn = zzTh;
    }
    
    protected com.google.android.gms.auth.api.signin.internal.zze zzax(final IBinder binder) {
        return com.google.android.gms.auth.api.signin.internal.zze.zza.zzaz(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.auth.api.signin.service.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.auth.api.signin.internal.ISignInService";
    }
}
