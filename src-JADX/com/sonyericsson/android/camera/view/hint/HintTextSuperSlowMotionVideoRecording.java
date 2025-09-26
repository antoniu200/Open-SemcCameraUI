package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextSuperSlowMotionVideoRecording extends HintTextSlowMotion {
    private final boolean mIsDone;

    public HintTextSuperSlowMotionVideoRecording(boolean z) {
        super(!z ? R.string.cam_strings_slow_motion_super_hint_2_txt : R.string.cam_strings_slow_motion_super_hint_3_txt);
        this.mIsDone = z;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public String getTag() {
        return createTag(this.mIsDone);
    }

    public static String createTag(boolean z) {
        return HintTextSuperSlowMotionVideoRecording.class.getSimpleName() + ":" + z;
    }
}
