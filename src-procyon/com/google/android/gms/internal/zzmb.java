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

public class zzmb extends zzj<zzmd>
{
    public zzmb(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 39, zzf, connectionCallbacks, onConnectionFailedListener);
    }
    
    protected zzmd zzaO(final IBinder binder) {
        return zzmd.zza.zzaQ(binder);
    }
    
    public String zzfK() {
        return "com.google.android.gms.common.service.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.common.internal.service.ICommonService";
    }
}
