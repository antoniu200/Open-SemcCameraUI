package com.sonyericsson.android.camera.view.animation;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AnimationRequest {
    public final AnimationDegree mDegree;
    public final CapturingMode mFrom;
    public final CapturingMode mTarget;
    public final AnimationType mType;

    public enum AnimationDegree {
        START,
        CANCEL,
        EXEC,
        FINISH
    }

    public enum AnimationType {
        NONE,
        MODE_TOUCH,
        MODE_ICON,
        MODE_SELECTOR,
        MRU_SHORTCUT,
        SWITCH_TOUCH
    }

    public AnimationRequest(AnimationType animationType, AnimationDegree animationDegree, CapturingMode capturingMode, CapturingMode capturingMode2) {
        this.mType = animationType;
        this.mDegree = animationDegree;
        this.mFrom = capturingMode;
        this.mTarget = capturingMode2;
    }
}
