// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.os.Handler;

public abstract class CaptureResultCheckerBase
{
    protected final Handler mHandler;
    
    public CaptureResultCheckerBase(final Handler mHandler) {
        this.mHandler = mHandler;
    }
    
    public abstract void check(final CaptureResultHolder p0);
}
