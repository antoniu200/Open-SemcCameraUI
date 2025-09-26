// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;

public class CameraImageActivity extends Activity
{
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Intent intent = this.getIntent();
        if (this.isVoiceInteractionRoot()) {
            intent.setClass((Context)this, (Class)InternalCameraActivity.class);
        }
        else {
            intent.setClass((Context)this, (Class)CameraActivity.class);
        }
        intent.putExtra("is_voice_interaction_root", this.isVoiceInteractionRoot());
        this.startActivity(intent);
        this.overridePendingTransition(0, 0);
        this.finish();
    }
}
