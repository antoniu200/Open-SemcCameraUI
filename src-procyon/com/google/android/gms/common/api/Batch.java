// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.internal.zzlc;

public final class Batch extends zzlc<BatchResult>
{
    private boolean zzaaA;
    private final PendingResult<?>[] zzaaB;
    private int zzaay;
    private boolean zzaaz;
    private final Object zzpd;
    
    private Batch(final List<PendingResult<?>> list, final GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzpd = new Object();
        this.zzaay = list.size();
        this.zzaaB = new PendingResult[this.zzaay];
        for (int i = 0; i < list.size(); ++i) {
            (this.zzaaB[i] = (PendingResult<?>)list.get(i)).zza((PendingResult.zza)new PendingResult.zza(this) {
                final Batch zzaaC;
                
                @Override
                public void zzt(Status zzabb) {
                    synchronized (this.zzaaC.zzpd) {
                        if (this.zzaaC.isCanceled()) {
                            return;
                        }
                        if (zzabb.isCanceled()) {
                            this.zzaaC.zzaaA = true;
                        }
                        else if (!zzabb.isSuccess()) {
                            this.zzaaC.zzaaz = true;
                        }
                        this.zzaaC.zzaay--;
                        if (this.zzaaC.zzaay == 0) {
                            if (this.zzaaC.zzaaA) {
                                this.zzaaC.cancel();
                            }
                            else {
                                if (this.zzaaC.zzaaz) {
                                    zzabb = new Status(13);
                                }
                                else {
                                    zzabb = Status.zzabb;
                                }
                                this.zzaaC.zzb(new BatchResult(zzabb, this.zzaaC.zzaaB));
                            }
                        }
                    }
                }
            });
        }
    }
    
    @Override
    public void cancel() {
        super.cancel();
        final PendingResult<?>[] zzaaB = this.zzaaB;
        for (int length = zzaaB.length, i = 0; i < length; ++i) {
            zzaaB[i].cancel();
        }
    }
    
    public BatchResult createFailedResult(final Status status) {
        return new BatchResult(status, this.zzaaB);
    }
    
    public static final class Builder
    {
        private GoogleApiClient zzVs;
        private List<PendingResult<?>> zzaaD;
        
        public Builder(final GoogleApiClient zzVs) {
            this.zzaaD = new ArrayList<PendingResult<?>>();
            this.zzVs = zzVs;
        }
        
        public <R extends Result> BatchResultToken<R> add(final PendingResult<R> pendingResult) {
            final BatchResultToken batchResultToken = new BatchResultToken(this.zzaaD.size());
            this.zzaaD.add(pendingResult);
            return batchResultToken;
        }
        
        public Batch build() {
            return new Batch(this.zzaaD, this.zzVs, null);
        }
    }
}
