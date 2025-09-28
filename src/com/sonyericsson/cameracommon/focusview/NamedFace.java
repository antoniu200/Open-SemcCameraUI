package com.sonyericsson.cameracommon.focusview;

import android.graphics.Rect;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class NamedFace {
    public final Rect mFacePosition;
    public final String mName;
    public final int mSmileScore;
    public final String mUuid;

    public NamedFace(String str, String str2, Rect rect, int i) {
        this.mName = str;
        this.mUuid = str2;
        this.mFacePosition = rect;
        this.mSmileScore = i;
    }
}
