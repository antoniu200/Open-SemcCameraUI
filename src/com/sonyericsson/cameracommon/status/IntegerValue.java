package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class IntegerValue implements CameraStatusValue {
    protected final int mValue;

    public IntegerValue(int i) {
        this.mValue = i;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getValueForDebug() {
        return String.valueOf(this.mValue);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public void putInto(ContentValues contentValues, String str) {
        contentValues.put(str + getKey(), Integer.valueOf(this.mValue));
    }
}
