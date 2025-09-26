// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import com.sonyericsson.cameracommon.status.CameraStatusPublisher;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.configuration.IntentReader;
import java.nio.ByteBuffer;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.storage.VideoSavingRequest;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import com.sonyericsson.android.camera.view.angle.VariableIndex;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.android.camera.controller.selftimerfeedback.LedLight;
import com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback;
import com.sonymobile.cameracommon.evf.Evf;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.cameracommon.status.eachcamera.Hdr;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;
import com.sonyericsson.cameracommon.status.eachcamera.VideoRecordingFps;
import com.sonyericsson.cameracommon.status.eachcamera.VideoResolution;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import android.os.PowerManager;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.parameter.dependency.DependencyCheckUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import android.os.SystemClock;
import android.util.Size;
import com.sonyericsson.android.camera.device.PlatformDependencyResolver;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import java.util.LinkedList;
import com.sonyericsson.cameracommon.storage.PredictiveCapturePathBuilder;
import android.content.Intent;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.Optional;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import android.content.Context;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import android.location.Location;
import android.app.Activity;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.Iterator;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import android.graphics.Rect;
import android.graphics.Point;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import android.graphics.Bitmap;
import com.sonyericsson.android.camera.view.animation.AnimationRequest;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import java.util.concurrent.CopyOnWriteArraySet;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import android.net.Uri;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.cameracommon.contentsview.PredictiveCaptureStoreInfo;
import java.util.List;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.Set;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.android.camera.setting.LastSettings;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.CameraActivity;

public class StateMachine
{
    private static final int COLOR_VALUE_MAX = 255;
    private static final int RESUME_TIMEOUT = 7000;
    private static final String TAG = "StateMachine";
    private static final int TERMINATE_WAIT_TIME = 4000;
    private static final String THREAD_NAME = "RequestStore";
    private static final boolean USE_PROGRESS_ON_PHOTO_THUMBNAIL = false;
    private static final boolean USE_PROGRESS_ON_VIDEO_THUMBNAIL = false;
    private final CameraActivity mActivity;
    private CameraDeviceHandler mCameraDeviceHandler;
    private ChangeCameraModeTask mChangeCameraModeTask;
    private ChapterThumbnail mChapterThumbnail;
    private ContentsViewController mContentsViewController;
    private State mCurrentState;
    private final ExecutorService mExecService;
    private final GestureShutter mGestureShutter;
    private final GestureShutter.ControllerHost mGestureShutterHost;
    private final Handler mHandler;
    private int mHighFrameRateVideoRecordingCountInSuperSlowMotion;
    private boolean mIsPausedAudioPlayback;
    boolean mIsSceneRecognitionValid;
    private boolean mIsSdPermissionFinished;
    private boolean mIsSemiAutoEnabled;
    private boolean mIsVideoRecording;
    private RequestFactory.PhotoSavingRequestBuilder mLastPhotoSavingRequest;
    private final LastSettings mLastSettings;
    private long mLastSmileCaptureTakenTime;
    private StoreDataResult mLastStoreDataResult;
    private RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest;
    private final Runnable mNotifyResumeTimeoutTask;
    private ObjectTrackingManager mObjectTracking;
    private final Set<OnStateChangedListener> mOnStateChangedListenerSet;
    private Storage.OnStoreCompletedListener mOnStoreCompletedListener;
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
    private final List<Runnable> mPendingTaskListForStandby;
    private final List<RequestFactory.PhotoSavingRequestBuilder> mPhotoSavingRequestList;
    private SuitablePredictiveApplier mPredictiveApplier;
    private PredictiveCaptureStoreInfo mPredictiveCaptureStoreInfo;
    private final RecorderController.RecorderListener mRecorderListener;
    private final UserSettingApplicable mSettingController;
    private StartRecordingTask mStartRecordingTask;
    private Storage mStorage;
    private Storage.StorageReadyStateListener mStorageReadyStateListener;
    private StorageStateAdapter mStorageStateListener;
    private final UserSettings mUserSettings;
    private ViewFinder mViewFinder;
    private final UserEventHandler.VirtualKeyEventDispatcher mVirtualKeyEventDispatcher;
    
    public StateMachine(final CameraActivity mActivity, final Storage mStorage) {
        this.mHandler = new Handler();
        this.mLastPhotoSavingRequest = null;
        this.mLastVideoSavingRequest = null;
        this.mContentsViewController = null;
        this.mExecService = ThreadUtil.buildExecutor("RequestStore");
        this.mIsVideoRecording = false;
        this.mPhotoSavingRequestList = new ArrayList<RequestFactory.PhotoSavingRequestBuilder>();
        this.mCurrentState = (State)new StateNone();
        this.mHighFrameRateVideoRecordingCountInSuperSlowMotion = 0;
        this.mLastStoreDataResult = null;
        this.mChangeCameraModeTask = null;
        this.mStartRecordingTask = null;
        this.mPendingTaskListForStandby = new ArrayList<Runnable>();
        this.mVirtualKeyEventDispatcher = new UserEventHandler.VirtualKeyEventDispatcher();
        this.mIsSceneRecognitionValid = false;
        this.mStorageStateListener = new StorageStateAdapter();
        this.mIsSdPermissionFinished = false;
        this.mOnStoreCompletedListener = new Storage.OnStoreCompletedListener() {
            final StateMachine this$0;
            
            private void notifyResult(final StoreDataResult storeDataResult) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, this.this$0.isSettingChangeAcceptable());
                this.this$0.mHandler.post((Runnable)new Runnable(this, storeDataResult) {
                    final StateMachine$1 this$1;
                    final StoreDataResult val$result;
                    
                    @Override
                    public void run() {
                        this.this$1.this$0.calculateRemainStorage();
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_ON_STORE_COMPLETED, this.val$result);
                    }
                });
            }
            
            @Override
            public void onStoreCompleted(final Uri obj, final SavingRequest obj2, final StorageType obj3) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke uri:");
                    sb.append(obj);
                    sb.append(", request:");
                    sb.append(obj2);
                    sb.append(", savedStorage:");
                    sb.append(obj3);
                    CamLog.d(sb.toString());
                }
                this.notifyResult(new StoreDataResult(MediaSavingResult.SUCCESS, obj, obj2));
            }
            
            @Override
            public void onStoreFailed(final Uri obj, final SavingRequest obj2, final int i) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke uri:");
                    sb.append(obj);
                    sb.append(", request:");
                    sb.append(obj2);
                    sb.append(", reason:");
                    sb.append(i);
                    CamLog.d(sb.toString());
                }
                this.notifyResult(new StoreDataResult(MediaSavingResult.FAIL, obj, obj2));
            }
        };
        this.mGestureShutterHost = new GestureShutter.ControllerHost() {
            final StateMachine this$0;
            
            @Override
            public void addOrientationListener(final CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener) {
                this.this$0.mActivity.addOrienationListener(layoutOrientationChangedListener);
            }
            
            @Override
            public CameraActivity.LayoutOrientation getLayoutOrientation() {
                return this.this$0.mActivity.getLastDetectedOrientation();
            }
            
            @Override
            public void prepareGestureShutterCountDown() {
                this.this$0.mViewFinder.prepareGestureShutterCountDown();
            }
            
            @Override
            public void removeOrientationListener(final CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener) {
                this.this$0.mActivity.removeOrienationListener(layoutOrientationChangedListener);
            }
            
            @Override
            public void resetGestureShutterCountDown() {
                this.this$0.updatePhotoSelftimer((SelfTimer)this.this$0.mUserSettings.get(UserSettingKey.SELF_TIMER));
            }
            
            @Override
            public void startGestureShutterCountDown() {
                if (this.this$0.mViewFinder.isMessageDialogOpened()) {
                    return;
                }
                if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                    return;
                }
                if (this.this$0.isLazyInitializationRunning()) {
                    return;
                }
                if (!this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                    return;
                }
                this.this$0.sendEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.GESTURE);
                this.this$0.mActivity.restartAutoPowerOffTimer();
            }
        };
        this.mOnStateChangedListenerSet = new CopyOnWriteArraySet<OnStateChangedListener>();
        this.mSettingController = new SettingsController();
        this.mNotifyResumeTimeoutTask = new Runnable() {
            final StateMachine this$0;
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke NotifyResumeTimeoutTask");
                }
                this.this$0.sendEvent(TransitterEvent.EVENT_RESUME_TIMEOUT, new Object[0]);
            }
        };
        this.mChapterThumbnail = null;
        this.mRecorderListener = new RecorderController.RecorderListener() {
            private RequestFactory.VideoSavingRequestBuilder mVideoSavingRequestBuilder = null;
            final StateMachine this$0;
            
            @Override
            public void onRecordError(final int i, final int j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ERROR:MediaRecorder (");
                sb.append(i);
                sb.append(", ");
                sb.append(j);
                sb.append(")");
                CamLog.e(sb.toString());
                this.this$0.mHandler.post((Runnable)new Runnable(this) {
                    final StateMachine$5 this$1;
                    
                    @Override
                    public void run() {
                        if (this.this$1.this$0.isCurrentStorageExternal() && !this.this$1.this$0.isStorageWritable(Storage.StorageType.EXTERNAL_CARD)) {
                            this.this$1.this$0.mViewFinder.showMessageDialog(DialogId.COULD_NOT_START_RECORDING, new Object[0]);
                            this.this$1.this$0.changeTo((State)new StateWarning(), new Object[0]);
                            this.this$1.this$0.mCameraDeviceHandler.releaseVideo();
                            return;
                        }
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_ON_RECORDING_ERROR, new Object[0]);
                        this.this$1.this$0.mCameraDeviceHandler.releaseVideo();
                    }
                });
            }
            
            @Override
            public void onRecordFinished(final Result obj) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke result:");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
                this.this$0.mHandler.post((Runnable)new Runnable(this, obj) {
                    final StateMachine$5 this$1;
                    final Result val$localResult;
                    
                    @Override
                    public void run() {
                        if (CamLog.DEBUG) {
                            CamLog.d("invoke onRecordFinished");
                        }
                        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[this.val$localResult.ordinal()]) {
                            case 4: {
                                this.this$1.this$0.sendEvent(TransitterEvent.EVENT_ON_RECORDING_ERROR, new Object[0]);
                                this.this$1.this$0.mCameraDeviceHandler.releaseVideo();
                                break;
                            }
                            case 3: {
                                if (this.this$1.this$0.mContentsViewController != null) {
                                    this.this$1.this$0.mContentsViewController.enableClick();
                                }
                                this.this$1.this$0.mCameraDeviceHandler.finalizeRecording();
                                this.this$1.this$0.onVideoRecordingDone();
                                this.this$1.this$0.requestStoreVideo(this.this$1.mVideoSavingRequestBuilder);
                                if (this.this$1.this$0.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                                    ResearchUtil.getInstance().sendSlowMotionEvent(this.this$1.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION).getValue(), this.this$1.this$0.mHighFrameRateVideoRecordingCountInSuperSlowMotion);
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                this.this$1.this$0.changeTo((State)this.this$1.this$0.new StateVideoStopping(this.val$localResult), new Object[0]);
                                this.this$1.this$0.doStopRecording(false);
                                break;
                            }
                            case 1: {
                                this.this$1.this$0.changeTo((State)this.this$1.this$0.new StateVideoStopping(this.val$localResult), new Object[0]);
                                this.this$1.this$0.doStopRecording(false);
                                break;
                            }
                        }
                    }
                });
            }
            
            @Override
            public void onRecordProgress(final long lng) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke progressMillis:");
                    sb.append(lng);
                    CamLog.d(sb.toString());
                }
                this.this$0.mHandler.post((Runnable)new Runnable(this, (int)lng) {
                    final StateMachine$5 this$1;
                    final int val$progress;
                    
                    @Override
                    public void run() {
                        this.this$1.this$0.updateRecordingProgress(this.val$progress);
                    }
                });
            }
            
            @Override
            public void setSavingRequestBuilder(final RequestFactory.VideoSavingRequestBuilder mVideoSavingRequestBuilder) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke");
                }
                this.mVideoSavingRequestBuilder = mVideoSavingRequestBuilder;
            }
        };
        this.mOnSuperSlowRecordingFinishedListener = new OnSuperSlowRecordingFinishedListener() {
            final StateMachine this$0;
            
            @Override
            public void onSuperSlowRecordingFinished() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke");
                }
                this.this$0.sendEvent(TransitterEvent.EVENT_HIGH_FRAME_RATE_RECORDING_DONE, new Object[0]);
            }
        };
        this.mIsPausedAudioPlayback = false;
        this.mPredictiveApplier = new SuitablePredictiveApplier();
        this.mActivity = mActivity;
        this.mStorage = mStorage;
        this.mGestureShutter = new GestureShutter(this.mGestureShutterHost, null);
        (this.mUserSettings = mActivity.getStoredSettings().getUserSettings()).register(this.mSettingController);
        this.mLastSettings = mActivity.getStoredSettings().getLastSettings();
        if (this.mStorageReadyStateListener == null) {
            this.mStorageReadyStateListener = new StorageReadyStateAdapter();
            this.mStorage.addStorageReadyStateListener(this.mStorageReadyStateListener);
        }
    }
    
    private void calculateRemainStorage() {
        if (this.mCameraDeviceHandler != null && this.mViewFinder != null && this.mActivity != null && this.getCurrentStorage() != null) {
            final Storage.StorageType currentStorage = this.getCurrentStorage();
            if (this.mStorage.getRemainStorage(currentStorage) <= 61440L) {
                this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, currentStorage, this.mStorage.getCurrentState(currentStorage));
            }
        }
    }
    
    private boolean canInvokePhotoSelfTimer() {
        return !this.isLazyInitializationRunning() && this.isPhotoSelfTimerEnabled() && this.isStorageWritable(this.getCurrentStorage());
    }
    
    private void cancelAutoFocus(final boolean b) {
        if (b) {
            this.mCameraDeviceHandler.resetFocusModeAndCommit();
        }
        this.mViewFinder.setDisplayFlashRequired(false);
        this.mViewFinder.setDisplayFlashColor(255, 255, 255);
        this.mCameraDeviceHandler.cancelAutoFocus();
    }
    
    private void changeModeTo(final CapturingMode currentCapturingMode, final AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke request:");
            sb.append(currentCapturingMode);
            sb.append(", current:");
            sb.append(this.getCurrentCapturingMode());
            CamLog.d(sb.toString());
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGING, new Object[0]);
        this.doStopObjectTracking();
        this.mUserSettings.resetTempParameters();
        this.mViewFinder.clearMessageDialog();
        this.mCameraDeviceHandler.savePreloadSettings((CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
        if (this.getCurrentCapturingMode().isFront() == currentCapturingMode.isFront()) {
            this.setCurrentCapturingMode(currentCapturingMode);
            this.changeTo((State)new StateWaitingEvfPreparedByModeChange(), new Object[0]);
            this.mChangeCameraModeTask = new ChangeCameraModeTask(currentCapturingMode, animationType);
            this.mHandler.post((Runnable)this.mChangeCameraModeTask);
        }
        else {
            this.setCurrentCapturingMode(currentCapturingMode);
            this.changeTo((State)new StateCameraSwitching(this.mActivity.prepareCameraDeviceHandler(FastCapture.LAUNCH_ONLY, currentCapturingMode, this.mUserSettings), FastCapture.LAUNCH_ONLY), new Object[0]);
            this.mViewFinder.notifyOnEvfPrepared();
        }
    }
    
    private void changeTo(final State mCurrentState, final Object... array) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke current:");
                sb.append(this.mCurrentState.getClass().getSimpleName());
                sb.append(", to:");
                sb.append(mCurrentState.getClass().getSimpleName());
                CamLog.d(sb.toString());
            }
            this.mCurrentState.exit();
            this.mCurrentState = mCurrentState;
            final Iterator<OnStateChangedListener> iterator = this.mOnStateChangedListenerSet.iterator();
            while (iterator.hasNext()) {
                iterator.next().onStateChanged(this.mCurrentState.getCaptureState(), array);
            }
            this.mCurrentState.entry();
        }
    }
    
    private void changeToStandby() {
        if (this.isVideo()) {
            this.changeTo((State)new StateVideoReady(), new Object[0]);
        }
        else {
            this.changeTo((State)new StatePhotoReady(true), new Object[0]);
        }
    }
    
    private boolean checkBurstConditions(final boolean b) {
        if (!this.isStorageWritable(Storage.StorageType.INTERNAL)) {
            return false;
        }
        if (this.mCameraDeviceHandler.getRemainSavingPhotoRequestCount() > 0) {
            return false;
        }
        if (this.getCurrentCapturingMode() == CapturingMode.SCENE_RECOGNITION && !b) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_IN_DARK_CONDITION);
            return false;
        }
        return true;
    }
    
    private void checkCallback(final RequestFactory.RequestBuilder requestBuilder) {
        requestBuilder.addCallback(this.mOnStoreCompletedListener);
    }
    
    private boolean checkSaveDestinationCanBeChange(Storage.StorageType storageType) {
        if (storageType == Storage.StorageType.INTERNAL) {
            storageType = Storage.StorageType.EXTERNAL_CARD;
        }
        else {
            if (storageType != Storage.StorageType.EXTERNAL_CARD) {
                return false;
            }
            storageType = Storage.StorageType.INTERNAL;
        }
        return this.isStorageWritable(storageType);
    }
    
    private void checkThermalWarning() {
        if (this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            return;
        }
        if (PlatformCapability.isPowerSavingSupported(this.getCurrentCameraId())) {
            if (this.mActivity.isThermalWarningReceived()) {
                this.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
            }
            else if (this.mActivity.isThermalWarningExtraState()) {
                this.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
            }
        }
    }
    
    private void cleanupPendingState() {
        this.updatePhotoSelftimer((SelfTimer)this.mUserSettings.get(UserSettingKey.SELF_TIMER));
    }
    
    private TakenStatusCommon createTakenStatusCommon(final SavingTaskManager.SavedFileType savedFileType, final Rect rect, final String s, final String s2, final String s3) {
        final long currentTimeMillis = System.currentTimeMillis();
        final int orientation = this.getOrientation();
        Location currentLocation;
        if (this.mActivity.isOneShot() && !PermissionsUtil.areCallerGeoPermissionsGranted(this.mActivity)) {
            currentLocation = null;
        }
        else {
            currentLocation = this.mActivity.getGeoTagManager().getCurrentLocation();
        }
        boolean b = false;
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[savedFileType.ordinal()]) {
            default: {
                b = false;
                break;
            }
            case 3: {
                b = this.mActivity.shouldAddToMediaStore();
                break;
            }
            case 2: {
                b = this.mActivity.shouldAddToMediaStore();
                break;
            }
            case 1: {
                b = true;
                break;
            }
        }
        return new TakenStatusCommon(currentTimeMillis, orientation, currentLocation, rect.width(), rect.height(), s, s2, savedFileType, s3, "", b, false);
    }
    
    private RequestFactory.VideoSavingRequestBuilder createVideoSavingRequest(final RecordingProfile recordingProfile) {
        VideoSize videoSize = (VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        if (this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            final SlowMotion slowMotion = (SlowMotion)this.getUserSetting().get(UserSettingKey.SLOW_MOTION);
            videoSize = videoSize;
            if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                videoSize = slowMotion.getVideoSize();
            }
        }
        final MaxVideoSize maxVideoSize = this.mUserSettings.getMaxVideoSize(this.mStorage, this.getCurrentStorage(), recordingProfile);
        final long min = Math.min(maxVideoSize.getMaxFileSize(), StorageUtil.getStorageMaximumFileSize((Context)this.mActivity, this.getCurrentStorage()));
        final long n = maxVideoSize.getMaxDuration();
        final String mime = recordingProfile.getMime();
        final String extension = recordingProfile.getExtension();
        String string = null;
        if (this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            string = this.getUserSetting().get(UserSettingKey.SLOW_MOTION).toString();
        }
        final RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder = new RequestFactory.VideoSavingRequestBuilder(this.createTakenStatusCommon(SavingTaskManager.SavedFileType.VIDEO, videoSize.getVideoRect(), mime, extension, null), new TakenStatusVideo(n, min));
        ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).addCallback(this.mOnStoreCompletedListener);
        ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).setExtraOutput(this.mActivity.getExtraOutput());
        videoSavingRequestBuilder.setSlowMotion(string, this.mStorage, this.getCurrentStorage());
        ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).setOneShot(this.mActivity.isOneShotVideo());
        ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).setStorageType(this.getCurrentStorage());
        return videoSavingRequestBuilder;
    }
    
    private void doCapture(final RequestFactory.PhotoSavingRequestBuilder mLastPhotoSavingRequest) {
        this.mLastPhotoSavingRequest = mLastPhotoSavingRequest;
        if (this.mLastPhotoSavingRequest != null) {
            this.mCameraDeviceHandler.applySavingRequest(this.mLastPhotoSavingRequest);
            this.mViewFinder.showDisplayFlashScreen();
            this.mCameraDeviceHandler.takePicture(this.mLastPhotoSavingRequest);
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, this.isSettingChangeAcceptable());
            this.sendResearchCaptureEvents();
        }
    }
    
    private void doCaptureWhileRecording() {
        final RequestFactory.PhotoSavingRequestBuilder photoSavingRequest = this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
        if (photoSavingRequest != null) {
            if (this.mContentsViewController != null) {
                this.mContentsViewController.stopAnimation(false);
            }
            this.mViewFinder.onShutterDone(true);
            this.mCameraDeviceHandler.captureWhileRecording(photoSavingRequest);
        }
    }
    
    private void doChangeSelectedFace(final Point point) {
        this.mObjectTracking.stop();
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTION_STARTED, new Object[0]);
        this.mCameraDeviceHandler.setSelectedFacePosition(point.x, point.y);
        if (this.isTouchAeEnabled()) {
            this.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering)this.mUserSettings.get(UserSettingKey.METERING));
        }
    }
    
    private void doFastestCapture() {
        if (!this.isStorageWritable(this.getCurrentStorage())) {
            this.changeTo((State)new StateWarning(), new Object[0]);
            return;
        }
        this.pauseAudioPlaybackForCapture();
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[this.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Un-Expected state:");
                sb.append(this.mCameraDeviceHandler.getPreProcessState());
                throw new IllegalStateException(sb.toString());
            }
            case 5:
            case 6: {
                return;
            }
            case 7: {
                this.changeTo((State)new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 4: {
                this.changeTo((State)new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 3: {
                this.changeTo((State)new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 2: {
                this.changeTo((State)new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 1: {
                this.changeTo((State)new StatePhotoCapture(), new Object[0]);
            }
        }
    }
    
    private void doHandleRecordingError() {
        this.mViewFinder.showMessageDialog(DialogId.ERROR_UNKNOWN, new Object[0]);
        this.changeTo((State)new StateWarning(), new Object[0]);
    }
    
    private void doPauseRecording() {
        this.mCameraDeviceHandler.pauseRecording();
        this.changeTo((State)new StateVideoRecordingPausing(), new Object[0]);
    }
    
    private void doResumeRecording() {
        this.mCameraDeviceHandler.resumeRecording();
        this.changeTo((State)new StateVideoRecording(), new Object[0]);
    }
    
    private void doStartObjectTracking(final Rect rect) {
        if (this.isStorageWritable(this.getCurrentStorage())) {
            this.mObjectTracking.start(rect);
        }
    }
    
    private void doStartRecording(final boolean recordBySideSense) {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.disableClick();
        }
        VideoSize videoSize = (VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        if (this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            final SlowMotion slowMotion = (SlowMotion)this.getUserSetting().get(UserSettingKey.SLOW_MOTION);
            videoSize = videoSize;
            if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                videoSize = slowMotion.getVideoSize();
            }
        }
        final VideoHdr videoHdr = (VideoHdr)this.mUserSettings.get(UserSettingKey.VIDEO_HDR);
        final RecordingProfile build = new RecordingProfile.Builder().videoSize(videoSize).setOneShot(this.mActivity.isOneShot()).videoHdr(videoHdr).build();
        this.mLastVideoSavingRequest = this.createVideoSavingRequest(build);
        this.mViewFinder.setRecordingOrientation(this.mActivity.getOrientation());
        final RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest = this.mLastVideoSavingRequest;
        final int n = 1;
        int n2 = 0;
        Label_0594: {
            if (mLastVideoSavingRequest != null) {
                final boolean b = (this.getCurrentCapturingMode() != CapturingMode.SLOW_MOTION && this.mUserSettings.get(UserSettingKey.VIDEO_SIZE) == VideoSize.MMS) || this.mUserSettings.isLimitForSizeOrDuration();
                this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, (int)this.mLastVideoSavingRequest.mVideoStatus.maxDurationMills, b, !b && this.mUserSettings.get(UserSettingKey.CAPTURING_MODE) != CapturingMode.SLOW_MOTION, videoHdr);
                try {
                    switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.mUserSettings.get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                        default: {
                            this.mCameraDeviceHandler.prepareRecorder(this.mLastVideoSavingRequest, this.mRecorderListener, this.mOnSuperSlowRecordingFinishedListener, this.shouldPlayShutterSound(), build, this.mStorage.createNotifier(this.getCurrentStorage(), 10));
                            break;
                        }
                        case 2:
                        case 3: {
                            this.mCameraDeviceHandler.updateRecorder(this.mLastVideoSavingRequest, this.shouldPlayShutterSound());
                            break;
                        }
                    }
                    this.mCameraDeviceHandler.startRecording();
                    if (this.isSettingChangeAcceptable()) {
                        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, false);
                    }
                    this.updateDateTaken(this.mLastVideoSavingRequest);
                    this.mRecorderListener.setSavingRequestBuilder(this.mLastVideoSavingRequest);
                    n2 = n;
                    break Label_0594;
                }
                catch (final RuntimeException ex) {
                    CamLog.w("Start recording failed.", ex);
                    if (this.mContentsViewController != null) {
                        this.mContentsViewController.enableClick();
                    }
                    if (this.isCurrentStorageExternal() && !this.isStorageWritable(Storage.StorageType.EXTERNAL_CARD)) {
                        this.mViewFinder.showMessageDialog(DialogId.COULD_NOT_START_RECORDING, new Object[0]);
                        this.changeTo((State)new StateVideoReady(), new Object[0]);
                        return;
                    }
                    this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
                    ResearchUtil.getInstance().sendCameraNotAvailableEvent();
                }
            }
            n2 = 0;
        }
        ResearchUtil.getInstance().setRecordBySideSense(recordBySideSense);
        if (n2 != 0) {
            this.transitionToRecordingState(false);
        }
        else {
            this.changeTo((State)new StateWarning(), new Object[0]);
        }
    }
    
    private void doStopObjectTracking() {
        this.mObjectTracking.stop();
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
    }
    
    private void doStopRecording(final boolean b) {
        PerfLog.STOP_REC.begin();
        if (this.mContentsViewController != null) {
            this.mContentsViewController.enableClick();
        }
        final Optional<Long> stopRecording = this.mCameraDeviceHandler.stopRecording(b);
        if (stopRecording.isPresent()) {
            ResearchUtil.getInstance().setOrientation(this.mLastVideoSavingRequest.mCommonStatus.orientation);
            LocalResearchUtil.getInstance().sendEventSettings();
            LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO);
            LocalResearchUtil.getInstance().sendRecordingEvent(Event.CaptureOperation.RECORDING, this.getCurrentRecordingStopOperation(), stopRecording.get().intValue(), false);
        }
        this.mViewFinder.onCaptureDone();
        if (VideoStabilizer.isIntelligentActive((VideoStabilizer)this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER))) {
            ((RequestFactory.RequestBuilder)this.mLastVideoSavingRequest).setRequestId(this.mViewFinder.getRequestId(false));
        }
        PerfLog.STOP_REC.end();
    }
    
    private void doZoomChangeAngle() {
        this.changeTo((State)new StateCropping(this.mCurrentState.getCaptureState()), new Object[0]);
    }
    
    private void finishOneShot(final StoreDataResult storeDataResult, final Bitmap bitmap) {
        this.mActivity.finishOneShot(new OneShotResult(storeDataResult.uri, storeDataResult.storeResult, storeDataResult.savingRequest, bitmap));
    }
    
    private CameraInfo.CameraId getCameraId(final CapturingMode capturingMode) {
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return CameraInfo.CameraId.BACK;
            }
            case 2:
            case 6:
            case 7: {
                return CameraInfo.CameraId.FRONT;
            }
            case 1:
            case 5:
            case 8: {
                return CameraInfo.CameraId.BACK;
            }
        }
    }
    
    private Event.StopOperation getCurrentRecordingStopOperation() {
        if (this.mActivity.isAlreadyHighTemperature()) {
            return Event.StopOperation.THERMAL_STOP;
        }
        if (this.mActivity.isAlreadyBcl()) {
            return Event.StopOperation.LOWBATTERY_STOP;
        }
        return Event.StopOperation.USER_STOP;
    }
    
    private Storage.StorageType getCurrentStorage() {
        if (this.mActivity.isOneShot()) {
            return this.mActivity.getLaunchCondition().getStorageTypeForOneshot();
        }
        DestinationToSave destinationToSave;
        if ((destinationToSave = (DestinationToSave)this.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE)) == null) {
            destinationToSave = (DestinationToSave)this.mUserSettings.get(this.mActivity.getLaunchCondition().getCapturingMode(), UserSettingKey.DESTINATION_TO_SAVE);
        }
        return destinationToSave.getType();
    }
    
    private static <T> T getEventParam(final Object[] array, final int n, final Class<T> clazz, final T t) {
        if (array != null && array.length > n && clazz.isInstance(array[n])) {
            return (T)array[n];
        }
        if (array == null) {
            CamLog.d("Specified parameter is empty.");
        }
        else if (array.length <= n) {
            CamLog.d("Specified parameter count is too short");
        }
        else if (!array[n].getClass().isInstance(clazz)) {
            CamLog.d("Specified parameter type is missmatch.");
        }
        return t;
    }
    
    private int getOrientation() {
        final int normalizedRotation = RotationUtil.getNormalizedRotation(this.mActivity.getSensorOrientationDegree());
        final CameraInfo cameraInfo = this.mCameraDeviceHandler.getCameraInfo();
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ORIENTATION:sensorOrientation:");
            sb.append(normalizedRotation);
            CamLog.d(sb.toString());
            CamLog.d("ORIENTATION:cameraOrientation", RotationUtil.orientationToString(cameraInfo.orientation));
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ORIENTATION:cameraFacing:");
            sb2.append(cameraInfo.facing);
            CamLog.d(sb2.toString());
        }
        int n = 0;
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId[cameraInfo.facing.ordinal()]) {
            default: {
                n = (cameraInfo.orientation + normalizedRotation) % 360;
                break;
            }
            case 2: {
                n = (cameraInfo.orientation + 360 - normalizedRotation) % 360;
                break;
            }
            case 1: {
                n = (cameraInfo.orientation + normalizedRotation) % 360;
                break;
            }
        }
        return n;
    }
    
    private Rect getPreviewRect() {
        final CapturingMode capturingMode = (CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
        Rect rect;
        if (capturingMode.isVideo()) {
            rect = ((VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect();
        }
        else {
            rect = ((Resolution)this.mUserSettings.get(UserSettingKey.RESOLUTION)).getPictureRect();
        }
        return LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mCameraDeviceHandler.getPreviewRect(capturingMode, rect));
    }
    
    private int getSensorOrientation() {
        int i;
        if (this.mActivity.getLastDetectedOrientation() == CameraActivity.LayoutOrientation.Portrait) {
            i = 1;
        }
        else {
            i = 2;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getOrientation: sensor orientation:");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        return i;
    }
    
    private boolean hasRemainSavingRequest() {
        return this.mCameraDeviceHandler.getRemainPrevSavingRequestCount() > 0;
    }
    
    private void initGeoTagManager() {
        if (this.mActivity.getGeoTagManager() != null) {
            if (this.mActivity.getGeoTagManager().isGeotagPermissionGranted()) {
                this.mUserSettings.set(Geotag.ON);
                this.mActivity.getGeoTagManager().setIsGeotagPermissionGranted(false);
            }
            this.mActivity.getGeoTagManager().initGeotag(this.mActivity, this.mActivity.isAllowToUseLocation());
            this.mActivity.getGeoTagManager().notifyStatus();
        }
    }
    
    private void initSideSenseSetting() {
        if (!SettingUtil.isSideSenseEnabled(true)) {
            this.mUserSettings.set(SideSense.OFF);
        }
    }
    
    private boolean isAllSnapshotCompleted() {
        return this.mCameraDeviceHandler.getRemainSavingPhotoRequestCount() == 0;
    }
    
    private boolean isBurstByCameraKeyEnabled() {
        return this.mUserSettings.get(UserSettingKey.CAMERA_KEY) == CameraKey.BURST_SHOT;
    }
    
    private boolean isCurrentStorageExternal() {
        return this.getCurrentStorage() == Storage.StorageType.EXTERNAL_CARD;
    }
    
    private boolean isEnoughStorageSizeAvailableForOneShotVideo() {
        final boolean oneShotVideo = this.mActivity.isOneShotVideo();
        boolean b = true;
        final boolean b2 = true;
        if (!oneShotVideo) {
            return true;
        }
        final VideoSize videoSize = (VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        final VideoHdr videoHdr = (VideoHdr)this.mUserSettings.get(UserSettingKey.VIDEO_HDR);
        VideoSize videoSize2 = videoSize;
        if (this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            final SlowMotion slowMotion = (SlowMotion)this.getUserSetting().get(UserSettingKey.SLOW_MOTION);
            videoSize2 = videoSize;
            if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                videoSize2 = slowMotion.getVideoSize();
            }
        }
        final RecordingProfile build = new RecordingProfile.Builder().videoSize(videoSize2).setOneShot(true).videoHdr(videoHdr).build();
        final MaxVideoSize maxVideoSize = this.mUserSettings.getMaxVideoSize(this.mStorage, this.getCurrentStorage(), build);
        if (maxVideoSize.getMaxDuration() > 0) {
            return maxVideoSize.getMaxDuration() >= 1000 && b2;
        }
        if (maxVideoSize.getMaxFileSize() > 0L) {
            if (maxVideoSize.getMaxFileSize() < build.minFileSize * 1024L) {
                b = false;
            }
            return b;
        }
        return true;
    }
    
    private boolean isFusionMonitoringNeeded() {
        final FusionMode[] options = FusionMode.getOptions(this.getCurrentCapturingMode());
        for (int length = options.length, i = 0; i < length; ++i) {
            if (options[i] != FusionMode.OFF) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isLazyInitializationRunning() {
        return this.mActivity.isLazyInitializationRunning();
    }
    
    private boolean isNeedRepairRequestId(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId() == -1) {
            if (photoSavingRequestBuilder.getCaptureIdForPredictiveCapture() == -1) {
                return true;
            }
            if (((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getSomcType() == 100) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isPhotoSelfTimerEnabled() {
        return this.mViewFinder.getPhotoSelfTimerSetting() != SelfTimer.OFF;
    }
    
    private boolean isSmoothZoomEnabled() {
        return !this.mCameraDeviceHandler.isCameraFront();
    }
    
    private boolean isStorageFull(final Storage.StorageType storageType) {
        return this.mStorage.getCurrentState(storageType) == Storage.StorageState.FULL;
    }
    
    private boolean isStorageWritable(@NonNull final Storage.StorageType storageType) {
        final Storage.StorageState currentState = this.mStorage.getCurrentState(storageType);
        return currentState != null && currentState.isWritable();
    }
    
    private boolean isTouchAeEnabled() {
        return PlatformCapability.isTouchAeSupported(this.getCurrentCameraId()) && this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE) != TouchCapture.ON && ((this.getCurrentCapturingMode() == CapturingMode.SCENE_RECOGNITION || this.getCurrentCapturingMode() == CapturingMode.SUPERIOR_FRONT || this.getCurrentCapturingMode() == CapturingMode.NORMAL) && this.mUserSettings.get(UserSettingKey.TOUCH_INTENTION) == TouchIntention.FOCUS_AND_EXPOSURE);
    }
    
    private boolean isVideo() {
        return this.getCurrentCapturingMode().getType() == 2;
    }
    
    private void moveToCameraNotAvailable() {
        CamLog.e(".startFastCapture():[Camera not available]");
        this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
        ResearchUtil.getInstance().sendCameraNotAvailableEvent();
    }
    
    private void notifyCoolingUltraLow(final boolean b) {
        if (this.mViewFinder != null) {
            this.mViewFinder.onNotifyCoolingUltraLow(b);
        }
        this.mCameraDeviceHandler.setUltraLowPower();
        if (this.mViewFinder != null && this.mViewFinder.isSetupHeadupDisplayInvoked() && this.mCameraDeviceHandler.isObjectTrackingRunning()) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
        }
        this.mObjectTracking.stop();
        this.mGestureShutter.handlePreviewStopped();
    }
    
    private NotifyDelayedEventTask notifyDelayedEvent(final TransitterEvent transitterEvent, final Object... array) {
        final NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(transitterEvent, array);
        this.mHandler.postDelayed((Runnable)notifyDelayedEventTask, 100L);
        return notifyDelayedEventTask;
    }
    
    private void notifySceneRecognitionDisabled() {
        final CameraParameters.SceneRecognitionResult sceneRecognitionResult = new CameraParameters.SceneRecognitionResult();
        sceneRecognitionResult.sceneMode = CameraParameterConverter.SceneMode.getSceneMode(0);
        sceneRecognitionResult.deviceStabilityCondition = CameraParameters.DeviceStabilityCondition.getCondition(0);
        sceneRecognitionResult.isMacroRange = false;
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED, sceneRecognitionResult);
    }
    
    private void onOneShotStoreCompleted(final StoreDataResult storeDataResult) {
        if (storeDataResult.savingRequest.getExtraOutput() == null) {
            this.requestLoadStoredPicture(storeDataResult);
        }
        else {
            this.finishOneShot(storeDataResult, null);
        }
    }
    
    private void onPredictiveCaptureStoreComplete(final StoreDataResult storeDataResult) {
        PerfLog.BURST_STORE_COMPLETE.transit();
        if (this.mPredictiveCaptureStoreInfo != null && storeDataResult.savingRequest.getSaveTimeForPredictiveCapture().equals(this.mPredictiveCaptureStoreInfo.getCaptureTime())) {
            this.mPredictiveCaptureStoreInfo = null;
        }
        final Intent intent = new Intent("com.sonyericsson.android.camera.intent.action.PREDICTIVE_CAPTURE_SAVE_COMPLETED");
        intent.putExtra("com.sonyericsson.android.camera.extra.PREDICTIVE_CAPTURE_DIRECTORY_PATH", PredictiveCapturePathBuilder.getPredictiveCaptureGroupIdPath(storeDataResult.savingRequest.getFilePath()));
        this.mActivity.sendBroadcast(intent);
    }
    
    private void onZoomChange(final int i) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mViewFinder != null && this.mViewFinder.isSetupHeadupDisplayInvoked()) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, i);
        }
    }
    
    private void pauseAudioPlaybackForCapture() {
        if (this.mIsPausedAudioPlayback) {
            return;
        }
        if (this.shouldPlayShutterSound()) {
            this.mActivity.pauseAudioPlayback();
            this.mIsPausedAudioPlayback = true;
        }
    }
    
    private void pauseAudioPlaybackForRecord() {
        this.mActivity.pauseAudioPlayback();
        this.mIsPausedAudioPlayback = true;
    }
    
    private void pauseVideoRecording(final Object... array) {
        this.doStopRecording(false);
        this.changeTo((State)new StatePause((boolean)array[0]), array);
    }
    
    private void playShutterSound() {
        if (this.shouldPlayShutterSound()) {
            this.mCameraDeviceHandler.playShutterSound(this.getCurrentCapturingMode().getType());
        }
    }
    
    public static final void preload() {
    }
    
    private void prepareZoom() {
    }
    
    private void removeChangeCameraModeTask() {
        this.mHandler.removeCallbacks((Runnable)this.mChangeCameraModeTask);
    }
    
    private void removeDelayedEvent(final NotifyDelayedEventTask notifyDelayedEventTask) {
        this.mHandler.removeCallbacks((Runnable)notifyDelayedEventTask);
    }
    
    private void removeStartRecordingTask() {
        this.mHandler.removeCallbacks((Runnable)this.mStartRecordingTask);
    }
    
    private void requestChangeModeTo(final CapturingMode capturingMode, final AnimationRequest.AnimationType animationType) {
        if (this.getCurrentCapturingMode() != capturingMode) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8: {
                    this.sendResearchSameActivityEvent(capturingMode);
                    this.mCameraDeviceHandler.savePreloadSettings((CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
                    if (this.isFusionMonitoringNeeded()) {
                        this.mCameraDeviceHandler.stopFusionMonitoring();
                        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, null);
                    }
                    this.changeModeTo(capturingMode, animationType);
                    break;
                }
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("The specified mode is already set, mode:");
            sb.append(this.getCurrentCapturingMode().name());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    private void requestLoadStoredPicture(final StoreDataResult storeDataResult) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke uri:");
            sb.append(storeDataResult.uri);
            sb.append(", semcType:");
            sb.append(storeDataResult.savingRequest.getSomcType());
            sb.append(", OneShot:");
            sb.append(this.mActivity.isOneShot());
            CamLog.d(sb.toString());
        }
        PerfLog.BYPASSCAMERA_ON_STORE_COMPLETE.transit();
        this.mStorage.requestLoad(storeDataResult.uri, storeDataResult.savingRequest.common.orientation, (Storage.OnLoadCompletedListener)new Storage.OnLoadCompletedListener(this, storeDataResult) {
            final StateMachine this$0;
            final StoreDataResult val$result;
            
            @Override
            public void onDataLoadCompleted(final int n, final boolean b, final LinkedList<Content.ContentInfo> list, final Bitmap bitmap) {
            }
            
            @Override
            public void onDataLoadFailed(final int n) {
            }
            
            @Override
            public void onLoadCompleted(final Uri obj, final Bitmap bitmap) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke uri:");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
                this.this$0.finishOneShot(this.val$result, bitmap);
            }
            
            @Override
            public void onLoadFailed(final Uri obj, final int i) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke uri:");
                    sb.append(obj);
                    sb.append(", reason:");
                    sb.append(i);
                    CamLog.d(sb.toString());
                }
                this.this$0.finishOneShot(this.val$result, null);
            }
        });
    }
    
    private void requestPhotoSmileCapture() {
        this.requestSmileCapture(((SmileCapture)this.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).getIntValue());
    }
    
    private void requestResizeEvf(final CapturingMode capturingMode, final boolean b) {
        if (this.mCurrentState.getCaptureState() == CaptureState.STATE_PAUSE && b) {
            return;
        }
        Rect rect = null;
        boolean b2 = false;
        Label_0138: {
            if (capturingMode.isVideo()) {
                if (capturingMode == CapturingMode.SLOW_MOTION) {
                    rect = ((SlowMotion)this.mUserSettings.get(capturingMode, UserSettingKey.SLOW_MOTION)).getVideoSize().getVideoRect();
                }
                else {
                    rect = ((VideoSize)this.mUserSettings.get(capturingMode, UserSettingKey.VIDEO_SIZE)).getVideoRect();
                    if (this.mUserSettings.get(capturingMode, UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                        b2 = true;
                        rect = rect;
                        break Label_0138;
                    }
                }
            }
            else {
                rect = ((Resolution)this.mUserSettings.get(capturingMode, UserSettingKey.RESOLUTION)).getPictureRect();
            }
            b2 = false;
        }
        final Rect previewRect = this.mCameraDeviceHandler.getPreviewRect(capturingMode, rect);
        if (previewRect != null) {
            final Size surfaceSize = PlatformDependencyResolver.getSurfaceSize(previewRect, b2);
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_RESIZE_EVF_SCOPE, new Rect(0, 0, surfaceSize.getWidth(), surfaceSize.getHeight()), b);
        }
    }
    
    private void requestSmileCapture(final int n) {
        if (SystemClock.uptimeMillis() - this.mLastSmileCaptureTakenTime > 1000L && n < this.mViewFinder.getSelectedFaceSmileScore()) {
            this.mVirtualKeyEventDispatcher.sendVirtualKeyEvent(UserEventHandler.VirtualKeyEvent.SMILE_CAPTURE);
            this.mLastSmileCaptureTakenTime = SystemClock.uptimeMillis();
        }
    }
    
    private void requestStorePicture(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        synchronized (this) {
            if (this.isNeedRepairRequestId(photoSavingRequestBuilder) && this.mContentsViewController != null) {
                ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setRequestId(this.mViewFinder.getRequestId(false));
            }
            this.mExecService.execute(new RequestStoreTask(photoSavingRequestBuilder));
        }
    }
    
    private void requestStoreVideo(final RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder) {
        monitorenter(this);
        if (videoSavingRequestBuilder == null) {
            monitorexit(this);
            return;
        }
        try {
            if (this.mContentsViewController != null) {
                this.mContentsViewController.stopAnimation(false);
            }
            if ((!VideoStabilizer.isIntelligentActive((VideoStabilizer)this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER)) || ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).getRequestId() == -1) && this.mContentsViewController != null) {
                ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).setRequestId(this.mViewFinder.getRequestId(false));
            }
            ((RequestFactory.RequestBuilder)videoSavingRequestBuilder).setDateTaken(System.currentTimeMillis());
            final SavingRequest savingRequest = RequestFactory.createSavingRequest((RequestFactory.RequestBuilder)videoSavingRequestBuilder);
            this.mStorage.requestStore(savingRequest, savingRequest.getStorageType(), this.mOnStoreCompletedListener);
            this.sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
        }
        finally {
            monitorexit(this);
        }
    }
    
    private void requestVideoSmileCapture() {
        this.requestSmileCapture(((VideoSmileCapture)this.mUserSettings.get(UserSettingKey.VIDEO_SMILE_CAPTURE)).getIntValue());
    }
    
    private void sendResearchCaptureEvents() {
        LocalResearchUtil.getInstance().sendEventSettings();
        LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
        ResearchUtil.getInstance().setOrientation(this.mLastPhotoSavingRequest.mCommonStatus.orientation);
        LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
        ResearchUtil.getInstance().setTimeCaptureStart();
    }
    
    private void sendResearchSameActivityEvent(final CapturingMode capturingMode) {
        LocalResearchUtil.getInstance().clearAllSettings();
        LocalResearchUtil.getInstance().clearTemporarySettingValues();
        ResearchUtil.getInstance().onPause(true);
        LocalResearchUtil.getInstance().sendView(LaunchCondition.LaunchTrigger.SAME_ACTIVITY, capturingMode);
    }
    
    private void sendResearchViewEvent() {
        LaunchCondition.LaunchTrigger launchTrigger;
        if ((launchTrigger = this.mActivity.getLaunchCondition().getLaunchTrigger()) == LaunchCondition.LaunchTrigger.VIDEO_EDITOR) {
            launchTrigger = LaunchCondition.LaunchTrigger.OTHER;
        }
        LocalResearchUtil.getInstance().sendView(launchTrigger, this.getCurrentCapturingMode());
    }
    
    private void sendVideoChapterThumbnailToViewFinder() {
        if (this.mChapterThumbnail != null && this.mViewFinder.isHeadUpDisplayReady()) {
            if (this.mLastVideoSavingRequest != null) {
                this.mChapterThumbnail.setOrientation(this.mLastVideoSavingRequest.mCommonStatus.orientation);
            }
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ADD_VIDEO_CHAPTER, this.mChapterThumbnail);
            this.mChapterThumbnail = null;
        }
    }
    
    private void setCurrentCapturingMode(final CapturingMode capturingMode) {
        this.getUserSetting().changeCapturingMode(capturingMode);
        LocalResearchUtil.getInstance().setSettingsValue(this.getUserSetting(), capturingMode);
    }
    
    private void setIsSceneRecognitionValid(final boolean mIsSceneRecognitionValid) {
        this.mIsSceneRecognitionValid = mIsSceneRecognitionValid;
    }
    
    private boolean shouldPlayShutterSound() {
        return this.mUserSettings.get(this.getCurrentCapturingMode(), UserSettingKey.SHUTTER_SOUND) != ShutterSound.OFF;
    }
    
    private void showBlackScreen() {
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_SHOW_BLACK_SCREEN, new Object[0]);
    }
    
    private boolean startAutoFocus() {
        if (this.mActivity != null) {
            if (!this.mStorage.isStorageActivated() || !this.isStorageWritable(this.getCurrentStorage())) {
                if (CamLog.DEBUG) {
                    CamLog.d("Storage is not ready");
                }
                return false;
            }
            this.pauseAudioPlaybackForCapture();
        }
        this.mCameraDeviceHandler.autoFocus();
        return true;
    }
    
    private void startFastCapture(final FastCapture fastCapture, final StartupAction startupAction) {
        final Rect previewRect = this.getPreviewRect();
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, previewRect.width() / (float)previewRect.height(), this.mActivity.getScreenAspect());
        PositionConverter.getInstance().init(((CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isFront(), surfaceViewRect, previewRect, PlatformCapability.getActiveArraySize(this.getCurrentCameraId()));
        PositionConverter.getInstance().setSurfaceSize(surfaceViewRect.width(), surfaceViewRect.height());
        PositionConverter.getInstance().setPreviewSize(previewRect.width(), previewRect.height());
        this.mActivity.requestPostLazyInitializationTaskExecute();
        if (!this.mActivity.awaitSetupAllReady()) {
            CamLog.e("Setup failed");
        }
        if (this.mViewFinder.isHeadUpDisplayReady() && this.isTutorialNeededToBeShownForCurrentMode()) {
            this.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
        }
        else if (this.isVideo()) {
            this.changeTo((State)new StateVideoReady(startupAction), new Object[0]);
        }
        else {
            this.changeTo((State)new StatePhotoReady(true, true, startupAction), new Object[0]);
        }
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture[fastCapture.ordinal()]) {
            default: {
                return;
            }
            case 3: {
                throw new IllegalArgumentException("StateMachine.Resume:[FastCapture OFF]");
            }
            case 2: {
                if (this.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
                    if (!PlatformCapability.hasDeviceError()) {
                        this.doFastestCapture();
                    }
                    this.mActivity.getLaunchCondition().clearExtraOperation();
                }
            }
            case 1: {}
        }
    }
    
    private void stopPlaySound() {
        this.mActivity.stopPlayingSound();
    }
    
    private void storePicture(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        final SavingRequest savingRequest = RequestFactory.createSavingRequest((RequestFactory.RequestBuilder)photoSavingRequestBuilder);
        this.mPredictiveApplier.entrySuppressor(savingRequest);
        this.mStorage.requestStore(savingRequest, savingRequest.getStorageType(), this.mOnStoreCompletedListener);
    }
    
    private void storeSavingRequestList() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke mPhotoSavingRequestList.size():");
            sb.append(this.mPhotoSavingRequestList.size());
            CamLog.d(sb.toString());
        }
        if (!this.mPhotoSavingRequestList.isEmpty()) {
            for (final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder : this.mPhotoSavingRequestList) {
                ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setRequestId(this.mViewFinder.getRequestId(false));
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("storePicture() requestId:");
                    sb2.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                    CamLog.d(sb2.toString());
                }
                final SavingRequest savingRequest = RequestFactory.createSavingRequest((RequestFactory.RequestBuilder)photoSavingRequestBuilder);
                this.mStorage.requestStore(savingRequest, savingRequest.getStorageType(), this.mOnStoreCompletedListener);
            }
            this.mPhotoSavingRequestList.clear();
        }
    }
    
    private void switchCamera(final CapturingMode obj, final AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke requestMode:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mCameraDeviceHandler.savePreloadSettings((CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
        this.mCameraDeviceHandler.releaseRecorder();
        if (this.isFusionMonitoringNeeded()) {
            this.mCameraDeviceHandler.stopFusionMonitoring();
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, null);
        }
        this.mCameraDeviceHandler.stopPreviewSynchronized();
        this.mCameraDeviceHandler.closeCamera();
        this.changeModeTo(obj, animationType);
        this.requestResizeEvf(obj, false);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED, obj, false, animationType);
        this.sendResearchSameActivityEvent(this.getCurrentCapturingMode());
    }
    
    private void switchCamera(final AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke current:");
            sb.append(this.getCurrentCapturingMode());
            CamLog.d(sb.toString());
        }
        CapturingMode capturingMode = this.getCurrentCapturingMode();
        switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            case 7: {
                capturingMode = CapturingMode.VIDEO;
                break;
            }
            case 6: {
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 5: {
                capturingMode = CapturingMode.SUPERIOR_FRONT;
                break;
            }
            case 3:
            case 8: {
                capturingMode = CapturingMode.FRONT_VIDEO;
                break;
            }
            case 2: {
                capturingMode = CapturingMode.NORMAL;
                break;
            }
            case 1: {
                capturingMode = CapturingMode.FRONT_PHOTO;
                break;
            }
        }
        this.switchCamera(capturingMode, animationType);
    }
    
    private void switchSceneRecognition(final boolean b) {
        if (this.mIsSceneRecognitionValid && b) {
            this.mCameraDeviceHandler.startSceneRecognition();
        }
        else {
            this.mCameraDeviceHandler.stopSceneRecognition();
        }
    }
    
    private void switchVideoFaceDetection() {
        if (DependencyCheckUtil.isFaceDetectionAvailable(this.getCurrentCapturingMode(), (VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr)this.mUserSettings.get(UserSettingKey.VIDEO_HDR))) {
            this.mCameraDeviceHandler.startFaceDetection();
        }
        else {
            this.mCameraDeviceHandler.stopFaceDetection();
        }
    }
    
    private void transitionToRecordingState(final boolean b) {
        if (this.mActivity != null) {
            this.mActivity.disableAutoPowerOffTimer();
        }
        this.mChapterThumbnail = null;
        if (!b) {
            this.mCameraDeviceHandler.requestOnePreviewFrame();
        }
        if (this.mActivity != null) {
            final int n = StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCurrentCapturingMode().ordinal()];
            if (n != 3) {
                switch (n) {
                    case 7:
                    case 8: {
                        this.changeTo((State)new StateVideoRecording(false), new Object[0]);
                        break;
                    }
                }
            }
            else {
                this.mHighFrameRateVideoRecordingCountInSuperSlowMotion = 0;
                switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                    case 3: {
                        this.changeTo((State)new StateHighFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                        this.mViewFinder.startSlowMotionFeedbackAnimation();
                        break;
                    }
                    case 2: {
                        this.changeTo((State)new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                        break;
                    }
                    case 1: {
                        this.changeTo((State)new StateVideoRecording(), new Object[0]);
                        break;
                    }
                }
            }
        }
    }
    
    private void updateAmberBlueColor(final float f) {
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(this.getCurrentCameraId());
        final int intValue = cameraCapability.MIN_AWB_AB.get();
        final int intValue2 = cameraCapability.MAX_AWB_AB.get();
        final int semiAutoSettingAmberBlueValue = (int)Math.ceil(intValue + (intValue2 - intValue) * f);
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke color-changed slider:");
            sb.append(f);
            sb.append(", min:");
            sb.append(intValue);
            sb.append(", max:");
            sb.append(intValue2);
            sb.append(", value:");
            sb.append(semiAutoSettingAmberBlueValue);
            CamLog.d(sb.toString());
        }
        this.mCameraDeviceHandler.setAmberBlueColorAndCommit(semiAutoSettingAmberBlueValue);
        LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(semiAutoSettingAmberBlueValue);
    }
    
    private void updateBrightness(final float f) {
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(this.getCurrentCameraId());
        final int intValue = cameraCapability.EV_MIN.get();
        final int intValue2 = cameraCapability.EV_MAX.get();
        final int semiAutoSettingBrightnessValue = (int)Math.ceil(intValue + (intValue2 - intValue) * f);
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke brightness-changed slider:");
            sb.append(f);
            sb.append(", min:");
            sb.append(intValue);
            sb.append(", max:");
            sb.append(intValue2);
            sb.append(", value:");
            sb.append(semiAutoSettingBrightnessValue);
            CamLog.d(sb.toString());
        }
        this.mCameraDeviceHandler.setBrightnessAndCommit(semiAutoSettingBrightnessValue);
        LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(semiAutoSettingBrightnessValue);
    }
    
    private void updateDateTaken(final RequestFactory.RequestBuilder requestBuilder) {
        requestBuilder.setDateTaken(System.currentTimeMillis());
    }
    
    private void updateFusionModeSetting(final FusionMode fusionMode) {
        final FusionMode fusionMode2 = (FusionMode)this.mUserSettings.get(UserSettingKey.FUSION_MODE);
        if (fusionMode == fusionMode2) {
            return;
        }
        final int n = StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCurrentCapturingMode().ordinal()];
        if (n != 1) {
            if (n == 8) {
                this.mUserSettings.set(fusionMode);
                LocalResearchUtil.getInstance().setSettingsValue(fusionMode2, fusionMode, this.getCurrentCapturingMode());
            }
        }
        else {
            final Iso iso = (Iso)this.mUserSettings.get(UserSettingKey.ISO);
            this.mUserSettings.set(fusionMode);
            if (iso != this.mUserSettings.get(UserSettingKey.ISO)) {
                this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ISO_CHANGED_BY_FUSION, new Object[0]);
            }
            LocalResearchUtil.getInstance().setSettingsValue(fusionMode2, fusionMode, this.getCurrentCapturingMode());
        }
    }
    
    private void updatePhotoSelftimer(final SelfTimer selfTimer) {
        if (this.mViewFinder != null) {
            this.mViewFinder.setSelfTimer((CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), selfTimer);
        }
    }
    
    private void updateRecordingProgress(final int i) {
        if (this.mViewFinder != null && this.mViewFinder.isSetupHeadupDisplayInvoked()) {
            if (this.mActivity != null) {
                this.mActivity.disableAutoPowerOffTimer();
            }
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_RECORDING_PROGRESS, i);
        }
    }
    
    public void addOnStateChangedListener(final OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.add(onStateChangedListener);
    }
    
    public boolean canApplicationBeFinished() {
        synchronized (this.mCurrentState) {
            return this.mCurrentState.getCaptureState().canApplicationBeFinished();
        }
    }
    
    public boolean canHandleAsynchronizedTask() {
        return this.mCurrentState.mCaptureState.canHandleAsynchronizedTask();
    }
    
    public boolean canHandleWearableCaptureRequest() {
        return this.mCurrentState.mCaptureState.canHandleWearableCaptureRequest();
    }
    
    public RequestFactory.PhotoSavingRequestBuilder createPhotoSavingRequest(final SavingTaskManager.SavedFileType obj) {
        if (obj != SavingTaskManager.SavedFileType.BURST && obj != SavingTaskManager.SavedFileType.PHOTO && obj != SavingTaskManager.SavedFileType.PHOTO_DURING_REC) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected type:");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
        final Resolution resolution = (Resolution)this.mUserSettings.get(UserSettingKey.RESOLUTION);
        TakenStatusPhoto.Facing facing;
        if (this.getCurrentCapturingMode().isFront()) {
            facing = TakenStatusPhoto.Facing.FRONT;
        }
        else {
            facing = TakenStatusPhoto.Facing.BACK;
        }
        final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = new RequestFactory.PhotoSavingRequestBuilder(this.createTakenStatusCommon(obj, resolution.getPictureRect(), "image/jpeg", ".JPG", null), new TakenStatusPhoto(facing), false);
        ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).addCallback(this.mOnStoreCompletedListener);
        photoSavingRequestBuilder.setOneshot(this.mActivity.isOneShotPhoto());
        if (obj == SavingTaskManager.SavedFileType.BURST) {
            ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setSomcType(129);
            ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setStorageType(Storage.StorageType.INTERNAL);
        }
        else {
            ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setStorageType(this.getCurrentStorage());
        }
        ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setExtraOutput(this.mActivity.getExtraOutput());
        return photoSavingRequestBuilder;
    }
    
    public CameraInfo.CameraId getCurrentCameraId() {
        return this.getCameraId(this.getCurrentCapturingMode());
    }
    
    @Deprecated
    public CaptureState getCurrentCaptureState() {
        synchronized (this) {
            return this.mCurrentState.getCaptureState();
        }
    }
    
    public CapturingMode getCurrentCapturingMode() {
        if (this.getUserSetting().get(UserSettingKey.CAPTURING_MODE) == null) {
            return this.mActivity.getLaunchCondition().getCapturingMode();
        }
        return (CapturingMode)this.getUserSetting().get(UserSettingKey.CAPTURING_MODE);
    }
    
    public CapturingMode getLaunchCapturingMode() {
        return this.mActivity.getLaunchCondition().getCapturingMode();
    }
    
    public PredictiveCaptureStoreInfo getPredictiveCaptureStoreInfo() {
        return this.mPredictiveCaptureStoreInfo;
    }
    
    public UserSettings getUserSetting() {
        return this.mUserSettings;
    }
    
    public UserEventHandler.VirtualKeyEventDispatcher getVirtualKeyEventDispatcher() {
        return this.mVirtualKeyEventDispatcher;
    }
    
    public Float getZoom() {
        return this.mCameraDeviceHandler.getZoom();
    }
    
    public boolean isAngleEventReceivable() {
        synchronized (this) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[this.mCurrentState.getCaptureState().ordinal()]) {
                default: {
                    return false;
                }
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8: {
                    return true;
                }
            }
        }
    }
    
    public boolean isDialogOpened() {
        synchronized (this) {
            return this.mCurrentState.getCaptureState() == CaptureState.STATE_OPERATION_RESTRICTED;
        }
    }
    
    public boolean isMenuAvailable() {
        return this.mCurrentState.getCaptureState().isMenuAvailable();
    }
    
    public boolean isRecording() {
        return this.mCurrentState != null && this.mCurrentState.getCaptureState().isRecording();
    }
    
    public boolean isSettingChangeAcceptable() {
        return !this.isLazyInitializationRunning() && this.isAllSnapshotCompleted();
    }
    
    public boolean isTutorialNeededToBeShownForCurrentMode() {
        if (this.mActivity.isOneShot() || this.mActivity.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            return false;
        }
        final MessageSettings messageSettings = this.mActivity.getStoredSettings().getMessageSettings();
        final CapturingMode currentCapturingMode = this.getCurrentCapturingMode();
        if (currentCapturingMode.isFront()) {
            if (currentCapturingMode.isVideo()) {
                return messageSettings.isNeverShow(MessageType.TUTORIAL_EYE_GUIDE) ^ true;
            }
            return messageSettings.isNeverShow(MessageType.TUTORIAL_HAND_SHUTTER) ^ true;
        }
        else {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                default: {
                    return currentCapturingMode.isVideo() && !this.mActivity.isOneShotVideo() && PlatformCapability.isHighSensitivityFusionSupported(currentCapturingMode.getCameraId()) && (messageSettings.isNeverShow(MessageType.TUTORIAL_VIDEO_FUSION) ^ true);
                }
                case 3: {
                    return messageSettings.isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT) ^ true;
                }
                case 2: {
                    return messageSettings.isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION) ^ true;
                }
                case 1: {
                    return messageSettings.isNeverShow(MessageType.TUTORIAL_STANDARD_SLOW_MOTION) ^ true;
                }
            }
        }
    }
    
    public boolean isVideoRecording() {
        return this.mIsVideoRecording;
    }
    
    public void onAutoFocusDone(final boolean b, final boolean b2, final boolean b3, final int n, final int n2, final int n3) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke isHighQualityBurstAvailable:");
            sb.append(b);
            sb.append(", isAfSuccessed:");
            sb.append(b2);
            sb.append(", requireDisplayFlash:");
            sb.append(b3);
            CamLog.d(sb.toString());
        }
        if (this.mViewFinder != null) {
            this.mViewFinder.setDisplayFlashRequired(b3);
            this.mViewFinder.setDisplayFlashColor(n, n2, n3);
        }
        ResearchUtil.getInstance().setTimeAfDone();
        this.sendEvent(TransitterEvent.EVENT_ON_AUTO_FOCUS_DONE, b2, b);
    }
    
    public void onCropRegionReady() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mActivity.runOnUiThread((Runnable)new Runnable(this) {
            final StateMachine this$0;
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke onCropRegionReady");
                }
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_HIDE_BLACK_SCREEN, new Object[0]);
            }
        });
    }
    
    public void onDeviceError(final CameraDeviceHandler.ErrorCode errorCode) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        final PowerManager powerManager = (PowerManager)this.mActivity.getSystemService("power");
        PlatformCapability.setDeviceError(true);
        if (powerManager.isScreenOn()) {
            CamLog.e("ERROR:[Screen backlight is ON.");
            this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
        }
        else {
            CamLog.e("ERROR:[Screen backlight is OFF. Force close application.]");
            this.mActivity.finishAndKillProcess();
        }
    }
    
    public void onFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (!this.mActivity.isThermalWarningReceived() && this.isStorageWritable(this.getCurrentStorage()) && !this.mCameraDeviceHandler.isObjectTrackingRunning()) {
            this.sendStaticEvent(StaticEvent.EVENT_ON_FACE_DETECTED, faceDetectionResult);
        }
    }
    
    public void onInitialAutoFocusDone(final boolean b) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke success:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        ResearchUtil.getInstance().setTimeAfDone();
        ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
        this.sendEvent(TransitterEvent.EVENT_ON_INITIAL_AUTO_FOCUS_DONE, b);
    }
    
    public void onObjectLost(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        this.sendStaticEvent(StaticEvent.EVENT_ON_OBJECT_TRACKING_LOST, objectTrackingResult);
    }
    
    public void onObjectTracked(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        this.sendStaticEvent(StaticEvent.EVENT_ON_OBJECT_TRACKED, objectTrackingResult);
    }
    
    public void onPreShutterDone(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        this.checkCallback(photoSavingRequestBuilder);
        this.sendEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, photoSavingRequestBuilder);
    }
    
    public void onPreTakePictureDone(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.checkCallback(photoSavingRequestBuilder);
        this.sendEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, photoSavingRequestBuilder);
    }
    
    public void onPrepareBurstDone(final boolean b) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke isSuccess:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        this.sendEvent(TransitterEvent.EVENT_ON_PREPARE_BURST_DONE, b);
    }
    
    public void onSceneModeChanged(final CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        this.sendStaticEvent(StaticEvent.EVENT_ON_SCENE_MODE_CHANGED, sceneRecognitionResult);
    }
    
    public void onShutterDone(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, final int i, final boolean b) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke captureNum:");
            sb.append(i);
            sb.append(", isAfSuccessed:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (i > 1) {
            this.mPredictiveCaptureStoreInfo = new PredictiveCaptureStoreInfo(i, photoSavingRequestBuilder.getSaveTimeForPredictiveCapture());
        }
        else {
            this.mPredictiveCaptureStoreInfo = null;
        }
        this.sendEvent(TransitterEvent.EVENT_ON_SHUTTER_DONE, photoSavingRequestBuilder, b);
    }
    
    public void onTakePictureDone(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.sendEvent(TransitterEvent.EVENT_ON_TAKE_PICTURE_DONE, photoSavingRequestBuilder);
    }
    
    public void onVideoRecordingDone() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mIsVideoRecording = false;
        this.sendEvent(TransitterEvent.EVENT_ON_VIDEO_RECORDING_DONE, new Object[0]);
    }
    
    public void releaseContentsViewController() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mContentsViewController != null) {
            this.mContentsViewController.clearContents();
        }
        this.mContentsViewController = null;
    }
    
    public void removeOnStateChangedListener(final OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.remove(onStateChangedListener);
    }
    
    public void sendEvent(final TransitterEvent obj, final Object... array) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke TransitterEvent:");
                sb.append(obj);
                sb.append(", current state:");
                sb.append(this.mCurrentState);
                CamLog.d(sb.toString());
            }
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[obj.ordinal()]) {
                case 79: {
                    this.mCurrentState.handleFinishZoom(array);
                    break;
                }
                case 78: {
                    this.mCurrentState.handlePerformZoom(array);
                    break;
                }
                case 77: {
                    this.mCurrentState.handlePrepareZoom(array);
                    break;
                }
                case 76: {
                    this.mCurrentState.handleChangeCapturingMode(array);
                    break;
                }
                case 75: {
                    this.mCurrentState.handleOnStorageReadyStateChanged(array);
                    break;
                }
                case 74: {
                    this.mCurrentState.handleFusionConditionChanged(array);
                    break;
                }
                case 73: {
                    this.mCurrentState.handleRequestUpdateHighSensitivityFusionMode(array);
                    break;
                }
                case 72: {
                    this.mCurrentState.handleTriggerSlowMotion(array);
                    break;
                }
                case 71: {
                    this.mCurrentState.handlePauseRecording(array);
                    break;
                }
                case 70: {
                    this.mCurrentState.handleResumeRecording(array);
                    break;
                }
                case 69: {
                    this.mCurrentState.handleStopRecording(array);
                    break;
                }
                case 68: {
                    this.mCurrentState.handleStartRecording(array);
                    break;
                }
                case 67: {
                    this.mCurrentState.handleRecordReady(array);
                    break;
                }
                case 66: {
                    this.mCurrentState.handleCaptureCancel(array);
                    break;
                }
                case 65: {
                    this.mCurrentState.handleCaptureBurst(array);
                    break;
                }
                case 64: {
                    this.mCurrentState.handleCapture(array);
                    break;
                }
                case 63: {
                    this.mCurrentState.handleStartCaptureCountDown(array);
                    break;
                }
                case 62: {
                    this.mCurrentState.handleCaptureReady(array);
                    break;
                }
                case 61: {
                    this.mCurrentState.handleChangeAngleStart(array);
                    break;
                }
                case 60: {
                    this.mCurrentState.handleHighFameRateRecordingDone(array);
                    break;
                }
                case 59: {
                    this.mCurrentState.handleSlowMotionFeedbackAnimationEnd(array);
                    break;
                }
                case 58: {
                    this.mCurrentState.handleStopRecordingSlowMotion(array);
                    break;
                }
                case 57: {
                    this.mCurrentState.handleOnCameraDeviceClosed(array);
                    break;
                }
                case 56: {
                    this.mCurrentState.handleOnCameraDeviceOpened(array);
                    break;
                }
                case 55: {
                    this.mCurrentState.handleSelfTimerCancel(array);
                    break;
                }
                case 54: {
                    this.mCurrentState.handleOnBrightnessChanged(array);
                    break;
                }
                case 53: {
                    this.mCurrentState.handleOnAmberBlueColorChanged(array);
                    break;
                }
                case 52: {
                    this.mCurrentState.handleOnSemiAutoDisabled(array);
                    break;
                }
                case 51: {
                    this.mCurrentState.handleOnSemiAutoEnabled(array);
                    break;
                }
                case 50: {
                    this.mCurrentState.handleCancelTouchedPosition(array);
                    break;
                }
                case 49: {
                    this.mCurrentState.handleSwitchCamera(array);
                    break;
                }
                case 48: {
                    this.mCurrentState.handleOnContinuousPreviewFrameUpdated(array);
                    break;
                }
                case 47: {
                    this.mCurrentState.handleOnOnePreviewFrameUpdated(array);
                    break;
                }
                case 46: {
                    this.mCurrentState.handleOnPredictiveCaptureGroupStoreCompleted(array);
                    break;
                }
                case 45: {
                    this.mCurrentState.handleOnBurstStoreCompleted(array);
                    break;
                }
                case 44: {
                    this.mCurrentState.handleOnBurstShutterDone(array);
                    break;
                }
                case 43: {
                    this.mCurrentState.handleStartAfAfterObjectTracked(array);
                    break;
                }
                case 42: {
                    this.mCurrentState.handleDeselectObjectPosition(array);
                    break;
                }
                case 41: {
                    this.mCurrentState.handleSetSelectedObjectPosition(array);
                    break;
                }
                case 40: {
                    this.mCurrentState.handleChangeSelectedFace(array);
                    break;
                }
                case 39: {
                    this.mCurrentState.handleOnRecordingError(array);
                    break;
                }
                case 38: {
                    this.mCurrentState.handleOnRecordingStartWaitDone(array);
                    break;
                }
                case 37: {
                    this.mCurrentState.handleRequestSetupHeadUpDisplay(array);
                    break;
                }
                case 36: {
                    this.mCurrentState.handleSetTouchedPosition(array);
                    break;
                }
                case 35: {
                    this.mCurrentState.handleOnStorageUngranted(array);
                    break;
                }
                case 34: {
                    this.mCurrentState.handleStorageMounted(array);
                    break;
                }
                case 33: {
                    this.mCurrentState.handleStorageError(array);
                    break;
                }
                case 32: {
                    this.mCurrentState.handleDialogClosed(array);
                    break;
                }
                case 31: {
                    this.mCurrentState.handleDialogOpened(array);
                    break;
                }
                case 30: {
                    this.mCurrentState.handleClearFocus(array);
                    break;
                }
                case 29: {
                    this.mCurrentState.handleFinishTransitionOperation(array);
                    break;
                }
                case 28: {
                    this.mCurrentState.handleStartTransitionOperation(array);
                    break;
                }
                case 27: {
                    this.mCurrentState.handleKeyMenu(array);
                    break;
                }
                case 26: {
                    this.mCurrentState.handleOnStoreCompleted(array);
                    break;
                }
                case 25: {
                    this.mCurrentState.handleOnStoreRequested(array);
                    break;
                }
                case 24: {
                    this.mCurrentState.handleTouchContentProgress();
                    break;
                }
                case 23: {
                    this.mCurrentState.handleOnVideoRecordingDone(array);
                    break;
                }
                case 22: {
                    this.mCurrentState.handleOnTakePictureDone(array);
                    break;
                }
                case 21: {
                    this.mCurrentState.handleOnPreTakePictureDone(array);
                    break;
                }
                case 20: {
                    this.mCurrentState.handleOnPrepareBurstDone(array);
                    break;
                }
                case 19: {
                    this.mCurrentState.handleOnShutterDone(array);
                    break;
                }
                case 18: {
                    this.mCurrentState.handleOnPreShutterDone(array);
                    break;
                }
                case 17: {
                    this.mCurrentState.handleOnAutoFocusDone(array);
                    break;
                }
                case 16: {
                    this.mCurrentState.handleOnInitialAutoFocusDone(array);
                    break;
                }
                case 15: {
                    this.mCurrentState.handleOnEvfPrepared(array);
                    break;
                }
                case 14: {
                    this.mCurrentState.handleOnHeatedOverNormal(array);
                    break;
                }
                case 13: {
                    this.mCurrentState.handleOnHeatedOverCritical(array);
                    break;
                }
                case 12: {
                    this.mCurrentState.handleOnReachBatteryLevelChanged((int)array[0]);
                    break;
                }
                case 11: {
                    this.mCurrentState.handleOnReachBatteryLow(new Object[0]);
                    break;
                }
                case 10: {
                    this.mCurrentState.handleOnReachBatteryLimit(new Object[0]);
                    break;
                }
                case 9: {
                    this.mCurrentState.handleOnHeatedOverCoolingUltraLow(array);
                    break;
                }
                case 8: {
                    this.mCurrentState.handleOnHeatedOverCoolingLow(array);
                    break;
                }
                case 7: {
                    this.mCurrentState.handleOnHeatedOverWarningExtra(array);
                    break;
                }
                case 6: {
                    this.mCurrentState.handleOnHeatedOverWarning(array);
                    break;
                }
                case 5: {
                    this.mCurrentState.handleFinalize(array);
                    break;
                }
                case 4: {
                    this.mCurrentState.handlePause(array);
                    break;
                }
                case 3: {
                    this.mCurrentState.handleResumeTimeout(array);
                    break;
                }
                case 2: {
                    this.mCurrentState.handleResume(array);
                    break;
                }
                case 1: {
                    this.mCurrentState.handleInitialize(array);
                    break;
                }
            }
        }
    }
    
    public void sendStaticEvent(final StaticEvent obj, final Object... array) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke StaticEvent:");
                sb.append(obj);
                sb.append(", current state:");
                sb.append(this.mCurrentState);
                CamLog.d(sb.toString());
            }
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[obj.ordinal()]) {
                case 11: {
                    this.mCurrentState.handleOnPreviewStarted();
                    break;
                }
                case 10: {
                    this.mIsSdPermissionFinished = true;
                    break;
                }
                case 9: {
                    this.mGestureShutter.handleSettingsChanged((boolean)array[0]);
                    break;
                }
                case 8: {
                    this.mCurrentState.handleOnObjectLost(array);
                    break;
                }
                case 7: {
                    this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, new Object[0]);
                    this.mCurrentState.handleOnLazyInitializationTaskRun(array);
                    break;
                }
                case 6: {
                    this.mCurrentState.handleOnOrientationChanged(array);
                    break;
                }
                case 5: {
                    this.mCurrentState.handleOnObjectTracked(array);
                    break;
                }
                case 4: {
                    this.mCurrentState.handleOnFaceDetected(array);
                    break;
                }
                case 3: {
                    this.mCurrentState.handleOnSceneModeChanged(array);
                    break;
                }
                case 2: {
                    switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[((ViewFinder.HeadUpDisplaySetupState)array[0]).ordinal()]) {
                        case 3: {
                            final VideoSize videoSize = (VideoSize)this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, (int)this.mLastVideoSavingRequest.mVideoStatus.maxDurationMills, videoSize != null && videoSize.isConstraint(), true, this.mUserSettings.get(UserSettingKey.VIDEO_HDR));
                            this.updateRecordingProgress(0);
                            this.mCameraDeviceHandler.requestOnePreviewFrame();
                            break;
                        }
                        case 1:
                        case 2: {
                            if (!this.mViewFinder.isMessageDialogOpened()) {
                                this.calculateRemainStorage();
                                break;
                            }
                            break;
                        }
                    }
                    if (this.isFusionMonitoringNeeded()) {
                        this.sendEvent(TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED, this.mCameraDeviceHandler.getLatestFusionResult());
                        break;
                    }
                    break;
                }
                case 1: {
                    this.mContentsViewController = (ContentsViewController)array[0];
                    if (this.mCurrentState.getCaptureState().isRecording()) {
                        this.mContentsViewController.disableClick();
                    }
                    this.storeSavingRequestList();
                    break;
                }
            }
        }
    }
    
    public void setDependencies(final ViewFinder mViewFinder, final CameraDeviceHandler mCameraDeviceHandler) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mViewFinder = mViewFinder;
        this.mCameraDeviceHandler = mCameraDeviceHandler;
        this.mObjectTracking = new ObjectTrackingManager(mViewFinder, mCameraDeviceHandler, this);
    }
    
    public void setGestureShutterWindowHost(final GestureShutter.WindowHost windowHost) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mGestureShutter.setWindowHost(windowHost);
    }
    
    public enum CaptureState
    {
        private static final CaptureState[] $VALUES;
        
        STATE_BURST_CAPTURE(true, false, false, false, false), 
        STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE(false, false, false, false, false), 
        STATE_CAMERA_SWITCHING(false, false, false, false, false), 
        STATE_CAPTURE_COUNTDOWN(true, false, false, false, false), 
        STATE_CROPPING(false, true, false, false, false), 
        STATE_FATAL(false, false, false, false, false), 
        STATE_FINALIZE(false, false, false, false, false), 
        STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION(true, false, false, false, true), 
        STATE_INITIALIZE(false, false, false, false, false), 
        STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION(true, false, false, false, true), 
        STATE_MODE_CHANGING(false, true, false, false, false), 
        STATE_NONE(false, false, false, false, false), 
        STATE_OPERATION_RESTRICTED(true, false, false, true, false), 
        STATE_PAUSE(false, true, false, false, false), 
        STATE_PHOTO_AF_DONE(false, false, false, false, false), 
        STATE_PHOTO_AF_SEARCH(false, false, false, false, false), 
        STATE_PHOTO_BASE(false, false, false, false, false), 
        STATE_PHOTO_CAPTURE(false, false, false, false, false), 
        STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE(false, false, false, false, false), 
        STATE_PHOTO_READY(true, true, true, true, false), 
        STATE_PHOTO_READY_FOR_RECORDING(false, false, false, false, false), 
        STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START(false, false, false, false, false), 
        STATE_PREPARE_FOR_RECORDING(false, false, false, false, false), 
        STATE_RESUME(false, false, false, false, false), 
        STATE_VIDEO_CAPTURE_WHILE_RECORDING(false, false, false, false, true), 
        STATE_VIDEO_READY(true, true, false, true, false), 
        STATE_VIDEO_RECORDING(true, false, false, false, true), 
        STATE_VIDEO_RECORDING_PAUSING(true, false, false, false, true), 
        STATE_VIDEO_STOPPING(true, false, false, false, true), 
        STATE_VIDEO_STORE(false, false, false, false, true), 
        STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING(false, false, false, false, false), 
        STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE(false, false, false, false, false), 
        STATE_WAITING_PRE_PROCESS_DONE(false, false, false, false, false), 
        STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE(true, false, false, false, true), 
        STATE_WARNING(true, true, false, true, false);
        
        private final boolean mCanApplicationBeFinished;
        private final boolean mCanHandleAsynchronizedTask;
        private final boolean mCanHandleWearableCaptureRequest;
        private final boolean mIsMenuAvailable;
        private final boolean mIsRecordingState;
        
        static {
            $VALUES = new CaptureState[] { CaptureState.STATE_NONE, CaptureState.STATE_INITIALIZE, CaptureState.STATE_RESUME, CaptureState.STATE_CAMERA_SWITCHING, CaptureState.STATE_PHOTO_BASE, CaptureState.STATE_PHOTO_READY, CaptureState.STATE_PHOTO_READY_FOR_RECORDING, CaptureState.STATE_PREPARE_FOR_RECORDING, CaptureState.STATE_CAPTURE_COUNTDOWN, CaptureState.STATE_OPERATION_RESTRICTED, CaptureState.STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START, CaptureState.STATE_PHOTO_AF_SEARCH, CaptureState.STATE_PHOTO_AF_DONE, CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE, CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE, CaptureState.STATE_PHOTO_CAPTURE, CaptureState.STATE_BURST_CAPTURE, CaptureState.STATE_VIDEO_RECORDING, CaptureState.STATE_VIDEO_CAPTURE_WHILE_RECORDING, CaptureState.STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING, CaptureState.STATE_VIDEO_STORE, CaptureState.STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION, CaptureState.STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION, CaptureState.STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE, CaptureState.STATE_VIDEO_STOPPING, CaptureState.STATE_PAUSE, CaptureState.STATE_WARNING, CaptureState.STATE_FINALIZE, CaptureState.STATE_VIDEO_RECORDING_PAUSING, CaptureState.STATE_VIDEO_READY, CaptureState.STATE_MODE_CHANGING, CaptureState.STATE_WAITING_PRE_PROCESS_DONE, CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE, CaptureState.STATE_CROPPING, CaptureState.STATE_FATAL };
        }
        
        private CaptureState(final boolean mCanHandleAsynchronizedTask, final boolean mCanApplicationBeFinished, final boolean mCanHandleWearableCaptureRequest, final boolean mIsMenuAvailable, final boolean mIsRecordingState) {
            this.mCanHandleAsynchronizedTask = mCanHandleAsynchronizedTask;
            this.mCanApplicationBeFinished = mCanApplicationBeFinished;
            this.mCanHandleWearableCaptureRequest = mCanHandleWearableCaptureRequest;
            this.mIsMenuAvailable = mIsMenuAvailable;
            this.mIsRecordingState = mIsRecordingState;
        }
        
        private boolean canApplicationBeFinished() {
            return this.mCanApplicationBeFinished;
        }
        
        private boolean canHandleAsynchronizedTask() {
            return this.mCanHandleAsynchronizedTask;
        }
        
        private boolean canHandleWearableCaptureRequest() {
            synchronized (this) {
                return this.mCanHandleWearableCaptureRequest;
            }
        }
        
        private boolean isMenuAvailable() {
            return this.mIsMenuAvailable;
        }
        
        private boolean isRecording() {
            return this.mIsRecordingState;
        }
    }
    
    private class ChangeCameraModeTask implements Runnable
    {
        private final CapturingMode mRequestMode;
        private final AnimationRequest.AnimationType mTriggerType;
        final StateMachine this$0;
        
        private ChangeCameraModeTask(final StateMachine this$0, final CapturingMode mRequestMode, final AnimationRequest.AnimationType mTriggerType) {
            this.this$0 = this$0;
            this.mRequestMode = mRequestMode;
            this.mTriggerType = mTriggerType;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke ChangeCameraModeTask");
            }
            if (this.this$0.mCameraDeviceHandler != null && this.this$0.mViewFinder != null) {
                this.this$0.mCameraDeviceHandler.releaseRecorder();
                this.this$0.mCameraDeviceHandler.stopFaceDetection();
                this.this$0.mCameraDeviceHandler.stopPreviewSynchronized();
                this.this$0.mViewFinder.hideSurface();
                PerfLog.MODE_CHANGE_TASK_START.transit();
                this.this$0.mUserSettings.applyCapturingMode();
                this.this$0.doStopObjectTracking();
                this.this$0.requestResizeEvf((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE), false);
                this.this$0.mViewFinder.showSurface();
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED, this.mRequestMode, true, this.mTriggerType);
                if (this.this$0.isFusionMonitoringNeeded()) {
                    this.this$0.mCameraDeviceHandler.startFusionMonitoring();
                }
                PerfLog.MODE_CHANGE_TASK_END.transit();
            }
        }
    }
    
    private enum NextCaptureCondition
    {
        private static final NextCaptureCondition[] $VALUES;
        
        READY, 
        REQUESTED, 
        UNACCEPTABLE;
        
        static {
            $VALUES = new NextCaptureCondition[] { NextCaptureCondition.READY, NextCaptureCondition.REQUESTED, NextCaptureCondition.UNACCEPTABLE };
        }
    }
    
    private class NotifyDelayedEventTask implements Runnable
    {
        private final Object[] mArgs;
        private final TransitterEvent mEvent;
        final StateMachine this$0;
        
        private NotifyDelayedEventTask(final StateMachine this$0, final TransitterEvent mEvent, final Object[] mArgs) {
            this.this$0 = this$0;
            this.mEvent = mEvent;
            this.mArgs = mArgs;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke NotifyDelayedEventTask");
            }
            this.this$0.sendEvent(this.mEvent, this.mArgs);
        }
    }
    
    private class OnPreviewStartedListenerImpl implements OnPreviewStartedListener
    {
        final StateMachine this$0;
        
        private OnPreviewStartedListenerImpl(final StateMachine this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPreviewStarted(final CameraSessionId cameraSessionId) {
            this.this$0.mActivity.runOnUiThread((Runnable)new Runnable(this) {
                final OnPreviewStartedListenerImpl this$1;
                
                @Override
                public void run() {
                    this.this$1.this$0.sendStaticEvent(StaticEvent.EVENT_ON_PREVIEW_STARTED, new Object[0]);
                }
            });
        }
    }
    
    public interface OnStateChangedListener
    {
        void onStateChanged(final CaptureState p0, final Object... p1);
    }
    
    public static class OneShotResult
    {
        public final Bitmap bitmap;
        public final int code;
        public final boolean isSuccess;
        public final SavingRequest savingRequest;
        public final Uri uri;
        
        private OneShotResult(final Uri uri, final MediaSavingResult mediaSavingResult, final SavingRequest savingRequest, final Bitmap bitmap) {
            this.uri = uri;
            this.code = mediaSavingResult.mResultCode;
            this.isSuccess = (mediaSavingResult == MediaSavingResult.SUCCESS);
            this.savingRequest = savingRequest;
            this.bitmap = bitmap;
        }
    }
    
    private class RequestStoreTask implements Runnable
    {
        private final RequestFactory.PhotoSavingRequestBuilder mRequest;
        final StateMachine this$0;
        
        private RequestStoreTask(final StateMachine this$0, final RequestFactory.PhotoSavingRequestBuilder mRequest) {
            this.this$0 = this$0;
            this.mRequest = mRequest;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke RequestStoreTask");
            }
            synchronized (this.this$0) {
                if (this.this$0.isNeedRepairRequestId(this.mRequest) && this.this$0.mContentsViewController != null) {
                    this.this$0.mActivity.runOnUiThread((Runnable)new Runnable(this) {
                        final RequestStoreTask this$1;
                        
                        @Override
                        public void run() {
                            this.this$1.this$0.requestStorePicture(this.this$1.mRequest);
                        }
                    });
                    return;
                }
                if (this.this$0.mContentsViewController == null && this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_PAUSE && this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_FINALIZE) {
                    if (this.mRequest.getShouldUpdateOrientationBeforeStoring()) {
                        this.mRequest.mCommonStatus.orientation = this.this$0.getOrientation();
                    }
                    if (this.this$0.mActivity.isDeviceInSecurityLock()) {
                        if (!this.mRequest.mCommonStatus.takenByFastCapture && !this.this$0.isNeedRepairRequestId(this.mRequest)) {
                            this.this$0.storePicture(this.mRequest);
                        }
                        else {
                            this.this$0.mPhotoSavingRequestList.add(this.mRequest);
                        }
                    }
                    else {
                        ((RequestFactory.RequestBuilder)this.mRequest).setRequestId(-1);
                        this.this$0.storePicture(this.mRequest);
                    }
                }
                else {
                    this.this$0.storePicture(this.mRequest);
                }
                monitorexit(this.this$0);
                this.this$0.mHandler.post((Runnable)new Runnable(this) {
                    final RequestStoreTask this$1;
                    
                    @Override
                    public void run() {
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
                    }
                });
            }
        }
    }
    
    private class SettingsController implements UserSettingApplicable
    {
        final StateMachine this$0;
        
        private SettingsController(final StateMachine this$0) {
            this.this$0 = this$0;
        }
        
        private boolean isSceneRecognitionValid(final CapturingMode capturingMode, final VideoSize videoSize, final VideoHdr videoHdr) {
            final int n = StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
            boolean b = true;
            switch (n) {
                case 7:
                case 8: {
                    if (VideoSize.FULL_HD == videoSize && videoHdr != VideoHdr.HDR_ON) {
                        b = b;
                        return b;
                    }
                    break;
                }
                case 5:
                case 6: {
                    return b;
                }
            }
            b = false;
            return b;
        }
        
        private void notifySettingChanged(final UserSettingValue userSettingValue) {
            synchronized (this.this$0) {
                if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked() && this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_MODE_CHANGING && this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_CAMERA_SWITCHING && this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE) {
                    this.this$0.mViewFinder.onSettingChanged(userSettingValue);
                }
            }
        }
        
        private void resetZoom() {
            this.this$0.mCameraDeviceHandler.setZoom(0.0f);
            this.this$0.onZoomChange(0);
        }
        
        @Override
        public void commit() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke");
            }
            this.this$0.mCameraDeviceHandler.commit();
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.commit();
            }
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters();
            if (parameters != null) {
                final CameraInfo.CameraId cameraId = ((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getCameraId();
                final EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher((Context)this.this$0.mActivity, cameraId);
                eachCameraStatusPublisher.putFromParameter(parameters, cameraId, this.this$0.getCurrentCapturingMode().isVideo());
                if (this.this$0.getCurrentCapturingMode().isVideo()) {
                    com.sonyericsson.cameracommon.status.eachcamera.PhotoLight.Value value;
                    if (this.this$0.mUserSettings.get(UserSettingKey.PHOTO_LIGHT).equals(PhotoLight.ON)) {
                        value = com.sonyericsson.cameracommon.status.eachcamera.PhotoLight.Value.ON;
                    }
                    else {
                        value = com.sonyericsson.cameracommon.status.eachcamera.PhotoLight.Value.OFF;
                    }
                    ((CameraStatusPublisher<com.sonyericsson.cameracommon.status.eachcamera.PhotoLight>)eachCameraStatusPublisher).put(new com.sonyericsson.cameracommon.status.eachcamera.PhotoLight(value));
                }
                if (this.this$0.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                    switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                        case 2:
                        case 3: {
                            final VideoSize videoSize = (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                            ((CameraStatusPublisher<com.sonyericsson.cameracommon.status.eachcamera.PhotoLight>)eachCameraStatusPublisher).put(new VideoResolution(videoSize.getVideoRect())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new VideoRecordingFps(RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF))).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)VideoStabilizerStatus.fromCameraParameter(this.this$0.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(com.sonyericsson.cameracommon.status.eachcamera.SlowMotion.Value.ON)).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new Hdr(Hdr.fromCameraParameter(VideoHdr.HDR_OFF))).publish();
                            break;
                        }
                        case 1: {
                            ((CameraStatusPublisher<com.sonyericsson.cameracommon.status.eachcamera.PhotoLight>)eachCameraStatusPublisher).put(new VideoResolution(((SlowMotion)this.this$0.mUserSettings.get(UserSettingKey.SLOW_MOTION)).getVideoSize().getVideoRect())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new VideoRecordingFps(RecordingProfile.getVideoFrameRate(((SlowMotion)this.this$0.mUserSettings.get(UserSettingKey.SLOW_MOTION)).getVideoSize(), VideoHdr.HDR_OFF))).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)VideoStabilizerStatus.fromCameraParameter(this.this$0.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(com.sonyericsson.cameracommon.status.eachcamera.SlowMotion.Value.OFF)).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new Hdr(Hdr.fromCameraParameter(VideoHdr.HDR_OFF))).publish();
                            break;
                        }
                    }
                }
                else {
                    ((CameraStatusPublisher<com.sonyericsson.cameracommon.status.eachcamera.PhotoLight>)eachCameraStatusPublisher).put(new VideoResolution(((VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new VideoRecordingFps(RecordingProfile.getVideoFrameRate((VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR)))).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)VideoStabilizerStatus.fromCameraParameter(this.this$0.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(com.sonyericsson.cameracommon.status.eachcamera.SlowMotion.Value.OFF)).put((com.sonyericsson.cameracommon.status.eachcamera.PhotoLight)new Hdr(Hdr.fromCameraParameter((VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR)))).publish();
                }
            }
        }
        
        @Override
        public void set(final AspectRatio aspectRatio) {
            this.notifySettingChanged(aspectRatio);
        }
        
        @Override
        public void set(final AutoReview autoReview) {
        }
        
        @Override
        public void set(final CameraKey cameraKey) {
        }
        
        @Override
        public void set(final CapturingMode capturingMode) {
            final VideoSize videoSize = (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
            this.this$0.setIsSceneRecognitionValid(this.isSceneRecognitionValid(capturingMode, videoSize, (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR)));
            final Float zoom = this.this$0.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom) {
                this.this$0.showBlackScreen();
            }
            this.this$0.mCameraDeviceHandler.setCapturingMode(capturingMode);
            this.this$0.mCameraDeviceHandler.setVideoSize(videoSize);
        }
        
        @Override
        public void set(final DestinationToSave destinationToSave) {
            this.this$0.mGestureShutter.setEnabled(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
        }
        
        @Override
        public void set(final DisplayFlash displayFlashMode) {
            this.this$0.mCameraDeviceHandler.setDisplayFlashMode(displayFlashMode);
            this.notifySettingChanged(displayFlashMode);
        }
        
        @Override
        public void set(final DistortionCorrection distortionCorrection) {
            this.this$0.mCameraDeviceHandler.setDistortionCorrection(distortionCorrection);
        }
        
        @Override
        public void set(final Ev ev) {
            this.this$0.mCameraDeviceHandler.setEv(ev);
        }
        
        @Override
        public void set(final Facing facing) {
        }
        
        @Override
        public void set(final FastCapture fastCapture) {
        }
        
        @Override
        public void set(final Flash flashMode) {
            this.this$0.mCameraDeviceHandler.setFlashMode(flashMode);
            this.notifySettingChanged(flashMode);
        }
        
        @Override
        public void set(final FocusMode focusMode) {
            this.this$0.mCameraDeviceHandler.setFocusMode(focusMode);
        }
        
        @Override
        public void set(final FocusRange focusRange) {
            this.this$0.mCameraDeviceHandler.setFocusRange(focusRange);
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.updateFocusIconType(focusRange != FocusRange.AF);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            }
            if (this.this$0.isTouchAeEnabled()) {
                this.this$0.mCameraDeviceHandler.setMetering((Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
            }
            if (focusRange != FocusRange.AF) {
                this.this$0.mObjectTracking.stop();
            }
        }
        
        @Override
        public void set(final FrontAngle obj) {
            if (this.this$0.mCurrentState.getCaptureState() != CaptureState.STATE_CROPPING && this.this$0.getCurrentCapturingMode().isFront()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("set(FrontAngle) value: ");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
                float zoom = 0.0f;
                if (obj == FrontAngle.CROPPED) {
                    zoom = (float)((PlatformCapability.getWideZoomTargetRatio(CameraInfo.CameraId.FRONT) - 1.0) / (PlatformCapability.getMaxZoomRatio(CameraInfo.CameraId.FRONT) - 1.0));
                }
                this.this$0.mCameraDeviceHandler.setZoom(zoom);
            }
        }
        
        @Override
        public void set(final FusionMode fusionMode) {
            if (DependencyCheckUtil.isFusionAvailableOnVideo(((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getCameraId(), (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR))) {
                this.this$0.mCameraDeviceHandler.setFusionMode(fusionMode);
            }
            else {
                this.this$0.mCameraDeviceHandler.setFusionMode(FusionMode.OFF);
            }
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, fusionMode);
            }
        }
        
        @Override
        public void set(final Geotag geotag) {
        }
        
        @Override
        public void set(final GridLine gridLine) {
        }
        
        @Override
        public void set(final com.sonyericsson.android.camera.configuration.parameters.Hdr hdr) {
            this.this$0.mCameraDeviceHandler.setHdr(hdr);
            this.notifySettingChanged(hdr);
        }
        
        @Override
        public void set(final HelpGuide helpGuide) {
        }
        
        @Override
        public void set(final Iso iso) {
            this.this$0.mCameraDeviceHandler.setIso(iso);
        }
        
        @Override
        public void set(final Metering metering) {
            this.this$0.mCameraDeviceHandler.setMetering(metering);
        }
        
        @Override
        public void set(final ObjectTracking objectTracking) {
            if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.set(FocusMode.getDefaultValue((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)));
            }
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mObjectTracking.stop();
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            }
        }
        
        @Override
        public void set(final PhotoLight photoLight) {
            final CameraDeviceHandler access$2600 = this.this$0.mCameraDeviceHandler;
            Flash flashMode;
            if (photoLight.getBooleanValue()) {
                flashMode = Flash.LED_ON;
            }
            else {
                flashMode = Flash.LED_OFF;
            }
            access$2600.setFlashMode(flashMode);
            this.notifySettingChanged(photoLight);
        }
        
        @Override
        public void set(final PredictiveCapture predictiveCapture) {
        }
        
        @Override
        public void set(final PredictiveLaunch predictiveLaunch) {
        }
        
        @Override
        public void set(final ResetSettings resetSettings) {
        }
        
        @Override
        public void set(final Resolution resolution) {
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            final Float zoom = this.this$0.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom) {
                this.this$0.showBlackScreen();
            }
            this.this$0.mCameraDeviceHandler.setResolution(capturingMode.getCameraId(), resolution);
            if (!capturingMode.isFront()) {
                this.resetZoom();
            }
            this.this$0.requestResizeEvf(capturingMode, true);
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                this.this$0.mObjectTracking.stop();
            }
        }
        
        @Override
        public void set(final SelfTimer selfTimer) {
            this.this$0.updatePhotoSelftimer(selfTimer);
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SELF_TIMER_CONDITION, selfTimer);
            }
            this.notifySettingChanged(selfTimer);
        }
        
        @Override
        public void set(final ShutterSound shutterSound) {
        }
        
        @Override
        public void set(final ShutterSpeed shutterSpeed) {
            this.this$0.mCameraDeviceHandler.setShutterSpeed(shutterSpeed);
        }
        
        @Override
        public void set(final ShutterTrigger shutterTrigger) {
            if (((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getType() == 1) {
                if (this.this$0.mViewFinder == null) {
                    return;
                }
                this.this$0.mCameraDeviceHandler.setShutterTrigger(shutterTrigger);
            }
            this.this$0.mGestureShutter.handleSettingsChanged(shutterTrigger.isGestureShutterOn());
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.setupFocusRectangles();
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            }
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.setShutterTrigger(shutterTrigger);
            }
        }
        
        @Override
        public void set(final SideSense sideSense) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.this$0.getCurrentCapturingMode().ordinal()]) {
                default: {
                    if (sideSense == SideSense.ON) {
                        this.this$0.mActivity.enableSideSense();
                    }
                    else {
                        this.this$0.mActivity.disableSideSense();
                    }
                }
                case 1:
                case 2:
                case 3:
                case 4: {}
            }
        }
        
        @Override
        public void set(final SlowMotion slowMotion) {
            this.this$0.mCameraDeviceHandler.releaseRecorder();
            this.this$0.mCameraDeviceHandler.setSlowMotion(slowMotion);
            if (slowMotion != SlowMotion.OFF) {
                final CapturingMode capturingMode = (CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
                VideoSize videoSize;
                if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                    videoSize = slowMotion.getVideoSize();
                }
                else {
                    videoSize = (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                }
                this.this$0.setIsSceneRecognitionValid(this.isSceneRecognitionValid(capturingMode, videoSize, (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR)));
                this.this$0.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, VideoHdr.HDR_OFF);
                this.this$0.mCameraDeviceHandler.setVideoSize(videoSize);
                if (!capturingMode.isFront()) {
                    this.resetZoom();
                }
            }
            this.this$0.mViewFinder.updateSlowMotionView(slowMotion);
        }
        
        @Override
        public void set(final SoftSkin softSkin) {
            this.this$0.mCameraDeviceHandler.setSoftSkin(softSkin);
        }
        
        @Override
        public void set(final TouchCapture touchCapture) {
        }
        
        @Override
        public void set(final TouchIntention touchIntention) {
            this.this$0.mCameraDeviceHandler.setMetering((Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked() && this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) != FocusRange.AF && touchIntention != TouchIntention.FOCUS_AND_EXPOSURE) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            }
        }
        
        @Override
        public void set(final VideoCodec videoCodec) {
        }
        
        @Override
        public void set(final VideoHdr videoHdr) {
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            if (this.this$0.getCurrentCapturingMode().isFront() && this.this$0.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                this.this$0.showBlackScreen();
            }
            final VideoSize videoSize = (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
            if (videoSize == VideoSize.FOUR_K_UHD_H264) {
                if (videoHdr == VideoHdr.HDR_ON) {
                    this.this$0.mCameraDeviceHandler.setVideoSize(VideoSize.FOUR_K_UHD_H265);
                }
                else if (videoHdr == VideoHdr.HDR_OFF) {
                    this.this$0.mCameraDeviceHandler.setVideoSize(videoSize);
                }
            }
            this.this$0.mCameraDeviceHandler.setVideoHdr(videoHdr);
            this.notifySettingChanged(videoHdr);
            FusionMode off = (FusionMode)this.this$0.mUserSettings.get(UserSettingKey.FUSION_MODE);
            if (!DependencyCheckUtil.isFusionAvailableOnVideo(capturingMode.getCameraId(), videoSize, videoHdr)) {
                off = FusionMode.OFF;
            }
            this.this$0.mCameraDeviceHandler.setFusionMode(off);
            final VideoHdr videoHdr2 = (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR);
            this.this$0.setIsSceneRecognitionValid(videoHdr != VideoHdr.HDR_ON && this.isSceneRecognitionValid(capturingMode, videoSize, videoHdr2));
            this.this$0.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, videoHdr);
            if (!capturingMode.isFront()) {
                this.resetZoom();
            }
            this.this$0.requestResizeEvf(capturingMode, true);
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, videoHdr, true);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
                this.this$0.switchVideoFaceDetection();
                if (!this.this$0.mIsSemiAutoEnabled) {
                    this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                }
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, off);
                this.this$0.mObjectTracking.stop();
            }
        }
        
        @Override
        public void set(final VideoShutterTrigger videoShutterTrigger) {
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.updateVideoShutterTrigger();
            }
        }
        
        @Override
        public void set(final VideoSize videoSize) {
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            final VideoHdr videoHdr = (VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR);
            if (capturingMode == CapturingMode.SLOW_MOTION) {
                this.this$0.mCameraDeviceHandler.releaseRecorder();
            }
            this.this$0.setIsSceneRecognitionValid(this.isSceneRecognitionValid(capturingMode, videoSize, videoHdr));
            this.this$0.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, videoHdr);
            final Float zoom = this.this$0.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom) {
                this.this$0.showBlackScreen();
            }
            if (!capturingMode.isFront()) {
                this.resetZoom();
            }
            this.this$0.mCameraDeviceHandler.setVideoSize(videoSize);
            this.this$0.mCameraDeviceHandler.setZoom(0.0f);
            FusionMode off = (FusionMode)this.this$0.mUserSettings.get(UserSettingKey.FUSION_MODE);
            if (!DependencyCheckUtil.isFusionAvailableOnVideo(capturingMode.getCameraId(), videoSize, videoHdr)) {
                off = FusionMode.OFF;
            }
            this.this$0.mCameraDeviceHandler.setFusionMode(off);
            this.this$0.requestResizeEvf(capturingMode, true);
            if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, videoHdr, false);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, off);
                this.this$0.mObjectTracking.stop();
            }
        }
        
        @Override
        public void set(final VideoStabilizer videoStabilizer) {
            if (((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getType() == 2) {
                VideoStabilizer videoStabilizer2 = videoStabilizer;
                if (this.this$0.mActivity.isOneShotVideo()) {
                    videoStabilizer2 = videoStabilizer;
                    if (this.this$0.mActivity.getExtraOutput() != null) {
                        videoStabilizer2 = videoStabilizer;
                        if (!this.this$0.mActivity.getExtraOutput().getScheme().equalsIgnoreCase("file")) {
                            if (this.this$0.mCameraDeviceHandler.isSteadyShotSupported()) {
                                videoStabilizer2 = VideoStabilizer.STEADY_SHOT;
                            }
                            else {
                                videoStabilizer2 = VideoStabilizer.OFF;
                            }
                        }
                    }
                }
                this.this$0.mCameraDeviceHandler.setVideoStabilizer(videoStabilizer2);
            }
        }
        
        @Override
        public void set(final VolumeKey volumeKey) {
        }
        
        @Override
        public void set(final WhiteBalance whiteBalance) {
            this.this$0.mCameraDeviceHandler.setWhiteBalance(whiteBalance);
        }
    }
    
    private class StartRecordingTask implements Runnable
    {
        private final boolean mBySideSense;
        final StateMachine this$0;
        
        public StartRecordingTask(final StateMachine this$0, final boolean mBySideSense) {
            this.this$0 = this$0;
            this.mBySideSense = mBySideSense;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StartRecordingTask");
            }
            this.this$0.doStartRecording(this.mBySideSense);
        }
    }
    
    public enum StartupAction
    {
        private static final StartupAction[] $VALUES;
        
        CAPTURE, 
        NONE, 
        RECORD;
        
        static {
            $VALUES = new StartupAction[] { StartupAction.NONE, StartupAction.CAPTURE, StartupAction.RECORD };
        }
    }
    
    class State
    {
        protected CaptureState mCaptureState;
        final StateMachine this$0;
        
        private State(final StateMachine this$0) {
            this.this$0 = this$0;
            this.mCaptureState = CaptureState.STATE_NONE;
        }
        
        private boolean isLastStoreDataResult(final StoreDataResult storeDataResult) {
            final boolean takenByFastCapture = storeDataResult.savingRequest.common.takenByFastCapture;
            final boolean b = true;
            final boolean b2 = true;
            boolean b3 = true;
            final boolean b4 = true;
            if (takenByFastCapture) {
                return true;
            }
            if (this.this$0.mLastPhotoSavingRequest == null && this.this$0.mLastVideoSavingRequest == null) {
                CamLog.w("Last saving request is not exist.");
                return true;
            }
            final int requestId = storeDataResult.savingRequest.getRequestId();
            if (this.this$0.mLastPhotoSavingRequest == null) {
                return ((RequestFactory.RequestBuilder)this.this$0.mLastVideoSavingRequest).getRequestId() == requestId && b4;
            }
            if (this.this$0.mLastVideoSavingRequest == null) {
                return ((RequestFactory.RequestBuilder)this.this$0.mLastPhotoSavingRequest).getRequestId() == requestId && b;
            }
            if (((RequestFactory.RequestBuilder)this.this$0.mLastPhotoSavingRequest).getRequestId() > ((RequestFactory.RequestBuilder)this.this$0.mLastVideoSavingRequest).getRequestId()) {
                return ((RequestFactory.RequestBuilder)this.this$0.mLastPhotoSavingRequest).getRequestId() == requestId && b2;
            }
            if (((RequestFactory.RequestBuilder)this.this$0.mLastVideoSavingRequest).getRequestId() != requestId) {
                b3 = false;
            }
            return b3;
        }
        
        public void entry() {
        }
        
        public void exit() {
        }
        
        public CaptureState getCaptureState() {
            return this.mCaptureState;
        }
        
        public void handleCancelTouchedPosition(final Object... array) {
            this.this$0.mCameraDeviceHandler.resetFocusAreaAndRect((FocusMode)this.this$0.getUserSetting().get(UserSettingKey.FOCUS_MODE));
            this.this$0.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
        }
        
        public void handleCapture(final Object... array) {
        }
        
        public void handleCaptureBurst(final Object... array) {
        }
        
        public void handleCaptureCancel(final Object... array) {
        }
        
        public void handleCaptureReady(final Object... array) {
        }
        
        public void handleChangeAngleStart(final Object... array) {
        }
        
        public void handleChangeCapturingMode(final Object... array) {
        }
        
        public void handleChangeSelectedFace(final Object... array) {
        }
        
        public void handleClearFocus(final Object... array) {
        }
        
        public void handleDeselectObjectPosition(final Object... array) {
        }
        
        public void handleDialogClosed(final Object... array) {
        }
        
        public void handleDialogOpened(final Object... array) {
        }
        
        public void handleFinalize(final Object... array) {
        }
        
        public void handleFinishTransitionOperation(final Object... array) {
        }
        
        public void handleFinishZoom(final Object... array) {
        }
        
        public void handleFusionConditionChanged(final Object... array) {
        }
        
        public void handleHighFameRateRecordingDone(final Object... array) {
        }
        
        public void handleInitialize(final Object... array) {
        }
        
        public void handleKeyMenu(final Object... array) {
        }
        
        public void handleOnAmberBlueColorChanged(final Object... array) {
        }
        
        public void handleOnAutoFocusDone(final Object... array) {
        }
        
        public void handleOnBrightnessChanged(final Object... array) {
        }
        
        public void handleOnBurstShutterDone(final Object... array) {
        }
        
        public void handleOnBurstStoreCompleted(final Object... array) {
        }
        
        public void handleOnCameraDeviceClosed(final Object... array) {
        }
        
        public void handleOnCameraDeviceOpened(final Object... array) {
        }
        
        public void handleOnContinuousPreviewFrameUpdated(final Object... array) {
        }
        
        public void handleOnEvfPrepared(final Object... array) {
        }
        
        public void handleOnFaceDetected(final Object... array) {
        }
        
        public void handleOnHeatedOverCoolingLow(final Object... array) {
        }
        
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
        }
        
        public void handleOnHeatedOverCritical(final Object... array) {
            final boolean booleanValue = (boolean)array[0];
            this.this$0.changeTo((State)new StateFatal(false, booleanValue), new Object[0]);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_CRITICAL, booleanValue);
        }
        
        public void handleOnHeatedOverNormal(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_NORMAL, new Object[0]);
        }
        
        public void handleOnHeatedOverWarning(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_WARNING, new Object[0]);
        }
        
        public void handleOnHeatedOverWarningExtra(final Object... array) {
        }
        
        public void handleOnInitialAutoFocusDone(final Object... array) {
            CamLog.e("ERROR:PRE-SCAN Event is not handled correctly. Check sequence.");
            this.this$0.cancelAutoFocus(false);
        }
        
        public void handleOnLazyInitializationTaskRun(final Object... array) {
        }
        
        public void handleOnObjectLost(final Object... array) {
            this.this$0.mViewFinder.onObjectLost();
        }
        
        public void handleOnObjectTracked(final Object... array) {
        }
        
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
        }
        
        public void handleOnOrientationChanged(final Object... array) {
        }
        
        public void handleOnPreShutterDone(final Object... array) {
        }
        
        public void handleOnPreTakePictureDone(final Object... array) {
        }
        
        public void handleOnPredictiveCaptureGroupStoreCompleted(final Object... array) {
            this.this$0.onPredictiveCaptureStoreComplete((StoreDataResult)array[0]);
        }
        
        public void handleOnPrepareBurstDone(final Object... array) {
        }
        
        public void handleOnPreviewStarted() {
        }
        
        public void handleOnReachBatteryLevelChanged(final Object... array) {
            this.this$0.mViewFinder.updateBatteryIndicator((int)array[0]);
        }
        
        public void handleOnReachBatteryLimit(final Object... array) {
            this.this$0.changeTo((State)new StateFatal(false, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, false);
        }
        
        public void handleOnReachBatteryLow(final Object... array) {
            this.this$0.mViewFinder.showMessageDialog(DialogId.LOW_BATTERY_WARNING, new Object[0]);
        }
        
        public void handleOnRecordingError(final Object... array) {
        }
        
        public void handleOnRecordingStartWaitDone(final Object... array) {
        }
        
        public void handleOnSceneModeChanged(final Object... array) {
        }
        
        public void handleOnSemiAutoDisabled(final Object... array) {
        }
        
        public void handleOnSemiAutoEnabled(final Object... array) {
        }
        
        public void handleOnShutterDone(final Object... array) {
        }
        
        public void handleOnStorageReadyStateChanged(final Object... array) {
        }
        
        public void handleOnStorageUngranted(final Object... array) {
            if (CamLog.DEBUG) {
                throw new IllegalStateException("Since processing is not executed in the current state, confirmation is necessary");
            }
        }
        
        public void handleOnStoreCompleted(final Object... array) {
            this.this$0.mLastStoreDataResult = (StoreDataResult)array[0];
            this.this$0.mPredictiveApplier.leaveSuppressor(this.this$0.mLastStoreDataResult.savingRequest);
            if (this.this$0.mLastStoreDataResult.savingRequest.getFilePath() != null && PredictiveCapturePathBuilder.isPredictiveCaptureLastImage(this.this$0.mLastStoreDataResult.savingRequest.getFilePath())) {
                this.this$0.sendEvent(TransitterEvent.EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED, this.this$0.mLastStoreDataResult);
            }
            if (this.this$0.mActivity.getWearableBridge() != null) {
                this.this$0.mActivity.getWearableBridge().getPhotoStateNotifier().onCaptureSucceeded();
            }
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_STORE_COMPLETED, this.this$0.mLastStoreDataResult, this.isLastStoreDataResult(this.this$0.mLastStoreDataResult));
            }
        }
        
        public void handleOnStoreRequested(final Object... array) {
        }
        
        public void handleOnTakePictureDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke id:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.requestStorePicture(photoSavingRequestBuilder);
        }
        
        public void handleOnVideoRecordingDone(final Object... array) {
        }
        
        public void handlePause(final Object... array) {
        }
        
        public void handlePauseRecording(final Object... array) {
        }
        
        public void handlePerformZoom(final Object... array) {
        }
        
        public void handlePrepareZoom(final Object... array) {
        }
        
        public void handleRecordReady(final Object... array) {
        }
        
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            this.this$0.mHandler.postDelayed((Runnable)new ReTrySetupHeadUpDisplayTask((boolean)array[0]), 100L);
        }
        
        public void handleRequestUpdateHighSensitivityFusionMode(final Object... array) {
        }
        
        public void handleResume(final Object... array) {
            if (this.this$0.mCameraDeviceHandler.isCameraDisabled()) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
            }
        }
        
        public void handleResumeRecording(final Object... array) {
        }
        
        public void handleResumeTimeout(final Object... array) {
        }
        
        public void handleSelfTimerCancel(final Object... array) {
        }
        
        public void handleSetSelectedObjectPosition(final Object... array) {
        }
        
        public void handleSetTouchedPosition(final Object... array) {
        }
        
        public void handleSlowMotionFeedbackAnimationEnd(final Object... array) {
        }
        
        public void handleStartAfAfterObjectTracked(final Object... array) {
        }
        
        public void handleStartCaptureCountDown(final Object... array) {
        }
        
        public void handleStartRecording(final Object... array) {
        }
        
        public void handleStartTransitionOperation(final Object... array) {
        }
        
        public void handleStopRecording(final Object... array) {
        }
        
        public void handleStopRecordingSlowMotion(final Object... array) {
        }
        
        public void handleStorageError(final Object... array) {
            final Storage.StorageType obj = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : type = ");
                sb.append(obj);
                sb.append(", state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                this.this$0.changeTo((State)new StateFatal(false, false), new Object[0]);
            }
        }
        
        public void handleStorageMounted(final Object... array) {
        }
        
        public void handleSwitchCamera(final Object... array) {
        }
        
        public void handleTouchContentProgress() {
        }
        
        public void handleTriggerSlowMotion(final Object... array) {
        }
        
        @Override
        public String toString() {
            if (this.mCaptureState == null) {
                return CaptureState.STATE_NONE.toString();
            }
            return this.mCaptureState.toString();
        }
        
        class ReTrySetupHeadUpDisplayTask implements Runnable
        {
            private final boolean mApplySettingsForFastCapture;
            final State this$1;
            
            public ReTrySetupHeadUpDisplayTask(final State this$1, final boolean mApplySettingsForFastCapture) {
                this.this$1 = this$1;
                this.mApplySettingsForFastCapture = mApplySettingsForFastCapture;
            }
            
            @Override
            public void run() {
                this.this$1.this$0.sendEvent(TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, this.mApplySettingsForFastCapture);
            }
        }
    }
    
    class StateBurstCapture extends StatePhotoBase
    {
        private static final int BURST_NUMBER_MAX = 100;
        private static final int BURST_NUMBER_MIN = 2;
        private static final int BURST_STATE_CAPTURING = 1;
        private static final int BURST_STATE_FINALIZE = 3;
        private static final int BURST_STATE_FINISH_AFTER_BUFFER_AVAILABLE = 4;
        private static final int BURST_STATE_INITIALIZE = 0;
        private static final int BURST_STATE_WAIT_FOR_BUFFER_AVAILABLE = 2;
        private static final String TAG = "StateMachine.StateBurstCapture";
        private int mBurstState;
        private final String mDataString;
        private int mIndex;
        final StateMachine this$0;
        
        private StateBurstCapture(final StateMachine this$0, final boolean b) {
            this.mCaptureState = CaptureState.STATE_BURST_CAPTURE;
            this$0.mCameraDeviceHandler.prepareBurst();
            this.mIndex = 0;
            this.mDataString = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(new Date());
            if (b) {
                this.mBurstState = 3;
            }
            else {
                this.mBurstState = 0;
            }
        }
        
        private void finishCapturing() {
            switch (this.mBurstState) {
                case 2: {
                    this.mBurstState = 4;
                    break;
                }
                case 0:
                case 1: {
                    this.mBurstState = 3;
                    break;
                }
            }
        }
        
        private void requestNextCapture() {
            if (this.this$0.mLastPhotoSavingRequest != null && this.this$0.mLastPhotoSavingRequest.mCommonStatus.savedFileType == SavingTaskManager.SavedFileType.BURST) {
                ((RequestFactory.RequestBuilder)this.this$0.mLastPhotoSavingRequest).setFinalInSavingGroup(false);
            }
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequest = this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.BURST);
            ((RequestFactory.RequestBuilder)photoSavingRequest).setRequestId(this.this$0.mViewFinder.getRequestId(false));
            photoSavingRequest.setSaveTimeForCaptureGroup(this.mDataString);
            photoSavingRequest.setCaptureIdForCaptureGourp(this.mIndex);
            this.this$0.doCapture(photoSavingRequest);
            ++this.mIndex;
        }
        
        private void requestStopBurstCapture() {
            this.this$0.mCameraDeviceHandler.finishBurst();
            this.this$0.cancelAutoFocus(false);
            this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
            CapturePerformanceLogger.setNumOfBurstTaken(this.mIndex);
        }
        
        @Override
        public void exit() {
            ResearchUtil.getInstance().setManualBurstCount(this.mIndex);
            LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, this.this$0.getCurrentCapturingMode());
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_FINISH, new Object[0]);
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.finishCapturing();
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handleOnPrepareBurstDone(final Object... array) {
            final boolean booleanValue = (boolean)array[0];
            final int mBurstState = this.mBurstState;
            if (mBurstState != 0) {
                if (mBurstState == 3) {
                    if (booleanValue) {
                        this.requestNextCapture();
                    }
                    else {
                        this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                        this.this$0.changeTo((State)new StatePhotoCapture(), new Object[0]);
                    }
                }
            }
            else if (booleanValue) {
                this.requestNextCapture();
                this.mBurstState = 1;
            }
            else {
                this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                this.this$0.changeTo((State)new StatePhotoCapture(), new Object[0]);
            }
        }
        
        @Override
        public void handleOnShutterDone(final Object... array) {
            final boolean booleanValue = (boolean)array[1];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke count:");
                sb.append(this.mIndex);
                sb.append(", af:");
                sb.append(booleanValue);
                CamLog.d(sb.toString());
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_SHUTTER_DONE, booleanValue, this.mIndex);
            if (this.mIndex >= 100) {
                this.finishCapturing();
            }
            switch (this.mBurstState) {
                case 3: {
                    if (!this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                        this.mBurstState = 4;
                        break;
                    }
                    if (this.mIndex >= 2) {
                        this.requestStopBurstCapture();
                        break;
                    }
                    if (this.this$0.isStorageWritable(Storage.StorageType.INTERNAL)) {
                        this.requestNextCapture();
                        break;
                    }
                    this.requestStopBurstCapture();
                    break;
                }
                case 2: {
                    throw new IllegalStateException("WAIT_FOR_BUFFER_AVAILABLE cannot accept ON_SHUTTER_DONE event");
                }
                case 1: {
                    if (!this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                        this.mBurstState = 2;
                        break;
                    }
                    if (this.this$0.isStorageWritable(Storage.StorageType.INTERNAL)) {
                        this.requestNextCapture();
                        break;
                    }
                    this.requestStopBurstCapture();
                    break;
                }
                case 0: {
                    throw new IllegalStateException("INITIALIZE cannot accept ON_SHUTTER_DONE event");
                }
            }
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            final int mBurstState = this.mBurstState;
            if (mBurstState != 2) {
                if (mBurstState == 4) {
                    if (!this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                        CamLog.e("ON_STORE_COMPLETED is received. But buffer is not available.");
                        return;
                    }
                    if (this.mIndex < 2) {
                        if (this.this$0.isStorageWritable(Storage.StorageType.INTERNAL)) {
                            this.requestNextCapture();
                        }
                        else {
                            this.requestStopBurstCapture();
                        }
                    }
                    else {
                        this.requestStopBurstCapture();
                    }
                }
            }
            else {
                if (!this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                    CamLog.e("ON_STORE_COMPLETED is received. But buffer is not available.");
                    return;
                }
                this.mBurstState = 1;
                this.requestNextCapture();
            }
        }
        
        @Override
        public void handleOnTakePictureDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke id:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.requestStorePicture(photoSavingRequestBuilder);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_BURST_CAPTURE);
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            final Storage.StorageType obj = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : type = ");
                sb.append(obj);
                sb.append(", state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            switch (this.mBurstState) {
                case 2: {
                    this.mBurstState = 4;
                    break;
                }
                case 1: {
                    this.mBurstState = 3;
                    break;
                }
            }
        }
    }
    
    class StatePhotoBase extends State
    {
        final StateMachine this$0;
        
        private StatePhotoBase(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_PHOTO_BASE;
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = false;
        }
        
        @Override
        public void handleOnSemiAutoEnabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = true;
        }
    }
    
    class StateBurstCaptureWaitForAfDone extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StateBurstCaptureWaitForAfDone";
        private boolean mIsCancelRequested;
        final StateMachine this$0;
        
        private StateBurstCaptureWaitForAfDone(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE;
            this.mIsCancelRequested = false;
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.mIsCancelRequested = true;
        }
        
        @Override
        public void handleOnAutoFocusDone(final Object... array) {
            if (this.this$0.checkBurstConditions((boolean)array[1])) {
                this.this$0.changeTo((State)new StateBurstCapture(this.mIsCancelRequested), array);
            }
            else {
                this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                this.this$0.changeTo((State)new StatePhotoCapture(), array);
            }
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.cancelAutoFocus(true);
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateCameraSwitching extends State
    {
        private static final String TAG = "StateMachine.StateCameraSwitching";
        private final FastCapture mFastCapture;
        private boolean mIsCameraOpened;
        private boolean mIsEvfPrepared;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final StateMachine this$0;
        
        private StateCameraSwitching(final StateMachine this$0, final CameraDeviceHandler.CameraSessionId mSessionId, final FastCapture mFastCapture) {
            this.mIsEvfPrepared = false;
            this.mIsCameraOpened = false;
            this.mCaptureState = CaptureState.STATE_CAMERA_SWITCHING;
            this.mSessionId = mSessionId;
            this.mFastCapture = mFastCapture;
            this$0.mHandler.removeCallbacks(this$0.mNotifyResumeTimeoutTask);
            this$0.mHandler.postDelayed(this$0.mNotifyResumeTimeoutTask, 7000L);
        }
        
        private void moveStateIfCaptureReady() {
            if (this.mIsCameraOpened && this.mIsEvfPrepared) {
                if (this.this$0.getCurrentCapturingMode().isFront() && this.this$0.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                    this.this$0.showBlackScreen();
                }
                this.this$0.mCameraDeviceHandler.startPreview();
                if (this.this$0.isFusionMonitoringNeeded()) {
                    this.this$0.mCameraDeviceHandler.startFusionMonitoring();
                }
                this.this$0.startFastCapture(this.mFastCapture, StartupAction.NONE);
            }
        }
        
        @Override
        public void exit() {
            super.exit();
            this.this$0.mHandler.removeCallbacks(this.this$0.mNotifyResumeTimeoutTask);
        }
        
        @Override
        public void handleOnCameraDeviceOpened(final Object... array) {
            if (array[0] == this.mSessionId) {
                this.mIsCameraOpened = true;
                this.moveStateIfCaptureReady();
            }
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.mIsEvfPrepared = true;
            this.moveStateIfCaptureReady();
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleResumeTimeout(final Object... array) {
            ResearchUtil.getInstance().setCameraNotAvailableFailedToOpen();
            CamLog.i("StateMachine.StateCameraSwitching", "[CameraNotAvailable] resume timeout.");
            PlatformCapability.setDeviceError(true);
            this.this$0.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
            this.this$0.changeTo((State)new StateWarning(), new Object[0]);
        }
    }
    
    class StateCaptureCountdown extends StatePhotoReady
    {
        private SelfTimerFeedback mFeedback;
        private final LedLight mLedLight;
        private final Event.SelfTimerTrigger mTrigger;
        final StateMachine this$0;
        
        private StateCaptureCountdown(final StateMachine this$0, final Event.SelfTimerTrigger mTrigger) {
            this.this$0 = this$0.super(false);
            this.mCaptureState = CaptureState.STATE_CAPTURE_COUNTDOWN;
            this.mLedLight = new LedLightImpl();
            this.mTrigger = mTrigger;
        }
        
        private void recoverFlash() {
            if (((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isVideo()) {
                this.this$0.mCameraDeviceHandler.setTorchAndCommit(((PhotoLight)this.this$0.mUserSettings.get(UserSettingKey.PHOTO_LIGHT)).getBooleanValue());
            }
            else if (!((CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isFront()) {
                this.this$0.mCameraDeviceHandler.setFlashModeAndCommit((Flash)this.this$0.mUserSettings.get(UserSettingKey.FLASH));
            }
        }
        
        private void start(final int n, final SoundPlayer.Type type) {
            (this.mFeedback = new SelfTimerFeedback(n, this.mLedLight, !this.this$0.getCurrentCapturingMode().isFront() && this.this$0.mUserSettings.get(UserSettingKey.FLASH) != Flash.OFF, (SelfTimerFeedback.SelfTimerFeedbackListener)new SelfTimerFeedback.SelfTimerFeedbackListener(this) {
                final StateCaptureCountdown this$1;
                
                @Override
                public void onBlinkFinished() {
                    this.this$1.recoverFlash();
                }
                
                @Override
                public void onCountDownFinished() {
                    if (this.this$1.this$0.getCurrentCapturingMode().isVideo()) {
                        this.this$1.this$0.changeTo((State)new StatePhotoReadyForRecording(this.this$1.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE), false);
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                    }
                    else if (this.this$1.this$0.startAutoFocus()) {
                        this.this$1.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
                    }
                    Event.CaptureTrigger captureTrigger = Event.CaptureTrigger.SELF_TIMER;
                    if (this.this$1.mTrigger == Event.SelfTimerTrigger.GESTURE) {
                        captureTrigger = Event.CaptureTrigger.GESTURE;
                        LocalResearchUtil.getInstance().setPredictiveLaunchState(false);
                    }
                    else if (this.this$1.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE) {
                        captureTrigger = Event.CaptureTrigger.SIDE_SENSE;
                    }
                    ResearchUtil.getInstance().setCaptureTrigger(captureTrigger);
                }
                
                @Override
                public void onSoundTypeChange(final long n) {
                    if (this.this$1.this$0.shouldPlayShutterSound()) {
                        if (n == 4000L) {
                            this.this$1.this$0.mActivity.playSound(SoundPlayer.Type.SELF_TIMER_4SEC);
                        }
                        else {
                            this.this$1.this$0.mActivity.playSound(SoundPlayer.Type.SELF_TIMER_1SEC);
                        }
                    }
                }
            })).start(0);
            if (type != null && this.this$0.shouldPlayShutterSound()) {
                this.this$0.mActivity.playSound(type);
            }
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateCaptureCountdown");
            }
            SelfTimer selfTimer2;
            final SelfTimer selfTimer = selfTimer2 = this.this$0.mViewFinder.getPhotoSelfTimerSetting();
            if (this.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE && (selfTimer2 = selfTimer) == SelfTimer.OFF) {
                selfTimer2 = SelfTimer.SIDE_COUNT_DOWN;
            }
            this.start(selfTimer2.getDurationInMillisecond(), selfTimer2.getSoundType());
            this.this$0.mGestureShutter.handleSelftimerStarted();
        }
        
        @Override
        public void exit() {
            this.this$0.stopPlaySound();
            this.mFeedback.stop();
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_SELFTIMER_FINISHED, new Object[0]);
            this.this$0.mGestureShutter.handleSelftimerStopped(true);
        }
        
        @Override
        public void handleCapture(final Object... array) {
            this.recoverFlash();
            if (this.this$0.startAutoFocus()) {
                this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
            }
        }
        
        @Override
        public void handleCaptureBurst(final Object... array) {
            if (this.this$0.checkBurstConditions(true)) {
                this.recoverFlash();
                if (this.this$0.startAutoFocus()) {
                    this.this$0.changeTo((State)new StateBurstCaptureWaitForAfDone(), new Object[0]);
                }
            }
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
        }
        
        @Override
        public void handleCaptureReady(final Object... array) {
            this.recoverFlash();
            if (this.this$0.startAutoFocus()) {
                this.this$0.changeTo((State)new StatePhotoAfSearch(), array);
            }
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
        }
        
        @Override
        public void handleDeselectObjectPosition(final Object... array) {
        }
        
        @Override
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
            this.this$0.notifyCoolingUltraLow(false);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.recoverFlash();
            super.handlePause(array);
        }
        
        @Override
        public void handleSelfTimerCancel(final Object... array) {
            this.recoverFlash();
            ResearchUtil.getInstance().sendSelfTimerCancelledEvent(this.mTrigger);
            this.this$0.changeToStandby();
        }
        
        @Override
        public void handleSetSelectedObjectPosition(final Object... array) {
            if (TouchCapture.ON == this.this$0.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                super.handleSetSelectedObjectPosition(array);
            }
        }
        
        @Override
        public void handleSetTouchedPosition(final Object... array) {
            if (!((SmileCapture)this.this$0.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn() && !((ShutterTrigger)this.this$0.mUserSettings.get(UserSettingKey.SHUTTER_TRIGGER)).isGestureShutterOn()) {
                if (TouchCapture.ON == this.this$0.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                    super.handleSetTouchedPosition(array);
                }
            }
        }
        
        @Override
        public void handleStartAfAfterObjectTracked(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            if (TouchCapture.ON == this.this$0.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                this.recoverFlash();
                super.handleStartAfAfterObjectTracked(array);
            }
        }
        
        @Override
        public void handleStartRecording(final Object... array) {
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke checkSaveDestinationCanBeChange:");
                sb.append(this.this$0.checkSaveDestinationCanBeChange(this.this$0.getCurrentStorage()));
                CamLog.d(sb.toString());
            }
            final Storage.StorageType obj = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Storage corruption : type = ");
                sb2.append(obj);
                sb2.append(", state = ");
                sb2.append(this.this$0.mCurrentState);
                CamLog.w(sb2.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.recoverFlash();
            if (!this.this$0.checkSaveDestinationCanBeChange(this.this$0.getCurrentStorage())) {
                this.this$0.changeTo((State)new StateWarning(), array);
            }
            else {
                this.this$0.changeToStandby();
            }
        }
        
        private class LedLightImpl implements LedLight
        {
            final StateCaptureCountdown this$1;
            
            private LedLightImpl(final StateCaptureCountdown this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void turnOff() {
                this.this$1.this$0.mCameraDeviceHandler.setTorchAndCommit(false);
            }
            
            @Override
            public void turnOn() {
                this.this$1.this$0.mCameraDeviceHandler.setTorchAndCommit(true);
            }
        }
    }
    
    class StatePhotoReady extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StatePhotoReady";
        private CameraParameters.FaceDetectionResult mLatestFaceDetectionResult;
        private NotifyDelayedEventTask mNotifyDelayedEventTask;
        private StartupAction mStartupAction;
        private final boolean mWithExtensionFeatures;
        final StateMachine this$0;
        
        public StatePhotoReady(final StateMachine stateMachine, final boolean b) {
            this(stateMachine, b, false, StartupAction.NONE);
        }
        
        public StatePhotoReady(final StateMachine this$0, final boolean mWithExtensionFeatures, final boolean b, final StartupAction mStartupAction) {
            this.mLatestFaceDetectionResult = null;
            this.mCaptureState = CaptureState.STATE_PHOTO_READY;
            this.mWithExtensionFeatures = mWithExtensionFeatures;
            this.mStartupAction = mStartupAction;
            this$0.sendResearchViewEvent();
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePhotoReady");
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.notifyStateIdleToWearable();
            }
            if (PlatformCapability.hasDeviceError()) {
                return;
            }
            boolean b = false;
            Label_0155: {
                if (this.this$0.mActivity != null) {
                    if (PlatformCapability.isPowerSavingSupported(this.this$0.getCurrentCameraId())) {
                        if (this.this$0.mActivity.isThermalWarningReceived()) {
                            this.this$0.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
                            b = true;
                            break Label_0155;
                        }
                        if (this.this$0.mActivity.isThermalWarningExtraState()) {
                            this.this$0.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
                        }
                    }
                    else if (this.this$0.mActivity.isThermalWarningExtraState()) {
                        this.this$0.mCameraDeviceHandler.enableFpsLimitation();
                    }
                }
                b = false;
            }
            if (this.mWithExtensionFeatures) {
                if (!this.this$0.mViewFinder.isTouchFocus()) {
                    this.this$0.mCameraDeviceHandler.startFaceDetection();
                }
                if (!this.this$0.mIsSemiAutoEnabled) {
                    this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                }
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.enableAutoPowerOffTimer();
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, this.this$0.getSensorOrientation());
            if (!b) {
                this.this$0.mGestureShutter.handlePreviewStarted(this.this$0.getCurrentCapturingMode(), this.this$0.mCameraDeviceHandler.getStreamingImageRetriever());
            }
            this.this$0.mPredictiveApplier.attemptCommitSettings();
            if (this.this$0.mViewFinder.isHeadUpDisplayReady() && this.this$0.isFusionMonitoringNeeded()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, this.this$0.mCameraDeviceHandler.getLatestFusionResult());
            }
            final Storage.StorageType access$400 = this.this$0.getCurrentStorage();
            if (!this.this$0.isStorageWritable(access$400) && !this.this$0.checkSaveDestinationCanBeChange(access$400)) {
                this.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, access$400, this.this$0.mStorage.getCurrentState(access$400));
            }
            if (!this.this$0.mIsSceneRecognitionValid) {
                this.this$0.notifySceneRecognitionDisabled();
            }
        }
        
        @Override
        public void exit() {
            this.this$0.mActivity.notifyStateBlockedToWearable();
            this.this$0.mGestureShutter.handlePreviewStopped();
            if (this.mNotifyDelayedEventTask != null) {
                this.this$0.removeDelayedEvent(this.mNotifyDelayedEventTask);
                this.mNotifyDelayedEventTask = null;
            }
            this.this$0.mPredictiveApplier.leaveSuppressor(this);
        }
        
        @Override
        public void handleCapture(final Object... array) {
            if (this.this$0.startAutoFocus()) {
                this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
            }
            else {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
            }
        }
        
        @Override
        public void handleCaptureBurst(final Object... array) {
            if (this.this$0.checkBurstConditions(true) && this.this$0.startAutoFocus()) {
                this.this$0.changeTo((State)new StateBurstCaptureWaitForAfDone(), array);
            }
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL, new Object[0]);
        }
        
        @Override
        public void handleCaptureReady(final Object... array) {
            if (this.this$0.startAutoFocus()) {
                this.this$0.changeTo((State)new StatePhotoAfSearch(), array);
            }
        }
        
        @Override
        public void handleChangeAngleStart(final Object... array) {
            this.this$0.doZoomChangeAngle();
        }
        
        @Override
        public void handleChangeSelectedFace(final Object... array) {
            this.this$0.doChangeSelectedFace((Point)array[0]);
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, new Object[0]);
            this.this$0.mCameraDeviceHandler.resetFocusModeAndCommit();
            if (this.this$0.isTouchAeEnabled()) {
                this.this$0.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
            }
        }
        
        @Override
        public void handleDeselectObjectPosition(final Object... array) {
            this.this$0.doStopObjectTracking();
        }
        
        @Override
        public void handleDialogOpened(final Object... array) {
            if (array != null && array.length != 0) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, array[0]);
            }
            this.this$0.changeTo((State)new StateOperationRestricted(), array);
            this.this$0.mActivity.notifyStateBlockedToWearable();
            if (this.this$0.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                this.this$0.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
            this.this$0.mPredictiveApplier.leaveSuppressor(this);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_STOP, new Object[0]);
        }
        
        @Override
        public void handleKeyMenu(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
            this.this$0.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.SETTING_DIALOG);
        }
        
        @Override
        public void handleOnAmberBlueColorChanged(final Object... array) {
            this.this$0.updateAmberBlueColor((float)array[0]);
        }
        
        @Override
        public void handleOnAutoFocusDone(final Object... array) {
            this.this$0.cancelAutoFocus(false);
        }
        
        @Override
        public void handleOnBrightnessChanged(final Object... array) {
            this.this$0.updateBrightness((float)array[0]);
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.mLatestFaceDetectionResult = (CameraParameters.FaceDetectionResult)array[0];
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, this.mLatestFaceDetectionResult);
            this.this$0.requestPhotoSmileCapture();
        }
        
        @Override
        public void handleOnHeatedOverCoolingLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setLowPower();
        }
        
        @Override
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
            this.this$0.notifyCoolingUltraLow(this.this$0.isStorageFull(this.this$0.getCurrentStorage()) ^ true);
        }
        
        @Override
        public void handleOnHeatedOverWarningExtra(final Object... array) {
            this.this$0.mCameraDeviceHandler.enableFpsLimitation();
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
        }
        
        @Override
        public void handleOnSceneModeChanged(final Object... array) {
            if (this.this$0.mIsSceneRecognitionValid) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED, array[0]);
            }
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            super.handleOnSemiAutoDisabled(array);
            this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
            this.this$0.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            this.this$0.mCameraDeviceHandler.setBrightnessAndCommit(0);
        }
        
        @Override
        public void handleOnSemiAutoEnabled(final Object... array) {
            super.handleOnSemiAutoEnabled(array);
            this.this$0.mCameraDeviceHandler.stopSceneRecognition();
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
            this.this$0.mUserSettings.set(DestinationToSave.EMMC);
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, false);
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
            final VariableIndex variableIndex = (VariableIndex)array[0];
            this.this$0.mCameraDeviceHandler.setZoomAndCommit(variableIndex.getRatio());
            this.this$0.onZoomChange(variableIndex.getIndex());
        }
        
        @Override
        public void handlePrepareZoom(final Object... array) {
            final VariableIndex variableIndex = (VariableIndex)array[0];
            this.handleClearFocus(array);
            this.this$0.mPredictiveApplier.entrySuppressor(this);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_START, variableIndex.getIndex());
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            if (array[0]) {
                this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                this.this$0.mCameraDeviceHandler.startFaceDetection();
            }
            if (this.mStartupAction == StartupAction.CAPTURE) {
                this.this$0.sendEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.NORMAL);
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
            if (this.this$0.isTutorialNeededToBeShownForCurrentMode()) {
                this.this$0.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            }
        }
        
        @Override
        public void handleRequestUpdateHighSensitivityFusionMode(final Object... array) {
            this.this$0.updateFusionModeSetting((FusionMode)array[0]);
        }
        
        @Override
        public void handleSetSelectedObjectPosition(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            final Rect rect = (Rect)array[1];
            if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.this$0.doStartObjectTracking(rect);
            }
        }
        
        @Override
        public void handleSetTouchedPosition(final Object... array) {
            if (!((SmileCapture)this.this$0.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn() && !((ShutterTrigger)this.this$0.mUserSettings.get(UserSettingKey.SHUTTER_TRIGGER)).isGestureShutterOn()) {
                if (array[2] == FocusRectangles.FocusSetType.RELEASE) {
                    final Rect position = this.this$0.mViewFinder.getPosition((Point)array[0]);
                    if (position.isEmpty()) {
                        return;
                    }
                    if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                        this.this$0.mCameraDeviceHandler.setFocusPositionAndCommit(position);
                    }
                    if (this.this$0.isTouchAeEnabled()) {
                        this.this$0.mCameraDeviceHandler.setMeteringAreaAndCommit(position, (Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
                    }
                }
                if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF || this.this$0.mUserSettings.get(UserSettingKey.TOUCH_INTENTION) == TouchIntention.FOCUS_AND_EXPOSURE) {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED, array[0], array[2]);
                }
            }
        }
        
        @Override
        public void handleStartAfAfterObjectTracked(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            if (!PlatformCapability.isObjectTrackingSupported(this.this$0.getCurrentCameraId())) {
                return;
            }
            if (!this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                return;
            }
            final Rect rect = (Rect)array[1];
            this.this$0.doStopObjectTracking();
            this.this$0.doStartObjectTracking(rect);
            this.this$0.changeTo((State)new StatePhotoWaitingTrackedObjectForAfStart(), new Object[0]);
        }
        
        @Override
        public void handleStartCaptureCountDown(final Object... array) {
            final Event.SelfTimerTrigger selfTimerTrigger = (Event.SelfTimerTrigger)getEventParam(array, 0, Event.SelfTimerTrigger.class, Event.SelfTimerTrigger.NORMAL);
            if (this.mNotifyDelayedEventTask != null) {
                this.this$0.removeDelayedEvent(this.mNotifyDelayedEventTask);
                this.mNotifyDelayedEventTask = null;
            }
            if (this.this$0.isLazyInitializationRunning()) {
                this.mNotifyDelayedEventTask = this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, array);
            }
            else if (this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                if (this.mStartupAction == StartupAction.CAPTURE) {
                    this.this$0.updatePhotoSelftimer(SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN);
                }
                this.this$0.changeTo((State)new StateCaptureCountdown(selfTimerTrigger), true);
            }
            else {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
            }
        }
        
        @Override
        public void handleStartTransitionOperation(final Object... array) {
            if (((AnimationRequest)array[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                this.this$0.changeTo((State)new StateModeChanging(), new Object[0]);
            }
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            final Storage.StorageType storageType = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                this.this$0.changeTo((State)new StateFatal(false, false), new Object[0]);
            }
            else if (!this.this$0.checkSaveDestinationCanBeChange(storageType)) {
                this.this$0.changeTo((State)new StateWarning(), array);
            }
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.mPredictiveApplier.attemptCommitSettings();
        }
        
        @Override
        public void handleSwitchCamera(final Object... array) {
            this.this$0.switchCamera((AnimationRequest.AnimationType)array[0]);
        }
        
        @Override
        public void handleTouchContentProgress() {
            if (this.this$0.mViewFinder == null) {
                return;
            }
            if (this.this$0.mLastVideoSavingRequest != null) {
                if (this.this$0.mLastPhotoSavingRequest == null) {
                    return;
                }
                if (((RequestFactory.RequestBuilder)this.this$0.mLastVideoSavingRequest).getDateTaken() > ((RequestFactory.RequestBuilder)this.this$0.mLastPhotoSavingRequest).getDateTaken()) {
                    return;
                }
            }
            if (this.this$0.mLastPhotoSavingRequest != null && this.this$0.mLastPhotoSavingRequest.getImageData() != null) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_INSTANT_VIEWER, this.this$0.mLastPhotoSavingRequest.getImageData(), this.this$0.mLastPhotoSavingRequest, this.this$0.mLastStoreDataResult);
            }
        }
    }
    
    class StateCropping extends State
    {
        private CaptureState mPreviousCaptureState;
        final StateMachine this$0;
        
        public StateCropping(final StateMachine this$0, final CaptureState mPreviousCaptureState) {
            this.mPreviousCaptureState = null;
            this.mCaptureState = CaptureState.STATE_CROPPING;
            this.mPreviousCaptureState = mPreviousCaptureState;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateCropping");
            }
        }
        
        @Override
        public void exit() {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ANGLE_CHANGE_COMPLETED, new Object[0]);
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[this.mPreviousCaptureState.ordinal()]) {
                case 4: {
                    this.this$0.changeTo((State)new StateWarning(), new Object[0]);
                    break;
                }
                case 3: {
                    this.this$0.changeTo((State)this.this$0.new StateVideoReady(), new Object[0]);
                    break;
                }
                case 2: {
                    this.this$0.changeTo((State)this.this$0.new StatePhotoReady(false), new Object[0]);
                    break;
                }
                case 1: {
                    this.this$0.changeTo((State)new StateOperationRestricted(), new Object[0]);
                    break;
                }
            }
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
            this.this$0.mCameraDeviceHandler.setZoomAndCommit(((VariableIndex)array[0]).getRatio());
        }
    }
    
    class StateFatal extends State
    {
        private static final String TAG = "StateMachine.StateFatal";
        private boolean mIsSaving;
        private boolean mLazyAbort;
        private LazyAbortTask mLazyAbortTask;
        private StoreDataResult mStoreDataResult;
        final StateMachine this$0;
        
        private StateFatal(final StateMachine this$0, final boolean mIsSaving, final boolean mLazyAbort) {
            this.mIsSaving = false;
            this.mLazyAbort = false;
            this.mStoreDataResult = null;
            this.mLazyAbortTask = null;
            this.mCaptureState = CaptureState.STATE_FATAL;
            this.mIsSaving = mIsSaving;
            this.mLazyAbort = mLazyAbort;
            if (!this$0.mCameraDeviceHandler.isRecorderWorking()) {
                this$0.mCameraDeviceHandler.releaseRecorder();
            }
            this$0.mCameraDeviceHandler.stopFaceDetection();
            this$0.mCameraDeviceHandler.stopSceneRecognition();
            if (this$0.mObjectTracking != null) {
                this$0.doStopObjectTracking();
            }
            if (!this$0.mPendingTaskListForStandby.isEmpty()) {
                this$0.mPendingTaskListForStandby.clear();
            }
            this$0.mCameraDeviceHandler.stopPreview();
            this$0.removeChangeCameraModeTask();
            this$0.removeStartRecordingTask();
            if (this.mLazyAbort && !this$0.mViewFinder.isMessageDialogOpened()) {
                this.doLazyAbort();
            }
        }
        
        private void doLazyAbort() {
            if (this.mLazyAbortTask != null) {
                this.this$0.mHandler.removeCallbacks((Runnable)this.mLazyAbortTask);
            }
            this.mLazyAbortTask = new LazyAbortTask();
            this.this$0.mHandler.postDelayed((Runnable)this.mLazyAbortTask, 4000L);
        }
        
        @Override
        public void exit() {
            if (this.mLazyAbortTask != null) {
                this.this$0.mHandler.removeCallbacks((Runnable)this.mLazyAbortTask);
                this.mLazyAbortTask = null;
            }
        }
        
        @Override
        public void handleDialogClosed(final Object... array) {
            if (array != null && array.length != 0 && array[0] == ViewFinder.UiComponentKind.FATAL_ALERT_DIALOG) {
                if (this.mLazyAbort) {
                    this.doLazyAbort();
                }
                else if (this.mIsSaving && this.mStoreDataResult == null) {
                    if (this.mStoreDataResult == null || !this.this$0.mActivity.isOneShot()) {
                        return;
                    }
                    this.this$0.onOneShotStoreCompleted(this.mStoreDataResult);
                }
                else {
                    this.this$0.mActivity.terminateApplication();
                }
            }
        }
        
        @Override
        public void handleFinalize(final Object... array) {
            this.this$0.changeTo((State)new StateFinalize(), array);
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            this.mStoreDataResult = (StoreDataResult)array[0];
            if (!this.this$0.mViewFinder.isMessageDialogOpened()) {
                if (this.this$0.mActivity.isOneShot()) {
                    this.this$0.onOneShotStoreCompleted(this.mStoreDataResult);
                }
                else {
                    this.this$0.mActivity.terminateApplication();
                }
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
            this.this$0.mActivity.terminateApplication();
        }
        
        private class LazyAbortTask implements Runnable
        {
            final StateFatal this$1;
            
            private LazyAbortTask(final StateFatal this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke LazyAbortTask");
                }
                this.this$1.this$0.mActivity.terminateApplication();
            }
        }
    }
    
    class StateFinalize extends State
    {
        final StateMachine this$0;
        
        private StateFinalize(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_FINALIZE;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateFinalize");
            }
            this.this$0.mGestureShutter.release();
            this.this$0.storeSavingRequestList();
            this.this$0.mObjectTracking = null;
        }
        
        @Override
        public void handleOnTakePictureDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke id:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.requestStorePicture(photoSavingRequestBuilder);
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
        }
    }
    
    class StateHighFrameRateVideoRecordingInSuperSlowMotion extends StateVideoBase
    {
        public static final String TAG = "StateMachine.StateHighFrameRateVideoRecordingInSuperSlowMotion";
        private boolean mAlreadyHighFrameRateRecordingDone;
        final StateMachine this$0;
        
        public StateHighFrameRateVideoRecordingInSuperSlowMotion(final StateMachine this$0) {
            this.this$0 = this$0.super();
            this.mAlreadyHighFrameRateRecordingDone = false;
            this.mCaptureState = CaptureState.STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION;
            this$0.mIsVideoRecording = true;
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
        }
        
        @Override
        public void handleHighFameRateRecordingDone(final Object... array) {
            this.mAlreadyHighFrameRateRecordingDone = true;
        }
        
        @Override
        public void handleOnReachBatteryLimit(final Object... array) {
            this.this$0.doStopRecording(false);
            this.this$0.changeTo((State)new StateFatal(true, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.pauseVideoRecording(array);
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
        }
        
        @Override
        public void handlePrepareZoom(final Object... array) {
        }
        
        @Override
        public void handleSlowMotionFeedbackAnimationEnd(final Object... array) {
            if (!this.mAlreadyHighFrameRateRecordingDone) {
                this.this$0.changeTo((State)this.this$0.new StateWaitForHighFrameRateVideoRecordingDone(), new Object[0]);
            }
            else {
                switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                    case 3: {
                        this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), new Object[0]);
                        this.this$0.doStopRecording(false);
                        break;
                    }
                    case 2: {
                        this.this$0.changeTo((State)this.this$0.new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                        break;
                    }
                }
            }
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
    }
    
    class StateVideoBase extends State
    {
        final StateMachine this$0;
        
        StateVideoBase(final StateMachine this$0) {
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_STOP, new Object[0]);
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleOnHeatedOverCritical(final Object... array) {
            if (this.this$0.mCameraDeviceHandler.isRecorderWorking()) {
                this.this$0.doStopRecording(false);
            }
            super.handleOnHeatedOverCritical(array);
        }
        
        @Override
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
            this.this$0.mChapterThumbnail = new ChapterThumbnail((byte[])array[0], (Integer)array[1], (Rect)array[2]);
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
        
        @Override
        public void handleOnRecordingError(final Object... array) {
            this.this$0.doHandleRecordingError();
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = false;
        }
        
        @Override
        public void handleOnSemiAutoEnabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = true;
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            final StoreDataResult storeDataResult = (StoreDataResult)array[0];
            if (this.this$0.mActivity.isOneShot()) {
                this.this$0.onOneShotStoreCompleted(storeDataResult);
            }
            else if (this.this$0.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION && !PlatformCapability.hasDeviceError()) {
                ApplicationLauncher.launchVideoEditor(this.this$0.mActivity, storeDataResult);
            }
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
            final VariableIndex variableIndex = (VariableIndex)array[0];
            this.this$0.mCameraDeviceHandler.setZoomAndCommit(variableIndex.getRatio());
            this.this$0.onZoomChange(variableIndex.getIndex());
        }
        
        @Override
        public void handlePrepareZoom(final Object... array) {
            final VariableIndex variableIndex = (VariableIndex)array[0];
            ((State)this).handleClearFocus(array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_START, variableIndex.getIndex());
        }
    }
    
    class StateInitialize extends State
    {
        private Evf mEvf;
        final StateMachine this$0;
        
        private StateInitialize(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_INITIALIZE;
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.mEvf = (Evf)array[0];
        }
        
        @Override
        public void handleOnInitialAutoFocusDone(final Object... array) {
            this.this$0.mCameraDeviceHandler.preCapture();
        }
        
        @Override
        public void handleOnPreShutterDone(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, array);
        }
        
        @Override
        public void handleOnPreTakePictureDone(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleResume(final Object... array) {
            final CameraDeviceHandler.CameraSessionId cameraSessionId = (CameraDeviceHandler.CameraSessionId)array[1];
            final StartupAction startupAction = (StartupAction)array[2];
            if (this.this$0.mCameraDeviceHandler.isCameraDisabled()) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
                return;
            }
            Label_0145: {
                switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[this.this$0.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
                    default: {
                        break Label_0145;
                    }
                    case 3: {
                        final RequestFactory.PhotoSavingRequestBuilder andClearPreCaptureResult = this.this$0.mCameraDeviceHandler.getAndClearPreCaptureResult();
                        if (andClearPreCaptureResult != null) {
                            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, andClearPreCaptureResult);
                        }
                        break Label_0145;
                    }
                    case 1: {
                        this.this$0.mCameraDeviceHandler.preCapture();
                    }
                    case 2: {
                        final FastCapture fastCapture = (FastCapture)array[0];
                        this.this$0.changeTo((State)new StateResume(cameraSessionId, fastCapture, this.mEvf, startupAction), fastCapture, this.this$0.hasRemainSavingRequest(), array);
                    }
                }
            }
        }
    }
    
    class StateLowFrameRateVideoRecordingInSuperSlowMotion extends StateVideoBase
    {
        private static final String TAG = "StateMachine.StateLowFrameRateVideoRecordingInSuperSlowMotion";
        final StateMachine this$0;
        
        public StateLowFrameRateVideoRecordingInSuperSlowMotion(final StateMachine this$0) {
            this.this$0 = this$0.super();
            this.mCaptureState = CaptureState.STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION;
            this$0.mIsVideoRecording = true;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateLowFrameRateVideoRecordingInSuperSlowMotion");
            }
            super.entry();
            this.this$0.checkThermalWarning();
        }
        
        @Override
        public void handleOnHeatedOverCoolingLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setLowPower();
        }
        
        @Override
        public void handleOnReachBatteryLimit(final Object... array) {
            this.this$0.doStopRecording(false);
            this.this$0.changeTo((State)new StateFatal(true, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.pauseVideoRecording(array);
        }
        
        @Override
        public void handleStopRecording(final Object... array) {
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
        
        @Override
        public void handleStopRecordingSlowMotion(final Object... array) {
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
        
        @Override
        public void handleTriggerSlowMotion(final Object... array) {
            this.this$0.mCameraDeviceHandler.startSuperSlowMotion();
            this.this$0.changeTo((State)this.this$0.new StateHighFrameRateVideoRecordingInSuperSlowMotion(), array);
            this.this$0.mHighFrameRateVideoRecordingCountInSuperSlowMotion++;
            this.this$0.mViewFinder.startSlowMotionFeedbackAnimation();
        }
    }
    
    class StateModeChanging extends StatePhotoBase
    {
        final StateMachine this$0;
        
        private StateModeChanging(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_MODE_CHANGING;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateModeChanging");
            }
            if (!PlatformCapability.isSceneRecognitionSupported(this.this$0.getCurrentCameraId())) {
                this.this$0.mCameraDeviceHandler.stopSceneRecognition();
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.notifyStateBlockedToWearable();
            }
        }
        
        @Override
        public void handleFinishTransitionOperation(final Object... array) {
            final AnimationRequest animationRequest = (AnimationRequest)array[0];
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()]) {
                case 2: {
                    if (animationRequest.mType == AnimationRequest.AnimationType.SWITCH_TOUCH || animationRequest.mTarget.isFront() != animationRequest.mFrom.isFront()) {
                        this.this$0.switchCamera(animationRequest.mTarget, animationRequest.mType);
                        break;
                    }
                    if (this.this$0.getCurrentCapturingMode() != animationRequest.mTarget) {
                        this.this$0.requestChangeModeTo(animationRequest.mTarget, animationRequest.mType);
                        break;
                    }
                    break;
                }
                case 1: {
                    this.this$0.changeToStandby();
                    break;
                }
            }
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            super.handleOnSemiAutoDisabled(array);
            this.this$0.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            this.this$0.mCameraDeviceHandler.setBrightnessAndCommit(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(0);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateNone extends State
    {
        final StateMachine this$0;
        
        private StateNone(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_NONE;
        }
        
        @Override
        public void handleInitialize(final Object... array) {
            this.this$0.changeTo((State)new StateInitialize(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateOperationRestricted extends State
    {
        private StoreDataResult mVideoStoreDataResult;
        final StateMachine this$0;
        
        private StateOperationRestricted(final StateMachine this$0) {
            this.mVideoStoreDataResult = null;
            this.mCaptureState = CaptureState.STATE_OPERATION_RESTRICTED;
            if (!this$0.getCurrentCapturingMode().isVideo() && this$0.mActivity.isThermalWarningExtraState()) {
                this$0.mCameraDeviceHandler.enableFpsLimitation();
            }
            if (this$0.mActivity != null) {
                this$0.mActivity.enableAutoPowerOffTimer();
            }
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateOperationRestricted");
            }
            this.this$0.mPredictiveApplier.entrySuppressor(this);
        }
        
        @Override
        public void exit() {
            this.this$0.mPredictiveApplier.leaveSuppressor(this);
        }
        
        @Override
        public void handleChangeAngleStart(final Object... array) {
            this.this$0.doZoomChangeAngle();
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, new Object[0]);
            this.this$0.mCameraDeviceHandler.resetFocusModeAndCommit();
            if (this.this$0.isTouchAeEnabled()) {
                this.this$0.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering)this.this$0.mUserSettings.get(UserSettingKey.METERING));
            }
        }
        
        @Override
        public void handleDeselectObjectPosition(final Object... array) {
            this.this$0.doStopObjectTracking();
        }
        
        @Override
        public void handleDialogClosed(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, array);
            this.this$0.mActivity.restartAutoPowerOffTimer();
            this.this$0.mActivity.notifyStateIdleToWearable();
            this.this$0.changeToStandby();
            if (this.mVideoStoreDataResult != null) {
                if (this.this$0.mActivity.isOneShot()) {
                    this.this$0.onOneShotStoreCompleted(this.mVideoStoreDataResult);
                }
                else if (this.this$0.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION && !PlatformCapability.hasDeviceError()) {
                    ApplicationLauncher.launchVideoEditor(this.this$0.mActivity, this.mVideoStoreDataResult);
                }
            }
        }
        
        @Override
        public void handleDialogOpened(final Object... array) {
            this.this$0.mActivity.notifyStateBlockedToWearable();
            if (array != null && array.length != 0) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, array[0]);
            }
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleKeyMenu(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
        }
        
        @Override
        public void handleOnAmberBlueColorChanged(final Object... array) {
            this.this$0.updateAmberBlueColor((float)array[0]);
        }
        
        @Override
        public void handleOnBrightnessChanged(final Object... array) {
            this.this$0.updateBrightness((float)array[0]);
        }
        
        @Override
        public void handleOnContinuousPreviewFrameUpdated(final Object... array) {
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
        }
        
        @Override
        public void handleOnHeatedOverWarningExtra(final Object... array) {
            if (!this.this$0.getCurrentCapturingMode().isVideo()) {
                this.this$0.mCameraDeviceHandler.enableFpsLimitation();
            }
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = false;
        }
        
        @Override
        public void handleOnSemiAutoEnabled(final Object... array) {
            this.this$0.mIsSemiAutoEnabled = true;
            this.this$0.mCameraDeviceHandler.stopSceneRecognition();
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            final StoreDataResult mVideoStoreDataResult = (StoreDataResult)array[0];
            if (mVideoStoreDataResult.savingRequest instanceof VideoSavingRequest) {
                this.mVideoStoreDataResult = mVideoStoreDataResult;
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.this$0.getCurrentCapturingMode().ordinal()]) {
                case 3:
                case 7:
                case 8: {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
                    break;
                }
                case 1:
                case 2:
                case 5:
                case 6: {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
                    break;
                }
            }
        }
        
        @Override
        public void handleRequestUpdateHighSensitivityFusionMode(final Object... array) {
            this.this$0.updateFusionModeSetting((FusionMode)array[0]);
        }
        
        @Override
        public void handleStartTransitionOperation(final Object... array) {
            if (((AnimationRequest)array[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                this.this$0.changeTo((State)new StateModeChanging(), new Object[0]);
            }
        }
        
        @Override
        public void handleSwitchCamera(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, new Object[0]);
            this.this$0.switchCamera((AnimationRequest.AnimationType)array[0]);
        }
    }
    
    class StatePause extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StatePause";
        final StateMachine this$0;
        
        private StatePause(final StateMachine this$0, final boolean b) {
            this.mCaptureState = CaptureState.STATE_PAUSE;
            this$0.mViewFinder.hideDisplayFlashScreen();
            if (!this$0.mCameraDeviceHandler.isRecorderWorking()) {
                this$0.mCameraDeviceHandler.releaseRecorder();
            }
            this$0.mCameraDeviceHandler.stopFaceDetection();
            this$0.mCameraDeviceHandler.stopSceneRecognition();
            this$0.mCameraDeviceHandler.stopFusionMonitoring();
            if (this$0.mObjectTracking != null) {
                this$0.doStopObjectTracking();
            }
            if (!this$0.mPendingTaskListForStandby.isEmpty()) {
                this$0.mPendingTaskListForStandby.clear();
            }
            this$0.mCameraDeviceHandler.stopPreview();
            this$0.mStorage.removeStorageStateListener((Storage.StorageStateListener)this$0.mStorageStateListener);
            this$0.mUserSettings.commit();
            this$0.mVirtualKeyEventDispatcher.stop();
            if (!this$0.mActivity.getLaunchCondition().getOneShotMode().isEnabled()) {
                final CapturingMode capturingMode = (CapturingMode)this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
                if (capturingMode != null) {
                    CapturingMode scene_RECOGNITION = null;
                    Label_0197: {
                        if (!ModeSelectorInternalMode.exists(capturingMode)) {
                            scene_RECOGNITION = capturingMode;
                            if (!capturingMode.equals(CapturingMode.FRONT_PHOTO)) {
                                break Label_0197;
                            }
                        }
                        scene_RECOGNITION = CapturingMode.SCENE_RECOGNITION;
                    }
                    this$0.mLastSettings.setCapturingMode(scene_RECOGNITION);
                    this$0.mLastSettings.writePauseTime();
                    this$0.mLastSettings.save();
                }
            }
            if (b) {
                this$0.removeChangeCameraModeTask();
            }
            this$0.removeStartRecordingTask();
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePause");
            }
        }
        
        @Override
        public void handleFinalize(final Object... array) {
            this.this$0.changeTo((State)new StateFinalize(), array);
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
        }
        
        @Override
        public void handleOnTakePictureDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke id:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.requestStorePicture(photoSavingRequestBuilder);
        }
        
        @Override
        public void handleResume(final Object... array) {
            final CameraDeviceHandler.CameraSessionId cameraSessionId = (CameraDeviceHandler.CameraSessionId)array[1];
            final StartupAction startupAction = (StartupAction)array[2];
            if (this.this$0.mCameraDeviceHandler.isCameraDisabled()) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
                return;
            }
            final FastCapture fastCapture = (FastCapture)array[0];
            this.this$0.changeTo((State)new StateResume(cameraSessionId, fastCapture, (Evf)null, startupAction), fastCapture, this.this$0.hasRemainSavingRequest());
        }
    }
    
    class StatePhotoAfDone extends StatePhotoBase
    {
        private boolean mIsClearObjectTrackingFocusFrame;
        private final boolean mIsHighQualityBurstAvailable;
        final StateMachine this$0;
        
        private StatePhotoAfDone(final StateMachine this$0, final boolean mIsHighQualityBurstAvailable) {
            this.mIsClearObjectTrackingFocusFrame = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_DONE;
            this.mIsHighQualityBurstAvailable = mIsHighQualityBurstAvailable;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePhotoAfDone");
            }
            super.entry();
            this.mIsClearObjectTrackingFocusFrame = false;
        }
        
        @Override
        public void exit() {
            super.exit();
            if (this.mIsClearObjectTrackingFocusFrame) {
                this.mIsClearObjectTrackingFocusFrame = false;
                if (this.this$0.mViewFinder != null && this.this$0.mViewFinder.isSetupHeadupDisplayInvoked()) {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                }
                this.this$0.mObjectTracking.stop();
            }
        }
        
        @Override
        public void handleCapture(final Object... array) {
            this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            this.this$0.changeTo((State)new StatePhotoCapture(), array);
        }
        
        @Override
        public void handleCaptureBurst(final Object... array) {
            if (this.this$0.checkBurstConditions(this.mIsHighQualityBurstAvailable)) {
                this.this$0.changeTo((State)new StateBurstCapture(false), array);
            }
            else {
                this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                this.this$0.changeTo((State)new StatePhotoCapture(), array);
            }
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.this$0.cancelAutoFocus(false);
            this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), array);
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnObjectLost(final Object... array) {
            super.handleOnObjectLost(array);
            this.mIsClearObjectTrackingFocusFrame = true;
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.mIsClearObjectTrackingFocusFrame = false;
            if (!PlatformCapability.isTrackingFocusDuringLockSupported(this.this$0.getCurrentCameraId())) {
                return;
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.cancelAutoFocus(true);
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.mPredictiveApplier.attemptCommitSettings();
        }
    }
    
    class StatePhotoAfSearch extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StatePhotoAfSearch";
        private boolean mIsCancelRequested;
        final StateMachine this$0;
        
        private StatePhotoAfSearch(final StateMachine this$0) {
            this.mIsCancelRequested = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_SEARCH;
        }
        
        @Override
        public void handleCapture(final Object... array) {
            if (!this.mIsCancelRequested) {
                this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), array);
            }
        }
        
        @Override
        public void handleCaptureBurst(final Object... array) {
            if (!this.mIsCancelRequested) {
                if (this.this$0.checkBurstConditions(true)) {
                    this.this$0.changeTo((State)new StateBurstCaptureWaitForAfDone(), array);
                }
                else {
                    this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), array);
                }
            }
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.mIsCancelRequested = true;
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
            this.handleCaptureCancel(array);
        }
        
        @Override
        public void handleOnAutoFocusDone(final Object... array) {
            final boolean booleanValue = (boolean)array[1];
            if (this.mIsCancelRequested) {
                this.this$0.cancelAutoFocus(false);
                this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
            }
            else {
                this.this$0.changeTo((State)new StatePhotoAfDone(booleanValue), array);
            }
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            if (!PlatformCapability.isTrackingFocusDuringLockSupported(this.this$0.getCurrentCameraId())) {
                return;
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.cancelAutoFocus(true);
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.mPredictiveApplier.attemptCommitSettings();
        }
    }
    
    class StatePhotoCapture extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StatePhotoCapture";
        private boolean mIsStorageError;
        private NextCaptureCondition mNextCapture;
        final StateMachine this$0;
        
        private StatePhotoCapture(final StateMachine this$0) {
            this.mIsStorageError = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE;
            if (this$0.mActivity.isOneShot()) {
                this.mNextCapture = NextCaptureCondition.UNACCEPTABLE;
            }
            else {
                this.mNextCapture = NextCaptureCondition.READY;
            }
            this$0.mViewFinder.cancelPredictiveCaptureIndicatorAnimation();
        }
        
        private boolean requestNextCaptureIfRequired() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke mNextCapture:");
                sb.append(this.mNextCapture);
                CamLog.d(sb.toString());
            }
            if (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] != 2) {
                return false;
            }
            this.mNextCapture = NextCaptureCondition.READY;
            this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            return true;
        }
        
        @Override
        public void exit() {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
        }
        
        @Override
        public void handleCapture(final Object... array) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke mNextCapture:");
                sb.append(this.mNextCapture);
                CamLog.d(sb.toString());
            }
            if (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] != 1) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Capture button is ignored. mNextCapture:");
                    sb2.append(this.mNextCapture);
                    sb2.append(", Saving request count:");
                    sb2.append(this.this$0.mCameraDeviceHandler.getRemainSavingPhotoRequestCount());
                    CamLog.d(sb2.toString());
                }
            }
            else if (this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                this.mNextCapture = NextCaptureCondition.REQUESTED;
            }
        }
        
        @Override
        public void handleOnPreShutterDone(final Object... array) {
        }
        
        @Override
        public void handleOnPreTakePictureDone(final Object... array) {
            if (!this.this$0.mStorage.isStorageActivated()) {
                this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, array);
                return;
            }
            final Storage.StorageType access$400 = this.this$0.getCurrentStorage();
            final boolean b = this.this$0.mStorage.getCurrentState(Storage.StorageType.EXTERNAL_CARD) == Storage.StorageState.CORRUPT;
            if (this.this$0.isStorageWritable(access$400) && !b) {
                final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
                this.this$0.mLastPhotoSavingRequest = null;
                this.this$0.mLastVideoSavingRequest = null;
                this.this$0.playShutterSound();
                final ByteBuffer imageReaderData = photoSavingRequestBuilder.getImageReaderData();
                final byte[] array2 = new byte[imageReaderData.remaining()];
                imageReaderData.get(array2);
                photoSavingRequestBuilder.setImageData(array2);
                photoSavingRequestBuilder.close();
                this.this$0.mCameraDeviceHandler.prepareCaptureImageReader((CameraDeviceHandler.ImageReaderInitializedCallback)new CameraDeviceHandler.ImageReaderInitializedCallback(this) {
                    final StatePhotoCapture this$1;
                    
                    @Override
                    public void onInitialized() {
                        this.this$1.this$0.mHandler.post((Runnable)new Runnable(this) {
                            final StateMachine$StatePhotoCapture$1 this$2;
                            
                            @Override
                            public void run() {
                                if (CamLog.DEBUG) {
                                    CamLog.d("invoke onInitialized");
                                }
                                if (this.this$2.this$1.this$0.getCurrentCaptureState() == CaptureState.STATE_PHOTO_CAPTURE) {
                                    this.this$2.this$1.this$0.cancelAutoFocus(true);
                                    this.this$2.this$1.this$0.changeTo((State)this.this$2.this$1.this$0.new StatePhotoReady(true), new Object[0]);
                                }
                            }
                        });
                    }
                });
                ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setStorageType(this.this$0.getCurrentStorage());
                this.this$0.requestStorePicture(photoSavingRequestBuilder);
                ResearchUtil.getInstance().setTimeAfDone();
                ResearchUtil.getInstance().setTimeCaptureStart();
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
                ResearchUtil.getInstance().setOrientation(photoSavingRequestBuilder.mCommonStatus.orientation);
                LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
                LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, this.this$0.getCurrentCapturingMode());
                return;
            }
            ((RequestFactory.PhotoSavingRequestBuilder)array[0]).close();
            this.this$0.mCameraDeviceHandler.prepareCaptureImageReader(null);
            this.this$0.cancelAutoFocus(false);
            this.this$0.mCameraDeviceHandler.cancelPreProcessState();
            if (b) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                this.this$0.changeTo((State)new StateFatal(false, false), new Object[0]);
            }
            else {
                this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
            }
        }
        
        @Override
        public void handleOnShutterDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            this.this$0.mViewFinder.hideDisplayFlashScreen();
            this.this$0.mViewFinder.onCaptureDone();
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, new Object[0]);
            if (((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getSomcType() == 100) {
                this.this$0.mViewFinder.startPredictiveCaptureIndicatorAnimation();
            }
            else {
                this.this$0.mViewFinder.cancelPredictiveCaptureIndicatorAnimation();
            }
            LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, this.this$0.getCurrentCapturingMode());
            if (this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable() && !this.mIsStorageError) {
                if (this.requestNextCaptureIfRequired()) {
                    ResearchUtil.getInstance().setContinuousCapture();
                    LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
                }
                else {
                    this.this$0.cancelAutoFocus(false);
                    if (!this.this$0.mActivity.isOneShot()) {
                        this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
                    }
                }
            }
            else {
                this.mNextCapture = NextCaptureCondition.UNACCEPTABLE;
            }
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            if (this.this$0.mActivity.isOneShot()) {
                this.this$0.onOneShotStoreCompleted((StoreDataResult)array[0]);
            }
            if (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] == 3) {
                if (this.this$0.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                    this.this$0.cancelAutoFocus(false);
                    this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
                }
            }
        }
        
        @Override
        public void handleOnTakePictureDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke id:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.requestStorePicture(photoSavingRequestBuilder);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.mViewFinder.setDisplayFlashRequired(false);
            this.this$0.mViewFinder.setDisplayFlashColor(255, 255, 255);
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            this.mIsStorageError = true;
        }
    }
    
    class StatePhotoCaptureWaitForAfDone extends StatePhotoBase
    {
        private static final String TAG = "StateMachine.StatePhotoCaptureWaitForAfDone";
        private final boolean mIsDirectCaptureRequired;
        final StateMachine this$0;
        
        private StatePhotoCaptureWaitForAfDone(final StateMachine this$0, final boolean mIsDirectCaptureRequired) {
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE;
            this.mIsDirectCaptureRequired = mIsDirectCaptureRequired;
        }
        
        @Override
        public void handleOnAutoFocusDone(final Object... array) {
            this.this$0.doCapture(this.this$0.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            this.this$0.changeTo((State)new StatePhotoAfDone(false), array);
            this.this$0.changeTo((State)new StatePhotoCapture(), array);
        }
        
        @Override
        public void handleOnInitialAutoFocusDone(final Object... array) {
            if (this.mIsDirectCaptureRequired) {
                this.this$0.changeTo((State)new StatePhotoAfDone(false), array);
                this.this$0.mCameraDeviceHandler.preCapture();
                this.this$0.changeTo((State)new StatePhotoCapture(), array);
            }
            else {
                this.this$0.cancelAutoFocus(false);
                this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), array);
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.cancelAutoFocus(true);
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.mPredictiveApplier.attemptCommitSettings();
        }
    }
    
    class StatePhotoReadyForRecording extends StatePhotoBase
    {
        private final boolean mBySideSense;
        final StateMachine this$0;
        
        private StatePhotoReadyForRecording(final StateMachine this$0, final boolean mBySideSense) {
            this.mCaptureState = CaptureState.STATE_PHOTO_READY_FOR_RECORDING;
            this.mBySideSense = mBySideSense;
        }
        
        private void requestStartRecording() {
            PerfLog.START_REC.begin();
            if (!this.this$0.mStorage.canPushStoreRequest(this.this$0.getCurrentStorage())) {
                this.this$0.changeTo((State)this.this$0.new StateVideoReady(), new Object[0]);
                return;
            }
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                this.this$0.changeTo((State)this.this$0.new StateVideoReady(), new Object[0]);
                return;
            }
            this.this$0.pauseAudioPlaybackForRecord();
            this.this$0.mViewFinder.hideHudIcons();
            this.this$0.changeTo((State)new StatePrepareForRecording(), new Object[0]);
            this.this$0.mStartRecordingTask = this.this$0.new StartRecordingTask(this.mBySideSense);
            this.this$0.mHandler.post((Runnable)this.this$0.mStartRecordingTask);
            PerfLog.START_REC.end();
        }
        
        @Override
        public void handleCapture(final Object... array) {
            this.requestStartRecording();
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.this$0.changeTo((State)this.this$0.new StateVideoReady(), new Object[0]);
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStartRecording(final Object... array) {
            this.requestStartRecording();
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            final Storage.StorageType obj = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : type = ");
                sb.append(obj);
                sb.append(", state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            if (!this.this$0.checkSaveDestinationCanBeChange(this.this$0.getCurrentStorage())) {
                this.this$0.changeTo((State)new StateWarning(), array);
            }
        }
        
        @Override
        public void handleTriggerSlowMotion(final Object... array) {
            this.requestStartRecording();
        }
    }
    
    class StatePhotoWaitingTrackedObjectForAfStart extends State
    {
        boolean mIsAutoFocusStarted;
        boolean mIsBurstCaptureRequired;
        boolean mIsCaptureRequired;
        boolean mIsFirstCallback;
        final StateMachine this$0;
        
        private StatePhotoWaitingTrackedObjectForAfStart(final StateMachine this$0) {
            this.mIsAutoFocusStarted = false;
            this.mIsFirstCallback = true;
            this.mIsCaptureRequired = false;
            this.mIsBurstCaptureRequired = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START;
        }
        
        @Override
        public void handleCapture(final Object... array) {
            if (!this.mIsBurstCaptureRequired) {
                this.mIsCaptureRequired = true;
            }
        }
        
        @Override
        public void handleCaptureBurst(final Object... array) {
            if (!this.mIsCaptureRequired) {
                this.mIsBurstCaptureRequired = true;
            }
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            if (this.mIsAutoFocusStarted) {
                this.this$0.cancelAutoFocus(true);
            }
            this.this$0.changeTo((State)this.this$0.new StatePhotoReady(true), new Object[0]);
        }
        
        @Override
        public void handleClearFocus(final Object... array) {
            this.handleCaptureCancel(array);
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleOnObjectLost(final Object... array) {
            super.handleOnObjectLost(array);
            if (this.mIsCaptureRequired) {
                this.this$0.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
            }
            else if (this.mIsBurstCaptureRequired) {
                this.this$0.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                if (this.this$0.checkBurstConditions(true)) {
                    this.this$0.changeTo((State)new StateBurstCaptureWaitForAfDone(), array);
                }
                else {
                    this.this$0.changeTo((State)new StatePhotoAfSearch(), new Object[0]);
                }
            }
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
            if (!((CameraParameters.ObjectTrackingResult)array[0]).mIsLost && !this.mIsFirstCallback) {
                this.this$0.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                if (this.mIsCaptureRequired) {
                    this.this$0.changeTo((State)new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
                }
                else if (this.mIsBurstCaptureRequired) {
                    if (this.this$0.checkBurstConditions(true)) {
                        this.this$0.changeTo((State)new StateBurstCaptureWaitForAfDone(), array);
                    }
                    else {
                        this.this$0.changeTo((State)new StatePhotoAfSearch(), new Object[0]);
                    }
                }
                return;
            }
            this.mIsFirstCallback = false;
        }
        
        @Override
        public void handlePause(final Object... array) {
            if (this.mIsAutoFocusStarted) {
                this.this$0.cancelAutoFocus(true);
            }
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleStartAfAfterObjectTracked(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            if (this.mIsAutoFocusStarted) {
                return;
            }
            if (!PlatformCapability.isObjectTrackingSupported(this.this$0.getCurrentCameraId())) {
                return;
            }
            if (!this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                return;
            }
            final Rect rect = (Rect)array[1];
            this.this$0.doStopObjectTracking();
            this.this$0.doStartObjectTracking(rect);
            this.mIsAutoFocusStarted = false;
            this.mIsCaptureRequired = false;
            this.mIsBurstCaptureRequired = false;
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.mPredictiveApplier.attemptCommitSettings();
        }
    }
    
    class StatePrepareForRecording extends StateNone
    {
        final StateMachine this$0;
        
        private StatePrepareForRecording(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_PREPARE_FOR_RECORDING;
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
    }
    
    class StateResume extends State
    {
        private final FastCapture mFastCapture;
        private boolean mIsCameraStarted;
        private boolean mIsCurrentStorageReady;
        private boolean mIsEvfPrepared;
        private boolean mIsResumeSequenceStarted;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        private StartupAction mStartupAction;
        private Storage.StorageReadyStateListener mStorageReadyStateListener;
        final StateMachine this$0;
        
        private StateResume(final StateMachine this$0, final CameraDeviceHandler.CameraSessionId mSessionId, final FastCapture mFastCapture, final Evf evf, final StartupAction mStartupAction) {
            this.mStorageReadyStateListener = new Storage.StorageReadyStateListener() {
                final StateResume this$1;
                
                @Override
                public void onStorageReadyStateChanged(final StorageType obj, final StorageReadyState obj2) {
                    if (CamLog.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("invoke type:");
                        sb.append(obj);
                        sb.append(", state:");
                        sb.append(obj2);
                        sb.append(", getCurrentStorage:");
                        sb.append(this.this$1.this$0.getCurrentStorage());
                        CamLog.d(sb.toString());
                    }
                    if (obj2 != StorageReadyState.SUSPENDED) {
                        this.this$1.this$0.mStorage.addStorageStateListener((Storage.StorageStateListener)this.this$1.this$0.mStorageStateListener);
                    }
                    if (this.this$1.this$0.getCurrentStorage() == obj) {
                        this.this$1.this$0.mHandler.post((Runnable)new Runnable(this, obj, obj2) {
                            final StateMachine$StateResume$1 this$2;
                            final StorageReadyState val$state;
                            final StorageType val$type;
                            
                            @Override
                            public void run() {
                                this.this$2.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED, this.val$type, this.val$state);
                            }
                        });
                    }
                }
            };
            PerfLog.STATE_RESUME.begin();
            this.mCaptureState = CaptureState.STATE_RESUME;
            this.mSessionId = mSessionId;
            this.mFastCapture = mFastCapture;
            this.mIsResumeSequenceStarted = false;
            this.mIsCameraStarted = false;
            this.mIsCurrentStorageReady = this$0.mStorage.isStorageReadable(this$0.getCurrentStorage());
            this$0.mVirtualKeyEventDispatcher.start();
            this.mStartupAction = mStartupAction;
            if (evf != null) {
                this$0.mCameraDeviceHandler.setPreviewSurface(evf.asSurface());
                this.mIsEvfPrepared = true;
            }
            else {
                this.mIsEvfPrepared = false;
            }
            this$0.mIsSemiAutoEnabled = false;
        }
        
        private void moveStateIfCaptureReady() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke isStorageReady:");
                sb.append(this.mIsCurrentStorageReady);
                sb.append(", isCameraStarted:");
                sb.append(this.mIsCameraStarted);
                sb.append(", isEvfPrepared:");
                sb.append(this.mIsEvfPrepared);
                sb.append(", getExtraOperation():");
                sb.append(this.this$0.mActivity.getLaunchCondition().getExtraOperation());
                CamLog.d(sb.toString());
            }
            if (this.mIsCameraStarted && this.mIsEvfPrepared && this.mIsCurrentStorageReady) {
                this.this$0.initGeoTagManager();
                this.this$0.initSideSenseSetting();
                if (this.this$0.getCurrentCapturingMode().isFront() && this.this$0.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                    this.this$0.showBlackScreen();
                }
                this.this$0.mCameraDeviceHandler.startPreview();
                if (this.this$0.isFusionMonitoringNeeded()) {
                    this.this$0.mCameraDeviceHandler.startFusionMonitoring();
                }
                this.this$0.mActivity.reportFullyDrawnOnce();
                if (this.this$0.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE) == DestinationToSave.SDCARD) {
                    final Storage.StorageState currentState = this.this$0.mStorage.getCurrentState(Storage.StorageType.EXTERNAL_CARD);
                    final boolean access$700 = this.this$0.checkSaveDestinationCanBeChange(Storage.StorageType.EXTERNAL_CARD);
                    if (this.this$0.mStorage.getSdGrantedUri() == null && (currentState != Storage.StorageState.REMOVED || this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE)) {
                        this.this$0.mUserSettings.set(DestinationToSave.EMMC);
                        if (access$700) {
                            if (this.this$0.mViewFinder != null) {
                                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE);
                            }
                        }
                        else {
                            this.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, Storage.StorageType.INTERNAL, currentState);
                            this.this$0.mViewFinder.notifyStorageStateChanged(Storage.StorageType.INTERNAL, currentState, access$700, false);
                        }
                    }
                }
                if (!this.this$0.isStorageWritable(this.this$0.getCurrentStorage())) {
                    switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[this.this$0.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
                        case 2:
                        case 4: {
                            this.this$0.changeTo((State)new StateWaitingPreProcessDone(this.mFastCapture), new Object[0]);
                            return;
                        }
                        case 1:
                        case 3: {
                            this.this$0.cancelAutoFocus(false);
                            this.this$0.mCameraDeviceHandler.cancelPreProcessState();
                            break;
                        }
                    }
                }
                this.this$0.startFastCapture(this.mFastCapture, this.mStartupAction);
            }
        }
        
        private void startResuming() {
            if (this.this$0.mActivity.isOneShotVideo()) {
                this.this$0.mActivity.awaitSetupAllReady();
            }
            this.mIsResumeSequenceStarted = true;
            this.this$0.mViewFinder.showSurface();
            this.this$0.requestResizeEvf(this.this$0.mActivity.getLaunchCondition().getCapturingMode(), false);
            PerfLog.RESIZE_EVF.begin();
            if (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[this.this$0.mCameraDeviceHandler.getPreProcessState().ordinal()] == 1) {
                this.this$0.mCameraDeviceHandler.preCapture();
            }
            this.this$0.mHandler.postDelayed(this.this$0.mNotifyResumeTimeoutTask, 7000L);
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateResume");
            }
            super.entry();
            this.this$0.mStorageStateListener.resume();
            this.this$0.mViewFinder.setIsCameraSwitching(false);
            this.this$0.mLastPhotoSavingRequest = null;
            this.this$0.mLastVideoSavingRequest = null;
            this.this$0.mIsPausedAudioPlayback = false;
            this.mIsCurrentStorageReady = this.this$0.mStorage.isStorageReadable(this.this$0.getCurrentStorage());
            this.this$0.mStorage.addStorageReadyStateListener(this.mStorageReadyStateListener);
            if (this.this$0.hasRemainSavingRequest()) {
                this.this$0.mActivity.disableAutoPowerOffTimer();
                this.this$0.mViewFinder.showSavingProgressBar();
            }
            else {
                this.startResuming();
                this.moveStateIfCaptureReady();
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, this.this$0.isSettingChangeAcceptable());
        }
        
        @Override
        public void exit() {
            super.exit();
            this.this$0.mStorage.removeStorageReadyStateListener(this.mStorageReadyStateListener);
            this.this$0.mViewFinder.hideSavingProgressBar();
            this.this$0.mHandler.removeCallbacks(this.this$0.mNotifyResumeTimeoutTask);
            PerfLog.STATE_RESUME.end();
        }
        
        @Override
        public void handleCapture(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_CAPTURE, array);
        }
        
        @Override
        public void handleOnCameraDeviceClosed(final Object... array) {
            if (!this.this$0.hasRemainSavingRequest() && !this.mIsResumeSequenceStarted) {
                this.this$0.mActivity.enableAutoPowerOffTimer();
                this.startResuming();
            }
        }
        
        @Override
        public void handleOnCameraDeviceOpened(final Object... array) {
            if (array[0] == this.mSessionId) {
                this.mIsCameraStarted = true;
                this.moveStateIfCaptureReady();
            }
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            if (!this.mIsResumeSequenceStarted) {
                this.this$0.mViewFinder.requestCheckEvfPreparationRetrying();
                return;
            }
            PerfLog.RESIZE_EVF.end();
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.mIsEvfPrepared = true;
            LocalResearchUtil.getInstance().setSettingsValue(this.this$0.mUserSettings, this.this$0.mActivity.getLaunchCondition().getCapturingMode());
            this.moveStateIfCaptureReady();
        }
        
        @Override
        public void handleOnInitialAutoFocusDone(final Object... array) {
            this.this$0.mCameraDeviceHandler.preCapture();
        }
        
        @Override
        public void handleOnPreShutterDone(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, array);
        }
        
        @Override
        public void handleOnPreTakePictureDone(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, array);
        }
        
        @Override
        public void handleOnStorageReadyStateChanged(final Object... array) {
            this.mIsCurrentStorageReady = this.this$0.mStorage.isStorageReadable(this.this$0.getCurrentStorage());
            if (this.mIsCurrentStorageReady) {
                this.moveStateIfCaptureReady();
            }
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleResumeTimeout(final Object... array) {
            ResearchUtil.getInstance().setCameraNotAvailableFailedToOpen();
            CamLog.e("StateMachine", "Camera application resume is timed-out.");
            final StringBuilder sb = new StringBuilder();
            sb.append("  CameraDevice is ready:");
            sb.append(this.mIsCameraStarted);
            CamLog.e("StateMachine", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  Evf is ready:");
            sb2.append(this.mIsEvfPrepared);
            CamLog.e("StateMachine", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  Storage is ready:");
            sb3.append(this.mIsCurrentStorageReady);
            CamLog.e("StateMachine", sb3.toString());
            PlatformCapability.setDeviceError(true);
            this.this$0.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
            this.this$0.changeTo((State)new StateWarning(), new Object[0]);
        }
        
        @Override
        public void handleStartCaptureCountDown(final Object... array) {
            this.this$0.notifyDelayedEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, array);
        }
    }
    
    class StateVideoCaptureWhileRecording extends State
    {
        private final boolean mIsPaused;
        private boolean mIsPausingRequested;
        private boolean mIsReturnToVideoRecordingRequired;
        final StateMachine this$0;
        
        private StateVideoCaptureWhileRecording(final StateMachine this$0, final boolean b) {
            this.mIsReturnToVideoRecordingRequired = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_CAPTURE_WHILE_RECORDING;
            this.mIsPaused = b;
            this.mIsPausingRequested = b;
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnHeatedOverCritical(final Object... array) {
            if (this.this$0.mCameraDeviceHandler.isRecorderWorking()) {
                this.this$0.doStopRecording(false);
            }
            super.handleOnHeatedOverCritical(array);
        }
        
        @Override
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
            this.this$0.mChapterThumbnail = new ChapterThumbnail((byte[])array[0], (Integer)array[1], (Rect)array[2]);
            this.this$0.sendVideoChapterThumbnailToViewFinder();
        }
        
        @Override
        public void handleOnOrientationChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, array[0]);
        }
        
        @Override
        public void handleOnReachBatteryLimit(final Object... array) {
            this.this$0.doStopRecording(true);
            this.this$0.changeTo((State)new StateFatal(true, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
        
        @Override
        public void handleOnRecordingError(final Object... array) {
            this.this$0.doHandleRecordingError();
        }
        
        @Override
        public void handleOnShutterDone(final Object... array) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder)array[0];
            synchronized (this) {
                if (this.this$0.mContentsViewController != null) {
                    this.this$0.mContentsViewController.setClickThumbnailProgressListener(null);
                    ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setRequestId(this.this$0.mContentsViewController.createProvisionalContentFrame());
                }
                else {
                    ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setRequestId(-1);
                }
            }
        }
        
        @Override
        public void handleOnStoreRequested(final Object... array) {
            if (this.mIsPaused) {
                if (this.mIsPausingRequested) {
                    this.this$0.changeTo((State)new StateVideoRecordingPausing(), array);
                }
                else {
                    this.this$0.mCameraDeviceHandler.requestOnePreviewFrame();
                    this.this$0.doResumeRecording();
                }
            }
            else if (!this.mIsPausingRequested) {
                this.this$0.changeTo((State)this.this$0.new StateVideoRecording(this.mIsReturnToVideoRecordingRequired), array);
            }
            else {
                this.this$0.doPauseRecording();
            }
            if (this.mIsReturnToVideoRecordingRequired) {
                this.this$0.doStopRecording(false);
            }
        }
        
        @Override
        public void handleOnTakePictureDone(final Object... array) {
            this.this$0.requestStorePicture((RequestFactory.PhotoSavingRequestBuilder)array[0]);
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.pauseVideoRecording(array);
        }
        
        @Override
        public void handlePauseRecording(final Object... array) {
            this.mIsPausingRequested = true;
        }
        
        @Override
        public void handleResumeRecording(final Object... array) {
            this.mIsPausingRequested = false;
        }
        
        @Override
        public void handleStopRecording(final Object... array) {
            this.mIsReturnToVideoRecordingRequired = true;
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.this$0.doStopRecording(true);
        }
    }
    
    class StateVideoReady extends StateVideoBase
    {
        private static final String TAG = "StateMachine.StateVideoReady";
        private StartupAction mStartupAction;
        final StateMachine this$0;
        
        public StateVideoReady(final StateMachine stateMachine) {
            this(stateMachine, StartupAction.NONE);
        }
        
        public StateVideoReady(final StateMachine this$0, final StartupAction mStartupAction) {
            this.this$0 = this$0.super();
            this.mCaptureState = CaptureState.STATE_VIDEO_READY;
            this.mStartupAction = mStartupAction;
            if (this$0.getCurrentCapturingMode() == CapturingMode.SCENE_RECOGNITION) {
                this$0.setCurrentCapturingMode(CapturingMode.VIDEO);
            }
            this$0.sendResearchViewEvent();
        }
        
        @Override
        public void entry() {
            if (this.this$0.mActivity != null) {
                this.this$0.checkThermalWarning();
                this.this$0.mActivity.notifyStateBlockedToWearable();
            }
            if (!this.this$0.mPendingTaskListForStandby.isEmpty()) {
                final Iterator iterator = this.this$0.mPendingTaskListForStandby.iterator();
                while (iterator.hasNext()) {
                    this.this$0.mHandler.post((Runnable)iterator.next());
                }
                this.this$0.mPendingTaskListForStandby.clear();
            }
            if (PlatformCapability.hasDeviceError()) {
                return;
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.enableAutoPowerOffTimer();
            }
            if (!this.this$0.mViewFinder.isTouchFocus()) {
                this.this$0.switchVideoFaceDetection();
            }
            if (!this.this$0.mIsSemiAutoEnabled) {
                this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, this.this$0.getSensorOrientation());
            if (this.this$0.mViewFinder.isHeadUpDisplayReady() && this.this$0.isFusionMonitoringNeeded()) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, this.this$0.mCameraDeviceHandler.getLatestFusionResult());
            }
            final Storage.StorageType access$400 = this.this$0.getCurrentStorage();
            if (!this.this$0.isStorageWritable(access$400)) {
                this.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, access$400, this.this$0.mStorage.getCurrentState(access$400));
                return;
            }
            final IntentReader.VideoQualityConfigurations videoQualityConfigurations = this.this$0.mActivity.getLaunchCondition().getVideoQualityConfigurations();
            if (!this.this$0.isEnoughStorageSizeAvailableForOneShotVideo()) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, new Object[0]);
                return;
            }
            if (this.this$0.mActivity.isOneShot() && VideoSize.MMS == this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE) && !videoQualityConfigurations.isQualityLow()) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, new Object[0]);
                return;
            }
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.this$0.mUserSettings.get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                case 2:
                case 3: {
                    VideoSize videoSize = (VideoSize)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                    if (this.this$0.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                        final SlowMotion slowMotion = (SlowMotion)this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION);
                        videoSize = videoSize;
                        if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                            videoSize = slowMotion.getVideoSize();
                        }
                    }
                    final RecordingProfile build = new RecordingProfile.Builder().videoSize(videoSize).setOneShot(this.this$0.mActivity.isOneShot()).videoHdr((VideoHdr)this.this$0.mUserSettings.get(UserSettingKey.VIDEO_HDR)).build();
                    this.this$0.mCameraDeviceHandler.prepareRecorder(this.this$0.createVideoSavingRequest(build), this.this$0.mRecorderListener, this.this$0.mOnSuperSlowRecordingFinishedListener, this.this$0.shouldPlayShutterSound(), build, this.this$0.mStorage.createNotifier(this.this$0.getCurrentStorage(), 10));
                    break;
                }
            }
            if (!this.this$0.mIsSceneRecognitionValid) {
                this.this$0.notifySceneRecognitionDisabled();
            }
        }
        
        @Override
        public void handleChangeAngleStart(final Object... array) {
            this.this$0.doZoomChangeAngle();
        }
        
        @Override
        public void handleChangeSelectedFace(final Object... array) {
            this.this$0.doChangeSelectedFace((Point)array[0]);
        }
        
        @Override
        public void handleDeselectObjectPosition(final Object... array) {
            this.this$0.doStopObjectTracking();
        }
        
        @Override
        public void handleDialogOpened(final Object... array) {
            if (array != null && array.length != 0) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, array[0]);
            }
            this.this$0.changeTo((State)new StateOperationRestricted(), array);
            if (this.this$0.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                this.this$0.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }
        
        @Override
        public void handleKeyMenu(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
            this.this$0.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.SETTING_DIALOG);
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
        }
        
        @Override
        public void handleOnHeatedOverCoolingLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setLowPower();
        }
        
        @Override
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
            this.this$0.notifyCoolingUltraLow(this.this$0.isStorageFull(this.this$0.getCurrentStorage()) ^ true);
        }
        
        @Override
        public void handleOnHeatedOverWarningExtra(final Object... array) {
            this.this$0.mCameraDeviceHandler.enableFpsLimitation();
        }
        
        @Override
        public void handleOnLazyInitializationTaskRun(final Object... array) {
            if (this.mStartupAction == StartupAction.RECORD) {
                this.this$0.mCameraDeviceHandler.setOnPreviewStartedListener((CameraDeviceHandler.OnPreviewStartedListener)new OnPreviewStartedListenerImpl());
                CameraApplication.getUiThreadHandler().post((Runnable)new Runnable(this) {
                    final StateVideoReady this$1;
                    
                    @Override
                    public void run() {
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                    }
                });
            }
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handleOnPreviewStarted() {
            if (this.mStartupAction == StartupAction.RECORD) {
                this.this$0.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
            }
        }
        
        @Override
        public void handleOnSemiAutoDisabled(final Object... array) {
            super.handleOnSemiAutoDisabled(array);
            this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
            this.this$0.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            this.this$0.mCameraDeviceHandler.setBrightnessAndCommit(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(0);
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
            this.this$0.mUserSettings.set(DestinationToSave.EMMC);
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, false);
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleRecordReady(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                CamLog.w("ignore the event because device is not ready");
                return;
            }
            this.this$0.changeTo((State)new StatePhotoReadyForRecording(false), array != null && array.length != 0 && (boolean)array[0]);
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            if (array[0]) {
                this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                this.this$0.switchVideoFaceDetection();
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
            if (this.this$0.isTutorialNeededToBeShownForCurrentMode()) {
                this.this$0.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            }
            else if (this.this$0.mActivity != null && this.this$0.mActivity.getLaunchCondition().getLaunchCameraMode().isSlowMotion()) {
                this.this$0.mViewFinder.postSlowMotionHintText();
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.clearLaunchCameraMode();
            }
        }
        
        @Override
        public void handleRequestUpdateHighSensitivityFusionMode(final Object... array) {
            this.this$0.updateFusionModeSetting((FusionMode)array[0]);
        }
        
        @Override
        public void handleSetSelectedObjectPosition(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            final Rect rect = (Rect)array[1];
            if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.this$0.doStartObjectTracking(rect);
            }
        }
        
        @Override
        public void handleStartCaptureCountDown(final Object... array) {
            this.this$0.changeTo((State)new StateCaptureCountdown((Event.SelfTimerTrigger)getEventParam(array, 0, Event.SelfTimerTrigger.class, Event.SelfTimerTrigger.NORMAL)), true);
        }
        
        @Override
        public void handleStartRecording(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                CamLog.w("ignore the event because device is not ready");
                return;
            }
            if (!this.this$0.isLazyInitializationRunning()) {
                this.this$0.changeTo((State)new StatePhotoReadyForRecording(false), false);
                this.this$0.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
            }
        }
        
        @Override
        public void handleStartTransitionOperation(final Object... array) {
            if (((AnimationRequest)array[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                this.this$0.changeTo((State)new StateModeChanging(), new Object[0]);
            }
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            final Storage.StorageType obj = (Storage.StorageType)array[0];
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : type = ");
                sb.append(obj);
                sb.append(", state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                this.this$0.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                this.this$0.changeTo((State)new StateFatal(false, false), new Object[0]);
            }
            else if (!this.this$0.checkSaveDestinationCanBeChange(this.this$0.getCurrentStorage())) {
                this.this$0.changeTo((State)new StateWarning(), array);
            }
        }
        
        @Override
        public void handleSwitchCamera(final Object... array) {
            this.this$0.switchCamera((AnimationRequest.AnimationType)array[0]);
        }
    }
    
    class StateVideoRecording extends StateVideoBase
    {
        private static final String TAG = "StateMachine.StateVideoRecording";
        private boolean mAlreadyRequestStop;
        final StateMachine this$0;
        
        private StateVideoRecording(final StateMachine this$0) {
            this.this$0 = this$0.super();
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            this$0.mIsVideoRecording = true;
            this$0.sendVideoChapterThumbnailToViewFinder();
        }
        
        public StateVideoRecording(final StateMachine this$0, final boolean mAlreadyRequestStop) {
            this.this$0 = this$0.super();
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            this$0.sendVideoChapterThumbnailToViewFinder();
            this$0.mIsVideoRecording = true;
            this.mAlreadyRequestStop = mAlreadyRequestStop;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateVideoRecording");
            }
            super.entry();
            this.this$0.checkThermalWarning();
        }
        
        @Override
        public void handleCapture(final Object... array) {
            if (this.mAlreadyRequestStop) {
                return;
            }
            if (!this.this$0.mActivity.getLaunchCondition().getOneShotMode().isEnabled() && this.this$0.mCameraDeviceHandler.canRecorderTakeSnapshot()) {
                this.this$0.changeTo((State)new StateVideoCaptureWhileRecording(this.isPaused()), new Object[0]);
                this.mAlreadyRequestStop = true;
                this.this$0.doCaptureWhileRecording();
                ResearchUtil.getInstance().incrementCountSnapshotInRecording();
            }
        }
        
        @Override
        public void handleChangeSelectedFace(final Object... array) {
            this.this$0.doChangeSelectedFace((Point)array[0]);
        }
        
        @Override
        public void handleDeselectObjectPosition(final Object... array) {
            this.this$0.doStopObjectTracking();
        }
        
        @Override
        public void handleDialogOpened(final Object... array) {
            if (array != null && array.length != 0) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, array[0]);
            }
            this.this$0.changeTo((State)new StateOperationRestricted(), array);
        }
        
        @Override
        public void handleOnFaceDetected(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, array[0]);
            this.this$0.requestVideoSmileCapture();
        }
        
        @Override
        public void handleOnHeatedOverCoolingLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setLowPower();
        }
        
        @Override
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
            this.this$0.notifyCoolingUltraLow(this.this$0.isStorageFull(this.this$0.getCurrentStorage()) ^ true);
        }
        
        @Override
        public void handleOnHeatedOverCritical(final Object... array) {
            this.mAlreadyRequestStop = true;
            super.handleOnHeatedOverCritical(array);
        }
        
        @Override
        public void handleOnObjectTracked(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, array[0]);
        }
        
        @Override
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
            super.handleOnOnePreviewFrameUpdated(array);
            this.this$0.sendVideoChapterThumbnailToViewFinder();
        }
        
        @Override
        public void handleOnReachBatteryLimit(final Object... array) {
            this.mAlreadyRequestStop = true;
            this.this$0.doStopRecording(false);
            this.this$0.changeTo((State)new StateFatal(true, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.mAlreadyRequestStop = true;
            this.this$0.pauseVideoRecording(array);
        }
        
        @Override
        public void handlePauseRecording(final Object... array) {
            if (this.mAlreadyRequestStop) {
                return;
            }
            this.this$0.doPauseRecording();
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            if (array[0]) {
                this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                this.this$0.switchVideoFaceDetection();
            }
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING);
            this.this$0.sendVideoChapterThumbnailToViewFinder();
        }
        
        @Override
        public void handleSetSelectedObjectPosition(final Object... array) {
            if (!this.this$0.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                return;
            }
            final Rect rect = (Rect)array[1];
            if (this.this$0.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.this$0.doStartObjectTracking(rect);
            }
        }
        
        @Override
        public void handleStartRecording(final Object... array) {
        }
        
        @Override
        public void handleStopRecording(final Object... array) {
            this.mAlreadyRequestStop = true;
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), new Object[0]);
            this.this$0.doStopRecording(false);
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.mAlreadyRequestStop = true;
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), new Object[0]);
            this.this$0.doStopRecording(false);
        }
        
        protected boolean isPaused() {
            return false;
        }
    }
    
    class StateVideoRecordingPausing extends StateVideoRecording
    {
        final StateMachine this$0;
        
        private StateVideoRecordingPausing(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING_PAUSING;
        }
        
        @Override
        public void handleResumeRecording(final Object... array) {
            this.this$0.doResumeRecording();
            this.this$0.mCameraDeviceHandler.requestOnePreviewFrame();
        }
        
        @Override
        protected boolean isPaused() {
            return true;
        }
    }
    
    class StateVideoStopping extends StateVideoBase
    {
        private static final String TAG = "StateMachine.StateVideoStopping";
        private final RecorderController.Result mResult;
        final StateMachine this$0;
        
        public StateVideoStopping(final StateMachine stateMachine) {
            this(stateMachine, RecorderController.Result.SUCCESS);
        }
        
        public StateVideoStopping(final StateMachine this$0, final RecorderController.Result mResult) {
            this.this$0 = this$0.super();
            this.mCaptureState = CaptureState.STATE_VIDEO_STOPPING;
            this.mResult = mResult;
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(this.mResult), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
        }
        
        @Override
        public void handlePrepareZoom(final Object... array) {
        }
    }
    
    class StateVideoStore extends StateVideoBase
    {
        private final RecorderController.Result mResult;
        final StateMachine this$0;
        
        private StateVideoStore(final StateMachine stateMachine) {
            this(stateMachine, RecorderController.Result.SUCCESS);
        }
        
        private StateVideoStore(final StateMachine this$0, final RecorderController.Result mResult) {
            this.this$0 = this$0.super();
            this.mCaptureState = CaptureState.STATE_VIDEO_STORE;
            this.mResult = mResult;
        }
        
        @Override
        public void handleOnOnePreviewFrameUpdated(final Object... array) {
        }
        
        @Override
        public void handleOnStoreCompleted(final Object... array) {
            super.handleOnStoreCompleted(array);
            if (this.this$0.mActivity.isOneShot()) {
                this.this$0.onOneShotStoreCompleted((StoreDataResult)array[0]);
            }
        }
        
        @Override
        public void handleOnStoreRequested(final Object... array) {
            if (!this.this$0.mActivity.isOneShotVideo() || this.this$0.mActivity.isInLockTaskMode()) {
                this.this$0.changeTo((State)this.this$0.new StateVideoReady(), new Object[0]);
                switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[this.mResult.ordinal()]) {
                    case 2: {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED, new Object[0]);
                        break;
                    }
                    case 1: {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_DURATION_REACHED, new Object[0]);
                        break;
                    }
                }
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateWaitForHighFrameRateVideoRecordingDone extends StateVideoBase
    {
        private static final String TAG = "StateMachine.StateWaitForHighFrameRateVideoRecordingDone";
        final StateMachine this$0;
        
        public StateWaitForHighFrameRateVideoRecordingDone(final StateMachine this$0) {
            this.this$0 = this$0.super();
            this.mCaptureState = CaptureState.STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE;
        }
        
        @Override
        public void handleFinishZoom(final Object... array) {
        }
        
        @Override
        public void handleHighFameRateRecordingDone(final Object... array) {
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.this$0.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                case 3: {
                    this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), new Object[0]);
                    this.this$0.doStopRecording(false);
                    break;
                }
                case 2: {
                    this.this$0.changeTo((State)this.this$0.new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                    break;
                }
            }
        }
        
        @Override
        public void handleOnReachBatteryLimit(final Object... array) {
            this.this$0.doStopRecording(false);
            this.this$0.changeTo((State)new StateFatal(true, false), array);
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
        
        @Override
        public void handleOnVideoRecordingDone(final Object... array) {
            this.this$0.changeTo((State)new StateVideoStore(), array);
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.pauseVideoRecording(array);
        }
        
        @Override
        public void handlePerformZoom(final Object... array) {
        }
        
        @Override
        public void handlePrepareZoom(final Object... array) {
        }
        
        @Override
        public void handleStopRecordingSlowMotion(final Object... array) {
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
        
        @Override
        public void handleStorageError(final Object... array) {
            if (array[1] == Storage.StorageState.CORRUPT) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Storage corruption : state = ");
                sb.append(this.this$0.mCurrentState);
                CamLog.w(sb.toString());
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.this$0.changeTo((State)this.this$0.new StateVideoStopping(), array);
            this.this$0.doStopRecording(false);
        }
    }
    
    class StateWaitingEvfPreparedByModeChange extends State
    {
        final StateMachine this$0;
        
        private StateWaitingEvfPreparedByModeChange(final StateMachine this$0) {
            this.mCaptureState = CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE;
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
            if (this.this$0.isTutorialNeededToBeShownForCurrentMode()) {
                this.this$0.changeTo((State)new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            }
            else {
                this.this$0.changeToStandby();
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateWaitingPreProcessDone extends State
    {
        private final FastCapture mFastCapture;
        final StateMachine this$0;
        
        private StateWaitingPreProcessDone(final StateMachine this$0, final FastCapture mFastCapture) {
            this.mCaptureState = CaptureState.STATE_WAITING_PRE_PROCESS_DONE;
            this.mFastCapture = mFastCapture;
        }
        
        private void cancelPreProcess() {
            this.this$0.cancelAutoFocus(false);
            this.this$0.mCameraDeviceHandler.cancelPreProcessState();
            this.this$0.startFastCapture(this.mFastCapture, StartupAction.NONE);
        }
        
        @Override
        public void handleFusionConditionChanged(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, array);
        }
        
        @Override
        public void handleOnInitialAutoFocusDone(final Object... array) {
            this.cancelPreProcess();
        }
        
        @Override
        public void handleOnPreTakePictureDone(final Object... array) {
            ((RequestFactory.PhotoSavingRequestBuilder)array[0]).close();
            this.this$0.mCameraDeviceHandler.prepareCaptureImageReader(null);
            this.cancelPreProcess();
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
            this.this$0.mUserSettings.set(DestinationToSave.EMMC);
            if (this.this$0.mViewFinder != null) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, true);
            }
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
    }
    
    class StateWarning extends StatePhotoBase
    {
        final StateMachine this$0;
        
        private StateWarning(final StateMachine this$0) {
            this$0.mIsVideoRecording = false;
            this$0.checkThermalWarning();
            this$0.cleanupPendingState();
            this.mCaptureState = CaptureState.STATE_WARNING;
        }
        
        @Override
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateWarning");
            }
            this.this$0.mPredictiveApplier.entrySuppressor(this);
        }
        
        @Override
        public void exit() {
            this.this$0.mViewFinder.clearHintText();
            this.this$0.mPredictiveApplier.leaveSuppressor(this);
        }
        
        @Override
        public void handleCapture(final Object... array) {
            this.this$0.mViewFinder.onCaptureDone();
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
        }
        
        @Override
        public void handleCaptureCancel(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL, new Object[0]);
        }
        
        @Override
        public void handleChangeAngleStart(final Object... array) {
            this.this$0.doZoomChangeAngle();
        }
        
        @Override
        public void handleDialogOpened(final Object... array) {
            if (array != null && array.length != 0) {
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, array[0]);
            }
            this.this$0.changeTo((State)new StateOperationRestricted(), array);
            this.this$0.mActivity.notifyStateBlockedToWearable();
            if (this.this$0.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                this.this$0.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }
        
        @Override
        public void handleKeyMenu(final Object... array) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
        }
        
        @Override
        public void handleOnAmberBlueColorChanged(final Object... array) {
            this.this$0.updateAmberBlueColor((float)array[0]);
        }
        
        @Override
        public void handleOnBrightnessChanged(final Object... array) {
            this.this$0.updateBrightness((float)array[0]);
        }
        
        @Override
        public void handleOnEvfPrepared(final Object... array) {
            this.this$0.mCameraDeviceHandler.setPreviewSurface(((Evf)array[0]).asSurface());
            this.this$0.mCameraDeviceHandler.startPreview();
        }
        
        @Override
        public void handleOnHeatedOverCoolingLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setLowPower();
        }
        
        @Override
        public void handleOnHeatedOverCoolingUltraLow(final Object... array) {
            this.this$0.mCameraDeviceHandler.setUltraLowPower();
        }
        
        @Override
        public void handleOnHeatedOverWarningExtra(final Object... array) {
            this.this$0.mCameraDeviceHandler.enableFpsLimitation();
        }
        
        @Override
        public void handleOnStorageUngranted(final Object... array) {
        }
        
        @Override
        public void handlePause(final Object... array) {
            this.this$0.changeTo((State)new StatePause((boolean)array[0]), array);
        }
        
        @Override
        public void handleRequestSetupHeadUpDisplay(final Object... array) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke current");
                sb.append(this.this$0.getCurrentCapturingMode());
                CamLog.d(sb.toString());
            }
            if (array[0]) {
                this.this$0.switchSceneRecognition(this.this$0.isStorageWritable(this.this$0.getCurrentStorage()));
                this.this$0.switchVideoFaceDetection();
            }
            switch (StateMachine$8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.this$0.getCurrentCapturingMode().ordinal()]) {
                case 3:
                case 7:
                case 8: {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
                    break;
                }
                case 1:
                case 2:
                case 5:
                case 6: {
                    this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
                    break;
                }
            }
        }
        
        @Override
        public void handleRequestUpdateHighSensitivityFusionMode(final Object... array) {
            this.this$0.updateFusionModeSetting((FusionMode)array[0]);
        }
        
        @Override
        public void handleStartTransitionOperation(final Object... array) {
            if (((AnimationRequest)array[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                this.this$0.changeTo((State)new StateModeChanging(), new Object[0]);
            }
        }
        
        @Override
        public void handleStorageMounted(final Object... array) {
            this.this$0.changeToStandby();
        }
        
        @Override
        public void handleSwitchCamera(final Object... array) {
            this.this$0.switchCamera((AnimationRequest.AnimationType)array[0]);
        }
    }
    
    public enum StaticEvent
    {
        private static final StaticEvent[] $VALUES;
        
        EVENT_ON_FACE_DETECTED, 
        EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED, 
        EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, 
        EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, 
        EVENT_ON_OBJECT_TRACKED, 
        EVENT_ON_OBJECT_TRACKING_LOST, 
        EVENT_ON_ORIENTATION_CHANGED, 
        EVENT_ON_PHOTO_STACK_INITIALIZED, 
        EVENT_ON_PREVIEW_STARTED, 
        EVENT_ON_SCENE_MODE_CHANGED, 
        EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED;
        
        static {
            $VALUES = new StaticEvent[] { StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED, StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, StaticEvent.EVENT_ON_SCENE_MODE_CHANGED, StaticEvent.EVENT_ON_FACE_DETECTED, StaticEvent.EVENT_ON_OBJECT_TRACKED, StaticEvent.EVENT_ON_ORIENTATION_CHANGED, StaticEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, StaticEvent.EVENT_ON_OBJECT_TRACKING_LOST, StaticEvent.EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED, StaticEvent.EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED, StaticEvent.EVENT_ON_PREVIEW_STARTED };
        }
    }
    
    private class StorageReadyStateAdapter implements StorageReadyStateListener
    {
        final StateMachine this$0;
        
        private StorageReadyStateAdapter(final StateMachine this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onStorageReadyStateChanged(final StorageType storageType, final StorageReadyState storageReadyState) {
            if (storageReadyState == StorageReadyState.COMPLETED && storageType == StorageType.EXTERNAL_CARD && this.this$0.mIsSdPermissionFinished) {
                this.this$0.mIsSdPermissionFinished = false;
                this.this$0.mHandler.post((Runnable)new Runnable(this, storageType) {
                    final StorageReadyStateAdapter this$1;
                    final StorageType val$type;
                    
                    @Override
                    public void run() {
                        if (this.this$1.this$0.mStorage.getCurrentState(this.val$type).isWritable()) {
                            final DestinationToSave destinationToSave = (DestinationToSave)this.this$1.this$0.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE);
                            this.this$1.this$0.mUserSettings.set(DestinationToSave.SDCARD);
                            if (destinationToSave != DestinationToSave.SDCARD) {
                                this.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED, new Object[0]);
                            }
                            if (this.this$1.this$0.mViewFinder != null) {
                                this.this$1.this$0.mViewFinder.showHiSpeedSdCardRecommendDialogOnDestinationChange();
                            }
                        }
                    }
                });
            }
        }
    }
    
    private class StorageStateAdapter implements StorageStateListener
    {
        private StorageState mOldStorageState;
        final StateMachine this$0;
        
        private StorageStateAdapter(final StateMachine this$0) {
            this.this$0 = this$0;
            this.mOldStorageState = StorageState.AVAILABLE;
        }
        
        private void resume() {
            this.mOldStorageState = StorageState.AVAILABLE;
            this.updateGestureShutterState(true);
        }
        
        private void updateGestureShutterState(final boolean enabled) {
            this.this$0.mGestureShutter.setEnabled(enabled);
        }
        
        @Override
        public void onStorageSizeChanged(final StorageType storageType, final long n) {
        }
        
        @Override
        public void onStorageStateChanged(final StorageType obj, final StorageState obj2, final StorageReadyState obj3) {
            synchronized (this) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke type:");
                    sb.append(obj);
                    sb.append(", state:");
                    sb.append(obj2);
                    sb.append(", readyState:");
                    sb.append(obj3);
                    CamLog.d(sb.toString());
                }
                if (obj3 != StorageReadyState.ACCESSIBLE && obj3 != StorageReadyState.COMPLETED) {
                    return;
                }
                CameraApplication.getUiThreadHandler().post((Runnable)new StorageStateChangeTask(obj, obj2));
            }
        }
        
        private class StorageStateChangeTask implements Runnable
        {
            private final StorageState mStorageState;
            private final StorageType mStorageType;
            final StorageStateAdapter this$1;
            
            private StorageStateChangeTask(@NonNull final StorageStateAdapter this$1, @NonNull final StorageType mStorageType, final StorageState mStorageState) {
                this.this$1 = this$1;
                this.mStorageType = mStorageType;
                this.mStorageState = mStorageState;
            }
            
            @Override
            public void run() {
                final StorageType access$400 = this.this$1.this$0.getCurrentStorage();
                final LaunchCondition.ExtraOperation extraOperation = this.this$1.this$0.mActivity.getLaunchCondition().getExtraOperation();
                final Storage.StorageState currentState = this.this$1.this$0.mStorage.getCurrentState(access$400);
                final StorageType mStorageType = this.mStorageType;
                boolean b = false;
                if (mStorageType == access$400 && (this.mStorageState.isWritable() || currentState.isWritable())) {
                    this.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_MOUNTED, new Object[0]);
                }
                else {
                    if (this.mStorageType == StorageType.EXTERNAL_CARD && this.mStorageState == StorageState.CORRUPT) {
                        this.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, this.mStorageType, this.mStorageState);
                        return;
                    }
                    if (this.mStorageType == StorageType.EXTERNAL_CARD && this.mStorageState == StorageState.UNGRANTED) {
                        if (access$400 == StorageType.EXTERNAL_CARD) {
                            this.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_UNGRANTED, new Object[0]);
                        }
                    }
                    else if (extraOperation != LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU && this.mStorageType == access$400) {
                        final boolean b2 = this.this$1.this$0.checkSaveDestinationCanBeChange(access$400) && !this.this$1.this$0.mActivity.isOneShot();
                        if (this.this$1.this$0.mIsSdPermissionFinished && access$400 != StorageType.EXTERNAL_CARD && b2) {
                            return;
                        }
                        if (this.this$1.mOldStorageState != null && this.this$1.mOldStorageState.isWritable()) {
                            if (access$400 != StorageType.EXTERNAL_CARD || extraOperation != LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE || this.this$1.this$0.isStorageWritable(StorageType.EXTERNAL_CARD) || !b2) {
                                this.this$1.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, this.mStorageType, this.mStorageState);
                                if (this.this$1.this$0.mCurrentState.getCaptureState() == CaptureState.STATE_BURST_CAPTURE || this.this$1.this$0.mCurrentState.getCaptureState() == CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE) {
                                    b = true;
                                }
                                this.this$1.this$0.mViewFinder.notifyStorageStateChanged(access$400, this.mStorageState, b2, b);
                            }
                        }
                    }
                }
                if (this.mStorageType == access$400) {
                    this.this$1.mOldStorageState = this.mStorageState;
                    this.this$1.this$0.switchSceneRecognition(this.mStorageState.isWritable());
                    if (!this.mStorageState.isWritable()) {
                        this.this$1.this$0.doStopObjectTracking();
                    }
                    this.this$1.updateGestureShutterState(this.mStorageState.isWritable());
                }
            }
        }
    }
    
    private class SuitablePredictiveApplier
    {
        private boolean isSuppressed;
        private ArrayList<Object> mSuppressors;
        final StateMachine this$0;
        
        private SuitablePredictiveApplier(final StateMachine this$0) {
            this.this$0 = this$0;
            this.isSuppressed = false;
        }
        
        private void attemptCommitSettings() {
            if (this.mSuppressors != null && !this.mSuppressors.isEmpty()) {
                if (!this.isSuppressed) {
                    this.this$0.mCameraDeviceHandler.setPredictiveCaptureAndCommit(PredictiveCapture.OFF);
                }
                this.isSuppressed = true;
            }
            else {
                final Storage.StorageState currentState = this.this$0.mStorage.getCurrentState(this.this$0.getCurrentStorage());
                PredictiveCapture off = PredictiveCapture.OFF;
                if (currentState == Storage.StorageState.AVAILABLE) {
                    if (this.this$0.mUserSettings.get(UserSettingKey.SELF_TIMER) != SelfTimer.OFF) {
                        off = off;
                    }
                    else {
                        off = (PredictiveCapture)this.this$0.mUserSettings.get(UserSettingKey.PREDICTIVE_CAPTURE);
                    }
                }
                this.this$0.mCameraDeviceHandler.setPredictiveCaptureAndCommit(off);
                this.isSuppressed = false;
            }
        }
        
        private void entrySuppressor(final Object o) {
            if (this.this$0.mUserSettings.get(UserSettingKey.PREDICTIVE_CAPTURE) == PredictiveCapture.OFF) {
                return;
            }
            if (this.mSuppressors == null) {
                this.mSuppressors = new ArrayList<Object>();
            }
            if (!this.mSuppressors.contains(o)) {
                this.mSuppressors.add(o);
                this.attemptCommitSettings();
            }
        }
        
        private void leaveSuppressor(final Object o) {
            if (this.mSuppressors == null) {
                return;
            }
            if (Boolean.valueOf(this.mSuppressors.remove(o)) != null) {
                this.attemptCommitSettings();
            }
        }
    }
    
    public enum TransitterEvent
    {
        private static final TransitterEvent[] $VALUES;
        
        EVENT_ANGLE_CHANGE_COMPLETED, 
        EVENT_ANGLE_CHANGE_START, 
        EVENT_CANCEL_TOUCHED_POSITION, 
        EVENT_CAPTURE, 
        EVENT_CAPTURE_BURST, 
        EVENT_CAPTURE_CANCEL, 
        EVENT_CAPTURE_READY, 
        EVENT_CHANGE_CAPTURING_MODE, 
        EVENT_CHANGE_SELECTED_FACE, 
        EVENT_CLEAR_FOCUS, 
        EVENT_DESELECT_OBJECT_POSITION, 
        EVENT_DIALOG_CLOSED, 
        EVENT_DIALOG_OPENED, 
        EVENT_FINALIZE, 
        EVENT_FINISH_TRANSITION_OPERATION, 
        EVENT_HIGH_FRAME_RATE_RECORDING_DONE, 
        EVENT_INITIALIZE, 
        EVENT_KEY_MENU, 
        EVENT_ON_AMBER_BLUE_COLOR_CHANGED, 
        EVENT_ON_AUTO_FOCUS_DONE, 
        EVENT_ON_BATTERY_LEVEL_CHANGED, 
        EVENT_ON_BRIGHTNESS_CHANGED, 
        EVENT_ON_BURST_GROUP_STORE_COMPLETED, 
        EVENT_ON_BURST_SHUTTER_DONE, 
        EVENT_ON_BURST_STORE_COMPLETED, 
        EVENT_ON_CAMERA_DEVICE_CLOSED, 
        EVENT_ON_CAMERA_DEVICE_OPENED, 
        EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED, 
        EVENT_ON_DEVICE_ERROR, 
        EVENT_ON_EVF_PREPARATION_FAILED, 
        EVENT_ON_EVF_PREPARED, 
        EVENT_ON_FUSION_CONDITION_CHANGED, 
        EVENT_ON_HEATED_OVER_COOLING_LOW, 
        EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, 
        EVENT_ON_HEATED_OVER_CRITICAL, 
        EVENT_ON_HEATED_OVER_NORMAL, 
        EVENT_ON_HEATED_OVER_WARNING, 
        EVENT_ON_HEATED_OVER_WARNING_EXTRA, 
        EVENT_ON_INITIAL_AUTO_FOCUS_DONE, 
        EVENT_ON_ONE_PREVIEW_FRAME_UPDATED, 
        EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED, 
        EVENT_ON_PREPARE_BURST_DONE, 
        EVENT_ON_PRE_SHUTTER_DONE, 
        EVENT_ON_PRE_TAKE_PICTURE_DONE, 
        EVENT_ON_REACH_BATTERY_LIMIT, 
        EVENT_ON_REACH_BATTERY_LOW, 
        EVENT_ON_RECORDING_ERROR, 
        EVENT_ON_RECORDING_START_WAIT_DONE, 
        EVENT_ON_SEMIAUTO_DISABLED, 
        EVENT_ON_SEMIAUTO_ENABLED, 
        EVENT_ON_SHUTTER_DONE, 
        EVENT_ON_STORE_COMPLETED, 
        EVENT_ON_STORE_REQUESTED, 
        EVENT_ON_SWITCH_CAMERA, 
        EVENT_ON_TAKE_PICTURE_DONE, 
        EVENT_ON_VIDEO_RECORDING_DONE, 
        EVENT_PAUSE, 
        EVENT_PAUSE_RECORDING, 
        EVENT_RECORD_READY, 
        EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, 
        EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, 
        EVENT_RESUME, 
        EVENT_RESUME_RECORDING, 
        EVENT_RESUME_TIMEOUT, 
        EVENT_SELFTIMER_CANCEL, 
        EVENT_SET_SELECTED_OBJECT_POSITION, 
        EVENT_SET_TOUCHED_POSITION, 
        EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END, 
        EVENT_START_AF_AFTER_OBJECT_TRACKED, 
        EVENT_START_CAPTURE_COUNTDOWN, 
        EVENT_START_RECORDING, 
        EVENT_START_TRANSITION_OPERATION, 
        EVENT_STOP_RECORDING, 
        EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE, 
        EVENT_STORAGE_ERROR, 
        EVENT_STORAGE_MOUNTED, 
        EVENT_STORAGE_READY_STATE_CHANGED, 
        EVENT_STORAGE_UNGRANTED, 
        EVENT_TOUCH_CONTENT_PROGRESS, 
        EVENT_TRIGGER_SLOW_MOTION, 
        EVENT_ZOOM_FINISH, 
        EVENT_ZOOM_PERFORM, 
        EVENT_ZOOM_PREPARE;
        
        static {
            $VALUES = new TransitterEvent[] { TransitterEvent.EVENT_INITIALIZE, TransitterEvent.EVENT_RESUME, TransitterEvent.EVENT_RESUME_TIMEOUT, TransitterEvent.EVENT_PAUSE, TransitterEvent.EVENT_FINALIZE, TransitterEvent.EVENT_ON_HEATED_OVER_WARNING, TransitterEvent.EVENT_ON_HEATED_OVER_WARNING_EXTRA, TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, TransitterEvent.EVENT_ON_HEATED_OVER_CRITICAL, TransitterEvent.EVENT_ON_HEATED_OVER_NORMAL, TransitterEvent.EVENT_ON_REACH_BATTERY_LOW, TransitterEvent.EVENT_ON_REACH_BATTERY_LIMIT, TransitterEvent.EVENT_ON_BATTERY_LEVEL_CHANGED, TransitterEvent.EVENT_ON_EVF_PREPARED, TransitterEvent.EVENT_ON_EVF_PREPARATION_FAILED, TransitterEvent.EVENT_ON_INITIAL_AUTO_FOCUS_DONE, TransitterEvent.EVENT_ON_AUTO_FOCUS_DONE, TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, TransitterEvent.EVENT_ON_SHUTTER_DONE, TransitterEvent.EVENT_ON_PREPARE_BURST_DONE, TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, TransitterEvent.EVENT_ON_TAKE_PICTURE_DONE, TransitterEvent.EVENT_ON_VIDEO_RECORDING_DONE, TransitterEvent.EVENT_ON_ONE_PREVIEW_FRAME_UPDATED, TransitterEvent.EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED, TransitterEvent.EVENT_ON_SWITCH_CAMERA, TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED, TransitterEvent.EVENT_ON_BURST_SHUTTER_DONE, TransitterEvent.EVENT_ON_BURST_STORE_COMPLETED, TransitterEvent.EVENT_ON_BURST_GROUP_STORE_COMPLETED, TransitterEvent.EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED, TransitterEvent.EVENT_ON_STORE_REQUESTED, TransitterEvent.EVENT_ON_STORE_COMPLETED, TransitterEvent.EVENT_STORAGE_ERROR, TransitterEvent.EVENT_STORAGE_MOUNTED, TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED, TransitterEvent.EVENT_STORAGE_UNGRANTED, TransitterEvent.EVENT_KEY_MENU, TransitterEvent.EVENT_SET_TOUCHED_POSITION, TransitterEvent.EVENT_CANCEL_TOUCHED_POSITION, TransitterEvent.EVENT_CHANGE_SELECTED_FACE, TransitterEvent.EVENT_SET_SELECTED_OBJECT_POSITION, TransitterEvent.EVENT_DESELECT_OBJECT_POSITION, TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED, TransitterEvent.EVENT_CLEAR_FOCUS, TransitterEvent.EVENT_TOUCH_CONTENT_PROGRESS, TransitterEvent.EVENT_START_TRANSITION_OPERATION, TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, TransitterEvent.EVENT_SELFTIMER_CANCEL, TransitterEvent.EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE, TransitterEvent.EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END, TransitterEvent.EVENT_HIGH_FRAME_RATE_RECORDING_DONE, TransitterEvent.EVENT_ANGLE_CHANGE_START, TransitterEvent.EVENT_ANGLE_CHANGE_COMPLETED, TransitterEvent.EVENT_CHANGE_CAPTURING_MODE, TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, TransitterEvent.EVENT_ON_RECORDING_START_WAIT_DONE, TransitterEvent.EVENT_ON_RECORDING_ERROR, TransitterEvent.EVENT_DIALOG_OPENED, TransitterEvent.EVENT_DIALOG_CLOSED, TransitterEvent.EVENT_ON_SEMIAUTO_ENABLED, TransitterEvent.EVENT_ON_SEMIAUTO_DISABLED, TransitterEvent.EVENT_ON_AMBER_BLUE_COLOR_CHANGED, TransitterEvent.EVENT_ON_BRIGHTNESS_CHANGED, TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, TransitterEvent.EVENT_ON_CAMERA_DEVICE_OPENED, TransitterEvent.EVENT_ON_CAMERA_DEVICE_CLOSED, TransitterEvent.EVENT_ON_DEVICE_ERROR, TransitterEvent.EVENT_CAPTURE_READY, TransitterEvent.EVENT_CAPTURE, TransitterEvent.EVENT_CAPTURE_CANCEL, TransitterEvent.EVENT_CAPTURE_BURST, TransitterEvent.EVENT_RECORD_READY, TransitterEvent.EVENT_START_RECORDING, TransitterEvent.EVENT_STOP_RECORDING, TransitterEvent.EVENT_RESUME_RECORDING, TransitterEvent.EVENT_PAUSE_RECORDING, TransitterEvent.EVENT_TRIGGER_SLOW_MOTION, TransitterEvent.EVENT_ZOOM_PREPARE, TransitterEvent.EVENT_ZOOM_PERFORM, TransitterEvent.EVENT_ZOOM_FINISH };
        }
    }
}
