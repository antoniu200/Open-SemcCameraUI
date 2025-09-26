// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import android.content.Context;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.proxy.ProxyApi;

public class zzkm implements ProxyApi
{
    @Override
    public PendingResult<ProxyResult> performProxyRequest(final GoogleApiClient googleApiClient, final ProxyRequest proxyRequest) {
        zzx.zzw(googleApiClient);
        zzx.zzw(proxyRequest);
        return googleApiClient.zzb((PendingResult<ProxyResult>)new zzkl(this, googleApiClient, proxyRequest) {
            final ProxyRequest zzSQ;
            final zzkm zzSR;
            
            @Override
            protected void zza(final Context context, final zzkk zzkk) throws RemoteException {
                zzkk.zza(new zzkh(this) {
                    final zzkm$1 zzSS;
                    
                    @Override
                    public void zza(final ProxyResponse proxyResponse) {
                        this.zzSS.zzb((R)new zzkn(proxyResponse));
                    }
                }, this.zzSQ);
            }
        });
    }
}
