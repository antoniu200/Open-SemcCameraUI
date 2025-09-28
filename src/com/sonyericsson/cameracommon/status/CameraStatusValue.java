package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface CameraStatusValue {
    String getKey();

    String getValueForDebug();

    int minRequiredVersion();

    void putInto(ContentValues contentValues, String str);
}
