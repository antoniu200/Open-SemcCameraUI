package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlb;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzlj {
    void begin();

    void connect();

    void disconnect();

    String getName();

    void onConnected(Bundle bundle);

    void onConnectionSuspended(int i);

    <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(T t);

    void zza(ConnectionResult connectionResult, Api<?> api, int i);

    <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(T t);
}
