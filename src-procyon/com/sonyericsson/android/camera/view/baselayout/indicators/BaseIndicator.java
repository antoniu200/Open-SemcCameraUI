// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.indicators;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.ViewStub;
import android.view.View;

public class BaseIndicator
{
    private static final boolean DEBUG = false;
    private static final String TAG = "BaseIndicator";
    private final String mDebugName;
    private boolean mOn;
    private int mOrientation;
    private View mView;
    private ViewStub mViewStub;
    private boolean mVisible;
    
    public BaseIndicator(final String mDebugName) {
        this.mDebugName = mDebugName;
        this.mViewStub = null;
        this.mView = null;
        this.mOn = false;
        this.mVisible = false;
        this.mOrientation = 0;
    }
    
    private boolean initViews() {
        if (this.mView == null) {
            this.mView = this.mViewStub.inflate();
        }
        return this.mView != null;
    }
    
    private void update() {
        if (this.mOn && this.mVisible) {
            if (!this.initViews()) {
                return;
            }
            this.onUpdated(this.mView, true, this.mOrientation);
        }
        else {
            if (this.mView == null) {
                return;
            }
            this.onUpdated(this.mView, false, this.mOrientation);
        }
    }
    
    public void hide() {
        this.mVisible = false;
        this.update();
    }
    
    protected void onUpdated(final View view, final boolean b, final int n) {
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        view.setVisibility(visibility);
        view.setRotation(RotationUtil.getAngle(n));
    }
    
    public void set(final boolean mOn) {
        this.mOn = mOn;
        this.update();
    }
    
    public void setSensorOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        this.update();
    }
    
    public void setup(final ViewStub mViewStub) {
        this.mViewStub = mViewStub;
        this.update();
    }
    
    public void show() {
        this.mVisible = true;
        this.update();
    }
}
