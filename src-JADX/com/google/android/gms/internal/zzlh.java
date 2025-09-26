package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.internal.zzli;
import java.util.Collections;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzlh implements zzlj {
    private final zzli zzabr;

    public zzlh(zzli zzliVar) {
        this.zzabr = zzliVar;
    }

    @Override // com.google.android.gms.internal.zzlj
    public void begin() {
        this.zzabr.zznZ();
        this.zzabr.zzaci = Collections.emptySet();
    }

    @Override // com.google.android.gms.internal.zzlj
    public void connect() {
        this.zzabr.zzoa();
    }

    @Override // com.google.android.gms.internal.zzlj
    public void disconnect() throws RemoteException {
        for (zzli.zzf<?> zzfVar : this.zzabr.zzaca) {
            zzfVar.zza(null);
            zzfVar.cancel();
        }
        this.zzabr.zzaca.clear();
        this.zzabr.zzach.clear();
        this.zzabr.zznY();
    }

    @Override // com.google.android.gms.internal.zzlj
    public String getName() {
        return "DISCONNECTED";
    }

    @Override // com.google.android.gms.internal.zzlj
    public void onConnected(Bundle bundle) {
    }

    @Override // com.google.android.gms.internal.zzlj
    public void onConnectionSuspended(int i) {
    }

    @Override // com.google.android.gms.internal.zzlj
    public <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(T t) {
        this.zzabr.zzaca.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzlj
    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    @Override // com.google.android.gms.internal.zzlj
    public <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
