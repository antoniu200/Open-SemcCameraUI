package com.sonyericsson.android.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.ExtraSettings;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.utility.OneShotUtility;
import com.sonymobile.cameracommon.research.ResearchUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LaunchCondition {
    public static final String ACTION_FRONT_STILL_IMAGE_CAMERA = "com.sonyericsson.android.camera.action.FRONT_STILL_IMAGE_CAMERA";
    public static final String ACTION_FRONT_VIDEO_CAMERA = "com.sonyericsson.android.camera.action.FRONT_VIDEO_CAMERA";
    public static final String ACTION_QUICK_LAUNCH = "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH";
    public static final String ACTION_QUICK_LAUNCH_AND_CAPTURE = "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE";
    public static final String CAMERA_IS_VOICE_INTERACTION_ROOT = "is_voice_interaction_root";
    private static final String CAMERA_LAUNCH_HDR_WITH_4K_RESOLUTION = "HDR_WITH_4K_RESOLUTION";
    private static final String CAMERA_LAUNCH_MANUAL_MODE = "MANUAL_MODE";
    private static final String CAMERA_LAUNCH_SLOW_MOTION = "SLOW_MOTION";
    public static final String CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER = "lift_to_launch_ml";
    public static final String CAMERA_LAUNCH_SOURCE_LOCKSCREEN = "lockscreen_affordance";
    public static final String CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = "power_double_tap";
    private static final String CAMERA_LAUNCH_SUPER_SLOW_MOTION = "SUPER_SLOW_MOTION";
    public static final String EXTRA_CAMERA_LAUNCH_SOURCE = "com.android.systemui.camera_launch_source";
    private static final String EXTRA_CAMERA_MODE = "com.google.assistant.extra.CAMERA_MODE";
    private static final String EXTRA_CAMERA_OPEN_ONLY = "com.google.assistant.extra.CAMERA_OPEN_ONLY";
    public static final String EXTRA_LAUNCHED_BY_ANOTHER_CAMERA = "com.sonyericsson.android.camera3d.extra.launchedByAnotherCamera";
    public static final String EXTRA_LAUNCHED_BY_FAST_CAPTURING = "com.sonyericsson.android.camera.extra.launchedByFastCapturing";
    private static final String EXTRA_LAUNCH_CAMERA_MODE = "android.intent.extra.CAMERA_MODE";
    public static final String EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE = "capturing_mode";
    public static final String EXTRA_LAUNCH_INTERNAL_MODE = "internal_mode";
    public static final String EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_KEY = "com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogkey";
    public static final String EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_OPEN = "com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogopen";
    public static final String EXTRA_REQUEST_STORAGE_SETTINGS_DIALOG_OPEN = "com.sonyericsson.android.camera3d.extra.requststoragesettingsdialogopen";
    private static final String EXTRA_TIMER_DURATION_SECONDS = "com.google.assistant.extra.TIMER_DURATION_SECONDS";
    private static final String EXTRA_USE_FRONT_CAMERA = "com.google.assistant.extra.USE_FRONT_CAMERA";
    private static final String EXTRA_USE_FRONT_CAMERA_MODE = "android.intent.extra.USE_FRONT_CAMERA";
    public static final String LAUNCH_TRIGGER = "com.sonyericsson.android.camera.extra.launchTrigger";
    public static final int RESET_LAUNCH_MODE_TIME_LIMIT_MILLIS = 30000;
    private static final int RESET_LAUNCH_TRIGGER_INTERVAL = 2000;
    private static final String TAG = "LaunchCondition";
    private SharedPreferencesAccessor mAccessor;
    private boolean mAddToMediaStore;
    private Uri mExtraOutput;
    private boolean mIsSecurePhotoLaunchedByIntent;
    private String mUserSettingKeyName;
    private final IntentReader.VideoQualityConfigurations mVideoQualityConfigurations;
    private ExtraOperation mExtraOperation = ExtraOperation.NONE;
    private CapturingMode mCapturingMode = CapturingMode.UNKNOWN;
    private OneShotMode mOneShot = OneShotMode.NONE;
    private Storage.StorageType mStorageTypeForOneshot = Storage.StorageType.INTERNAL;
    private final ExtraSettings mExtraSettings = new ExtraSettings();
    private LaunchTrigger mLaunchTrigger = LaunchTrigger.OTHER;
    private LaunchCameraMode mLaunchCameraMode = LaunchCameraMode.NONE;
    private int mGoogleAssistantSelfTimer = 0;
    private boolean mIsGoogleAssistantLaunchOnly = true;
    private long mCheckStartTimeInMillis = 0;
    private int mInternalModeValue = -1;
    private int mLaunchInternlCallingCapturingModeValue = -1;
    private boolean mIsLaunchedByIntent = false;
    private boolean mIsLaunchedByActivityResult = false;

    public enum ExtraOperation {
        NONE,
        OPEN_SETTINGS_MENU,
        LAUNCH_AND_CAPTURE
    }

    public enum LaunchTrigger {
        LOCK_SCREEN,
        HW_CAMERA_KEY,
        HW_CAMERA_KEY_LOCK,
        HOME,
        ONE_SHOT_APP,
        SETTINGS_SECURE_LOCK,
        POWER_KEY_DOUBLE_TAP,
        ADDONS,
        LIFT_TRIGGER,
        GOOGLE_ASSISTANT,
        APP_SHORTCUT,
        VIEWER,
        VIDEO_EDITOR,
        DUAL_CAMERA_EFFECT,
        PORTRAIT_SELFIE,
        OTHER,
        HISTORY,
        SAME_ACTIVITY,
        MODE_SELECTOR,
        MRU_SHORTCUT
    }

    public enum OneShotMode {
        NONE,
        PHOTO,
        VIDEO;

        public boolean isEnabled() {
            return this != NONE;
        }

        public boolean isPhoto() {
            return this == PHOTO;
        }

        public boolean isVideo() {
            return this == VIDEO;
        }
    }

    public enum LaunchCameraMode {
        NONE,
        FOUR_K_HDR,
        SLOW_MOTION,
        SUPER_SLOW_MOTION;

        public boolean isLaunchedByGoogleAssistant() {
            return this != NONE;
        }

        public boolean isSlowMotion() {
            return this == SLOW_MOTION || this == SUPER_SLOW_MOTION;
        }
    }

    public void onActivityResult(int i, Intent intent) {
        if (i != 14) {
            switch (i) {
                case 8:
                    setLaunchTrigger(LaunchTrigger.VIEWER);
                    updateCheckStartTime();
                    break;
                case 9:
                    setLaunchTrigger(LaunchTrigger.VIEWER);
                    updateCheckStartTime();
                    break;
                default:
                    switch (i) {
                        case 16:
                        case 17:
                            setLaunchTrigger(LaunchTrigger.DUAL_CAMERA_EFFECT);
                            setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                            updateCheckStartTime();
                            break;
                        case 18:
                            setLaunchTrigger(LaunchTrigger.PORTRAIT_SELFIE);
                            setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                            updateCheckStartTime();
                            break;
                        case 19:
                            setLaunchTrigger(LaunchTrigger.ADDONS);
                            setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                            updateCheckStartTime();
                            break;
                    }
            }
        } else {
            setLaunchTrigger(LaunchTrigger.VIDEO_EDITOR);
            updateCheckStartTime();
        }
        this.mIsLaunchedByActivityResult = true;
    }

    public void onRestart(boolean z, boolean z2) {
        clearLaunchTrigger();
        if (this.mIsLaunchedByIntent || this.mIsLaunchedByActivityResult) {
            return;
        }
        if (!(z && z2) && isResetCapturingMode(this.mOneShot.isEnabled())) {
            setCapturingMode(CapturingMode.SCENE_RECOGNITION);
        }
    }

    public LaunchTrigger getLaunchTrigger() {
        return this.mLaunchTrigger;
    }

    private void setLaunchTrigger(LaunchTrigger launchTrigger) {
        this.mLaunchTrigger = launchTrigger;
    }

    public void onResume() {
        clearLaunchTrigger();
    }

    private void clearLaunchTrigger() {
        if (isTimeIntervalBeyondThreshold() && this.mExtraOperation == ExtraOperation.NONE) {
            setLaunchTrigger(LaunchTrigger.OTHER);
        }
    }

    public void onPause() {
        this.mIsLaunchedByIntent = false;
        this.mIsLaunchedByActivityResult = false;
        this.mGoogleAssistantSelfTimer = 0;
        this.mIsGoogleAssistantLaunchOnly = true;
        this.mExtraSettings.clearAll();
    }

    public boolean isSecurePhotoLaunchedByIntent() {
        return this.mIsSecurePhotoLaunchedByIntent;
    }

    private void setIsSecurePhotoLaunchedByIntent(boolean z) {
        this.mIsSecurePhotoLaunchedByIntent = z;
    }

    public Uri getExtraOutput() {
        return this.mExtraOutput;
    }

    private void setExtraOutput(Uri uri) {
        this.mExtraOutput = uri;
    }

    public boolean getAddToMediaStore() {
        return this.mAddToMediaStore;
    }

    private void setAddToMediaStore(boolean z) {
        this.mAddToMediaStore = z;
    }

    public void setup(Intent intent, CapturingMode capturingMode, boolean z) {
        String action;
        if (CamLog.VERBOSE) {
            CamLog.d("setup: " + intent);
        }
        this.mIsLaunchedByIntent = true;
        action = intent.getAction();
        if (action == null) {
            action = "android.intent.action.MAIN";
            intent.setAction("android.intent.action.MAIN");
        }
        setLaunchTrigger(LaunchTrigger.OTHER);
        if (isResetCapturingMode(action == "android.media.action.IMAGE_CAPTURE" || action == "android.media.action.IMAGE_CAPTURE_SECURE" || action == "android.media.action.VIDEO_CAPTURE")) {
            capturingMode = CapturingMode.SCENE_RECOGNITION;
        }
        setIsSecurePhotoLaunchedByIntent(false);
        clearLaunchCameraMode();
        this.mGoogleAssistantSelfTimer = 0;
        this.mIsGoogleAssistantLaunchOnly = true;
        if (CamLog.VERBOSE) {
            CamLog.d("setLaunchMode: action: " + action);
        }
        switch (action) {
            case "android.intent.action.MAIN":
            case "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH":
                if (capturingMode == CapturingMode.UNKNOWN) {
                    setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                } else {
                    setCapturingMode(capturingMode, OneShotMode.NONE);
                    break;
                }
            case "android.media.action.IMAGE_CAPTURE":
                setCapturingMode(getOneShotCapturingMode(capturingMode), OneShotMode.PHOTO);
                break;
            case "android.media.action.IMAGE_CAPTURE_SECURE":
                setIsSecurePhotoLaunchedByIntent(true);
                setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.PHOTO);
                break;
            case "android.media.action.VIDEO_CAPTURE":
                CapturingMode capturingMode2 = CapturingMode.VIDEO;
                if (getOneShotCapturingMode(capturingMode) == CapturingMode.SUPERIOR_FRONT) {
                    capturingMode2 = CapturingMode.FRONT_VIDEO;
                }
                setCapturingMode(capturingMode2, OneShotMode.VIDEO);
                break;
            case "android.media.action.STILL_IMAGE_CAMERA":
                if (isLaunchedByPowerKeyDoubleTap(intent) || isLaunchedByLockScreen(intent)) {
                    if (capturingMode == CapturingMode.UNKNOWN) {
                        setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                        break;
                    } else {
                        setCapturingMode(capturingMode, OneShotMode.NONE);
                        break;
                    }
                } else {
                    setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    checkLaunchCameraModeFromGoogleAssistant(action, intent, z);
                    break;
                }
                break;
            case "android.media.action.STILL_IMAGE_CAMERA_SECURE":
                setIsSecurePhotoLaunchedByIntent(true);
                if ((!isLaunchedByPowerKeyDoubleTap(intent) && !isLaunchedByLockScreen(intent)) || capturingMode == CapturingMode.UNKNOWN) {
                    setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                } else {
                    setCapturingMode(capturingMode, OneShotMode.NONE);
                    break;
                }
                break;
            case "android.media.action.VIDEO_CAMERA":
                setCapturingMode(CapturingMode.VIDEO, OneShotMode.NONE);
                checkLaunchCameraModeFromGoogleAssistant(action, intent, z);
                break;
            case "com.sonyericsson.android.camera.action.FRONT_STILL_IMAGE_CAMERA":
                if (PlatformCapability.isFrontCameraSupported()) {
                    setCapturingMode(CapturingMode.SUPERIOR_FRONT, OneShotMode.NONE);
                    break;
                }
                break;
            case "com.sonyericsson.android.camera.action.FRONT_VIDEO_CAMERA":
                setCapturingMode(CapturingMode.FRONT_VIDEO, OneShotMode.NONE);
                break;
            case "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE":
                this.mExtraOperation = ExtraOperation.LAUNCH_AND_CAPTURE;
                setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                break;
            default:
                if (action.equals(CapturingMode.NORMAL.getValue())) {
                    setCapturingMode(CapturingMode.NORMAL, OneShotMode.NONE);
                    break;
                } else if (action.equals(CapturingMode.FRONT_PHOTO.getValue())) {
                    setCapturingMode(CapturingMode.FRONT_PHOTO, OneShotMode.NONE);
                    break;
                } else if (action.equals(CapturingMode.SLOW_MOTION.getValue())) {
                    setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                    break;
                } else {
                    setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                }
        }
        Bundle extras = intent.getExtras();
        if (extras != null && getOneShotMode().isEnabled()) {
            for (String str : extras.keySet()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("#### intent extra key: " + str);
                }
            }
            setExtraOutput((Uri) extras.getParcelable("output"));
            setAddToMediaStore(extras.getBoolean(OneShotUtility.KEY_ADD_TO_MEDIA_STORE));
        } else {
            setExtraOutput(null);
            setAddToMediaStore(true);
        }
        if (getOneShotMode().isEnabled()) {
            this.mStorageTypeForOneshot = Storage.StorageType.INTERNAL;
            if (StorageUtil.getStorageTypeFromUri(getExtraOutput(), CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD) {
                this.mStorageTypeForOneshot = Storage.StorageType.EXTERNAL_CARD;
            }
        }
        readExtra(intent);
        String stringExtra = intent.getStringExtra(LAUNCH_TRIGGER);
        if (stringExtra != null) {
            if (LaunchTrigger.HW_CAMERA_KEY.toString().equals(stringExtra)) {
                setLaunchTrigger(LaunchTrigger.HW_CAMERA_KEY);
            } else if (LaunchTrigger.HW_CAMERA_KEY_LOCK.toString().equals(stringExtra)) {
                setLaunchTrigger(LaunchTrigger.HW_CAMERA_KEY_LOCK);
            } else if (LaunchTrigger.LOCK_SCREEN.toString().equals(stringExtra)) {
                setLaunchTrigger(LaunchTrigger.LOCK_SCREEN);
            } else if (LaunchTrigger.ADDONS.toString().equals(stringExtra)) {
                setLaunchTrigger(LaunchTrigger.ADDONS);
            }
        }
        if (getExtraOperation() == ExtraOperation.OPEN_SETTINGS_MENU) {
            setLaunchTrigger(LaunchTrigger.SETTINGS_SECURE_LOCK);
        }
        if (isLaunchedByLockScreen(intent)) {
            setLaunchTrigger(LaunchTrigger.LOCK_SCREEN);
        } else if (isLaunchedByPowerKeyDoubleTap(intent)) {
            setLaunchTrigger(LaunchTrigger.POWER_KEY_DOUBLE_TAP);
        } else if (isLaunchedByLiftTrigger(intent)) {
            setLaunchTrigger(LaunchTrigger.LIFT_TRIGGER);
        }
        if (getLaunchTrigger() == LaunchTrigger.OTHER) {
            if (getOneShotMode().isEnabled()) {
                setLaunchTrigger(LaunchTrigger.ONE_SHOT_APP);
            } else {
                setLaunchTrigger(LaunchTrigger.HOME);
            }
        }
        if (intent.hasExtra(EXTRA_LAUNCH_INTERNAL_MODE)) {
            this.mInternalModeValue = intent.getIntExtra(EXTRA_LAUNCH_INTERNAL_MODE, ModeSelectorInternalMode.MANUAL.ordinal());
            intent.removeExtra(EXTRA_LAUNCH_INTERNAL_MODE);
        }
        if (intent.hasExtra(EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE)) {
            this.mLaunchInternlCallingCapturingModeValue = intent.getIntExtra(EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE, CapturingMode.NORMAL.ordinal());
            intent.removeExtra(EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE);
        }
        updateCheckStartTime();
    }

    private boolean isLaunchedByPowerKeyDoubleTap(Intent intent) {
        return CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP.equals(intent.getStringExtra(EXTRA_CAMERA_LAUNCH_SOURCE));
    }

    private boolean isLaunchedByLiftTrigger(Intent intent) {
        return CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER.equals(intent.getStringExtra(EXTRA_CAMERA_LAUNCH_SOURCE));
    }

    private static boolean isLaunchedByLockScreen(Intent intent) {
        return CAMERA_LAUNCH_SOURCE_LOCKSCREEN.equals(intent.getStringExtra(EXTRA_CAMERA_LAUNCH_SOURCE));
    }

    private CapturingMode getOneShotCapturingMode(CapturingMode capturingMode) {
        return (capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.FRONT_VIDEO) ? CapturingMode.SUPERIOR_FRONT : CapturingMode.SCENE_RECOGNITION;
    }

    private boolean isResetCapturingMode(boolean z) {
        if (z || this.mIsLaunchedByActivityResult) {
            return false;
        }
        if (this.mAccessor == null) {
            this.mAccessor = new SharedPreferencesAccessor(CameraApplication.getContext(), SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME);
        }
        long j = this.mAccessor.readLong(SharedPreferencesConstants.KEY_TIME_APP_PAUSED, 0L);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (j != 0) {
            return jCurrentTimeMillis < j || jCurrentTimeMillis - j > 30000;
        }
        return false;
    }

    public void setCapturingMode(CapturingMode capturingMode, OneShotMode oneShotMode) {
        this.mCapturingMode = capturingMode;
        this.mOneShot = oneShotMode;
    }

    public void setCapturingMode(CapturingMode capturingMode) {
        this.mCapturingMode = capturingMode;
    }

    public LaunchCondition(IntentReader.VideoQualityConfigurations videoQualityConfigurations) {
        this.mVideoQualityConfigurations = videoQualityConfigurations;
    }

    public IntentReader.VideoQualityConfigurations getVideoQualityConfigurations() {
        return this.mVideoQualityConfigurations;
    }

    public ExtraOperation getExtraOperation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getExtraOperation: " + this.mExtraOperation);
        }
        return this.mExtraOperation;
    }

    public ExtraSettings getExtraSettings() {
        return this.mExtraSettings;
    }

    public String getUserSettingKeyName() {
        if (CamLog.VERBOSE) {
            CamLog.d("getUserSettingKeyName: " + this.mUserSettingKeyName);
        }
        return this.mUserSettingKeyName;
    }

    public CapturingMode getCapturingMode() {
        return this.mCapturingMode;
    }

    public void clearExtraOperation() {
        this.mExtraOperation = ExtraOperation.NONE;
        this.mUserSettingKeyName = null;
    }

    public OneShotMode getOneShotMode() {
        return this.mOneShot;
    }

    public Storage.StorageType getStorageTypeForOneshot() {
        return this.mStorageTypeForOneshot;
    }

    private void readExtra(Intent intent) {
        if (intent.getBooleanExtra(EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_OPEN, false)) {
            this.mExtraOperation = ExtraOperation.OPEN_SETTINGS_MENU;
            this.mUserSettingKeyName = intent.getStringExtra(EXTRA_REQUEST_ADVANCED_SETTINGS_DIALOG_KEY);
        } else if (getExtraOperation() == ExtraOperation.OPEN_SETTINGS_MENU) {
            clearExtraOperation();
        }
    }

    public LaunchCameraMode getLaunchCameraMode() {
        return this.mLaunchCameraMode;
    }

    public int getGoogleAssistantSelfTimer() {
        return this.mGoogleAssistantSelfTimer;
    }

    public boolean isGoogleAssistantLaunchOnly() {
        return this.mIsGoogleAssistantLaunchOnly;
    }

    public void clearLaunchCameraMode() {
        setLaunchCameraMode(LaunchCameraMode.NONE);
    }

    private void setLaunchCameraMode(LaunchCameraMode launchCameraMode) {
        this.mLaunchCameraMode = launchCameraMode;
    }

    // Mirrors the fallback control flow 1:1
    private void checkLaunchCameraModeFromGoogleAssistant(String action, Intent intent, boolean isVoiceInteractionRootFlag) {
        // Voice-interaction root gating (only true when both extras & flag are true)
        boolean fromAssistantVoiceRoot =
                intent.getBooleanExtra("is_voice_interaction_root", false) && isVoiceInteractionRootFlag;

        // Explicit launchTrigger extra takes precedence
        String trig = intent.getStringExtra("com.sonyericsson.android.camera.extra.launchTrigger");
        if (LaunchTrigger.APP_SHORTCUT.toString().equals(trig)) {
            setLaunchTrigger(LaunchTrigger.APP_SHORTCUT);
        } else {
            // Set GOOGLE_ASSISTANT unless launched by lock screen / power double-tap / lift
            boolean hasVoiceCat = intent.hasCategory("android.intent.category.VOICE");
            if (hasVoiceCat
                    || (!isLaunchedByLockScreen(intent)
                        && !isLaunchedByPowerKeyDoubleTap(intent)
                        && !isLaunchedByLiftTrigger(intent))) {
                setLaunchTrigger(LaunchTrigger.GOOGLE_ASSISTANT);
            }

            if (fromAssistantVoiceRoot) {
                mIsGoogleAssistantLaunchOnly =
                        intent.getBooleanExtra("com.google.assistant.extra.CAMERA_OPEN_ONLY", false);
            }
        }

        // Camera mode string (Google first, fallback to AOSP extra)
        final String mode = intent.hasExtra("com.google.assistant.extra.CAMERA_MODE")
                ? intent.getStringExtra("com.google.assistant.extra.CAMERA_MODE")
                : intent.getStringExtra("android.intent.extra.CAMERA_MODE");

        // Front camera request (Google or AOSP key)
        final boolean useFront =
                intent.getBooleanExtra("android.intent.extra.USE_FRONT_CAMERA", false)
             || intent.getBooleanExtra("com.google.assistant.extra.USE_FRONT_CAMERA", false);

        // Action dispatch
        if ("android.media.action.VIDEO_CAMERA".equals(action)) {
            // VIDEO branch
            if ("HDR_WITH_4K_RESOLUTION".equals(mode)
                    && PlatformCapability.isVideoHdrSupported(CameraInfo.CameraId.BACK)) {
                setLaunchCameraMode(LaunchCameraMode.FOUR_K_HDR);
            } else if ("SLOW_MOTION".equals(mode)) {
                setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                setLaunchCameraMode(LaunchCameraMode.SLOW_MOTION);
            } else if ("SUPER_SLOW_MOTION".equals(mode)) {
                if (PlatformCapability.isSuperSlowMotionSupported(CameraInfo.CameraId.BACK)) {
                    setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                    setLaunchCameraMode(LaunchCameraMode.SUPER_SLOW_MOTION);
                }
            } else if (useFront && PlatformCapability.isFrontCameraSupported()) {
                setCapturingMode(CapturingMode.FRONT_VIDEO, OneShotMode.NONE);
            }

        } else if ("android.media.action.STILL_IMAGE_CAMERA".equals(action)) {
            // STILL branch
            if (fromAssistantVoiceRoot) {
                if (intent.hasExtra("com.google.assistant.extra.TIMER_DURATION_SECONDS")) {
                    int sec = intent.getIntExtra("com.google.assistant.extra.TIMER_DURATION_SECONDS", 0);
                    ResearchUtil.getInstance().setAssistSelfTimer(sec);
                    // clamp between 3 and 30
                    if (sec <= 3) sec = 3;
                    else if (sec >= 30) sec = 30;
                    mIsGoogleAssistantLaunchOnly = false;
                    mGoogleAssistantSelfTimer = sec * 1000;
                } else if (!isGoogleAssistantLaunchOnly()) {
                    mGoogleAssistantSelfTimer = 3000; // default 3s
                }
                SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN
                        .setDurationInMillisecond(getGoogleAssistantSelfTimer());
            }

            if (useFront && PlatformCapability.isFrontCameraSupported()) {
                if ("MANUAL_MODE".equals(mode)) {
                    setCapturingMode(CapturingMode.FRONT_PHOTO, OneShotMode.NONE);
                } else {
                    setCapturingMode(CapturingMode.SUPERIOR_FRONT, OneShotMode.NONE);
                }
            } else if ("MANUAL_MODE".equals(mode)) {
                setCapturingMode(CapturingMode.NORMAL, OneShotMode.NONE);
            }
        }
    }

    public boolean isCorrectExtraOutputPath() {
        if (this.mExtraOutput != null) {
            if (!DocumentsContract.isDocumentUri(CameraApplication.getContext(), this.mExtraOutput)) {
                if (StorageUtil.getStorageTypeFromUri(this.mExtraOutput, CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD && !"content".equalsIgnoreCase(this.mExtraOutput.getScheme())) {
                    return false;
                }
            } else if (!StorageUtil.exists(CameraApplication.getContext(), this.mExtraOutput)) {
                return false;
            }
        }
        return true;
    }

    private void updateCheckStartTime() {
        this.mCheckStartTimeInMillis = SystemClock.elapsedRealtime();
    }

    private boolean isTimeIntervalBeyondThreshold() {
        return SystemClock.elapsedRealtime() - this.mCheckStartTimeInMillis > 2000;
    }

    public boolean isLaunchInternalMode() {
        return this.mInternalModeValue != -1;
    }

    public int getLaunchInternalMode() {
        return this.mInternalModeValue;
    }

    public void clearLaunchInternalMode() {
        this.mInternalModeValue = -1;
    }

    public int getLaunchInternalCallingCapturingMode() {
        return this.mLaunchInternlCallingCapturingModeValue;
    }

    public void clearLaunchInternalCallingCapturingMode() {
        this.mLaunchInternlCallingCapturingModeValue = -1;
    }
}
