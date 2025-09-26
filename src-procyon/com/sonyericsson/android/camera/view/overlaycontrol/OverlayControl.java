// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol;

import com.sonyericsson.android.camera.util.CamLog;

public abstract class OverlayControl
{
    private boolean mIsAllowedToShow;
    private boolean mIsEnabled;
    private StateListener mStateListener;
    
    public OverlayControl(final StateListener mStateListener) {
        if (CamLog.DEBUG) {
            CamLog.d("init");
        }
        this.mStateListener = mStateListener;
    }
    
    public void disable() {
        if (CamLog.DEBUG) {
            CamLog.d("Invoked");
        }
        final boolean visible = this.isVisible();
        this.mIsEnabled = false;
        if (visible != this.isVisible()) {
            this.onVisibilityUpdated();
        }
    }
    
    public void enable() {
        if (CamLog.DEBUG) {
            CamLog.d("Invoked");
        }
        final boolean visible = this.isVisible();
        this.mIsEnabled = true;
        if (visible != this.isVisible()) {
            this.onVisibilityUpdated();
        }
    }
    
    public void hide() {
        if (CamLog.DEBUG) {
            CamLog.d("Invoked");
        }
        final boolean visible = this.isVisible();
        this.mIsAllowedToShow = false;
        if (visible != this.isVisible()) {
            this.onVisibilityUpdated();
        }
    }
    
    public boolean isEnabled() {
        return this.mIsEnabled;
    }
    
    public boolean isVisible() {
        return this.mIsEnabled && this.mIsAllowedToShow;
    }
    
    protected void notifyValueUpdateEnd() {
        if (this.mStateListener != null) {
            this.mStateListener.onValueUpdateEnd();
        }
    }
    
    protected void notifyValueUpdateStart() {
        if (this.mStateListener != null) {
            this.mStateListener.onValueUpdateStart();
        }
    }
    
    protected abstract void onOrientationChanged(final int p0);
    
    protected abstract void onVisibilityUpdated();
    
    public abstract void refresh();
    
    public abstract void release();
    
    public void setOrientation(final int n) {
        this.onOrientationChanged(n);
    }
    
    public void show() {
        if (CamLog.DEBUG) {
            CamLog.d("Invoked");
        }
        final boolean visible = this.isVisible();
        this.mIsAllowedToShow = true;
        if (visible != this.isVisible()) {
            this.onVisibilityUpdated();
        }
    }
    
    public interface StateListener
    {
        void onValueUpdateEnd();
        
        void onValueUpdateStart();
    }
}
