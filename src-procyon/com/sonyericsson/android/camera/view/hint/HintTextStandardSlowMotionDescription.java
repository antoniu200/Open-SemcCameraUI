// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import android.content.Context;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

public class HintTextStandardSlowMotionDescription extends HintTextSlowMotionDescription
{
    public HintTextStandardSlowMotionDescription(final TutorialController tutorialController, final Context context) {
        super(tutorialController, TutorialController.TutorialType.STANDARD_SLOW_MOTION, 2131690148, String.format(context.getString(2131690143), "120"));
    }
}
