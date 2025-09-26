// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;

abstract class zzma<R extends Result> extends zzlb.zza<R, zzmb>
{
    public zzma(final GoogleApiClient googleApiClient) {
        super(zzlx.zzRk, googleApiClient);
    }
    
    abstract static class zza extends zzma<Status>
    {
        public zza(final GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }
        
        public Status zzd(final Status status) {
            return status;
        }
    }
}
