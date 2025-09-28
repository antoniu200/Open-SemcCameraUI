package com.google.android.gms.internal;

import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.Status;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class zzkn implements ProxyApi.ProxyResult {
    private Status zzSC;
    private ProxyResponse zzST;

    public zzkn(ProxyResponse proxyResponse) {
        this.zzST = proxyResponse;
        this.zzSC = Status.zzabb;
    }

    public zzkn(Status status) {
        this.zzSC = status;
    }

    @Override // com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult
    public ProxyResponse getResponse() {
        return this.zzST;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.zzSC;
    }
}
