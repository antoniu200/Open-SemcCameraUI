package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ObserveWearableInterface {

    public interface LifeCycleObserver {
        void onPause();

        void onResume();
    }

    public interface PhotoEventObserver {
        void onPhotoCaptureRequested();
    }

    public interface VideoEventObserver {
        void onStartVideoRecRequested();

        void onStopVideoRecRequested();
    }
}
