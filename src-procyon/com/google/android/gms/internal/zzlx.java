// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.api.Api;

public final class zzlx
{
    public static final Api<Api.ApiOptions.NoOptions> API;
    public static final Api.zzc<zzmb> zzRk;
    private static final Api.zza<zzmb, Api.ApiOptions.NoOptions> zzRl;
    public static final zzly zzagw;
    
    static {
        zzRk = new Api.zzc();
        zzRl = new Api.zza<zzmb, Api.ApiOptions.NoOptions>() {
            public zzmb zze(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzmb(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            }
        };
        API = new Api<Api.ApiOptions.NoOptions>("Common.API", (Api.zza<C, Api.ApiOptions.NoOptions>)zzlx.zzRl, (Api.zzc<C>)zzlx.zzRk);
        zzagw = new zzlz();
    }
}
