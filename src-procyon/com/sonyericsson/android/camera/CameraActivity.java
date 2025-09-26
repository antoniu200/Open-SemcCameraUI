// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.content.ActivityNotFoundException;
import android.app.ActivityOptions;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import java.lang.ref.WeakReference;
import android.support.annotation.NonNull;
import android.os.Process;
import android.os.SystemClock;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.setting.SettingsFactory;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
import com.sonyericsson.cameracommon.utility.OneShotUtility;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import android.content.IntentFilter;
import com.sonyericsson.android.camera.util.PerfLog;
import java.util.Iterator;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.media.AudioManager$OnAudioFocusChangeListener;
import android.media.AudioManager;
import android.os.Build$VERSION;
import android.util.Log;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import android.content.BroadcastReceiver;
import android.app.AlertDialog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeUtil;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.configuration.IntentReader;
import android.content.Context;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import android.net.Uri;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.List;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import java.util.ArrayList;
import android.content.Intent;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.support.annotation.MainThread;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.CopyOnWriteArraySet;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.OrientationEventListener;
import android.os.Handler;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import java.util.Set;
import android.support.annotation.Nullable;
import android.app.KeyguardManager;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import java.util.concurrent.Future;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import java.util.concurrent.ExecutorService;
import com.sonyericsson.cameracommon.activity.OnActivityResultListener;
import android.util.SparseArray;
import com.sonyericsson.cameracommon.activity.TerminateListener;
import android.content.DialogInterface$OnCancelListener;
import android.app.Activity;

public class CameraActivity extends Activity implements DialogInterface$OnCancelListener, TerminateListener
{
    private static final String ACTION_REQUEST_SOMC_CAMERA_SERVICE = "com.sonymobile.cameracommon.action.REQUEST_SOMC_CAMERA_SERVICE";
    private static final int ASYNC_ACT_TIMEOUT_MILLIS = 3000;
    private static final String CAMERA_COMMON_PACKAGE_NAME = "com.sonymobile.cameracommon";
    public static final String INTENT_SUBJECT_CANCEL = "cancel";
    public static final String INTENT_SUBJECT_PAUSED = "activity-paused";
    public static final String INTENT_SUBJECT_PREPARE = "prepare";
    public static final String INTENT_SUBJECT_RESUMED = "activity-resumed";
    public static final String INTENT_SUBJECT_START = "start";
    public static final String INTENT_SUBJECT_START_SECURE = "start-secure";
    private static final long ON_RESUME_DELAY_NON_SECURE_MILLIS = 15L;
    private static final long ON_RESUME_DELAY_SECURE_MILLIS = 30L;
    private static final long PREPARE_PLATFORM_CAPABILITY_TIMED_OUT_MILLIS = 2000L;
    private static final int RESULT_AUTO_OFF_TIMER = 2;
    private static final long SEND_PAUSE_EVENT_DELAY_MILLIS = 500L;
    public static final int SETUP_DEVICE_SETUP_WAIT_TIME = 100;
    private static final int SETUP_LAZY_EXECUTION_WAIT_TIME = 200;
    private static final String TAG = "CameraActivity";
    private static boolean sIsReportFullyDrawnAlreadyReported = false;
    private final String[] REQUESTED_PERMISSIONS;
    private SparseArray<OnActivityResultListener> mActivityResultListeners;
    private AutoPowerOffTimer mAutoPowerOffTimer;
    private ExecutorService mBackgroundWorker;
    private BatteryChangedReceiver.BatteryChangedReceiverListener mBatteryChangedListener;
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private CameraActivityFinishBroadcastReceiver mCameraActivityFinishReceiver;
    private CameraDeviceHandler mCameraDeviceHandler;
    private boolean mCanFinishByScreenOff;
    private Future<?> mCreateContentInfoTaskFuture;
    private CameraDeviceHandler.CameraSessionId mCurrentCameraSessionId;
    private boolean mDisableMultiWindow;
    private ForceExitRequestReceiver mForceExitRequestReceiver;
    private GeotagManager mGeotagManager;
    private boolean mIsCalledOnDestroy;
    private boolean mIsColdBoot;
    private boolean mIsLazyInitializationRunning;
    private boolean mIsNeedToCloseBypassCameraBecauseModeChanged;
    private boolean mIsReceiverResistered;
    private final UserEventHandler.KeyEventDispatcher mKeyEventDispatcher;
    @Nullable
    private KeyguardManager mKeyguardManager;
    private LayoutOrientation mLastDetectedOrientation;
    private int mLastDeterminedOrientationDegree;
    private int mLastOrientationDegree;
    private LaunchCondition mLaunchCondition;
    private final Set<LayoutOrientationChangedListener> mLayoutOrientationChangedListenerSet;
    private LazyInitializationTask mLazyInitializationiTask;
    private LocationSettingsReader mLocationSettingsReader;
    private Handler mMainHandler;
    private final Runnable mOnResumeTasks;
    private OrientationEventListener mOrientationEventListener;
    private PostDeviceInitializationTask mPostDeviceInitializationTask;
    private boolean mResetSettingsRequested;
    private boolean mReturnOneShotResult;
    private LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private ScreenOffReceiver mScreenOffReceiver;
    private final Runnable mSendPauseEventAndReleaseCameraTask;
    private int mSensorOrientationDegree;
    private Future<?> mSetupAllTaskFuture;
    private OnActivityResultListener mSetupWizardResultListener;
    private ShutDownReceiver mShutDownReceiver;
    private final UserEventHandler.SideTouchEventDispatcher mSideTouchEventDispatcher;
    private boolean mSkippedOnResume;
    private SoundPlayer mSoundPlayer;
    protected StateMachine mStateMachine;
    private Storage mStorage;
    private StoredSettings mStoredSettings;
    private ThermalAlertReceiver.ThermalAlertReceiverListener mThermalAlertListener;
    private ThermalAlertReceiver mThermalAlertReceiver;
    private Future<?> mThermalAlertReceiverReadyTaskFuture;
    private UserEventHandler mUserEventHandler;
    private ViewFinder mViewFinder;
    private Future<?> mViewFinderInitializationTaskFuture;
    private WearableBridgeClient mWearableBridgeClient;
    private ObserveWearableInterface.LifeCycleObserver mWearableBridgeLifeCycleObserver;
    private ObserveWearableInterface.PhotoEventObserver mWearableBridgePhotoEventObserver;
    private ObserveWearableInterface.VideoEventObserver mWearableBridgeVideoEventObserver;
    
    public CameraActivity() {
        this.mReturnOneShotResult = true;
        this.mDisableMultiWindow = false;
        this.mSoundPlayer = null;
        this.mShutDownReceiver = new ShutDownReceiver();
        this.mPostDeviceInitializationTask = null;
        this.mLazyInitializationiTask = null;
        this.mWearableBridgeClient = null;
        this.mWearableBridgeLifeCycleObserver = null;
        this.mWearableBridgePhotoEventObserver = null;
        this.mWearableBridgeVideoEventObserver = null;
        this.mIsLazyInitializationRunning = true;
        this.REQUESTED_PERMISSIONS = new String[] { "android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE" };
        this.mBackgroundWorker = null;
        this.mThermalAlertReceiverReadyTaskFuture = null;
        this.mViewFinderInitializationTaskFuture = null;
        this.mSetupAllTaskFuture = null;
        this.mCreateContentInfoTaskFuture = null;
        this.mIsColdBoot = false;
        this.mIsNeedToCloseBypassCameraBecauseModeChanged = false;
        this.mKeyguardManager = null;
        this.mSkippedOnResume = false;
        this.mLastDetectedOrientation = LayoutOrientation.Unknown;
        this.mLayoutOrientationChangedListenerSet = new CopyOnWriteArraySet<LayoutOrientationChangedListener>();
        this.mSensorOrientationDegree = -1;
        this.mLastOrientationDegree = -1;
        this.mLastDeterminedOrientationDegree = -1;
        this.mIsReceiverResistered = false;
        this.mForceExitRequestReceiver = null;
        this.mCameraActivityFinishReceiver = null;
        this.mKeyEventDispatcher = new UserEventHandler.KeyEventDispatcher();
        this.mSideTouchEventDispatcher = new UserEventHandler.SideTouchEventDispatcher();
        this.mOnResumeTasks = new Runnable() {
            final CameraActivity this$0;
            
            @MainThread
            @Override
            public void run() {
                if (this.this$0.mSkippedOnResume) {
                    if (CamLog.DEBUG) {
                        CamLog.d("Runnable --> onResumeTasks()");
                    }
                    this.this$0.mSkippedOnResume = false;
                    this.this$0.onResumeTasks();
                }
                else if (CamLog.DEBUG) {
                    throw new IllegalStateException("OnResumeTasks was executed when onResume is not skipped.");
                }
            }
        };
        this.mSendPauseEventAndReleaseCameraTask = new Runnable() {
            final CameraActivity this$0;
            
            @MainThread
            @Override
            public void run() {
                if (this.this$0.mSkippedOnResume) {
                    if (CamLog.DEBUG) {
                        CamLog.d("Runnable --> sendEvent(EVENT_PAUSE) & closeCamera()");
                    }
                    this.this$0.mSkippedOnResume = false;
                    this.this$0.notifyActivityState("activity-paused");
                    if (this.this$0.mStateMachine != null) {
                        this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, true);
                    }
                    this.this$0.getCameraDevice().closeCamera(this.this$0.mCurrentCameraSessionId);
                    this.this$0.mCurrentCameraSessionId = null;
                }
                else if (CamLog.DEBUG) {
                    throw new IllegalStateException("PauseEventAndReleaseCameraTask was executed when onResume is not skipped.");
                }
            }
        };
        this.mThermalAlertListener = new ThermalAlertReceiver.ThermalAlertReceiverListener() {
            final CameraActivity this$0;
            
            @Override
            public void onNotifyThermalNormal() {
                LocalResearchUtil.getInstance().setMeasurementThermal(false);
                if (this.this$0.mStateMachine != null) {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_NORMAL, new Object[0]);
                }
            }
            
            @Override
            public void onNotifyThermalWarning(final boolean b) {
                LocalResearchUtil.getInstance().setMeasurementThermal(true);
                if (this.this$0.mStateMachine != null) {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_WARNING, new Object[0]);
                    if (b) {
                        ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_ULTRA_LOW_ON_STARTUP, this.this$0.isRecording());
                    }
                    else {
                        ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_ULTRA_LOW, this.this$0.isRecording());
                    }
                    if (this.this$0.mCameraDeviceHandler != null && PlatformCapability.isPowerSavingSupported(this.this$0.mStateMachine.getCurrentCameraId())) {
                        this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
                    }
                }
            }
            
            @Override
            public void onNotifyThermalWarningExtra(final boolean b) {
                LocalResearchUtil.getInstance().setMeasurementThermal(true);
                if (this.this$0.mStateMachine != null) {
                    if (!this.this$0.isThermalWarningReceived()) {
                        if (b) {
                            ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_LOW_ON_STARTUP, this.this$0.isRecording());
                        }
                        else {
                            ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_LOW, this.this$0.isRecording());
                        }
                    }
                    if (this.this$0.mCameraDeviceHandler != null && PlatformCapability.isPowerSavingSupported(this.this$0.mStateMachine.getCurrentCameraId())) {
                        if (!this.this$0.isThermalWarningReceived()) {
                            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
                        }
                    }
                    else {
                        this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_WARNING_EXTRA, new Object[0]);
                    }
                }
            }
            
            @Override
            public void onReachCriticalTemperature(final boolean b) {
                LocalResearchUtil.getInstance().setMeasurementThermal(true);
                if (this.this$0.mStateMachine != null) {
                    ResearchUtil.getInstance().sendThermalEvent(b, this.this$0.isRecording());
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_CRITICAL, b);
                }
                this.this$0.releaseCamera();
            }
        };
        this.mBatteryChangedListener = new BatteryChangedReceiver.BatteryChangedReceiverListener() {
            final CameraActivity this$0;
            
            @Override
            public void onBatteryLevelChanged(final int n) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("onBatteryLevelChanged : ");
                    sb.append(n);
                    CamLog.d(sb.toString());
                }
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BATTERY_LEVEL_CHANGED, n);
            }
            
            @Override
            public void onReachBatteryLimit(final boolean b) {
                ResearchUtil.getInstance().sendLowBatteryEvent(b, this.this$0.isRecording());
                if (this.this$0.mStateMachine != null) {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_REACH_BATTERY_LIMIT, new Object[0]);
                }
                this.this$0.releaseCamera();
            }
            
            @Override
            public void onReachLowBattery() {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_REACH_BATTERY_LOW, new Object[0]);
            }
        };
        this.mSetupWizardResultListener = new OnActivityResultListener() {
            final CameraActivity this$0;
            
            private void requestLocationChange(final boolean b) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("requestLocationChange: enable = ");
                    sb.append(b);
                    CamLog.d(sb.toString());
                }
                if (b) {
                    this.this$0.getGeoTagManager().setIsGeotagPermissionGranted(true);
                }
                final MessageSettings messageSettings = this.this$0.getStoredSettings().getMessageSettings();
                messageSettings.setNeverShow(MessageType.SETUP_WIZARD, true);
                messageSettings.save();
            }
            
            @Override
            public boolean onActivityResult(final int n, final int n2, final Intent intent) {
                if (n2 != -1) {
                    if (n2 == 1) {
                        this.this$0.finish();
                    }
                }
                else {
                    final boolean booleanExtra = intent.getBooleanExtra("geo_tag_result", false);
                    final boolean booleanExtra2 = intent.getBooleanExtra("side_sense_result", false);
                    this.requestLocationChange(booleanExtra);
                    final ArrayList list = new ArrayList();
                    SideSense e;
                    if (booleanExtra2) {
                        e = SideSense.ON;
                    }
                    else {
                        e = SideSense.OFF;
                    }
                    list.add(e);
                    if (PlatformCapability.isLiftTriggerSupported()) {
                        list.add(PredictiveLaunch.TOUCH_TO_LAUNCH);
                    }
                    this.this$0.getLaunchCondition().getExtraSettings().set(this.this$0.getLaunchCondition().getCapturingMode(), list);
                }
                return true;
            }
        };
        this.mCanFinishByScreenOff = true;
    }
    
    private void abort(final boolean b) {
        if (this.mStateMachine != null) {
            if (b) {
                if (!this.isDeviceInSecurityLock() && !this.isInLockTaskMode()) {
                    if (this.isOneShot()) {
                        if (this.mReturnOneShotResult) {
                            this.setResult(0);
                        }
                        this.finishUrgently();
                    }
                    else {
                        this.requestSuspend();
                    }
                }
                else {
                    this.finish();
                }
            }
            else {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, false);
            }
        }
    }
    
    private boolean addActivityResultListener(final int n, final OnActivityResultListener onActivityResultListener) {
        if (this.mActivityResultListeners == null) {
            this.mActivityResultListeners = (SparseArray<OnActivityResultListener>)new SparseArray();
        }
        if (this.mActivityResultListeners.get(n) != null) {
            return false;
        }
        this.mActivityResultListeners.put(n, (Object)onActivityResultListener);
        return true;
    }
    
    private void addExternalCameraAppContent(final Intent intent) {
        if (intent == null) {
            return;
        }
        if (this.isDeviceInSecurityLock()) {
            final ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra("com.sonyericsson.android.camera.extra.CAPTURED_URIS");
            if (parcelableArrayListExtra != null && !parcelableArrayListExtra.isEmpty()) {
                this.mCreateContentInfoTaskFuture = this.mBackgroundWorker.submit(new CreateContentInfoTask(parcelableArrayListExtra));
            }
        }
    }
    
    private void allowFinishByScreenOff() {
        this.mCanFinishByScreenOff = true;
    }
    
    private boolean awaitCreateContentInfoReady() {
        return getFuture(this.mCreateContentInfoTaskFuture);
    }
    
    private boolean awaitThermalAlertReceiverReady() {
        return getFuture(this.mThermalAlertReceiverReadyTaskFuture);
    }
    
    private boolean canFinishByScreenOff() {
        return this.mCanFinishByScreenOff;
    }
    
    private void cancelDelayedEvent(final Runnable runnable) {
        CameraApplication.getUiThreadHandler().removeCallbacks(runnable);
    }
    
    private void changeParametersForGoogleAssistant() {
        final UserSettings userSettings = this.mStoredSettings.getUserSettings();
        final Parameters parameters = userSettings.getParameters();
        if (parameters == null) {
            this.clearLaunchCameraMode();
            return;
        }
        switch (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$LaunchCondition$LaunchCameraMode[this.mLaunchCondition.getLaunchCameraMode().ordinal()]) {
            case 2: {
                userSettings.set(SlowMotion.SUPER_SLOW_MOTION);
                break;
            }
            case 1: {
                final VideoCodec videoCodec = parameters.getVideoCodec();
                final VideoSize videoSize = parameters.getVideoSize();
                if (videoCodec != null && videoSize != null) {
                    if (!videoSize.is4KVideo()) {
                        if (videoCodec == VideoCodec.H264) {
                            userSettings.set(VideoSize.FOUR_K_UHD_H264);
                        }
                        else if (videoCodec == VideoCodec.H265) {
                            userSettings.set(VideoSize.FOUR_K_UHD_H265);
                        }
                    }
                    userSettings.set(VideoHdr.HDR_ON);
                    break;
                }
                this.clearLaunchCameraMode();
            }
        }
    }
    
    private void createBatteryChangedReceiver() {
        this.mBatteryChangedReceiver = new BatteryChangedReceiver((Context)this, this.mBatteryChangedListener);
    }
    
    private void createLaunchCondition() {
        this.mLaunchCondition = new LaunchCondition(new IntentReader().getVideoQualityConfigurations(this.getIntent()));
    }
    
    private ViewFinderImpl createViewFinder() {
        final ViewFinderImpl viewFinderImpl = new ViewFinderImpl((Context)this, this.shouldShowWhenLocked(), this.getScreenAspect(), this.getStoredSettings().getUiControlSettings());
        viewFinderImpl.setStateMachine(this.mStateMachine);
        viewFinderImpl.setCameraDevice(this.getCameraDevice());
        viewFinderImpl.prepareSelfTimerAndTouchCapture();
        this.mViewFinderInitializationTaskFuture = this.mBackgroundWorker.submit(new ViewFinderInitializationTask(viewFinderImpl));
        return viewFinderImpl;
    }
    
    private void disableOrientation() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableOrientation()");
        }
        if (this.mOrientationEventListener != null) {
            this.mOrientationEventListener.disable();
            this.mOrientationEventListener = null;
        }
    }
    
    private void doPause() {
        this.getCameraDevice().removeOnPreviewStartedListener();
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, true);
        if (this.mCameraDeviceHandler != null) {
            final CapturingMode capturingMode = (CapturingMode)this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE);
            if (capturingMode != null) {
                this.mCameraDeviceHandler.savePreloadSettings(capturingMode, this.mStoredSettings.getUserSettings(), this.mStoredSettings.getLastSettings(), this.isOneShot());
            }
        }
        if (this.mResetSettingsRequested) {
            this.mStoredSettings.clearAllSettings(this.getStorage());
        }
        this.releaseCamera();
        this.mCurrentCameraSessionId = null;
        this.mBackgroundWorker.execute(new ThermalAlertReceiverOnPauseTask());
        if (this.mGeotagManager != null) {
            this.mGeotagManager.releaseResource();
        }
        if (this.mPostDeviceInitializationTask != null) {
            this.cancelDelayedEvent(this.mPostDeviceInitializationTask);
        }
        if (this.mLazyInitializationiTask != null) {
            this.cancelDelayedEvent(this.mLazyInitializationiTask);
        }
        this.mStateMachine.releaseContentsViewController();
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getLifeCycleNotifier().onPause();
        }
        if (this.mIsReceiverResistered) {
            this.mIsReceiverResistered = false;
        }
        this.disableAutoPowerOffTimer();
        this.getWindow().clearFlags(128);
        this.unmuteSound();
        this.mViewFinder.clearMessageDialog();
        this.disableOrientation();
        ResearchUtil.getInstance().onPause(false);
        if (this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE) != null) {
            this.mLaunchCondition.setCapturingMode((CapturingMode)this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE));
        }
        this.mLaunchCondition.onPause();
        this.mBackgroundWorker.execute(new StartGyroCalibrationOnPauseTask());
    }
    
    private void doResume() {
        if (!this.mLaunchCondition.isCorrectExtraOutputPath()) {
            final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
            alertDialog$Builder.setTitle(2131689789).setMessage(2131689788).setCancelable(false).setPositiveButton(2131689975, (DialogInterface$OnClickListener)null).setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener(this) {
                final CameraActivity this$0;
                
                public void onDismiss(final DialogInterface dialogInterface) {
                    this.this$0.abort();
                }
            });
            final AlertDialog create = alertDialog$Builder.create();
            create.getWindow().addFlags(128);
            create.show();
            return;
        }
        if (!this.isKeyguardLocked() && this.mLaunchCondition.isLaunchInternalMode()) {
            final ModeSelectorInternalMode obj = ModeSelectorInternalMode.values()[this.mLaunchCondition.getLaunchInternalMode()];
            this.mLaunchCondition.clearLaunchInternalMode();
            final CapturingMode capturingMode = CapturingMode.values()[this.mLaunchCondition.getLaunchInternalCallingCapturingMode()];
            this.mLaunchCondition.clearLaunchInternalCallingCapturingMode();
            final boolean contains = CapturingModeUtil.MODE_WHITE_LIST.contains(obj.name());
            switch (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[obj.ordinal()]) {
                default: {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Bad launch internalMode :");
                        sb.append(obj);
                        CamLog.e(sb.toString());
                        break;
                    }
                    break;
                }
                case 3: {
                    ApplicationLauncher.launchExternalCamera(this, 16, this.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, contains);
                    return;
                }
                case 2: {
                    ApplicationLauncher.launchExternalCamera(this, 17, this.mStateMachine.getUserSetting(), capturingMode, contains);
                    return;
                }
                case 1: {
                    ApplicationLauncher.launchExternalCamera(this, 18, this.mStateMachine.getUserSetting(), capturingMode, contains);
                    return;
                }
            }
        }
        this.enableOrientation();
        if (this.getLaunchCondition().getLaunchTrigger() != LaunchCondition.LaunchTrigger.VIEWER) {
            this.mStoredSettings.getUserSettings().clearCachedUserSetting();
        }
        this.mStoredSettings.getUserSettings().changeCapturingMode(this.mLaunchCondition.getCapturingMode());
        if (this.mLaunchCondition.getLaunchCameraMode().isLaunchedByGoogleAssistant()) {
            this.changeParametersForGoogleAssistant();
        }
        (this.mCameraDeviceHandler = this.getCameraDevice()).setStateMachine(this.mStateMachine);
        FastCapture fastCapture;
        if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            fastCapture = FastCapture.LAUNCH_AND_CAPTURE;
        }
        else {
            fastCapture = FastCapture.LAUNCH_ONLY;
        }
        this.mCurrentCameraSessionId = this.prepareCameraDeviceHandler(fastCapture, this.mLaunchCondition.getCapturingMode(), this.mStoredSettings.getUserSettings());
        this.mCameraDeviceHandler.setOnPreviewStartedListener((CameraDeviceHandler.OnPreviewStartedListener)new OnPreviewStartedListenerImpl(this.mCurrentCameraSessionId));
        this.getWindow().clearFlags(2048);
        this.getWindow().addFlags(1024);
        this.getWindow().addFlags(256);
        this.getWindow().addFlags(512);
        this.getWindow().addFlags(128);
        this.getCameraDevice().awaitLoadSettingsThread();
        this.mLocationSettingsReader.readLocationSettings((Context)this);
        this.setupAutoPowerOffTimeOutDuration(this.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER);
        this.enableAutoPowerOffTimer();
        ResearchUtil.getInstance().onResume(false);
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().joinInitializeTask();
            this.getWearableBridge().getLifeCycleNotifier().onResume();
        }
        this.getCameraDevice().disableFpsLimitation();
        ((ViewFinderImpl)this.mViewFinder).updatePreviewLayoutParams();
        if (this.isDeviceInSecurityLock() && (this.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.DUAL_CAMERA_EFFECT || this.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.PORTRAIT_SELFIE)) {
            this.awaitCreateContentInfoReady();
        }
        if (LocalResearchUtil.getInstance().isMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE) && LocalResearchUtil.getInstance().isMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE)) {
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        }
        LocalResearchUtil.getInstance().setLaunchBy(this.mLaunchCondition.getLaunchTrigger());
        LocalResearchUtil.getInstance().setView(this.mLaunchCondition.getCapturingMode());
        final StateMachine.StartupAction none = StateMachine.StartupAction.NONE;
        Enum<StateMachine.StartupAction> enum1;
        if (this.mLaunchCondition.getGoogleAssistantSelfTimer() > 0) {
            enum1 = StateMachine.StartupAction.CAPTURE;
        }
        else {
            enum1 = none;
            if (!this.mLaunchCondition.isGoogleAssistantLaunchOnly()) {
                enum1 = none;
                if (this.mLaunchCondition.getCapturingMode().isVideo()) {
                    enum1 = StateMachine.StartupAction.RECORD;
                }
            }
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_RESUME, fastCapture, this.mCurrentCameraSessionId, enum1);
        if (this.mLastDetectedOrientation == LayoutOrientation.Unknown) {
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 2) {
                this.setLayoutOrientation(LayoutOrientation.Landscape);
                this.mLastDetectedOrientation = LayoutOrientation.Landscape;
            }
            else {
                this.setLayoutOrientation(LayoutOrientation.Portrait);
                this.mLastDetectedOrientation = LayoutOrientation.Portrait;
            }
        }
        if (this.isInMultiWindowMode() && !this.mDisableMultiWindow) {
            this.launchMultiWindow();
        }
        this.mBatteryChangedReceiver.checkStartupStatus();
        final boolean neverShow = this.getStoredSettings().getMessageSettings().isNeverShow(MessageType.SETUP_WIZARD);
        if (PlatformCapability.isLiftTriggerSupported() && this.mStoredSettings.getUserSettings().get(UserSettingKey.PREDICTIVE_LAUNCH) == PredictiveLaunch.OFF && this.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            this.mBackgroundWorker.execute(new ScreenOffTask());
            this.terminateApplication();
            return;
        }
        if ((neverShow ^ true) && !this.mBatteryChangedReceiver.isAlreadyBcl() && !this.isOneShot() && this.getLaunchCondition().getLaunchTrigger() != LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            this.startActivityForResult(new Intent((Context)this, (Class)SetupWizardActivity.class), 15, this.mSetupWizardResultListener);
        }
        else {
            this.mThermalAlertReceiverReadyTaskFuture = this.mBackgroundWorker.submit(new ThermalAlertReceiverOnResumeTask());
        }
        this.requestInflateUiComponents();
    }
    
    private void enableOrientation() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableOrientation()");
        }
        if (this.mOrientationEventListener == null) {
            (this.mOrientationEventListener = new ExtendedOrientationEventListener((Context)this)).enable();
        }
    }
    
    private void finishUrgently() {
        this.prepareFinish();
        super.finish();
    }
    
    private void getDownAll() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINALIZE, new Object[0]);
        this.mStoredSettings.getUserSettings().release();
        this.mStateMachine = null;
        this.mViewFinder = null;
        this.mAutoPowerOffTimer = null;
        this.mUserEventHandler.release();
        this.mUserEventHandler = null;
        this.releaseSoundPlayer();
        if (this.mGeotagManager != null) {
            this.mGeotagManager.release();
        }
        this.unregisterReceiver((BroadcastReceiver)this.mScreenOffReceiver);
        this.mScreenOffReceiver = null;
        this.mThermalAlertReceiverReadyTaskFuture = null;
        this.mViewFinderInitializationTaskFuture = null;
        this.mSetupAllTaskFuture = null;
        this.mShutDownReceiver = null;
    }
    
    private static boolean getFuture(final Future<?> future) {
        if (future == null) {
            return true;
        }
        try {
            future.get(3000L, TimeUnit.MILLISECONDS);
            return true;
        }
        catch (final TimeoutException ex) {
            CamLog.e("Failed to get Future.", ex);
        }
        catch (final InterruptedException ex2) {
            CamLog.e("Failed to get Future.", ex2);
        }
        catch (final ExecutionException ex3) {
            CamLog.e("Failed to get Future.", ex3);
        }
        catch (final CancellationException ex4) {
            CamLog.e("Failed to get Future.", ex4);
        }
        return false;
    }
    
    private int getOrientationDegree(final LayoutOrientation layoutOrientation) {
        int n = 0;
        switch (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[layoutOrientation.ordinal()]) {
            default: {
                return -1;
            }
            case 5: {
                n = 270;
                break;
            }
            case 4: {
                n = 180;
                break;
            }
            case 3: {
                n = 90;
                break;
            }
            case 1:
            case 2: {
                n = 0;
                break;
            }
        }
        return (n + ProductConfig.getMountAngle((Context)this)) % 360;
    }
    
    private static boolean in(final int n, final int n2, final int n3) {
        return n >= n2 && n < n3;
    }
    
    private boolean isKeyguardLocked() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager)this.getSystemService("keyguard");
        }
        return this.mKeyguardManager != null && this.mKeyguardManager.isKeyguardLocked();
    }
    
    private boolean isLaunchedFromLocked(final Intent intent) {
        final String stringExtra = intent.getStringExtra("com.sonyericsson.android.camera.extra.launchTrigger");
        if (stringExtra != null) {
            if (LaunchCondition.LaunchTrigger.HW_CAMERA_KEY.toString().equals(stringExtra)) {
                return true;
            }
            if (LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK.toString().equals(stringExtra)) {
                return true;
            }
            if (LaunchCondition.LaunchTrigger.LOCK_SCREEN.toString().equals(stringExtra)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isRecording() {
        return this.mStateMachine != null && this.mStateMachine.isRecording();
    }
    
    private void launchMultiWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("Launch multi window mode activity ");
        }
        this.mDisableMultiWindow = true;
        this.mReturnOneShotResult = false;
        final Intent obj = new Intent(this.getIntent());
        obj.setClass(this.getApplicationContext(), (Class)MultiWindowActivity.class);
        if (obj.getAction() == null) {
            if (this.isOneShotPhoto()) {
                obj.setAction("android.media.action.IMAGE_CAPTURE");
            }
            else {
                if (!this.isOneShotVideo()) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("SomcCamera not support multi window mode, intent :[");
                        sb.append(obj);
                        sb.append("]");
                        CamLog.e(sb.toString());
                    }
                    this.terminateApplication();
                    return;
                }
                obj.setAction("android.media.action.VIDEO_CAPTURE");
            }
        }
        else if (!this.isOneShotVideo() && !this.isOneShotPhoto()) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("SomcCamera not support multi window mode, intent :[");
                sb2.append(obj);
                sb2.append("]");
                CamLog.e(sb2.toString());
            }
            this.terminateApplication();
            return;
        }
        obj.addFlags(33554432);
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Start multi window activity, intent : ");
            sb3.append(obj);
            CamLog.d(sb3.toString());
        }
        this.startActivity(obj);
        this.terminateApplication();
    }
    
    private void logLifeCycleIn(final String s, final LifeCycleIds obj) {
        MeasurePerformance.measureTime(obj.mPerformanceIds, true);
        final StringBuilder sb = new StringBuilder();
        sb.append("Start ");
        sb.append(obj);
        MeasurePerformance.measureResource(sb.toString());
        if (obj != LifeCycleIds.ON_CREATE && obj != LifeCycleIds.ON_DESTROY) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(obj);
                sb2.append(" is called");
                CamLog.d(s, sb2.toString());
            }
        }
        else if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(obj);
            sb3.append(" is called:");
            sb3.append(this.hashCode());
            CamLog.d(s, sb3.toString());
        }
    }
    
    private void logLifeCycleOut(final String s, final LifeCycleIds lifeCycleIds) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append(lifeCycleIds);
            sb.append(" FINISH.");
            CamLog.d(s, sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("End ");
        sb2.append(lifeCycleIds);
        MeasurePerformance.measureResource(sb2.toString());
        MeasurePerformance.measureTime(lifeCycleIds.mPerformanceIds, false);
        if (lifeCycleIds == LifeCycleIds.ON_PAUSE) {
            MeasurePerformance.outResult();
        }
    }
    
    private static void logPerformance(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[PERFORMANCE] [TIME = ");
        sb.append(System.currentTimeMillis());
        sb.append("] [");
        sb.append("CameraActivity");
        sb.append("] [");
        sb.append(Thread.currentThread().getName());
        sb.append(" : ");
        sb.append(str);
        sb.append("]");
        Log.e("TraceLog", sb.toString());
    }
    
    private void muteSound() {
        if (Build$VERSION.SDK_INT >= 8) {
            final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
            if (audioManager != null) {
                if (audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)null, 3, 1) == 1) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("muteSound: success");
                    }
                }
                else if (CamLog.VERBOSE) {
                    CamLog.d("muteSound: fail");
                }
            }
        }
    }
    
    private void notifyActivityState(final String s) {
        final Intent intent = new Intent("android.intent.action.CAMERA_BUTTON", (Uri)null);
        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)new KeyEvent(0, 27));
        intent.putExtra("android.intent.extra.SUBJECT", s);
        intent.addFlags(268435456);
        intent.setPackage(this.getPackageName());
        this.getApplicationContext().sendOrderedBroadcast(intent, (String)null);
    }
    
    private void notifyLayoutOrientationChanged(final LayoutOrientation layoutOrientation) {
        if (layoutOrientation == this.mLastDetectedOrientation) {
            return;
        }
        if (layoutOrientation == LayoutOrientation.Unknown) {
            return;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ORIENTATION : Layout orientation(sensor) is changed from ");
            sb.append(this.mLastDetectedOrientation);
            sb.append(" to ");
            sb.append(layoutOrientation);
            CamLog.d(sb.toString());
        }
        this.mLastDetectedOrientation = layoutOrientation;
        final Iterator<LayoutOrientationChangedListener> iterator = this.mLayoutOrientationChangedListenerSet.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLayoutOrientationChanged(this.mLastDetectedOrientation);
        }
    }
    
    private void onResumeTasks() {
        PerfLog.ACTIVITY_ON_RESUME.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : E");
        }
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_RESUME);
        }
        this.getCameraDevice().setActivityForeground(true);
        this.getLaunchCondition().onResume();
        if (!this.checkAndRequestSelfPermissions(12, this.REQUESTED_PERMISSIONS)) {
            this.doResume();
        }
        else if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            if (CamLog.VERBOSE) {
                CamLog.d("start clearExtraOperation");
            }
            this.mLaunchCondition.clearExtraOperation();
        }
        this.sendBroadcast(new Intent("com.sonyericsson.android.camera.intent.action.LAUNCH"));
        CameraButtonIntentReceiver.releaseCameraDeviceReleaseTimer();
        this.notifyActivityState("activity-resumed");
        this.mKeyEventDispatcher.start();
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_RESUME);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : X");
        }
        PerfLog.ACTIVITY_ON_RESUME.end();
    }
    
    private void postEvent(final Runnable runnable) {
        CameraApplication.getUiThreadHandler().post(runnable);
    }
    
    public static final void preload() {
    }
    
    private void prepareFinish() {
        if (this.mStateMachine != null) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, false);
        }
    }
    
    private void registerForceExitRequestReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("registerForceExitRequestReceiver()");
        }
        this.registerReceiver((BroadcastReceiver)(this.mForceExitRequestReceiver = new ForceExitRequestReceiver()), new IntentFilter("com.sonymobile.cameracommon.intent.ACTION_FORCE_EXIT_REQUEST"));
        if (!this.shouldShowWhenLocked()) {
            this.registerReceiver((BroadcastReceiver)(this.mCameraActivityFinishReceiver = new CameraActivityFinishBroadcastReceiver(this)), new IntentFilter("com.sonyericsson.android.camera.intent.action.FINISH_CAMERAACTIVITY"));
        }
    }
    
    private void registerShutDownReceiver() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.registerReceiver((BroadcastReceiver)this.mShutDownReceiver, intentFilter);
    }
    
    private void releaseCamera() {
        if (this.mCameraDeviceHandler != null) {
            this.mCameraDeviceHandler.disableFpsLimitation();
            this.mCameraDeviceHandler.closeCamera();
            this.mCameraDeviceHandler.setStateMachine(null);
            this.mCameraDeviceHandler = null;
        }
        else {
            this.getCameraDevice().closeCamera();
        }
    }
    
    private void releaseSoundPlayer() {
        if (this.mSoundPlayer != null) {
            this.mSoundPlayer.release();
            this.mSoundPlayer = null;
        }
    }
    
    private void releaseWearableFramework() {
        if (this.mWearableBridgeClient != null) {
            this.mWearableBridgeClient.release();
            this.mWearableBridgeClient = null;
        }
        this.mWearableBridgeLifeCycleObserver = null;
        this.mWearableBridgePhotoEventObserver = null;
        this.mWearableBridgeVideoEventObserver = null;
    }
    
    private void requestFinishCameraActivity() {
        if (CamLog.VERBOSE) {
            CamLog.d("requestFinishCameraActivity()");
        }
        if (this.shouldShowWhenLocked()) {
            this.sendBroadcast(new Intent("com.sonyericsson.android.camera.intent.action.FINISH_CAMERAACTIVITY"));
        }
    }
    
    private void requestInflateUiComponents() {
        this.mViewFinder.requestInflate(this.getLayoutInflater());
    }
    
    private void setLayoutOrientation(final LayoutOrientation layoutOrientation) {
        final int orientationDegree = this.getOrientationDegree(layoutOrientation);
        this.mSensorOrientationDegree = orientationDegree;
        this.mLastOrientationDegree = orientationDegree;
        this.mLastDeterminedOrientationDegree = orientationDegree;
    }
    
    private void setupAll() {
        this.mGeotagManager = new GeotagManager((Context)this);
        this.mSoundPlayer = new SoundPlayer(this.getApplicationContext());
    }
    
    private void setupCoreInstance() {
        if (CamLog.VERBOSE) {
            CamLog.d("invoked setupCoreInstance()");
        }
        this.mStateMachine = new StateMachine(this, this.mStorage);
        final ViewFinderImpl viewFinder = this.createViewFinder();
        this.mViewFinder = viewFinder;
        this.mUserEventHandler = new UserEventHandler(this, viewFinder, this.mStateMachine, this.mStorage, this.mStoredSettings.getUserSettings(), this.mStoredSettings.getMessageSettings(), this.getLaunchCondition().getOneShotMode().isEnabled() ^ true);
        this.mKeyEventDispatcher.attach(this.mUserEventHandler);
        this.mKeyEventDispatcher.start();
        this.mSideTouchEventDispatcher.attach(this.mUserEventHandler);
        viewFinder.getTouchEventDispatcher().attach(this.mUserEventHandler);
        viewFinder.getTouchEventDispatcher().start();
        this.mStateMachine.getVirtualKeyEventDispatcher().attach(this.mUserEventHandler);
        this.mStateMachine.getVirtualKeyEventDispatcher().start();
        this.mStateMachine.setDependencies(this.mViewFinder, this.getCameraDevice());
    }
    
    private void setupScreenOffReceiver() {
        this.mScreenOffReceiver = new ScreenOffReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        if (this.shouldShowWhenLocked()) {
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }
        this.registerReceiver((BroadcastReceiver)this.mScreenOffReceiver, intentFilter);
    }
    
    private void setupWearableFramework() {
        this.mWearableBridgeLifeCycleObserver = new WearableBridgeLifeCycleObserver();
        this.mWearableBridgePhotoEventObserver = new WearableBridgePhotoEventObserver();
        this.mWearableBridgeVideoEventObserver = new WearableBridgeVideoEventObserver();
        this.mWearableBridgeClient = new WearableBridgeClient(this, new Handler(), this.mWearableBridgeLifeCycleObserver, this.mWearableBridgePhotoEventObserver, this.mWearableBridgeVideoEventObserver);
    }
    
    private void unRegisterShutDownReceiver() {
        this.unregisterReceiver((BroadcastReceiver)this.mShutDownReceiver);
    }
    
    private void unmuteSound() {
        if (Build$VERSION.SDK_INT >= 8) {
            final AudioManager audioManager = (AudioManager)this.getSystemService("audio");
            if (audioManager != null) {
                if (audioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)null) == 1) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("unmuteSound: success");
                    }
                }
                else if (CamLog.VERBOSE) {
                    CamLog.d("unmuteSound: fail");
                }
            }
        }
    }
    
    private void unregisterForceExitRequestReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("unregisterForceExitRequestReceiver()");
        }
        if (this.mForceExitRequestReceiver != null) {
            this.unregisterReceiver((BroadcastReceiver)this.mForceExitRequestReceiver);
            this.mForceExitRequestReceiver = null;
        }
        if (this.mCameraActivityFinishReceiver != null) {
            this.unregisterReceiver((BroadcastReceiver)this.mCameraActivityFinishReceiver);
            this.mCameraActivityFinishReceiver = null;
        }
    }
    
    public void abort() {
        if (CamLog.VERBOSE) {
            CamLog.d("call abort()");
        }
        this.abort(true);
    }
    
    public void addOrienationListener(final LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.add(layoutOrientationChangedListener);
    }
    
    public boolean awaitSetupAllReady() {
        return getFuture(this.mSetupAllTaskFuture);
    }
    
    public boolean awaitViewFinderReady() {
        boolean b;
        if (this.mViewFinderInitializationTaskFuture != null) {
            final boolean future = getFuture(this.mViewFinderInitializationTaskFuture);
            this.mViewFinderInitializationTaskFuture = null;
            b = future;
            if (future) {
                this.mViewFinder.attachToWindow();
                b = future;
            }
        }
        else {
            b = true;
        }
        return b;
    }
    
    public boolean checkAndRequestSelfPermissions(final int n, final String[] array) {
        return this.checkAndRequestSelfPermissions(n, array, null);
    }
    
    public boolean checkAndRequestSelfPermissions(final int n, final String[] array, final PermissionCheckCallback permissionCheckCallback) {
        final boolean checkAndRequestSelfPermissions = PermissionsUtil.checkAndRequestSelfPermissions(this, n, array);
        if (checkAndRequestSelfPermissions) {
            this.addActivityResultListener(n, new OnActivityResultListener(this, array, permissionCheckCallback) {
                final CameraActivity this$0;
                final PermissionCheckCallback val$callback;
                final String[] val$permissions;
                
                @Override
                public boolean onActivityResult(final int i, final int j, final Intent intent) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("RequestPermissionActivity requestCode=");
                        sb.append(i);
                        sb.append(", result=");
                        sb.append(j);
                        CamLog.d(sb.toString());
                    }
                    Label_0105: {
                        switch (i) {
                            default: {
                                break Label_0105;
                            }
                            case 12: {
                                if (j == -1 && !PermissionsUtil.arePermissionsGranted((Context)this.this$0, this.val$permissions)) {
                                    this.this$0.finish();
                                }
                                break Label_0105;
                            }
                            case 13: {
                                if (this.val$callback != null) {
                                    this.val$callback.onPermissionChecked(this.val$permissions);
                                }
                                return true;
                            }
                        }
                    }
                }
            });
        }
        return checkAndRequestSelfPermissions;
    }
    
    public void clearLaunchCameraMode() {
        this.mLaunchCondition.clearLaunchCameraMode();
    }
    
    public final void disableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableAutoPowerOffTimer: ");
        }
        this.mViewFinder.hideAutoPowerOffHintText();
        this.mAutoPowerOffTimer.disableAutoPowerOffTimer();
    }
    
    public void disableSideSense() {
        this.mSideTouchEventDispatcher.stop();
    }
    
    public final void enableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableAutoPowerOffTimer: ");
        }
        this.mAutoPowerOffTimer.enableAutoPowerOffTimer();
    }
    
    public void enableSideSense() {
        this.mSideTouchEventDispatcher.start();
    }
    
    public void finish() {
        if (!this.isInLockTaskMode()) {
            this.prepareFinish();
        }
        super.finish();
    }
    
    public void finishAndKillProcess() {
        this.prepareFinish();
        this.finishAndRemoveTask();
    }
    
    public void finishOneShot(final StateMachine.OneShotResult oneShotResult) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("finishOneShot: result: ");
            sb.append(oneShotResult.uri);
            CamLog.d(sb.toString());
        }
        this.mDisableMultiWindow = true;
        if (this.isOneShotVideo()) {
            final Intent intent = new Intent();
            intent.setData(oneShotResult.uri);
            intent.addFlags(1);
            this.setResult(oneShotResult.code, intent);
            this.terminateApplication();
        }
        else if (this.isOneShotPhoto()) {
            if (oneShotResult.savingRequest.getExtraOutput() == null) {
                if (oneShotResult.isSuccess) {
                    this.setResult(oneShotResult.code, OneShotUtility.createResultIntent(this, oneShotResult.uri, oneShotResult.savingRequest.common.mimeType, oneShotResult.savingRequest.common.orientation, oneShotResult.bitmap));
                }
                else {
                    this.setResult(0);
                }
            }
            else {
                this.setResult(oneShotResult.code);
            }
            this.finish();
        }
    }
    
    public int getBatteryLevel() {
        return this.mBatteryChangedReceiver.getBatteryLevel();
    }
    
    public CameraDeviceHandler getCameraDevice() {
        return ((CameraApplication)this.getApplicationContext()).getCameraDevice();
    }
    
    public Uri getExtraOutput() {
        return this.mLaunchCondition.getExtraOutput();
    }
    
    public GeotagManager getGeoTagManager() {
        return this.mGeotagManager;
    }
    
    public LayoutOrientation getLastDetectedOrientation() {
        return this.mLastDetectedOrientation;
    }
    
    public LaunchCondition getLaunchCondition() {
        return this.mLaunchCondition;
    }
    
    public LayoutOrientation getLayoutOrientation() {
        int n;
        if ((n = this.mLastOrientationDegree) == -1) {
            n = this.mLastDeterminedOrientationDegree;
        }
        if (n == -1) {
            return LayoutOrientation.Unknown;
        }
        final int n2 = (n + (360 - ProductConfig.getMountAngle((Context)this))) % 360;
        int n3;
        if (this.mLastDetectedOrientation == LayoutOrientation.Portrait || this.mLastDetectedOrientation == LayoutOrientation.ReversePortrait) {
            n3 = 60;
        }
        else {
            n3 = 30;
        }
        final int n4 = 90 + n3;
        if (in(n2, 90 - n3, n4)) {
            return LayoutOrientation.Portrait;
        }
        final int n5 = 270 - n3;
        if (in(n2, n4, n5)) {
            return LayoutOrientation.ReverseLandscape;
        }
        if (in(n2, n5, 270 + n3)) {
            return LayoutOrientation.ReversePortrait;
        }
        return LayoutOrientation.Landscape;
    }
    
    public int getOrientation() {
        LayoutOrientation layoutOrientation;
        if ((layoutOrientation = this.mLastDetectedOrientation) == LayoutOrientation.Unknown) {
            layoutOrientation = this.getLayoutOrientation();
        }
        switch (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[layoutOrientation.ordinal()]) {
            default: {
                switch (CameraActivity$12.$SwitchMap$com$sonyericsson$cameracommon$utility$LayoutOrientationResolver$LayoutOrientationType[LayoutOrientationResolver.getInstance().getOrientation().ordinal()]) {
                    default: {
                        return 0;
                    }
                    case 2: {
                        return 2;
                    }
                    case 1: {
                        return 1;
                    }
                }
                break;
            }
            case 3: {
                return 1;
            }
            case 2:
            case 4:
            case 5: {
                return 2;
            }
        }
    }
    
    public LayoutDependencyResolver.ScreenAspect getScreenAspect() {
        if (this.mScreenAspect == null) {
            final Display defaultDisplay = ((WindowManager)this.getSystemService("window")).getDefaultDisplay();
            final Point point = new Point();
            defaultDisplay.getRealSize(point);
            final float n = Math.max(point.y, point.x) * 1.0f / Math.min(point.y, point.x);
            if (Math.abs(n - 2.0f) < 0.01f) {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE;
            }
            else if (Math.abs(n - 1.7777778f) < 0.01f) {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE;
            }
            else {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.NOT_DEFINED;
            }
        }
        return this.mScreenAspect;
    }
    
    public int getSensorOrientationDegree() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ORIENTATION:getSensorOrientationDegree = ");
            sb.append(this.mSensorOrientationDegree);
            CamLog.d(sb.toString());
        }
        return this.mSensorOrientationDegree;
    }
    
    public Storage getStorage() {
        return this.mStorage;
    }
    
    @Deprecated
    public StoredSettings getStoredSettings() {
        return this.mStoredSettings;
    }
    
    @Nullable
    public WearableBridgeClient getWearableBridge() {
        if (this.isOneShot()) {
            return null;
        }
        return this.mWearableBridgeClient;
    }
    
    public boolean hasExtraOutputPath() {
        return this.mLaunchCondition.getExtraOutput() != null && this.mStorage.checkValidUri(this.mLaunchCondition.getExtraOutput());
    }
    
    public boolean isAllowToUseLocation() {
        return !this.isOneShot() || PermissionsUtil.areCallerGeoPermissionsGranted(this);
    }
    
    public boolean isAlreadyBcl() {
        return this.mBatteryChangedReceiver.isAlreadyBcl();
    }
    
    public boolean isAlreadyHighTemperature() {
        return this.awaitThermalAlertReceiverReady() && this.mThermalAlertReceiver.isAlreadyHighTemperature();
    }
    
    public boolean isDeviceInSecurityLock() {
        final Bundle extras = this.getIntent().getExtras();
        String string;
        if (extras != null) {
            string = extras.getString("android.intent.extra.SUBJECT");
        }
        else {
            string = null;
        }
        final boolean keyguardLocked = ((KeyguardManager)this.getSystemService("keyguard")).isKeyguardLocked();
        return ("start-secure".equals(string) || this.mLaunchCondition.isSecurePhotoLaunchedByIntent()) && keyguardLocked;
    }
    
    public boolean isGpsLocationAllowed() {
        return this.mLocationSettingsReader.getIsGpsLocationAllowed();
    }
    
    @SuppressLint({ "NewApi" })
    public boolean isInLockTaskMode() {
        return ((ActivityManager)this.getSystemService("activity")).getLockTaskModeState() != 0;
    }
    
    public boolean isKeyguardSecure() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager)this.getSystemService("keyguard");
        }
        return this.mKeyguardManager != null && this.mKeyguardManager.isKeyguardSecure();
    }
    
    public boolean isLazyInitializationRunning() {
        return this.mIsLazyInitializationRunning;
    }
    
    public boolean isNetworkLocationAllowed() {
        return this.mLocationSettingsReader.getIsNetworkLocationAllowed();
    }
    
    public boolean isOneShot() {
        return this.mLaunchCondition.getOneShotMode().isEnabled();
    }
    
    public boolean isOneShotPhoto() {
        return this.mLaunchCondition.getOneShotMode().isPhoto();
    }
    
    public boolean isOneShotVideo() {
        return this.mLaunchCondition.getOneShotMode().isVideo();
    }
    
    public final boolean isStillImageCamera() {
        final Intent intent = this.getIntent();
        return intent != null && "android.media.action.STILL_IMAGE_CAMERA".equals(intent.getAction());
    }
    
    public boolean isThermalWarningExtraState() {
        return this.awaitThermalAlertReceiverReady() && this.mThermalAlertReceiver.isWarningExtraState();
    }
    
    public boolean isThermalWarningReceived() {
        return this.awaitThermalAlertReceiverReady() && this.mThermalAlertReceiver.isThermalWarningReceived();
    }
    
    public boolean isThermalWarningState() {
        return this.awaitThermalAlertReceiverReady() && this.mThermalAlertReceiver.isWarningState();
    }
    
    public void notifyStateBlockedToWearable() {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStateBlockedToWearable()");
        }
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.BLOCKED);
            this.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.BLOCKED);
        }
    }
    
    public void notifyStateIdleToWearable() {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStateIdleToWearable()");
        }
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.IDLE);
            this.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.IDLE);
        }
    }
    
    public void onActivityResult(final int i, final int j, final Intent intent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onActivityResult: requestCode: ");
            sb.append(i);
            sb.append(", resultCode: ");
            sb.append(j);
            CamLog.d(sb.toString());
        }
        super.onActivityResult(i, j, intent);
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        if (this.isDeviceInSecurityLock()) {
            ((ViewFinderImpl)this.mViewFinder).reconstructLocalCache();
        }
        if (this.mActivityResultListeners != null) {
            final OnActivityResultListener onActivityResultListener = (OnActivityResultListener)this.mActivityResultListeners.get(i);
            if (onActivityResultListener != null && onActivityResultListener.onActivityResult(i, j, intent)) {
                this.mActivityResultListeners.remove(i);
            }
            if (this.mActivityResultListeners.size() == 0) {
                this.mActivityResultListeners = null;
            }
        }
        this.mLaunchCondition.onActivityResult(i, intent);
        switch (i) {
            case 20: {
                this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED, new Object[0]);
                if (j == 0) {
                    this.mViewFinder.showMessageDialog(DialogId.SD_CARD_PERMISSION_UNAVAILABLE, new Object[0]);
                    break;
                }
                break;
            }
            case 19: {
                if (j == 2) {
                    this.finish();
                    break;
                }
                break;
            }
            case 18: {
                this.addExternalCameraAppContent(intent);
                ResearchUtil.getInstance().sendPortraitSelfieEvent(this.getApplicationContext(), LocalResearchUtil.getInstance().getModeName(this.mLaunchCondition.getCapturingMode()));
                if (j == 2) {
                    this.finish();
                    break;
                }
                break;
            }
            case 16:
            case 17: {
                this.addExternalCameraAppContent(intent);
                ResearchUtil.getInstance().sendDualCameraEffectEvent(this.getApplicationContext());
                if (j == 2) {
                    this.finish();
                    break;
                }
                break;
            }
        }
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCancel finish()");
        }
        this.finish();
    }
    
    public void onCreate(final Bundle bundle) {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        PerfLog.ACTIVITY_ON_CREATE.begin();
        final boolean awaitPrepare = PlatformCapability.awaitPrepare(2000L, TimeUnit.MILLISECONDS);
        if (!PlatformCapability.isPrepared() || PlatformCapability.hasDeviceError()) {
            if (CamLog.DEBUG) {
                CamLog.d("PlatformCapability is not prepared yet");
            }
            if (!(awaitPrepare ^ true)) {
                if (CamLog.DEBUG) {
                    CamLog.d("Retry preparing PlatformCapability");
                }
                PlatformCapability.prepareAsync((PlatformCapability.OnPlatformCapabilityPreparedCallback)new PlatformCapability.OnPlatformCapabilityPreparedCallback(this) {
                    final CameraActivity this$0;
                    
                    @Override
                    public void onPrepared() {
                        if (CamLog.DEBUG) {
                            CamLog.d("PlatformCapability Prepared");
                        }
                        ((CameraApplication)this.this$0.getApplication()).getUserSettingsLoader().load();
                    }
                });
                PlatformCapability.awaitPrepare(2000L, TimeUnit.MILLISECONDS);
            }
            if (!PlatformCapability.isPrepared()) {
                PlatformCapability.setDeviceError(true);
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
                alertDialog$Builder.setTitle(2131689770).setMessage(2131689773).setCancelable(false).setPositiveButton(2131689975, (DialogInterface$OnClickListener)null).setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener(this) {
                    final CameraActivity this$0;
                    
                    public void onDismiss(final DialogInterface dialogInterface) {
                        this.this$0.finishUrgently();
                    }
                });
                final AlertDialog create = alertDialog$Builder.create();
                create.getWindow().addFlags(128);
                create.show();
                super.onCreate(bundle);
                return;
            }
        }
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_CREATE);
        }
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        if (this.getIntent().getAction() == null) {
            this.getIntent().setAction("android.intent.action.MAIN");
        }
        this.getWindow().requestFeature(12);
        this.mStorage = ((CameraApplication)this.getApplication()).getStorage();
        this.mStoredSettings = SettingsFactory.create(this.getApplicationContext(), this.mStorage);
        this.mBackgroundWorker = ThreadUtil.buildExecutor("AsyncAct", 10);
        this.createLaunchCondition();
        this.mLaunchCondition.setup(this.getIntent(), this.mStoredSettings.getLastSettings().getCapturingMode(), this instanceof InternalCameraActivity);
        this.notifyActivityState("activity-resumed");
        final LaunchCondition.LaunchTrigger launchTrigger = this.getLaunchCondition().getLaunchTrigger();
        if (launchTrigger == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        }
        if (!this.isLaunchedFromLocked(this.getIntent()) && launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        }
        final UserSettings userSettings = this.mStoredSettings.getUserSettings();
        userSettings.prepare(this.getApplicationContext(), this.mLaunchCondition.getOneShotMode(), this.mLaunchCondition.getExtraOutput(), this.mLaunchCondition.getVideoQualityConfigurations(), this.mLaunchCondition.getExtraSettings());
        userSettings.changeCapturingMode(this.mLaunchCondition.getCapturingMode());
        this.mCurrentCameraSessionId = this.getCameraDevice().preloadCamera((Context)this, userSettings, this.mLaunchCondition.getCapturingMode(), this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE && !this.isInLockTaskMode());
        this.mAutoPowerOffTimer = new AutoPowerOffTimer(this, (AutoPowerOffTimer.AutoPowerOffListener)new AutoPowerOffListenerImpl());
        this.setupCoreInstance();
        super.onCreate((Bundle)null);
        this.mLocationSettingsReader = new LocationSettingsReader();
        ResearchUtil.getInstance().onCreate((Context)this);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_INITIALIZE, new Object[0]);
        if (this.mStorage.isStorageReadable()) {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageActivated was already finished.");
            }
            this.mSetupAllTaskFuture = this.mBackgroundWorker.submit(new SetupAllTask());
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageActivated was not finished yet.");
            }
            this.mStorage.addStorageReadyStateListener((Storage.StorageReadyStateListener)new Storage.StorageReadyStateListener(this) {
                final CameraActivity this$0;
                
                @Override
                public void onStorageReadyStateChanged(final StorageType storageType, final StorageReadyState storageReadyState) {
                    if (this.this$0.mStorage.isStorageReadable()) {
                        if (CamLog.DEBUG) {
                            CamLog.d("onStorageReadyStateChanged: Storage can be readable, Got to setupAll");
                        }
                        CameraApplication.getUiThreadHandler().post((Runnable)new Runnable(this, this) {
                            final CameraActivity$8 this$1;
                            final StorageReadyStateListener val$listener;
                            
                            @Override
                            public void run() {
                                this.this$1.this$0.mSetupAllTaskFuture = this.this$1.this$0.mBackgroundWorker.submit(new SetupAllTask());
                                this.this$1.this$0.mStorage.removeStorageReadyStateListener(this.val$listener);
                            }
                        });
                    }
                }
            });
        }
        this.createBatteryChangedReceiver();
        LocalResearchUtil.getInstance().setBatteryChangedReceiver(this.mBatteryChangedReceiver);
        this.mBackgroundWorker.execute(new ThermalAlertReceiverCreateTask());
        this.mBackgroundWorker.execute(new ThermalAlertReceiverOnCreateTask());
        this.registerForceExitRequestReceiver();
        if (PlatformCapability.isWearableSupported()) {
            this.setupWearableFramework();
        }
        this.setupScreenOffReceiver();
        this.mIsColdBoot = true;
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_CREATE);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
        PerfLog.ACTIVITY_ON_CREATE.end();
    }
    
    public void onDestroy() {
        PerfLog.ACTIVITY_ON_DESTROY.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : E");
        }
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_DESTROY);
        }
        super.onDestroy();
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        this.mLayoutOrientationChangedListenerSet.clear();
        ResearchUtil.getInstance().onDestroy();
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy():[IN]");
        }
        this.unregisterForceExitRequestReceiver();
        this.getDownAll();
        this.mIsCalledOnDestroy = false;
        this.mBackgroundWorker.execute(new ThermalAlertReceiverOnDestroyTask());
        this.mBackgroundWorker.shutdown();
        Label_0235: {
            try {
                if (this.mBackgroundWorker.awaitTermination(1000L, TimeUnit.MILLISECONDS)) {
                    break Label_0235;
                }
                synchronized (this.mThermalAlertReceiver) {
                    if (!this.mIsCalledOnDestroy) {
                        this.mIsCalledOnDestroy = true;
                        this.mThermalAlertReceiver.onDestroy();
                        this.mBatteryChangedReceiver.onDestroy();
                    }
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("mBackgroundWorker.shutdown is Timeout.");
                synchronized (this.mThermalAlertReceiver) {
                    if (!this.mIsCalledOnDestroy) {
                        this.mIsCalledOnDestroy = true;
                        this.mThermalAlertReceiver.onDestroy();
                        this.mBatteryChangedReceiver.onDestroy();
                    }
                    monitorexit(this.mThermalAlertReceiver);
                    if (PlatformCapability.isWearableSupported()) {
                        this.releaseWearableFramework();
                    }
                    if (CamLog.VERBOSE) {
                        this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_DESTROY);
                    }
                    if (CamLog.DEBUG) {
                        CamLog.d("onDestroy() : X");
                    }
                    PerfLog.ACTIVITY_ON_DESTROY.end();
                }
            }
        }
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        return (motionEvent.isFromSource(536870912) && this.mSideTouchEventDispatcher.send(motionEvent)) || super.onGenericMotionEvent(motionEvent);
    }
    
    public boolean onKeyDown(final int i, final KeyEvent keyEvent) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyEvent.getEventTime() = ");
            sb.append(keyEvent.getEventTime());
            CamLog.d(sb.toString());
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("CameraActivity.onKeyDown() : KEYCODE=");
            sb2.append(i);
            CamLog.d(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyDown():[IN]");
        }
        return this.isFinishing() || this.mKeyEventDispatcher.sendKeyDown(keyEvent) || super.onKeyDown(i, keyEvent);
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return this.mKeyEventDispatcher.sendKeyLongPress(keyEvent) || super.onKeyLongPress(n, keyEvent);
    }
    
    public boolean onKeyUp(final int i, final KeyEvent keyEvent) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyEvent.getEventTime() = ");
            sb.append(keyEvent.getEventTime());
            CamLog.d(sb.toString());
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("CameraActivity.onKeyUp() : KEYCODE=");
            sb2.append(i);
            CamLog.d(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyUp():[IN]");
        }
        if (this.isFinishing()) {
            return true;
        }
        if (this.mKeyEventDispatcher.sendKeyUp(keyEvent)) {
            return true;
        }
        if (i == 4) {
            this.abort();
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
        super.onMultiWindowModeChanged(b);
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onMultiWindowModeChanged() : ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (b && PermissionsUtil.arePermissionsGranted((Context)this, this.REQUESTED_PERMISSIONS) && !this.mDisableMultiWindow) {
            this.launchMultiWindow();
        }
    }
    
    protected void onNewIntent(final Intent intent) {
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        final CapturingMode capturingMode = this.mLaunchCondition.getCapturingMode();
        this.mLaunchCondition.setup(intent, this.mStoredSettings.getLastSettings().getCapturingMode(), this instanceof InternalCameraActivity);
        this.notifyActivityState("activity-resumed");
        final LaunchCondition.LaunchTrigger launchTrigger = this.getLaunchCondition().getLaunchTrigger();
        if (launchTrigger == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        }
        if (!this.isLaunchedFromLocked(intent) && launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        }
        if (this.mIsColdBoot && capturingMode != this.mLaunchCondition.getCapturingMode()) {
            this.mIsNeedToCloseBypassCameraBecauseModeChanged = true;
        }
        this.mCanFinishByScreenOff = false;
        this.setIntent(intent);
    }
    
    public final void onPause() {
        if (this.isRecording()) {
            this.mCameraDeviceHandler.stopAudioRecording();
        }
        if (!PlatformCapability.isPrepared()) {
            super.onPause();
            this.getCameraDevice().closeCamera();
            this.finishUrgently();
            return;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onPause(): isKeyguardLocked() = ");
            sb.append(this.isKeyguardLocked());
            CamLog.d(sb.toString());
        }
        if (this.mMainHandler != null) {
            this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        }
        CameraApplication.getUiThreadHandler().removeCallbacksAndMessages(this.getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken());
        if (!this.mSkippedOnResume) {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() --> onPauseTasks()");
            }
            this.onPauseTasks();
        }
        else {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() --> postAtTime(SendPauseEventAndReleaseCameraTask,500)");
            }
            CameraApplication.getUiThreadHandler().postAtTime(this.mSendPauseEventAndReleaseCameraTask, this.getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken(), SystemClock.uptimeMillis() + 500L);
        }
        this.unRegisterShutDownReceiver();
        super.onPause();
    }
    
    public void onPauseTasks() {
        PerfLog.ACTIVITY_ON_PAUSE.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : E");
        }
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_PAUSE);
        }
        this.notifyActivityState("activity-paused");
        final LaunchCondition.LaunchTrigger launchTrigger = this.getLaunchCondition().getLaunchTrigger();
        if (launchTrigger != LaunchCondition.LaunchTrigger.POWER_KEY_DOUBLE_TAP && launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        }
        if (launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        }
        LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        this.getCameraDevice().setActivityForeground(false);
        this.awaitSetupAllReady();
        this.sendBroadcast(new Intent("com.sonyericsson.android.camera.intent.action.FINISH"));
        if (PermissionsUtil.arePermissionsGranted(this.getApplicationContext(), this.REQUESTED_PERMISSIONS)) {
            this.doPause();
        }
        else {
            this.releaseCamera();
            this.mCurrentCameraSessionId = null;
            this.mViewFinder.clearMessageDialog();
        }
        this.mKeyEventDispatcher.stop();
        this.mSideTouchEventDispatcher.stop();
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_PAUSE);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : X");
        }
        PerfLog.ACTIVITY_ON_PAUSE.end();
    }
    
    public void onRestart() {
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_RESTART);
        }
        super.onRestart();
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        this.mLaunchCondition.onRestart(this.isKeyguardLocked(), this.shouldShowWhenLocked());
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_RESTART);
        }
    }
    
    public final void onResume() {
        this.mIsColdBoot = false;
        if (!PlatformCapability.isPrepared()) {
            super.onResume();
            return;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onResume(): isKeyguardLocked() = ");
            sb.append(this.isKeyguardLocked());
            CamLog.d(sb.toString());
        }
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(this.getMainLooper());
        }
        this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        CameraApplication.getUiThreadHandler().removeCallbacksAndMessages(this.getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken());
        this.getCameraDevice().setIsInShutdownNow(false);
        this.registerShutDownReceiver();
        if (!this.isKeyguardLocked() && !this.mSkippedOnResume) {
            if (CamLog.VERBOSE) {
                CamLog.d("onResume() --> onResumeTasks()");
            }
            this.mSkippedOnResume = false;
            this.onResumeTasks();
        }
        else {
            this.mSkippedOnResume = true;
            long lng;
            if (this.isKeyguardSecure()) {
                lng = 30L;
            }
            else {
                lng = 15L;
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("onResume() --> postDelayed(mOnResumeTasks,");
                sb2.append(lng);
                sb2.append(")");
                CamLog.d(sb2.toString());
            }
            this.mMainHandler.postDelayed(this.mOnResumeTasks, lng);
        }
        super.onResume();
    }
    
    public boolean onSearchRequested() {
        return false;
    }
    
    public void onStart() {
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_START);
        }
        super.onStart();
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_START);
        }
    }
    
    public void onStop() {
        if (CamLog.VERBOSE) {
            this.logLifeCycleIn("CameraActivity", LifeCycleIds.ON_STOP);
        }
        PerfLog.ACTIVITY_ON_STOP.begin();
        super.onStop();
        if (PlatformCapability.hasDeviceError()) {
            this.finishAndRemoveTask();
            Process.killProcess(Process.myPid());
            return;
        }
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        if (this.mResetSettingsRequested) {
            final Intent launchIntentForPackage = this.getBaseContext().getPackageManager().getLaunchIntentForPackage(this.getBaseContext().getPackageName());
            launchIntentForPackage.addFlags(268468224);
            this.startActivity(launchIntentForPackage, (Bundle)null);
            this.mResetSettingsRequested = false;
        }
        PerfLog.ACTIVITY_ON_STOP.end();
        if (CamLog.VERBOSE) {
            this.logLifeCycleOut("CameraActivity", LifeCycleIds.ON_STOP);
        }
    }
    
    public void onUserInteraction() {
        super.onUserInteraction();
        if (this.mAutoPowerOffTimer != null && !AutoPowerOffType.LIFT_TRIGGER.getReason().equals(this.mAutoPowerOffTimer.getUserdata())) {
            this.restartAutoPowerOffTimer();
        }
    }
    
    protected void onUserLeaveHint() {
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (!PlatformCapability.isPrepared()) {
            return;
        }
        if (b && this.mViewFinder != null) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY, new Object[0]);
        }
    }
    
    public void pauseAudioPlayback() {
        final Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.sendBroadcast(intent);
        this.muteSound();
    }
    
    public void playSound(@NonNull final SoundPlayer.Type type) {
        if (this.mSoundPlayer == null) {
            return;
        }
        this.mSoundPlayer.play(type);
    }
    
    public void postDelayedEvent(final Runnable runnable, final long n) {
        CameraApplication.getUiThreadHandler().postDelayed(runnable, n);
    }
    
    public CameraDeviceHandler.CameraSessionId prepareCameraDeviceHandler(final FastCapture fastCapture, final CapturingMode capturingMode, final UserSettings userSettings) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("prepareCameraDeviceHandler() launch:");
            String name;
            if (fastCapture == null) {
                name = "null";
            }
            else {
                name = fastCapture.name();
            }
            sb.append(name);
            sb.append(" capturing-mode:");
            sb.append(capturingMode.name());
            CamLog.d(sb.toString());
        }
        if (this.mIsNeedToCloseBypassCameraBecauseModeChanged) {
            this.mIsNeedToCloseBypassCameraBecauseModeChanged = false;
            this.getCameraDevice().closeCamera();
        }
        if (this.getCameraDevice().prepareCamera(fastCapture, capturingMode, userSettings)) {
            userSettings.applyCapturingMode();
            return this.getCameraDevice().openCamera(fastCapture, capturingMode, userSettings);
        }
        return null;
    }
    
    public void readLocationSettings() {
        this.mLocationSettingsReader.readLocationSettings((Context)this);
    }
    
    public void removeOrienationListener(final LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.remove(layoutOrientationChangedListener);
    }
    
    public void reportFullyDrawnOnce() {
        if (!CameraActivity.sIsReportFullyDrawnAlreadyReported) {
            CameraActivity.sIsReportFullyDrawnAlreadyReported = true;
            CamLog.d("Report fully drawn");
            this.reportFullyDrawn();
        }
    }
    
    public void requestLaunchAdvancedCamera(final LaunchCondition.ExtraOperation extraOperation, final String s) {
        CameraApplication.getUiThreadHandler().post((Runnable)new RequestLaunchAdvancedCameraTask(this.mStateMachine.getCurrentCapturingMode(), extraOperation, s));
    }
    
    public void requestPostLazyInitializationTaskExecute() {
        this.mPostDeviceInitializationTask = new PostDeviceInitializationTask(this);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, false);
        if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            this.postDelayedEvent(this.mPostDeviceInitializationTask, 100L);
        }
        else {
            this.postEvent(this.mPostDeviceInitializationTask);
        }
    }
    
    public void requestRestartCameraActivityAfterResetSettings() {
        this.mResetSettingsRequested = true;
        this.finish();
    }
    
    public void requestSuspend() {
        if (CamLog.VERBOSE) {
            CamLog.d("requestSuspend():[IN]");
        }
        if (!this.moveTaskToBack(true)) {
            if (CamLog.VERBOSE) {
                CamLog.d("requestSuspend():[FAILED]");
            }
            this.finish();
        }
    }
    
    public final void restartAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("restartAutoPowerOffTimer: ");
        }
        if (this.mAutoPowerOffTimer == null) {
            return;
        }
        this.postEvent(new Runnable(this) {
            final CameraActivity this$0;
            
            @Override
            public void run() {
                if (this.this$0.mViewFinder != null) {
                    this.this$0.mViewFinder.hideAutoPowerOffHintText();
                }
            }
        });
        this.mAutoPowerOffTimer.restartAutoPowerOffTimer();
    }
    
    public void setupAutoPowerOffTimeOutDuration(final boolean b) {
        AutoPowerOffType autoPowerOffType;
        if (b) {
            if (!this.mStoredSettings.getMessageSettings().isNeverShow(MessageType.PREDICTIVE_LAUNCH_DESCRIPTION)) {
                autoPowerOffType = AutoPowerOffType.LIFT_TRIGGER_DIALOG;
            }
            else {
                autoPowerOffType = AutoPowerOffType.LIFT_TRIGGER;
            }
        }
        else if (this.shouldShowWhenLocked()) {
            autoPowerOffType = AutoPowerOffType.ON_LOCKSCREEN;
        }
        else {
            autoPowerOffType = AutoPowerOffType.DEFAULT;
        }
        this.mAutoPowerOffTimer.setTimeOutDuration(autoPowerOffType.getDuration(), this.mViewFinder.getAutoPowerOffHintTextTimeOutDuration(), autoPowerOffType.getReason());
    }
    
    public boolean shouldAddToMediaStore() {
        return this.mLaunchCondition.getAddToMediaStore();
    }
    
    protected boolean shouldShowWhenLocked() {
        return false;
    }
    
    public void startActivityForResult(final Intent intent, final int n, final Bundle bundle) {
        if (this.isDeviceInSecurityLock()) {
            ((ViewFinderImpl)this.mViewFinder).saveLocalCache();
        }
        super.startActivityForResult(intent, n, bundle);
    }
    
    public boolean startActivityForResult(final Intent intent, final int n, final OnActivityResultListener onActivityResultListener) {
        if (!this.addActivityResultListener(n, onActivityResultListener)) {
            return false;
        }
        this.startActivityForResult(intent, n);
        return true;
    }
    
    public void stopPlayingSound() {
        if (this.mSoundPlayer != null) {
            this.mSoundPlayer.stop();
        }
    }
    
    public void terminateApplication() {
        this.finish();
    }
    
    private class AutoPowerOffListenerImpl implements AutoPowerOffListener
    {
        final CameraActivity this$0;
        
        private AutoPowerOffListenerImpl(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onAutoPowerOff(final Object o) {
            if (this.this$0.isInLockTaskMode()) {
                this.this$0.mViewFinder.hideAutoPowerOffHintText();
            }
            final boolean equals = o.equals(AutoPowerOffType.LIFT_TRIGGER.getReason());
            final Event.AutoPowerOffAction autoPowerOffAction = null;
            if (equals || o.equals(AutoPowerOffType.LIFT_TRIGGER_DIALOG.getReason())) {
                this.this$0.mBackgroundWorker.execute(new ScreenOffTask());
            }
            final AutoPowerOffType[] values = AutoPowerOffType.values();
            final int length = values.length;
            int n = 0;
            Event.AutoPowerOffAction action;
            while (true) {
                action = autoPowerOffAction;
                if (n >= length) {
                    break;
                }
                final AutoPowerOffType autoPowerOffType = values[n];
                if (o.equals(autoPowerOffType.getReason())) {
                    action = autoPowerOffType.getAction();
                    break;
                }
                ++n;
            }
            if (action != null) {
                ResearchUtil.getInstance().sendAutoPowerOffEvent(action);
            }
            else {
                CamLog.w("AutoPowerOffAction is not found.");
            }
            this.this$0.abort();
        }
        
        @Override
        public void onAutoPowerOffWarning() {
            if (this.this$0.mViewFinder != null && !this.this$0.isInLockTaskMode()) {
                this.this$0.mViewFinder.showAutoPowerOffHintText();
            }
        }
    }
    
    private enum AutoPowerOffType
    {
        private static final AutoPowerOffType[] $VALUES;
        
        DEFAULT(180000, "DEFAULT", Event.AutoPowerOffAction.DEFAULT), 
        LIFT_TRIGGER(5000, "LIFT_TRIGGER", Event.AutoPowerOffAction.LIFT_TRIGGER), 
        LIFT_TRIGGER_DIALOG(15000, "LIFT_TRIGGER_DIALOG", Event.AutoPowerOffAction.LIFT_TRIGGER_DIALOG), 
        ON_LOCKSCREEN(30000, "ON_LOCKSCREEN", Event.AutoPowerOffAction.ON_LOCKSCREEN);
        
        private final Event.AutoPowerOffAction mAction;
        private final int mDuration;
        private final String mReason;
        
        static {
            $VALUES = new AutoPowerOffType[] { AutoPowerOffType.DEFAULT, AutoPowerOffType.ON_LOCKSCREEN, AutoPowerOffType.LIFT_TRIGGER, AutoPowerOffType.LIFT_TRIGGER_DIALOG };
        }
        
        private AutoPowerOffType(final int mDuration, final String mReason, final Event.AutoPowerOffAction mAction) {
            this.mDuration = mDuration;
            this.mReason = mReason;
            this.mAction = mAction;
        }
        
        public Event.AutoPowerOffAction getAction() {
            return this.mAction;
        }
        
        public int getDuration() {
            return this.mDuration;
        }
        
        public String getReason() {
            return this.mReason;
        }
    }
    
    private static class CameraActivityFinishBroadcastReceiver extends BroadcastReceiver
    {
        final WeakReference<CameraActivity> mCameraActivityRef;
        
        CameraActivityFinishBroadcastReceiver(final CameraActivity referent) {
            this.mCameraActivityRef = new WeakReference<CameraActivity>(referent);
        }
        
        public void onReceive(final Context context, final Intent intent) {
            final CameraActivity cameraActivity = this.mCameraActivityRef.get();
            if (cameraActivity != null && !cameraActivity.isFinishing()) {
                cameraActivity.abort();
            }
        }
    }
    
    private class CreateContentInfoTask implements Runnable
    {
        ArrayList<Uri> mCapturedUris;
        final CameraActivity this$0;
        
        CreateContentInfoTask(final CameraActivity this$0, final ArrayList<Uri> mCapturedUris) {
            this.this$0 = this$0;
            this.mCapturedUris = new ArrayList<Uri>();
            this.mCapturedUris = mCapturedUris;
        }
        
        @Override
        public void run() {
            ((ViewFinderImpl)this.this$0.mViewFinder).requestCreateContentInfoSync(this.mCapturedUris);
        }
    }
    
    private class ExtendedOrientationEventListener extends OrientationEventListener
    {
        final CameraActivity this$0;
        
        public ExtendedOrientationEventListener(final CameraActivity this$0, final Context context) {
            this.this$0 = this$0;
            super(context);
        }
        
        public void onOrientationChanged(final int n) {
            if (n != -1) {
                this.this$0.mSensorOrientationDegree = n;
            }
            if (n == this.this$0.mLastOrientationDegree) {
                return;
            }
            this.this$0.mLastOrientationDegree = n;
            if (this.this$0.mLastOrientationDegree != -1) {
                this.this$0.mLastDeterminedOrientationDegree = this.this$0.mLastOrientationDegree;
            }
            this.this$0.notifyLayoutOrientationChanged(this.this$0.getLayoutOrientation());
        }
    }
    
    private class ForceExitRequestReceiver extends BroadcastReceiver
    {
        final CameraActivity this$0;
        
        private ForceExitRequestReceiver(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (CamLog.VERBOSE) {
                CamLog.d("ForceExitRequestReceiver.onReceive()");
            }
            if (intent == null) {
                return;
            }
            if (this.this$0.getStoredSettings().getMessageSettings().isNeverShow(MessageType.SETUP_WIZARD) && "com.sonymobile.cameracommon.intent.ACTION_FORCE_EXIT_REQUEST".equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("ForceExitRequestReceiver() Force Exit");
                }
                this.this$0.finish();
            }
        }
    }
    
    public enum LayoutOrientation
    {
        private static final LayoutOrientation[] $VALUES;
        
        Landscape, 
        Portrait, 
        ReverseLandscape, 
        ReversePortrait, 
        Unknown;
        
        static {
            $VALUES = new LayoutOrientation[] { LayoutOrientation.Unknown, LayoutOrientation.Portrait, LayoutOrientation.Landscape, LayoutOrientation.ReversePortrait, LayoutOrientation.ReverseLandscape };
        }
    }
    
    public interface LayoutOrientationChangedListener
    {
        void onLayoutOrientationChanged(final LayoutOrientation p0);
    }
    
    class LazyInitializationTask implements Runnable
    {
        final CameraActivity this$0;
        
        LazyInitializationTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        private void retry() {
            this.this$0.postDelayedEvent(this, 200L);
        }
        
        @Override
        public void run() {
            if (this.this$0.mStateMachine == null || this.this$0.mCameraDeviceHandler == null || this.this$0.mViewFinder == null || !this.this$0.mViewFinder.isHeadUpDisplayReady()) {
                this.retry();
                return;
            }
            if (!this.this$0.mStateMachine.canHandleAsynchronizedTask()) {
                this.retry();
                return;
            }
            this.this$0.requestFinishCameraActivity();
            this.this$0.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, new Object[0]);
            this.this$0.mIsLazyInitializationRunning = false;
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, this.this$0.mStateMachine.isSettingChangeAcceptable());
        }
    }
    
    public enum LifeCycleIds
    {
        private static final LifeCycleIds[] $VALUES;
        
        ON_CREATE(MeasurePerformance.PerformanceIds.ON_CREATE, "onCreate()"), 
        ON_DESTROY(MeasurePerformance.PerformanceIds.ON_DESTROY, "onDestroy()"), 
        ON_PAUSE(MeasurePerformance.PerformanceIds.ON_PAUSE, "onPause()"), 
        ON_RESTART(MeasurePerformance.PerformanceIds.ON_RESTART, "onRestart()"), 
        ON_RESUME(MeasurePerformance.PerformanceIds.ON_RESUME, "onResume()"), 
        ON_START(MeasurePerformance.PerformanceIds.ON_START, "onStart()"), 
        ON_STOP(MeasurePerformance.PerformanceIds.ON_STOP, "onStop()");
        
        private final String mLog;
        private final MeasurePerformance.PerformanceIds mPerformanceIds;
        
        static {
            $VALUES = new LifeCycleIds[] { LifeCycleIds.ON_CREATE, LifeCycleIds.ON_START, LifeCycleIds.ON_RESTART, LifeCycleIds.ON_RESUME, LifeCycleIds.ON_PAUSE, LifeCycleIds.ON_STOP, LifeCycleIds.ON_DESTROY };
        }
        
        private LifeCycleIds(final MeasurePerformance.PerformanceIds mPerformanceIds, final String mLog) {
            this.mPerformanceIds = mPerformanceIds;
            this.mLog = mLog;
        }
        
        @Override
        public String toString() {
            return this.mLog;
        }
    }
    
    private class OnPreviewStartedListenerImpl implements OnPreviewStartedListener
    {
        private final CameraSessionId mCameraSessionId;
        final CameraActivity this$0;
        
        public OnPreviewStartedListenerImpl(final CameraActivity this$0, final CameraSessionId mCameraSessionId) {
            this.this$0 = this$0;
            this.mCameraSessionId = mCameraSessionId;
        }
        
        @Override
        public void onPreviewStarted(final CameraSessionId cameraSessionId) {
            if (this.mCameraSessionId == cameraSessionId) {
                this.this$0.reportFullyDrawnOnce();
            }
        }
    }
    
    public interface PermissionCheckCallback
    {
        boolean onPermissionChecked(final String[] p0);
    }
    
    private class PostDeviceInitializationTask implements Runnable
    {
        private final CameraActivity mActivity;
        final CameraActivity this$0;
        
        PostDeviceInitializationTask(final CameraActivity this$0, final CameraActivity mActivity) {
            this.this$0 = this$0;
            this.mActivity = mActivity;
            this$0.mIsLazyInitializationRunning = true;
        }
        
        private void retry() {
            this.this$0.postDelayedEvent(this, 100L);
        }
        
        @Override
        public void run() {
            if (this.this$0.mStateMachine == null || this.this$0.mCameraDeviceHandler == null || this.this$0.mViewFinder == null) {
                this.retry();
                return;
            }
            if (!this.this$0.mCameraDeviceHandler.isPreScanOnGoing() && !this.this$0.mCameraDeviceHandler.isPreCaptureOnGoing()) {
                if (this.this$0.mStateMachine.canHandleAsynchronizedTask()) {
                    final UserSettings userSetting = this.this$0.mStateMachine.getUserSetting();
                    if (((CapturingMode)userSetting.get(UserSettingKey.CAPTURING_MODE)).isVideo()) {
                        this.this$0.mCameraDeviceHandler.setTorchAndCommit(((PhotoLight)userSetting.get(UserSettingKey.PHOTO_LIGHT)).getBooleanValue());
                    }
                    else if (((CapturingMode)userSetting.get(UserSettingKey.CAPTURING_MODE)).isFront()) {
                        this.this$0.mCameraDeviceHandler.setDisplayFlashModeAndCommit((DisplayFlash)userSetting.get(UserSettingKey.DISPLAY_FLASH));
                    }
                    else {
                        this.this$0.mCameraDeviceHandler.setFlashModeAndCommit((Flash)userSetting.get(UserSettingKey.FLASH));
                    }
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, true);
                    if (this.this$0.mGeotagManager != null) {
                        this.this$0.mGeotagManager.notifyStatus();
                    }
                    this.this$0.mLazyInitializationiTask = this.this$0.new LazyInitializationTask();
                    this.this$0.postEvent(this.this$0.mLazyInitializationiTask);
                }
                else {
                    this.retry();
                }
                return;
            }
            this.retry();
        }
    }
    
    class RequestLaunchAdvancedCameraTask implements Runnable
    {
        private static final String TAG = "RequestLaunchAdvancedCameraTask";
        private final LaunchCondition.ExtraOperation mExtraOperation;
        private final CapturingMode mRequestMode;
        private final String mUserSettingKeyName;
        final CameraActivity this$0;
        
        private RequestLaunchAdvancedCameraTask(final CameraActivity this$0, final CapturingMode mRequestMode, final LaunchCondition.ExtraOperation mExtraOperation, final String mUserSettingKeyName) {
            this.this$0 = this$0;
            this.mRequestMode = mRequestMode;
            this.mExtraOperation = mExtraOperation;
            this.mUserSettingKeyName = mUserSettingKeyName;
        }
        
        @Override
        public void run() {
            this.this$0.abort(false);
            final Intent intent = new Intent();
            intent.setClass(this.this$0.getApplicationContext(), (Class)CameraActivity.class);
            intent.addFlags(268435456);
            intent.putExtra("com.sonyericsson.android.camera.extra.launchedByFastCapturing", true);
            if (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[this.mExtraOperation.ordinal()] == 1) {
                intent.putExtra("com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogopen", true);
                intent.putExtra("com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogkey", this.mUserSettingKeyName);
            }
            switch (CameraActivity$12.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.mRequestMode.ordinal()]) {
                default: {
                    intent.setAction(CapturingMode.SCENE_RECOGNITION.getValue());
                    break;
                }
                case 7: {
                    intent.setAction(CapturingMode.SLOW_MOTION.getValue());
                    break;
                }
                case 6: {
                    intent.setAction("com.sonyericsson.android.camera.action.FRONT_VIDEO_CAMERA");
                    break;
                }
                case 5: {
                    intent.setAction("android.media.action.VIDEO_CAMERA");
                    break;
                }
                case 4: {
                    intent.setAction("com.sonyericsson.android.camera.action.FRONT_STILL_IMAGE_CAMERA");
                    break;
                }
                case 3: {
                    intent.setAction(CapturingMode.FRONT_PHOTO.getValue());
                    break;
                }
                case 2: {
                    intent.setAction(CapturingMode.NORMAL.getValue());
                    break;
                }
                case 1: {
                    intent.setAction(CapturingMode.SCENE_RECOGNITION.getValue());
                    break;
                }
            }
            if (CommonUtility.isActivityAvailable(this.this$0.getApplicationContext(), intent)) {
                try {
                    this.this$0.startActivity(intent, ActivityOptions.makeCustomAnimation((Context)this.this$0, 0, 0).toBundle());
                    this.this$0.overridePendingTransition(0, 0);
                }
                catch (final ActivityNotFoundException ex) {
                    if (CamLog.VERBOSE) {
                        CamLog.e(".onClick():[activity is not found error]");
                    }
                }
            }
            this.this$0.requestSuspend();
        }
    }
    
    private enum RequestTypeForSomcCameraService
    {
        private static final RequestTypeForSomcCameraService[] $VALUES;
        
        GYRO_CALIBRATION(0), 
        SCREEN_OFF(1);
        
        public final int mExtraValue;
        
        static {
            $VALUES = new RequestTypeForSomcCameraService[] { RequestTypeForSomcCameraService.GYRO_CALIBRATION, RequestTypeForSomcCameraService.SCREEN_OFF };
        }
        
        private RequestTypeForSomcCameraService(final int mExtraValue) {
            this.mExtraValue = mExtraValue;
        }
    }
    
    private class ScreenOffReceiver extends BroadcastReceiver
    {
        private static final String TAG = "ScreenOffReceiver";
        final CameraActivity this$0;
        
        private ScreenOffReceiver(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onReceive():[IN]");
            }
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onReceive():[Receive SCREEN_OFF]");
                }
                if (this.this$0.isFinishing() || this.this$0.isDestroyed()) {
                    return;
                }
                if (this.this$0.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.POWER_KEY_DOUBLE_TAP && !this.this$0.canFinishByScreenOff()) {
                    this.this$0.allowFinishByScreenOff();
                    return;
                }
                this.this$0.requestSuspend();
            }
        }
    }
    
    private final class ScreenOffTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ScreenOffTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            final Intent intent = new Intent("com.sonymobile.cameracommon.action.REQUEST_SOMC_CAMERA_SERVICE");
            intent.setPackage("com.sonymobile.cameracommon");
            intent.putExtra("android.intent.extra.SUBJECT", RequestTypeForSomcCameraService.SCREEN_OFF.mExtraValue);
            this.this$0.getApplicationContext().startService(intent);
        }
    }
    
    private class SetupAllTask implements Runnable
    {
        final CameraActivity this$0;
        
        private SetupAllTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.setupAll();
        }
    }
    
    private class ShutDownReceiver extends BroadcastReceiver
    {
        final CameraActivity this$0;
        
        private ShutDownReceiver(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                this.this$0.getCameraDevice().setIsInShutdownNow(true);
            }
        }
    }
    
    private final class StartGyroCalibrationOnPauseTask implements Runnable
    {
        final CameraActivity this$0;
        
        private StartGyroCalibrationOnPauseTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            final Intent intent = new Intent("com.sonymobile.cameracommon.action.REQUEST_SOMC_CAMERA_SERVICE");
            intent.setPackage("com.sonymobile.cameracommon");
            intent.putExtra("android.intent.extra.SUBJECT", RequestTypeForSomcCameraService.GYRO_CALIBRATION.mExtraValue);
            this.this$0.getApplicationContext().startService(intent);
        }
    }
    
    private final class ThermalAlertReceiverCreateTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ThermalAlertReceiverCreateTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mThermalAlertReceiver = new ThermalAlertReceiver(this.this$0, this.this$0.mThermalAlertListener);
        }
    }
    
    private final class ThermalAlertReceiverOnCreateTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ThermalAlertReceiverOnCreateTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mThermalAlertReceiver.onCreate();
            this.this$0.mBatteryChangedReceiver.onCreate();
        }
    }
    
    private final class ThermalAlertReceiverOnDestroyTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ThermalAlertReceiverOnDestroyTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0.mThermalAlertReceiver) {
                if (!this.this$0.mIsCalledOnDestroy) {
                    this.this$0.mIsCalledOnDestroy = true;
                    this.this$0.mThermalAlertReceiver.onDestroy();
                    this.this$0.mBatteryChangedReceiver.onDestroy();
                }
            }
        }
    }
    
    private final class ThermalAlertReceiverOnPauseTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ThermalAlertReceiverOnPauseTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mThermalAlertReceiver.onPause();
            LocalResearchUtil.getInstance().setMeasurementThermal(false);
            this.this$0.mBatteryChangedReceiver.onPause();
        }
    }
    
    private final class ThermalAlertReceiverOnResumeTask implements Runnable
    {
        final CameraActivity this$0;
        
        private ThermalAlertReceiverOnResumeTask(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mThermalAlertReceiver.onResume();
            this.this$0.mBatteryChangedReceiver.onResume();
        }
    }
    
    private static class ViewFinderInitializationTask implements Runnable
    {
        private final ViewFinderImpl mViewFinderImpl;
        
        private ViewFinderInitializationTask(final ViewFinderImpl mViewFinderImpl) {
            this.mViewFinderImpl = mViewFinderImpl;
        }
        
        @Override
        public void run() {
            PerfLog.TASK_VIEW_FINDER_INITIALIZATION.begin();
            this.mViewFinderImpl.initialize();
            this.mViewFinderImpl.setContentView();
            PerfLog.TASK_VIEW_FINDER_INITIALIZATION.end();
        }
    }
    
    private class WearableBridgeLifeCycleObserver implements LifeCycleObserver
    {
        final CameraActivity this$0;
        
        private WearableBridgeLifeCycleObserver(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPause() {
        }
        
        @Override
        public void onResume() {
            if (this.this$0.mStateMachine.canHandleWearableCaptureRequest()) {
                this.this$0.notifyStateIdleToWearable();
            }
            else {
                this.this$0.notifyStateBlockedToWearable();
            }
        }
    }
    
    private class WearableBridgePhotoEventObserver implements PhotoEventObserver
    {
        final CameraActivity this$0;
        
        private WearableBridgePhotoEventObserver(final CameraActivity this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPhotoCaptureRequested() {
            if (this.this$0.mStateMachine.canHandleWearableCaptureRequest()) {
                this.this$0.restartAutoPowerOffTimer();
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_READY, new Object[0]);
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE, new Object[0]);
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.WEARABLE);
            }
            else {
                final WearableBridgeClient wearableBridge = this.this$0.getWearableBridge();
                if (wearableBridge != null) {
                    wearableBridge.getPhotoStateNotifier().onCaptureFailed();
                }
            }
        }
    }
    
    private static class WearableBridgeVideoEventObserver implements VideoEventObserver
    {
        @Override
        public void onStartVideoRecRequested() {
        }
        
        @Override
        public void onStopVideoRecRequested() {
        }
    }
}
