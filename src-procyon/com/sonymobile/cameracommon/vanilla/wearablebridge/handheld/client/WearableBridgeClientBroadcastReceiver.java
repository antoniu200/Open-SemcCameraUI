// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import android.content.Intent;
import android.content.Context;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;
import android.content.BroadcastReceiver;

class WearableBridgeClientBroadcastReceiver extends BroadcastReceiver
{
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
    
    public WearableBridgeClientBroadcastReceiver(final Handler mCallbackHandler, final ObserveWearableInterface.LifeCycleObserver mLifeCycleObserver, final ObserveWearableInterface.PhotoEventObserver mPhotoEventObserver, final ObserveWearableInterface.VideoEventObserver mVideoEventObserver) {
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
        this.mCallbackHandler = mCallbackHandler;
        this.mLifeCycleObserver = mLifeCycleObserver;
        this.mPhotoEventObserver = mPhotoEventObserver;
        this.mVideoEventObserver = mVideoEventObserver;
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : X");
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (CamLog.DEBUG) {
            CamLog.d("onReceive() : E");
        }
        final String action = intent.getAction();
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("    ACTION = ");
            sb.append(action);
            CamLog.d(sb.toString());
        }
        if (action != null) {
            if ("com.sonymobile.cameracommon.wearablebridge.SERVER_LIFECYCLE_ON_RESUME".equals(action)) {
                this.mCallbackHandler.post((Runnable)this.mNotifyOnResumeTask);
            }
            else if ("com.sonymobile.cameracommon.wearablebridge.SERVER_LIFECYCLE_ON_PAUSE".equals(action)) {
                this.mCallbackHandler.post((Runnable)this.mNotifyOnPauseTask);
            }
            else if ("com.sonymobile.cameracommon.wearablebridge.SERVER_PHOTO_CAPTURE_REQUESTED".equals(action)) {
                this.mCallbackHandler.post((Runnable)this.mNotifyPhotoCaptureRequestedTask);
            }
            else if ("com.sonymobile.cameracommon.wearablebridge.SERVER_VIDEO_START_REC_REQUESTED".equals(action)) {
                this.mCallbackHandler.post((Runnable)this.mNotifyVideoStartRecRequestedTask);
            }
            else if ("com.sonymobile.cameracommon.wearablebridge.SERVER_VIDEO_STOP_REC_REQUESTED".equals(action)) {
                this.mCallbackHandler.post((Runnable)this.mNotifyVideoStopRecRequestedTask);
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("onReceive() : X");
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
    
    private class NotifyOnPauseTask implements Runnable
    {
        final WearableBridgeClientBroadcastReceiver this$0;
        
        private NotifyOnPauseTask(final WearableBridgeClientBroadcastReceiver this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mLifeCycleObserver.onPause();
        }
    }
    
    private class NotifyOnResumeTask implements Runnable
    {
        final WearableBridgeClientBroadcastReceiver this$0;
        
        private NotifyOnResumeTask(final WearableBridgeClientBroadcastReceiver this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mLifeCycleObserver.onResume();
        }
    }
    
    private class NotifyPhotoCaptureRequestedTask implements Runnable
    {
        final WearableBridgeClientBroadcastReceiver this$0;
        
        private NotifyPhotoCaptureRequestedTask(final WearableBridgeClientBroadcastReceiver this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mPhotoEventObserver.onPhotoCaptureRequested();
        }
    }
    
    private class NotifyVideoStartRecRequestedTask implements Runnable
    {
        final WearableBridgeClientBroadcastReceiver this$0;
        
        private NotifyVideoStartRecRequestedTask(final WearableBridgeClientBroadcastReceiver this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mVideoEventObserver.onStartVideoRecRequested();
        }
    }
    
    private class NotifyVideoStopRecRequestedTask implements Runnable
    {
        final WearableBridgeClientBroadcastReceiver this$0;
        
        private NotifyVideoStopRecRequestedTask(final WearableBridgeClientBroadcastReceiver this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mVideoEventObserver.onStopVideoRecRequested();
        }
    }
}
