package com.google.android.gms.auth;

import android.content.Intent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GooglePlayServicesAvailabilityException extends UserRecoverableAuthException {
    private final int zzRy;

    GooglePlayServicesAvailabilityException(int i, String str, Intent intent) {
        super(str, intent);
        this.zzRy = i;
    }

    public int getConnectionStatusCode() {
        return this.zzRy;
    }
}
