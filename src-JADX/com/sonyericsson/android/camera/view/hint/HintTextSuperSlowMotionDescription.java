package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextSuperSlowMotionDescription extends HintTextSlowMotionDescription {
    public HintTextSuperSlowMotionDescription(TutorialController tutorialController, Context context) {
        super(tutorialController, TutorialController.TutorialType.SUPER_SLOW_MOTION, R.string.cam_strings_slow_motion_super_txt, String.format(context.getString(R.string.cam_strings_slow_motion_super_hint_1_txt), "960"));
    }
}
