package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.signin.internal.zze;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzqw extends Api.zzb {
    void connect();

    void zzCe();

    void zza(zzp zzpVar, Set<Scope> set, zze zzeVar);

    void zza(zzp zzpVar, boolean z);

    void zza(zzt zztVar);
}
