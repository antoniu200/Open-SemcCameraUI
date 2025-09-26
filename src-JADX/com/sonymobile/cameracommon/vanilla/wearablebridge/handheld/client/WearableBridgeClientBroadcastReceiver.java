package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.IntentConstants;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class WearableBridgeClientBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WearableBridgeClientBroadcastReceiver";
    private Handler mCallbackHandler;
    private ObserveWearableInterface.LifeCycleObserver mLifeCycleObserver;
    private NotifyOnPauseTask mNotifyOnPauseTask;
    private NotifyOnResumeTask mNotifyOnResumeTask;
    private NotifyPhotoCaptureRequestedTask mNotifyPhotoCaptureRequestedTask;
    private NotifyVideoStartRecRequestedTask mNotifyVideoStartRecRequestedTask;
    private NotifyVideoStopRecRequestedTask mNotifyVideoStopRecRequestedTask;
    private ObserveWearableInterface.PhotoEventObserver mPhotoEventObserver;
    private ObserveWearableInterface.VideoEventObserver mVideoEventObserver;

    public WearableBridgeClientBroadcastReceiver(Handler handler, ObserveWearableInterface.LifeCycleObserver lifeCycleObserver, ObserveWearableInterface.PhotoEventObserver photoEventObserver, ObserveWearableInterface.VideoEventObserver videoEventObserver) {
        this.mCallbackHandler = null;
        this.mNotifyOnResumeTask = new NotifyOnResumeTask();
        this.mNotifyOnPauseTask = new NotifyOnPauseTask();
        this.mNotifyPhotoCaptureRequestedTask = new NotifyPhotoCaptureRequestedTask();
        this.mNotifyVideoStartRecRequestedTask = new NotifyVideoStartRecRequestedTask();
        this.mNotifyVideoStopRecRequestedTask = new NotifyVideoStopRecRequestedTask();
        this.mLifeCycleObserver = null;
        this.mPhotoEventObserver = null;
        this.mVideoEventObserver = null;
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : E");
        }
        this.mCallbackHandler = handler;
        this.mLifeCycleObserver = lifeCycleObserver;
        this.mPhotoEventObserver = photoEventObserver;
        this.mVideoEventObserver = videoEventObserver;
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : X");
        }
    }

    public void release() {
        if (CamLog.DEBUG) {
            CamLog.d("release() : E");
        }
        this.mCallbackHandler = null;
        this.mLifeCycleObserver = null;
        this.mPhotoEventObserver = null;
        this.mVideoEventObserver = null;
        if (CamLog.DEBUG) {
            CamLog.d("release() : X");
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (CamLog.DEBUG) {
            CamLog.d("onReceive() : E");
        }
        String action = intent.getAction();
        if (CamLog.DEBUG) {
            CamLog.d("    ACTION = " + action);
        }
        if (action != null) {
            if (IntentConstants.SERVER_LIFECYCLE_OBSERVER_ON_RESUME.equals(action)) {
                this.mCallbackHandler.post(this.mNotifyOnResumeTask);
            } else if (IntentConstants.SERVER_LIFECYCLE_OBSERVER_ON_PAUSE.equals(action)) {
                this.mCallbackHandler.post(this.mNotifyOnPauseTask);
            } else if (IntentConstants.SERVER_PHOTO_CAPTURE_REQUESTED.equals(action)) {
                this.mCallbackHandler.post(this.mNotifyPhotoCaptureRequestedTask);
            } else if (IntentConstants.SERVER_VIDEO_START_REC_REQUESTED.equals(action)) {
                this.mCallbackHandler.post(this.mNotifyVideoStartRecRequestedTask);
            } else if (IntentConstants.SERVER_VIDEO_STOP_REC_REQUESTED.equals(action)) {
                this.mCallbackHandler.post(this.mNotifyVideoStopRecRequestedTask);
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("onReceive() : X");
        }
    }

    private class NotifyOnResumeTask implements Runnable {
        private NotifyOnResumeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClientBroadcastReceiver.this.mLifeCycleObserver.onResume();
        }
    }

    private class NotifyOnPauseTask implements Runnable {
        private NotifyOnPauseTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClientBroadcastReceiver.this.mLifeCycleObserver.onPause();
        }
    }

    private class NotifyPhotoCaptureRequestedTask implements Runnable {
        private NotifyPhotoCaptureRequestedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClientBroadcastReceiver.this.mPhotoEventObserver.onPhotoCaptureRequested();
        }
    }

    private class NotifyVideoStartRecRequestedTask implements Runnable {
        private NotifyVideoStartRecRequestedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClientBroadcastReceiver.this.mVideoEventObserver.onStartVideoRecRequested();
        }
    }

    private class NotifyVideoStopRecRequestedTask implements Runnable {
        private NotifyVideoStopRecRequestedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClientBroadcastReceiver.this.mVideoEventObserver.onStopVideoRecRequested();
        }
    }
}
