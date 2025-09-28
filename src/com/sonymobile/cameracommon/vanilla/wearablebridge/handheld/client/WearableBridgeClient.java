package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.IntentConstants;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class WearableBridgeClient {
    private static final int BACKGROUND_TASK_TIMEOUT_MILLIS = 3000;
    private static final String TAG = "WearableBridgeClient";
    private Activity mActivity;
    private ExecutorService mBackWorker;
    private Handler mCallbackHandler;
    private ObserveWearableInterface.LifeCycleObserver mLifeCycleObserver;
    private ObserveWearableInterface.PhotoEventObserver mPhotoEventObserver;
    private ObserveWearableInterface.VideoEventObserver mVideoEventObserver;
    private boolean mIsObserverEnabled = false;
    private NotifyWearableInterface.LifeCycleNotifier mLifeCycleNotifier = null;
    private NotifyWearableInterface.PhotoStateNotifier mPhotoStateNotifier = null;
    private NotifyWearableInterface.VideoStateNotifier mVideoStateNotifier = null;
    private WearableBridgeClientBroadcastReceiver mWearableBridgeClientBroadcastReceiver = null;
    private IntentFilter mWearableBridgeClientBroadcastFilter = null;
    private final CountDownLatch mInitializationDone = new CountDownLatch(1);

    public WearableBridgeClient(Activity activity, Handler handler, ObserveWearableInterface.LifeCycleObserver lifeCycleObserver, ObserveWearableInterface.PhotoEventObserver photoEventObserver, ObserveWearableInterface.VideoEventObserver videoEventObserver) {
        this.mActivity = null;
        this.mCallbackHandler = null;
        this.mLifeCycleObserver = null;
        this.mPhotoEventObserver = null;
        this.mVideoEventObserver = null;
        this.mBackWorker = null;
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : E");
        }
        this.mActivity = activity;
        this.mCallbackHandler = handler;
        this.mLifeCycleObserver = lifeCycleObserver;
        this.mPhotoEventObserver = photoEventObserver;
        this.mVideoEventObserver = videoEventObserver;
        this.mBackWorker = ThreadUtil.buildExecutor(TAG);
        this.mBackWorker.execute(new InitializeTask());
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : X");
        }
    }

    private class InitializeTask implements Runnable {
        private InitializeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WearableBridgeClient.this.mLifeCycleNotifier = new LifeCycleNotifierImpl();
            WearableBridgeClient.this.mPhotoStateNotifier = new PhotoStateNotifierImpl();
            WearableBridgeClient.this.mVideoStateNotifier = new VideoStateNotifierImpl();
            WearableBridgeClient.this.mWearableBridgeClientBroadcastReceiver = new WearableBridgeClientBroadcastReceiver(WearableBridgeClient.this.mCallbackHandler, WearableBridgeClient.this.mLifeCycleObserver, WearableBridgeClient.this.mPhotoEventObserver, WearableBridgeClient.this.mVideoEventObserver);
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter = new IntentFilter();
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter.addAction(IntentConstants.SERVER_LIFECYCLE_OBSERVER_ON_RESUME);
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter.addAction(IntentConstants.SERVER_LIFECYCLE_OBSERVER_ON_PAUSE);
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter.addAction(IntentConstants.SERVER_PHOTO_CAPTURE_REQUESTED);
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter.addAction(IntentConstants.SERVER_VIDEO_START_REC_REQUESTED);
            WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter.addAction(IntentConstants.SERVER_VIDEO_STOP_REC_REQUESTED);
            WearableBridgeClient.this.mInitializationDone.countDown();
        }
    }

    public void release() {
        if (CamLog.DEBUG) {
            CamLog.d("release() : E");
        }
        this.mBackWorker.shutdown();
        try {
            this.mBackWorker.awaitTermination(MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            CamLog.e("Failed to shutdown mBackWorker.", e);
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

    /* JADX INFO: Access modifiers changed from: private */
    public Intent getNotifierIntent(String str) {
        Intent intent = new Intent(str);
        intent.setPackage(IntentConstants.BRIDGE_APK_PACKAGE_NAME);
        intent.putExtra(IntentConstants.EXTRA_KEY_CLIENT_PACKAGE_NAME, this.mActivity.getPackageName());
        intent.addFlags(36);
        return intent;
    }

    private class LifeCycleNotifierImpl implements NotifyWearableInterface.LifeCycleNotifier {
        private LifeCycleNotifierImpl() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.LifeCycleNotifier
        public void onResume() {
            if (CamLog.DEBUG) {
                CamLog.d("onResume() : E");
            }
            WearableBridgeClient.this.mBackWorker.execute(new NotifyOnResumeTask());
            if (CamLog.DEBUG) {
                CamLog.d("onResume() : X");
            }
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.LifeCycleNotifier
        public void onPause() {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() : E");
            }
            WearableBridgeClient.this.mBackWorker.execute(new NotifyOnPauseTask());
            if (CamLog.DEBUG) {
                CamLog.d("onPause() : X");
            }
        }
    }

    private class NotifyOnResumeTask implements Runnable {
        private NotifyOnResumeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotyfyOnResumeTask.run() : E");
            }
            if (WearableBridgeClient.this.mIsObserverEnabled) {
                if (CamLog.DEBUG) {
                    CamLog.d("Already resumed.");
                    return;
                }
                return;
            }
            WearableBridgeClient.this.mActivity.registerReceiver(WearableBridgeClient.this.mWearableBridgeClientBroadcastReceiver, WearableBridgeClient.this.mWearableBridgeClientBroadcastFilter);
            WearableBridgeClient.this.mActivity.sendBroadcast(WearableBridgeClient.this.getNotifierIntent(IntentConstants.CLIENT_LIFECYCLE_NOTIFIER_ON_RESUME));
            WearableBridgeClient.this.mIsObserverEnabled = true;
            if (CamLog.DEBUG) {
                CamLog.d("NotyfyOnResumeTask.run() : X");
            }
        }
    }

    private class NotifyOnPauseTask implements Runnable {
        private NotifyOnPauseTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyOnPauseTask.run() : E");
            }
            if (WearableBridgeClient.this.mIsObserverEnabled) {
                WearableBridgeClient.this.mIsObserverEnabled = false;
                WearableBridgeClient.this.mActivity.sendBroadcast(WearableBridgeClient.this.getNotifierIntent(IntentConstants.CLIENT_LIFECYCLE_NOTIFIER_ON_PAUSE));
                WearableBridgeClient.this.mActivity.unregisterReceiver(WearableBridgeClient.this.mWearableBridgeClientBroadcastReceiver);
                if (CamLog.DEBUG) {
                    CamLog.d("NotifyOnPauseTask.run() : X");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("Already paused.");
            }
        }
    }

    private class PhotoStateNotifierImpl implements NotifyWearableInterface.PhotoStateNotifier {
        private PhotoStateNotifierImpl() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.PhotoStateNotifier
        public void onStateChanged(AbstractCapturableState.AbstractPhotoState abstractPhotoState) {
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : E");
            }
            WearableBridgeClient.this.mBackWorker.execute(WearableBridgeClient.this.new NotifyPhotoStateTask(abstractPhotoState));
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : X");
            }
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.PhotoStateNotifier
        public void onCaptureSucceeded() {
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureSucceeded() : E");
            }
            WearableBridgeClient.this.mBackWorker.execute(new NotifyCaptureSucceededTask());
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureSucceeded() : X");
            }
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.PhotoStateNotifier
        public void onCaptureFailed() {
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureFailed() : E");
            }
            WearableBridgeClient.this.mBackWorker.execute(new NotifyCaptureFailedTask());
            if (CamLog.DEBUG) {
                CamLog.d("onCaptureFailed() : X");
            }
        }
    }

    private class NotifyPhotoStateTask implements Runnable {
        private final AbstractCapturableState.AbstractPhotoState mPhotoState;

        NotifyPhotoStateTask(AbstractCapturableState.AbstractPhotoState abstractPhotoState) {
            this.mPhotoState = abstractPhotoState;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyPhotoStateTask.run() : E");
            }
            if (WearableBridgeClient.this.mIsObserverEnabled) {
                Intent notifierIntent = WearableBridgeClient.this.getNotifierIntent(IntentConstants.CLIENT_PHOTO_STATE_CHANGED);
                notifierIntent.putExtra(IntentConstants.EXTRA_KEY_PHOTO_STATE, this.mPhotoState.name());
                WearableBridgeClient.this.mActivity.sendBroadcast(notifierIntent);
                if (CamLog.DEBUG) {
                    CamLog.d("NotifyPhotoStateTask.run() : X");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : Observer disabled.");
            }
        }
    }

    private class NotifyCaptureSucceededTask implements Runnable {
        private NotifyCaptureSucceededTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureSucceededTask.run() : E");
            }
            if (WearableBridgeClient.this.mIsObserverEnabled) {
                Intent notifierIntent = WearableBridgeClient.this.getNotifierIntent(IntentConstants.CLIENT_PHOTO_CAPTURE_COMPLETED);
                notifierIntent.putExtra(IntentConstants.EXTRA_KEY_COMPLETION_STATUS, true);
                WearableBridgeClient.this.mActivity.sendBroadcast(notifierIntent);
                if (CamLog.DEBUG) {
                    CamLog.d("NotifyCaptureSucceededTask.run() : X");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : Observer disabled.");
            }
        }
    }

    private class NotifyCaptureFailedTask implements Runnable {
        private NotifyCaptureFailedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("NotifyCaptureFailedTask.run() : E");
            }
            if (WearableBridgeClient.this.mIsObserverEnabled) {
                Intent notifierIntent = WearableBridgeClient.this.getNotifierIntent(IntentConstants.CLIENT_PHOTO_CAPTURE_COMPLETED);
                notifierIntent.putExtra(IntentConstants.EXTRA_KEY_COMPLETION_STATUS, false);
                WearableBridgeClient.this.mActivity.sendBroadcast(notifierIntent);
                if (CamLog.DEBUG) {
                    CamLog.d("NotifyCaptureFailedTask.run() : X");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("onStateChanged() : Observer disabled.");
            }
        }
    }

    private static class VideoStateNotifierImpl implements NotifyWearableInterface.VideoStateNotifier {
        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.VideoStateNotifier
        public void onStartRecordingFailed() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.VideoStateNotifier
        public void onStartRecordingSucceeded() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.VideoStateNotifier
        public void onStateChanged(AbstractCapturableState.AbstractVideoState abstractVideoState) {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.VideoStateNotifier
        public void onStopRecordingFailed() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface.VideoStateNotifier
        public void onStopRecordingSucceeded() {
        }

        private VideoStateNotifierImpl() {
        }
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
        } catch (InterruptedException e) {
            CamLog.e("InitializeTaskFeature has been interrupted: " + e);
        }
        if (CamLog.DEBUG) {
            CamLog.d("joinInitializeTask out");
        }
    }
}
