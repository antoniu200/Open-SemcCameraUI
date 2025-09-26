package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface NotifyWearableInterface {

    public interface LifeCycleNotifier {
        void onPause();

        void onResume();
    }

    public interface PhotoStateNotifier {
        void onCaptureFailed();

        void onCaptureSucceeded();

        void onStateChanged(AbstractCapturableState.AbstractPhotoState abstractPhotoState);
    }

    public interface VideoStateNotifier {
        void onStartRecordingFailed();

        void onStartRecordingSucceeded();

        void onStateChanged(AbstractCapturableState.AbstractVideoState abstractVideoState);

        void onStopRecordingFailed();

        void onStopRecordingSucceeded();
    }
}
