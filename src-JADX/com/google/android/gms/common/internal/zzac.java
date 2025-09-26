package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzac<T extends IInterface> extends zzj<T> {
    private final Api.zzd<T> zzagt;

    public zzac(Context context, Looper looper, int i, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, zzf zzfVar, Api.zzd zzdVar) {
        super(context, looper, i, zzfVar, connectionCallbacks, onConnectionFailedListener);
        this.zzagt = zzdVar;
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected T zzW(IBinder iBinder) {
        return (T) this.zzagt.zzW(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected void zzc(int i, T t) {
        this.zzagt.zza(i, t);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfK() {
        return this.zzagt.zzfK();
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfL() {
        return this.zzagt.zzfL();
    }
}
