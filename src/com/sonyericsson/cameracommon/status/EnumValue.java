package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;
import java.lang.Enum;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class EnumValue<T extends Enum<T>> implements CameraStatusValue {
    private final String mValueString;

    public EnumValue(T t) {
        this.mValueString = t.toString();
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getValueForDebug() {
        return this.mValueString;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public void putInto(ContentValues contentValues, String str) {
        contentValues.put(str + getKey(), this.mValueString);
    }
}
