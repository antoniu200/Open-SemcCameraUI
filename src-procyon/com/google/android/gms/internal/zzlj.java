// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Api;
import android.os.Bundle;

public interface zzlj
{
    void begin();
    
    void connect();
    
    void disconnect();
    
    String getName();
    
    void onConnected(final Bundle p0);
    
    void onConnectionSuspended(final int p0);
    
     <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(final T p0);
    
    void zza(final ConnectionResult p0, final Api<?> p1, final int p2);
    
     <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(final T p0);
}
