package com.sonymobile.cameracommon.vanilla.wearablebridge.common;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface AbstractCapturableState {

    public enum AbstractPhotoState {
        IDLE,
        BLOCKED
    }

    public enum AbstractVideoState {
        IDLE,
        STARTING_REC,
        RECORDING,
        BLOCKED
    }
}
