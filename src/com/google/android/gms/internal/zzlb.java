package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzli;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzlb {

    public static abstract class zza<R extends Result, A extends Api.zzb> extends zzlc<R> implements zzb<R>, zzli.zzf<A> {
        private final Api.zzc<A> zzZM;
        private AtomicReference<zzli.zze> zzabg;

        protected zza(Api.zzc<A> zzcVar, GoogleApiClient googleApiClient) {
            super(((GoogleApiClient) zzx.zzb(googleApiClient, "GoogleApiClient must not be null")).getLooper());
            this.zzabg = new AtomicReference<>();
            this.zzZM = (Api.zzc) zzx.zzw(zzcVar);
        }

        private void zza(RemoteException remoteException) {
            zzv(new Status(8, remoteException.getLocalizedMessage(), null));
        }

        protected abstract void zza(A a) throws RemoteException;

        @Override // com.google.android.gms.internal.zzli.zzf
        public void zza(zzli.zze zzeVar) {
            this.zzabg.set(zzeVar);
        }

        @Override // com.google.android.gms.internal.zzli.zzf
        public final void zzb(A a) throws DeadObjectException {
            try {
                zza((zza<R, A>) a);
            } catch (DeadObjectException e) {
                zza(e);
                throw e;
            } catch (RemoteException e2) {
                zza(e2);
            }
        }

        @Override // com.google.android.gms.internal.zzli.zzf
        public void zznJ() {
            setResultCallback(null);
        }

        @Override // com.google.android.gms.internal.zzli.zzf
        public int zznK() {
            return 0;
        }

        @Override // com.google.android.gms.internal.zzlc
        protected void zznL() {
            zzli.zze andSet = this.zzabg.getAndSet(null);
            if (andSet != null) {
                andSet.zzc(this);
            }
        }

        @Override // com.google.android.gms.internal.zzli.zzf
        public final Api.zzc<A> zznx() {
            return this.zzZM;
        }

        @Override // com.google.android.gms.internal.zzlb.zzb
        public /* synthetic */ void zzp(Object obj) {
            super.zzb((zza<R, A>) obj);
        }

        @Override // com.google.android.gms.internal.zzlb.zzb, com.google.android.gms.internal.zzli.zzf
        public final void zzv(Status status) {
            zzx.zzb(!status.isSuccess(), "Failed result must not be success");
            zzb((zza<R, A>) zzb(status));
        }
    }

    public interface zzb<R> {
        void zzp(R r);

        void zzv(Status status);
    }
}
