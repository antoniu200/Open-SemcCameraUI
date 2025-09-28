package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class HintTextSlowMotionDescription extends HintTextContent {
    private static final long HINT_TIMEOUT_MILLIS = 10000;
    private final String mDescription;
    private final int mNameId;
    private final TutorialController mTutorial;
    private final TutorialController.TutorialType mTutorialType;

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonDescriptionResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return R.string.cam_strings_cooling_mode_learn_more_txt;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageDescriptionResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public long getTimedOutDuration() {
        return HINT_TIMEOUT_MILLIS;
    }

    public HintTextSlowMotionDescription(TutorialController tutorialController, TutorialController.TutorialType tutorialType, int i, String str) {
        this.mTutorial = tutorialController;
        this.mTutorialType = tutorialType;
        this.mNameId = i;
        this.mDescription = str;
    }

    public TutorialController.TutorialType getTutorialType() {
        return this.mTutorialType;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageResourceId() {
        return this.mNameId;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public String getSubMessage() {
        return this.mDescription;
    }
}
