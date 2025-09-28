package com.sonyericsson.cameracommon.focusview;

import android.graphics.Point;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface FocusActionListener {
    void onCanceled();

    void onFaceSelected(Point point);

    void onLongPressed();

    void onReleased();

    void onTouched();
}
