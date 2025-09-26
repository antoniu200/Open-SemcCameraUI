// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.signin.internal.zzh;
import java.util.concurrent.Executors;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.internal.zzi;
import com.google.android.gms.common.api.Api;

public final class zzqu
{
    public static final Api<zzqx> API;
    public static final Api.zzc<zzi> zzRk;
    public static final Api.zza<zzi, zzqx> zzRl;
    public static final Scope zzTe;
    public static final Scope zzTf;
    static final Api.zza<zzi, Api.ApiOptions.NoOptions> zzaUX;
    public static final zzqv zzaUY;
    public static final Api<Api.ApiOptions.NoOptions> zzaiH;
    public static final Api.zzc<zzi> zzapF;
    
    static {
        zzRk = new Api.zzc();
        zzapF = new Api.zzc();
        zzRl = new Api.zza<zzi, zzqx>() {
            public zzi zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final zzqx zzqx, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                zzqx zzaUZ = zzqx;
                if (zzqx == null) {
                    zzaUZ = zzqx.zzaUZ;
                }
                return new zzi(context, looper, true, zzf, zzaUZ, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
            }
        };
        zzaUX = new Api.zza<zzi, Api.ApiOptions.NoOptions>() {
            public zzi zzt(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzi(context, looper, false, zzf, zzqx.zzaUZ, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
            }
        };
        zzTe = new Scope("profile");
        zzTf = new Scope("email");
        API = new Api<zzqx>("SignIn.API", (Api.zza<C, zzqx>)zzqu.zzRl, (Api.zzc<C>)zzqu.zzRk);
        zzaiH = new Api<Api.ApiOptions.NoOptions>("SignIn.INTERNAL_API", (Api.zza<C, Api.ApiOptions.NoOptions>)zzqu.zzaUX, (Api.zzc<C>)zzqu.zzapF);
        zzaUY = new zzh();
    }
}
