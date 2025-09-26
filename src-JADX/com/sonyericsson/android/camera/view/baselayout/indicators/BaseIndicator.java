package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.view.View;
import android.view.ViewStub;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BaseIndicator {
    private static final boolean DEBUG = false;
    private static final String TAG = "BaseIndicator";
    private final String mDebugName;
    private ViewStub mViewStub = null;
    private View mView = null;
    private boolean mOn = false;
    private boolean mVisible = false;
    private int mOrientation = 0;

    public BaseIndicator(String str) {
        this.mDebugName = str;
    }

    public void setup(ViewStub viewStub) {
        this.mViewStub = viewStub;
        update();
    }

    private boolean initViews() {
        if (this.mView == null) {
            this.mView = this.mViewStub.inflate();
        }
        return this.mView != null;
    }

    public void show() {
        this.mVisible = true;
        update();
    }

    public void hide() {
        this.mVisible = false;
        update();
    }

    public void set(boolean z) {
        this.mOn = z;
        update();
    }

    public void setSensorOrientation(int i) {
        this.mOrientation = i;
        update();
    }

    private void update() {
        if (this.mOn && this.mVisible) {
            if (initViews()) {
                onUpdated(this.mView, true, this.mOrientation);
            }
        } else {
            if (this.mView == null) {
                return;
            }
            onUpdated(this.mView, false, this.mOrientation);
        }
    }

    protected void onUpdated(View view, boolean z, int i) {
        view.setVisibility(z ? 0 : 8);
        view.setRotation(RotationUtil.getAngle(i));
    }
}
