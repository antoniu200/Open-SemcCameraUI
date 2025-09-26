package com.google.android.gms.auth.api.credentials.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlb;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class zzd<R extends Result> extends zzlb.zza<R, zze> {
    zzd(GoogleApiClient googleApiClient) {
        super(Auth.zzRF, googleApiClient);
    }

    protected abstract void zza(Context context, zzh zzhVar) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.zzlb.zza
    public final void zza(zze zzeVar) throws RemoteException {
        zza(zzeVar.getContext(), zzeVar.zzpc());
    }
}
