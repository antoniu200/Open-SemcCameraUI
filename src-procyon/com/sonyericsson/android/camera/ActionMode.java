// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.device.CameraInfo;

public class ActionMode
{
    public final CameraInfo.CameraId mCameraId;
    public final boolean mIsOneShot;
    public final int mType;
    
    public ActionMode(final boolean mIsOneShot, final int mType, final CameraInfo.CameraId mCameraId) {
        this.mIsOneShot = mIsOneShot;
        this.mType = mType;
        this.mCameraId = mCameraId;
    }
}
