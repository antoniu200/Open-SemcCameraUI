package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzkm implements ProxyApi {
    @Override // com.google.android.gms.auth.api.proxy.ProxyApi
    public PendingResult<ProxyApi.ProxyResult> performProxyRequest(GoogleApiClient googleApiClient, final ProxyRequest proxyRequest) {
        zzx.zzw(googleApiClient);
        zzx.zzw(proxyRequest);
        return googleApiClient.zzb(new zzkl(googleApiClient) { // from class: com.google.android.gms.internal.zzkm.1
            @Override // com.google.android.gms.internal.zzkl
            protected void zza(Context context, zzkk zzkkVar) throws RemoteException {
                zzkkVar.zza(new zzkh() { // from class: com.google.android.gms.internal.zzkm.1.1
                    @Override // com.google.android.gms.internal.zzkh, com.google.android.gms.internal.zzkj
                    public void zza(ProxyResponse proxyResponse) {
                        zzb((AnonymousClass1) new zzkn(proxyResponse));
                    }
                }, proxyRequest);
            }
        });
    }
}
