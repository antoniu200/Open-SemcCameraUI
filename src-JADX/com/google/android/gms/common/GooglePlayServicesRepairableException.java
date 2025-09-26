package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GooglePlayServicesRepairableException extends UserRecoverableException {
    private final int zzRy;

    GooglePlayServicesRepairableException(int i, String str, Intent intent) {
        super(str, intent);
        this.zzRy = i;
    }

    public int getConnectionStatusCode() {
        return this.zzRy;
    }
}
