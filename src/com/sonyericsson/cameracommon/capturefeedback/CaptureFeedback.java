package com.sonyericsson.cameracommon.capturefeedback;

import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface CaptureFeedback {
    void onPause();

    void onResume();

    void release();

    void start(CaptureFeedbackAnimation captureFeedbackAnimation);
}
