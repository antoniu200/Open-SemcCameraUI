// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.view.tutorial.TutorialController;

public abstract class HintTextSlowMotionDescription extends HintTextContent
{
    private static final long HINT_TIMEOUT_MILLIS = 10000L;
    private final String mDescription;
    private final int mNameId;
    private final TutorialController mTutorial;
    private final TutorialController.TutorialType mTutorialType;
    
    public HintTextSlowMotionDescription(final TutorialController mTutorial, final TutorialController.TutorialType mTutorialType, final int mNameId, final String mDescription) {
        this.mTutorial = mTutorial;
        this.mTutorialType = mTutorialType;
        this.mNameId = mNameId;
        this.mDescription = mDescription;
    }
    
    @Override
    public int getButtonDescriptionResourceId() {
        return -1;
    }
    
    @Override
    public int getButtonMessageResourceId() {
        return 2131689704;
    }
    
    @Override
    public int getMessageDescriptionResourceId() {
        return -1;
    }
    
    @Override
    public int getMessageResourceId() {
        return this.mNameId;
    }
    
    @Override
    public String getSubMessage() {
        return this.mDescription;
    }
    
    @Override
    public long getTimedOutDuration() {
        return 10000L;
    }
    
    public TutorialController.TutorialType getTutorialType() {
        return this.mTutorialType;
    }
}
