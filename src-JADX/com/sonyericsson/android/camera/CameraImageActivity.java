package com.sonyericsson.android.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraImageActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (isVoiceInteractionRoot()) {
            intent.setClass(this, InternalCameraActivity.class);
        } else {
            intent.setClass(this, CameraActivity.class);
        }
        intent.putExtra(LaunchCondition.CAMERA_IS_VOICE_INTERACTION_ROOT, isVoiceInteractionRoot());
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
