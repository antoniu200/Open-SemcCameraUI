// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import com.google.android.gms.internal.zzlc;
import com.google.android.gms.internal.zzln;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlo;
import android.os.Looper;

public final class PendingResults
{
    private PendingResults() {
    }
    
    public static PendingResult<Status> canceledPendingResult() {
        final zzlo zzlo = new zzlo(Looper.getMainLooper());
        zzlo.cancel();
        return zzlo;
    }
    
    public static <R extends Result> PendingResult<R> canceledPendingResult(final R r) {
        zzx.zzb(r, "Result must not be null");
        zzx.zzb(r.getStatus().getStatusCode() == 16, (Object)"Status code must be CommonStatusCodes.CANCELED");
        final zza zza = new zza(r);
        zza.cancel();
        return zza;
    }
    
    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(final R r) {
        zzx.zzb(r, "Result must not be null");
        final zzc zzc = new zzc((GoogleApiClient)null);
        zzc.zzb(r);
        return new zzln<R>(zzc);
    }
    
    public static PendingResult<Status> immediatePendingResult(final Status status) {
        zzx.zzb(status, "Result must not be null");
        final zzlo zzlo = new zzlo(Looper.getMainLooper());
        zzlo.zzb(status);
        return zzlo;
    }
    
    public static <R extends Result> PendingResult<R> zza(final R r, final GoogleApiClient googleApiClient) {
        zzx.zzb(r, "Result must not be null");
        zzx.zzb(r.getStatus().isSuccess() ^ true, (Object)"Status code must not be SUCCESS");
        final zzb zzb = new zzb(googleApiClient, (R)r);
        zzb.zzb(r);
        return zzb;
    }
    
    public static PendingResult<Status> zza(final Status status, final GoogleApiClient googleApiClient) {
        zzx.zzb(status, "Result must not be null");
        final zzlo zzlo = new zzlo(googleApiClient);
        zzlo.zzb(status);
        return zzlo;
    }
    
    private static final class zza<R extends Result> extends zzlc<R>
    {
        private final R zzaaW;
        
        public zza(final R zzaaW) {
            super(Looper.getMainLooper());
            this.zzaaW = zzaaW;
        }
        
        @Override
        protected R zzb(final Status status) {
            if (status.getStatusCode() != this.zzaaW.getStatus().getStatusCode()) {
                throw new UnsupportedOperationException("Creating failed results is not supported");
            }
            return this.zzaaW;
        }
    }
    
    private static final class zzb<R extends Result> extends zzlc<R>
    {
        private final R zzaaX;
        
        public zzb(final GoogleApiClient googleApiClient, final R zzaaX) {
            super(googleApiClient);
            this.zzaaX = zzaaX;
        }
        
        @Override
        protected R zzb(final Status status) {
            return this.zzaaX;
        }
    }
    
    private static final class zzc<R extends Result> extends zzlc<R>
    {
        public zzc(final GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }
        
        @Override
        protected R zzb(final Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }
}
