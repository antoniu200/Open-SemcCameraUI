// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.auth.api.proxy.ProxyApi;

class zzkn implements ProxyResult
{
    private Status zzSC;
    private ProxyResponse zzST;
    
    public zzkn(final ProxyResponse zzST) {
        this.zzST = zzST;
        this.zzSC = Status.zzabb;
    }
    
    public zzkn(final Status zzSC) {
        this.zzSC = zzSC;
    }
    
    @Override
    public ProxyResponse getResponse() {
        return this.zzST;
    }
    
    @Override
    public Status getStatus() {
        return this.zzSC;
    }
}
