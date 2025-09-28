package com.google.android.gms.auth.api.credentials.internal;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.api.Status;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzb implements CredentialRequestResult {
    private final Status zzSC;
    private final Credential zzSD;

    public zzb(Status status, Credential credential) {
        this.zzSC = status;
        this.zzSD = credential;
    }

    public static zzb zzh(Status status) {
        return new zzb(status, null);
    }

    @Override // com.google.android.gms.auth.api.credentials.CredentialRequestResult
    public Credential getCredential() {
        return this.zzSD;
    }

    @Override // com.google.android.gms.common.api.Result
    public Status getStatus() {
        return this.zzSC;
    }
}
