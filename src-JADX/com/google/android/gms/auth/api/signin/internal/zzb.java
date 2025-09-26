package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzj;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzb extends zzj<zze> {
    private final GoogleSignInConfig zzTn;

    public zzb(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, GoogleSignInConfig googleSignInConfig, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 91, zzfVar, connectionCallbacks, onConnectionFailedListener);
        this.zzTn = googleSignInConfig == null ? GoogleSignInConfig.zzTh : googleSignInConfig;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzax, reason: merged with bridge method [inline-methods] */
    public zze zzW(IBinder iBinder) {
        return zze.zza.zzaz(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfK() {
        return "com.google.android.gms.auth.api.signin.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfL() {
        return "com.google.android.gms.auth.api.signin.internal.ISignInService";
    }
}
