package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.internal.zzlc;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Batch extends zzlc<BatchResult> {
    private boolean zzaaA;
    private final PendingResult<?>[] zzaaB;
    private int zzaay;
    private boolean zzaaz;
    private final Object zzpd;

    public static final class Builder {
        private GoogleApiClient zzVs;
        private List<PendingResult<?>> zzaaD = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzVs = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken<>(this.zzaaD.size());
            this.zzaaD.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zzaaD, this.zzVs);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzpd = new Object();
        this.zzaay = list.size();
        this.zzaaB = new PendingResult[this.zzaay];
        for (int i = 0; i < list.size(); i++) {
            PendingResult<?> pendingResult = list.get(i);
            this.zzaaB[i] = pendingResult;
            pendingResult.zza(new PendingResult.zza() { // from class: com.google.android.gms.common.api.Batch.1
                @Override // com.google.android.gms.common.api.PendingResult.zza
                public void zzt(Status status) {
                    synchronized (Batch.this.zzpd) {
                        if (Batch.this.isCanceled()) {
                            return;
                        }
                        if (status.isCanceled()) {
                            Batch.this.zzaaA = true;
                        } else if (!status.isSuccess()) {
                            Batch.this.zzaaz = true;
                        }
                        Batch.zzb(Batch.this);
                        if (Batch.this.zzaay == 0) {
                            if (Batch.this.zzaaA) {
                                Batch.super.cancel();
                            } else {
                                Batch.this.zzb((Batch) new BatchResult(Batch.this.zzaaz ? new Status(13) : Status.zzabb, Batch.this.zzaaB));
                            }
                        }
                    }
                }
            });
        }
    }

    static /* synthetic */ int zzb(Batch batch) {
        int i = batch.zzaay;
        batch.zzaay = i - 1;
        return i;
    }

    @Override // com.google.android.gms.internal.zzlc, com.google.android.gms.common.api.PendingResult
    public void cancel() {
        super.cancel();
        for (PendingResult<?> pendingResult : this.zzaaB) {
            pendingResult.cancel();
        }
    }

    @Override // com.google.android.gms.internal.zzlc
    /* renamed from: createFailedResult, reason: merged with bridge method [inline-methods] */
    public BatchResult zzb(Status status) {
        return new BatchResult(status, this.zzaaB);
    }
}
