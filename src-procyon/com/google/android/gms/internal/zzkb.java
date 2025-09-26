// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.internal.zzj;

public class zzkb extends zzj<zzkd>
{
    public zzkb(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 74, zzf, connectionCallbacks, onConnectionFailedListener);
    }
    
    protected zzkd zzam(final IBinder binder) {
        return zzkd.zza.zzao(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.auth.api.accountstatus.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService";
    }
}
