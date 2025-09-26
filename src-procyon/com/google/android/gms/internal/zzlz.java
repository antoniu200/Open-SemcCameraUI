// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;

public final class zzlz implements zzly
{
    @Override
    public PendingResult<Status> zzb(final GoogleApiClient googleApiClient) {
        return googleApiClient.zzb((PendingResult<Status>)new zzma.zza(this, googleApiClient) {
            final zzlz zzagx;
            
            protected void zza(final zzmb zzmb) throws RemoteException {
                zzmb.zzpc().zza(new zzlz.zza((zzlb.zzb<Status>)this));
            }
        });
    }
    
    private static class zza extends zzlw
    {
        private final zzlb.zzb<Status> zzagy;
        
        public zza(final zzlb.zzb<Status> zzagy) {
            this.zzagy = zzagy;
        }
        
        @Override
        public void zzbN(final int n) throws RemoteException {
            this.zzagy.zzp(new Status(n));
        }
    }
}
