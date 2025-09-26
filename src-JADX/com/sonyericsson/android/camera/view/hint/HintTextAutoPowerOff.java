package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.hint.HintTextContent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextAutoPowerOff extends HintTextContent {
    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageResourceId() {
        return R.string.cam_strings_auto_power_off_txt;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public boolean isTransparentBackground() {
        return false;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public HintTextContent.HintPriority getPriority() {
        return HintTextContent.HintPriority.HIGH;
    }
}
