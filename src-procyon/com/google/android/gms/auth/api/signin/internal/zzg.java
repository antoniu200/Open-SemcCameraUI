// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.internal.zzj;

public class zzg extends zzj<com.google.android.gms.auth.api.signin.internal.zze>
{
    private final com.google.android.gms.auth.api.signin.zzg zzTq;
    
    public zzg(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final com.google.android.gms.auth.api.signin.zzg zzg, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 87, zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzTq = zzx.zzw(zzg);
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
