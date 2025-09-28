package com.sonyericsson.android.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import com.sonyericsson.android.camera.AutoPowerOffTimer;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.setting.SettingsFactory;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeUtil;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.activity.OnActivityResultListener;
import com.sonyericsson.cameracommon.activity.TerminateListener;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.intent.IntentConstants;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.OneShotUtility;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import com.sonymobile.sidetouchgesturedetector.SideTouchUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraActivity extends Activity implements DialogInterface.OnCancelListener, TerminateListener {
    private static final String ACTION_REQUEST_SOMC_CAMERA_SERVICE = "com.sonymobile.cameracommon.action.REQUEST_SOMC_CAMERA_SERVICE";
    private static final int ASYNC_ACT_TIMEOUT_MILLIS = 3000;
    private static final String CAMERA_COMMON_PACKAGE_NAME = "com.sonymobile.cameracommon";
    public static final String INTENT_SUBJECT_CANCEL = "cancel";
    public static final String INTENT_SUBJECT_PAUSED = "activity-paused";
    public static final String INTENT_SUBJECT_PREPARE = "prepare";
    public static final String INTENT_SUBJECT_RESUMED = "activity-resumed";
    public static final String INTENT_SUBJECT_START = "start";
    public static final String INTENT_SUBJECT_START_SECURE = "start-secure";
    private static final long ON_RESUME_DELAY_NON_SECURE_MILLIS = 15;
    private static final long ON_RESUME_DELAY_SECURE_MILLIS = 30;
    private static final long PREPARE_PLATFORM_CAPABILITY_TIMED_OUT_MILLIS = 2000;
    private static final int RESULT_AUTO_OFF_TIMER = 2;
    private static final long SEND_PAUSE_EVENT_DELAY_MILLIS = 500;
    public static final int SETUP_DEVICE_SETUP_WAIT_TIME = 100;
    private static final int SETUP_LAZY_EXECUTION_WAIT_TIME = 200;
    private static final String TAG = "CameraActivity";
    private static boolean sIsReportFullyDrawnAlreadyReported = false;
    private SparseArray<OnActivityResultListener> mActivityResultListeners;
    private AutoPowerOffTimer mAutoPowerOffTimer;
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private CameraDeviceHandler mCameraDeviceHandler;
    private CameraDeviceHandler.CameraSessionId mCurrentCameraSessionId;
    private GeotagManager mGeotagManager;
    private boolean mIsCalledOnDestroy;
    private LaunchCondition mLaunchCondition;
    private LocationSettingsReader mLocationSettingsReader;
    private Handler mMainHandler;
    private OrientationEventListener mOrientationEventListener;
    private boolean mResetSettingsRequested;
    private LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private ScreenOffReceiver mScreenOffReceiver;
    protected StateMachine mStateMachine;
    private Storage mStorage;
    private StoredSettings mStoredSettings;
    private ThermalAlertReceiver mThermalAlertReceiver;
    private UserEventHandler mUserEventHandler;
    private ViewFinder mViewFinder;
    private boolean mReturnOneShotResult = true;
    private boolean mDisableMultiWindow = false;
    private SoundPlayer mSoundPlayer = null;
    private ShutDownReceiver mShutDownReceiver = new ShutDownReceiver();
    private PostDeviceInitializationTask mPostDeviceInitializationTask = null;
    private LazyInitializationTask mLazyInitializationiTask = null;
    private WearableBridgeClient mWearableBridgeClient = null;
    private ObserveWearableInterface.LifeCycleObserver mWearableBridgeLifeCycleObserver = null;
    private ObserveWearableInterface.PhotoEventObserver mWearableBridgePhotoEventObserver = null;
    private ObserveWearableInterface.VideoEventObserver mWearableBridgeVideoEventObserver = null;
    private boolean mIsLazyInitializationRunning = true;
    private final String[] REQUESTED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private ExecutorService mBackgroundWorker = null;
    private Future<?> mThermalAlertReceiverReadyTaskFuture = null;
    private Future<?> mViewFinderInitializationTaskFuture = null;
    private Future<?> mSetupAllTaskFuture = null;
    private Future<?> mCreateContentInfoTaskFuture = null;
    private boolean mIsColdBoot = false;
    private boolean mIsNeedToCloseBypassCameraBecauseModeChanged = false;

    @Nullable
    private KeyguardManager mKeyguardManager = null;
    private boolean mSkippedOnResume = false;
    private LayoutOrientation mLastDetectedOrientation = LayoutOrientation.Unknown;
    private final Set<LayoutOrientationChangedListener> mLayoutOrientationChangedListenerSet = new CopyOnWriteArraySet();
    private int mSensorOrientationDegree = -1;
    private int mLastOrientationDegree = -1;
    private int mLastDeterminedOrientationDegree = -1;
    private boolean mIsReceiverResistered = false;
    private ForceExitRequestReceiver mForceExitRequestReceiver = null;
    private CameraActivityFinishBroadcastReceiver mCameraActivityFinishReceiver = null;
    private final UserEventHandler.KeyEventDispatcher mKeyEventDispatcher = new UserEventHandler.KeyEventDispatcher();
    private final UserEventHandler.SideTouchEventDispatcher mSideTouchEventDispatcher = new UserEventHandler.SideTouchEventDispatcher();
    private final Runnable mOnResumeTasks = new Runnable() { // from class: com.sonyericsson.android.camera.CameraActivity.1
        @Override // java.lang.Runnable
        @MainThread
        public void run() throws IOException {
            if (CameraActivity.this.mSkippedOnResume) {
                if (CamLog.DEBUG) {
                    CamLog.d("Runnable --> onResumeTasks()");
                }
                CameraActivity.this.mSkippedOnResume = false;
                CameraActivity.this.onResumeTasks();
                return;
            }
            if (CamLog.DEBUG) {
                throw new IllegalStateException("OnResumeTasks was executed when onResume is not skipped.");
            }
        }
    };
    private final Runnable mSendPauseEventAndReleaseCameraTask = new Runnable() { // from class: com.sonyericsson.android.camera.CameraActivity.2
        @Override // java.lang.Runnable
        @MainThread
        public void run() {
            if (CameraActivity.this.mSkippedOnResume) {
                if (CamLog.DEBUG) {
                    CamLog.d("Runnable --> sendEvent(EVENT_PAUSE) & closeCamera()");
                }
                CameraActivity.this.mSkippedOnResume = false;
                CameraActivity.this.notifyActivityState(CameraActivity.INTENT_SUBJECT_PAUSED);
                if (CameraActivity.this.mStateMachine != null) {
                    CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, true);
                }
                CameraActivity.this.getCameraDevice().closeCamera(CameraActivity.this.mCurrentCameraSessionId);
                CameraActivity.this.mCurrentCameraSessionId = null;
                return;
            }
            if (CamLog.DEBUG) {
                throw new IllegalStateException("PauseEventAndReleaseCameraTask was executed when onResume is not skipped.");
            }
        }
    };
    private ThermalAlertReceiver.ThermalAlertReceiverListener mThermalAlertListener = new ThermalAlertReceiver.ThermalAlertReceiverListener() { // from class: com.sonyericsson.android.camera.CameraActivity.3
        @Override // com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver.ThermalAlertReceiverListener
        public void onReachCriticalTemperature(boolean z) {
            LocalResearchUtil.getInstance().setMeasurementThermal(true);
            if (CameraActivity.this.mStateMachine != null) {
                ResearchUtil.getInstance().sendThermalEvent(z, CameraActivity.this.isRecording());
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_CRITICAL, Boolean.valueOf(z));
            }
            CameraActivity.this.releaseCamera();
        }

        @Override // com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver.ThermalAlertReceiverListener
        public void onNotifyThermalNormal() {
            LocalResearchUtil.getInstance().setMeasurementThermal(false);
            if (CameraActivity.this.mStateMachine != null) {
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_NORMAL, new Object[0]);
            }
        }

        @Override // com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver.ThermalAlertReceiverListener
        public void onNotifyThermalWarning(boolean z) {
            LocalResearchUtil.getInstance().setMeasurementThermal(true);
            if (CameraActivity.this.mStateMachine != null) {
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_WARNING, new Object[0]);
                if (z) {
                    ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_ULTRA_LOW_ON_STARTUP, CameraActivity.this.isRecording());
                } else {
                    ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_ULTRA_LOW, CameraActivity.this.isRecording());
                }
                if (CameraActivity.this.mCameraDeviceHandler == null || !PlatformCapability.isPowerSavingSupported(CameraActivity.this.mStateMachine.getCurrentCameraId())) {
                    return;
                }
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
            }
        }

        @Override // com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver.ThermalAlertReceiverListener
        public void onNotifyThermalWarningExtra(boolean z) {
            LocalResearchUtil.getInstance().setMeasurementThermal(true);
            if (CameraActivity.this.mStateMachine != null) {
                if (!CameraActivity.this.isThermalWarningReceived()) {
                    if (z) {
                        ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_LOW_ON_STARTUP, CameraActivity.this.isRecording());
                    } else {
                        ResearchUtil.getInstance().sendCoolModeEvent(Event.CoolMode.HEATED_OVER_COOLING_LOW, CameraActivity.this.isRecording());
                    }
                }
                if (CameraActivity.this.mCameraDeviceHandler != null && PlatformCapability.isPowerSavingSupported(CameraActivity.this.mStateMachine.getCurrentCameraId())) {
                    if (CameraActivity.this.isThermalWarningReceived()) {
                        return;
                    }
                    CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
                    return;
                }
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_WARNING_EXTRA, new Object[0]);
            }
        }
    };
    private BatteryChangedReceiver.BatteryChangedReceiverListener mBatteryChangedListener = new BatteryChangedReceiver.BatteryChangedReceiverListener() { // from class: com.sonyericsson.android.camera.CameraActivity.4
        @Override // com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver.BatteryChangedReceiverListener
        public void onReachLowBattery() {
            CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_REACH_BATTERY_LOW, new Object[0]);
        }

        @Override // com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver.BatteryChangedReceiverListener
        public void onReachBatteryLimit(boolean z) {
            ResearchUtil.getInstance().sendLowBatteryEvent(z, CameraActivity.this.isRecording());
            if (CameraActivity.this.mStateMachine != null) {
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_REACH_BATTERY_LIMIT, new Object[0]);
            }
            CameraActivity.this.releaseCamera();
        }

        @Override // com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver.BatteryChangedReceiverListener
        public void onBatteryLevelChanged(int i) {
            if (CamLog.VERBOSE) {
                CamLog.d("onBatteryLevelChanged : " + i);
            }
            CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BATTERY_LEVEL_CHANGED, Integer.valueOf(i));
        }
    };
    private OnActivityResultListener mSetupWizardResultListener = new OnActivityResultListener() { // from class: com.sonyericsson.android.camera.CameraActivity.5
        @Override // com.sonyericsson.cameracommon.activity.OnActivityResultListener
        public boolean onActivityResult(int i, int i2, Intent intent) {
            if (i2 == -1) {
                boolean booleanExtra = intent.getBooleanExtra("geo_tag_result", false);
                boolean booleanExtra2 = intent.getBooleanExtra("side_sense_result", false);
                requestLocationChange(booleanExtra);
                ArrayList arrayList = new ArrayList();
                arrayList.add(booleanExtra2 ? SideSense.ON : SideSense.OFF);
                if (PlatformCapability.isLiftTriggerSupported()) {
                    arrayList.add(PredictiveLaunch.TOUCH_TO_LAUNCH);
                }
                CameraActivity.this.getLaunchCondition().getExtraSettings().set(CameraActivity.this.getLaunchCondition().getCapturingMode(), arrayList);
            } else if (i2 == 1) {
                CameraActivity.this.finish();
            }
            return true;
        }

        private void requestLocationChange(boolean z) {
            if (CamLog.VERBOSE) {
                CamLog.d("requestLocationChange: enable = " + z);
            }
            if (z) {
                CameraActivity.this.getGeoTagManager().setIsGeotagPermissionGranted(true);
            }
            MessageSettings messageSettings = CameraActivity.this.getStoredSettings().getMessageSettings();
            messageSettings.setNeverShow(MessageType.SETUP_WIZARD, true);
            messageSettings.save();
        }
    };
    private boolean mCanFinishByScreenOff = true;

    public enum LayoutOrientation {
        Unknown,
        Portrait,
        Landscape,
        ReversePortrait,
        ReverseLandscape
    }

    public interface LayoutOrientationChangedListener {
        void onLayoutOrientationChanged(LayoutOrientation layoutOrientation);
    }

    public interface PermissionCheckCallback {
        boolean onPermissionChecked(String[] strArr);
    }

    private static boolean in(int i, int i2, int i3) {
        return i >= i2 && i < i3;
    }

    public static final void preload() {
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onSearchRequested() {
        return false;
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
    }

    protected boolean shouldShowWhenLocked() {
        return false;
    }

    private static void logPerformance(String str) {
        Log.e("TraceLog", "[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + str + "]");
    }

    private enum AutoPowerOffType {
        DEFAULT(Constants.TIMEOUT_NO_OPERATION, "DEFAULT", Event.AutoPowerOffAction.DEFAULT),
        ON_LOCKSCREEN(LaunchCondition.RESET_LAUNCH_MODE_TIME_LIMIT_MILLIS, "ON_LOCKSCREEN", Event.AutoPowerOffAction.ON_LOCKSCREEN),
        LIFT_TRIGGER(RecordingProfile.VIDEO_AUDIO_BIT_RATE_MMS, "LIFT_TRIGGER", Event.AutoPowerOffAction.LIFT_TRIGGER),
        LIFT_TRIGGER_DIALOG(15000, "LIFT_TRIGGER_DIALOG", Event.AutoPowerOffAction.LIFT_TRIGGER_DIALOG);

        private final Event.AutoPowerOffAction mAction;
        private final int mDuration;
        private final String mReason;

        AutoPowerOffType(int i, String str, Event.AutoPowerOffAction autoPowerOffAction) {
            this.mDuration = i;
            this.mReason = str;
            this.mAction = autoPowerOffAction;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public String getReason() {
            return this.mReason;
        }

        public Event.AutoPowerOffAction getAction() {
            return this.mAction;
        }
    }

    private enum RequestTypeForSomcCameraService {
        GYRO_CALIBRATION(0),
        SCREEN_OFF(1);

        public final int mExtraValue;

        RequestTypeForSomcCameraService(int i) {
            this.mExtraValue = i;
        }
    }

    public enum LifeCycleIds {
        ON_CREATE(MeasurePerformance.PerformanceIds.ON_CREATE, "onCreate()"),
        ON_START(MeasurePerformance.PerformanceIds.ON_START, "onStart()"),
        ON_RESTART(MeasurePerformance.PerformanceIds.ON_RESTART, "onRestart()"),
        ON_RESUME(MeasurePerformance.PerformanceIds.ON_RESUME, "onResume()"),
        ON_PAUSE(MeasurePerformance.PerformanceIds.ON_PAUSE, "onPause()"),
        ON_STOP(MeasurePerformance.PerformanceIds.ON_STOP, "onStop()"),
        ON_DESTROY(MeasurePerformance.PerformanceIds.ON_DESTROY, "onDestroy()");

        private final String mLog;
        private final MeasurePerformance.PerformanceIds mPerformanceIds;

        LifeCycleIds(MeasurePerformance.PerformanceIds performanceIds, String str) {
            this.mPerformanceIds = performanceIds;
            this.mLog = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mLog;
        }
    }

    private static boolean getFuture(Future<?> future) throws ExecutionException, InterruptedException, TimeoutException {
        if (future == null) {
            return true;
        }
        try {
            future.get(MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS, TimeUnit.MILLISECONDS);
            return true;
        } catch (InterruptedException e) {
            CamLog.e("Failed to get Future.", e);
            return false;
        } catch (CancellationException e2) {
            CamLog.e("Failed to get Future.", e2);
            return false;
        } catch (ExecutionException e3) {
            CamLog.e("Failed to get Future.", e3);
            return false;
        } catch (TimeoutException e4) {
            CamLog.e("Failed to get Future.", e4);
            return false;
        }
    }

    @Override // com.sonyericsson.cameracommon.activity.TerminateListener
    public void terminateApplication() {
        finish();
    }

    private class ScreenOffReceiver extends BroadcastReceiver {
        private static final String TAG = "ScreenOffReceiver";

        private ScreenOffReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onReceive():[IN]");
            }
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onReceive():[Receive SCREEN_OFF]");
                }
                if (CameraActivity.this.isFinishing() || CameraActivity.this.isDestroyed()) {
                    return;
                }
                if (CameraActivity.this.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.POWER_KEY_DOUBLE_TAP && !CameraActivity.this.canFinishByScreenOff()) {
                    CameraActivity.this.allowFinishByScreenOff();
                } else {
                    CameraActivity.this.requestSuspend();
                }
            }
        }
    }

    private class ShutDownReceiver extends BroadcastReceiver {
        private ShutDownReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                CameraActivity.this.getCameraDevice().setIsInShutdownNow(true);
            }
        }
    }

    public CameraDeviceHandler.CameraSessionId prepareCameraDeviceHandler(FastCapture fastCapture, CapturingMode capturingMode, UserSettings userSettings) {
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("prepareCameraDeviceHandler() launch:");
            sb.append(fastCapture == null ? "null" : fastCapture.name());
            sb.append(" capturing-mode:");
            sb.append(capturingMode.name());
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        if (this.mIsNeedToCloseBypassCameraBecauseModeChanged) {
            this.mIsNeedToCloseBypassCameraBecauseModeChanged = false;
            getCameraDevice().closeCamera();
        }
        if (!getCameraDevice().prepareCamera(fastCapture, capturingMode, userSettings)) {
            return null;
        }
        userSettings.applyCapturingMode();
        return getCameraDevice().openCamera(fastCapture, capturingMode, userSettings);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) throws InterruptedException, IOException {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        PerfLog.ACTIVITY_ON_CREATE.begin();
        boolean z = !PlatformCapability.awaitPrepare(PREPARE_PLATFORM_CAPABILITY_TIMED_OUT_MILLIS, TimeUnit.MILLISECONDS);
        if (!PlatformCapability.isPrepared() || PlatformCapability.hasDeviceError()) {
            if (CamLog.DEBUG) {
                CamLog.d("PlatformCapability is not prepared yet");
            }
            if (!z) {
                if (CamLog.DEBUG) {
                    CamLog.d("Retry preparing PlatformCapability");
                }
                PlatformCapability.prepareAsync(new PlatformCapability.OnPlatformCapabilityPreparedCallback() { // from class: com.sonyericsson.android.camera.CameraActivity.6
                    @Override // com.sonyericsson.android.camera.util.capability.PlatformCapability.OnPlatformCapabilityPreparedCallback
                    public void onPrepared() {
                        if (CamLog.DEBUG) {
                            CamLog.d("PlatformCapability Prepared");
                        }
                        ((CameraApplication) CameraActivity.this.getApplication()).getUserSettingsLoader().load();
                    }
                });
                PlatformCapability.awaitPrepare(PREPARE_PLATFORM_CAPABILITY_TIMED_OUT_MILLIS, TimeUnit.MILLISECONDS);
            }
            if (!PlatformCapability.isPrepared()) {
                PlatformCapability.setDeviceError(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.cam_strings_error_dialog_title_txt).setMessage(R.string.cam_strings_error_fatal_txt).setCancelable(false).setPositiveButton(R.string.cam_strings_ok_txt, (DialogInterface.OnClickListener) null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.sonyericsson.android.camera.CameraActivity.7
                    @Override // android.content.DialogInterface.OnDismissListener
                    public void onDismiss(DialogInterface dialogInterface) {
                        CameraActivity.this.finishUrgently();
                    }
                });
                AlertDialog alertDialogCreate = builder.create();
                alertDialogCreate.getWindow().addFlags(128);
                alertDialogCreate.show();
                super.onCreate(bundle);
                return;
            }
        }
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_CREATE);
        }
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        if (getIntent().getAction() == null) {
            getIntent().setAction("android.intent.action.MAIN");
        }
        getWindow().requestFeature(12);
        this.mStorage = ((CameraApplication) getApplication()).getStorage();
        this.mStoredSettings = SettingsFactory.create(getApplicationContext(), this.mStorage);
        this.mBackgroundWorker = ThreadUtil.buildExecutor("AsyncAct", 10);
        createLaunchCondition();
        this.mLaunchCondition.setup(getIntent(), this.mStoredSettings.getLastSettings().getCapturingMode(), this instanceof InternalCameraActivity);
        notifyActivityState(INTENT_SUBJECT_RESUMED);
        LaunchCondition.LaunchTrigger launchTrigger = getLaunchCondition().getLaunchTrigger();
        if (launchTrigger == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        }
        if (!isLaunchedFromLocked(getIntent()) && launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        }
        UserSettings userSettings = this.mStoredSettings.getUserSettings();
        userSettings.prepare(getApplicationContext(), this.mLaunchCondition.getOneShotMode(), this.mLaunchCondition.getExtraOutput(), this.mLaunchCondition.getVideoQualityConfigurations(), this.mLaunchCondition.getExtraSettings());
        userSettings.changeCapturingMode(this.mLaunchCondition.getCapturingMode());
        this.mCurrentCameraSessionId = getCameraDevice().preloadCamera(this, userSettings, this.mLaunchCondition.getCapturingMode(), this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE && !isInLockTaskMode());
        this.mAutoPowerOffTimer = new AutoPowerOffTimer(this, new AutoPowerOffListenerImpl());
        setupCoreInstance();
        super.onCreate(null);
        this.mLocationSettingsReader = new LocationSettingsReader();
        ResearchUtil.getInstance().onCreate(this);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_INITIALIZE, new Object[0]);
        if (this.mStorage.isStorageReadable()) {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageActivated was already finished.");
            }
            this.mSetupAllTaskFuture = this.mBackgroundWorker.submit(new SetupAllTask());
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageActivated was not finished yet.");
            }
            this.mStorage.addStorageReadyStateListener(new Storage.StorageReadyStateListener() { // from class: com.sonyericsson.android.camera.CameraActivity.8
                @Override // com.sonyericsson.cameracommon.storage.Storage.StorageReadyStateListener
                public void onStorageReadyStateChanged(Storage.StorageType storageType, Storage.StorageReadyState storageReadyState) {
                    if (CameraActivity.this.mStorage.isStorageReadable()) {
                        if (CamLog.DEBUG) {
                            CamLog.d("onStorageReadyStateChanged: Storage can be readable, Got to setupAll");
                        }
                        CameraApplication.getUiThreadHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.CameraActivity.8.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CameraActivity.this.mSetupAllTaskFuture = CameraActivity.this.mBackgroundWorker.submit(new SetupAllTask());
                                CameraActivity.this.mStorage.removeStorageReadyStateListener(this);
                            }
                        });
                    }
                }
            });
        }
        createBatteryChangedReceiver();
        LocalResearchUtil.getInstance().setBatteryChangedReceiver(this.mBatteryChangedReceiver);
        this.mBackgroundWorker.execute(new ThermalAlertReceiverCreateTask());
        this.mBackgroundWorker.execute(new ThermalAlertReceiverOnCreateTask());
        registerForceExitRequestReceiver();
        if (PlatformCapability.isWearableSupported()) {
            setupWearableFramework();
        }
        setupScreenOffReceiver();
        this.mIsColdBoot = true;
        if (CamLog.VERBOSE) {
            logLifeCycleOut(TAG, LifeCycleIds.ON_CREATE);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
        PerfLog.ACTIVITY_ON_CREATE.end();
    }

    public void setupAutoPowerOffTimeOutDuration(boolean z) {
        AutoPowerOffType autoPowerOffType;
        if (z) {
            if (!this.mStoredSettings.getMessageSettings().isNeverShow(MessageType.PREDICTIVE_LAUNCH_DESCRIPTION)) {
                autoPowerOffType = AutoPowerOffType.LIFT_TRIGGER_DIALOG;
            } else {
                autoPowerOffType = AutoPowerOffType.LIFT_TRIGGER;
            }
        } else if (shouldShowWhenLocked()) {
            autoPowerOffType = AutoPowerOffType.ON_LOCKSCREEN;
        } else {
            autoPowerOffType = AutoPowerOffType.DEFAULT;
        }
        this.mAutoPowerOffTimer.setTimeOutDuration(autoPowerOffType.getDuration(), this.mViewFinder.getAutoPowerOffHintTextTimeOutDuration(), autoPowerOffType.getReason());
    }

    private void setupScreenOffReceiver() {
        this.mScreenOffReceiver = new ScreenOffReceiver();
        IntentFilter intentFilter = new IntentFilter();
        if (shouldShowWhenLocked()) {
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }
        registerReceiver(this.mScreenOffReceiver, intentFilter);
    }

    private void registerShutDownReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        registerReceiver(this.mShutDownReceiver, intentFilter);
    }

    private void unRegisterShutDownReceiver() {
        unregisterReceiver(this.mShutDownReceiver);
    }

    private class SetupAllTask implements Runnable {
        private SetupAllTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.setupAll();
        }
    }

    public boolean awaitSetupAllReady() {
        return getFuture(this.mSetupAllTaskFuture);
    }

    private final class ThermalAlertReceiverCreateTask implements Runnable {
        private ThermalAlertReceiverCreateTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.mThermalAlertReceiver = new ThermalAlertReceiver(CameraActivity.this, CameraActivity.this.mThermalAlertListener);
        }
    }

    private final class ThermalAlertReceiverOnCreateTask implements Runnable {
        private ThermalAlertReceiverOnCreateTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.mThermalAlertReceiver.onCreate();
            CameraActivity.this.mBatteryChangedReceiver.onCreate();
        }
    }

    private void setupWearableFramework() {
        this.mWearableBridgeLifeCycleObserver = new WearableBridgeLifeCycleObserver();
        this.mWearableBridgePhotoEventObserver = new WearableBridgePhotoEventObserver();
        this.mWearableBridgeVideoEventObserver = new WearableBridgeVideoEventObserver();
        this.mWearableBridgeClient = new WearableBridgeClient(this, new Handler(), this.mWearableBridgeLifeCycleObserver, this.mWearableBridgePhotoEventObserver, this.mWearableBridgeVideoEventObserver);
    }

    public void reportFullyDrawnOnce() {
        if (sIsReportFullyDrawnAlreadyReported) {
            return;
        }
        sIsReportFullyDrawnAlreadyReported = true;
        CamLog.d("Report fully drawn");
        reportFullyDrawn();
    }

    public void finishOneShot(StateMachine.OneShotResult oneShotResult) {
        if (CamLog.VERBOSE) {
            CamLog.d("finishOneShot: result: " + oneShotResult.uri);
        }
        this.mDisableMultiWindow = true;
        if (isOneShotVideo()) {
            Intent intent = new Intent();
            intent.setData(oneShotResult.uri);
            intent.addFlags(1);
            setResult(oneShotResult.code, intent);
            terminateApplication();
            return;
        }
        if (isOneShotPhoto()) {
            if (oneShotResult.savingRequest.getExtraOutput() == null) {
                if (oneShotResult.isSuccess) {
                    setResult(oneShotResult.code, OneShotUtility.createResultIntent(this, oneShotResult.uri, oneShotResult.savingRequest.common.mimeType, oneShotResult.savingRequest.common.orientation, oneShotResult.bitmap));
                } else {
                    setResult(0);
                }
            } else {
                setResult(oneShotResult.code);
            }
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAll() {
        this.mGeotagManager = new GeotagManager(this);
        this.mSoundPlayer = new SoundPlayer(getApplicationContext());
    }

    private ViewFinderImpl createViewFinder() {
        ViewFinderImpl viewFinderImpl = new ViewFinderImpl(this, shouldShowWhenLocked(), getScreenAspect(), getStoredSettings().getUiControlSettings());
        viewFinderImpl.setStateMachine(this.mStateMachine);
        viewFinderImpl.setCameraDevice(getCameraDevice());
        viewFinderImpl.prepareSelfTimerAndTouchCapture();
        this.mViewFinderInitializationTaskFuture = this.mBackgroundWorker.submit(new ViewFinderInitializationTask(viewFinderImpl));
        return viewFinderImpl;
    }

    private static class ViewFinderInitializationTask implements Runnable {
        private final ViewFinderImpl mViewFinderImpl;

        private ViewFinderInitializationTask(ViewFinderImpl viewFinderImpl) {
            this.mViewFinderImpl = viewFinderImpl;
        }

        @Override // java.lang.Runnable
        public void run() {
            PerfLog.TASK_VIEW_FINDER_INITIALIZATION.begin();
            this.mViewFinderImpl.initialize();
            this.mViewFinderImpl.setContentView();
            PerfLog.TASK_VIEW_FINDER_INITIALIZATION.end();
        }
    }

    private void setupCoreInstance() {
        if (CamLog.VERBOSE) {
            CamLog.d("invoked setupCoreInstance()");
        }
        this.mStateMachine = new StateMachine(this, this.mStorage);
        ViewFinderImpl viewFinderImplCreateViewFinder = createViewFinder();
        this.mViewFinder = viewFinderImplCreateViewFinder;
        this.mUserEventHandler = new UserEventHandler(this, viewFinderImplCreateViewFinder, this.mStateMachine, this.mStorage, this.mStoredSettings.getUserSettings(), this.mStoredSettings.getMessageSettings(), !getLaunchCondition().getOneShotMode().isEnabled());
        this.mKeyEventDispatcher.attach(this.mUserEventHandler);
        this.mKeyEventDispatcher.start();
        this.mSideTouchEventDispatcher.attach(this.mUserEventHandler);
        viewFinderImplCreateViewFinder.getTouchEventDispatcher().attach(this.mUserEventHandler);
        viewFinderImplCreateViewFinder.getTouchEventDispatcher().start();
        this.mStateMachine.getVirtualKeyEventDispatcher().attach(this.mUserEventHandler);
        this.mStateMachine.getVirtualKeyEventDispatcher().start();
        this.mStateMachine.setDependencies(this.mViewFinder, getCameraDevice());
    }

    public LayoutDependencyResolver.ScreenAspect getScreenAspect() {
        if (this.mScreenAspect == null) {
            ((WindowManager) getSystemService("window")).getDefaultDisplay().getRealSize(new Point());
            float fMax = (Math.max(r1.y, r1.x) * 1.0f) / Math.min(r1.y, r1.x);
            if (Math.abs(fMax - 2.0f) < 0.01f) {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE;
            } else if (Math.abs(fMax - 1.7777778f) < 0.01f) {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE;
            } else {
                this.mScreenAspect = LayoutDependencyResolver.ScreenAspect.NOT_DEFINED;
            }
        }
        return this.mScreenAspect;
    }

    @Override // android.app.Activity
    public void onStart() throws IOException {
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_START);
        }
        super.onStart();
        if (PlatformCapability.isPrepared() && CamLog.VERBOSE) {
            logLifeCycleOut(TAG, LifeCycleIds.ON_START);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canFinishByScreenOff() {
        return this.mCanFinishByScreenOff;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allowFinishByScreenOff() {
        this.mCanFinishByScreenOff = true;
    }

    private class OnPreviewStartedListenerImpl implements CameraDeviceHandler.OnPreviewStartedListener {
        private final CameraDeviceHandler.CameraSessionId mCameraSessionId;

        public OnPreviewStartedListenerImpl(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mCameraSessionId = cameraSessionId;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.OnPreviewStartedListener
        public void onPreviewStarted(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            if (this.mCameraSessionId == cameraSessionId) {
                CameraActivity.this.reportFullyDrawnOnce();
            }
        }
    }

    @Override // android.app.Activity
    public void onRestart() throws IOException {
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_RESTART);
        }
        super.onRestart();
        if (PlatformCapability.isPrepared()) {
            this.mLaunchCondition.onRestart(isKeyguardLocked(), shouldShowWhenLocked());
            if (CamLog.VERBOSE) {
                logLifeCycleOut(TAG, LifeCycleIds.ON_RESTART);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResumeTasks() throws IOException {
        PerfLog.ACTIVITY_ON_RESUME.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : E");
        }
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_RESUME);
        }
        getCameraDevice().setActivityForeground(true);
        getLaunchCondition().onResume();
        if (!checkAndRequestSelfPermissions(12, this.REQUESTED_PERMISSIONS)) {
            doResume();
        } else if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            if (CamLog.VERBOSE) {
                CamLog.d("start clearExtraOperation");
            }
            this.mLaunchCondition.clearExtraOperation();
        }
        sendBroadcast(new Intent(IntentConstants.BroadcastIntent.ACTION_CAMERA_LAUNCH));
        CameraButtonIntentReceiver.releaseCameraDeviceReleaseTimer();
        notifyActivityState(INTENT_SUBJECT_RESUMED);
        this.mKeyEventDispatcher.start();
        if (CamLog.VERBOSE) {
            logLifeCycleOut(TAG, LifeCycleIds.ON_RESUME);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : X");
        }
        PerfLog.ACTIVITY_ON_RESUME.end();
    }

    private void doResume() {
        FastCapture fastCapture;
        if (!this.mLaunchCondition.isCorrectExtraOutputPath()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.cam_strings_error_memory_title_txt).setMessage(R.string.cam_strings_error_memory_ims_unavailable_txt).setCancelable(false).setPositiveButton(R.string.cam_strings_ok_txt, (DialogInterface.OnClickListener) null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.sonyericsson.android.camera.CameraActivity.9
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    CameraActivity.this.abort();
                }
            });
            AlertDialog alertDialogCreate = builder.create();
            alertDialogCreate.getWindow().addFlags(128);
            alertDialogCreate.show();
            return;
        }
        if (!isKeyguardLocked() && this.mLaunchCondition.isLaunchInternalMode()) {
            ModeSelectorInternalMode modeSelectorInternalMode = ModeSelectorInternalMode.values()[this.mLaunchCondition.getLaunchInternalMode()];
            this.mLaunchCondition.clearLaunchInternalMode();
            CapturingMode capturingMode = CapturingMode.values()[this.mLaunchCondition.getLaunchInternalCallingCapturingMode()];
            this.mLaunchCondition.clearLaunchInternalCallingCapturingMode();
            boolean zContains = CapturingModeUtil.MODE_WHITE_LIST.contains(modeSelectorInternalMode.name());
            switch (modeSelectorInternalMode) {
                case PORTRAIT_SELFIE:
                    ApplicationLauncher.launchExternalCamera(this, 18, this.mStateMachine.getUserSetting(), capturingMode, zContains);
                    return;
                case DUAL_MONOCHROME:
                    ApplicationLauncher.launchExternalCamera(this, 17, this.mStateMachine.getUserSetting(), capturingMode, zContains);
                    return;
                case DUAL_BACKGROUND_DEFOCUS:
                    ApplicationLauncher.launchExternalCamera(this, 16, this.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, zContains);
                    return;
                default:
                    if (CamLog.VERBOSE) {
                        CamLog.e("Bad launch internalMode :" + modeSelectorInternalMode);
                        break;
                    }
                    break;
            }
        }
        enableOrientation();
        if (getLaunchCondition().getLaunchTrigger() != LaunchCondition.LaunchTrigger.VIEWER) {
            this.mStoredSettings.getUserSettings().clearCachedUserSetting();
        }
        this.mStoredSettings.getUserSettings().changeCapturingMode(this.mLaunchCondition.getCapturingMode());
        if (this.mLaunchCondition.getLaunchCameraMode().isLaunchedByGoogleAssistant()) {
            changeParametersForGoogleAssistant();
        }
        this.mCameraDeviceHandler = getCameraDevice();
        this.mCameraDeviceHandler.setStateMachine(this.mStateMachine);
        if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            fastCapture = FastCapture.LAUNCH_AND_CAPTURE;
        } else {
            fastCapture = FastCapture.LAUNCH_ONLY;
        }
        this.mCurrentCameraSessionId = prepareCameraDeviceHandler(fastCapture, this.mLaunchCondition.getCapturingMode(), this.mStoredSettings.getUserSettings());
        this.mCameraDeviceHandler.setOnPreviewStartedListener(new OnPreviewStartedListenerImpl(this.mCurrentCameraSessionId));
        getWindow().clearFlags(2048);
        getWindow().addFlags(1024);
        getWindow().addFlags(256);
        getWindow().addFlags(512);
        getWindow().addFlags(128);
        getCameraDevice().awaitLoadSettingsThread();
        this.mLocationSettingsReader.readLocationSettings(this);
        setupAutoPowerOffTimeOutDuration(getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER);
        enableAutoPowerOffTimer();
        ResearchUtil.getInstance().onResume(false);
        if (getWearableBridge() != null) {
            getWearableBridge().joinInitializeTask();
            getWearableBridge().getLifeCycleNotifier().onResume();
        }
        getCameraDevice().disableFpsLimitation();
        ((ViewFinderImpl) this.mViewFinder).updatePreviewLayoutParams();
        if (isDeviceInSecurityLock() && (getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.DUAL_CAMERA_EFFECT || getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.PORTRAIT_SELFIE)) {
            awaitCreateContentInfoReady();
        }
        if (LocalResearchUtil.getInstance().isMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE) && LocalResearchUtil.getInstance().isMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE)) {
            LocalResearchUtil.getInstance().setMeasurementInvalid(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE);
        }
        LocalResearchUtil.getInstance().setLaunchBy(this.mLaunchCondition.getLaunchTrigger());
        LocalResearchUtil.getInstance().setView(this.mLaunchCondition.getCapturingMode());
        StateMachine.StartupAction startupAction = StateMachine.StartupAction.NONE;
        if (this.mLaunchCondition.getGoogleAssistantSelfTimer() > 0) {
            startupAction = StateMachine.StartupAction.CAPTURE;
        } else if (!this.mLaunchCondition.isGoogleAssistantLaunchOnly() && this.mLaunchCondition.getCapturingMode().isVideo()) {
            startupAction = StateMachine.StartupAction.RECORD;
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_RESUME, fastCapture, this.mCurrentCameraSessionId, startupAction);
        if (this.mLastDetectedOrientation == LayoutOrientation.Unknown) {
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 2) {
                setLayoutOrientation(LayoutOrientation.Landscape);
                this.mLastDetectedOrientation = LayoutOrientation.Landscape;
            } else {
                setLayoutOrientation(LayoutOrientation.Portrait);
                this.mLastDetectedOrientation = LayoutOrientation.Portrait;
            }
        }
        if (isInMultiWindowMode() && !this.mDisableMultiWindow) {
            launchMultiWindow();
        }
        this.mBatteryChangedReceiver.checkStartupStatus();
        boolean z = !getStoredSettings().getMessageSettings().isNeverShow(MessageType.SETUP_WIZARD);
        if (PlatformCapability.isLiftTriggerSupported() && this.mStoredSettings.getUserSettings().get(UserSettingKey.PREDICTIVE_LAUNCH) == PredictiveLaunch.OFF && getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            this.mBackgroundWorker.execute(new ScreenOffTask());
            terminateApplication();
            return;
        }
        if (z && !this.mBatteryChangedReceiver.isAlreadyBcl() && !isOneShot() && getLaunchCondition().getLaunchTrigger() != LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            startActivityForResult(new Intent(this, (Class<?>) SetupWizardActivity.class), 15, this.mSetupWizardResultListener);
        } else {
            this.mThermalAlertReceiverReadyTaskFuture = this.mBackgroundWorker.submit(new ThermalAlertReceiverOnResumeTask());
        }
        requestInflateUiComponents();
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        if (PlatformCapability.isPrepared()) {
            if (CamLog.VERBOSE) {
                CamLog.d("onMultiWindowModeChanged() : " + z);
            }
            if (z && PermissionsUtil.arePermissionsGranted(this, this.REQUESTED_PERMISSIONS) && !this.mDisableMultiWindow) {
                launchMultiWindow();
            }
        }
    }

    @Override // android.app.Activity
    public void onUserInteraction() {
        super.onUserInteraction();
        if (this.mAutoPowerOffTimer == null || AutoPowerOffType.LIFT_TRIGGER.getReason().equals(this.mAutoPowerOffTimer.getUserdata())) {
            return;
        }
        restartAutoPowerOffTimer();
    }

    private void launchMultiWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("Launch multi window mode activity ");
        }
        this.mDisableMultiWindow = true;
        this.mReturnOneShotResult = false;
        Intent intent = new Intent(getIntent());
        intent.setClass(getApplicationContext(), MultiWindowActivity.class);
        if (intent.getAction() == null) {
            if (isOneShotPhoto()) {
                intent.setAction("android.media.action.IMAGE_CAPTURE");
            } else if (isOneShotVideo()) {
                intent.setAction("android.media.action.VIDEO_CAPTURE");
            } else {
                if (CamLog.VERBOSE) {
                    CamLog.e("SomcCamera not support multi window mode, intent :[" + intent + "]");
                }
                terminateApplication();
                return;
            }
        } else if (!isOneShotVideo() && !isOneShotPhoto()) {
            if (CamLog.VERBOSE) {
                CamLog.e("SomcCamera not support multi window mode, intent :[" + intent + "]");
            }
            terminateApplication();
            return;
        }
        intent.addFlags(33554432);
        if (CamLog.VERBOSE) {
            CamLog.d("Start multi window activity, intent : " + intent);
        }
        startActivity(intent);
        terminateApplication();
    }

    private void requestInflateUiComponents() {
        this.mViewFinder.requestInflate(getLayoutInflater());
    }

    public boolean awaitViewFinderReady() throws ExecutionException, InterruptedException, TimeoutException {
        if (this.mViewFinderInitializationTaskFuture == null) {
            return true;
        }
        boolean future = getFuture(this.mViewFinderInitializationTaskFuture);
        this.mViewFinderInitializationTaskFuture = null;
        if (!future) {
            return future;
        }
        this.mViewFinder.attachToWindow();
        return future;
    }

    private final class ThermalAlertReceiverOnResumeTask implements Runnable {
        private ThermalAlertReceiverOnResumeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.mThermalAlertReceiver.onResume();
            CameraActivity.this.mBatteryChangedReceiver.onResume();
        }
    }

    private boolean awaitThermalAlertReceiverReady() {
        return getFuture(this.mThermalAlertReceiverReadyTaskFuture);
    }

    public boolean isAlreadyHighTemperature() {
        if (awaitThermalAlertReceiverReady()) {
            return this.mThermalAlertReceiver.isAlreadyHighTemperature();
        }
        return false;
    }

    public boolean isThermalWarningState() {
        if (awaitThermalAlertReceiverReady()) {
            return this.mThermalAlertReceiver.isWarningState();
        }
        return false;
    }

    public boolean isThermalWarningExtraState() {
        if (awaitThermalAlertReceiverReady()) {
            return this.mThermalAlertReceiver.isWarningExtraState();
        }
        return false;
    }

    public boolean isThermalWarningReceived() {
        if (awaitThermalAlertReceiverReady()) {
            return this.mThermalAlertReceiver.isThermalWarningReceived();
        }
        return false;
    }

    public void requestPostLazyInitializationTaskExecute() {
        this.mPostDeviceInitializationTask = new PostDeviceInitializationTask(this);
        this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, false);
        if (this.mLaunchCondition.getExtraOperation() == LaunchCondition.ExtraOperation.LAUNCH_AND_CAPTURE) {
            postDelayedEvent(this.mPostDeviceInitializationTask, 100L);
        } else {
            postEvent(this.mPostDeviceInitializationTask);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postEvent(Runnable runnable) {
        CameraApplication.getUiThreadHandler().post(runnable);
    }

    public void postDelayedEvent(Runnable runnable, long j) {
        CameraApplication.getUiThreadHandler().postDelayed(runnable, j);
    }

    private void cancelDelayedEvent(Runnable runnable) {
        CameraApplication.getUiThreadHandler().removeCallbacks(runnable);
    }

    class LazyInitializationTask implements Runnable {
        LazyInitializationTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CameraActivity.this.mStateMachine == null || CameraActivity.this.mCameraDeviceHandler == null || CameraActivity.this.mViewFinder == null || !CameraActivity.this.mViewFinder.isHeadUpDisplayReady()) {
                retry();
                return;
            }
            if (CameraActivity.this.mStateMachine.canHandleAsynchronizedTask()) {
                CameraActivity.this.requestFinishCameraActivity();
                CameraActivity.this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, new Object[0]);
                CameraActivity.this.mIsLazyInitializationRunning = false;
                CameraActivity.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, Boolean.valueOf(CameraActivity.this.mStateMachine.isSettingChangeAcceptable()));
                return;
            }
            retry();
        }

        private void retry() {
            CameraActivity.this.postDelayedEvent(this, 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestFinishCameraActivity() {
        if (CamLog.VERBOSE) {
            CamLog.d("requestFinishCameraActivity()");
        }
        if (shouldShowWhenLocked()) {
            sendBroadcast(new Intent(IntentConstants.BroadcastIntent.ACTION_CAMERA_FINISH_CAMERAACTIVITY));
        }
    }

    private class PostDeviceInitializationTask implements Runnable {
        private final CameraActivity mActivity;

        PostDeviceInitializationTask(CameraActivity cameraActivity) {
            this.mActivity = cameraActivity;
            CameraActivity.this.mIsLazyInitializationRunning = true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CameraActivity.this.mStateMachine != null && CameraActivity.this.mCameraDeviceHandler != null && CameraActivity.this.mViewFinder != null) {
                if (CameraActivity.this.mCameraDeviceHandler.isPreScanOnGoing() || CameraActivity.this.mCameraDeviceHandler.isPreCaptureOnGoing()) {
                    retry();
                    return;
                }
                if (CameraActivity.this.mStateMachine.canHandleAsynchronizedTask()) {
                    UserSettings userSetting = CameraActivity.this.mStateMachine.getUserSetting();
                    if (((CapturingMode) userSetting.get(UserSettingKey.CAPTURING_MODE)).isVideo()) {
                        CameraActivity.this.mCameraDeviceHandler.setTorchAndCommit(((PhotoLight) userSetting.get(UserSettingKey.PHOTO_LIGHT)).getBooleanValue());
                    } else if (!((CapturingMode) userSetting.get(UserSettingKey.CAPTURING_MODE)).isFront()) {
                        CameraActivity.this.mCameraDeviceHandler.setFlashModeAndCommit((Flash) userSetting.get(UserSettingKey.FLASH));
                    } else {
                        CameraActivity.this.mCameraDeviceHandler.setDisplayFlashModeAndCommit((DisplayFlash) userSetting.get(UserSettingKey.DISPLAY_FLASH));
                    }
                    CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, true);
                    if (CameraActivity.this.mGeotagManager != null) {
                        CameraActivity.this.mGeotagManager.notifyStatus();
                    }
                    CameraActivity.this.mLazyInitializationiTask = CameraActivity.this.new LazyInitializationTask();
                    CameraActivity.this.postEvent(CameraActivity.this.mLazyInitializationiTask);
                    return;
                }
                retry();
                return;
            }
            retry();
        }

        private void retry() {
            CameraActivity.this.postDelayedEvent(this, 100L);
        }
    }

    public void requestSuspend() {
        if (CamLog.VERBOSE) {
            CamLog.d("requestSuspend():[IN]");
        }
        if (moveTaskToBack(true)) {
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("requestSuspend():[FAILED]");
        }
        finish();
    }

    public void requestRestartCameraActivityAfterResetSettings() {
        this.mResetSettingsRequested = true;
        finish();
    }

    public void onPauseTasks() throws IOException {
        PerfLog.ACTIVITY_ON_PAUSE.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : E");
        }
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_PAUSE);
        }
        notifyActivityState(INTENT_SUBJECT_PAUSED);
        LaunchCondition.LaunchTrigger launchTrigger = getLaunchCondition().getLaunchTrigger();
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
        getCameraDevice().setActivityForeground(false);
        awaitSetupAllReady();
        sendBroadcast(new Intent(IntentConstants.BroadcastIntent.ACTION_CAMERA_FINISH));
        if (PermissionsUtil.arePermissionsGranted(getApplicationContext(), this.REQUESTED_PERMISSIONS)) {
            doPause();
        } else {
            releaseCamera();
            this.mCurrentCameraSessionId = null;
            this.mViewFinder.clearMessageDialog();
        }
        this.mKeyEventDispatcher.stop();
        this.mSideTouchEventDispatcher.stop();
        if (CamLog.VERBOSE) {
            logLifeCycleOut(TAG, LifeCycleIds.ON_PAUSE);
        }
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : X");
        }
        PerfLog.ACTIVITY_ON_PAUSE.end();
    }

    private void doPause() {
        CapturingMode capturingMode;
        getCameraDevice().removeOnPreviewStartedListener();
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, true);
        if (this.mCameraDeviceHandler != null && (capturingMode = (CapturingMode) this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE)) != null) {
            this.mCameraDeviceHandler.savePreloadSettings(capturingMode, this.mStoredSettings.getUserSettings(), this.mStoredSettings.getLastSettings(), isOneShot());
        }
        if (this.mResetSettingsRequested) {
            this.mStoredSettings.clearAllSettings(getStorage());
        }
        releaseCamera();
        this.mCurrentCameraSessionId = null;
        this.mBackgroundWorker.execute(new ThermalAlertReceiverOnPauseTask());
        if (this.mGeotagManager != null) {
            this.mGeotagManager.releaseResource();
        }
        if (this.mPostDeviceInitializationTask != null) {
            cancelDelayedEvent(this.mPostDeviceInitializationTask);
        }
        if (this.mLazyInitializationiTask != null) {
            cancelDelayedEvent(this.mLazyInitializationiTask);
        }
        this.mStateMachine.releaseContentsViewController();
        if (getWearableBridge() != null) {
            getWearableBridge().getLifeCycleNotifier().onPause();
        }
        if (this.mIsReceiverResistered) {
            this.mIsReceiverResistered = false;
        }
        disableAutoPowerOffTimer();
        getWindow().clearFlags(128);
        unmuteSound();
        this.mViewFinder.clearMessageDialog();
        disableOrientation();
        ResearchUtil.getInstance().onPause(false);
        if (this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE) != null) {
            this.mLaunchCondition.setCapturingMode((CapturingMode) this.mStoredSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE));
        }
        this.mLaunchCondition.onPause();
        this.mBackgroundWorker.execute(new StartGyroCalibrationOnPauseTask());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseCamera() {
        if (this.mCameraDeviceHandler != null) {
            this.mCameraDeviceHandler.disableFpsLimitation();
            this.mCameraDeviceHandler.closeCamera();
            this.mCameraDeviceHandler.setStateMachine(null);
            this.mCameraDeviceHandler = null;
            return;
        }
        getCameraDevice().closeCamera();
    }

    private final class ThermalAlertReceiverOnPauseTask implements Runnable {
        private ThermalAlertReceiverOnPauseTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.mThermalAlertReceiver.onPause();
            LocalResearchUtil.getInstance().setMeasurementThermal(false);
            CameraActivity.this.mBatteryChangedReceiver.onPause();
        }
    }

    @Override // android.app.Activity
    public void onStop() throws IOException {
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_STOP);
        }
        PerfLog.ACTIVITY_ON_STOP.begin();
        super.onStop();
        if (PlatformCapability.hasDeviceError()) {
            finishAndRemoveTask();
            Process.killProcess(Process.myPid());
            return;
        }
        if (PlatformCapability.isPrepared()) {
            if (this.mResetSettingsRequested) {
                Intent launchIntentForPackage = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                launchIntentForPackage.addFlags(268468224);
                startActivity(launchIntentForPackage, null);
                this.mResetSettingsRequested = false;
            }
            PerfLog.ACTIVITY_ON_STOP.end();
            if (CamLog.VERBOSE) {
                logLifeCycleOut(TAG, LifeCycleIds.ON_STOP);
            }
        }
    }

    @Override // android.app.Activity
    public void onDestroy() throws IOException {
        PerfLog.ACTIVITY_ON_DESTROY.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : E");
        }
        if (CamLog.VERBOSE) {
            logLifeCycleIn(TAG, LifeCycleIds.ON_DESTROY);
        }
        super.onDestroy();
        if (PlatformCapability.isPrepared()) {
            this.mLayoutOrientationChangedListenerSet.clear();
            ResearchUtil.getInstance().onDestroy();
            if (CamLog.VERBOSE) {
                CamLog.d("onDestroy():[IN]");
            }
            unregisterForceExitRequestReceiver();
            getDownAll();
            this.mIsCalledOnDestroy = false;
            this.mBackgroundWorker.execute(new ThermalAlertReceiverOnDestroyTask());
            this.mBackgroundWorker.shutdown();
            try {
                if (!this.mBackgroundWorker.awaitTermination(1000L, TimeUnit.MILLISECONDS)) {
                    synchronized (this.mThermalAlertReceiver) {
                        if (!this.mIsCalledOnDestroy) {
                            this.mIsCalledOnDestroy = true;
                            this.mThermalAlertReceiver.onDestroy();
                            this.mBatteryChangedReceiver.onDestroy();
                        }
                    }
                }
            } catch (InterruptedException unused) {
                CamLog.e("mBackgroundWorker.shutdown is Timeout.");
                synchronized (this.mThermalAlertReceiver) {
                    if (!this.mIsCalledOnDestroy) {
                        this.mIsCalledOnDestroy = true;
                        this.mThermalAlertReceiver.onDestroy();
                        this.mBatteryChangedReceiver.onDestroy();
                    }
                }
            }
            if (PlatformCapability.isWearableSupported()) {
                releaseWearableFramework();
            }
            if (CamLog.VERBOSE) {
                logLifeCycleOut(TAG, LifeCycleIds.ON_DESTROY);
            }
            if (CamLog.DEBUG) {
                CamLog.d("onDestroy() : X");
            }
            PerfLog.ACTIVITY_ON_DESTROY.end();
        }
    }

    private final class ThermalAlertReceiverOnDestroyTask implements Runnable {
        private ThermalAlertReceiverOnDestroyTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraActivity.this.mThermalAlertReceiver) {
                if (!CameraActivity.this.mIsCalledOnDestroy) {
                    CameraActivity.this.mIsCalledOnDestroy = true;
                    CameraActivity.this.mThermalAlertReceiver.onDestroy();
                    CameraActivity.this.mBatteryChangedReceiver.onDestroy();
                }
            }
        }
    }

    private void getDownAll() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINALIZE, new Object[0]);
        this.mStoredSettings.getUserSettings().release();
        this.mStateMachine = null;
        this.mViewFinder = null;
        this.mAutoPowerOffTimer = null;
        this.mUserEventHandler.release();
        this.mUserEventHandler = null;
        releaseSoundPlayer();
        if (this.mGeotagManager != null) {
            this.mGeotagManager.release();
        }
        unregisterReceiver(this.mScreenOffReceiver);
        this.mScreenOffReceiver = null;
        this.mThermalAlertReceiverReadyTaskFuture = null;
        this.mViewFinderInitializationTaskFuture = null;
        this.mSetupAllTaskFuture = null;
        this.mShutDownReceiver = null;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (this.mKeyEventDispatcher.sendKeyLongPress(keyEvent)) {
            return true;
        }
        return super.onKeyLongPress(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (CamLog.DEBUG) {
            CamLog.d("KeyEvent.getEventTime() = " + keyEvent.getEventTime());
        }
        if (CamLog.DEBUG) {
            CamLog.d("CameraActivity.onKeyDown() : KEYCODE=" + i);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyDown():[IN]");
        }
        if (isFinishing() || this.mKeyEventDispatcher.sendKeyDown(keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (CamLog.DEBUG) {
            CamLog.d("KeyEvent.getEventTime() = " + keyEvent.getEventTime());
        }
        if (CamLog.DEBUG) {
            CamLog.d("CameraActivity.onKeyUp() : KEYCODE=" + i);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyUp():[IN]");
        }
        if (isFinishing() || this.mKeyEventDispatcher.sendKeyUp(keyEvent)) {
            return true;
        }
        if (i == 4) {
            abort();
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void playSound(@NonNull SoundPlayer.Type type) {
        if (this.mSoundPlayer == null) {
            return;
        }
        this.mSoundPlayer.play(type);
    }

    public void stopPlayingSound() {
        if (this.mSoundPlayer != null) {
            this.mSoundPlayer.stop();
        }
    }

    private void releaseSoundPlayer() {
        if (this.mSoundPlayer != null) {
            this.mSoundPlayer.release();
            this.mSoundPlayer = null;
        }
    }

    public void abort() {
        if (CamLog.VERBOSE) {
            CamLog.d("call abort()");
        }
        abort(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abort(boolean z) {
        if (this.mStateMachine != null) {
            if (z) {
                if (isDeviceInSecurityLock() || isInLockTaskMode()) {
                    finish();
                    return;
                } else {
                    if (isOneShot()) {
                        if (this.mReturnOneShotResult) {
                            setResult(0);
                        }
                        finishUrgently();
                        return;
                    }
                    requestSuspend();
                    return;
                }
            }
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, false);
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCancel finish()");
        }
        finish();
    }

    class RequestLaunchAdvancedCameraTask implements Runnable {
        private static final String TAG = "RequestLaunchAdvancedCameraTask";
        private final LaunchCondition.ExtraOperation mExtraOperation;
        private final CapturingMode mRequestMode;
        private final String mUserSettingKeyName;

        private RequestLaunchAdvancedCameraTask(CapturingMode capturingMode, LaunchCondition.ExtraOperation extraOperation, String str) {
            this.mRequestMode = capturingMode;
            this.mExtraOperation = extraOperation;
            this.mUserSettingKeyName = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraActivity.this.abort(false);
            Intent intent = new Intent();
            intent.setClass(CameraActivity.this.getApplicationContext(), CameraActivity.class);
            intent.addFlags(268435456);
            intent.putExtra(LaunchCondition.EXTRA_LAUNCHED_BY_FAST_CAPTURING, true);
            if (AnonymousClass12.$SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[this.mExtraOperation.ordinal()] == 1) {
                intent.putExtra(LaunchCondition.EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_OPEN, true);
                intent.putExtra(LaunchCondition.EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_KEY, this.mUserSettingKeyName);
            }
            switch (this.mRequestMode) {
                case SCENE_RECOGNITION:
                    intent.setAction(CapturingMode.SCENE_RECOGNITION.getValue());
                    break;
                case NORMAL:
                    intent.setAction(CapturingMode.NORMAL.getValue());
                    break;
                case FRONT_PHOTO:
                    intent.setAction(CapturingMode.FRONT_PHOTO.getValue());
                    break;
                case SUPERIOR_FRONT:
                    intent.setAction(LaunchCondition.ACTION_FRONT_STILL_IMAGE_CAMERA);
                    break;
                case VIDEO:
                    intent.setAction("android.media.action.VIDEO_CAMERA");
                    break;
                case FRONT_VIDEO:
                    intent.setAction(LaunchCondition.ACTION_FRONT_VIDEO_CAMERA);
                    break;
                case SLOW_MOTION:
                    intent.setAction(CapturingMode.SLOW_MOTION.getValue());
                    break;
                default:
                    intent.setAction(CapturingMode.SCENE_RECOGNITION.getValue());
                    break;
            }
            if (CommonUtility.isActivityAvailable(CameraActivity.this.getApplicationContext(), intent)) {
                try {
                    CameraActivity.this.startActivity(intent, ActivityOptions.makeCustomAnimation(CameraActivity.this, 0, 0).toBundle());
                    CameraActivity.this.overridePendingTransition(0, 0);
                } catch (ActivityNotFoundException unused) {
                    if (CamLog.VERBOSE) {
                        CamLog.e(".onClick():[activity is not found error]");
                    }
                }
            }
            CameraActivity.this.requestSuspend();
        }
    }

    public void requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation extraOperation, String str) {
        CameraApplication.getUiThreadHandler().post(new RequestLaunchAdvancedCameraTask(this.mStateMachine.getCurrentCapturingMode(), extraOperation, str));
    }

    public boolean isDeviceInSecurityLock() {
        Bundle extras = getIntent().getExtras();
        return (INTENT_SUBJECT_START_SECURE.equals(extras != null ? extras.getString("android.intent.extra.SUBJECT") : null) || this.mLaunchCondition.isSecurePhotoLaunchedByIntent()) && ((KeyguardManager) getSystemService("keyguard")).isKeyguardLocked();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
        CapturingMode capturingMode = this.mLaunchCondition.getCapturingMode();
        this.mLaunchCondition.setup(intent, this.mStoredSettings.getLastSettings().getCapturingMode(), this instanceof InternalCameraActivity);
        notifyActivityState(INTENT_SUBJECT_RESUMED);
        LaunchCondition.LaunchTrigger launchTrigger = getLaunchCondition().getLaunchTrigger();
        if (launchTrigger == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        }
        if (!isLaunchedFromLocked(intent) && launchTrigger != LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE);
        }
        if (this.mIsColdBoot && capturingMode != this.mLaunchCondition.getCapturingMode()) {
            this.mIsNeedToCloseBypassCameraBecauseModeChanged = true;
        }
        this.mCanFinishByScreenOff = false;
        setIntent(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyActivityState(String str) {
        Intent intent = new Intent("android.intent.action.CAMERA_BUTTON", (Uri) null);
        intent.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 27));
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.addFlags(268435456);
        intent.setPackage(getPackageName());
        getApplicationContext().sendOrderedBroadcast(intent, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRecording() {
        if (this.mStateMachine != null) {
            return this.mStateMachine.isRecording();
        }
        return false;
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

    private class WearableBridgeLifeCycleObserver implements ObserveWearableInterface.LifeCycleObserver {
        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface.LifeCycleObserver
        public void onPause() {
        }

        private WearableBridgeLifeCycleObserver() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface.LifeCycleObserver
        public void onResume() {
            if (CameraActivity.this.mStateMachine.canHandleWearableCaptureRequest()) {
                CameraActivity.this.notifyStateIdleToWearable();
            } else {
                CameraActivity.this.notifyStateBlockedToWearable();
            }
        }
    }

    private class WearableBridgePhotoEventObserver implements ObserveWearableInterface.PhotoEventObserver {
        private WearableBridgePhotoEventObserver() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface.PhotoEventObserver
        public void onPhotoCaptureRequested() {
            if (CameraActivity.this.mStateMachine.canHandleWearableCaptureRequest()) {
                CameraActivity.this.restartAutoPowerOffTimer();
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_READY, new Object[0]);
                CameraActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE, new Object[0]);
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.WEARABLE);
                return;
            }
            WearableBridgeClient wearableBridge = CameraActivity.this.getWearableBridge();
            if (wearableBridge != null) {
                wearableBridge.getPhotoStateNotifier().onCaptureFailed();
            }
        }
    }

    private static class WearableBridgeVideoEventObserver implements ObserveWearableInterface.VideoEventObserver {
        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface.VideoEventObserver
        public void onStartVideoRecRequested() {
        }

        @Override // com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface.VideoEventObserver
        public void onStopVideoRecRequested() {
        }

        private WearableBridgeVideoEventObserver() {
        }
    }

    @Nullable
    public WearableBridgeClient getWearableBridge() {
        if (isOneShot()) {
            return null;
        }
        return this.mWearableBridgeClient;
    }

    public void notifyStateBlockedToWearable() {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStateBlockedToWearable()");
        }
        if (getWearableBridge() != null) {
            getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.BLOCKED);
            getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.BLOCKED);
        }
    }

    public void notifyStateIdleToWearable() {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStateIdleToWearable()");
        }
        if (getWearableBridge() != null) {
            getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.IDLE);
            getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.IDLE);
        }
    }

    public CameraDeviceHandler getCameraDevice() {
        return ((CameraApplication) getApplicationContext()).getCameraDevice();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (PlatformCapability.isPrepared() && z && this.mViewFinder != null) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY, new Object[0]);
        }
    }

    public boolean isLazyInitializationRunning() {
        return this.mIsLazyInitializationRunning;
    }

    public void finishAndKillProcess() {
        prepareFinish();
        finishAndRemoveTask();
    }

    private void prepareFinish() {
        if (this.mStateMachine != null) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, false);
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (CamLog.VERBOSE) {
            CamLog.d("onActivityResult: requestCode: " + i + ", resultCode: " + i2);
        }
        super.onActivityResult(i, i2, intent);
        if (PlatformCapability.isPrepared()) {
            if (isDeviceInSecurityLock()) {
                ((ViewFinderImpl) this.mViewFinder).reconstructLocalCache();
            }
            if (this.mActivityResultListeners != null) {
                OnActivityResultListener onActivityResultListener = this.mActivityResultListeners.get(i);
                if (onActivityResultListener != null && onActivityResultListener.onActivityResult(i, i2, intent)) {
                    this.mActivityResultListeners.remove(i);
                }
                if (this.mActivityResultListeners.size() == 0) {
                    this.mActivityResultListeners = null;
                }
            }
            this.mLaunchCondition.onActivityResult(i, intent);
            switch (i) {
                case 16:
                case 17:
                    addExternalCameraAppContent(intent);
                    ResearchUtil.getInstance().sendDualCameraEffectEvent(getApplicationContext());
                    if (i2 == 2) {
                        finish();
                        break;
                    }
                    break;
                case 18:
                    addExternalCameraAppContent(intent);
                    ResearchUtil.getInstance().sendPortraitSelfieEvent(getApplicationContext(), LocalResearchUtil.getInstance().getModeName(this.mLaunchCondition.getCapturingMode()));
                    if (i2 == 2) {
                        finish();
                        break;
                    }
                    break;
                case 19:
                    if (i2 == 2) {
                        finish();
                        break;
                    }
                    break;
                case 20:
                    this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_SD_PERMISSION_DISPLAY_FINISHED, new Object[0]);
                    if (i2 == 0) {
                        this.mViewFinder.showMessageDialog(DialogId.SD_CARD_PERMISSION_UNAVAILABLE, new Object[0]);
                        break;
                    }
                    break;
            }
        }
    }

    private void addExternalCameraAppContent(Intent intent) {
        ArrayList parcelableArrayListExtra;
        if (intent == null || !isDeviceInSecurityLock() || (parcelableArrayListExtra = intent.getParcelableArrayListExtra("com.sonyericsson.android.camera.extra.CAPTURED_URIS")) == null || parcelableArrayListExtra.isEmpty()) {
            return;
        }
        this.mCreateContentInfoTaskFuture = this.mBackgroundWorker.submit(new CreateContentInfoTask(parcelableArrayListExtra));
    }

    private class CreateContentInfoTask implements Runnable {
        ArrayList<Uri> mCapturedUris;

        CreateContentInfoTask(ArrayList<Uri> arrayList) {
            this.mCapturedUris = new ArrayList<>();
            this.mCapturedUris = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            ((ViewFinderImpl) CameraActivity.this.mViewFinder).requestCreateContentInfoSync(this.mCapturedUris);
        }
    }

    private boolean awaitCreateContentInfoReady() {
        return getFuture(this.mCreateContentInfoTaskFuture);
    }

    private void registerForceExitRequestReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("registerForceExitRequestReceiver()");
        }
        IntentFilter intentFilter = new IntentFilter(CommonConstants.INTENT_ACTION_FORCE_EXIT_REQUEST);
        this.mForceExitRequestReceiver = new ForceExitRequestReceiver();
        registerReceiver(this.mForceExitRequestReceiver, intentFilter);
        if (shouldShowWhenLocked()) {
            return;
        }
        IntentFilter intentFilter2 = new IntentFilter(IntentConstants.BroadcastIntent.ACTION_CAMERA_FINISH_CAMERAACTIVITY);
        this.mCameraActivityFinishReceiver = new CameraActivityFinishBroadcastReceiver(this);
        registerReceiver(this.mCameraActivityFinishReceiver, intentFilter2);
    }

    private void unregisterForceExitRequestReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("unregisterForceExitRequestReceiver()");
        }
        if (this.mForceExitRequestReceiver != null) {
            unregisterReceiver(this.mForceExitRequestReceiver);
            this.mForceExitRequestReceiver = null;
        }
        if (this.mCameraActivityFinishReceiver != null) {
            unregisterReceiver(this.mCameraActivityFinishReceiver);
            this.mCameraActivityFinishReceiver = null;
        }
    }

    private class ForceExitRequestReceiver extends BroadcastReceiver {
        private ForceExitRequestReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (CamLog.VERBOSE) {
                CamLog.d("ForceExitRequestReceiver.onReceive()");
            }
            if (intent != null && CameraActivity.this.getStoredSettings().getMessageSettings().isNeverShow(MessageType.SETUP_WIZARD) && CommonConstants.INTENT_ACTION_FORCE_EXIT_REQUEST.equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("ForceExitRequestReceiver() Force Exit");
                }
                CameraActivity.this.finish();
            }
        }
    }

    private static class CameraActivityFinishBroadcastReceiver extends BroadcastReceiver {
        final WeakReference<CameraActivity> mCameraActivityRef;

        CameraActivityFinishBroadcastReceiver(CameraActivity cameraActivity) {
            this.mCameraActivityRef = new WeakReference<>(cameraActivity);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            CameraActivity cameraActivity = this.mCameraActivityRef.get();
            if (cameraActivity == null || cameraActivity.isFinishing()) {
                return;
            }
            cameraActivity.abort();
        }
    }

    private final class StartGyroCalibrationOnPauseTask implements Runnable {
        private StartGyroCalibrationOnPauseTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent intent = new Intent(CameraActivity.ACTION_REQUEST_SOMC_CAMERA_SERVICE);
            intent.setPackage("com.sonymobile.cameracommon");
            intent.putExtra("android.intent.extra.SUBJECT", RequestTypeForSomcCameraService.GYRO_CALIBRATION.mExtraValue);
            CameraActivity.this.getApplicationContext().startService(intent);
        }
    }

    private final class ScreenOffTask implements Runnable {
        private ScreenOffTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent intent = new Intent(CameraActivity.ACTION_REQUEST_SOMC_CAMERA_SERVICE);
            intent.setPackage("com.sonymobile.cameracommon");
            intent.putExtra("android.intent.extra.SUBJECT", RequestTypeForSomcCameraService.SCREEN_OFF.mExtraValue);
            CameraActivity.this.getApplicationContext().startService(intent);
        }
    }

    private void createLaunchCondition() {
        this.mLaunchCondition = new LaunchCondition(new IntentReader().getVideoQualityConfigurations(getIntent()));
    }

    @Deprecated
    public StoredSettings getStoredSettings() {
        return this.mStoredSettings;
    }

    @Override // android.app.Activity
    public void finish() {
        if (!isInLockTaskMode()) {
            prepareFinish();
        }
        super.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishUrgently() {
        prepareFinish();
        super.finish();
    }

    public LaunchCondition getLaunchCondition() {
        return this.mLaunchCondition;
    }

    private void createBatteryChangedReceiver() {
        this.mBatteryChangedReceiver = new BatteryChangedReceiver(this, this.mBatteryChangedListener);
    }

    @Override // android.app.Activity
    public final void onResume() throws IOException {
        this.mIsColdBoot = false;
        if (!PlatformCapability.isPrepared()) {
            super.onResume();
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onResume(): isKeyguardLocked() = " + isKeyguardLocked());
        }
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(getMainLooper());
        }
        this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        CameraApplication.getUiThreadHandler().removeCallbacksAndMessages(getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken());
        getCameraDevice().setIsInShutdownNow(false);
        registerShutDownReceiver();
        if (isKeyguardLocked() || this.mSkippedOnResume) {
            this.mSkippedOnResume = true;
            long j = isKeyguardSecure() ? ON_RESUME_DELAY_SECURE_MILLIS : ON_RESUME_DELAY_NON_SECURE_MILLIS;
            if (CamLog.VERBOSE) {
                CamLog.d("onResume() --> postDelayed(mOnResumeTasks," + j + ")");
            }
            this.mMainHandler.postDelayed(this.mOnResumeTasks, j);
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("onResume() --> onResumeTasks()");
            }
            this.mSkippedOnResume = false;
            onResumeTasks();
        }
        super.onResume();
    }

    @Override // android.app.Activity
    public final void onPause() throws IOException {
        if (isRecording()) {
            this.mCameraDeviceHandler.stopAudioRecording();
        }
        if (!PlatformCapability.isPrepared()) {
            super.onPause();
            getCameraDevice().closeCamera();
            finishUrgently();
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onPause(): isKeyguardLocked() = " + isKeyguardLocked());
        }
        if (this.mMainHandler != null) {
            this.mMainHandler.removeCallbacks(this.mOnResumeTasks);
        }
        CameraApplication.getUiThreadHandler().removeCallbacksAndMessages(getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken());
        if (!this.mSkippedOnResume) {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() --> onPauseTasks()");
            }
            onPauseTasks();
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("onPause() --> postAtTime(SendPauseEventAndReleaseCameraTask,500)");
            }
            CameraApplication.getUiThreadHandler().postAtTime(this.mSendPauseEventAndReleaseCameraTask, getCameraDevice().getSendPauseEventAndReleaseCameraTaskToken(), SystemClock.uptimeMillis() + SEND_PAUSE_EVENT_DELAY_MILLIS);
        }
        unRegisterShutDownReceiver();
        super.onPause();
    }

    public final boolean isStillImageCamera() {
        Intent intent = getIntent();
        return intent != null && "android.media.action.STILL_IMAGE_CAMERA".equals(intent.getAction());
    }

    private void muteSound() {
        AudioManager audioManager;
        if (Build.VERSION.SDK_INT < 8 || (audioManager = (AudioManager) getSystemService("audio")) == null) {
            return;
        }
        if (audioManager.requestAudioFocus(null, 3, 1) == 1) {
            if (CamLog.VERBOSE) {
                CamLog.d("muteSound: success");
            }
        } else if (CamLog.VERBOSE) {
            CamLog.d("muteSound: fail");
        }
    }

    private void unmuteSound() {
        AudioManager audioManager;
        if (Build.VERSION.SDK_INT < 8 || (audioManager = (AudioManager) getSystemService("audio")) == null) {
            return;
        }
        if (audioManager.abandonAudioFocus(null) == 1) {
            if (CamLog.VERBOSE) {
                CamLog.d("unmuteSound: success");
            }
        } else if (CamLog.VERBOSE) {
            CamLog.d("unmuteSound: fail");
        }
    }

    public void pauseAudioPlayback() {
        Intent intent = new Intent(IntentConstants.BroadcastIntent.ACTION_MUSICSERVICE_COMMAND);
        intent.putExtra(IntentConstants.BroadcastIntent.MUSICSERVICE_COMMAND, IntentConstants.BroadcastIntent.MUSICSERVICE_COMMAND_PAUSE);
        sendBroadcast(intent);
        muteSound();
    }

    public final void enableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableAutoPowerOffTimer: ");
        }
        this.mAutoPowerOffTimer.enableAutoPowerOffTimer();
    }

    public final void disableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableAutoPowerOffTimer: ");
        }
        this.mViewFinder.hideAutoPowerOffHintText();
        this.mAutoPowerOffTimer.disableAutoPowerOffTimer();
    }

    public final void restartAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("restartAutoPowerOffTimer: ");
        }
        if (this.mAutoPowerOffTimer == null) {
            return;
        }
        postEvent(new Runnable() { // from class: com.sonyericsson.android.camera.CameraActivity.10
            @Override // java.lang.Runnable
            public void run() {
                if (CameraActivity.this.mViewFinder != null) {
                    CameraActivity.this.mViewFinder.hideAutoPowerOffHintText();
                }
            }
        });
        this.mAutoPowerOffTimer.restartAutoPowerOffTimer();
    }

    public GeotagManager getGeoTagManager() {
        return this.mGeotagManager;
    }

    private void enableOrientation() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableOrientation()");
        }
        if (this.mOrientationEventListener == null) {
            this.mOrientationEventListener = new ExtendedOrientationEventListener(this);
            this.mOrientationEventListener.enable();
        }
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

    public void addOrienationListener(LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.add(layoutOrientationChangedListener);
    }

    public void removeOrienationListener(LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.remove(layoutOrientationChangedListener);
    }

    private class ExtendedOrientationEventListener extends OrientationEventListener {
        public ExtendedOrientationEventListener(Context context) {
            super(context);
        }

        @Override // android.view.OrientationEventListener
        public void onOrientationChanged(int i) {
            if (i != -1) {
                CameraActivity.this.mSensorOrientationDegree = i;
            }
            if (i == CameraActivity.this.mLastOrientationDegree) {
                return;
            }
            CameraActivity.this.mLastOrientationDegree = i;
            if (CameraActivity.this.mLastOrientationDegree != -1) {
                CameraActivity.this.mLastDeterminedOrientationDegree = CameraActivity.this.mLastOrientationDegree;
            }
            CameraActivity.this.notifyLayoutOrientationChanged(CameraActivity.this.getLayoutOrientation());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyLayoutOrientationChanged(LayoutOrientation layoutOrientation) {
        if (layoutOrientation == this.mLastDetectedOrientation || layoutOrientation == LayoutOrientation.Unknown) {
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("ORIENTATION : Layout orientation(sensor) is changed from " + this.mLastDetectedOrientation + " to " + layoutOrientation);
        }
        this.mLastDetectedOrientation = layoutOrientation;
        Iterator<LayoutOrientationChangedListener> it = this.mLayoutOrientationChangedListenerSet.iterator();
        while (it.hasNext()) {
            it.next().onLayoutOrientationChanged(this.mLastDetectedOrientation);
        }
    }

    public LayoutOrientation getLayoutOrientation() {
        int i = this.mLastOrientationDegree;
        if (i == -1) {
            i = this.mLastDeterminedOrientationDegree;
        }
        if (i == -1) {
            return LayoutOrientation.Unknown;
        }
        int mountAngle = (i + (360 - ProductConfig.getMountAngle(this))) % 360;
        int i2 = this.mLastDetectedOrientation == LayoutOrientation.Portrait || this.mLastDetectedOrientation == LayoutOrientation.ReversePortrait ? 60 : 30;
        int i3 = 90 + i2;
        if (in(mountAngle, 90 - i2, i3)) {
            return LayoutOrientation.Portrait;
        }
        int i4 = 270 - i2;
        if (in(mountAngle, i3, i4)) {
            return LayoutOrientation.ReverseLandscape;
        }
        if (in(mountAngle, i4, 270 + i2)) {
            return LayoutOrientation.ReversePortrait;
        }
        return LayoutOrientation.Landscape;
    }

    private void setLayoutOrientation(LayoutOrientation layoutOrientation) {
        int orientationDegree = getOrientationDegree(layoutOrientation);
        this.mSensorOrientationDegree = orientationDegree;
        this.mLastOrientationDegree = orientationDegree;
        this.mLastDeterminedOrientationDegree = orientationDegree;
    }

    private int getOrientationDegree(LayoutOrientation layoutOrientation) {
        int i;
        switch (layoutOrientation) {
            case Unknown:
            case Landscape:
                i = 0;
                break;
            case Portrait:
                i = 90;
                break;
            case ReverseLandscape:
                i = 180;
                break;
            case ReversePortrait:
                i = 270;
                break;
            default:
                return -1;
        }
        return (i + ProductConfig.getMountAngle(this)) % 360;
    }

    public int getOrientation() {
        LayoutOrientation layoutOrientation = this.mLastDetectedOrientation;
        if (layoutOrientation == LayoutOrientation.Unknown) {
            layoutOrientation = getLayoutOrientation();
        }
        switch (layoutOrientation) {
            case Landscape:
            case ReverseLandscape:
            case ReversePortrait:
                return 2;
            case Portrait:
                return 1;
            default:
                switch (LayoutOrientationResolver.getInstance().getOrientation()) {
                    case PORTRAIT:
                        return 1;
                    case LANDSCAPE:
                        return 2;
                    default:
                        return 0;
                }
        }
    }

    public int getSensorOrientationDegree() {
        if (CamLog.VERBOSE) {
            CamLog.d("ORIENTATION:getSensorOrientationDegree = " + this.mSensorOrientationDegree);
        }
        return this.mSensorOrientationDegree;
    }

    public LayoutOrientation getLastDetectedOrientation() {
        return this.mLastDetectedOrientation;
    }

    public Storage getStorage() {
        return this.mStorage;
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

    public Uri getExtraOutput() {
        return this.mLaunchCondition.getExtraOutput();
    }

    public boolean hasExtraOutputPath() {
        if (this.mLaunchCondition.getExtraOutput() == null) {
            return false;
        }
        return this.mStorage.checkValidUri(this.mLaunchCondition.getExtraOutput());
    }

    public boolean shouldAddToMediaStore() {
        return this.mLaunchCondition.getAddToMediaStore();
    }

    public boolean isGpsLocationAllowed() {
        return this.mLocationSettingsReader.getIsGpsLocationAllowed();
    }

    public boolean isNetworkLocationAllowed() {
        return this.mLocationSettingsReader.getIsNetworkLocationAllowed();
    }

    private void logLifeCycleIn(String str, LifeCycleIds lifeCycleIds) {
        MeasurePerformance.measureTime(lifeCycleIds.mPerformanceIds, true);
        MeasurePerformance.measureResource("Start " + lifeCycleIds);
        if (lifeCycleIds == LifeCycleIds.ON_CREATE || lifeCycleIds == LifeCycleIds.ON_DESTROY) {
            if (CamLog.VERBOSE) {
                CamLog.d(str, lifeCycleIds + " is called:" + hashCode());
                return;
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d(str, lifeCycleIds + " is called");
        }
    }

    private void logLifeCycleOut(String str, LifeCycleIds lifeCycleIds) throws IOException {
        if (CamLog.VERBOSE) {
            CamLog.d(str, lifeCycleIds + " FINISH.");
        }
        MeasurePerformance.measureResource("End " + lifeCycleIds);
        MeasurePerformance.measureTime(lifeCycleIds.mPerformanceIds, false);
        if (lifeCycleIds == LifeCycleIds.ON_PAUSE) {
            MeasurePerformance.outResult();
        }
    }

    public void readLocationSettings() {
        this.mLocationSettingsReader.readLocationSettings(this);
    }

    @SuppressLint({"NewApi"})
    public boolean isInLockTaskMode() {
        return ((ActivityManager) getSystemService(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY)).getLockTaskModeState() != 0;
    }

    private boolean isLaunchedFromLocked(Intent intent) {
        String stringExtra = intent.getStringExtra(LaunchCondition.LAUNCH_TRIGGER);
        if (stringExtra != null) {
            return LaunchCondition.LaunchTrigger.HW_CAMERA_KEY.toString().equals(stringExtra) || LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK.toString().equals(stringExtra) || LaunchCondition.LaunchTrigger.LOCK_SCREEN.toString().equals(stringExtra);
        }
        return false;
    }

    private boolean addActivityResultListener(int i, OnActivityResultListener onActivityResultListener) {
        if (this.mActivityResultListeners == null) {
            this.mActivityResultListeners = new SparseArray<>();
        }
        if (this.mActivityResultListeners.get(i) != null) {
            return false;
        }
        this.mActivityResultListeners.put(i, onActivityResultListener);
        return true;
    }

    @Override // android.app.Activity
    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        if (isDeviceInSecurityLock()) {
            ((ViewFinderImpl) this.mViewFinder).saveLocalCache();
        }
        super.startActivityForResult(intent, i, bundle);
    }

    public boolean startActivityForResult(Intent intent, int i, OnActivityResultListener onActivityResultListener) {
        if (!addActivityResultListener(i, onActivityResultListener)) {
            return false;
        }
        startActivityForResult(intent, i);
        return true;
    }

    public boolean checkAndRequestSelfPermissions(int i, String[] strArr) {
        return checkAndRequestSelfPermissions(i, strArr, null);
    }

    public boolean checkAndRequestSelfPermissions(int i, final String[] strArr, final PermissionCheckCallback permissionCheckCallback) {
        boolean zCheckAndRequestSelfPermissions = PermissionsUtil.checkAndRequestSelfPermissions(this, i, strArr);
        if (zCheckAndRequestSelfPermissions) {
            addActivityResultListener(i, new OnActivityResultListener() { // from class: com.sonyericsson.android.camera.CameraActivity.11
                @Override // com.sonyericsson.cameracommon.activity.OnActivityResultListener
                public boolean onActivityResult(int i2, int i3, Intent intent) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("RequestPermissionActivity requestCode=" + i2 + ", result=" + i3);
                    }
                    switch (i2) {
                        case 12:
                            if (i3 == -1 && !PermissionsUtil.arePermissionsGranted(CameraActivity.this, strArr)) {
                                CameraActivity.this.finish();
                                break;
                            }
                            break;
                    }
                    if (permissionCheckCallback != null) {
                        permissionCheckCallback.onPermissionChecked(strArr);
                    }
                    return true;
                }
            });
        }
        return zCheckAndRequestSelfPermissions;
    }

    public int getBatteryLevel() {
        return this.mBatteryChangedReceiver.getBatteryLevel();
    }

    public boolean isAlreadyBcl() {
        return this.mBatteryChangedReceiver.isAlreadyBcl();
    }

    private boolean isKeyguardLocked() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) getSystemService("keyguard");
        }
        if (this.mKeyguardManager != null) {
            return this.mKeyguardManager.isKeyguardLocked();
        }
        return false;
    }

    public boolean isKeyguardSecure() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) getSystemService("keyguard");
        }
        if (this.mKeyguardManager != null) {
            return this.mKeyguardManager.isKeyguardSecure();
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (motionEvent.isFromSource(SideTouchUtils.SOURCE_SIDETOUCH) && this.mSideTouchEventDispatcher.send(motionEvent)) {
            return true;
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    public void clearLaunchCameraMode() {
        this.mLaunchCondition.clearLaunchCameraMode();
    }

    private void changeParametersForGoogleAssistant() {
        UserSettings userSettings = this.mStoredSettings.getUserSettings();
        Parameters parameters = userSettings.getParameters();
        if (parameters == null) {
            clearLaunchCameraMode();
        }
        switch (this.mLaunchCondition.getLaunchCameraMode()) {
            case FOUR_K_HDR:
                VideoCodec videoCodec = parameters.getVideoCodec();
                VideoSize videoSize = parameters.getVideoSize();
                if (videoCodec == null || videoSize == null) {
                    clearLaunchCameraMode();
                    break;
                } else {
                    if (!videoSize.is4KVideo()) {
                        if (videoCodec == VideoCodec.H264) {
                            userSettings.set(VideoSize.FOUR_K_UHD_H264);
                        } else if (videoCodec == VideoCodec.H265) {
                            userSettings.set(VideoSize.FOUR_K_UHD_H265);
                        }
                    }
                    userSettings.set(VideoHdr.HDR_ON);
                    break;
                }
            case SUPER_SLOW_MOTION:
                userSettings.set(SlowMotion.SUPER_SLOW_MOTION);
                break;
        }
    }

    /* renamed from: com.sonyericsson.android.camera.CameraActivity$12, reason: invalid class name */
    static /* synthetic */ class AnonymousClass12 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation;

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$LaunchCameraMode[LaunchCondition.LaunchCameraMode.FOUR_K_HDR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$LaunchCameraMode[LaunchCondition.LaunchCameraMode.SUPER_SLOW_MOTION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$LaunchCameraMode[LaunchCondition.LaunchCameraMode.SLOW_MOTION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$sonyericsson$cameracommon$utility$LayoutOrientationResolver$LayoutOrientationType = new int[LayoutOrientationResolver.LayoutOrientationType.values().length];
            try {
                $SwitchMap$com$sonyericsson$cameracommon$utility$LayoutOrientationResolver$LayoutOrientationType[LayoutOrientationResolver.LayoutOrientationType.PORTRAIT.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$utility$LayoutOrientationResolver$LayoutOrientationType[LayoutOrientationResolver.LayoutOrientationType.LANDSCAPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation = new int[LayoutOrientation.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[LayoutOrientation.Unknown.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[LayoutOrientation.Landscape.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[LayoutOrientation.Portrait.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[LayoutOrientation.ReverseLandscape.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$CameraActivity$LayoutOrientation[LayoutOrientation.ReversePortrait.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode = new int[CapturingMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SCENE_RECOGNITION.ordinal()] = 1;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_PHOTO.ordinal()] = 3;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SUPERIOR_FRONT.ordinal()] = 4;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.VIDEO.ordinal()] = 5;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_VIDEO.ordinal()] = 6;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SLOW_MOTION.ordinal()] = 7;
            } catch (NoSuchFieldError unused17) {
            }
            $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation = new int[LaunchCondition.ExtraOperation.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError unused18) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode = new int[ModeSelectorInternalMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.PORTRAIT_SELFIE.ordinal()] = 1;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.DUAL_MONOCHROME.ordinal()] = 2;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS.ordinal()] = 3;
            } catch (NoSuchFieldError unused21) {
            }
        }
    }

    public void enableSideSense() {
        this.mSideTouchEventDispatcher.start();
    }

    public void disableSideSense() {
        this.mSideTouchEventDispatcher.stop();
    }

    private class AutoPowerOffListenerImpl implements AutoPowerOffTimer.AutoPowerOffListener {
        private AutoPowerOffListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.AutoPowerOffTimer.AutoPowerOffListener
        public void onAutoPowerOffWarning() {
            if (CameraActivity.this.mViewFinder == null || CameraActivity.this.isInLockTaskMode()) {
                return;
            }
            CameraActivity.this.mViewFinder.showAutoPowerOffHintText();
        }

        @Override // com.sonyericsson.android.camera.AutoPowerOffTimer.AutoPowerOffListener
        public void onAutoPowerOff(Object obj) {
            if (CameraActivity.this.isInLockTaskMode()) {
                CameraActivity.this.mViewFinder.hideAutoPowerOffHintText();
            }
            Event.AutoPowerOffAction action = null;
            byte b = 0;
            if (obj.equals(AutoPowerOffType.LIFT_TRIGGER.getReason()) || obj.equals(AutoPowerOffType.LIFT_TRIGGER_DIALOG.getReason())) {
                CameraActivity.this.mBackgroundWorker.execute(new ScreenOffTask());
            }
            AutoPowerOffType[] autoPowerOffTypeArrValues = AutoPowerOffType.values();
            int length = autoPowerOffTypeArrValues.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                AutoPowerOffType autoPowerOffType = autoPowerOffTypeArrValues[i];
                if (obj.equals(autoPowerOffType.getReason())) {
                    action = autoPowerOffType.getAction();
                    break;
                }
                i++;
            }
            if (action != null) {
                ResearchUtil.getInstance().sendAutoPowerOffEvent(action);
            } else {
                CamLog.w("AutoPowerOffAction is not found.");
            }
            CameraActivity.this.abort();
        }
    }

    public boolean isAllowToUseLocation() {
        return !isOneShot() || PermissionsUtil.areCallerGeoPermissionsGranted(this);
    }
}
