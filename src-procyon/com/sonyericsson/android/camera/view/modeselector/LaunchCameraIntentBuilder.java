// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.content.Intent;

public class LaunchCameraIntentBuilder
{
    public static final String EXTRA_CALLING_ACTIVITY = "calling-activity";
    public static final String EXTRA_CALLING_MODE = "calling-mode";
    public static final String EXTRA_CALLING_PACKAGE = "calling-package";
    private String mActivity;
    private String mCallingActivity;
    private String mCallingMode;
    private String mCallingPackage;
    private String mMode;
    private String mPackage;
    
    private LaunchCameraIntentBuilder() {
        this.mMode = null;
        this.mPackage = null;
        this.mActivity = null;
        this.mCallingMode = null;
        this.mCallingPackage = null;
        this.mCallingActivity = null;
    }
    
    public static LaunchCameraIntentBuilder create() {
        return new LaunchCameraIntentBuilder();
    }
    
    public LaunchCameraIntentBuilder activity(final String mPackage, final String mActivity) {
        this.mPackage = mPackage;
        this.mActivity = mActivity;
        return this;
    }
    
    public LaunchCameraIntentBuilder callingActivity(final String mCallingPackage, final String mCallingActivity) {
        this.mCallingPackage = mCallingPackage;
        this.mCallingActivity = mCallingActivity;
        return this;
    }
    
    public LaunchCameraIntentBuilder callingMode(final String mCallingMode) {
        this.mCallingMode = mCallingMode;
        return this;
    }
    
    public Intent commit() {
        if (this.mMode != null && this.mPackage != null && this.mActivity != null) {
            final Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setClassName(this.mPackage, this.mActivity);
            intent.putExtra("com.sonymobile.camera.addon.intent.extra.CAPTURING_MODE", this.mMode);
            if (this.mCallingMode != null && this.mCallingPackage != null && this.mCallingActivity != null) {
                intent.putExtra("calling-package", this.mCallingPackage);
                intent.putExtra("calling-activity", this.mCallingActivity);
                intent.putExtra("calling-mode", this.mCallingMode);
            }
            return intent;
        }
        throw new IllegalStateException("This builder object is specified enough arguments.");
    }
    
    public LaunchCameraIntentBuilder mode(final String mMode) {
        this.mMode = mMode;
        return this;
    }
}
