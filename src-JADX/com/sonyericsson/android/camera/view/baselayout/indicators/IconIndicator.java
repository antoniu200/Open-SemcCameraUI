package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.view.View;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IconIndicator extends BaseIndicator {
    private int mImageResourceId;

    public IconIndicator(String str) {
        super(str);
        this.mImageResourceId = -1;
    }

    public void setBackgroundResource(int i) {
        this.mImageResourceId = i;
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.indicators.BaseIndicator
    protected void onUpdated(View view, boolean z, int i) {
        super.onUpdated(view, z, i);
        if (this.mImageResourceId != -1) {
            view.setBackgroundResource(this.mImageResourceId);
        }
    }
}
