package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.internal.zzma;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzlz implements zzly {

    private static class zza extends zzlw {
        private final zzlb.zzb<Status> zzagy;

        public zza(zzlb.zzb<Status> zzbVar) {
            this.zzagy = zzbVar;
        }

        @Override // com.google.android.gms.internal.zzlw, com.google.android.gms.internal.zzmc
        public void zzbN(int i) throws RemoteException {
            this.zzagy.zzp(new Status(i));
        }
    }

    @Override // com.google.android.gms.internal.zzly
    public PendingResult<Status> zzb(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zzma.zza(googleApiClient) { // from class: com.google.android.gms.internal.zzlz.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlb.zza
            public void zza(zzmb zzmbVar) throws RemoteException {
                zzmbVar.zzpc().zza(new zza(this));
            }
        });
    }
}
