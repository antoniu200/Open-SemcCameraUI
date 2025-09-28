package com.sonyericsson.android.camera.view.modeselector;

import android.content.Intent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LaunchCameraIntentBuilder {
    public static final String EXTRA_CALLING_ACTIVITY = "calling-activity";
    public static final String EXTRA_CALLING_MODE = "calling-mode";
    public static final String EXTRA_CALLING_PACKAGE = "calling-package";
    private String mMode = null;
    private String mPackage = null;
    private String mActivity = null;
    private String mCallingMode = null;
    private String mCallingPackage = null;
    private String mCallingActivity = null;

    private LaunchCameraIntentBuilder() {
    }

    public static LaunchCameraIntentBuilder create() {
        return new LaunchCameraIntentBuilder();
    }

    public LaunchCameraIntentBuilder mode(String str) {
        this.mMode = str;
        return this;
    }

    public LaunchCameraIntentBuilder activity(String str, String str2) {
        this.mPackage = str;
        this.mActivity = str2;
        return this;
    }

    public LaunchCameraIntentBuilder callingMode(String str) {
        this.mCallingMode = str;
        return this;
    }

    public LaunchCameraIntentBuilder callingActivity(String str, String str2) {
        this.mCallingPackage = str;
        this.mCallingActivity = str2;
        return this;
    }

    public Intent commit() {
        if (this.mMode == null || this.mPackage == null || this.mActivity == null) {
            throw new IllegalStateException("This builder object is specified enough arguments.");
        }
        Intent intent = new Intent("android.intent.action.MAIN");
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
}
