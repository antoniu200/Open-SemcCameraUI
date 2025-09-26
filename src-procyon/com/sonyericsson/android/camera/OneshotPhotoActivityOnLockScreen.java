// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

public class OneshotPhotoActivityOnLockScreen extends CameraActivityOnLockScreen
{
    @Override
    public void terminateApplication() {
        this.finish();
    }
}
