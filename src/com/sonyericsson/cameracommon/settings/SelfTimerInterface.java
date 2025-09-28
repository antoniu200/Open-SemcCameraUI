package com.sonyericsson.cameracommon.settings;

import com.sonyericsson.cameracommon.sound.SoundPlayer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface SelfTimerInterface {
    int getCountDownIconId();

    int getDurationInMillisecond();

    SoundPlayer.Type getSoundType();
}
