package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class zzkl extends zzlb.zza<ProxyApi.ProxyResult, zzki> {
    public zzkl(GoogleApiClient googleApiClient) {
        super(Auth.zzRE, googleApiClient);
    }

    protected abstract void zza(Context context, zzkk zzkkVar) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.zzlb.zza
    public final void zza(zzki zzkiVar) throws RemoteException {
        zza(zzkiVar.getContext(), zzkiVar.zzpc());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.zzlc
    /* renamed from: zzj, reason: merged with bridge method [inline-methods] */
    public ProxyApi.ProxyResult zzb(Status status) {
        return new zzkn(status);
    }
}
