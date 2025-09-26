package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextStandardSlowMotionDescription extends HintTextSlowMotionDescription {
    public HintTextStandardSlowMotionDescription(TutorialController tutorialController, Context context) {
        super(tutorialController, TutorialController.TutorialType.STANDARD_SLOW_MOTION, R.string.cam_strings_slow_motion_standard_txt, String.format(context.getString(R.string.cam_strings_slow_motion_standard_hint_1_txt), "120"));
    }
}
