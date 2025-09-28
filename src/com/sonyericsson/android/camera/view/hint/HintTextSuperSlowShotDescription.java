package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextSuperSlowShotDescription extends HintTextSlowMotionDescription {
    public HintTextSuperSlowShotDescription(TutorialController tutorialController, Context context) {
        super(tutorialController, TutorialController.TutorialType.SUPER_SLOW_MOTION_SHOT, R.string.cam_strings_slow_motion_shot_txt, String.format(context.getString(R.string.cam_strings_slow_motion_shot_hint_1_txt), "960"));
    }
}
