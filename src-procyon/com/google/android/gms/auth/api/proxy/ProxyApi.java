// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.proxy;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface ProxyApi
{
    PendingResult<ProxyResult> performProxyRequest(final GoogleApiClient p0, final ProxyRequest p1);
    
    public interface ProxyResult extends Result
    {
        ProxyResponse getResponse();
    }
}
