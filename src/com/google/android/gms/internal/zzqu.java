package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.signin.internal.zzh;
import com.google.android.gms.signin.internal.zzi;
import java.util.concurrent.Executors;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzqu {
    public static final Api.zzc<zzi> zzRk = new Api.zzc<>();
    public static final Api.zzc<zzi> zzapF = new Api.zzc<>();
    public static final Api.zza<zzi, zzqx> zzRl = new Api.zza<zzi, zzqx>() { // from class: com.google.android.gms.internal.zzqu.1
        @Override // com.google.android.gms.common.api.Api.zza
        public zzi zza(Context context, Looper looper, zzf zzfVar, zzqx zzqxVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            if (zzqxVar == null) {
                zzqxVar = zzqx.zzaUZ;
            }
            return new zzi(context, looper, true, zzfVar, zzqxVar, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
        }
    };
    static final Api.zza<zzi, Api.ApiOptions.NoOptions> zzaUX = new Api.zza<zzi, Api.ApiOptions.NoOptions>() { // from class: com.google.android.gms.internal.zzqu.2
        @Override // com.google.android.gms.common.api.Api.zza
        /* renamed from: zzt, reason: merged with bridge method [inline-methods] */
        public zzi zza(Context context, Looper looper, zzf zzfVar, Api.ApiOptions.NoOptions noOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzi(context, looper, false, zzfVar, zzqx.zzaUZ, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
        }
    };
    public static final Scope zzTe = new Scope(Scopes.PROFILE);
    public static final Scope zzTf = new Scope("email");
    public static final Api<zzqx> API = new Api<>("SignIn.API", zzRl, zzRk);
    public static final Api<Api.ApiOptions.NoOptions> zzaiH = new Api<>("SignIn.INTERNAL_API", zzaUX, zzapF);
    public static final zzqv zzaUY = new zzh();
}
