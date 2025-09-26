// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

public class HintTextSuperSlowMotionDescription extends HintTextSlowMotionDescription
{
    public HintTextSuperSlowMotionDescription(final TutorialController tutorialController, final Context context) {
        super(tutorialController, TutorialController.TutorialType.SUPER_SLOW_MOTION, 2131690157, String.format(context.getString(2131690150), "960"));
    }
}
