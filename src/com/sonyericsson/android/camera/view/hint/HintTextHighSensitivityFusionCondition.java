package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextHighSensitivityFusionCondition extends HintTextContent {
    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageResourceId() {
        return R.string.cam_strings_fusion_error_too_close_txt;
    }

    public HintTextHighSensitivityFusionCondition() {
        this.mTransparentBackground = false;
    }
}
