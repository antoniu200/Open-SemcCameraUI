package com.sonyericsson.android.camera.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.controller.GestureShutter;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.controller.selftimerfeedback.LedLight;
import com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.parameter.dependency.DependencyCheckUtil;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.LastSettings;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.view.angle.VariableIndex;
import com.sonyericsson.android.camera.view.animation.AnimationRequest;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.PredictiveCaptureStoreInfo;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import com.sonyericsson.cameracommon.intent.IntentConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.cameracommon.status.eachcamera.SlowMotion;
import com.sonyericsson.cameracommon.status.eachcamera.VideoRecordingFps;
import com.sonyericsson.cameracommon.status.eachcamera.VideoResolution;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;
import com.sonyericsson.cameracommon.storage.PredictiveCapturePathBuilder;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.storage.VideoSavingRequest;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.evf.Evf;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StateMachine {
    private static final int COLOR_VALUE_MAX = 255;
    private static final int RESUME_TIMEOUT = 7000;
    private static final String TAG = "StateMachine";
    private static final int TERMINATE_WAIT_TIME = 4000;
    private static final String THREAD_NAME = "RequestStore";
    private static final boolean USE_PROGRESS_ON_PHOTO_THUMBNAIL = false;
    private static final boolean USE_PROGRESS_ON_VIDEO_THUMBNAIL = false;
    private final CameraActivity mActivity;
    private CameraDeviceHandler mCameraDeviceHandler;
    private State mCurrentState;
    private boolean mIsSemiAutoEnabled;
    private final LastSettings mLastSettings;
    private long mLastSmileCaptureTakenTime;
    private ObjectTrackingManager mObjectTracking;
    private SuitablePredictiveApplier mPredictiveApplier;
    private PredictiveCaptureStoreInfo mPredictiveCaptureStoreInfo;
    private final UserSettingApplicable mSettingController;
    private Storage mStorage;
    private Storage.StorageReadyStateListener mStorageReadyStateListener;
    private StorageStateAdapter mStorageStateListener;
    private final UserSettings mUserSettings;
    private ViewFinder mViewFinder;
    private final Handler mHandler = new Handler();
    private RequestFactory.PhotoSavingRequestBuilder mLastPhotoSavingRequest = null;
    private RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest = null;
    private ContentsViewController mContentsViewController = null;
    private final ExecutorService mExecService = ThreadUtil.buildExecutor(THREAD_NAME);
    private boolean mIsVideoRecording = false;
    private final List<RequestFactory.PhotoSavingRequestBuilder> mPhotoSavingRequestList = new ArrayList();
    private int mHighFrameRateVideoRecordingCountInSuperSlowMotion = 0;
    private StoreDataResult mLastStoreDataResult = null;
    private ChangeCameraModeTask mChangeCameraModeTask = null;
    private StartRecordingTask mStartRecordingTask = null;
    private final List<Runnable> mPendingTaskListForStandby = new ArrayList();
    private final UserEventHandler.VirtualKeyEventDispatcher mVirtualKeyEventDispatcher = new UserEventHandler.VirtualKeyEventDispatcher();
    boolean mIsSceneRecognitionValid = false;
    private boolean mIsSdPermissionFinished = false;
    private Storage.OnStoreCompletedListener mOnStoreCompletedListener = new Storage.OnStoreCompletedListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.1
        @Override // com.sonyericsson.cameracommon.storage.Storage.OnStoreCompletedListener
        public void onStoreCompleted(Uri uri, SavingRequest savingRequest, Storage.StorageType storageType) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke uri:" + uri + ", request:" + savingRequest + ", savedStorage:" + storageType);
            }
            notifyResult(new StoreDataResult(MediaSavingResult.SUCCESS, uri, savingRequest));
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.OnStoreCompletedListener
        public void onStoreFailed(Uri uri, SavingRequest savingRequest, int i) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke uri:" + uri + ", request:" + savingRequest + ", reason:" + i);
            }
            notifyResult(new StoreDataResult(MediaSavingResult.FAIL, uri, savingRequest));
        }

        private void notifyResult(final StoreDataResult storeDataResult) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, Boolean.valueOf(StateMachine.this.isSettingChangeAcceptable()));
            StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.1.1
                @Override // java.lang.Runnable
                public void run() {
                    StateMachine.this.calculateRemainStorage();
                    StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_STORE_COMPLETED, storeDataResult);
                }
            });
        }
    };
    private final GestureShutter.ControllerHost mGestureShutterHost = new GestureShutter.ControllerHost() { // from class: com.sonyericsson.android.camera.controller.StateMachine.3
        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public void prepareGestureShutterCountDown() {
            StateMachine.this.mViewFinder.prepareGestureShutterCountDown();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public void startGestureShutterCountDown() {
            if (StateMachine.this.mViewFinder.isMessageDialogOpened() || StateMachine.this.mViewFinder.isSwitchingAnimationProgress() || StateMachine.this.isLazyInitializationRunning() || !StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                return;
            }
            StateMachine.this.sendEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.GESTURE);
            StateMachine.this.mActivity.restartAutoPowerOffTimer();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public void resetGestureShutterCountDown() {
            StateMachine.this.updatePhotoSelftimer((SelfTimer) StateMachine.this.mUserSettings.get(UserSettingKey.SELF_TIMER));
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public void removeOrientationListener(CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener) {
            StateMachine.this.mActivity.removeOrienationListener(layoutOrientationChangedListener);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public CameraActivity.LayoutOrientation getLayoutOrientation() {
            return StateMachine.this.mActivity.getLastDetectedOrientation();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.ControllerHost
        public void addOrientationListener(CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener) {
            StateMachine.this.mActivity.addOrienationListener(layoutOrientationChangedListener);
        }
    };
    private final Set<OnStateChangedListener> mOnStateChangedListenerSet = new CopyOnWriteArraySet();
    private final Runnable mNotifyResumeTimeoutTask = new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.4
        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke NotifyResumeTimeoutTask");
            }
            StateMachine.this.sendEvent(TransitterEvent.EVENT_RESUME_TIMEOUT, new Object[0]);
        }
    };
    private ChapterThumbnail mChapterThumbnail = null;
    private final RecorderController.RecorderListener mRecorderListener = new RecorderController.RecorderListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.5
        private RequestFactory.VideoSavingRequestBuilder mVideoSavingRequestBuilder = null;

        @Override // com.sonyericsson.android.camera.recorder.RecorderController.RecorderListener
        public void setSavingRequestBuilder(RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke");
            }
            this.mVideoSavingRequestBuilder = videoSavingRequestBuilder;
        }

        @Override // com.sonyericsson.android.camera.recorder.RecorderController.RecorderListener
        public void onRecordProgress(long j) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke progressMillis:" + j);
            }
            final int i = (int) j;
            StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.5.1
                @Override // java.lang.Runnable
                public void run() {
                    StateMachine.this.updateRecordingProgress(i);
                }
            });
        }

        @Override // com.sonyericsson.android.camera.recorder.RecorderController.RecorderListener
        public void onRecordError(int i, int i2) {
            CamLog.e("ERROR:MediaRecorder (" + i + ", " + i2 + ")");
            StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.5.2
                @Override // java.lang.Runnable
                public void run() {
                    if (StateMachine.this.isCurrentStorageExternal() && !StateMachine.this.isStorageWritable(Storage.StorageType.EXTERNAL_CARD)) {
                        StateMachine.this.mViewFinder.showMessageDialog(DialogId.COULD_NOT_START_RECORDING, new Object[0]);
                        StateMachine.this.changeTo(new StateWarning(), new Object[0]);
                        StateMachine.this.mCameraDeviceHandler.releaseVideo();
                    } else {
                        StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_RECORDING_ERROR, new Object[0]);
                        StateMachine.this.mCameraDeviceHandler.releaseVideo();
                    }
                }
            });
        }

        @Override // com.sonyericsson.android.camera.recorder.RecorderController.RecorderListener
        public void onRecordFinished(final RecorderController.Result result) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke result:" + result);
            }
            StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.5.3
                @Override // java.lang.Runnable
                public void run() {
                    if (CamLog.DEBUG) {
                        CamLog.d("invoke onRecordFinished");
                    }
                    switch (AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[result.ordinal()]) {
                        case 1:
                            StateMachine.this.changeTo(StateMachine.this.new StateVideoStopping(result), new Object[0]);
                            StateMachine.this.doStopRecording(false);
                            break;
                        case 2:
                            StateMachine.this.changeTo(StateMachine.this.new StateVideoStopping(result), new Object[0]);
                            StateMachine.this.doStopRecording(false);
                            break;
                        case 3:
                            if (StateMachine.this.mContentsViewController != null) {
                                StateMachine.this.mContentsViewController.enableClick();
                            }
                            StateMachine.this.mCameraDeviceHandler.finalizeRecording();
                            StateMachine.this.onVideoRecordingDone();
                            StateMachine.this.requestStoreVideo(AnonymousClass5.this.mVideoSavingRequestBuilder);
                            if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                                ResearchUtil.getInstance().sendSlowMotionEvent(StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION).getValue(), StateMachine.this.mHighFrameRateVideoRecordingCountInSuperSlowMotion);
                                break;
                            }
                            break;
                        case 4:
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_RECORDING_ERROR, new Object[0]);
                            StateMachine.this.mCameraDeviceHandler.releaseVideo();
                            break;
                    }
                }
            });
        }
    };
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener = new OnSuperSlowRecordingFinishedListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.6
        @Override // com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener
        public void onSuperSlowRecordingFinished() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke");
            }
            StateMachine.this.sendEvent(TransitterEvent.EVENT_HIGH_FRAME_RATE_RECORDING_DONE, new Object[0]);
        }
    };
    private boolean mIsPausedAudioPlayback = false;
    private final GestureShutter mGestureShutter = new GestureShutter(this.mGestureShutterHost, null);

    private enum NextCaptureCondition {
        READY,
        REQUESTED,
        UNACCEPTABLE
    }

    public interface OnStateChangedListener {
        void onStateChanged(CaptureState captureState, Object... objArr);
    }

    public enum StartupAction {
        NONE,
        CAPTURE,
        RECORD
    }

    public enum StaticEvent {
        EVENT_ON_PHOTO_STACK_INITIALIZED,
        EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED,
        EVENT_ON_SCENE_MODE_CHANGED,
        EVENT_ON_FACE_DETECTED,
        EVENT_ON_OBJECT_TRACKED,
        EVENT_ON_ORIENTATION_CHANGED,
        EVENT_ON_LAZY_INITIALIZATION_TASK_RUN,
        EVENT_ON_OBJECT_TRACKING_LOST,
        EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED,
        EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED,
        EVENT_ON_PREVIEW_STARTED
    }

    public enum TransitterEvent {
        EVENT_INITIALIZE,
        EVENT_RESUME,
        EVENT_RESUME_TIMEOUT,
        EVENT_PAUSE,
        EVENT_FINALIZE,
        EVENT_ON_HEATED_OVER_WARNING,
        EVENT_ON_HEATED_OVER_WARNING_EXTRA,
        EVENT_ON_HEATED_OVER_COOLING_LOW,
        EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW,
        EVENT_ON_HEATED_OVER_CRITICAL,
        EVENT_ON_HEATED_OVER_NORMAL,
        EVENT_ON_REACH_BATTERY_LOW,
        EVENT_ON_REACH_BATTERY_LIMIT,
        EVENT_ON_BATTERY_LEVEL_CHANGED,
        EVENT_ON_EVF_PREPARED,
        EVENT_ON_EVF_PREPARATION_FAILED,
        EVENT_ON_INITIAL_AUTO_FOCUS_DONE,
        EVENT_ON_AUTO_FOCUS_DONE,
        EVENT_ON_PRE_SHUTTER_DONE,
        EVENT_ON_SHUTTER_DONE,
        EVENT_ON_PREPARE_BURST_DONE,
        EVENT_ON_PRE_TAKE_PICTURE_DONE,
        EVENT_ON_TAKE_PICTURE_DONE,
        EVENT_ON_VIDEO_RECORDING_DONE,
        EVENT_ON_ONE_PREVIEW_FRAME_UPDATED,
        EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED,
        EVENT_ON_SWITCH_CAMERA,
        EVENT_ON_FUSION_CONDITION_CHANGED,
        EVENT_ON_BURST_SHUTTER_DONE,
        EVENT_ON_BURST_STORE_COMPLETED,
        EVENT_ON_BURST_GROUP_STORE_COMPLETED,
        EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED,
        EVENT_ON_STORE_REQUESTED,
        EVENT_ON_STORE_COMPLETED,
        EVENT_STORAGE_ERROR,
        EVENT_STORAGE_MOUNTED,
        EVENT_STORAGE_READY_STATE_CHANGED,
        EVENT_STORAGE_UNGRANTED,
        EVENT_KEY_MENU,
        EVENT_SET_TOUCHED_POSITION,
        EVENT_CANCEL_TOUCHED_POSITION,
        EVENT_CHANGE_SELECTED_FACE,
        EVENT_SET_SELECTED_OBJECT_POSITION,
        EVENT_DESELECT_OBJECT_POSITION,
        EVENT_START_AF_AFTER_OBJECT_TRACKED,
        EVENT_CLEAR_FOCUS,
        EVENT_TOUCH_CONTENT_PROGRESS,
        EVENT_START_TRANSITION_OPERATION,
        EVENT_FINISH_TRANSITION_OPERATION,
        EVENT_SELFTIMER_CANCEL,
        EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE,
        EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END,
        EVENT_HIGH_FRAME_RATE_RECORDING_DONE,
        EVENT_ANGLE_CHANGE_START,
        EVENT_ANGLE_CHANGE_COMPLETED,
        EVENT_CHANGE_CAPTURING_MODE,
        EVENT_START_CAPTURE_COUNTDOWN,
        EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY,
        EVENT_ON_RECORDING_START_WAIT_DONE,
        EVENT_ON_RECORDING_ERROR,
        EVENT_DIALOG_OPENED,
        EVENT_DIALOG_CLOSED,
        EVENT_ON_SEMIAUTO_ENABLED,
        EVENT_ON_SEMIAUTO_DISABLED,
        EVENT_ON_AMBER_BLUE_COLOR_CHANGED,
        EVENT_ON_BRIGHTNESS_CHANGED,
        EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE,
        EVENT_ON_CAMERA_DEVICE_OPENED,
        EVENT_ON_CAMERA_DEVICE_CLOSED,
        EVENT_ON_DEVICE_ERROR,
        EVENT_CAPTURE_READY,
        EVENT_CAPTURE,
        EVENT_CAPTURE_CANCEL,
        EVENT_CAPTURE_BURST,
        EVENT_RECORD_READY,
        EVENT_START_RECORDING,
        EVENT_STOP_RECORDING,
        EVENT_RESUME_RECORDING,
        EVENT_PAUSE_RECORDING,
        EVENT_TRIGGER_SLOW_MOTION,
        EVENT_ZOOM_PREPARE,
        EVENT_ZOOM_PERFORM,
        EVENT_ZOOM_FINISH
    }

    public static final void preload() {
    }

    private void prepareZoom() {
    }

    static /* synthetic */ int access$11608(StateMachine stateMachine) {
        int i = stateMachine.mHighFrameRateVideoRecordingCountInSuperSlowMotion;
        stateMachine.mHighFrameRateVideoRecordingCountInSuperSlowMotion = i + 1;
        return i;
    }

    private class StorageStateAdapter implements Storage.StorageStateListener {
        private Storage.StorageState mOldStorageState;

        @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
        public void onStorageSizeChanged(Storage.StorageType storageType, long j) {
        }

        private StorageStateAdapter() {
            this.mOldStorageState = Storage.StorageState.AVAILABLE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resume() {
            this.mOldStorageState = Storage.StorageState.AVAILABLE;
            updateGestureShutterState(true);
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
        public synchronized void onStorageStateChanged(Storage.StorageType storageType, Storage.StorageState storageState, Storage.StorageReadyState storageReadyState) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke type:" + storageType + ", state:" + storageState + ", readyState:" + storageReadyState);
            }
            if (storageReadyState == Storage.StorageReadyState.ACCESSIBLE || storageReadyState == Storage.StorageReadyState.COMPLETED) {
                CameraApplication.getUiThreadHandler().post(new StorageStateChangeTask(storageType, storageState));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateGestureShutterState(boolean z) {
            StateMachine.this.mGestureShutter.setEnabled(z);
        }

        private class StorageStateChangeTask implements Runnable {
            private final Storage.StorageState mStorageState;
            private final Storage.StorageType mStorageType;

            private StorageStateChangeTask(@NonNull Storage.StorageType storageType, @NonNull Storage.StorageState storageState) {
                this.mStorageType = storageType;
                this.mStorageState = storageState;
            }

            @Override // java.lang.Runnable
            public void run() {
                Storage.StorageType currentStorage = StateMachine.this.getCurrentStorage();
                LaunchCondition.ExtraOperation extraOperation = StateMachine.this.mActivity.getLaunchCondition().getExtraOperation();
                Storage.StorageState currentState = StateMachine.this.mStorage.getCurrentState(currentStorage);
                if (this.mStorageType == currentStorage && (this.mStorageState.isWritable() || currentState.isWritable())) {
                    StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_MOUNTED, new Object[0]);
                } else {
                    if (this.mStorageType == Storage.StorageType.EXTERNAL_CARD && this.mStorageState == Storage.StorageState.CORRUPT) {
                        StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, this.mStorageType, this.mStorageState);
                        return;
                    }
                    if (this.mStorageType == Storage.StorageType.EXTERNAL_CARD && this.mStorageState == Storage.StorageState.UNGRANTED) {
                        if (currentStorage == Storage.StorageType.EXTERNAL_CARD) {
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_UNGRANTED, new Object[0]);
                        }
                    } else if (extraOperation != LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU && this.mStorageType == currentStorage) {
                        boolean z = StateMachine.this.checkSaveDestinationCanBeChange(currentStorage) && !StateMachine.this.mActivity.isOneShot();
                        if (StateMachine.this.mIsSdPermissionFinished && currentStorage != Storage.StorageType.EXTERNAL_CARD && z) {
                            return;
                        }
                        if (StorageStateAdapter.this.mOldStorageState != null && StorageStateAdapter.this.mOldStorageState.isWritable() && (currentStorage != Storage.StorageType.EXTERNAL_CARD || extraOperation != LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE || StateMachine.this.isStorageWritable(Storage.StorageType.EXTERNAL_CARD) || !z)) {
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, this.mStorageType, this.mStorageState);
                            StateMachine.this.mViewFinder.notifyStorageStateChanged(currentStorage, this.mStorageState, z, StateMachine.this.mCurrentState.getCaptureState() == CaptureState.STATE_BURST_CAPTURE || StateMachine.this.mCurrentState.getCaptureState() == CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE);
                        }
                    }
                }
                if (this.mStorageType == currentStorage) {
                    StorageStateAdapter.this.mOldStorageState = this.mStorageState;
                    StateMachine.this.switchSceneRecognition(this.mStorageState.isWritable());
                    if (!this.mStorageState.isWritable()) {
                        StateMachine.this.doStopObjectTracking();
                    }
                    StorageStateAdapter.this.updateGestureShutterState(this.mStorageState.isWritable());
                }
            }
        }
    }

    private class StorageReadyStateAdapter implements Storage.StorageReadyStateListener {
        private StorageReadyStateAdapter() {
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.StorageReadyStateListener
        public void onStorageReadyStateChanged(final Storage.StorageType storageType, Storage.StorageReadyState storageReadyState) {
            if (storageReadyState == Storage.StorageReadyState.COMPLETED && storageType == Storage.StorageType.EXTERNAL_CARD && StateMachine.this.mIsSdPermissionFinished) {
                StateMachine.this.mIsSdPermissionFinished = false;
                StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StorageReadyStateAdapter.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (StateMachine.this.mStorage.getCurrentState(storageType).isWritable()) {
                            DestinationToSave destinationToSave = (DestinationToSave) StateMachine.this.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE);
                            StateMachine.this.mUserSettings.set(DestinationToSave.SDCARD);
                            if (destinationToSave != DestinationToSave.SDCARD) {
                                StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED, new Object[0]);
                            }
                            if (StateMachine.this.mViewFinder != null) {
                                StateMachine.this.mViewFinder.showHiSpeedSdCardRecommendDialogOnDestinationChange();
                            }
                        }
                    }
                });
            }
        }
    }

    private void requestLoadStoredPicture(final StoreDataResult storeDataResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke uri:" + storeDataResult.uri + ", semcType:" + storeDataResult.savingRequest.getSomcType() + ", OneShot:" + this.mActivity.isOneShot());
        }
        PerfLog.BYPASSCAMERA_ON_STORE_COMPLETE.transit();
        this.mStorage.requestLoad(storeDataResult.uri, storeDataResult.savingRequest.common.orientation, new Storage.OnLoadCompletedListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.2
            @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
            public void onDataLoadCompleted(int i, boolean z, LinkedList<Content.ContentInfo> linkedList, Bitmap bitmap) {
            }

            @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
            public void onDataLoadFailed(int i) {
            }

            @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
            public void onLoadCompleted(Uri uri, Bitmap bitmap) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke uri:" + uri);
                }
                StateMachine.this.finishOneShot(storeDataResult, bitmap);
            }

            @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
            public void onLoadFailed(Uri uri, int i) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke uri:" + uri + ", reason:" + i);
                }
                StateMachine.this.finishOneShot(storeDataResult, null);
            }
        });
    }

    private boolean isVideo() {
        return getCurrentCapturingMode().getType() == 2;
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

    public enum CaptureState {
        STATE_NONE(false, false, false, false, false),
        STATE_INITIALIZE(false, false, false, false, false),
        STATE_RESUME(false, false, false, false, false),
        STATE_CAMERA_SWITCHING(false, false, false, false, false),
        STATE_PHOTO_BASE(false, false, false, false, false),
        STATE_PHOTO_READY(true, true, true, true, false),
        STATE_PHOTO_READY_FOR_RECORDING(false, false, false, false, false),
        STATE_PREPARE_FOR_RECORDING(false, false, false, false, false),
        STATE_CAPTURE_COUNTDOWN(true, false, false, false, false),
        STATE_OPERATION_RESTRICTED(true, false, false, true, false),
        STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START(false, false, false, false, false),
        STATE_PHOTO_AF_SEARCH(false, false, false, false, false),
        STATE_PHOTO_AF_DONE(false, false, false, false, false),
        STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE(false, false, false, false, false),
        STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE(false, false, false, false, false),
        STATE_PHOTO_CAPTURE(false, false, false, false, false),
        STATE_BURST_CAPTURE(true, false, false, false, false),
        STATE_VIDEO_RECORDING(true, false, false, false, true),
        STATE_VIDEO_CAPTURE_WHILE_RECORDING(false, false, false, false, true),
        STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING(false, false, false, false, false),
        STATE_VIDEO_STORE(false, false, false, false, true),
        STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION(true, false, false, false, true),
        STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION(true, false, false, false, true),
        STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE(true, false, false, false, true),
        STATE_VIDEO_STOPPING(true, false, false, false, true),
        STATE_PAUSE(false, true, false, false, false),
        STATE_WARNING(true, true, false, true, false),
        STATE_FINALIZE(false, false, false, false, false),
        STATE_VIDEO_RECORDING_PAUSING(true, false, false, false, true),
        STATE_VIDEO_READY(true, true, false, true, false),
        STATE_MODE_CHANGING(false, true, false, false, false),
        STATE_WAITING_PRE_PROCESS_DONE(false, false, false, false, false),
        STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE(false, false, false, false, false),
        STATE_CROPPING(false, true, false, false, false),
        STATE_FATAL(false, false, false, false, false);

        private final boolean mCanApplicationBeFinished;
        private final boolean mCanHandleAsynchronizedTask;
        private final boolean mCanHandleWearableCaptureRequest;
        private final boolean mIsMenuAvailable;
        private final boolean mIsRecordingState;

        CaptureState(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.mCanHandleAsynchronizedTask = z;
            this.mCanApplicationBeFinished = z2;
            this.mCanHandleWearableCaptureRequest = z3;
            this.mIsMenuAvailable = z4;
            this.mIsRecordingState = z5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean canHandleAsynchronizedTask() {
            return this.mCanHandleAsynchronizedTask;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean canApplicationBeFinished() {
            return this.mCanApplicationBeFinished;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean canHandleWearableCaptureRequest() {
            return this.mCanHandleWearableCaptureRequest;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isMenuAvailable() {
            return this.mIsMenuAvailable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRecording() {
            return this.mIsRecordingState;
        }
    }

    public synchronized boolean isDialogOpened() {
        return this.mCurrentState.getCaptureState() == CaptureState.STATE_OPERATION_RESTRICTED;
    }

    private boolean isAllSnapshotCompleted() {
        return this.mCameraDeviceHandler.getRemainSavingPhotoRequestCount() == 0;
    }

    public UserSettings getUserSetting() {
        return this.mUserSettings;
    }

    public Float getZoom() {
        return this.mCameraDeviceHandler.getZoom();
    }

    public void addOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.add(onStateChangedListener);
    }

    public void removeOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.remove(onStateChangedListener);
    }

    public StateMachine(CameraActivity cameraActivity, Storage storage) {
        this.mCurrentState = new StateNone();
        this.mStorageStateListener = new StorageStateAdapter();
        this.mSettingController = new SettingsController();
        this.mPredictiveApplier = new SuitablePredictiveApplier();
        this.mActivity = cameraActivity;
        this.mStorage = storage;
        this.mUserSettings = cameraActivity.getStoredSettings().getUserSettings();
        this.mUserSettings.register(this.mSettingController);
        this.mLastSettings = cameraActivity.getStoredSettings().getLastSettings();
        if (this.mStorageReadyStateListener == null) {
            this.mStorageReadyStateListener = new StorageReadyStateAdapter();
            this.mStorage.addStorageReadyStateListener(this.mStorageReadyStateListener);
        }
    }

    private class SettingsController implements UserSettingApplicable {
        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(AutoReview autoReview) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(CameraKey cameraKey) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Facing facing) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(FastCapture fastCapture) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Geotag geotag) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(GridLine gridLine) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(HelpGuide helpGuide) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(PredictiveCapture predictiveCapture) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(PredictiveLaunch predictiveLaunch) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(ResetSettings resetSettings) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(ShutterSound shutterSound) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(TouchCapture touchCapture) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VideoCodec videoCodec) {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VolumeKey volumeKey) {
        }

        private SettingsController() {
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(CapturingMode capturingMode) {
            VideoSize videoSize = (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
            StateMachine.this.setIsSceneRecognitionValid(isSceneRecognitionValid(capturingMode, videoSize, (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)));
            Float zoom = StateMachine.this.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom.floatValue()) {
                StateMachine.this.showBlackScreen();
            }
            StateMachine.this.mCameraDeviceHandler.setCapturingMode(capturingMode);
            StateMachine.this.mCameraDeviceHandler.setVideoSize(videoSize);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(DisplayFlash displayFlash) {
            StateMachine.this.mCameraDeviceHandler.setDisplayFlashMode(displayFlash);
            notifySettingChanged(displayFlash);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Ev ev) {
            StateMachine.this.mCameraDeviceHandler.setEv(ev);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Flash flash) {
            StateMachine.this.mCameraDeviceHandler.setFlashMode(flash);
            notifySettingChanged(flash);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(FrontAngle frontAngle) {
            if (StateMachine.this.mCurrentState.getCaptureState() == CaptureState.STATE_CROPPING || !StateMachine.this.getCurrentCapturingMode().isFront()) {
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("set(FrontAngle) value: " + frontAngle);
            }
            StateMachine.this.mCameraDeviceHandler.setZoom(frontAngle == FrontAngle.CROPPED ? (float) ((PlatformCapability.getWideZoomTargetRatio(CameraInfo.CameraId.FRONT) - 1.0d) / (PlatformCapability.getMaxZoomRatio(CameraInfo.CameraId.FRONT) - 1.0d)) : 0.0f);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(FocusMode focusMode) {
            StateMachine.this.mCameraDeviceHandler.setFocusMode(focusMode);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Hdr hdr) {
            StateMachine.this.mCameraDeviceHandler.setHdr(hdr);
            notifySettingChanged(hdr);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Iso iso) {
            StateMachine.this.mCameraDeviceHandler.setIso(iso);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Metering metering) {
            StateMachine.this.mCameraDeviceHandler.setMetering(metering);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(PhotoLight photoLight) {
            StateMachine.this.mCameraDeviceHandler.setFlashMode(photoLight.getBooleanValue() ? Flash.LED_ON : Flash.LED_OFF);
            notifySettingChanged(photoLight);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(Resolution resolution) {
            CapturingMode capturingMode = (CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            Float zoom = StateMachine.this.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom.floatValue()) {
                StateMachine.this.showBlackScreen();
            }
            StateMachine.this.mCameraDeviceHandler.setResolution(capturingMode.getCameraId(), resolution);
            if (!capturingMode.isFront()) {
                resetZoom();
            }
            StateMachine.this.requestResizeEvf(capturingMode, true);
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            StateMachine.this.mObjectTracking.stop();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(AspectRatio aspectRatio) {
            notifySettingChanged(aspectRatio);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(SelfTimer selfTimer) {
            StateMachine.this.updatePhotoSelftimer(selfTimer);
            if (StateMachine.this.mViewFinder != null && StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SELF_TIMER_CONDITION, selfTimer);
            }
            notifySettingChanged(selfTimer);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(ShutterTrigger shutterTrigger) {
            if (((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getType() == 1) {
                if (StateMachine.this.mViewFinder == null) {
                    return;
                } else {
                    StateMachine.this.mCameraDeviceHandler.setShutterTrigger(shutterTrigger);
                }
            }
            StateMachine.this.mGestureShutter.handleSettingsChanged(shutterTrigger.isGestureShutterOn());
            if (StateMachine.this.mViewFinder != null && StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                StateMachine.this.mViewFinder.setupFocusRectangles();
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            }
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.setShutterTrigger(shutterTrigger);
            }
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(SoftSkin softSkin) {
            StateMachine.this.mCameraDeviceHandler.setSoftSkin(softSkin);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VideoStabilizer videoStabilizer) {
            if (((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getType() == 2) {
                if (StateMachine.this.mActivity.isOneShotVideo() && StateMachine.this.mActivity.getExtraOutput() != null && !StateMachine.this.mActivity.getExtraOutput().getScheme().equalsIgnoreCase("file")) {
                    if (StateMachine.this.mCameraDeviceHandler.isSteadyShotSupported()) {
                        videoStabilizer = VideoStabilizer.STEADY_SHOT;
                    } else {
                        videoStabilizer = VideoStabilizer.OFF;
                    }
                }
                StateMachine.this.mCameraDeviceHandler.setVideoStabilizer(videoStabilizer);
            }
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(TouchIntention touchIntention) {
            StateMachine.this.mCameraDeviceHandler.setMetering((Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked() || StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF || touchIntention == TouchIntention.FOCUS_AND_EXPOSURE) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VideoSize videoSize) {
            CapturingMode capturingMode = (CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            VideoHdr videoHdr = (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR);
            if (capturingMode == CapturingMode.SLOW_MOTION) {
                StateMachine.this.mCameraDeviceHandler.releaseRecorder();
            }
            StateMachine.this.setIsSceneRecognitionValid(isSceneRecognitionValid(capturingMode, videoSize, videoHdr));
            StateMachine.this.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, videoHdr);
            Float zoom = StateMachine.this.getZoom();
            if (!capturingMode.isFront() && zoom != null && 0.0f < zoom.floatValue()) {
                StateMachine.this.showBlackScreen();
            }
            if (!capturingMode.isFront()) {
                resetZoom();
            }
            StateMachine.this.mCameraDeviceHandler.setVideoSize(videoSize);
            StateMachine.this.mCameraDeviceHandler.setZoom(0.0f);
            FusionMode fusionMode = (FusionMode) StateMachine.this.mUserSettings.get(UserSettingKey.FUSION_MODE);
            if (!DependencyCheckUtil.isFusionAvailableOnVideo(capturingMode.getCameraId(), videoSize, videoHdr)) {
                fusionMode = FusionMode.OFF;
            }
            StateMachine.this.mCameraDeviceHandler.setFusionMode(fusionMode);
            StateMachine.this.requestResizeEvf(capturingMode, true);
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, videoHdr, false);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, fusionMode);
            StateMachine.this.mObjectTracking.stop();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VideoHdr videoHdr) {
            CapturingMode capturingMode = (CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            if (StateMachine.this.getCurrentCapturingMode().isFront() && StateMachine.this.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                StateMachine.this.showBlackScreen();
            }
            VideoSize videoSize = (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
            if (videoSize == VideoSize.FOUR_K_UHD_H264) {
                if (videoHdr == VideoHdr.HDR_ON) {
                    StateMachine.this.mCameraDeviceHandler.setVideoSize(VideoSize.FOUR_K_UHD_H265);
                } else if (videoHdr == VideoHdr.HDR_OFF) {
                    StateMachine.this.mCameraDeviceHandler.setVideoSize(videoSize);
                }
            }
            StateMachine.this.mCameraDeviceHandler.setVideoHdr(videoHdr);
            notifySettingChanged(videoHdr);
            FusionMode fusionMode = (FusionMode) StateMachine.this.mUserSettings.get(UserSettingKey.FUSION_MODE);
            if (!DependencyCheckUtil.isFusionAvailableOnVideo(capturingMode.getCameraId(), videoSize, videoHdr)) {
                fusionMode = FusionMode.OFF;
            }
            StateMachine.this.mCameraDeviceHandler.setFusionMode(fusionMode);
            StateMachine.this.setIsSceneRecognitionValid(videoHdr != VideoHdr.HDR_ON && isSceneRecognitionValid(capturingMode, videoSize, (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)));
            StateMachine.this.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, videoHdr);
            if (!capturingMode.isFront()) {
                resetZoom();
            }
            StateMachine.this.requestResizeEvf(capturingMode, true);
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, videoHdr, true);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, 0);
            StateMachine.this.switchVideoFaceDetection();
            if (!StateMachine.this.mIsSemiAutoEnabled) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, fusionMode);
            StateMachine.this.mObjectTracking.stop();
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(VideoShutterTrigger videoShutterTrigger) {
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.updateVideoShutterTrigger();
            }
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(WhiteBalance whiteBalance) {
            StateMachine.this.mCameraDeviceHandler.setWhiteBalance(whiteBalance);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void commit() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke");
            }
            StateMachine.this.mCameraDeviceHandler.commit();
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.commit();
            }
            CameraParameters parameters = StateMachine.this.mCameraDeviceHandler.getParameters();
            if (parameters != null) {
                CameraInfo.CameraId cameraId = ((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getCameraId();
                EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(StateMachine.this.mActivity, cameraId);
                eachCameraStatusPublisher.putFromParameter(parameters, cameraId, StateMachine.this.getCurrentCapturingMode().isVideo());
                if (StateMachine.this.getCurrentCapturingMode().isVideo()) {
                    eachCameraStatusPublisher.put(new com.sonyericsson.cameracommon.status.eachcamera.PhotoLight(StateMachine.this.mUserSettings.get(UserSettingKey.PHOTO_LIGHT).equals(PhotoLight.ON) ? PhotoLight.Value.ON : PhotoLight.Value.OFF));
                }
                if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                    switch ((SlowMotion) StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                        case STANDARD_SLOW_MOTION:
                            eachCameraStatusPublisher.put(new VideoResolution(((SlowMotion) StateMachine.this.mUserSettings.get(UserSettingKey.SLOW_MOTION)).getVideoSize().getVideoRect())).put(new VideoRecordingFps(RecordingProfile.getVideoFrameRate(((SlowMotion) StateMachine.this.mUserSettings.get(UserSettingKey.SLOW_MOTION)).getVideoSize(), VideoHdr.HDR_OFF))).put(VideoStabilizerStatus.fromCameraParameter(StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put(new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(SlowMotion.Value.OFF)).put(new com.sonyericsson.cameracommon.status.eachcamera.Hdr(com.sonyericsson.cameracommon.status.eachcamera.Hdr.fromCameraParameter(VideoHdr.HDR_OFF))).publish();
                            break;
                        case SUPER_SLOW_MOTION:
                        case SUPER_SLOW_SHOT:
                            VideoSize videoSize = (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                            eachCameraStatusPublisher.put(new VideoResolution(videoSize.getVideoRect())).put(new VideoRecordingFps(RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF))).put(VideoStabilizerStatus.fromCameraParameter(StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put(new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(SlowMotion.Value.ON)).put(new com.sonyericsson.cameracommon.status.eachcamera.Hdr(com.sonyericsson.cameracommon.status.eachcamera.Hdr.fromCameraParameter(VideoHdr.HDR_OFF))).publish();
                            break;
                    }
                }
                eachCameraStatusPublisher.put(new VideoResolution(((VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect())).put(new VideoRecordingFps(RecordingProfile.getVideoFrameRate((VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)))).put(VideoStabilizerStatus.fromCameraParameter(StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER).getValue())).put(new com.sonyericsson.cameracommon.status.eachcamera.SlowMotion(SlowMotion.Value.OFF)).put(new com.sonyericsson.cameracommon.status.eachcamera.Hdr(com.sonyericsson.cameracommon.status.eachcamera.Hdr.fromCameraParameter((VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)))).publish();
            }
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(ObjectTracking objectTracking) {
            if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                set(FocusMode.getDefaultValue((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)));
            }
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                return;
            }
            StateMachine.this.mObjectTracking.stop();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(FocusRange focusRange) {
            StateMachine.this.mCameraDeviceHandler.setFocusRange(focusRange);
            if (StateMachine.this.mViewFinder != null && StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                StateMachine.this.mViewFinder.updateFocusIconType(focusRange != FocusRange.AF);
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
            }
            if (StateMachine.this.isTouchAeEnabled()) {
                StateMachine.this.mCameraDeviceHandler.setMetering((Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
            }
            if (focusRange != FocusRange.AF) {
                StateMachine.this.mObjectTracking.stop();
            }
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(ShutterSpeed shutterSpeed) {
            StateMachine.this.mCameraDeviceHandler.setShutterSpeed(shutterSpeed);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion) {
            VideoSize videoSize;
            StateMachine.this.mCameraDeviceHandler.releaseRecorder();
            StateMachine.this.mCameraDeviceHandler.setSlowMotion(slowMotion);
            if (slowMotion != com.sonyericsson.android.camera.configuration.parameters.SlowMotion.OFF) {
                CapturingMode capturingMode = (CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
                if (slowMotion != com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION) {
                    videoSize = (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                } else {
                    videoSize = slowMotion.getVideoSize();
                }
                StateMachine.this.setIsSceneRecognitionValid(isSceneRecognitionValid(capturingMode, videoSize, (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)));
                StateMachine.this.mCameraDeviceHandler.setPreviewSizeAndFpsRangeForVideo(capturingMode.getCameraId(), videoSize, VideoHdr.HDR_OFF);
                StateMachine.this.mCameraDeviceHandler.setVideoSize(videoSize);
                if (!capturingMode.isFront()) {
                    resetZoom();
                }
            }
            StateMachine.this.mViewFinder.updateSlowMotionView(slowMotion);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(DestinationToSave destinationToSave) {
            StateMachine.this.mGestureShutter.setEnabled(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(DistortionCorrection distortionCorrection) {
            StateMachine.this.mCameraDeviceHandler.setDistortionCorrection(distortionCorrection);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(FusionMode fusionMode) {
            if (DependencyCheckUtil.isFusionAvailableOnVideo(((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).getCameraId(), (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR))) {
                StateMachine.this.mCameraDeviceHandler.setFusionMode(fusionMode);
            } else {
                StateMachine.this.mCameraDeviceHandler.setFusionMode(FusionMode.OFF);
            }
            if (StateMachine.this.mViewFinder == null || !StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, fusionMode);
        }

        @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
        public void set(SideSense sideSense) {
            switch (StateMachine.this.getCurrentCapturingMode()) {
                case NORMAL:
                case FRONT_PHOTO:
                case SLOW_MOTION:
                case UNKNOWN:
                    break;
                default:
                    if (sideSense == SideSense.ON) {
                        StateMachine.this.mActivity.enableSideSense();
                        break;
                    } else {
                        StateMachine.this.mActivity.disableSideSense();
                        break;
                    }
            }
        }

        private void notifySettingChanged(UserSettingValue userSettingValue) {
            synchronized (StateMachine.this) {
                if (StateMachine.this.mViewFinder != null && StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked() && StateMachine.this.mCurrentState.getCaptureState() != CaptureState.STATE_MODE_CHANGING && StateMachine.this.mCurrentState.getCaptureState() != CaptureState.STATE_CAMERA_SWITCHING && StateMachine.this.mCurrentState.getCaptureState() != CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE) {
                    StateMachine.this.mViewFinder.onSettingChanged(userSettingValue);
                }
            }
        }

        private boolean isSceneRecognitionValid(CapturingMode capturingMode, VideoSize videoSize, VideoHdr videoHdr) {
            switch (capturingMode) {
                case SCENE_RECOGNITION:
                case SUPERIOR_FRONT:
                    return true;
                case FRONT_VIDEO:
                case VIDEO:
                    if (VideoSize.FULL_HD == videoSize && videoHdr != VideoHdr.HDR_ON) {
                        return true;
                    }
                    break;
            }
            return false;
        }

        private void resetZoom() {
            StateMachine.this.mCameraDeviceHandler.setZoom(0.0f);
            StateMachine.this.onZoomChange(0);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setDependencies(ViewFinder viewFinder, CameraDeviceHandler cameraDeviceHandler) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mViewFinder = viewFinder;
        this.mCameraDeviceHandler = cameraDeviceHandler;
        this.mObjectTracking = new ObjectTrackingManager(viewFinder, cameraDeviceHandler, this);
    }

    class State {
        protected CaptureState mCaptureState;

        public void entry() {
        }

        public void exit() {
        }

        public void handleCapture(Object... objArr) {
        }

        public void handleCaptureBurst(Object... objArr) {
        }

        public void handleCaptureCancel(Object... objArr) {
        }

        public void handleCaptureReady(Object... objArr) {
        }

        public void handleChangeAngleStart(Object... objArr) {
        }

        public void handleChangeCapturingMode(Object... objArr) {
        }

        public void handleChangeSelectedFace(Object... objArr) {
        }

        public void handleClearFocus(Object... objArr) {
        }

        public void handleDeselectObjectPosition(Object... objArr) {
        }

        public void handleDialogClosed(Object... objArr) {
        }

        public void handleDialogOpened(Object... objArr) {
        }

        public void handleFinalize(Object... objArr) {
        }

        public void handleFinishTransitionOperation(Object... objArr) {
        }

        public void handleFinishZoom(Object... objArr) {
        }

        public void handleFusionConditionChanged(Object... objArr) {
        }

        public void handleHighFameRateRecordingDone(Object... objArr) {
        }

        public void handleInitialize(Object... objArr) {
        }

        public void handleKeyMenu(Object... objArr) {
        }

        public void handleOnAmberBlueColorChanged(Object... objArr) {
        }

        public void handleOnAutoFocusDone(Object... objArr) {
        }

        public void handleOnBrightnessChanged(Object... objArr) {
        }

        public void handleOnBurstShutterDone(Object... objArr) {
        }

        public void handleOnBurstStoreCompleted(Object... objArr) {
        }

        public void handleOnCameraDeviceClosed(Object... objArr) {
        }

        public void handleOnCameraDeviceOpened(Object... objArr) {
        }

        public void handleOnContinuousPreviewFrameUpdated(Object... objArr) {
        }

        public void handleOnEvfPrepared(Object... objArr) {
        }

        public void handleOnFaceDetected(Object... objArr) {
        }

        public void handleOnHeatedOverCoolingLow(Object... objArr) {
        }

        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
        }

        public void handleOnHeatedOverWarningExtra(Object... objArr) {
        }

        public void handleOnLazyInitializationTaskRun(Object... objArr) {
        }

        public void handleOnObjectTracked(Object... objArr) {
        }

        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
        }

        public void handleOnOrientationChanged(Object... objArr) {
        }

        public void handleOnPreShutterDone(Object... objArr) {
        }

        public void handleOnPreTakePictureDone(Object... objArr) {
        }

        public void handleOnPrepareBurstDone(Object... objArr) {
        }

        public void handleOnPreviewStarted() {
        }

        public void handleOnRecordingError(Object... objArr) {
        }

        public void handleOnRecordingStartWaitDone(Object... objArr) {
        }

        public void handleOnSceneModeChanged(Object... objArr) {
        }

        public void handleOnSemiAutoDisabled(Object... objArr) {
        }

        public void handleOnSemiAutoEnabled(Object... objArr) {
        }

        public void handleOnShutterDone(Object... objArr) {
        }

        public void handleOnStorageReadyStateChanged(Object... objArr) {
        }

        public void handleOnStoreRequested(Object... objArr) {
        }

        public void handleOnVideoRecordingDone(Object... objArr) {
        }

        public void handlePause(Object... objArr) {
        }

        public void handlePauseRecording(Object... objArr) {
        }

        public void handlePerformZoom(Object... objArr) {
        }

        public void handlePrepareZoom(Object... objArr) {
        }

        public void handleRecordReady(Object... objArr) {
        }

        public void handleRequestUpdateHighSensitivityFusionMode(Object... objArr) {
        }

        public void handleResumeRecording(Object... objArr) {
        }

        public void handleResumeTimeout(Object... objArr) {
        }

        public void handleSelfTimerCancel(Object... objArr) {
        }

        public void handleSetSelectedObjectPosition(Object... objArr) {
        }

        public void handleSetTouchedPosition(Object... objArr) {
        }

        public void handleSlowMotionFeedbackAnimationEnd(Object... objArr) {
        }

        public void handleStartAfAfterObjectTracked(Object... objArr) {
        }

        public void handleStartCaptureCountDown(Object... objArr) {
        }

        public void handleStartRecording(Object... objArr) {
        }

        public void handleStartTransitionOperation(Object... objArr) {
        }

        public void handleStopRecording(Object... objArr) {
        }

        public void handleStopRecordingSlowMotion(Object... objArr) {
        }

        public void handleStorageMounted(Object... objArr) {
        }

        public void handleSwitchCamera(Object... objArr) {
        }

        public void handleTouchContentProgress() {
        }

        public void handleTriggerSlowMotion(Object... objArr) {
        }

        private State() {
            this.mCaptureState = CaptureState.STATE_NONE;
        }

        public String toString() {
            if (this.mCaptureState == null) {
                return CaptureState.STATE_NONE.toString();
            }
            return this.mCaptureState.toString();
        }

        public CaptureState getCaptureState() {
            return this.mCaptureState;
        }

        public void handleResume(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDisabled()) {
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
            }
        }

        public void handleOnHeatedOverWarning(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_WARNING, new Object[0]);
        }

        public void handleOnHeatedOverCritical(Object... objArr) {
            boolean zBooleanValue = ((Boolean) objArr[0]).booleanValue();
            StateMachine.this.changeTo(new StateFatal(false, zBooleanValue), new Object[0]);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_CRITICAL, Boolean.valueOf(zBooleanValue));
        }

        public void handleOnHeatedOverNormal(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_NORMAL, new Object[0]);
        }

        public void handleOnInitialAutoFocusDone(Object... objArr) {
            CamLog.e("ERROR:PRE-SCAN Event is not handled correctly. Check sequence.");
            StateMachine.this.cancelAutoFocus(false);
        }

        public void handleOnTakePictureDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            if (CamLog.DEBUG) {
                CamLog.d("invoke id:" + photoSavingRequestBuilder.getRequestId());
            }
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
        }

        public void handleOnStoreCompleted(Object... objArr) {
            StateMachine.this.mLastStoreDataResult = (StoreDataResult) objArr[0];
            StateMachine.this.mPredictiveApplier.leaveSuppressor(StateMachine.this.mLastStoreDataResult.savingRequest);
            if (StateMachine.this.mLastStoreDataResult.savingRequest.getFilePath() != null && PredictiveCapturePathBuilder.isPredictiveCaptureLastImage(StateMachine.this.mLastStoreDataResult.savingRequest.getFilePath())) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED, StateMachine.this.mLastStoreDataResult);
            }
            if (StateMachine.this.mActivity.getWearableBridge() != null) {
                StateMachine.this.mActivity.getWearableBridge().getPhotoStateNotifier().onCaptureSucceeded();
            }
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_STORE_COMPLETED, StateMachine.this.mLastStoreDataResult, Boolean.valueOf(isLastStoreDataResult(StateMachine.this.mLastStoreDataResult)));
            }
        }

        private boolean isLastStoreDataResult(StoreDataResult storeDataResult) {
            if (storeDataResult.savingRequest.common.takenByFastCapture) {
                return true;
            }
            if (StateMachine.this.mLastPhotoSavingRequest == null && StateMachine.this.mLastVideoSavingRequest == null) {
                CamLog.w("Last saving request is not exist.");
                return true;
            }
            int requestId = storeDataResult.savingRequest.getRequestId();
            return StateMachine.this.mLastPhotoSavingRequest == null ? StateMachine.this.mLastVideoSavingRequest.getRequestId() == requestId : StateMachine.this.mLastVideoSavingRequest == null ? StateMachine.this.mLastPhotoSavingRequest.getRequestId() == requestId : StateMachine.this.mLastPhotoSavingRequest.getRequestId() > StateMachine.this.mLastVideoSavingRequest.getRequestId() ? StateMachine.this.mLastPhotoSavingRequest.getRequestId() == requestId : StateMachine.this.mLastVideoSavingRequest.getRequestId() == requestId;
        }

        public void handleStorageError(Object... objArr) {
            boolean z = false;
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : type = " + storageType + ", state = " + StateMachine.this.mCurrentState);
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                StateMachine.this.changeTo(new StateFatal(z, z), new Object[0]);
            }
        }

        public void handleOnStorageUngranted(Object... objArr) {
            if (CamLog.DEBUG) {
                throw new IllegalStateException("Since processing is not executed in the current state, confirmation is necessary");
            }
        }

        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            StateMachine.this.mHandler.postDelayed(new ReTrySetupHeadUpDisplayTask(((Boolean) objArr[0]).booleanValue()), 100L);
        }

        class ReTrySetupHeadUpDisplayTask implements Runnable {
            private final boolean mApplySettingsForFastCapture;

            public ReTrySetupHeadUpDisplayTask(boolean z) {
                this.mApplySettingsForFastCapture = z;
            }

            @Override // java.lang.Runnable
            public void run() {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, Boolean.valueOf(this.mApplySettingsForFastCapture));
            }
        }

        public void handleCancelTouchedPosition(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.resetFocusAreaAndRect((FocusMode) StateMachine.this.getUserSetting().get(UserSettingKey.FOCUS_MODE));
            StateMachine.this.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
        }

        public void handleOnPredictiveCaptureGroupStoreCompleted(Object... objArr) {
            StateMachine.this.onPredictiveCaptureStoreComplete((StoreDataResult) objArr[0]);
        }

        public void handleOnObjectLost(Object... objArr) {
            StateMachine.this.mViewFinder.onObjectLost();
        }

        public void handleOnReachBatteryLimit(Object... objArr) {
            boolean z = false;
            StateMachine.this.changeTo(new StateFatal(z, z), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, false);
        }

        public void handleOnReachBatteryLow(Object... objArr) {
            StateMachine.this.mViewFinder.showMessageDialog(DialogId.LOW_BATTERY_WARNING, new Object[0]);
        }

        public void handleOnReachBatteryLevelChanged(Object... objArr) {
            StateMachine.this.mViewFinder.updateBatteryIndicator(((Integer) objArr[0]).intValue());
        }
    }

    class StateNone extends State {
        private StateNone() {
            super();
            this.mCaptureState = CaptureState.STATE_NONE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleInitialize(Object... objArr) {
            StateMachine.this.changeTo(new StateInitialize(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }
    }

    class StateWaitingEvfPreparedByModeChange extends State {
        private StateWaitingEvfPreparedByModeChange() {
            super();
            this.mCaptureState = CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
            if (StateMachine.this.isTutorialNeededToBeShownForCurrentMode()) {
                StateMachine.this.changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            } else {
                StateMachine.this.changeToStandby();
            }
        }
    }

    class StateInitialize extends State {
        private Evf mEvf;

        private StateInitialize() {
            super();
            this.mCaptureState = CaptureState.STATE_INITIALIZE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResume(Object... objArr) {
            CameraDeviceHandler.CameraSessionId cameraSessionId = (CameraDeviceHandler.CameraSessionId) objArr[1];
            StartupAction startupAction = (StartupAction) objArr[2];
            if (StateMachine.this.mCameraDeviceHandler.isCameraDisabled()) {
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
                return;
            }
            switch (StateMachine.this.mCameraDeviceHandler.getPreProcessState()) {
                case PRE_SCAN_DONE:
                    StateMachine.this.mCameraDeviceHandler.preCapture();
                    break;
                case PRE_CAPTURE_DONE:
                    RequestFactory.PhotoSavingRequestBuilder andClearPreCaptureResult = StateMachine.this.mCameraDeviceHandler.getAndClearPreCaptureResult();
                    if (andClearPreCaptureResult != null) {
                        StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, andClearPreCaptureResult);
                        break;
                    }
                    break;
            }
            FastCapture fastCapture = (FastCapture) objArr[0];
            StateMachine.this.changeTo(new StateResume(cameraSessionId, fastCapture, this.mEvf, startupAction), fastCapture, Boolean.valueOf(StateMachine.this.hasRemainSavingRequest()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnInitialAutoFocusDone(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.preCapture();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreShutterDone(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreTakePictureDone(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            this.mEvf = (Evf) objArr[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NotifyDelayedEventTask notifyDelayedEvent(TransitterEvent transitterEvent, Object... objArr) {
        NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(transitterEvent, objArr);
        this.mHandler.postDelayed(notifyDelayedEventTask, 100L);
        return notifyDelayedEventTask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDelayedEvent(NotifyDelayedEventTask notifyDelayedEventTask) {
        this.mHandler.removeCallbacks(notifyDelayedEventTask);
    }

    private class NotifyDelayedEventTask implements Runnable {
        private final Object[] mArgs;
        private final TransitterEvent mEvent;

        private NotifyDelayedEventTask(TransitterEvent transitterEvent, Object[] objArr) {
            this.mEvent = transitterEvent;
            this.mArgs = objArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke NotifyDelayedEventTask");
            }
            StateMachine.this.sendEvent(this.mEvent, this.mArgs);
        }
    }

    class StateCameraSwitching extends State {
        private static final String TAG = "StateMachine.StateCameraSwitching";
        private final FastCapture mFastCapture;
        private boolean mIsCameraOpened;
        private boolean mIsEvfPrepared;
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        private StateCameraSwitching(CameraDeviceHandler.CameraSessionId cameraSessionId, FastCapture fastCapture) {
            super();
            this.mIsEvfPrepared = false;
            this.mIsCameraOpened = false;
            this.mCaptureState = CaptureState.STATE_CAMERA_SWITCHING;
            this.mSessionId = cameraSessionId;
            this.mFastCapture = fastCapture;
            StateMachine.this.mHandler.removeCallbacks(StateMachine.this.mNotifyResumeTimeoutTask);
            StateMachine.this.mHandler.postDelayed(StateMachine.this.mNotifyResumeTimeoutTask, 7000L);
        }

        private void moveStateIfCaptureReady() {
            if (this.mIsCameraOpened && this.mIsEvfPrepared) {
                if (StateMachine.this.getCurrentCapturingMode().isFront() && StateMachine.this.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                    StateMachine.this.showBlackScreen();
                }
                StateMachine.this.mCameraDeviceHandler.startPreview();
                if (StateMachine.this.isFusionMonitoringNeeded()) {
                    StateMachine.this.mCameraDeviceHandler.startFusionMonitoring();
                }
                StateMachine.this.startFastCapture(this.mFastCapture, StartupAction.NONE);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnCameraDeviceOpened(Object... objArr) {
            if (((CameraDeviceHandler.CameraSessionId) objArr[0]) == this.mSessionId) {
                this.mIsCameraOpened = true;
                moveStateIfCaptureReady();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            this.mIsEvfPrepared = true;
            moveStateIfCaptureReady();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResumeTimeout(Object... objArr) {
            ResearchUtil.getInstance().setCameraNotAvailableFailedToOpen();
            CamLog.i(TAG, "[CameraNotAvailable] resume timeout.");
            PlatformCapability.setDeviceError(true);
            StateMachine.this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
            StateMachine.this.changeTo(new StateWarning(), new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            super.exit();
            StateMachine.this.mHandler.removeCallbacks(StateMachine.this.mNotifyResumeTimeoutTask);
        }
    }

    class StateResume extends State {
        private final FastCapture mFastCapture;
        private boolean mIsCameraStarted;
        private boolean mIsCurrentStorageReady;
        private boolean mIsEvfPrepared;
        private boolean mIsResumeSequenceStarted;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        private StartupAction mStartupAction;
        private Storage.StorageReadyStateListener mStorageReadyStateListener;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
        }

        private StateResume(CameraDeviceHandler.CameraSessionId cameraSessionId, FastCapture fastCapture, Evf evf, StartupAction startupAction) {
            super();
            this.mStorageReadyStateListener = new Storage.StorageReadyStateListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StateResume.1
                @Override // com.sonyericsson.cameracommon.storage.Storage.StorageReadyStateListener
                public void onStorageReadyStateChanged(final Storage.StorageType storageType, final Storage.StorageReadyState storageReadyState) {
                    if (CamLog.DEBUG) {
                        CamLog.d("invoke type:" + storageType + ", state:" + storageReadyState + ", getCurrentStorage:" + StateMachine.this.getCurrentStorage());
                    }
                    if (storageReadyState != Storage.StorageReadyState.SUSPENDED) {
                        StateMachine.this.mStorage.addStorageStateListener(StateMachine.this.mStorageStateListener);
                    }
                    if (StateMachine.this.getCurrentStorage() == storageType) {
                        StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StateResume.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED, storageType, storageReadyState);
                            }
                        });
                    }
                }
            };
            PerfLog.STATE_RESUME.begin();
            this.mCaptureState = CaptureState.STATE_RESUME;
            this.mSessionId = cameraSessionId;
            this.mFastCapture = fastCapture;
            this.mIsResumeSequenceStarted = false;
            this.mIsCameraStarted = false;
            this.mIsCurrentStorageReady = StateMachine.this.mStorage.isStorageReadable(StateMachine.this.getCurrentStorage());
            StateMachine.this.mVirtualKeyEventDispatcher.start();
            this.mStartupAction = startupAction;
            if (evf != null) {
                StateMachine.this.mCameraDeviceHandler.setPreviewSurface(evf.asSurface());
                this.mIsEvfPrepared = true;
            } else {
                this.mIsEvfPrepared = false;
            }
            StateMachine.this.mIsSemiAutoEnabled = false;
        }

        private void moveStateIfCaptureReady() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke isStorageReady:" + this.mIsCurrentStorageReady + ", isCameraStarted:" + this.mIsCameraStarted + ", isEvfPrepared:" + this.mIsEvfPrepared + ", getExtraOperation():" + StateMachine.this.mActivity.getLaunchCondition().getExtraOperation());
            }
            if (this.mIsCameraStarted && this.mIsEvfPrepared && this.mIsCurrentStorageReady) {
                StateMachine.this.initGeoTagManager();
                StateMachine.this.initSideSenseSetting();
                if (StateMachine.this.getCurrentCapturingMode().isFront() && StateMachine.this.mUserSettings.get(UserSettingKey.FRONT_ANGLE) == FrontAngle.CROPPED) {
                    StateMachine.this.showBlackScreen();
                }
                StateMachine.this.mCameraDeviceHandler.startPreview();
                if (StateMachine.this.isFusionMonitoringNeeded()) {
                    StateMachine.this.mCameraDeviceHandler.startFusionMonitoring();
                }
                StateMachine.this.mActivity.reportFullyDrawnOnce();
                if (StateMachine.this.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE) == DestinationToSave.SDCARD) {
                    Storage.StorageState currentState = StateMachine.this.mStorage.getCurrentState(Storage.StorageType.EXTERNAL_CARD);
                    boolean zCheckSaveDestinationCanBeChange = StateMachine.this.checkSaveDestinationCanBeChange(Storage.StorageType.EXTERNAL_CARD);
                    if (StateMachine.this.mStorage.getSdGrantedUri() == null && (currentState != Storage.StorageState.REMOVED || this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE)) {
                        StateMachine.this.mUserSettings.set(DestinationToSave.EMMC);
                        if (zCheckSaveDestinationCanBeChange) {
                            if (StateMachine.this.mViewFinder != null) {
                                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, Boolean.valueOf(this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE));
                            }
                        } else {
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, Storage.StorageType.INTERNAL, currentState);
                            StateMachine.this.mViewFinder.notifyStorageStateChanged(Storage.StorageType.INTERNAL, currentState, zCheckSaveDestinationCanBeChange, false);
                        }
                    }
                }
                if (!StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                    switch (StateMachine.this.mCameraDeviceHandler.getPreProcessState()) {
                        case PRE_SCAN_DONE:
                        case PRE_CAPTURE_DONE:
                            StateMachine.this.cancelAutoFocus(false);
                            StateMachine.this.mCameraDeviceHandler.cancelPreProcessState();
                            break;
                        case PRE_CAPTURE_STARTED:
                        case PRE_SCAN_STARTED:
                            StateMachine.this.changeTo(new StateWaitingPreProcessDone(this.mFastCapture), new Object[0]);
                            return;
                    }
                }
                StateMachine.this.startFastCapture(this.mFastCapture, this.mStartupAction);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateResume");
            }
            super.entry();
            StateMachine.this.mStorageStateListener.resume();
            StateMachine.this.mViewFinder.setIsCameraSwitching(false);
            StateMachine.this.mLastPhotoSavingRequest = null;
            StateMachine.this.mLastVideoSavingRequest = null;
            StateMachine.this.mIsPausedAudioPlayback = false;
            this.mIsCurrentStorageReady = StateMachine.this.mStorage.isStorageReadable(StateMachine.this.getCurrentStorage());
            StateMachine.this.mStorage.addStorageReadyStateListener(this.mStorageReadyStateListener);
            if (StateMachine.this.hasRemainSavingRequest()) {
                StateMachine.this.mActivity.disableAutoPowerOffTimer();
                StateMachine.this.mViewFinder.showSavingProgressBar();
            } else {
                startResuming();
                moveStateIfCaptureReady();
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, Boolean.valueOf(StateMachine.this.isSettingChangeAcceptable()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            super.exit();
            StateMachine.this.mStorage.removeStorageReadyStateListener(this.mStorageReadyStateListener);
            StateMachine.this.mViewFinder.hideSavingProgressBar();
            StateMachine.this.mHandler.removeCallbacks(StateMachine.this.mNotifyResumeTimeoutTask);
            PerfLog.STATE_RESUME.end();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            if (!this.mIsResumeSequenceStarted) {
                StateMachine.this.mViewFinder.requestCheckEvfPreparationRetrying();
                return;
            }
            PerfLog.RESIZE_EVF.end();
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            this.mIsEvfPrepared = true;
            LocalResearchUtil.getInstance().setSettingsValue(StateMachine.this.mUserSettings, StateMachine.this.mActivity.getLaunchCondition().getCapturingMode());
            moveStateIfCaptureReady();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnInitialAutoFocusDone(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.preCapture();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartCaptureCountDown(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_CAPTURE, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreShutterDone(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreTakePictureDone(Object... objArr) {
            StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnCameraDeviceOpened(Object... objArr) {
            if (((CameraDeviceHandler.CameraSessionId) objArr[0]) == this.mSessionId) {
                this.mIsCameraStarted = true;
                moveStateIfCaptureReady();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnCameraDeviceClosed(Object... objArr) {
            if (StateMachine.this.hasRemainSavingRequest() || this.mIsResumeSequenceStarted) {
                return;
            }
            StateMachine.this.mActivity.enableAutoPowerOffTimer();
            startResuming();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResumeTimeout(Object... objArr) {
            ResearchUtil.getInstance().setCameraNotAvailableFailedToOpen();
            CamLog.e(StateMachine.TAG, "Camera application resume is timed-out.");
            CamLog.e(StateMachine.TAG, "  CameraDevice is ready:" + this.mIsCameraStarted);
            CamLog.e(StateMachine.TAG, "  Evf is ready:" + this.mIsEvfPrepared);
            CamLog.e(StateMachine.TAG, "  Storage is ready:" + this.mIsCurrentStorageReady);
            PlatformCapability.setDeviceError(true);
            StateMachine.this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
            StateMachine.this.changeTo(new StateWarning(), new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageReadyStateChanged(Object... objArr) {
            this.mIsCurrentStorageReady = StateMachine.this.mStorage.isStorageReadable(StateMachine.this.getCurrentStorage());
            if (this.mIsCurrentStorageReady) {
                moveStateIfCaptureReady();
            }
        }

        private void startResuming() {
            if (StateMachine.this.mActivity.isOneShotVideo()) {
                StateMachine.this.mActivity.awaitSetupAllReady();
            }
            this.mIsResumeSequenceStarted = true;
            StateMachine.this.mViewFinder.showSurface();
            StateMachine.this.requestResizeEvf(StateMachine.this.mActivity.getLaunchCondition().getCapturingMode(), false);
            PerfLog.RESIZE_EVF.begin();
            if (AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[StateMachine.this.mCameraDeviceHandler.getPreProcessState().ordinal()] == 1) {
                StateMachine.this.mCameraDeviceHandler.preCapture();
            }
            StateMachine.this.mHandler.postDelayed(StateMachine.this.mNotifyResumeTimeoutTask, 7000L);
        }
    }

    class StatePhotoBase extends State {
        private StatePhotoBase() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_BASE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoEnabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }
    }

    class StatePhotoReady extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoReady";
        private CameraParameters.FaceDetectionResult mLatestFaceDetectionResult;
        private NotifyDelayedEventTask mNotifyDelayedEventTask;
        private StartupAction mStartupAction;
        private final boolean mWithExtensionFeatures;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
        }

        public StatePhotoReady(StateMachine stateMachine, boolean z) {
            this(z, false, StartupAction.NONE);
        }

        public StatePhotoReady(boolean z, boolean z2, StartupAction startupAction) {
            super();
            this.mLatestFaceDetectionResult = null;
            this.mCaptureState = CaptureState.STATE_PHOTO_READY;
            this.mWithExtensionFeatures = z;
            this.mStartupAction = startupAction;
            StateMachine.this.sendResearchViewEvent();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            boolean z;
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePhotoReady");
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.notifyStateIdleToWearable();
            }
            if (PlatformCapability.hasDeviceError()) {
                return;
            }
            if (StateMachine.this.mActivity == null) {
                z = false;
            } else {
                if (PlatformCapability.isPowerSavingSupported(StateMachine.this.getCurrentCameraId())) {
                    if (!StateMachine.this.mActivity.isThermalWarningReceived()) {
                        if (StateMachine.this.mActivity.isThermalWarningExtraState()) {
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
                        }
                    } else {
                        StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
                        z = true;
                    }
                } else if (StateMachine.this.mActivity.isThermalWarningExtraState()) {
                    StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
                }
                z = false;
            }
            if (this.mWithExtensionFeatures) {
                if (!StateMachine.this.mViewFinder.isTouchFocus()) {
                    StateMachine.this.mCameraDeviceHandler.startFaceDetection();
                }
                if (!StateMachine.this.mIsSemiAutoEnabled) {
                    StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
                }
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.enableAutoPowerOffTimer();
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, Integer.valueOf(StateMachine.this.getSensorOrientation()));
            if (!z) {
                StateMachine.this.mGestureShutter.handlePreviewStarted(StateMachine.this.getCurrentCapturingMode(), StateMachine.this.mCameraDeviceHandler.getStreamingImageRetriever());
            }
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
            if (StateMachine.this.mViewFinder.isHeadUpDisplayReady() && StateMachine.this.isFusionMonitoringNeeded()) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, StateMachine.this.mCameraDeviceHandler.getLatestFusionResult());
            }
            Storage.StorageType currentStorage = StateMachine.this.getCurrentStorage();
            if (!StateMachine.this.isStorageWritable(currentStorage) && !StateMachine.this.checkSaveDestinationCanBeChange(currentStorage)) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, currentStorage, StateMachine.this.mStorage.getCurrentState(currentStorage));
            }
            if (StateMachine.this.mIsSceneRecognitionValid) {
                return;
            }
            StateMachine.this.notifySceneRecognitionDisabled();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
            StateMachine.this.mGestureShutter.handlePreviewStopped();
            if (this.mNotifyDelayedEventTask != null) {
                StateMachine.this.removeDelayedEvent(this.mNotifyDelayedEventTask);
                this.mNotifyDelayedEventTask = null;
            }
            StateMachine.this.mPredictiveApplier.leaveSuppressor(this);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeAngleStart(Object... objArr) {
            StateMachine.this.doZoomChangeAngle();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            if (((Boolean) objArr[0]).booleanValue()) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
                StateMachine.this.mCameraDeviceHandler.startFaceDetection();
            }
            if (this.mStartupAction == StartupAction.CAPTURE) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.NORMAL);
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
            if (StateMachine.this.isTutorialNeededToBeShownForCurrentMode()) {
                StateMachine.this.changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogOpened(Object... objArr) {
            if (objArr != null && objArr.length != 0) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, (ViewFinder.UiComponentKind) objArr[0]);
            }
            StateMachine.this.changeTo(new StateOperationRestricted(), objArr);
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
            if (StateMachine.this.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                StateMachine.this.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureReady(Object... objArr) {
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoAfSearch(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartCaptureCountDown(Object... objArr) {
            Event.SelfTimerTrigger selfTimerTrigger = (Event.SelfTimerTrigger) StateMachine.getEventParam(objArr, 0, Event.SelfTimerTrigger.class, Event.SelfTimerTrigger.NORMAL);
            if (this.mNotifyDelayedEventTask != null) {
                StateMachine.this.removeDelayedEvent(this.mNotifyDelayedEventTask);
                this.mNotifyDelayedEventTask = null;
            }
            if (StateMachine.this.isLazyInitializationRunning()) {
                this.mNotifyDelayedEventTask = StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, objArr);
            } else {
                if (!StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
                    return;
                }
                if (this.mStartupAction == StartupAction.CAPTURE) {
                    StateMachine.this.updatePhotoSelftimer(SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN);
                }
                StateMachine.this.changeTo(new StateCaptureCountdown(selfTimerTrigger), true);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            boolean z = false;
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(z), new Object[0]);
            } else {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureBurst(Object... objArr) {
            if (StateMachine.this.checkBurstConditions(true) && StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StateBurstCaptureWaitForAfDone(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, new Object[0]);
            StateMachine.this.mCameraDeviceHandler.resetFocusModeAndCommit();
            if (StateMachine.this.isTouchAeEnabled()) {
                StateMachine.this.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAutoFocusDone(Object... objArr) {
            StateMachine.this.cancelAutoFocus(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            boolean z = false;
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                StateMachine.this.changeTo(new StateFatal(z, z), new Object[0]);
            } else {
                if (StateMachine.this.checkSaveDestinationCanBeChange(storageType)) {
                    return;
                }
                StateMachine.this.changeTo(new StateWarning(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
            StateMachine.this.mUserSettings.set(DestinationToSave.EMMC);
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, false);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSceneModeChanged(Object... objArr) {
            if (StateMachine.this.mIsSceneRecognitionValid) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED, objArr[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            this.mLatestFaceDetectionResult = (CameraParameters.FaceDetectionResult) objArr[0];
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, this.mLatestFaceDetectionResult);
            StateMachine.this.requestPhotoSmileCapture();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeSelectedFace(Object... objArr) {
            StateMachine.this.doChangeSelectedFace((Point) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetSelectedObjectPosition(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                Rect rect = (Rect) objArr[1];
                if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                    StateMachine.this.doStartObjectTracking(rect);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDeselectObjectPosition(Object... objArr) {
            StateMachine.this.doStopObjectTracking();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartAfAfterObjectTracked(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady() && PlatformCapability.isObjectTrackingSupported(StateMachine.this.getCurrentCameraId()) && StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                Rect rect = (Rect) objArr[1];
                StateMachine.this.doStopObjectTracking();
                StateMachine.this.doStartObjectTracking(rect);
                StateMachine.this.changeTo(new StatePhotoWaitingTrackedObjectForAfStart(), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetTouchedPosition(Object... objArr) {
            if (((SmileCapture) StateMachine.this.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn() || ((ShutterTrigger) StateMachine.this.mUserSettings.get(UserSettingKey.SHUTTER_TRIGGER)).isGestureShutterOn()) {
                return;
            }
            if (((FocusRectangles.FocusSetType) objArr[2]) == FocusRectangles.FocusSetType.RELEASE) {
                Rect position = StateMachine.this.mViewFinder.getPosition((Point) objArr[0]);
                if (position.isEmpty()) {
                    return;
                }
                if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                    StateMachine.this.mCameraDeviceHandler.setFocusPositionAndCommit(position);
                }
                if (StateMachine.this.isTouchAeEnabled()) {
                    StateMachine.this.mCameraDeviceHandler.setMeteringAreaAndCommit(position, (Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
                }
            }
            if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF || StateMachine.this.mUserSettings.get(UserSettingKey.TOUCH_INTENTION) == TouchIntention.FOCUS_AND_EXPOSURE) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED, objArr[0], objArr[2]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleKeyMenu(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
            StateMachine.this.changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.SETTING_DIALOG);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSwitchCamera(Object... objArr) {
            StateMachine.this.switchCamera((AnimationRequest.AnimationType) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleTouchContentProgress() {
            if (StateMachine.this.mViewFinder == null) {
                return;
            }
            if ((StateMachine.this.mLastVideoSavingRequest != null && (StateMachine.this.mLastPhotoSavingRequest == null || StateMachine.this.mLastVideoSavingRequest.getDateTaken() > StateMachine.this.mLastPhotoSavingRequest.getDateTaken())) || StateMachine.this.mLastPhotoSavingRequest == null || StateMachine.this.mLastPhotoSavingRequest.getImageData() == null) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_INSTANT_VIEWER, StateMachine.this.mLastPhotoSavingRequest.getImageData(), StateMachine.this.mLastPhotoSavingRequest, StateMachine.this.mLastStoreDataResult);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoEnabled(Object... objArr) {
            super.handleOnSemiAutoEnabled(objArr);
            StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            super.handleOnSemiAutoDisabled(objArr);
            StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
            StateMachine.this.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            StateMachine.this.mCameraDeviceHandler.setBrightnessAndCommit(0);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAmberBlueColorChanged(Object... objArr) {
            StateMachine.this.updateAmberBlueColor(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnBrightnessChanged(Object... objArr) {
            StateMachine.this.updateBrightness(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartTransitionOperation(Object... objArr) {
            if (((AnimationRequest) objArr[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                StateMachine.this.changeTo(new StateModeChanging(), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
            StateMachine.this.notifyCoolingUltraLow(!StateMachine.this.isStorageFull(StateMachine.this.getCurrentStorage()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverWarningExtra(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestUpdateHighSensitivityFusionMode(Object... objArr) {
            StateMachine.this.updateFusionModeSetting((FusionMode) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePrepareZoom(Object... objArr) {
            VariableIndex variableIndex = (VariableIndex) objArr[0];
            handleClearFocus(objArr);
            StateMachine.this.mPredictiveApplier.entrySuppressor(this);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_START, Integer.valueOf(variableIndex.getIndex()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
            VariableIndex variableIndex = (VariableIndex) objArr[0];
            StateMachine.this.mCameraDeviceHandler.setZoomAndCommit(variableIndex.getRatio());
            StateMachine.this.onZoomChange(variableIndex.getIndex());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
            StateMachine.this.mPredictiveApplier.leaveSuppressor(this);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_STOP, new Object[0]);
        }
    }

    class StateVideoReady extends StateVideoBase {
        private static final String TAG = "StateMachine.StateVideoReady";
        private StartupAction mStartupAction;

        public StateVideoReady(StateMachine stateMachine) {
            this(StartupAction.NONE);
        }

        public StateVideoReady(StartupAction startupAction) {
            super();
            this.mCaptureState = CaptureState.STATE_VIDEO_READY;
            this.mStartupAction = startupAction;
            if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SCENE_RECOGNITION) {
                StateMachine.this.setCurrentCapturingMode(CapturingMode.VIDEO);
            }
            StateMachine.this.sendResearchViewEvent();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion;
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.checkThermalWarning();
                StateMachine.this.mActivity.notifyStateBlockedToWearable();
            }
            if (!StateMachine.this.mPendingTaskListForStandby.isEmpty()) {
                Iterator it = StateMachine.this.mPendingTaskListForStandby.iterator();
                while (it.hasNext()) {
                    StateMachine.this.mHandler.post((Runnable) it.next());
                }
                StateMachine.this.mPendingTaskListForStandby.clear();
            }
            if (PlatformCapability.hasDeviceError()) {
                return;
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.enableAutoPowerOffTimer();
            }
            if (!StateMachine.this.mViewFinder.isTouchFocus()) {
                StateMachine.this.switchVideoFaceDetection();
            }
            if (!StateMachine.this.mIsSemiAutoEnabled) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, Integer.valueOf(StateMachine.this.getSensorOrientation()));
            if (StateMachine.this.mViewFinder.isHeadUpDisplayReady() && StateMachine.this.isFusionMonitoringNeeded()) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, StateMachine.this.mCameraDeviceHandler.getLatestFusionResult());
            }
            Storage.StorageType currentStorage = StateMachine.this.getCurrentStorage();
            if (!StateMachine.this.isStorageWritable(currentStorage)) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, currentStorage, StateMachine.this.mStorage.getCurrentState(currentStorage));
                return;
            }
            IntentReader.VideoQualityConfigurations videoQualityConfigurations = StateMachine.this.mActivity.getLaunchCondition().getVideoQualityConfigurations();
            if (!StateMachine.this.isEnoughStorageSizeAvailableForOneShotVideo()) {
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, new Object[0]);
                return;
            }
            if (!StateMachine.this.mActivity.isOneShot() || VideoSize.MMS != StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE) || videoQualityConfigurations.isQualityLow()) {
                switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) StateMachine.this.mUserSettings.get(UserSettingKey.SLOW_MOTION)) {
                    case SUPER_SLOW_MOTION:
                    case SUPER_SLOW_SHOT:
                        VideoSize videoSize = (VideoSize) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                        if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && (slowMotion = (com.sonyericsson.android.camera.configuration.parameters.SlowMotion) StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION)) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION) {
                            videoSize = slowMotion.getVideoSize();
                        }
                        RecordingProfile recordingProfileBuild = new RecordingProfile.Builder().videoSize(videoSize).setOneShot(StateMachine.this.mActivity.isOneShot()).videoHdr((VideoHdr) StateMachine.this.mUserSettings.get(UserSettingKey.VIDEO_HDR)).build();
                        StateMachine.this.mCameraDeviceHandler.prepareRecorder(StateMachine.this.createVideoSavingRequest(recordingProfileBuild), StateMachine.this.mRecorderListener, StateMachine.this.mOnSuperSlowRecordingFinishedListener, StateMachine.this.shouldPlayShutterSound(), recordingProfileBuild, StateMachine.this.mStorage.createNotifier(StateMachine.this.getCurrentStorage(), 10));
                        break;
                }
                if (StateMachine.this.mIsSceneRecognitionValid) {
                    return;
                }
                StateMachine.this.notifySceneRecognitionDisabled();
                return;
            }
            StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeAngleStart(Object... objArr) {
            StateMachine.this.doZoomChangeAngle();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            if (((Boolean) objArr[0]).booleanValue()) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
                StateMachine.this.switchVideoFaceDetection();
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
            if (StateMachine.this.isTutorialNeededToBeShownForCurrentMode()) {
                StateMachine.this.changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
            } else if (StateMachine.this.mActivity != null && StateMachine.this.mActivity.getLaunchCondition().getLaunchCameraMode().isSlowMotion()) {
                StateMachine.this.mViewFinder.postSlowMotionHintText();
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.clearLaunchCameraMode();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogOpened(Object... objArr) {
            if (objArr != null && objArr.length != 0) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, (ViewFinder.UiComponentKind) objArr[0]);
            }
            StateMachine.this.changeTo(new StateOperationRestricted(), objArr);
            if (StateMachine.this.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                StateMachine.this.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            boolean z = false;
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : type = " + storageType + ", state = " + StateMachine.this.mCurrentState);
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                StateMachine.this.changeTo(new StateFatal(z, z), new Object[0]);
                return;
            }
            if (StateMachine.this.checkSaveDestinationCanBeChange(StateMachine.this.getCurrentStorage())) {
                return;
            }
            StateMachine.this.changeTo(new StateWarning(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
            StateMachine.this.mUserSettings.set(DestinationToSave.EMMC);
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, false);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            super.handleOnSemiAutoDisabled(objArr);
            StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
            StateMachine.this.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            StateMachine.this.mCameraDeviceHandler.setBrightnessAndCommit(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(0);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleKeyMenu(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
            StateMachine.this.changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.SETTING_DIALOG);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSwitchCamera(Object... objArr) {
            StateMachine.this.switchCamera((AnimationRequest.AnimationType) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRecordReady(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                StateMachine.this.changeTo(new StatePhotoReadyForRecording(false), Boolean.valueOf((objArr == null || objArr.length == 0) ? false : ((Boolean) objArr[0]).booleanValue()));
            } else {
                CamLog.w("ignore the event because device is not ready");
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartRecording(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                if (StateMachine.this.isLazyInitializationRunning()) {
                    return;
                }
                StateMachine.this.changeTo(new StatePhotoReadyForRecording(false), false);
                StateMachine.this.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                return;
            }
            CamLog.w("ignore the event because device is not ready");
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartCaptureCountDown(Object... objArr) {
            StateMachine.this.changeTo(new StateCaptureCountdown((Event.SelfTimerTrigger) StateMachine.getEventParam(objArr, 0, Event.SelfTimerTrigger.class, Event.SelfTimerTrigger.NORMAL)), true);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverWarningExtra(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartTransitionOperation(Object... objArr) {
            if (((AnimationRequest) objArr[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                StateMachine.this.changeTo(new StateModeChanging(), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeSelectedFace(Object... objArr) {
            StateMachine.this.doChangeSelectedFace((Point) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetSelectedObjectPosition(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                Rect rect = (Rect) objArr[1];
                if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                    StateMachine.this.doStartObjectTracking(rect);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDeselectObjectPosition(Object... objArr) {
            StateMachine.this.doStopObjectTracking();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
            StateMachine.this.notifyCoolingUltraLow(!StateMachine.this.isStorageFull(StateMachine.this.getCurrentStorage()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestUpdateHighSensitivityFusionMode(Object... objArr) {
            StateMachine.this.updateFusionModeSetting((FusionMode) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreviewStarted() {
            if (this.mStartupAction == StartupAction.RECORD) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnLazyInitializationTaskRun(Object... objArr) {
            if (this.mStartupAction == StartupAction.RECORD) {
                StateMachine.this.mCameraDeviceHandler.setOnPreviewStartedListener(new OnPreviewStartedListenerImpl());
                CameraApplication.getUiThreadHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StateVideoReady.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StateMachine.this.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                    }
                });
            }
        }
    }

    class StateCaptureCountdown extends StatePhotoReady {
        private SelfTimerFeedback mFeedback;
        private final LedLight mLedLight;
        private final Event.SelfTimerTrigger mTrigger;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDeselectObjectPosition(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartRecording(Object... objArr) {
        }

        private StateCaptureCountdown(Event.SelfTimerTrigger selfTimerTrigger) {
            super(StateMachine.this, false);
            this.mCaptureState = CaptureState.STATE_CAPTURE_COUNTDOWN;
            this.mLedLight = new LedLightImpl();
            this.mTrigger = selfTimerTrigger;
        }

        private void start(int i, SoundPlayer.Type type) {
            this.mFeedback = new SelfTimerFeedback(i, this.mLedLight, (StateMachine.this.getCurrentCapturingMode().isFront() || StateMachine.this.mUserSettings.get(UserSettingKey.FLASH) == Flash.OFF) ? false : true, new SelfTimerFeedback.SelfTimerFeedbackListener() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StateCaptureCountdown.1
                @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.SelfTimerFeedbackListener
                public void onBlinkFinished() {
                    StateCaptureCountdown.this.recoverFlash();
                }

                @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.SelfTimerFeedbackListener
                public void onCountDownFinished() {
                    boolean z = false;
                    if (StateMachine.this.getCurrentCapturingMode().isVideo()) {
                        StateMachine.this.changeTo(new StatePhotoReadyForRecording(StateCaptureCountdown.this.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE), false);
                        StateMachine.this.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                    } else if (StateMachine.this.startAutoFocus()) {
                        StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(z), new Object[0]);
                    }
                    Event.CaptureTrigger captureTrigger = Event.CaptureTrigger.SELF_TIMER;
                    if (StateCaptureCountdown.this.mTrigger != Event.SelfTimerTrigger.GESTURE) {
                        if (StateCaptureCountdown.this.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE) {
                            captureTrigger = Event.CaptureTrigger.SIDE_SENSE;
                        }
                    } else {
                        captureTrigger = Event.CaptureTrigger.GESTURE;
                        LocalResearchUtil.getInstance().setPredictiveLaunchState(false);
                    }
                    ResearchUtil.getInstance().setCaptureTrigger(captureTrigger);
                }

                @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.SelfTimerFeedbackListener
                public void onSoundTypeChange(long j) {
                    if (StateMachine.this.shouldPlayShutterSound()) {
                        if (j == 4000) {
                            StateMachine.this.mActivity.playSound(SoundPlayer.Type.SELF_TIMER_4SEC);
                        } else {
                            StateMachine.this.mActivity.playSound(SoundPlayer.Type.SELF_TIMER_1SEC);
                        }
                    }
                }
            });
            this.mFeedback.start(0);
            if (type == null || !StateMachine.this.shouldPlayShutterSound()) {
                return;
            }
            StateMachine.this.mActivity.playSound(type);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recoverFlash() {
            if (!((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isVideo()) {
                if (((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isFront()) {
                    return;
                }
                StateMachine.this.mCameraDeviceHandler.setFlashModeAndCommit((Flash) StateMachine.this.mUserSettings.get(UserSettingKey.FLASH));
                return;
            }
            StateMachine.this.mCameraDeviceHandler.setTorchAndCommit(((com.sonyericsson.android.camera.configuration.parameters.PhotoLight) StateMachine.this.mUserSettings.get(UserSettingKey.PHOTO_LIGHT)).getBooleanValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateCaptureCountdown");
            }
            SelfTimer photoSelfTimerSetting = StateMachine.this.mViewFinder.getPhotoSelfTimerSetting();
            if (this.mTrigger == Event.SelfTimerTrigger.SIDE_SENSE && photoSelfTimerSetting == SelfTimer.OFF) {
                photoSelfTimerSetting = SelfTimer.SIDE_COUNT_DOWN;
            }
            start(photoSelfTimerSetting.getDurationInMillisecond(), photoSelfTimerSetting.getSoundType());
            StateMachine.this.mGestureShutter.handleSelftimerStarted();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.stopPlaySound();
            this.mFeedback.stop();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_SELFTIMER_FINISHED, new Object[0]);
            StateMachine.this.mGestureShutter.handleSelftimerStopped(true);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureReady(Object... objArr) {
            recoverFlash();
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoAfSearch(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            recoverFlash();
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureBurst(Object... objArr) {
            if (StateMachine.this.checkBurstConditions(true)) {
                recoverFlash();
                if (StateMachine.this.startAutoFocus()) {
                    StateMachine.this.changeTo(new StateBurstCaptureWaitForAfDone(), new Object[0]);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            recoverFlash();
            super.handlePause(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke checkSaveDestinationCanBeChange:" + StateMachine.this.checkSaveDestinationCanBeChange(StateMachine.this.getCurrentStorage()));
            }
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : type = " + storageType + ", state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            recoverFlash();
            if (!StateMachine.this.checkSaveDestinationCanBeChange(StateMachine.this.getCurrentStorage())) {
                StateMachine.this.changeTo(new StateWarning(), objArr);
            } else {
                StateMachine.this.changeToStandby();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetTouchedPosition(Object... objArr) {
            if (((SmileCapture) StateMachine.this.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn() || ((ShutterTrigger) StateMachine.this.mUserSettings.get(UserSettingKey.SHUTTER_TRIGGER)).isGestureShutterOn() || TouchCapture.ON != StateMachine.this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                return;
            }
            super.handleSetTouchedPosition(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetSelectedObjectPosition(Object... objArr) {
            if (TouchCapture.ON == StateMachine.this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                super.handleSetSelectedObjectPosition(objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartAfAfterObjectTracked(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady() && TouchCapture.ON == StateMachine.this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE)) {
                recoverFlash();
                super.handleStartAfAfterObjectTracked(objArr);
            }
        }

        private class LedLightImpl implements LedLight {
            private LedLightImpl() {
            }

            @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.LedLight
            public void turnOn() {
                StateMachine.this.mCameraDeviceHandler.setTorchAndCommit(true);
            }

            @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.LedLight
            public void turnOff() {
                StateMachine.this.mCameraDeviceHandler.setTorchAndCommit(false);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSelfTimerCancel(Object... objArr) {
            recoverFlash();
            ResearchUtil.getInstance().sendSelfTimerCancelledEvent(this.mTrigger);
            StateMachine.this.changeToStandby();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoReady, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
            StateMachine.this.notifyCoolingUltraLow(false);
        }
    }

    class StatePhotoReadyForRecording extends StatePhotoBase {
        private final boolean mBySideSense;

        private StatePhotoReadyForRecording(boolean z) {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_READY_FOR_RECORDING;
            this.mBySideSense = z;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : type = " + storageType + ", state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            if (StateMachine.this.checkSaveDestinationCanBeChange(StateMachine.this.getCurrentStorage())) {
                return;
            }
            StateMachine.this.changeTo(new StateWarning(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            requestStartRecording();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoReady(StateMachine.this), new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartRecording(Object... objArr) {
            requestStartRecording();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }

        private void requestStartRecording() {
            PerfLog.START_REC.begin();
            if (!StateMachine.this.mStorage.canPushStoreRequest(StateMachine.this.getCurrentStorage())) {
                StateMachine.this.changeTo(new StateVideoReady(StateMachine.this), new Object[0]);
                return;
            }
            if (!StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                StateMachine.this.changeTo(new StateVideoReady(StateMachine.this), new Object[0]);
                return;
            }
            StateMachine.this.pauseAudioPlaybackForRecord();
            StateMachine.this.mViewFinder.hideHudIcons();
            StateMachine.this.changeTo(new StatePrepareForRecording(), new Object[0]);
            StateMachine.this.mStartRecordingTask = StateMachine.this.new StartRecordingTask(this.mBySideSense);
            StateMachine.this.mHandler.post(StateMachine.this.mStartRecordingTask);
            PerfLog.START_REC.end();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleTriggerSlowMotion(Object... objArr) {
            requestStartRecording();
        }
    }

    class StatePrepareForRecording extends StateNone {
        private StatePrepareForRecording() {
            super();
            this.mCaptureState = CaptureState.STATE_PREPARE_FOR_RECORDING;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }
    }

    class StateCropping extends State {
        private CaptureState mPreviousCaptureState;

        public StateCropping(CaptureState captureState) {
            super();
            this.mPreviousCaptureState = null;
            this.mCaptureState = CaptureState.STATE_CROPPING;
            this.mPreviousCaptureState = captureState;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateCropping");
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ANGLE_CHANGE_COMPLETED, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setZoomAndCommit(((VariableIndex) objArr[0]).getRatio());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
            switch (this.mPreviousCaptureState) {
                case STATE_OPERATION_RESTRICTED:
                    StateMachine.this.changeTo(new StateOperationRestricted(), new Object[0]);
                    break;
                case STATE_PHOTO_READY:
                    StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, false), new Object[0]);
                    break;
                case STATE_VIDEO_READY:
                    StateMachine.this.changeTo(new StateVideoReady(StateMachine.this), new Object[0]);
                    break;
                case STATE_WARNING:
                    StateMachine.this.changeTo(new StateWarning(), new Object[0]);
                    break;
            }
        }
    }

    class StateOperationRestricted extends State {
        private StoreDataResult mVideoStoreDataResult;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnContinuousPreviewFrameUpdated(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
        }

        private StateOperationRestricted() {
            super();
            this.mVideoStoreDataResult = null;
            this.mCaptureState = CaptureState.STATE_OPERATION_RESTRICTED;
            if (!StateMachine.this.getCurrentCapturingMode().isVideo() && StateMachine.this.mActivity.isThermalWarningExtraState()) {
                StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.enableAutoPowerOffTimer();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateOperationRestricted");
            }
            StateMachine.this.mPredictiveApplier.entrySuppressor(this);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.mPredictiveApplier.leaveSuppressor(this);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, new Object[0]);
            StateMachine.this.mCameraDeviceHandler.resetFocusModeAndCommit();
            if (StateMachine.this.isTouchAeEnabled()) {
                StateMachine.this.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering) StateMachine.this.mUserSettings.get(UserSettingKey.METERING));
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeAngleStart(Object... objArr) {
            StateMachine.this.doZoomChangeAngle();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogOpened(Object... objArr) {
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
            if (objArr == null || objArr.length == 0) {
                return;
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, (ViewFinder.UiComponentKind) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogClosed(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, objArr);
            StateMachine.this.mActivity.restartAutoPowerOffTimer();
            StateMachine.this.mActivity.notifyStateIdleToWearable();
            StateMachine.this.changeToStandby();
            if (this.mVideoStoreDataResult != null) {
                if (StateMachine.this.mActivity.isOneShot()) {
                    StateMachine.this.onOneShotStoreCompleted(this.mVideoStoreDataResult);
                } else if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION && !PlatformCapability.hasDeviceError()) {
                    ApplicationLauncher.launchVideoEditor(StateMachine.this.mActivity, this.mVideoStoreDataResult);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleKeyMenu(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSwitchCamera(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, new Object[0]);
            StateMachine.this.switchCamera((AnimationRequest.AnimationType) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAmberBlueColorChanged(Object... objArr) {
            StateMachine.this.updateAmberBlueColor(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnBrightnessChanged(Object... objArr) {
            StateMachine.this.updateBrightness(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverWarningExtra(Object... objArr) {
            if (StateMachine.this.getCurrentCapturingMode().isVideo()) {
                return;
            }
            StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartTransitionOperation(Object... objArr) {
            if (((AnimationRequest) objArr[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                StateMachine.this.changeTo(new StateModeChanging(), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoEnabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = true;
            StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDeselectObjectPosition(Object... objArr) {
            StateMachine.this.doStopObjectTracking();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            switch (StateMachine.this.getCurrentCapturingMode()) {
                case NORMAL:
                case FRONT_PHOTO:
                case SCENE_RECOGNITION:
                case SUPERIOR_FRONT:
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
                    break;
                case SLOW_MOTION:
                case FRONT_VIDEO:
                case VIDEO:
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            StoreDataResult storeDataResult = (StoreDataResult) objArr[0];
            if (storeDataResult.savingRequest instanceof VideoSavingRequest) {
                this.mVideoStoreDataResult = storeDataResult;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestUpdateHighSensitivityFusionMode(Object... objArr) {
            StateMachine.this.updateFusionModeSetting((FusionMode) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }
    }

    class StatePhotoWaitingTrackedObjectForAfStart extends State {
        boolean mIsAutoFocusStarted;
        boolean mIsBurstCaptureRequired;
        boolean mIsCaptureRequired;
        boolean mIsFirstCallback;

        private StatePhotoWaitingTrackedObjectForAfStart() {
            super();
            this.mIsAutoFocusStarted = false;
            this.mIsFirstCallback = true;
            this.mIsCaptureRequired = false;
            this.mIsBurstCaptureRequired = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            boolean z = false;
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
            if (!((CameraParameters.ObjectTrackingResult) objArr[0]).mIsLost && !this.mIsFirstCallback) {
                StateMachine.this.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                if (this.mIsCaptureRequired) {
                    StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(z), new Object[0]);
                    return;
                } else {
                    if (this.mIsBurstCaptureRequired) {
                        if (StateMachine.this.checkBurstConditions(true)) {
                            StateMachine.this.changeTo(new StateBurstCaptureWaitForAfDone(), objArr);
                            return;
                        } else {
                            StateMachine.this.changeTo(new StatePhotoAfSearch(), new Object[0]);
                            return;
                        }
                    }
                    return;
                }
            }
            this.mIsFirstCallback = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartAfAfterObjectTracked(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady() && !this.mIsAutoFocusStarted && PlatformCapability.isObjectTrackingSupported(StateMachine.this.getCurrentCameraId()) && StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                Rect rect = (Rect) objArr[1];
                StateMachine.this.doStopObjectTracking();
                StateMachine.this.doStartObjectTracking(rect);
                this.mIsAutoFocusStarted = false;
                this.mIsCaptureRequired = false;
                this.mIsBurstCaptureRequired = false;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectLost(Object... objArr) {
            super.handleOnObjectLost(objArr);
            boolean z = false;
            if (this.mIsCaptureRequired) {
                StateMachine.this.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(z), new Object[0]);
            } else if (this.mIsBurstCaptureRequired) {
                StateMachine.this.startAutoFocus();
                this.mIsAutoFocusStarted = true;
                if (StateMachine.this.checkBurstConditions(true)) {
                    StateMachine.this.changeTo(new StateBurstCaptureWaitForAfDone(), objArr);
                } else {
                    StateMachine.this.changeTo(new StatePhotoAfSearch(), new Object[0]);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            if (this.mIsAutoFocusStarted) {
                StateMachine.this.cancelAutoFocus(true);
            }
            StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
            handleCaptureCancel(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            if (this.mIsBurstCaptureRequired) {
                return;
            }
            this.mIsCaptureRequired = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            if (this.mIsAutoFocusStarted) {
                StateMachine.this.cancelAutoFocus(true);
            }
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureBurst(Object... objArr) {
            if (this.mIsCaptureRequired) {
                return;
            }
            this.mIsBurstCaptureRequired = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
        }
    }

    class StatePhotoAfSearch extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoAfSearch";
        private boolean mIsCancelRequested;

        private StatePhotoAfSearch() {
            super();
            this.mIsCancelRequested = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_SEARCH;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAutoFocusDone(Object... objArr) {
            boolean zBooleanValue = ((Boolean) objArr[1]).booleanValue();
            if (this.mIsCancelRequested) {
                StateMachine.this.cancelAutoFocus(false);
                StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
            } else {
                StateMachine.this.changeTo(new StatePhotoAfDone(zBooleanValue), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
            handleCaptureCancel(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            if (PlatformCapability.isTrackingFocusDuringLockSupported(StateMachine.this.getCurrentCameraId())) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            this.mIsCancelRequested = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureBurst(Object... objArr) {
            if (this.mIsCancelRequested) {
                return;
            }
            if (StateMachine.this.checkBurstConditions(true)) {
                StateMachine.this.changeTo(new StateBurstCaptureWaitForAfDone(), objArr);
            } else {
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            if (this.mIsCancelRequested) {
                return;
            }
            StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
        }
    }

    class StatePhotoAfDone extends StatePhotoBase {
        private boolean mIsClearObjectTrackingFocusFrame;
        private final boolean mIsHighQualityBurstAvailable;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleClearFocus(Object... objArr) {
        }

        private StatePhotoAfDone(boolean z) {
            super();
            this.mIsClearObjectTrackingFocusFrame = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_DONE;
            this.mIsHighQualityBurstAvailable = z;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePhotoAfDone");
            }
            super.entry();
            this.mIsClearObjectTrackingFocusFrame = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureBurst(Object... objArr) {
            if (StateMachine.this.checkBurstConditions(this.mIsHighQualityBurstAvailable)) {
                StateMachine.this.changeTo(new StateBurstCapture(false), objArr);
            } else {
                StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                StateMachine.this.changeTo(new StatePhotoCapture(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            StateMachine.this.changeTo(new StatePhotoCapture(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            this.mIsClearObjectTrackingFocusFrame = false;
            if (PlatformCapability.isTrackingFocusDuringLockSupported(StateMachine.this.getCurrentCameraId())) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectLost(Object... objArr) {
            super.handleOnObjectLost(objArr);
            this.mIsClearObjectTrackingFocusFrame = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            super.exit();
            if (this.mIsClearObjectTrackingFocusFrame) {
                this.mIsClearObjectTrackingFocusFrame = false;
                if (StateMachine.this.mViewFinder != null && StateMachine.this.mViewFinder.isSetupHeadupDisplayInvoked()) {
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
                }
                StateMachine.this.mObjectTracking.stop();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
        }
    }

    class StatePhotoCaptureWaitForAfDone extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCaptureWaitForAfDone";
        private final boolean mIsDirectCaptureRequired;

        private StatePhotoCaptureWaitForAfDone(boolean z) {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE;
            this.mIsDirectCaptureRequired = z;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnInitialAutoFocusDone(Object... objArr) {
            boolean z = false;
            if (this.mIsDirectCaptureRequired) {
                StateMachine.this.changeTo(new StatePhotoAfDone(z), objArr);
                StateMachine.this.mCameraDeviceHandler.preCapture();
                StateMachine.this.changeTo(new StatePhotoCapture(), objArr);
                return;
            }
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAutoFocusDone(Object... objArr) {
            StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            StateMachine.this.changeTo(new StatePhotoAfDone(false), objArr);
            StateMachine.this.changeTo(new StatePhotoCapture(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.mPredictiveApplier.attemptCommitSettings();
        }
    }

    class StatePhotoCapture extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCapture";
        private boolean mIsStorageError;
        private NextCaptureCondition mNextCapture;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreShutterDone(Object... objArr) {
        }

        private StatePhotoCapture() {
            super();
            this.mIsStorageError = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE;
            if (StateMachine.this.mActivity.isOneShot()) {
                this.mNextCapture = NextCaptureCondition.UNACCEPTABLE;
            } else {
                this.mNextCapture = NextCaptureCondition.READY;
            }
            StateMachine.this.mViewFinder.cancelPredictiveCaptureIndicatorAnimation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreTakePictureDone(Object... objArr) throws ExecutionException, InterruptedException {
            if (!StateMachine.this.mStorage.isStorageActivated()) {
                StateMachine.this.notifyDelayedEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, objArr);
                return;
            }
            Storage.StorageType currentStorage = StateMachine.this.getCurrentStorage();
            boolean z = false;
            boolean z2 = StateMachine.this.mStorage.getCurrentState(Storage.StorageType.EXTERNAL_CARD) == Storage.StorageState.CORRUPT;
            if (!StateMachine.this.isStorageWritable(currentStorage) || z2) {
                ((RequestFactory.PhotoSavingRequestBuilder) objArr[0]).close();
                StateMachine.this.mCameraDeviceHandler.prepareCaptureImageReader(null);
                StateMachine.this.cancelAutoFocus(false);
                StateMachine.this.mCameraDeviceHandler.cancelPreProcessState();
                if (z2) {
                    StateMachine.this.mViewFinder.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, new Object[0]);
                    StateMachine.this.changeTo(new StateFatal(z, z), new Object[0]);
                    return;
                } else {
                    StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
                    return;
                }
            }
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            StateMachine.this.mLastPhotoSavingRequest = null;
            StateMachine.this.mLastVideoSavingRequest = null;
            StateMachine.this.playShutterSound();
            ByteBuffer imageReaderData = photoSavingRequestBuilder.getImageReaderData();
            byte[] bArr = new byte[imageReaderData.remaining()];
            imageReaderData.get(bArr);
            photoSavingRequestBuilder.setImageData(bArr);
            photoSavingRequestBuilder.close();
            StateMachine.this.mCameraDeviceHandler.prepareCaptureImageReader(new CameraDeviceHandler.ImageReaderInitializedCallback() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StatePhotoCapture.1
                @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.ImageReaderInitializedCallback
                public void onInitialized() {
                    StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.StatePhotoCapture.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (CamLog.DEBUG) {
                                CamLog.d("invoke onInitialized");
                            }
                            if (StateMachine.this.getCurrentCaptureState() == CaptureState.STATE_PHOTO_CAPTURE) {
                                StateMachine.this.cancelAutoFocus(true);
                                StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
                            }
                        }
                    });
                }
            });
            photoSavingRequestBuilder.setStorageType(StateMachine.this.getCurrentStorage());
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
            ResearchUtil.getInstance().setTimeAfDone();
            ResearchUtil.getInstance().setTimeCaptureStart();
            ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
            ResearchUtil.getInstance().setOrientation(photoSavingRequestBuilder.mCommonStatus.orientation);
            LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
            LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, StateMachine.this.getCurrentCapturingMode());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke mNextCapture:" + this.mNextCapture);
            }
            if (AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] == 1) {
                if (StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage())) {
                    this.mNextCapture = NextCaptureCondition.REQUESTED;
                }
            } else if (CamLog.DEBUG) {
                CamLog.d("Capture button is ignored. mNextCapture:" + this.mNextCapture + ", Saving request count:" + StateMachine.this.mCameraDeviceHandler.getRemainSavingPhotoRequestCount());
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnShutterDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            StateMachine.this.mViewFinder.hideDisplayFlashScreen();
            StateMachine.this.mViewFinder.onCaptureDone();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, new Object[0]);
            if (photoSavingRequestBuilder.getSomcType() == 100) {
                StateMachine.this.mViewFinder.startPredictiveCaptureIndicatorAnimation();
            } else {
                StateMachine.this.mViewFinder.cancelPredictiveCaptureIndicatorAnimation();
            }
            LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, StateMachine.this.getCurrentCapturingMode());
            if (StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable() && !this.mIsStorageError) {
                if (!requestNextCaptureIfRequired()) {
                    StateMachine.this.cancelAutoFocus(false);
                    if (StateMachine.this.mActivity.isOneShot()) {
                        return;
                    }
                    StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
                    return;
                }
                ResearchUtil.getInstance().setContinuousCapture();
                LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
                return;
            }
            this.mNextCapture = NextCaptureCondition.UNACCEPTABLE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnTakePictureDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            if (CamLog.DEBUG) {
                CamLog.d("invoke id:" + photoSavingRequestBuilder.getRequestId());
            }
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            if (StateMachine.this.mActivity.isOneShot()) {
                StateMachine.this.onOneShotStoreCompleted((StoreDataResult) objArr[0]);
            }
            if (AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] == 3 && StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                StateMachine.this.cancelAutoFocus(false);
                StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            this.mIsStorageError = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.mViewFinder.setDisplayFlashRequired(false);
            StateMachine.this.mViewFinder.setDisplayFlashColor(255, 255, 255);
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        private boolean requestNextCaptureIfRequired() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke mNextCapture:" + this.mNextCapture);
            }
            if (AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[this.mNextCapture.ordinal()] != 2) {
                return false;
            }
            this.mNextCapture = NextCaptureCondition.READY;
            StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
            return true;
        }
    }

    class StateBurstCaptureWaitForAfDone extends StatePhotoBase {
        private static final String TAG = "StateMachine.StateBurstCaptureWaitForAfDone";
        private boolean mIsCancelRequested;

        private StateBurstCaptureWaitForAfDone() {
            super();
            this.mCaptureState = CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE;
            this.mIsCancelRequested = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            this.mIsCancelRequested = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAutoFocusDone(Object... objArr) {
            if (StateMachine.this.checkBurstConditions(((Boolean) objArr[1]).booleanValue())) {
                StateMachine.this.changeTo(new StateBurstCapture(this.mIsCancelRequested), objArr);
            } else {
                StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                StateMachine.this.changeTo(new StatePhotoCapture(), objArr);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }
    }

    class StateBurstCapture extends StatePhotoBase {
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

        private StateBurstCapture(boolean z) {
            super();
            this.mCaptureState = CaptureState.STATE_BURST_CAPTURE;
            StateMachine.this.mCameraDeviceHandler.prepareBurst();
            this.mIndex = 0;
            this.mDataString = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(new Date());
            if (z) {
                this.mBurstState = 3;
            } else {
                this.mBurstState = 0;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            ResearchUtil.getInstance().setManualBurstCount(this.mIndex);
            LocalResearchUtil.getInstance().setUserOperation(Event.CaptureOperation.SHOOTING, StateMachine.this.getCurrentCapturingMode());
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_FINISH, new Object[0]);
        }

        private void finishCapturing() {
            switch (this.mBurstState) {
                case 0:
                case 1:
                    this.mBurstState = 3;
                    break;
                case 2:
                    this.mBurstState = 4;
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_BURST_CAPTURE);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPrepareBurstDone(Object... objArr) {
            boolean zBooleanValue = ((Boolean) objArr[0]).booleanValue();
            int i = this.mBurstState;
            if (i == 0) {
                if (!zBooleanValue) {
                    StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                    StateMachine.this.changeTo(new StatePhotoCapture(), new Object[0]);
                    return;
                } else {
                    requestNextCapture();
                    this.mBurstState = 1;
                    return;
                }
            }
            if (i != 3) {
                return;
            }
            if (!zBooleanValue) {
                StateMachine.this.doCapture(StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO));
                StateMachine.this.changeTo(new StatePhotoCapture(), new Object[0]);
            } else {
                requestNextCapture();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            finishCapturing();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnShutterDone(Object... objArr) {
            boolean zBooleanValue = ((Boolean) objArr[1]).booleanValue();
            if (CamLog.DEBUG) {
                CamLog.d("invoke count:" + this.mIndex + ", af:" + zBooleanValue);
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_SHUTTER_DONE, Boolean.valueOf(zBooleanValue), Integer.valueOf(this.mIndex));
            if (this.mIndex >= 100) {
                finishCapturing();
            }
            switch (this.mBurstState) {
                case 0:
                    throw new IllegalStateException("INITIALIZE cannot accept ON_SHUTTER_DONE event");
                case 1:
                    if (StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                        if (StateMachine.this.isStorageWritable(Storage.StorageType.INTERNAL)) {
                            requestNextCapture();
                            return;
                        } else {
                            requestStopBurstCapture();
                            return;
                        }
                    }
                    this.mBurstState = 2;
                    return;
                case 2:
                    throw new IllegalStateException("WAIT_FOR_BUFFER_AVAILABLE cannot accept ON_SHUTTER_DONE event");
                case 3:
                    if (StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                        if (this.mIndex < 2) {
                            if (StateMachine.this.isStorageWritable(Storage.StorageType.INTERNAL)) {
                                requestNextCapture();
                                return;
                            } else {
                                requestStopBurstCapture();
                                return;
                            }
                        }
                        requestStopBurstCapture();
                        return;
                    }
                    this.mBurstState = 4;
                    return;
                default:
                    return;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnTakePictureDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            if (CamLog.DEBUG) {
                CamLog.d("invoke id:" + photoSavingRequestBuilder.getRequestId());
            }
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            int i = this.mBurstState;
            if (i == 2) {
                if (!StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                    CamLog.e("ON_STORE_COMPLETED is received. But buffer is not available.");
                    return;
                } else {
                    this.mBurstState = 1;
                    requestNextCapture();
                    return;
                }
            }
            if (i != 4) {
                return;
            }
            if (!StateMachine.this.mCameraDeviceHandler.isBypassCameraNextShotAvailable()) {
                CamLog.e("ON_STORE_COMPLETED is received. But buffer is not available.");
                return;
            }
            if (this.mIndex < 2) {
                if (StateMachine.this.isStorageWritable(Storage.StorageType.INTERNAL)) {
                    requestNextCapture();
                    return;
                } else {
                    requestStopBurstCapture();
                    return;
                }
            }
            requestStopBurstCapture();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            Storage.StorageType storageType = (Storage.StorageType) objArr[0];
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : type = " + storageType + ", state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            switch (this.mBurstState) {
                case 1:
                    this.mBurstState = 3;
                    return;
                case 2:
                    this.mBurstState = 4;
                    return;
                default:
                    return;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        private void requestNextCapture() {
            if (StateMachine.this.mLastPhotoSavingRequest != null && StateMachine.this.mLastPhotoSavingRequest.mCommonStatus.savedFileType == SavingTaskManager.SavedFileType.BURST) {
                StateMachine.this.mLastPhotoSavingRequest.setFinalInSavingGroup(false);
            }
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderCreatePhotoSavingRequest = StateMachine.this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.BURST);
            photoSavingRequestBuilderCreatePhotoSavingRequest.setRequestId(StateMachine.this.mViewFinder.getRequestId(false));
            photoSavingRequestBuilderCreatePhotoSavingRequest.setSaveTimeForCaptureGroup(this.mDataString);
            photoSavingRequestBuilderCreatePhotoSavingRequest.setCaptureIdForCaptureGourp(this.mIndex);
            StateMachine.this.doCapture(photoSavingRequestBuilderCreatePhotoSavingRequest);
            this.mIndex++;
        }

        private void requestStopBurstCapture() {
            StateMachine.this.mCameraDeviceHandler.finishBurst();
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StatePhotoReady(StateMachine.this, true), new Object[0]);
            CapturePerformanceLogger.setNumOfBurstTaken(this.mIndex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVideoChapterThumbnailToViewFinder() {
        if (this.mChapterThumbnail == null || !this.mViewFinder.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mLastVideoSavingRequest != null) {
            this.mChapterThumbnail.setOrientation(this.mLastVideoSavingRequest.mCommonStatus.orientation);
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ADD_VIDEO_CHAPTER, this.mChapterThumbnail);
        this.mChapterThumbnail = null;
    }

    class StateVideoBase extends State {
        StateVideoBase() {
            super();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
            StateMachine.this.mChapterThumbnail = new ChapterThumbnail((byte[]) objArr[0], (Integer) objArr[1], (Rect) objArr[2]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoEnabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            StateMachine.this.mIsSemiAutoEnabled = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            StoreDataResult storeDataResult = (StoreDataResult) objArr[0];
            if (StateMachine.this.mActivity.isOneShot()) {
                StateMachine.this.onOneShotStoreCompleted(storeDataResult);
            } else if (StateMachine.this.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION && !PlatformCapability.hasDeviceError()) {
                ApplicationLauncher.launchVideoEditor(StateMachine.this.mActivity, storeDataResult);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnRecordingError(Object... objArr) {
            StateMachine.this.doHandleRecordingError();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCritical(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isRecorderWorking()) {
                StateMachine.this.doStopRecording(false);
            }
            super.handleOnHeatedOverCritical(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePrepareZoom(Object... objArr) {
            VariableIndex variableIndex = (VariableIndex) objArr[0];
            handleClearFocus(objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_START, Integer.valueOf(variableIndex.getIndex()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
            VariableIndex variableIndex = (VariableIndex) objArr[0];
            StateMachine.this.mCameraDeviceHandler.setZoomAndCommit(variableIndex.getRatio());
            StateMachine.this.onZoomChange(variableIndex.getIndex());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_STOP, new Object[0]);
        }
    }

    class StateLowFrameRateVideoRecordingInSuperSlowMotion extends StateVideoBase {
        private static final String TAG = "StateMachine.StateLowFrameRateVideoRecordingInSuperSlowMotion";

        public StateLowFrameRateVideoRecordingInSuperSlowMotion() {
            super();
            this.mCaptureState = CaptureState.STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION;
            StateMachine.this.mIsVideoRecording = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateLowFrameRateVideoRecordingInSuperSlowMotion");
            }
            super.entry();
            StateMachine.this.checkThermalWarning();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleTriggerSlowMotion(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.startSuperSlowMotion();
            StateMachine.this.changeTo(StateMachine.this.new StateHighFrameRateVideoRecordingInSuperSlowMotion(), objArr);
            StateMachine.access$11608(StateMachine.this);
            StateMachine.this.mViewFinder.startSlowMotionFeedbackAnimation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.pauseVideoRecording(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStopRecording(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStopRecordingSlowMotion(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnReachBatteryLimit(Object... objArr) {
            StateMachine.this.doStopRecording(false);
            StateMachine.this.changeTo(new StateFatal(true, false), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
    }

    class StateHighFrameRateVideoRecordingInSuperSlowMotion extends StateVideoBase {
        public static final String TAG = "StateMachine.StateHighFrameRateVideoRecordingInSuperSlowMotion";
        private boolean mAlreadyHighFrameRateRecordingDone;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePrepareZoom(Object... objArr) {
        }

        public StateHighFrameRateVideoRecordingInSuperSlowMotion() {
            super();
            this.mAlreadyHighFrameRateRecordingDone = false;
            this.mCaptureState = CaptureState.STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION;
            StateMachine.this.mIsVideoRecording = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.pauseVideoRecording(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleHighFameRateRecordingDone(Object... objArr) {
            this.mAlreadyHighFrameRateRecordingDone = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSlowMotionFeedbackAnimationEnd(Object... objArr) {
            if (!this.mAlreadyHighFrameRateRecordingDone) {
                StateMachine.this.changeTo(StateMachine.this.new StateWaitForHighFrameRateVideoRecordingDone(), new Object[0]);
                return;
            }
            switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                case SUPER_SLOW_MOTION:
                    StateMachine.this.changeTo(StateMachine.this.new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                    break;
                case SUPER_SLOW_SHOT:
                    StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), new Object[0]);
                    StateMachine.this.doStopRecording(false);
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnReachBatteryLimit(Object... objArr) {
            StateMachine.this.doStopRecording(false);
            StateMachine.this.changeTo(new StateFatal(true, false), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
    }

    class StateWaitForHighFrameRateVideoRecordingDone extends StateVideoBase {
        private static final String TAG = "StateMachine.StateWaitForHighFrameRateVideoRecordingDone";

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePrepareZoom(Object... objArr) {
        }

        public StateWaitForHighFrameRateVideoRecordingDone() {
            super();
            this.mCaptureState = CaptureState.STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleHighFameRateRecordingDone(Object... objArr) {
            switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) StateMachine.this.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                case SUPER_SLOW_MOTION:
                    StateMachine.this.changeTo(StateMachine.this.new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                    break;
                case SUPER_SLOW_SHOT:
                    StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), new Object[0]);
                    StateMachine.this.doStopRecording(false);
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStopRecordingSlowMotion(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.pauseVideoRecording(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), objArr);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnReachBatteryLimit(Object... objArr) {
            StateMachine.this.doStopRecording(false);
            StateMachine.this.changeTo(new StateFatal(true, false), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }
    }

    class StateVideoStopping extends StateVideoBase {
        private static final String TAG = "StateMachine.StateVideoStopping";
        private final RecorderController.Result mResult;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePerformZoom(Object... objArr) {
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePrepareZoom(Object... objArr) {
        }

        public StateVideoStopping(RecorderController.Result result) {
            super();
            this.mCaptureState = CaptureState.STATE_VIDEO_STOPPING;
            this.mResult = result;
        }

        public StateVideoStopping(StateMachine stateMachine) {
            this(RecorderController.Result.SUCCESS);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(this.mResult), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }
    }

    class StateVideoRecording extends StateVideoBase {
        private static final String TAG = "StateMachine.StateVideoRecording";
        private boolean mAlreadyRequestStop;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartRecording(Object... objArr) {
        }

        protected boolean isPaused() {
            return false;
        }

        private StateVideoRecording() {
            super();
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            StateMachine.this.mIsVideoRecording = true;
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        public StateVideoRecording(boolean z) {
            super();
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
            StateMachine.this.mIsVideoRecording = true;
            this.mAlreadyRequestStop = z;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateVideoRecording");
            }
            super.entry();
            StateMachine.this.checkThermalWarning();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            if (((Boolean) objArr[0]).booleanValue()) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
                StateMachine.this.switchVideoFaceDetection();
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING);
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePauseRecording(Object... objArr) {
            if (this.mAlreadyRequestStop) {
                return;
            }
            StateMachine.this.doPauseRecording();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            if (this.mAlreadyRequestStop || StateMachine.this.mActivity.getLaunchCondition().getOneShotMode().isEnabled() || !StateMachine.this.mCameraDeviceHandler.canRecorderTakeSnapshot()) {
                return;
            }
            StateMachine.this.changeTo(new StateVideoCaptureWhileRecording(isPaused()), new Object[0]);
            this.mAlreadyRequestStop = true;
            StateMachine.this.doCaptureWhileRecording();
            ResearchUtil.getInstance().incrementCountSnapshotInRecording();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            this.mAlreadyRequestStop = true;
            StateMachine.this.pauseVideoRecording(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            this.mAlreadyRequestStop = true;
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), new Object[0]);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStopRecording(Object... objArr) {
            this.mAlreadyRequestStop = true;
            StateMachine.this.changeTo(new StateVideoStopping(StateMachine.this), new Object[0]);
            StateMachine.this.doStopRecording(false);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
            super.handleOnOnePreviewFrameUpdated(objArr);
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
            StateMachine.this.requestVideoSmileCapture();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSetSelectedObjectPosition(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isCameraDeviceStatusReady()) {
                Rect rect = (Rect) objArr[1];
                if (StateMachine.this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                    StateMachine.this.doStartObjectTracking(rect);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnObjectTracked(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDeselectObjectPosition(Object... objArr) {
            StateMachine.this.doStopObjectTracking();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
            StateMachine.this.notifyCoolingUltraLow(!StateMachine.this.isStorageFull(StateMachine.this.getCurrentStorage()));
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeSelectedFace(Object... objArr) {
            StateMachine.this.doChangeSelectedFace((Point) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogOpened(Object... objArr) {
            if (objArr != null && objArr.length != 0) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, (ViewFinder.UiComponentKind) objArr[0]);
            }
            StateMachine.this.changeTo(new StateOperationRestricted(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnReachBatteryLimit(Object... objArr) {
            this.mAlreadyRequestStop = true;
            StateMachine.this.doStopRecording(false);
            StateMachine.this.changeTo(new StateFatal(true, false), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCritical(Object... objArr) {
            this.mAlreadyRequestStop = true;
            super.handleOnHeatedOverCritical(objArr);
        }
    }

    class StateVideoCaptureWhileRecording extends State {
        private final boolean mIsPaused;
        private boolean mIsPausingRequested;
        private boolean mIsReturnToVideoRecordingRequired;

        private StateVideoCaptureWhileRecording(boolean z) {
            super();
            this.mIsReturnToVideoRecordingRequired = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_CAPTURE_WHILE_RECORDING;
            this.mIsPaused = z;
            this.mIsPausingRequested = z;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnTakePictureDone(Object... objArr) {
            StateMachine.this.requestStorePicture((RequestFactory.PhotoSavingRequestBuilder) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreRequested(Object... objArr) {
            if (this.mIsPaused) {
                if (this.mIsPausingRequested) {
                    StateMachine.this.changeTo(new StateVideoRecordingPausing(), objArr);
                } else {
                    StateMachine.this.mCameraDeviceHandler.requestOnePreviewFrame();
                    StateMachine.this.doResumeRecording();
                }
            } else if (!this.mIsPausingRequested) {
                StateMachine.this.changeTo(StateMachine.this.new StateVideoRecording(this.mIsReturnToVideoRecordingRequired), objArr);
            } else {
                StateMachine.this.doPauseRecording();
            }
            if (this.mIsReturnToVideoRecordingRequired) {
                StateMachine.this.doStopRecording(false);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePauseRecording(Object... objArr) {
            this.mIsPausingRequested = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResumeRecording(Object... objArr) {
            this.mIsPausingRequested = false;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.pauseVideoRecording(objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStopRecording(Object... objArr) {
            this.mIsReturnToVideoRecordingRequired = true;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnShutterDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            synchronized (this) {
                if (StateMachine.this.mContentsViewController != null) {
                    StateMachine.this.mContentsViewController.setClickThumbnailProgressListener(null);
                    photoSavingRequestBuilder.setRequestId(StateMachine.this.mContentsViewController.createProvisionalContentFrame());
                } else {
                    photoSavingRequestBuilder.setRequestId(-1);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
            StateMachine.this.mChapterThumbnail = new ChapterThumbnail((byte[]) objArr[0], (Integer) objArr[1], (Rect) objArr[2]);
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageError(Object... objArr) {
            if (((Storage.StorageState) objArr[1]) == Storage.StorageState.CORRUPT) {
                CamLog.w("Storage corruption : state = " + StateMachine.this.mCurrentState);
                if (CamLog.DEBUG) {
                    throw new IllegalStateException();
                }
            }
            StateMachine.this.doStopRecording(true);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnVideoRecordingDone(Object... objArr) {
            StateMachine.this.changeTo(new StateVideoStore(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnFaceDetected(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnRecordingError(Object... objArr) {
            StateMachine.this.doHandleRecordingError();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnReachBatteryLimit(Object... objArr) {
            StateMachine.this.doStopRecording(true);
            StateMachine.this.changeTo(new StateFatal(true, false), objArr);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, true);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCritical(Object... objArr) {
            if (StateMachine.this.mCameraDeviceHandler.isRecorderWorking()) {
                StateMachine.this.doStopRecording(false);
            }
            super.handleOnHeatedOverCritical(objArr);
        }
    }

    class StateVideoStore extends StateVideoBase {
        private final RecorderController.Result mResult;

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOnePreviewFrameUpdated(Object... objArr) {
        }

        private StateVideoStore(RecorderController.Result result) {
            super();
            this.mCaptureState = CaptureState.STATE_VIDEO_STORE;
            this.mResult = result;
        }

        private StateVideoStore(StateMachine stateMachine) {
            this(RecorderController.Result.SUCCESS);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreRequested(Object... objArr) {
            if (!StateMachine.this.mActivity.isOneShotVideo() || StateMachine.this.mActivity.isInLockTaskMode()) {
                StateMachine.this.changeTo(new StateVideoReady(StateMachine.this), new Object[0]);
                switch (this.mResult) {
                    case MAX_DURATION_REACHED:
                        StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_DURATION_REACHED, new Object[0]);
                        break;
                    case MAX_FILESIZE_REACHED:
                        StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED, new Object[0]);
                        break;
                }
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            if (StateMachine.this.mActivity.isOneShot()) {
                StateMachine.this.onOneShotStoreCompleted((StoreDataResult) objArr[0]);
            }
        }
    }

    class StateModeChanging extends StatePhotoBase {
        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
        }

        private StateModeChanging() {
            super();
            this.mCaptureState = CaptureState.STATE_MODE_CHANGING;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateModeChanging");
            }
            if (!PlatformCapability.isSceneRecognitionSupported(StateMachine.this.getCurrentCameraId())) {
                StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
            }
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.notifyStateBlockedToWearable();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinishTransitionOperation(Object... objArr) {
            AnimationRequest animationRequest = (AnimationRequest) objArr[0];
            switch (animationRequest.mDegree) {
                case CANCEL:
                    StateMachine.this.changeToStandby();
                    break;
                case EXEC:
                    if (animationRequest.mType == AnimationRequest.AnimationType.SWITCH_TOUCH || animationRequest.mTarget.isFront() != animationRequest.mFrom.isFront()) {
                        StateMachine.this.switchCamera(animationRequest.mTarget, animationRequest.mType);
                        break;
                    } else if (StateMachine.this.getCurrentCapturingMode() != animationRequest.mTarget) {
                        StateMachine.this.requestChangeModeTo(animationRequest.mTarget, animationRequest.mType);
                        break;
                    }
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnSemiAutoDisabled(Object... objArr) {
            super.handleOnSemiAutoDisabled(objArr);
            StateMachine.this.mCameraDeviceHandler.setAmberBlueColorAndCommit(0);
            StateMachine.this.mCameraDeviceHandler.setBrightnessAndCommit(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(0);
            LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(0);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }
    }

    class StatePause extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePause";

        @Override // com.sonyericsson.android.camera.controller.StateMachine.StatePhotoBase, com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
        }

        private StatePause(boolean z) {
            CapturingMode capturingMode;
            super();
            this.mCaptureState = CaptureState.STATE_PAUSE;
            StateMachine.this.mViewFinder.hideDisplayFlashScreen();
            if (!StateMachine.this.mCameraDeviceHandler.isRecorderWorking()) {
                StateMachine.this.mCameraDeviceHandler.releaseRecorder();
            }
            StateMachine.this.mCameraDeviceHandler.stopFaceDetection();
            StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
            StateMachine.this.mCameraDeviceHandler.stopFusionMonitoring();
            if (StateMachine.this.mObjectTracking != null) {
                StateMachine.this.doStopObjectTracking();
            }
            if (!StateMachine.this.mPendingTaskListForStandby.isEmpty()) {
                StateMachine.this.mPendingTaskListForStandby.clear();
            }
            StateMachine.this.mCameraDeviceHandler.stopPreview();
            StateMachine.this.mStorage.removeStorageStateListener(StateMachine.this.mStorageStateListener);
            StateMachine.this.mUserSettings.commit();
            StateMachine.this.mVirtualKeyEventDispatcher.stop();
            if (!StateMachine.this.mActivity.getLaunchCondition().getOneShotMode().isEnabled() && (capturingMode = (CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)) != null) {
                StateMachine.this.mLastSettings.setCapturingMode((ModeSelectorInternalMode.exists(capturingMode) || capturingMode.equals(CapturingMode.FRONT_PHOTO)) ? CapturingMode.SCENE_RECOGNITION : capturingMode);
                StateMachine.this.mLastSettings.writePauseTime();
                StateMachine.this.mLastSettings.save();
            }
            if (z) {
                StateMachine.this.removeChangeCameraModeTask();
            }
            StateMachine.this.removeStartRecordingTask();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StatePause");
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResume(Object... objArr) {
            CameraDeviceHandler.CameraSessionId cameraSessionId = (CameraDeviceHandler.CameraSessionId) objArr[1];
            StartupAction startupAction = (StartupAction) objArr[2];
            if (StateMachine.this.mCameraDeviceHandler.isCameraDisabled()) {
                StateMachine.this.mViewFinder.showMessageDialog(DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, new Object[0]);
            } else {
                FastCapture fastCapture = (FastCapture) objArr[0];
                StateMachine.this.changeTo(new StateResume(cameraSessionId, fastCapture, null, startupAction), fastCapture, Boolean.valueOf(StateMachine.this.hasRemainSavingRequest()));
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinalize(Object... objArr) {
            StateMachine.this.changeTo(new StateFinalize(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnTakePictureDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            if (CamLog.DEBUG) {
                CamLog.d("invoke id:" + photoSavingRequestBuilder.getRequestId());
            }
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
        }
    }

    class StateWarning extends StatePhotoBase {
        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
        }

        private StateWarning() {
            super();
            StateMachine.this.mIsVideoRecording = false;
            StateMachine.this.checkThermalWarning();
            StateMachine.this.cleanupPendingState();
            this.mCaptureState = CaptureState.STATE_WARNING;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateWarning");
            }
            StateMachine.this.mPredictiveApplier.entrySuppressor(this);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            StateMachine.this.mViewFinder.clearHintText();
            StateMachine.this.mPredictiveApplier.leaveSuppressor(this);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleChangeAngleStart(Object... objArr) {
            StateMachine.this.doZoomChangeAngle();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverCoolingUltraLow(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setUltraLowPower();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCapture(Object... objArr) {
            StateMachine.this.mViewFinder.onCaptureDone();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleCaptureCancel(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogOpened(Object... objArr) {
            if (objArr != null && objArr.length != 0) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, (ViewFinder.UiComponentKind) objArr[0]);
            }
            StateMachine.this.changeTo(new StateOperationRestricted(), objArr);
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
            if (StateMachine.this.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU) {
                StateMachine.this.mActivity.getLaunchCondition().clearExtraOperation();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
            if (CamLog.DEBUG) {
                CamLog.d("invoke current" + StateMachine.this.getCurrentCapturingMode());
            }
            if (((Boolean) objArr[0]).booleanValue()) {
                StateMachine.this.switchSceneRecognition(StateMachine.this.isStorageWritable(StateMachine.this.getCurrentStorage()));
                StateMachine.this.switchVideoFaceDetection();
            }
            switch (StateMachine.this.getCurrentCapturingMode()) {
                case NORMAL:
                case FRONT_PHOTO:
                case SCENE_RECOGNITION:
                case SUPERIOR_FRONT:
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.PHOTO_READY);
                    break;
                case SLOW_MOTION:
                case FRONT_VIDEO:
                case VIDEO:
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewFinder.HeadUpDisplaySetupState.VIDEO_READY);
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleKeyMenu(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewFinder.UiComponentKind.SETTING_DIALOG);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStorageMounted(Object... objArr) {
            StateMachine.this.changeToStandby();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleSwitchCamera(Object... objArr) {
            StateMachine.this.switchCamera((AnimationRequest.AnimationType) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnHeatedOverWarningExtra(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.enableFpsLimitation();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleStartTransitionOperation(Object... objArr) {
            if (((AnimationRequest) objArr[0]).mDegree == AnimationRequest.AnimationDegree.START) {
                StateMachine.this.changeTo(new StateModeChanging(), new Object[0]);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnAmberBlueColorChanged(Object... objArr) {
            StateMachine.this.updateAmberBlueColor(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnBrightnessChanged(Object... objArr) {
            StateMachine.this.updateBrightness(((Float) objArr[0]).floatValue());
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestUpdateHighSensitivityFusionMode(Object... objArr) {
            StateMachine.this.updateFusionModeSetting((FusionMode) objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnEvfPrepared(Object... objArr) {
            StateMachine.this.mCameraDeviceHandler.setPreviewSurface(((Evf) objArr[0]).asSurface());
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }
    }

    class StateFatal extends State {
        private static final String TAG = "StateMachine.StateFatal";
        private boolean mIsSaving;
        private boolean mLazyAbort;
        private LazyAbortTask mLazyAbortTask;
        private StoreDataResult mStoreDataResult;

        private StateFatal(boolean z, boolean z2) {
            super();
            this.mIsSaving = false;
            this.mLazyAbort = false;
            this.mStoreDataResult = null;
            this.mLazyAbortTask = null;
            this.mCaptureState = CaptureState.STATE_FATAL;
            this.mIsSaving = z;
            this.mLazyAbort = z2;
            if (!StateMachine.this.mCameraDeviceHandler.isRecorderWorking()) {
                StateMachine.this.mCameraDeviceHandler.releaseRecorder();
            }
            StateMachine.this.mCameraDeviceHandler.stopFaceDetection();
            StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
            if (StateMachine.this.mObjectTracking != null) {
                StateMachine.this.doStopObjectTracking();
            }
            if (!StateMachine.this.mPendingTaskListForStandby.isEmpty()) {
                StateMachine.this.mPendingTaskListForStandby.clear();
            }
            StateMachine.this.mCameraDeviceHandler.stopPreview();
            StateMachine.this.removeChangeCameraModeTask();
            StateMachine.this.removeStartRecordingTask();
            if (!this.mLazyAbort || StateMachine.this.mViewFinder.isMessageDialogOpened()) {
                return;
            }
            doLazyAbort();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnOrientationChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, objArr[0]);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStoreCompleted(Object... objArr) {
            super.handleOnStoreCompleted(objArr);
            this.mStoreDataResult = (StoreDataResult) objArr[0];
            if (StateMachine.this.mViewFinder.isMessageDialogOpened()) {
                return;
            }
            if (StateMachine.this.mActivity.isOneShot()) {
                StateMachine.this.onOneShotStoreCompleted(this.mStoreDataResult);
            } else {
                StateMachine.this.mActivity.terminateApplication();
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleDialogClosed(Object... objArr) {
            if (objArr == null || objArr.length == 0 || objArr[0] != ViewFinder.UiComponentKind.FATAL_ALERT_DIALOG) {
                return;
            }
            if (this.mLazyAbort) {
                doLazyAbort();
                return;
            }
            if (!this.mIsSaving || this.mStoreDataResult != null) {
                StateMachine.this.mActivity.terminateApplication();
            } else {
                if (this.mStoreDataResult == null || !StateMachine.this.mActivity.isOneShot()) {
                    return;
                }
                StateMachine.this.onOneShotStoreCompleted(this.mStoreDataResult);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFinalize(Object... objArr) {
            StateMachine.this.changeTo(new StateFinalize(), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
            StateMachine.this.mActivity.terminateApplication();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void exit() {
            if (this.mLazyAbortTask != null) {
                StateMachine.this.mHandler.removeCallbacks(this.mLazyAbortTask);
                this.mLazyAbortTask = null;
            }
        }

        private void doLazyAbort() {
            if (this.mLazyAbortTask != null) {
                StateMachine.this.mHandler.removeCallbacks(this.mLazyAbortTask);
            }
            this.mLazyAbortTask = new LazyAbortTask();
            StateMachine.this.mHandler.postDelayed(this.mLazyAbortTask, 4000L);
        }

        private class LazyAbortTask implements Runnable {
            private LazyAbortTask() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke LazyAbortTask");
                }
                StateMachine.this.mActivity.terminateApplication();
            }
        }
    }

    class StateFinalize extends State {
        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleRequestSetupHeadUpDisplay(Object... objArr) {
        }

        private StateFinalize() {
            super();
            this.mCaptureState = CaptureState.STATE_FINALIZE;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void entry() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StateFinalize");
            }
            StateMachine.this.mGestureShutter.release();
            StateMachine.this.storeSavingRequestList();
            StateMachine.this.mObjectTracking = null;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnTakePictureDone(Object... objArr) {
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = (RequestFactory.PhotoSavingRequestBuilder) objArr[0];
            if (CamLog.DEBUG) {
                CamLog.d("invoke id:" + photoSavingRequestBuilder.getRequestId());
            }
            StateMachine.this.requestStorePicture(photoSavingRequestBuilder);
        }
    }

    class StateWaitingPreProcessDone extends State {
        private final FastCapture mFastCapture;

        private StateWaitingPreProcessDone(FastCapture fastCapture) {
            super();
            this.mCaptureState = CaptureState.STATE_WAITING_PRE_PROCESS_DONE;
            this.mFastCapture = fastCapture;
        }

        private void cancelPreProcess() {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.mCameraDeviceHandler.cancelPreProcessState();
            StateMachine.this.startFastCapture(this.mFastCapture, StartupAction.NONE);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnInitialAutoFocusDone(Object... objArr) {
            cancelPreProcess();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnPreTakePictureDone(Object... objArr) {
            ((RequestFactory.PhotoSavingRequestBuilder) objArr[0]).close();
            StateMachine.this.mCameraDeviceHandler.prepareCaptureImageReader(null);
            cancelPreProcess();
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handlePause(Object... objArr) {
            StateMachine.this.changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleFusionConditionChanged(Object... objArr) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, objArr);
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleOnStorageUngranted(Object... objArr) {
            StateMachine.this.mUserSettings.set(DestinationToSave.EMMC);
            if (StateMachine.this.mViewFinder != null) {
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, true);
            }
        }
    }

    public synchronized void sendEvent(TransitterEvent transitterEvent, Object... objArr) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke TransitterEvent:" + transitterEvent + ", current state:" + this.mCurrentState);
        }
        switch (transitterEvent) {
            case EVENT_INITIALIZE:
                this.mCurrentState.handleInitialize(objArr);
                break;
            case EVENT_RESUME:
                this.mCurrentState.handleResume(objArr);
                break;
            case EVENT_RESUME_TIMEOUT:
                this.mCurrentState.handleResumeTimeout(objArr);
                break;
            case EVENT_PAUSE:
                this.mCurrentState.handlePause(objArr);
                break;
            case EVENT_FINALIZE:
                this.mCurrentState.handleFinalize(objArr);
                break;
            case EVENT_ON_HEATED_OVER_WARNING:
                this.mCurrentState.handleOnHeatedOverWarning(objArr);
                break;
            case EVENT_ON_HEATED_OVER_WARNING_EXTRA:
                this.mCurrentState.handleOnHeatedOverWarningExtra(objArr);
                break;
            case EVENT_ON_HEATED_OVER_COOLING_LOW:
                this.mCurrentState.handleOnHeatedOverCoolingLow(objArr);
                break;
            case EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW:
                this.mCurrentState.handleOnHeatedOverCoolingUltraLow(objArr);
                break;
            case EVENT_ON_REACH_BATTERY_LIMIT:
                this.mCurrentState.handleOnReachBatteryLimit(new Object[0]);
                break;
            case EVENT_ON_REACH_BATTERY_LOW:
                this.mCurrentState.handleOnReachBatteryLow(new Object[0]);
                break;
            case EVENT_ON_BATTERY_LEVEL_CHANGED:
                this.mCurrentState.handleOnReachBatteryLevelChanged(Integer.valueOf(((Integer) objArr[0]).intValue()));
                break;
            case EVENT_ON_HEATED_OVER_CRITICAL:
                this.mCurrentState.handleOnHeatedOverCritical(objArr);
                break;
            case EVENT_ON_HEATED_OVER_NORMAL:
                this.mCurrentState.handleOnHeatedOverNormal(objArr);
                break;
            case EVENT_ON_EVF_PREPARED:
                this.mCurrentState.handleOnEvfPrepared(objArr);
                break;
            case EVENT_ON_INITIAL_AUTO_FOCUS_DONE:
                this.mCurrentState.handleOnInitialAutoFocusDone(objArr);
                break;
            case EVENT_ON_AUTO_FOCUS_DONE:
                this.mCurrentState.handleOnAutoFocusDone(objArr);
                break;
            case EVENT_ON_PRE_SHUTTER_DONE:
                this.mCurrentState.handleOnPreShutterDone(objArr);
                break;
            case EVENT_ON_SHUTTER_DONE:
                this.mCurrentState.handleOnShutterDone(objArr);
                break;
            case EVENT_ON_PREPARE_BURST_DONE:
                this.mCurrentState.handleOnPrepareBurstDone(objArr);
                break;
            case EVENT_ON_PRE_TAKE_PICTURE_DONE:
                this.mCurrentState.handleOnPreTakePictureDone(objArr);
                break;
            case EVENT_ON_TAKE_PICTURE_DONE:
                this.mCurrentState.handleOnTakePictureDone(objArr);
                break;
            case EVENT_ON_VIDEO_RECORDING_DONE:
                this.mCurrentState.handleOnVideoRecordingDone(objArr);
                break;
            case EVENT_TOUCH_CONTENT_PROGRESS:
                this.mCurrentState.handleTouchContentProgress();
                break;
            case EVENT_ON_STORE_REQUESTED:
                this.mCurrentState.handleOnStoreRequested(objArr);
                break;
            case EVENT_ON_STORE_COMPLETED:
                this.mCurrentState.handleOnStoreCompleted(objArr);
                break;
            case EVENT_KEY_MENU:
                this.mCurrentState.handleKeyMenu(objArr);
                break;
            case EVENT_START_TRANSITION_OPERATION:
                this.mCurrentState.handleStartTransitionOperation(objArr);
                break;
            case EVENT_FINISH_TRANSITION_OPERATION:
                this.mCurrentState.handleFinishTransitionOperation(objArr);
                break;
            case EVENT_CLEAR_FOCUS:
                this.mCurrentState.handleClearFocus(objArr);
                break;
            case EVENT_DIALOG_OPENED:
                this.mCurrentState.handleDialogOpened(objArr);
                break;
            case EVENT_DIALOG_CLOSED:
                this.mCurrentState.handleDialogClosed(objArr);
                break;
            case EVENT_STORAGE_ERROR:
                this.mCurrentState.handleStorageError(objArr);
                break;
            case EVENT_STORAGE_MOUNTED:
                this.mCurrentState.handleStorageMounted(objArr);
                break;
            case EVENT_STORAGE_UNGRANTED:
                this.mCurrentState.handleOnStorageUngranted(objArr);
                break;
            case EVENT_SET_TOUCHED_POSITION:
                this.mCurrentState.handleSetTouchedPosition(objArr);
                break;
            case EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY:
                this.mCurrentState.handleRequestSetupHeadUpDisplay(objArr);
                break;
            case EVENT_ON_RECORDING_START_WAIT_DONE:
                this.mCurrentState.handleOnRecordingStartWaitDone(objArr);
                break;
            case EVENT_ON_RECORDING_ERROR:
                this.mCurrentState.handleOnRecordingError(objArr);
                break;
            case EVENT_CHANGE_SELECTED_FACE:
                this.mCurrentState.handleChangeSelectedFace(objArr);
                break;
            case EVENT_SET_SELECTED_OBJECT_POSITION:
                this.mCurrentState.handleSetSelectedObjectPosition(objArr);
                break;
            case EVENT_DESELECT_OBJECT_POSITION:
                this.mCurrentState.handleDeselectObjectPosition(objArr);
                break;
            case EVENT_START_AF_AFTER_OBJECT_TRACKED:
                this.mCurrentState.handleStartAfAfterObjectTracked(objArr);
                break;
            case EVENT_ON_BURST_SHUTTER_DONE:
                this.mCurrentState.handleOnBurstShutterDone(objArr);
                break;
            case EVENT_ON_BURST_STORE_COMPLETED:
                this.mCurrentState.handleOnBurstStoreCompleted(objArr);
                break;
            case EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED:
                this.mCurrentState.handleOnPredictiveCaptureGroupStoreCompleted(objArr);
                break;
            case EVENT_ON_ONE_PREVIEW_FRAME_UPDATED:
                this.mCurrentState.handleOnOnePreviewFrameUpdated(objArr);
                break;
            case EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED:
                this.mCurrentState.handleOnContinuousPreviewFrameUpdated(objArr);
                break;
            case EVENT_ON_SWITCH_CAMERA:
                this.mCurrentState.handleSwitchCamera(objArr);
                break;
            case EVENT_CANCEL_TOUCHED_POSITION:
                this.mCurrentState.handleCancelTouchedPosition(objArr);
                break;
            case EVENT_ON_SEMIAUTO_ENABLED:
                this.mCurrentState.handleOnSemiAutoEnabled(objArr);
                break;
            case EVENT_ON_SEMIAUTO_DISABLED:
                this.mCurrentState.handleOnSemiAutoDisabled(objArr);
                break;
            case EVENT_ON_AMBER_BLUE_COLOR_CHANGED:
                this.mCurrentState.handleOnAmberBlueColorChanged(objArr);
                break;
            case EVENT_ON_BRIGHTNESS_CHANGED:
                this.mCurrentState.handleOnBrightnessChanged(objArr);
                break;
            case EVENT_SELFTIMER_CANCEL:
                this.mCurrentState.handleSelfTimerCancel(objArr);
                break;
            case EVENT_ON_CAMERA_DEVICE_OPENED:
                this.mCurrentState.handleOnCameraDeviceOpened(objArr);
                break;
            case EVENT_ON_CAMERA_DEVICE_CLOSED:
                this.mCurrentState.handleOnCameraDeviceClosed(objArr);
                break;
            case EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE:
                this.mCurrentState.handleStopRecordingSlowMotion(objArr);
                break;
            case EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END:
                this.mCurrentState.handleSlowMotionFeedbackAnimationEnd(objArr);
                break;
            case EVENT_HIGH_FRAME_RATE_RECORDING_DONE:
                this.mCurrentState.handleHighFameRateRecordingDone(objArr);
                break;
            case EVENT_ANGLE_CHANGE_START:
                this.mCurrentState.handleChangeAngleStart(objArr);
                break;
            case EVENT_CAPTURE_READY:
                this.mCurrentState.handleCaptureReady(objArr);
                break;
            case EVENT_START_CAPTURE_COUNTDOWN:
                this.mCurrentState.handleStartCaptureCountDown(objArr);
                break;
            case EVENT_CAPTURE:
                this.mCurrentState.handleCapture(objArr);
                break;
            case EVENT_CAPTURE_BURST:
                this.mCurrentState.handleCaptureBurst(objArr);
                break;
            case EVENT_CAPTURE_CANCEL:
                this.mCurrentState.handleCaptureCancel(objArr);
                break;
            case EVENT_RECORD_READY:
                this.mCurrentState.handleRecordReady(objArr);
                break;
            case EVENT_START_RECORDING:
                this.mCurrentState.handleStartRecording(objArr);
                break;
            case EVENT_STOP_RECORDING:
                this.mCurrentState.handleStopRecording(objArr);
                break;
            case EVENT_RESUME_RECORDING:
                this.mCurrentState.handleResumeRecording(objArr);
                break;
            case EVENT_PAUSE_RECORDING:
                this.mCurrentState.handlePauseRecording(objArr);
                break;
            case EVENT_TRIGGER_SLOW_MOTION:
                this.mCurrentState.handleTriggerSlowMotion(objArr);
                break;
            case EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE:
                this.mCurrentState.handleRequestUpdateHighSensitivityFusionMode(objArr);
                break;
            case EVENT_ON_FUSION_CONDITION_CHANGED:
                this.mCurrentState.handleFusionConditionChanged(objArr);
                break;
            case EVENT_STORAGE_READY_STATE_CHANGED:
                this.mCurrentState.handleOnStorageReadyStateChanged(objArr);
                break;
            case EVENT_CHANGE_CAPTURING_MODE:
                this.mCurrentState.handleChangeCapturingMode(objArr);
                break;
            case EVENT_ZOOM_PREPARE:
                this.mCurrentState.handlePrepareZoom(objArr);
                break;
            case EVENT_ZOOM_PERFORM:
                this.mCurrentState.handlePerformZoom(objArr);
                break;
            case EVENT_ZOOM_FINISH:
                this.mCurrentState.handleFinishZoom(objArr);
                break;
        }
    }

    public synchronized void sendStaticEvent(StaticEvent staticEvent, Object... objArr) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke StaticEvent:" + staticEvent + ", current state:" + this.mCurrentState);
        }
        switch (staticEvent) {
            case EVENT_ON_PHOTO_STACK_INITIALIZED:
                this.mContentsViewController = (ContentsViewController) objArr[0];
                if (this.mCurrentState.getCaptureState().isRecording()) {
                    this.mContentsViewController.disableClick();
                }
                storeSavingRequestList();
                break;
            case EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED:
                switch ((ViewFinder.HeadUpDisplaySetupState) objArr[0]) {
                    case PHOTO_READY:
                    case VIDEO_READY:
                        if (!this.mViewFinder.isMessageDialogOpened()) {
                            calculateRemainStorage();
                            break;
                        }
                        break;
                    case VIDEO_RECORDING:
                        VideoSize videoSize = (VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
                        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, Integer.valueOf((int) this.mLastVideoSavingRequest.mVideoStatus.maxDurationMills), Boolean.valueOf(videoSize != null && videoSize.isConstraint()), true, (VideoHdr) this.mUserSettings.get(UserSettingKey.VIDEO_HDR));
                        updateRecordingProgress(0);
                        this.mCameraDeviceHandler.requestOnePreviewFrame();
                        break;
                }
                if (isFusionMonitoringNeeded()) {
                    sendEvent(TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED, this.mCameraDeviceHandler.getLatestFusionResult());
                    break;
                }
                break;
            case EVENT_ON_SCENE_MODE_CHANGED:
                this.mCurrentState.handleOnSceneModeChanged(objArr);
                break;
            case EVENT_ON_FACE_DETECTED:
                this.mCurrentState.handleOnFaceDetected(objArr);
                break;
            case EVENT_ON_OBJECT_TRACKED:
                this.mCurrentState.handleOnObjectTracked(objArr);
                break;
            case EVENT_ON_ORIENTATION_CHANGED:
                this.mCurrentState.handleOnOrientationChanged(objArr);
                break;
            case EVENT_ON_LAZY_INITIALIZATION_TASK_RUN:
                this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, new Object[0]);
                this.mCurrentState.handleOnLazyInitializationTaskRun(objArr);
                break;
            case EVENT_ON_OBJECT_TRACKING_LOST:
                this.mCurrentState.handleOnObjectLost(objArr);
                break;
            case EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED:
                this.mGestureShutter.handleSettingsChanged(((Boolean) objArr[0]).booleanValue());
                break;
            case EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED:
                this.mIsSdPermissionFinished = true;
                break;
            case EVENT_ON_PREVIEW_STARTED:
                this.mCurrentState.handleOnPreviewStarted();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBlackScreen() {
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_SHOW_BLACK_SCREEN, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeSavingRequestList() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke mPhotoSavingRequestList.size():" + this.mPhotoSavingRequestList.size());
        }
        if (this.mPhotoSavingRequestList.isEmpty()) {
            return;
        }
        for (RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder : this.mPhotoSavingRequestList) {
            photoSavingRequestBuilder.setRequestId(this.mViewFinder.getRequestId(false));
            if (CamLog.DEBUG) {
                CamLog.d("storePicture() requestId:" + photoSavingRequestBuilder.getRequestId());
            }
            SavingRequest savingRequestCreateSavingRequest = RequestFactory.createSavingRequest(photoSavingRequestBuilder);
            this.mStorage.requestStore(savingRequestCreateSavingRequest, savingRequestCreateSavingRequest.getStorageType(), this.mOnStoreCompletedListener);
        }
        this.mPhotoSavingRequestList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void changeTo(State state, Object... objArr) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke current:" + this.mCurrentState.getClass().getSimpleName() + ", to:" + state.getClass().getSimpleName());
        }
        this.mCurrentState.exit();
        this.mCurrentState = state;
        Iterator<OnStateChangedListener> it = this.mOnStateChangedListenerSet.iterator();
        while (it.hasNext()) {
            it.next().onStateChanged(this.mCurrentState.getCaptureState(), objArr);
        }
        this.mCurrentState.entry();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFastCapture(FastCapture fastCapture, StartupAction startupAction) {
        Rect previewRect = getPreviewRect();
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, previewRect.width() / previewRect.height(), this.mActivity.getScreenAspect());
        PositionConverter.getInstance().init(((CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE)).isFront(), surfaceViewRect, previewRect, PlatformCapability.getActiveArraySize(getCurrentCameraId()));
        PositionConverter.getInstance().setSurfaceSize(surfaceViewRect.width(), surfaceViewRect.height());
        PositionConverter.getInstance().setPreviewSize(previewRect.width(), previewRect.height());
        this.mActivity.requestPostLazyInitializationTaskExecute();
        if (!this.mActivity.awaitSetupAllReady()) {
            CamLog.e("Setup failed");
        }
        if (this.mViewFinder.isHeadUpDisplayReady() && isTutorialNeededToBeShownForCurrentMode()) {
            changeTo(new StateOperationRestricted(), ViewFinder.UiComponentKind.TUTORIAL);
        } else if (isVideo()) {
            changeTo(new StateVideoReady(startupAction), new Object[0]);
        } else {
            changeTo(new StatePhotoReady(true, true, startupAction), new Object[0]);
        }
        switch (fastCapture) {
            case LAUNCH_ONLY:
            default:
                return;
            case LAUNCH_AND_CAPTURE:
                if (this.mActivity.getLaunchCondition().getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
                    if (!PlatformCapability.hasDeviceError()) {
                        doFastestCapture();
                    }
                    this.mActivity.getLaunchCondition().clearExtraOperation();
                    return;
                }
                return;
            case OFF:
                throw new IllegalArgumentException("StateMachine.Resume:[FastCapture OFF]");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchCamera(AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke current:" + getCurrentCapturingMode());
        }
        CapturingMode currentCapturingMode = getCurrentCapturingMode();
        switch (currentCapturingMode) {
            case NORMAL:
                currentCapturingMode = CapturingMode.FRONT_PHOTO;
                break;
            case FRONT_PHOTO:
                currentCapturingMode = CapturingMode.NORMAL;
                break;
            case SLOW_MOTION:
            case VIDEO:
                currentCapturingMode = CapturingMode.FRONT_VIDEO;
                break;
            case SCENE_RECOGNITION:
                currentCapturingMode = CapturingMode.SUPERIOR_FRONT;
                break;
            case SUPERIOR_FRONT:
                currentCapturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            case FRONT_VIDEO:
                currentCapturingMode = CapturingMode.VIDEO;
                break;
        }
        switchCamera(currentCapturingMode, animationType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchCamera(CapturingMode capturingMode, AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke requestMode:" + capturingMode);
        }
        this.mCameraDeviceHandler.savePreloadSettings((CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
        this.mCameraDeviceHandler.releaseRecorder();
        if (isFusionMonitoringNeeded()) {
            this.mCameraDeviceHandler.stopFusionMonitoring();
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, (CameraParameters.FusionResult) null);
        }
        this.mCameraDeviceHandler.stopPreviewSynchronized();
        this.mCameraDeviceHandler.closeCamera();
        changeModeTo(capturingMode, animationType);
        requestResizeEvf(capturingMode, false);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED, capturingMode, false, animationType);
        sendResearchSameActivityEvent(getCurrentCapturingMode());
    }

    private void changeModeTo(CapturingMode capturingMode, AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke request:" + capturingMode + ", current:" + getCurrentCapturingMode());
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGING, new Object[0]);
        doStopObjectTracking();
        this.mUserSettings.resetTempParameters();
        this.mViewFinder.clearMessageDialog();
        this.mCameraDeviceHandler.savePreloadSettings((CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
        if (getCurrentCapturingMode().isFront() == capturingMode.isFront()) {
            setCurrentCapturingMode(capturingMode);
            changeTo(new StateWaitingEvfPreparedByModeChange(), new Object[0]);
            this.mChangeCameraModeTask = new ChangeCameraModeTask(capturingMode, animationType);
            this.mHandler.post(this.mChangeCameraModeTask);
            return;
        }
        setCurrentCapturingMode(capturingMode);
        changeTo(new StateCameraSwitching(this.mActivity.prepareCameraDeviceHandler(FastCapture.LAUNCH_ONLY, capturingMode, this.mUserSettings), FastCapture.LAUNCH_ONLY), new Object[0]);
        this.mViewFinder.notifyOnEvfPrepared();
    }

    private void doFastestCapture() {
        if (!isStorageWritable(getCurrentStorage())) {
            changeTo(new StateWarning(), new Object[0]);
            return;
        }
        pauseAudioPlaybackForCapture();
        switch (this.mCameraDeviceHandler.getPreProcessState()) {
            case PRE_SCAN_DONE:
                changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            case PRE_CAPTURE_STARTED:
                changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            case PRE_CAPTURE_DONE:
                changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            case PRE_SCAN_STARTED:
                changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            case NOT_STARTED:
            case PRE_CAPTURE_RELEASED:
                return;
            case PRE_SHUTTER_DONE:
                changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            default:
                throw new IllegalStateException("Un-Expected state:" + this.mCameraDeviceHandler.getPreProcessState());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTouchAeEnabled() {
        if (PlatformCapability.isTouchAeSupported(getCurrentCameraId()) && this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE) != TouchCapture.ON) {
            return (getCurrentCapturingMode() == CapturingMode.SCENE_RECOGNITION || getCurrentCapturingMode() == CapturingMode.SUPERIOR_FRONT || getCurrentCapturingMode() == CapturingMode.NORMAL) && this.mUserSettings.get(UserSettingKey.TOUCH_INTENTION) == TouchIntention.FOCUS_AND_EXPOSURE;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doChangeSelectedFace(Point point) {
        this.mObjectTracking.stop();
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTION_STARTED, new Object[0]);
        this.mCameraDeviceHandler.setSelectedFacePosition(point.x, point.y);
        if (isTouchAeEnabled()) {
            this.mCameraDeviceHandler.setMeteringAreaAndCommit(null, (Metering) this.mUserSettings.get(UserSettingKey.METERING));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startAutoFocus() {
        if (this.mActivity != null) {
            if (!this.mStorage.isStorageActivated() || !isStorageWritable(getCurrentStorage())) {
                if (!CamLog.DEBUG) {
                    return false;
                }
                CamLog.d("Storage is not ready");
                return false;
            }
            pauseAudioPlaybackForCapture();
        }
        this.mCameraDeviceHandler.autoFocus();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAutoFocus(boolean z) {
        if (z) {
            this.mCameraDeviceHandler.resetFocusModeAndCommit();
        }
        this.mViewFinder.setDisplayFlashRequired(false);
        this.mViewFinder.setDisplayFlashColor(255, 255, 255);
        this.mCameraDeviceHandler.cancelAutoFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCapture(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        this.mLastPhotoSavingRequest = photoSavingRequestBuilder;
        if (this.mLastPhotoSavingRequest != null) {
            this.mCameraDeviceHandler.applySavingRequest(this.mLastPhotoSavingRequest);
            this.mViewFinder.showDisplayFlashScreen();
            this.mCameraDeviceHandler.takePicture(this.mLastPhotoSavingRequest);
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, Boolean.valueOf(isSettingChangeAcceptable()));
            sendResearchCaptureEvents();
        }
    }

    private void sendResearchCaptureEvents() {
        LocalResearchUtil.getInstance().sendEventSettings();
        LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
        ResearchUtil.getInstance().setOrientation(this.mLastPhotoSavingRequest.mCommonStatus.orientation);
        LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO);
        ResearchUtil.getInstance().setTimeCaptureStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCaptureWhileRecording() {
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderCreatePhotoSavingRequest = createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
        if (photoSavingRequestBuilderCreatePhotoSavingRequest != null) {
            if (this.mContentsViewController != null) {
                this.mContentsViewController.stopAnimation(false);
            }
            this.mViewFinder.onShutterDone(true);
            this.mCameraDeviceHandler.captureWhileRecording(photoSavingRequestBuilderCreatePhotoSavingRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartRecording(boolean z) {
        com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion;
        if (this.mContentsViewController != null) {
            this.mContentsViewController.disableClick();
        }
        VideoSize videoSize = (VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        if (getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && (slowMotion = (com.sonyericsson.android.camera.configuration.parameters.SlowMotion) getUserSetting().get(UserSettingKey.SLOW_MOTION)) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION) {
            videoSize = slowMotion.getVideoSize();
        }
        VideoHdr videoHdr = (VideoHdr) this.mUserSettings.get(UserSettingKey.VIDEO_HDR);
        RecordingProfile recordingProfileBuild = new RecordingProfile.Builder().videoSize(videoSize).setOneShot(this.mActivity.isOneShot()).videoHdr(videoHdr).build();
        this.mLastVideoSavingRequest = createVideoSavingRequest(recordingProfileBuild);
        this.mViewFinder.setRecordingOrientation(this.mActivity.getOrientation());
        boolean z2 = true;
        if (this.mLastVideoSavingRequest != null) {
            boolean z3 = (getCurrentCapturingMode() != CapturingMode.SLOW_MOTION && this.mUserSettings.get(UserSettingKey.VIDEO_SIZE) == VideoSize.MMS) || this.mUserSettings.isLimitForSizeOrDuration();
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, Integer.valueOf((int) this.mLastVideoSavingRequest.mVideoStatus.maxDurationMills), Boolean.valueOf(z3), Boolean.valueOf((z3 || this.mUserSettings.get(UserSettingKey.CAPTURING_MODE) == CapturingMode.SLOW_MOTION) ? false : true), videoHdr);
            try {
                switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) this.mUserSettings.get(UserSettingKey.SLOW_MOTION)) {
                    case SUPER_SLOW_MOTION:
                    case SUPER_SLOW_SHOT:
                        this.mCameraDeviceHandler.updateRecorder(this.mLastVideoSavingRequest, shouldPlayShutterSound());
                        break;
                    default:
                        this.mCameraDeviceHandler.prepareRecorder(this.mLastVideoSavingRequest, this.mRecorderListener, this.mOnSuperSlowRecordingFinishedListener, shouldPlayShutterSound(), recordingProfileBuild, this.mStorage.createNotifier(getCurrentStorage(), 10));
                        break;
                }
                this.mCameraDeviceHandler.startRecording();
                if (isSettingChangeAcceptable()) {
                    this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, false);
                }
                updateDateTaken(this.mLastVideoSavingRequest);
                this.mRecorderListener.setSavingRequestBuilder(this.mLastVideoSavingRequest);
            } catch (RuntimeException e) {
                CamLog.w("Start recording failed.", e);
                if (this.mContentsViewController != null) {
                    this.mContentsViewController.enableClick();
                }
                if (isCurrentStorageExternal() && !isStorageWritable(Storage.StorageType.EXTERNAL_CARD)) {
                    this.mViewFinder.showMessageDialog(DialogId.COULD_NOT_START_RECORDING, new Object[0]);
                    changeTo(new StateVideoReady(this), new Object[0]);
                    return;
                } else {
                    this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
                    ResearchUtil.getInstance().sendCameraNotAvailableEvent();
                }
            }
        } else {
            z2 = false;
        }
        ResearchUtil.getInstance().setRecordBySideSense(z);
        if (z2) {
            transitionToRecordingState(false);
        } else {
            changeTo(new StateWarning(), new Object[0]);
        }
    }

    private void transitionToRecordingState(boolean z) {
        if (this.mActivity != null) {
            this.mActivity.disableAutoPowerOffTimer();
        }
        this.mChapterThumbnail = null;
        if (!z) {
            this.mCameraDeviceHandler.requestOnePreviewFrame();
        }
        if (this.mActivity != null) {
            int i = AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[getCurrentCapturingMode().ordinal()];
            if (i == 3) {
                this.mHighFrameRateVideoRecordingCountInSuperSlowMotion = 0;
                switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                    case STANDARD_SLOW_MOTION:
                        changeTo(new StateVideoRecording(), new Object[0]);
                        break;
                    case SUPER_SLOW_MOTION:
                        changeTo(new StateLowFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                        break;
                    case SUPER_SLOW_SHOT:
                        changeTo(new StateHighFrameRateVideoRecordingInSuperSlowMotion(), new Object[0]);
                        this.mViewFinder.startSlowMotionFeedbackAnimation();
                        break;
                }
            }
            switch (i) {
                case 7:
                case 8:
                    changeTo(new StateVideoRecording(false), new Object[0]);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestChangeModeTo(CapturingMode capturingMode, AnimationRequest.AnimationType animationType) {
        if (getCurrentCapturingMode() == capturingMode) {
            if (CamLog.DEBUG) {
                throw new IllegalArgumentException("The specified mode is already set, mode:" + getCurrentCapturingMode().name());
            }
            return;
        }
        switch (capturingMode) {
            case NORMAL:
            case FRONT_PHOTO:
            case SLOW_MOTION:
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
            case FRONT_VIDEO:
            case VIDEO:
                sendResearchSameActivityEvent(capturingMode);
                this.mCameraDeviceHandler.savePreloadSettings((CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), this.mUserSettings, this.mLastSettings, this.mActivity.isOneShot());
                if (isFusionMonitoringNeeded()) {
                    this.mCameraDeviceHandler.stopFusionMonitoring();
                    this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, (CameraParameters.FusionResult) null);
                }
                changeModeTo(capturingMode, animationType);
                return;
            case UNKNOWN:
            default:
                return;
        }
    }

    public boolean isVideoRecording() {
        return this.mIsVideoRecording;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFusionModeSetting(FusionMode fusionMode) {
        FusionMode fusionMode2 = (FusionMode) this.mUserSettings.get(UserSettingKey.FUSION_MODE);
        if (fusionMode == fusionMode2) {
            return;
        }
        int i = AnonymousClass8.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[getCurrentCapturingMode().ordinal()];
        if (i != 1) {
            if (i != 8) {
                return;
            }
            this.mUserSettings.set(fusionMode);
            LocalResearchUtil.getInstance().setSettingsValue(fusionMode2, fusionMode, getCurrentCapturingMode());
            return;
        }
        Iso iso = (Iso) this.mUserSettings.get(UserSettingKey.ISO);
        this.mUserSettings.set(fusionMode);
        if (iso != this.mUserSettings.get(UserSettingKey.ISO)) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ISO_CHANGED_BY_FUSION, new Object[0]);
        }
        LocalResearchUtil.getInstance().setSettingsValue(fusionMode2, fusionMode, getCurrentCapturingMode());
    }

    public boolean canHandleWearableCaptureRequest() {
        return this.mCurrentState.mCaptureState.canHandleWearableCaptureRequest();
    }

    public boolean canHandleAsynchronizedTask() {
        return this.mCurrentState.mCaptureState.canHandleAsynchronizedTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStartRecordingTask() {
        this.mHandler.removeCallbacks(this.mStartRecordingTask);
    }

    private class StartRecordingTask implements Runnable {
        private final boolean mBySideSense;

        public StartRecordingTask(boolean z) {
            this.mBySideSense = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke StartRecordingTask");
            }
            StateMachine.this.doStartRecording(this.mBySideSense);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRecordingProgress(int i) {
        if (this.mViewFinder == null || !this.mViewFinder.isSetupHeadupDisplayInvoked()) {
            return;
        }
        if (this.mActivity != null) {
            this.mActivity.disableAutoPowerOffTimer();
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_RECORDING_PROGRESS, Integer.valueOf(i));
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

    /* JADX INFO: Access modifiers changed from: private */
    public void doStopRecording(boolean z) {
        PerfLog.STOP_REC.begin();
        if (this.mContentsViewController != null) {
            this.mContentsViewController.enableClick();
        }
        Optional<Long> optionalStopRecording = this.mCameraDeviceHandler.stopRecording(z);
        if (optionalStopRecording.isPresent()) {
            ResearchUtil.getInstance().setOrientation(this.mLastVideoSavingRequest.mCommonStatus.orientation);
            LocalResearchUtil.getInstance().sendEventSettings();
            LocalResearchUtil.getInstance().sendSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO);
            LocalResearchUtil.getInstance().sendRecordingEvent(Event.CaptureOperation.RECORDING, getCurrentRecordingStopOperation(), optionalStopRecording.get().intValue(), false);
        }
        this.mViewFinder.onCaptureDone();
        if (VideoStabilizer.isIntelligentActive((VideoStabilizer) this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER))) {
            this.mLastVideoSavingRequest.setRequestId(this.mViewFinder.getRequestId(false));
        }
        PerfLog.STOP_REC.end();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doPauseRecording() {
        this.mCameraDeviceHandler.pauseRecording();
        changeTo(new StateVideoRecordingPausing(), new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doResumeRecording() {
        this.mCameraDeviceHandler.resumeRecording();
        changeTo(new StateVideoRecording(), new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doHandleRecordingError() {
        this.mViewFinder.showMessageDialog(DialogId.ERROR_UNKNOWN, new Object[0]);
        changeTo(new StateWarning(), new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOrientation() {
        int normalizedRotation = RotationUtil.getNormalizedRotation(this.mActivity.getSensorOrientationDegree());
        CameraInfo cameraInfo = this.mCameraDeviceHandler.getCameraInfo();
        if (CamLog.DEBUG) {
            CamLog.d("ORIENTATION:sensorOrientation:" + normalizedRotation);
            CamLog.d("ORIENTATION:cameraOrientation", RotationUtil.orientationToString(cameraInfo.orientation));
            CamLog.d("ORIENTATION:cameraFacing:" + cameraInfo.facing);
        }
        switch (cameraInfo.facing) {
        }
        return (cameraInfo.orientation + normalizedRotation) % 360;
    }

    private TakenStatusCommon createTakenStatusCommon(SavingTaskManager.SavedFileType savedFileType, Rect rect, String str, String str2, String str3) {
        boolean zShouldAddToMediaStore;
        boolean z;
        long jCurrentTimeMillis = System.currentTimeMillis();
        int orientation = getOrientation();
        Location currentLocation = (!this.mActivity.isOneShot() || PermissionsUtil.areCallerGeoPermissionsGranted(this.mActivity)) ? this.mActivity.getGeoTagManager().getCurrentLocation() : null;
        switch (savedFileType) {
            case BURST:
                zShouldAddToMediaStore = true;
                z = zShouldAddToMediaStore;
                break;
            case VIDEO:
                zShouldAddToMediaStore = this.mActivity.shouldAddToMediaStore();
                z = zShouldAddToMediaStore;
                break;
            case PHOTO:
                zShouldAddToMediaStore = this.mActivity.shouldAddToMediaStore();
                z = zShouldAddToMediaStore;
                break;
            default:
                z = false;
                break;
        }
        return new TakenStatusCommon(jCurrentTimeMillis, orientation, currentLocation, rect.width(), rect.height(), str, str2, savedFileType, str3, "", z, false);
    }

    /* renamed from: com.sonyericsson.android.camera.controller.StateMachine$8, reason: invalid class name */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition;

        static {
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.BURST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.VIDEO.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.PHOTO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId = new int[CameraInfo.CameraId.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId[CameraInfo.CameraId.BACK.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId[CameraInfo.CameraId.FRONT.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture = new int[FastCapture.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture[FastCapture.LAUNCH_ONLY.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture[FastCapture.LAUNCH_AND_CAPTURE.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture[FastCapture.OFF.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent = new int[StaticEvent.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_SCENE_MODE_CHANGED.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_FACE_DETECTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_OBJECT_TRACKED.ordinal()] = 5;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_ORIENTATION_CHANGED.ordinal()] = 6;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN.ordinal()] = 7;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_OBJECT_TRACKING_LOST.ordinal()] = 8;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED.ordinal()] = 9;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED.ordinal()] = 10;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$StaticEvent[StaticEvent.EVENT_ON_PREVIEW_STARTED.ordinal()] = 11;
            } catch (NoSuchFieldError unused19) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState = new int[ViewFinder.HeadUpDisplaySetupState.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.PHOTO_READY.ordinal()] = 1;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.VIDEO_READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING.ordinal()] = 3;
            } catch (NoSuchFieldError unused22) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent = new int[TransitterEvent.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_INITIALIZE.ordinal()] = 1;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_RESUME.ordinal()] = 2;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_RESUME_TIMEOUT.ordinal()] = 3;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_FINALIZE.ordinal()] = 5;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_WARNING.ordinal()] = 6;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_WARNING_EXTRA.ordinal()] = 7;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW.ordinal()] = 8;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW.ordinal()] = 9;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_REACH_BATTERY_LIMIT.ordinal()] = 10;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_REACH_BATTERY_LOW.ordinal()] = 11;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_BATTERY_LEVEL_CHANGED.ordinal()] = 12;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_CRITICAL.ordinal()] = 13;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_HEATED_OVER_NORMAL.ordinal()] = 14;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_EVF_PREPARED.ordinal()] = 15;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_INITIAL_AUTO_FOCUS_DONE.ordinal()] = 16;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_AUTO_FOCUS_DONE.ordinal()] = 17;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE.ordinal()] = 18;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_SHUTTER_DONE.ordinal()] = 19;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_PREPARE_BURST_DONE.ordinal()] = 20;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE.ordinal()] = 21;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_TAKE_PICTURE_DONE.ordinal()] = 22;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_VIDEO_RECORDING_DONE.ordinal()] = 23;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_TOUCH_CONTENT_PROGRESS.ordinal()] = 24;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_STORE_REQUESTED.ordinal()] = 25;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_STORE_COMPLETED.ordinal()] = 26;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_KEY_MENU.ordinal()] = 27;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_START_TRANSITION_OPERATION.ordinal()] = 28;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION.ordinal()] = 29;
            } catch (NoSuchFieldError unused51) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CLEAR_FOCUS.ordinal()] = 30;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_DIALOG_OPENED.ordinal()] = 31;
            } catch (NoSuchFieldError unused53) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_DIALOG_CLOSED.ordinal()] = 32;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STORAGE_ERROR.ordinal()] = 33;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STORAGE_MOUNTED.ordinal()] = 34;
            } catch (NoSuchFieldError unused56) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STORAGE_UNGRANTED.ordinal()] = 35;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_SET_TOUCHED_POSITION.ordinal()] = 36;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY.ordinal()] = 37;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_RECORDING_START_WAIT_DONE.ordinal()] = 38;
            } catch (NoSuchFieldError unused60) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_RECORDING_ERROR.ordinal()] = 39;
            } catch (NoSuchFieldError unused61) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CHANGE_SELECTED_FACE.ordinal()] = 40;
            } catch (NoSuchFieldError unused62) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_SET_SELECTED_OBJECT_POSITION.ordinal()] = 41;
            } catch (NoSuchFieldError unused63) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_DESELECT_OBJECT_POSITION.ordinal()] = 42;
            } catch (NoSuchFieldError unused64) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED.ordinal()] = 43;
            } catch (NoSuchFieldError unused65) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_BURST_SHUTTER_DONE.ordinal()] = 44;
            } catch (NoSuchFieldError unused66) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_BURST_STORE_COMPLETED.ordinal()] = 45;
            } catch (NoSuchFieldError unused67) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_PREDICTIVE_CAPTURE_GROUP_STORE_COMPLETED.ordinal()] = 46;
            } catch (NoSuchFieldError unused68) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_ONE_PREVIEW_FRAME_UPDATED.ordinal()] = 47;
            } catch (NoSuchFieldError unused69) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED.ordinal()] = 48;
            } catch (NoSuchFieldError unused70) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_SWITCH_CAMERA.ordinal()] = 49;
            } catch (NoSuchFieldError unused71) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CANCEL_TOUCHED_POSITION.ordinal()] = 50;
            } catch (NoSuchFieldError unused72) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_SEMIAUTO_ENABLED.ordinal()] = 51;
            } catch (NoSuchFieldError unused73) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_SEMIAUTO_DISABLED.ordinal()] = 52;
            } catch (NoSuchFieldError unused74) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_AMBER_BLUE_COLOR_CHANGED.ordinal()] = 53;
            } catch (NoSuchFieldError unused75) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_BRIGHTNESS_CHANGED.ordinal()] = 54;
            } catch (NoSuchFieldError unused76) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_SELFTIMER_CANCEL.ordinal()] = 55;
            } catch (NoSuchFieldError unused77) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_CAMERA_DEVICE_OPENED.ordinal()] = 56;
            } catch (NoSuchFieldError unused78) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_CAMERA_DEVICE_CLOSED.ordinal()] = 57;
            } catch (NoSuchFieldError unused79) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE.ordinal()] = 58;
            } catch (NoSuchFieldError unused80) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END.ordinal()] = 59;
            } catch (NoSuchFieldError unused81) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_HIGH_FRAME_RATE_RECORDING_DONE.ordinal()] = 60;
            } catch (NoSuchFieldError unused82) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ANGLE_CHANGE_START.ordinal()] = 61;
            } catch (NoSuchFieldError unused83) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CAPTURE_READY.ordinal()] = 62;
            } catch (NoSuchFieldError unused84) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN.ordinal()] = 63;
            } catch (NoSuchFieldError unused85) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CAPTURE.ordinal()] = 64;
            } catch (NoSuchFieldError unused86) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CAPTURE_BURST.ordinal()] = 65;
            } catch (NoSuchFieldError unused87) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CAPTURE_CANCEL.ordinal()] = 66;
            } catch (NoSuchFieldError unused88) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_RECORD_READY.ordinal()] = 67;
            } catch (NoSuchFieldError unused89) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_START_RECORDING.ordinal()] = 68;
            } catch (NoSuchFieldError unused90) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STOP_RECORDING.ordinal()] = 69;
            } catch (NoSuchFieldError unused91) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_RESUME_RECORDING.ordinal()] = 70;
            } catch (NoSuchFieldError unused92) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_PAUSE_RECORDING.ordinal()] = 71;
            } catch (NoSuchFieldError unused93) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_TRIGGER_SLOW_MOTION.ordinal()] = 72;
            } catch (NoSuchFieldError unused94) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE.ordinal()] = 73;
            } catch (NoSuchFieldError unused95) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED.ordinal()] = 74;
            } catch (NoSuchFieldError unused96) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_STORAGE_READY_STATE_CHANGED.ordinal()] = 75;
            } catch (NoSuchFieldError unused97) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_CHANGE_CAPTURING_MODE.ordinal()] = 76;
            } catch (NoSuchFieldError unused98) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ZOOM_PREPARE.ordinal()] = 77;
            } catch (NoSuchFieldError unused99) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ZOOM_PERFORM.ordinal()] = 78;
            } catch (NoSuchFieldError unused100) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[TransitterEvent.EVENT_ZOOM_FINISH.ordinal()] = 79;
            } catch (NoSuchFieldError unused101) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree = new int[AnimationRequest.AnimationDegree.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.CANCEL.ordinal()] = 1;
            } catch (NoSuchFieldError unused102) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.EXEC.ordinal()] = 2;
            } catch (NoSuchFieldError unused103) {
            }
            $SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result = new int[RecorderController.Result.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[RecorderController.Result.MAX_DURATION_REACHED.ordinal()] = 1;
            } catch (NoSuchFieldError unused104) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[RecorderController.Result.MAX_FILESIZE_REACHED.ordinal()] = 2;
            } catch (NoSuchFieldError unused105) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[RecorderController.Result.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused106) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[RecorderController.Result.FAIL.ordinal()] = 4;
            } catch (NoSuchFieldError unused107) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition = new int[NextCaptureCondition.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[NextCaptureCondition.READY.ordinal()] = 1;
            } catch (NoSuchFieldError unused108) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[NextCaptureCondition.REQUESTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused109) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$NextCaptureCondition[NextCaptureCondition.UNACCEPTABLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused110) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState = new int[CaptureState.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_OPERATION_RESTRICTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused111) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_PHOTO_READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused112) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_VIDEO_READY.ordinal()] = 3;
            } catch (NoSuchFieldError unused113) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_WARNING.ordinal()] = 4;
            } catch (NoSuchFieldError unused114) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION.ordinal()] = 5;
            } catch (NoSuchFieldError unused115) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_VIDEO_RECORDING.ordinal()] = 6;
            } catch (NoSuchFieldError unused116) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_VIDEO_RECORDING_PAUSING.ordinal()] = 7;
            } catch (NoSuchFieldError unused117) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[CaptureState.STATE_VIDEO_STORE.ordinal()] = 8;
            } catch (NoSuchFieldError unused118) {
            }
            $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState = new int[CameraDeviceHandler.PreProcessState.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_SCAN_DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused119) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_CAPTURE_STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused120) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_CAPTURE_DONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused121) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_SCAN_STARTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused122) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.NOT_STARTED.ordinal()] = 5;
            } catch (NoSuchFieldError unused123) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED.ordinal()] = 6;
            } catch (NoSuchFieldError unused124) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$PreProcessState[CameraDeviceHandler.PreProcessState.PRE_SHUTTER_DONE.ordinal()] = 7;
            } catch (NoSuchFieldError unused125) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode = new int[CapturingMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused126) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_PHOTO.ordinal()] = 2;
            } catch (NoSuchFieldError unused127) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SLOW_MOTION.ordinal()] = 3;
            } catch (NoSuchFieldError unused128) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused129) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SCENE_RECOGNITION.ordinal()] = 5;
            } catch (NoSuchFieldError unused130) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SUPERIOR_FRONT.ordinal()] = 6;
            } catch (NoSuchFieldError unused131) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_VIDEO.ordinal()] = 7;
            } catch (NoSuchFieldError unused132) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.VIDEO.ordinal()] = 8;
            } catch (NoSuchFieldError unused133) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion = new int[com.sonyericsson.android.camera.configuration.parameters.SlowMotion.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused134) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[com.sonyericsson.android.camera.configuration.parameters.SlowMotion.SUPER_SLOW_MOTION.ordinal()] = 2;
            } catch (NoSuchFieldError unused135) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[com.sonyericsson.android.camera.configuration.parameters.SlowMotion.SUPER_SLOW_SHOT.ordinal()] = 3;
            } catch (NoSuchFieldError unused136) {
            }
        }
    }

    public RequestFactory.PhotoSavingRequestBuilder createPhotoSavingRequest(SavingTaskManager.SavedFileType savedFileType) {
        TakenStatusPhoto.Facing facing;
        if (savedFileType != SavingTaskManager.SavedFileType.BURST && savedFileType != SavingTaskManager.SavedFileType.PHOTO && savedFileType != SavingTaskManager.SavedFileType.PHOTO_DURING_REC) {
            throw new IllegalArgumentException("Unexpected type:" + savedFileType);
        }
        Resolution resolution = (Resolution) this.mUserSettings.get(UserSettingKey.RESOLUTION);
        if (getCurrentCapturingMode().isFront()) {
            facing = TakenStatusPhoto.Facing.FRONT;
        } else {
            facing = TakenStatusPhoto.Facing.BACK;
        }
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = new RequestFactory.PhotoSavingRequestBuilder(createTakenStatusCommon(savedFileType, resolution.getPictureRect(), MediaSavingConstants.MEDIA_TYPE_JPEG_MIME, MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, null), new TakenStatusPhoto(facing), false);
        photoSavingRequestBuilder.addCallback(this.mOnStoreCompletedListener);
        photoSavingRequestBuilder.setOneshot(this.mActivity.isOneShotPhoto());
        if (savedFileType == SavingTaskManager.SavedFileType.BURST) {
            photoSavingRequestBuilder.setSomcType(129);
            photoSavingRequestBuilder.setStorageType(Storage.StorageType.INTERNAL);
        } else {
            photoSavingRequestBuilder.setStorageType(getCurrentStorage());
        }
        photoSavingRequestBuilder.setExtraOutput(this.mActivity.getExtraOutput());
        return photoSavingRequestBuilder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RequestFactory.VideoSavingRequestBuilder createVideoSavingRequest(RecordingProfile recordingProfile) {
        com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion;
        VideoSize videoSize = (VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        if (getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && (slowMotion = (com.sonyericsson.android.camera.configuration.parameters.SlowMotion) getUserSetting().get(UserSettingKey.SLOW_MOTION)) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION) {
            videoSize = slowMotion.getVideoSize();
        }
        MaxVideoSize maxVideoSize = this.mUserSettings.getMaxVideoSize(this.mStorage, getCurrentStorage(), recordingProfile);
        long jMin = Math.min(maxVideoSize.getMaxFileSize(), StorageUtil.getStorageMaximumFileSize(this.mActivity, getCurrentStorage()));
        long maxDuration = maxVideoSize.getMaxDuration();
        String mime = recordingProfile.getMime();
        String extension = recordingProfile.getExtension();
        String string = getCurrentCapturingMode() == CapturingMode.SLOW_MOTION ? getUserSetting().get(UserSettingKey.SLOW_MOTION).toString() : null;
        RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder = new RequestFactory.VideoSavingRequestBuilder(createTakenStatusCommon(SavingTaskManager.SavedFileType.VIDEO, videoSize.getVideoRect(), mime, extension, null), new TakenStatusVideo(maxDuration, jMin));
        videoSavingRequestBuilder.addCallback(this.mOnStoreCompletedListener);
        videoSavingRequestBuilder.setExtraOutput(this.mActivity.getExtraOutput());
        videoSavingRequestBuilder.setSlowMotion(string, this.mStorage, getCurrentStorage());
        videoSavingRequestBuilder.setOneShot(this.mActivity.isOneShotVideo());
        videoSavingRequestBuilder.setStorageType(getCurrentStorage());
        return videoSavingRequestBuilder;
    }

    private void updateDateTaken(RequestFactory.RequestBuilder requestBuilder) {
        requestBuilder.setDateTaken(System.currentTimeMillis());
    }

    public void onInitialAutoFocusDone(boolean z) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke success:" + z);
        }
        ResearchUtil.getInstance().setTimeAfDone();
        ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
        sendEvent(TransitterEvent.EVENT_ON_INITIAL_AUTO_FOCUS_DONE, Boolean.valueOf(z));
    }

    public void onAutoFocusDone(boolean z, boolean z2, boolean z3, int i, int i2, int i3) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke isHighQualityBurstAvailable:" + z + ", isAfSuccessed:" + z2 + ", requireDisplayFlash:" + z3);
        }
        if (this.mViewFinder != null) {
            this.mViewFinder.setDisplayFlashRequired(z3);
            this.mViewFinder.setDisplayFlashColor(i, i2, i3);
        }
        ResearchUtil.getInstance().setTimeAfDone();
        sendEvent(TransitterEvent.EVENT_ON_AUTO_FOCUS_DONE, Boolean.valueOf(z2), Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPlaySound() {
        this.mActivity.stopPlayingSound();
    }

    public void onPreShutterDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        checkCallback(photoSavingRequestBuilder);
        sendEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, photoSavingRequestBuilder);
    }

    public void onShutterDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i, boolean z) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke captureNum:" + i + ", isAfSuccessed:" + z);
        }
        if (i > 1) {
            this.mPredictiveCaptureStoreInfo = new PredictiveCaptureStoreInfo(i, photoSavingRequestBuilder.getSaveTimeForPredictiveCapture());
        } else {
            this.mPredictiveCaptureStoreInfo = null;
        }
        sendEvent(TransitterEvent.EVENT_ON_SHUTTER_DONE, photoSavingRequestBuilder, Boolean.valueOf(z));
    }

    public void onPreTakePictureDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        checkCallback(photoSavingRequestBuilder);
        sendEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, photoSavingRequestBuilder);
    }

    public void onTakePictureDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        sendEvent(TransitterEvent.EVENT_ON_TAKE_PICTURE_DONE, photoSavingRequestBuilder);
    }

    public void onPrepareBurstDone(boolean z) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke isSuccess:" + z);
        }
        sendEvent(TransitterEvent.EVENT_ON_PREPARE_BURST_DONE, Boolean.valueOf(z));
    }

    private void checkCallback(RequestFactory.RequestBuilder requestBuilder) {
        requestBuilder.addCallback(this.mOnStoreCompletedListener);
    }

    public void onVideoRecordingDone() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mIsVideoRecording = false;
        sendEvent(TransitterEvent.EVENT_ON_VIDEO_RECORDING_DONE, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPhotoSmileCapture() {
        requestSmileCapture(((SmileCapture) this.mUserSettings.get(UserSettingKey.SMILE_CAPTURE)).getIntValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestVideoSmileCapture() {
        requestSmileCapture(((VideoSmileCapture) this.mUserSettings.get(UserSettingKey.VIDEO_SMILE_CAPTURE)).getIntValue());
    }

    private void requestSmileCapture(int i) {
        if (SystemClock.uptimeMillis() - this.mLastSmileCaptureTakenTime <= 1000 || i >= this.mViewFinder.getSelectedFaceSmileScore()) {
            return;
        }
        this.mVirtualKeyEventDispatcher.sendVirtualKeyEvent(UserEventHandler.VirtualKeyEvent.SMILE_CAPTURE);
        this.mLastSmileCaptureTakenTime = SystemClock.uptimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storePicture(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        SavingRequest savingRequestCreateSavingRequest = RequestFactory.createSavingRequest(photoSavingRequestBuilder);
        this.mPredictiveApplier.entrySuppressor(savingRequestCreateSavingRequest);
        this.mStorage.requestStore(savingRequestCreateSavingRequest, savingRequestCreateSavingRequest.getStorageType(), this.mOnStoreCompletedListener);
    }

    private class RequestStoreTask implements Runnable {
        private final RequestFactory.PhotoSavingRequestBuilder mRequest;

        private RequestStoreTask(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
            this.mRequest = photoSavingRequestBuilder;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke RequestStoreTask");
            }
            synchronized (StateMachine.this) {
                if (!StateMachine.this.isNeedRepairRequestId(this.mRequest) || StateMachine.this.mContentsViewController == null) {
                    if (StateMachine.this.mContentsViewController != null || StateMachine.this.mCurrentState.getCaptureState() == CaptureState.STATE_PAUSE || StateMachine.this.mCurrentState.getCaptureState() == CaptureState.STATE_FINALIZE) {
                        StateMachine.this.storePicture(this.mRequest);
                    } else {
                        if (this.mRequest.getShouldUpdateOrientationBeforeStoring()) {
                            this.mRequest.mCommonStatus.orientation = StateMachine.this.getOrientation();
                        }
                        if (StateMachine.this.mActivity.isDeviceInSecurityLock()) {
                            if (this.mRequest.mCommonStatus.takenByFastCapture || StateMachine.this.isNeedRepairRequestId(this.mRequest)) {
                                StateMachine.this.mPhotoSavingRequestList.add(this.mRequest);
                            } else {
                                StateMachine.this.storePicture(this.mRequest);
                            }
                        } else {
                            this.mRequest.setRequestId(-1);
                            StateMachine.this.storePicture(this.mRequest);
                        }
                    }
                    StateMachine.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.RequestStoreTask.2
                        @Override // java.lang.Runnable
                        public void run() {
                            StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
                        }
                    });
                    return;
                }
                StateMachine.this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.RequestStoreTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StateMachine.this.requestStorePicture(RequestStoreTask.this.mRequest);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void requestStorePicture(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (isNeedRepairRequestId(photoSavingRequestBuilder) && this.mContentsViewController != null) {
            photoSavingRequestBuilder.setRequestId(this.mViewFinder.getRequestId(false));
        }
        this.mExecService.execute(new RequestStoreTask(photoSavingRequestBuilder));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void requestStoreVideo(RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder) {
        if (videoSavingRequestBuilder == null) {
            return;
        }
        if (this.mContentsViewController != null) {
            this.mContentsViewController.stopAnimation(false);
        }
        if ((!VideoStabilizer.isIntelligentActive((VideoStabilizer) this.mUserSettings.get(UserSettingKey.VIDEO_STABILIZER)) || videoSavingRequestBuilder.getRequestId() == -1) && this.mContentsViewController != null) {
            videoSavingRequestBuilder.setRequestId(this.mViewFinder.getRequestId(false));
        }
        videoSavingRequestBuilder.setDateTaken(System.currentTimeMillis());
        SavingRequest savingRequestCreateSavingRequest = RequestFactory.createSavingRequest(videoSavingRequestBuilder);
        this.mStorage.requestStore(savingRequestCreateSavingRequest, savingRequestCreateSavingRequest.getStorageType(), this.mOnStoreCompletedListener);
        sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOneShotStoreCompleted(StoreDataResult storeDataResult) {
        if (storeDataResult.savingRequest.getExtraOutput() == null) {
            requestLoadStoredPicture(storeDataResult);
        } else {
            finishOneShot(storeDataResult, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOneShot(StoreDataResult storeDataResult, Bitmap bitmap) {
        this.mActivity.finishOneShot(new OneShotResult(storeDataResult.uri, storeDataResult.storeResult, storeDataResult.savingRequest, bitmap));
    }

    public void onSceneModeChanged(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        sendStaticEvent(StaticEvent.EVENT_ON_SCENE_MODE_CHANGED, sceneRecognitionResult);
    }

    public void onFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived() || !isStorageWritable(getCurrentStorage()) || this.mCameraDeviceHandler.isObjectTrackingRunning()) {
            return;
        }
        sendStaticEvent(StaticEvent.EVENT_ON_FACE_DETECTED, faceDetectionResult);
    }

    public void onObjectTracked(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        sendStaticEvent(StaticEvent.EVENT_ON_OBJECT_TRACKED, objectTrackingResult);
    }

    public void onObjectLost(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mActivity.isThermalWarningReceived()) {
            return;
        }
        sendStaticEvent(StaticEvent.EVENT_ON_OBJECT_TRACKING_LOST, objectTrackingResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doZoomChangeAngle() {
        changeTo(new StateCropping(this.mCurrentState.getCaptureState()), new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onZoomChange(int i) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        if (this.mViewFinder == null || !this.mViewFinder.isSetupHeadupDisplayInvoked()) {
            return;
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, Integer.valueOf(i));
    }

    public void onCropRegionReady() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.7
            @Override // java.lang.Runnable
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke onCropRegionReady");
                }
                StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_HIDE_BLACK_SCREEN, new Object[0]);
            }
        });
    }

    public void onDeviceError(CameraDeviceHandler.ErrorCode errorCode) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        PowerManager powerManager = (PowerManager) this.mActivity.getSystemService("power");
        PlatformCapability.setDeviceError(true);
        if (powerManager.isScreenOn()) {
            CamLog.e("ERROR:[Screen backlight is ON.");
            this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
        } else {
            CamLog.e("ERROR:[Screen backlight is OFF. Force close application.]");
            this.mActivity.finishAndKillProcess();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeChangeCameraModeTask() {
        this.mHandler.removeCallbacks(this.mChangeCameraModeTask);
    }

    private class ChangeCameraModeTask implements Runnable {
        private final CapturingMode mRequestMode;
        private final AnimationRequest.AnimationType mTriggerType;

        private ChangeCameraModeTask(CapturingMode capturingMode, AnimationRequest.AnimationType animationType) {
            this.mRequestMode = capturingMode;
            this.mTriggerType = animationType;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke ChangeCameraModeTask");
            }
            if (StateMachine.this.mCameraDeviceHandler == null || StateMachine.this.mViewFinder == null) {
                return;
            }
            StateMachine.this.mCameraDeviceHandler.releaseRecorder();
            StateMachine.this.mCameraDeviceHandler.stopFaceDetection();
            StateMachine.this.mCameraDeviceHandler.stopPreviewSynchronized();
            StateMachine.this.mViewFinder.hideSurface();
            PerfLog.MODE_CHANGE_TASK_START.transit();
            StateMachine.this.mUserSettings.applyCapturingMode();
            StateMachine.this.doStopObjectTracking();
            StateMachine.this.requestResizeEvf((CapturingMode) StateMachine.this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), false);
            StateMachine.this.mViewFinder.showSurface();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED, this.mRequestMode, true, this.mTriggerType);
            if (StateMachine.this.isFusionMonitoringNeeded()) {
                StateMachine.this.mCameraDeviceHandler.startFusionMonitoring();
            }
            PerfLog.MODE_CHANGE_TASK_END.transit();
        }
    }

    private Rect getPreviewRect() {
        Rect pictureRect;
        CapturingMode capturingMode = (CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
        if (capturingMode.isVideo()) {
            pictureRect = ((VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect();
        } else {
            pictureRect = ((Resolution) this.mUserSettings.get(UserSettingKey.RESOLUTION)).getPictureRect();
        }
        return LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mCameraDeviceHandler.getPreviewRect(capturingMode, pictureRect));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateRemainStorage() {
        if (this.mCameraDeviceHandler == null || this.mViewFinder == null || this.mActivity == null || getCurrentStorage() == null) {
            return;
        }
        Storage.StorageType currentStorage = getCurrentStorage();
        if (this.mStorage.getRemainStorage(currentStorage) <= CommonConstants.STORAGE_REMAIN_MIN) {
            sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, currentStorage, this.mStorage.getCurrentState(currentStorage));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSensorOrientation() {
        int i = this.mActivity.getLastDetectedOrientation() == CameraActivity.LayoutOrientation.Portrait ? 1 : 2;
        if (CamLog.DEBUG) {
            CamLog.d("getOrientation: sensor orientation:" + i);
        }
        return i;
    }

    private void moveToCameraNotAvailable() {
        CamLog.e(".startFastCapture():[Camera not available]");
        this.mViewFinder.showMessageDialog(DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, new Object[0]);
        ResearchUtil.getInstance().sendCameraNotAvailableEvent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPredictiveCaptureStoreComplete(StoreDataResult storeDataResult) {
        PerfLog.BURST_STORE_COMPLETE.transit();
        if (this.mPredictiveCaptureStoreInfo != null && storeDataResult.savingRequest.getSaveTimeForPredictiveCapture().equals(this.mPredictiveCaptureStoreInfo.getCaptureTime())) {
            this.mPredictiveCaptureStoreInfo = null;
        }
        Intent intent = new Intent(IntentConstants.BroadcastIntent.PREDICTIVE_CAPTURE_SAVE_COMPLETED);
        intent.putExtra(IntentConstants.BroadcastIntent.EXTRA_PREDICTIVE_CAPTURE_DIRECTORY_PATH, PredictiveCapturePathBuilder.getPredictiveCaptureGroupIdPath(storeDataResult.savingRequest.getFilePath()));
        this.mActivity.sendBroadcast(intent);
    }

    class StateVideoRecordingPausing extends StateVideoRecording {
        @Override // com.sonyericsson.android.camera.controller.StateMachine.StateVideoRecording
        protected boolean isPaused() {
            return true;
        }

        private StateVideoRecordingPausing() {
            super();
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING_PAUSING;
        }

        @Override // com.sonyericsson.android.camera.controller.StateMachine.State
        public void handleResumeRecording(Object... objArr) {
            StateMachine.this.doResumeRecording();
            StateMachine.this.mCameraDeviceHandler.requestOnePreviewFrame();
        }
    }

    public CapturingMode getCurrentCapturingMode() {
        if (getUserSetting().get(UserSettingKey.CAPTURING_MODE) == null) {
            return this.mActivity.getLaunchCondition().getCapturingMode();
        }
        return (CapturingMode) getUserSetting().get(UserSettingKey.CAPTURING_MODE);
    }

    public CameraInfo.CameraId getCurrentCameraId() {
        return getCameraId(getCurrentCapturingMode());
    }

    private CameraInfo.CameraId getCameraId(CapturingMode capturingMode) {
        switch (capturingMode) {
            case NORMAL:
            case SCENE_RECOGNITION:
            case VIDEO:
                return CameraInfo.CameraId.BACK;
            case FRONT_PHOTO:
            case SUPERIOR_FRONT:
            case FRONT_VIDEO:
                return CameraInfo.CameraId.FRONT;
            case SLOW_MOTION:
            case UNKNOWN:
            default:
                return CameraInfo.CameraId.BACK;
        }
    }

    public boolean canApplicationBeFinished() {
        boolean zCanApplicationBeFinished;
        synchronized (this.mCurrentState) {
            zCanApplicationBeFinished = this.mCurrentState.getCaptureState().canApplicationBeFinished();
        }
        return zCanApplicationBeFinished;
    }

    public boolean isMenuAvailable() {
        return this.mCurrentState.getCaptureState().isMenuAvailable();
    }

    public boolean isRecording() {
        if (this.mCurrentState != null) {
            return this.mCurrentState.getCaptureState().isRecording();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentCapturingMode(CapturingMode capturingMode) {
        getUserSetting().changeCapturingMode(capturingMode);
        LocalResearchUtil.getInstance().setSettingsValue(getUserSetting(), capturingMode);
    }

    private boolean isSmoothZoomEnabled() {
        return !this.mCameraDeviceHandler.isCameraFront();
    }

    private boolean isPhotoSelfTimerEnabled() {
        return this.mViewFinder.getPhotoSelfTimerSetting() != SelfTimer.OFF;
    }

    private boolean isBurstByCameraKeyEnabled() {
        return this.mUserSettings.get(UserSettingKey.CAMERA_KEY) == CameraKey.BURST_SHOT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLazyInitializationRunning() {
        return this.mActivity.isLazyInitializationRunning();
    }

    private boolean canInvokePhotoSelfTimer() {
        return !isLazyInitializationRunning() && isPhotoSelfTimerEnabled() && isStorageWritable(getCurrentStorage());
    }

    @Deprecated
    public synchronized CaptureState getCurrentCaptureState() {
        return this.mCurrentState.getCaptureState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAmberBlueColor(float f) {
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(getCurrentCameraId());
        int iIntValue = cameraCapability.MIN_AWB_AB.get().intValue();
        int iIntValue2 = cameraCapability.MAX_AWB_AB.get().intValue();
        int iCeil = (int) Math.ceil(iIntValue + ((iIntValue2 - iIntValue) * f));
        if (CamLog.DEBUG) {
            CamLog.d("invoke color-changed slider:" + f + ", min:" + iIntValue + ", max:" + iIntValue2 + ", value:" + iCeil);
        }
        this.mCameraDeviceHandler.setAmberBlueColorAndCommit(iCeil);
        LocalResearchUtil.getInstance().setSemiAutoSettingAmberBlueValue(iCeil);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBrightness(float f) {
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(getCurrentCameraId());
        int iIntValue = cameraCapability.EV_MIN.get().intValue();
        int iIntValue2 = cameraCapability.EV_MAX.get().intValue();
        int iCeil = (int) Math.ceil(iIntValue + ((iIntValue2 - iIntValue) * f));
        if (CamLog.DEBUG) {
            CamLog.d("invoke brightness-changed slider:" + f + ", min:" + iIntValue + ", max:" + iIntValue2 + ", value:" + iCeil);
        }
        this.mCameraDeviceHandler.setBrightnessAndCommit(iCeil);
        LocalResearchUtil.getInstance().setSemiAutoSettingBrightnessValue(iCeil);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeToStandby() {
        if (isVideo()) {
            changeTo(new StateVideoReady(this), new Object[0]);
        } else {
            changeTo(new StatePhotoReady(this, true), new Object[0]);
        }
    }

    private void sendResearchSameActivityEvent(CapturingMode capturingMode) {
        LocalResearchUtil.getInstance().clearAllSettings();
        LocalResearchUtil.getInstance().clearTemporarySettingValues();
        ResearchUtil.getInstance().onPause(true);
        LocalResearchUtil.getInstance().sendView(LaunchCondition.LaunchTrigger.SAME_ACTIVITY, capturingMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCoolingUltraLow(boolean z) {
        if (this.mViewFinder != null) {
            this.mViewFinder.onNotifyCoolingUltraLow(z);
        }
        this.mCameraDeviceHandler.setUltraLowPower();
        if (this.mViewFinder != null && this.mViewFinder.isSetupHeadupDisplayInvoked() && this.mCameraDeviceHandler.isObjectTrackingRunning()) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
        }
        this.mObjectTracking.stop();
        this.mGestureShutter.handlePreviewStopped();
    }

    private void pauseAudioPlaybackForCapture() {
        if (!this.mIsPausedAudioPlayback && shouldPlayShutterSound()) {
            this.mActivity.pauseAudioPlayback();
            this.mIsPausedAudioPlayback = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseAudioPlaybackForRecord() {
        this.mActivity.pauseAudioPlayback();
        this.mIsPausedAudioPlayback = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playShutterSound() throws ExecutionException, InterruptedException {
        if (shouldPlayShutterSound()) {
            this.mCameraDeviceHandler.playShutterSound(getCurrentCapturingMode().getType());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldPlayShutterSound() {
        return ((ShutterSound) this.mUserSettings.get(getCurrentCapturingMode(), UserSettingKey.SHUTTER_SOUND)) != ShutterSound.OFF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartObjectTracking(Rect rect) {
        if (isStorageWritable(getCurrentStorage())) {
            this.mObjectTracking.start(rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStopObjectTracking() {
        this.mObjectTracking.stop();
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
    }

    public void setGestureShutterWindowHost(GestureShutter.WindowHost windowHost) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke");
        }
        this.mGestureShutter.setWindowHost(windowHost);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhotoSelftimer(SelfTimer selfTimer) {
        if (this.mViewFinder != null) {
            this.mViewFinder.setSelfTimer((CapturingMode) this.mUserSettings.get(UserSettingKey.CAPTURING_MODE), selfTimer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsSceneRecognitionValid(boolean z) {
        this.mIsSceneRecognitionValid = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchSceneRecognition(boolean z) {
        if (this.mIsSceneRecognitionValid && z) {
            this.mCameraDeviceHandler.startSceneRecognition();
        } else {
            this.mCameraDeviceHandler.stopSceneRecognition();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchVideoFaceDetection() {
        if (DependencyCheckUtil.isFaceDetectionAvailable(getCurrentCapturingMode(), (VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE), (VideoHdr) this.mUserSettings.get(UserSettingKey.VIDEO_HDR))) {
            this.mCameraDeviceHandler.startFaceDetection();
        } else {
            this.mCameraDeviceHandler.stopFaceDetection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySceneRecognitionDisabled() {
        CameraParameters.SceneRecognitionResult sceneRecognitionResult = new CameraParameters.SceneRecognitionResult();
        sceneRecognitionResult.sceneMode = CameraParameterConverter.SceneMode.getSceneMode(0);
        sceneRecognitionResult.deviceStabilityCondition = CameraParameters.DeviceStabilityCondition.getCondition(0);
        sceneRecognitionResult.isMacroRange = false;
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED, sceneRecognitionResult);
    }

    public CapturingMode getLaunchCapturingMode() {
        return this.mActivity.getLaunchCondition().getCapturingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResearchViewEvent() {
        LaunchCondition.LaunchTrigger launchTrigger = this.mActivity.getLaunchCondition().getLaunchTrigger();
        if (launchTrigger == LaunchCondition.LaunchTrigger.VIDEO_EDITOR) {
            launchTrigger = LaunchCondition.LaunchTrigger.OTHER;
        }
        LocalResearchUtil.getInstance().sendView(launchTrigger, getCurrentCapturingMode());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEnoughStorageSizeAvailableForOneShotVideo() {
        com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion;
        if (!this.mActivity.isOneShotVideo()) {
            return true;
        }
        VideoSize videoSize = (VideoSize) this.mUserSettings.get(UserSettingKey.VIDEO_SIZE);
        VideoHdr videoHdr = (VideoHdr) this.mUserSettings.get(UserSettingKey.VIDEO_HDR);
        if (getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && (slowMotion = (com.sonyericsson.android.camera.configuration.parameters.SlowMotion) getUserSetting().get(UserSettingKey.SLOW_MOTION)) == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.STANDARD_SLOW_MOTION) {
            videoSize = slowMotion.getVideoSize();
        }
        RecordingProfile recordingProfileBuild = new RecordingProfile.Builder().videoSize(videoSize).setOneShot(true).videoHdr(videoHdr).build();
        MaxVideoSize maxVideoSize = this.mUserSettings.getMaxVideoSize(this.mStorage, getCurrentStorage(), recordingProfileBuild);
        return maxVideoSize.getMaxDuration() > 0 ? maxVideoSize.getMaxDuration() >= 1000 : maxVideoSize.getMaxFileSize() <= 0 || maxVideoSize.getMaxFileSize() >= recordingProfileBuild.minFileSize * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    private class SuitablePredictiveApplier {
        private boolean isSuppressed;
        private ArrayList<Object> mSuppressors;

        private SuitablePredictiveApplier() {
            this.isSuppressed = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void entrySuppressor(Object obj) {
            if (((PredictiveCapture) StateMachine.this.mUserSettings.get(UserSettingKey.PREDICTIVE_CAPTURE)) == PredictiveCapture.OFF) {
                return;
            }
            if (this.mSuppressors == null) {
                this.mSuppressors = new ArrayList<>();
            }
            if (this.mSuppressors.contains(obj)) {
                return;
            }
            this.mSuppressors.add(obj);
            attemptCommitSettings();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void leaveSuppressor(Object obj) {
            if (this.mSuppressors == null || Boolean.valueOf(this.mSuppressors.remove(obj)) == null) {
                return;
            }
            attemptCommitSettings();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void attemptCommitSettings() {
            if (this.mSuppressors == null || this.mSuppressors.isEmpty()) {
                Storage.StorageState currentState = StateMachine.this.mStorage.getCurrentState(StateMachine.this.getCurrentStorage());
                PredictiveCapture predictiveCapture = PredictiveCapture.OFF;
                if (currentState == Storage.StorageState.AVAILABLE && StateMachine.this.mUserSettings.get(UserSettingKey.SELF_TIMER) == SelfTimer.OFF) {
                    predictiveCapture = (PredictiveCapture) StateMachine.this.mUserSettings.get(UserSettingKey.PREDICTIVE_CAPTURE);
                }
                StateMachine.this.mCameraDeviceHandler.setPredictiveCaptureAndCommit(predictiveCapture);
                this.isSuppressed = false;
                return;
            }
            if (!this.isSuppressed) {
                StateMachine.this.mCameraDeviceHandler.setPredictiveCaptureAndCommit(PredictiveCapture.OFF);
            }
            this.isSuppressed = true;
        }
    }

    public PredictiveCaptureStoreInfo getPredictiveCaptureStoreInfo() {
        return this.mPredictiveCaptureStoreInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkThermalWarning() {
        if (getCurrentCapturingMode() != CapturingMode.SLOW_MOTION && PlatformCapability.isPowerSavingSupported(getCurrentCameraId())) {
            if (this.mActivity.isThermalWarningReceived()) {
                sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
            } else if (this.mActivity.isThermalWarningExtraState()) {
                sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseVideoRecording(Object... objArr) {
        doStopRecording(false);
        changeTo(new StatePause(((Boolean) objArr[0]).booleanValue()), objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasRemainSavingRequest() {
        return this.mCameraDeviceHandler.getRemainPrevSavingRequestCount() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initGeoTagManager() {
        if (this.mActivity.getGeoTagManager() != null) {
            if (this.mActivity.getGeoTagManager().isGeotagPermissionGranted()) {
                this.mUserSettings.set(Geotag.ON);
                this.mActivity.getGeoTagManager().setIsGeotagPermissionGranted(false);
            }
            this.mActivity.getGeoTagManager().initGeotag(this.mActivity, this.mActivity.isAllowToUseLocation());
            this.mActivity.getGeoTagManager().notifyStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSideSenseSetting() {
        if (SettingUtil.isSideSenseEnabled(true)) {
            return;
        }
        this.mUserSettings.set(SideSense.OFF);
    }

    private class OnPreviewStartedListenerImpl implements CameraDeviceHandler.OnPreviewStartedListener {
        private OnPreviewStartedListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.OnPreviewStartedListener
        public void onPreviewStarted(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            StateMachine.this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.controller.StateMachine.OnPreviewStartedListenerImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    StateMachine.this.sendStaticEvent(StaticEvent.EVENT_ON_PREVIEW_STARTED, new Object[0]);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPendingState() {
        updatePhotoSelftimer((SelfTimer) this.mUserSettings.get(UserSettingKey.SELF_TIMER));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStorageWritable(@NonNull Storage.StorageType storageType) {
        Storage.StorageState currentState = this.mStorage.getCurrentState(storageType);
        return currentState != null && currentState.isWritable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStorageFull(Storage.StorageType storageType) {
        return this.mStorage.getCurrentState(storageType) == Storage.StorageState.FULL;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkSaveDestinationCanBeChange(Storage.StorageType storageType) {
        Storage.StorageType storageType2;
        if (storageType == Storage.StorageType.INTERNAL) {
            storageType2 = Storage.StorageType.EXTERNAL_CARD;
        } else {
            if (storageType != Storage.StorageType.EXTERNAL_CARD) {
                return false;
            }
            storageType2 = Storage.StorageType.INTERNAL;
        }
        return isStorageWritable(storageType2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Storage.StorageType getCurrentStorage() {
        if (this.mActivity.isOneShot()) {
            return this.mActivity.getLaunchCondition().getStorageTypeForOneshot();
        }
        DestinationToSave destinationToSave = (DestinationToSave) this.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE);
        if (destinationToSave == null) {
            destinationToSave = (DestinationToSave) this.mUserSettings.get(this.mActivity.getLaunchCondition().getCapturingMode(), UserSettingKey.DESTINATION_TO_SAVE);
        }
        return destinationToSave.getType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCurrentStorageExternal() {
        return getCurrentStorage() == Storage.StorageType.EXTERNAL_CARD;
    }

    public static class OneShotResult {
        public final Bitmap bitmap;
        public final int code;
        public final boolean isSuccess;
        public final SavingRequest savingRequest;
        public final Uri uri;

        private OneShotResult(Uri uri, MediaSavingResult mediaSavingResult, SavingRequest savingRequest, Bitmap bitmap) {
            this.uri = uri;
            this.code = mediaSavingResult.mResultCode;
            this.isSuccess = mediaSavingResult == MediaSavingResult.SUCCESS;
            this.savingRequest = savingRequest;
            this.bitmap = bitmap;
        }
    }

    public boolean isTutorialNeededToBeShownForCurrentMode() {
        if (this.mActivity.isOneShot() || this.mActivity.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            return false;
        }
        MessageSettings messageSettings = this.mActivity.getStoredSettings().getMessageSettings();
        CapturingMode currentCapturingMode = getCurrentCapturingMode();
        if (currentCapturingMode.isFront()) {
            if (currentCapturingMode.isVideo()) {
                return !messageSettings.isNeverShow(MessageType.TUTORIAL_EYE_GUIDE);
            }
            return !messageSettings.isNeverShow(MessageType.TUTORIAL_HAND_SHUTTER);
        }
        switch ((com.sonyericsson.android.camera.configuration.parameters.SlowMotion) getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
            case STANDARD_SLOW_MOTION:
                return !messageSettings.isNeverShow(MessageType.TUTORIAL_STANDARD_SLOW_MOTION);
            case SUPER_SLOW_MOTION:
                return !messageSettings.isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION);
            case SUPER_SLOW_SHOT:
                return !messageSettings.isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT);
            default:
                if (currentCapturingMode.isVideo() && !this.mActivity.isOneShotVideo() && PlatformCapability.isHighSensitivityFusionSupported(currentCapturingMode.getCameraId())) {
                    return !messageSettings.isNeverShow(MessageType.TUTORIAL_VIDEO_FUSION);
                }
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedRepairRequestId(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (photoSavingRequestBuilder.getRequestId() == -1) {
            return photoSavingRequestBuilder.getCaptureIdForPredictiveCapture() == -1 || photoSavingRequestBuilder.getSomcType() == 100;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFusionMonitoringNeeded() {
        for (FusionMode fusionMode : FusionMode.getOptions(getCurrentCapturingMode())) {
            if (fusionMode != FusionMode.OFF) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkBurstConditions(boolean z) {
        if (!isStorageWritable(Storage.StorageType.INTERNAL) || this.mCameraDeviceHandler.getRemainSavingPhotoRequestCount() > 0) {
            return false;
        }
        if (getCurrentCapturingMode() != CapturingMode.SCENE_RECOGNITION || z) {
            return true;
        }
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_IN_DARK_CONDITION);
        return false;
    }

    public boolean isSettingChangeAcceptable() {
        return !isLazyInitializationRunning() && isAllSnapshotCompleted();
    }

    public synchronized boolean isAngleEventReceivable() {
        switch (this.mCurrentState.getCaptureState()) {
            case STATE_PHOTO_READY:
            case STATE_VIDEO_READY:
            case STATE_WARNING:
            case STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION:
            case STATE_VIDEO_RECORDING:
            case STATE_VIDEO_RECORDING_PAUSING:
            case STATE_VIDEO_STORE:
                return true;
            default:
                return false;
        }
    }

    public UserEventHandler.VirtualKeyEventDispatcher getVirtualKeyEventDispatcher() {
        return this.mVirtualKeyEventDispatcher;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T getEventParam(Object[] objArr, int i, Class<T> cls, T t) {
        if (objArr != null && objArr.length > i && cls.isInstance(objArr[i])) {
            return (T) objArr[i];
        }
        if (objArr == null) {
            CamLog.d("Specified parameter is empty.");
        } else if (objArr.length <= i) {
            CamLog.d("Specified parameter count is too short");
        } else if (!objArr[i].getClass().isInstance(cls)) {
            CamLog.d("Specified parameter type is missmatch.");
        }
        return t;
    }
}
