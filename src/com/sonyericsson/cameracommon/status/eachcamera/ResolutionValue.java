package com.sonyericsson.cameracommon.status.eachcamera;

import android.content.ContentValues;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.status.CameraStatusValue;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ResolutionValue implements CameraStatusValue, EachCameraStatusValue {
    private static int REQUIRED_PROVIDER_VERSION = 1;
    private int mHeight;
    private int mWidth;

    public ResolutionValue(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public ResolutionValue(Rect rect) {
        this.mWidth = rect.width();
        this.mHeight = rect.height();
    }

    public String toString() {
        return "" + this.mWidth + SharedPrefsTranslator.CONNECTOR_CROSS + this.mHeight;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getValueForDebug() {
        return toString();
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public void putInto(ContentValues contentValues, String str) {
        contentValues.put(str + getKey(), toString());
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
