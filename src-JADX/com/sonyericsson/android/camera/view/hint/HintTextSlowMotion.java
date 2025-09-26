package com.sonyericsson.android.camera.view.hint;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class HintTextSlowMotion extends HintTextContent {
    private final int mNameId;

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonDescriptionResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageDescriptionResourceId() {
        return -1;
    }

    public HintTextSlowMotion(int i) {
        this.mNameId = i;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageResourceId() {
        return this.mNameId;
    }
}
