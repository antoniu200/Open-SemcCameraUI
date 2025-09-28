package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzg extends zzj<zze> {
    private final com.google.android.gms.auth.api.signin.zzg zzTq;

    public zzg(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, com.google.android.gms.auth.api.signin.zzg zzgVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 87, zzfVar, connectionCallbacks, onConnectionFailedListener);
        this.zzTq = (com.google.android.gms.auth.api.signin.zzg) zzx.zzw(zzgVar);
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
