package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlc;
import com.google.android.gms.internal.zzln;
import com.google.android.gms.internal.zzlo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class PendingResults {

    private static final class zza<R extends Result> extends zzlc<R> {
        private final R zzaaW;

        public zza(R r) {
            super(Looper.getMainLooper());
            this.zzaaW = r;
        }

        @Override // com.google.android.gms.internal.zzlc
        protected R zzb(Status status) {
            if (status.getStatusCode() != this.zzaaW.getStatus().getStatusCode()) {
                throw new UnsupportedOperationException("Creating failed results is not supported");
            }
            return this.zzaaW;
        }
    }

    private static final class zzb<R extends Result> extends zzlc<R> {
        private final R zzaaX;

        public zzb(GoogleApiClient googleApiClient, R r) {
            super(googleApiClient);
            this.zzaaX = r;
        }

        @Override // com.google.android.gms.internal.zzlc
        protected R zzb(Status status) {
            return this.zzaaX;
        }
    }

    private static final class zzc<R extends Result> extends zzlc<R> {
        public zzc(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        @Override // com.google.android.gms.internal.zzlc
        protected R zzb(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private PendingResults() {
    }

    public static PendingResult<Status> canceledPendingResult() {
        zzlo zzloVar = new zzlo(Looper.getMainLooper());
        zzloVar.cancel();
        return zzloVar;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        zzx.zzb(r, "Result must not be null");
        zzx.zzb(r.getStatus().getStatusCode() == 16, "Status code must be CommonStatusCodes.CANCELED");
        zza zzaVar = new zza(r);
        zzaVar.cancel();
        return zzaVar;
    }

    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r) {
        zzx.zzb(r, "Result must not be null");
        zzc zzcVar = new zzc(null);
        zzcVar.zzb((zzc) r);
        return new zzln(zzcVar);
    }

    public static PendingResult<Status> immediatePendingResult(Status status) {
        zzx.zzb(status, "Result must not be null");
        zzlo zzloVar = new zzlo(Looper.getMainLooper());
        zzloVar.zzb((zzlo) status);
        return zzloVar;
    }

    public static <R extends Result> PendingResult<R> zza(R r, GoogleApiClient googleApiClient) {
        zzx.zzb(r, "Result must not be null");
        zzx.zzb(!r.getStatus().isSuccess(), "Status code must not be SUCCESS");
        zzb zzbVar = new zzb(googleApiClient, r);
        zzbVar.zzb((zzb) r);
        return zzbVar;
    }

    public static PendingResult<Status> zza(Status status, GoogleApiClient googleApiClient) {
        zzx.zzb(status, "Result must not be null");
        zzlo zzloVar = new zzlo(googleApiClient);
        zzloVar.zzb((zzlo) status);
        return zzloVar;
    }
}
