// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import android.content.BroadcastReceiver;
import java.util.concurrent.TimeUnit;
import android.content.Intent;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.IntentFilter;
import java.util.concurrent.CountDownLatch;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.app.Activity;

public class WearableBridgeClient
{
    private static final int BACKGROUND_TASK_TIMEOUT_MILLIS = 3000;
    private static final String TAG = "WearableBridgeClient";
    private Activity mActivity;
    private ExecutorService mBackWorker;
    private Handler mCallbackHandler;
    private final CountDownLatch mInitializationDone;
    private boolean mIsObserverEnabled;
    private NotifyWearableInterface.LifeCycleNotifier mLifeCycleNotifier;
    private ObserveWearableInterface.LifeCycleObserver mLifeCycleObserver;
    private ObserveWearableInterface.PhotoEventObserver mPhotoEventObserver;
    private NotifyWearableInterface.PhotoStateNotifier mPhotoStateNotifier;
    private ObserveWearableInterface.VideoEventObserver mVideoEventObserver;
    private NotifyWearableInterface.VideoStateNotifier mVideoStateNotifier;
    private IntentFilter mWearableBridgeClientBroadcastFilter;
    private WearableBridgeClientBroadcastReceiver mWearableBridgeClientBroadcastReceiver;
    
    public WearableBridgeClient(final Activity mActivity, final Handler mCallbackHandler, final ObserveWearableInterface.LifeCycleObserver mLifeCycleObserver, final ObserveWearableInterface.PhotoEventObserver mPhotoEventObserver, final ObserveWearableInterface.VideoEventObserver mVideoEventObserver) {
        this.mActivity = null;
        this.mCallbackHandler = null;
        this.mIsObserverEnabled = false;
        this.mLifeCycleObserver = null;
        this.mPhotoEventObserver = null;
        this.mVideoEventObserver = null;
        this.mLifeCycleNotifier = null;
        this.mPhotoStateNotifier = null;
        this.mVideoStateNotifier = null;
        this.mWearableBridgeClientBroadcastReceiver = null;
        this.mWearableBridgeClientBroadcastFilter = null;
        this.mBackWorker = null;
        this.mInitializationDone = new CountDownLatch(1);
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : E");
        }
        this.mActivity = mActivity;
        this.mCallbackHandler = mCallbackHandler;
        this.mLifeCycleObserver = mLifeCycleObserver;
        this.mPhotoEventObserver = mPhotoEventObserver;
        this.mVideoEventObserver = mVideoEventObserver;
        (this.mBackWorker = ThreadUtil.buildExecutor("WearableBridgeClient")).execute(new InitializeTask());
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : X");
        }
    }
    
    private Intent getNotifierIntent(final String s) {
        final Intent intent = new Intent(s);
        intent.setPackage("com.sonymobile.cameracommon.wearablebridge");
        intent.putExtra("wearable-bridge-client-package-name-key", this.mActivity.getPackageName());
        intent.addFlags(36);
        return intent;
    }
    
    public NotifyWearableInterface.LifeCycleNotifier getLifeCycleNotifier() {
        return this.mLifeCycleNotifier;
    }
    
    public NotifyWearableInterface.PhotoStateNotifier getPhotoStateNotifier() {
        return this.mPhotoStateNotifier;
    }
    
    public NotifyWearableInterface.VideoStateNotifier getVideoStateNotifier() {
        return this.mVideoStateNotifier;
    }
    
    public void joinInitializeTask() {
        if (CamLog.DEBUG) {
            CamLog.d("joinInitializeTask in");
        }
        try {
            if (CamLog.DEBUG) {
                CamLog.d("Future.get() in:");
            }
            this.mInitializationDone.await();
            if (CamLog.DEBUG) {
                CamLog.d("Future.get() out:");
            }
        }
        catch (final InterruptedException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("InitializeTaskFeature has been interrupted: ");
            sb.append(obj);
            CamLog.e(sb.toString());
        }
        if (CamLog.DEBUG) {
            CamLog.d("joinInitializeTask out");
        }
    }
    
    public void release() {
        if (CamLog.DEBUG) {
            CamLog.d("release() : E");
        }
        this.mBackWorker.shutdown();
        try {
            this.mBackWorker.awaitTermination(3000L, TimeUnit.MILLISECONDS);
        }
        catch (final InterruptedException ex) {
            CamLog.e("Failed to shutdown mBackWorker.", ex);
        }
        this.mWearableBridgeClientBroadcastReceiver.release();
        this.mWearableBridgeClientBroadcastReceiver = null;
        this.mWearableBridgeClientBroadcastFilter = null;
        this.mLifeCycleNotifier = null;
        this.mPhotoStateNotifier = null;
        this.mVideoStateNotifier = null;
        this.mActivity = null;
        this.mCallbackHandler = null;
        this.mLifeCycleObserver = null;
        this.mPhotoEventObserver = null;
        this.mVideoEventObserver = null;
        if (CamLog.DEBUG) {
            CamLog.d("release() : X");
        }
    }
    
    private class InitializeTask implements Runnable
    {
        final WearableBridgeClient this$0;
        
        private InitializeTask(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mLifeCycleNotifier = new LifeCycleNotifierImpl();
            this.this$0.mPhotoStateNotifier = new PhotoStateNotifierImpl();
            this.this$0.mVideoStateNotifier = new VideoStateNotifierImpl();
            this.this$0.mWearableBridgeClientBroadcastReceiver = new WearableBridgeClientBroadcastReceiver(this.this$0.mCallbackHandler, this.this$0.mLifeCycleObserver, this.this$0.mPhotoEventObserver, this.this$0.mVideoEventObserver);
            this.this$0.mWearableBridgeClientBroadcastFilter = new IntentFilter();
            this.this$0.mWearableBridgeClientBroadcastFilter.addAction("com.sonymobile.cameracommon.wearablebridge.SERVER_LIFECYCLE_ON_RESUME");
            this.this$0.mWearableBridgeClientBroadcastFilter.addAction("com.sonymobile.cameracommon.wearablebridge.SERVER_LIFECYCLE_ON_PAUSE");
            this.this$0.mWearableBridgeClientBroadcastFilter.addAction("com.sonymobile.cameracommon.wearablebridge.SERVER_PHOTO_CAPTURE_REQUESTED");
            this.this$0.mWearableBridgeClientBroadcastFilter.addAction("com.sonymobile.cameracommon.wearablebridge.SERVER_VIDEO_START_REC_REQUESTED");
            this.this$0.mWearableBridgeClientBroadcastFilter.addAction("com.sonymobile.cameracommon.wearablebridge.SERVER_VIDEO_STOP_REC_REQUESTED");
            this.this$0.mInitializationDone.countDown();
        }
    }
    
    private class LifeCycleNotifierImpl implements LifeCycleNotifier
    {
        final WearableBridgeClient this$0;
        
        private LifeCycleNotifierImpl(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPause() {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() : E");
            }
            this.this$0.mBackWorker.execute(new NotifyOnPauseTask());
            if (CamLog.DEBUG) {
                CamLog.d("onPause() : X");
            }
        }
        
        @Override
        public void onResume() {
            if (CamLog.DEBUG) {
                CamLog.d("onResume() : E");
            }
            this.this$0.mBackWorker.execute(new NotifyOnResumeTask());
            if (CamLog.DEBUG) {
                CamLog.d("onResume() : X");
            }
        }
    }
    
    private class NotifyCaptureFailedTask implements Runnable
    {
        final WearableBridgeClient this$0;
        
        private NotifyCaptureFailedTask(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureFailedTask.run() : E");
            }
            if (!this.this$0.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("onStateChanged() : Observer disabled.");
                }
                return;
            }
            final Intent access$1900 = this.this$0.getNotifierIntent("com.sonymobile.cameracommon.wearablebridge.CLIENT_PHOTO_CAPTURE_COMPLETED");
            access$1900.putExtra("wearable-bridge-completion-status-key", false);
            this.this$0.mActivity.sendBroadcast(access$1900);
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureFailedTask.run() : X");
            }
        }
    }
    
    private class NotifyCaptureSucceededTask implements Runnable
    {
        final WearableBridgeClient this$0;
        
        private NotifyCaptureSucceededTask(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureSucceededTask.run() : E");
            }
            if (!this.this$0.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("onStateChanged() : Observer disabled.");
                }
                return;
            }
            final Intent access$1900 = this.this$0.getNotifierIntent("com.sonymobile.cameracommon.wearablebridge.CLIENT_PHOTO_CAPTURE_COMPLETED");
            access$1900.putExtra("wearable-bridge-completion-status-key", true);
            this.this$0.mActivity.sendBroadcast(access$1900);
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureSucceededTask.run() : X");
            }
        }
    }
    
    private class NotifyOnPauseTask implements Runnable
    {
        final WearableBridgeClient this$0;
        
        private NotifyOnPauseTask(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyOnPauseTask.run() : E");
            }
            if (!this.this$0.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("Already paused.");
                }
                return;
            }
            this.this$0.mIsObserverEnabled = false;
            this.this$0.mActivity.sendBroadcast(this.this$0.getNotifierIntent("com.sonymobile.cameracommon.wearablebridge.CLIENT_LIFECYCLE_ON_PAUSE"));
            this.this$0.mActivity.unregisterReceiver((BroadcastReceiver)this.this$0.mWearableBridgeClientBroadcastReceiver);
            if (CamLog.DEBUG) {
                CamLog.d("NotifyOnPauseTask.run() : X");
            }
        }
    }
    
    private class NotifyOnResumeTask implements Runnable
    {
        final WearableBridgeClient this$0;
        
        private NotifyOnResumeTask(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotyfyOnResumeTask.run() : E");
            }
            if (this.this$0.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("Already resumed.");
                }
                return;
            }
            this.this$0.mActivity.registerReceiver((BroadcastReceiver)this.this$0.mWearableBridgeClientBroadcastReceiver, this.this$0.mWearableBridgeClientBroadcastFilter);
            this.this$0.mActivity.sendBroadcast(this.this$0.getNotifierIntent("com.sonymobile.cameracommon.wearablebridge.CLIENT_LIFECYCLE_ON_RESUME"));
            this.this$0.mIsObserverEnabled = true;
            if (CamLog.DEBUG) {
                CamLog.d("NotyfyOnResumeTask.run() : X");
            }
        }
    }
    
    private class NotifyPhotoStateTask implements Runnable
    {
        private final AbstractCapturableState.AbstractPhotoState mPhotoState;
        final WearableBridgeClient this$0;
        
        NotifyPhotoStateTask(final WearableBridgeClient this$0, final AbstractCapturableState.AbstractPhotoState mPhotoState) {
            this.this$0 = this$0;
            this.mPhotoState = mPhotoState;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyPhotoStateTask.run() : E");
            }
            if (!this.this$0.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("onStateChanged() : Observer disabled.");
                }
                return;
            }
            final Intent access$1900 = this.this$0.getNotifierIntent("com.sonymobile.cameracommon.wearablebridge.CLIENT_PHOTO_STATE_CHANGED");
            access$1900.putExtra("wearable-bridge-photo-state-key", this.mPhotoState.name());
            this.this$0.mActivity.sendBroadcast(access$1900);
            if (CamLog.DEBUG) {
                CamLog.d("NotifyPhotoStateTask.run() : X");
            }
        }
    }
    
    private class PhotoStateNotifierImpl implements PhotoStateNotifier
    {
        final WearableBridgeClient this$0;
        
        private PhotoStateNotifierImpl(final WearableBridgeClient this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCaptureFailed() {
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureFailed() : E");
            }
            this.this$0.mBackWorker.execute(new NotifyCaptureFailedTask());
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureFailed() : X");
            }
        }
        
        @Override
        public void onCaptureSucceeded() {
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureSucceeded() : E");
            }
            this.this$0.mBackWorker.execute(new NotifyCaptureSucceededTask());
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureSucceeded() : X");
            }
        }
        
        @Override
        public void onStateChanged(final AbstractCapturableState.AbstractPhotoState abstractPhotoState) {
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : E");
            }
            this.this$0.mBackWorker.execute(this.this$0.new NotifyPhotoStateTask(abstractPhotoState));
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : X");
            }
        }
    }
    
    private static class VideoStateNotifierImpl implements VideoStateNotifier
    {
        @Override
        public void onStartRecordingFailed() {
        }
        
        @Override
        public void onStartRecordingSucceeded() {
        }
        
        @Override
        public void onStateChanged(final AbstractCapturableState.AbstractVideoState abstractVideoState) {
        }
        
        @Override
        public void onStopRecordingFailed() {
        }
        
        @Override
        public void onStopRecordingSucceeded() {
        }
    }
}
