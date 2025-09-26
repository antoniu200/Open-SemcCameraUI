package com.sonyericsson.android.camera.controller;

import android.graphics.Rect;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ChapterThumbnail {
    public final Integer format;
    private int mOrientation = 0;
    public final Rect rect;
    public final byte[] yuvData;

    public ChapterThumbnail(byte[] bArr, Integer num, Rect rect) {
        this.yuvData = bArr;
        this.format = num;
        this.rect = rect;
    }

    public int orientation() {
        return this.mOrientation;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }
}
