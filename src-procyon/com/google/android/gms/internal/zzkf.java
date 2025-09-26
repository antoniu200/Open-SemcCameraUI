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

public class zzkf extends zzj<zzkg>
{
    public zzkf(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 92, zzf, connectionCallbacks, onConnectionFailedListener);
    }
    
    protected zzkg zzap(final IBinder binder) {
        return zzkg.zza.zzaq(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.auth.api.consent.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.auth.api.consent.internal.IConsentService";
    }
}
