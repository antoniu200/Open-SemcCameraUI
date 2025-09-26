// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

public class HintTextSuperSlowShotDescription extends HintTextSlowMotionDescription
{
    public HintTextSuperSlowShotDescription(final TutorialController tutorialController, final Context context) {
        super(tutorialController, TutorialController.TutorialType.SUPER_SLOW_MOTION_SHOT, 2131690142, String.format(context.getString(2131690139), "960"));
    }
}
