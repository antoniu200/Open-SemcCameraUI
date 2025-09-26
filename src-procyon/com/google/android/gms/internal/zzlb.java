// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.ResultCallback;
import android.os.DeadObjectException;
import android.app.PendingIntent;
import com.google.android.gms.common.api.Status;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.concurrent.atomic.AtomicReference;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;

public class zzlb
{
    public abstract static class zza<R extends Result, A extends Api.zzb> extends zzlc<R> implements zzlb.zzb<R>, zzf<A>
    {
        private final Api.zzc<A> zzZM;
        private AtomicReference<zze> zzabg;
        
        protected zza(final Api.zzc<A> zzc, final GoogleApiClient googleApiClient) {
            super(zzx.zzb(googleApiClient, "GoogleApiClient must not be null").getLooper());
            this.zzabg = new AtomicReference<zze>();
            this.zzZM = (Api.zzc<A>)zzx.zzw((Api.zzc)zzc);
        }
        
        private void zza(final RemoteException ex) {
            this.zzv(new Status(8, ex.getLocalizedMessage(), null));
        }
        
        protected abstract void zza(final A p0) throws RemoteException;
        
        @Override
        public void zza(final zze newValue) {
            this.zzabg.set(newValue);
        }
        
        @Override
        public final void zzb(final A a) throws DeadObjectException {
            try {
                this.zza(a);
            }
            catch (final RemoteException ex) {
                this.zza(ex);
            }
            catch (final DeadObjectException ex2) {
                this.zza((RemoteException)ex2);
                throw ex2;
            }
        }
        
        @Override
        public void zznJ() {
            this.setResultCallback(null);
        }
        
        @Override
        public int zznK() {
            return 0;
        }
        
        @Override
        protected void zznL() {
            final zze zze = this.zzabg.getAndSet(null);
            if (zze != null) {
                zze.zzc(this);
            }
        }
        
        @Override
        public final Api.zzc<A> zznx() {
            return this.zzZM;
        }
        
        @Override
        public final void zzv(final Status status) {
            zzx.zzb(status.isSuccess() ^ true, (Object)"Failed result must not be success");
            this.zzb(this.zzb(status));
        }
    }
    
    public interface zzb<R>
    {
        void zzp(final R p0);
        
        void zzv(final Status p0);
    }
}
