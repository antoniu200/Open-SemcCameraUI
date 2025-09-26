// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

public class CameraActivityOnLockScreen extends CameraActivity
{
    @Override
    public void requestSuspend() {
        this.finish();
    }
    
    @Override
    protected boolean shouldShowWhenLocked() {
        return true;
    }
}
