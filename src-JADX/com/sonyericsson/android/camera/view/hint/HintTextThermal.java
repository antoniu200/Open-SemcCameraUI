package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.hint.HintTextContent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class HintTextThermal extends HintTextContent {
    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonDescriptionResourceId() {
        return R.string.cam_strings_cooling_mode_learn_more_txt;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return R.string.cam_strings_cooling_mode_learn_more_txt;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public HintTextContent.HintPriority getPriority() {
        return HintTextContent.HintPriority.MIDDLE;
    }
}
