package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UserRecoverableException extends Exception {
    private final Intent mIntent;

    public UserRecoverableException(String str, Intent intent) {
        super(str);
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return new Intent(this.mIntent);
    }
}
