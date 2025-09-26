// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import com.google.android.gms.common.api.Status;

public class zzlo extends zzlc<Status>
{
    @Deprecated
    public zzlo(final Looper looper) {
        super(looper);
    }
    
    public zzlo(final GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }
    
    protected Status zzd(final Status status) {
        return status;
    }
}
