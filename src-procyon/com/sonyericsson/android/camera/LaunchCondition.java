// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import java.util.Iterator;
import android.os.Bundle;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import android.provider.DocumentsContract;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.SystemClock;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonymobile.cameracommon.research.ResearchUtil;
import android.content.Intent;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.setting.ExtraSettings;
import android.net.Uri;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;

public class LaunchCondition
{
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
    private CapturingMode mCapturingMode;
    private long mCheckStartTimeInMillis;
    private ExtraOperation mExtraOperation;
    private Uri mExtraOutput;
    private final ExtraSettings mExtraSettings;
    private int mGoogleAssistantSelfTimer;
    private int mInternalModeValue;
    private boolean mIsGoogleAssistantLaunchOnly;
    private boolean mIsLaunchedByActivityResult;
    private boolean mIsLaunchedByIntent;
    private boolean mIsSecurePhotoLaunchedByIntent;
    private LaunchCameraMode mLaunchCameraMode;
    private int mLaunchInternlCallingCapturingModeValue;
    private LaunchTrigger mLaunchTrigger;
    private OneShotMode mOneShot;
    private Storage.StorageType mStorageTypeForOneshot;
    private String mUserSettingKeyName;
    private final IntentReader.VideoQualityConfigurations mVideoQualityConfigurations;
    
    public LaunchCondition(final IntentReader.VideoQualityConfigurations mVideoQualityConfigurations) {
        this.mExtraOperation = ExtraOperation.NONE;
        this.mCapturingMode = CapturingMode.UNKNOWN;
        this.mOneShot = OneShotMode.NONE;
        this.mStorageTypeForOneshot = Storage.StorageType.INTERNAL;
        this.mExtraSettings = new ExtraSettings();
        this.mLaunchTrigger = LaunchTrigger.OTHER;
        this.mLaunchCameraMode = LaunchCameraMode.NONE;
        this.mGoogleAssistantSelfTimer = 0;
        this.mIsGoogleAssistantLaunchOnly = true;
        this.mCheckStartTimeInMillis = 0L;
        this.mInternalModeValue = -1;
        this.mLaunchInternlCallingCapturingModeValue = -1;
        this.mIsLaunchedByIntent = false;
        this.mIsLaunchedByActivityResult = false;
        this.mVideoQualityConfigurations = mVideoQualityConfigurations;
    }
    
    private void checkLaunchCameraModeFromGoogleAssistant(final String s, final Intent intent, final boolean b) {
        final boolean booleanExtra = intent.getBooleanExtra("is_voice_interaction_root", false);
        int n = 1;
        final boolean b2 = booleanExtra && b;
        if (LaunchTrigger.APP_SHORTCUT.toString().equals(intent.getStringExtra("com.sonyericsson.android.camera.extra.launchTrigger"))) {
            this.setLaunchTrigger(LaunchTrigger.APP_SHORTCUT);
        }
        else {
            if (intent.hasCategory("android.intent.category.VOICE") || (!isLaunchedByLockScreen(intent) && !this.isLaunchedByPowerKeyDoubleTap(intent) && !this.isLaunchedByLiftTrigger(intent))) {
                this.setLaunchTrigger(LaunchTrigger.GOOGLE_ASSISTANT);
            }
            if (b2) {
                this.mIsGoogleAssistantLaunchOnly = intent.getBooleanExtra("com.google.assistant.extra.CAMERA_OPEN_ONLY", false);
            }
        }
        String anObject;
        if (intent.hasExtra("com.google.assistant.extra.CAMERA_MODE")) {
            anObject = intent.getStringExtra("com.google.assistant.extra.CAMERA_MODE");
        }
        else {
            anObject = intent.getStringExtra("android.intent.extra.CAMERA_MODE");
        }
        final boolean b3 = intent.getBooleanExtra("android.intent.extra.USE_FRONT_CAMERA", false) | intent.getBooleanExtra("com.google.assistant.extra.USE_FRONT_CAMERA", false);
        final int hashCode = s.hashCode();
        Label_0217: {
            if (hashCode != 464109999) {
                if (hashCode == 1130890360) {
                    if (s.equals("android.media.action.VIDEO_CAMERA")) {
                        n = 0;
                        break Label_0217;
                    }
                }
            }
            else if (s.equals("android.media.action.STILL_IMAGE_CAMERA")) {
                break Label_0217;
            }
            n = -1;
        }
        switch (n) {
            case 1: {
                if (b2) {
                    if (intent.hasExtra("com.google.assistant.extra.TIMER_DURATION_SECONDS")) {
                        final int intExtra = intent.getIntExtra("com.google.assistant.extra.TIMER_DURATION_SECONDS", 0);
                        ResearchUtil.getInstance().setAssistSelfTimer(intExtra);
                        int n2;
                        if (intExtra <= 3) {
                            n2 = 3;
                        }
                        else if ((n2 = intExtra) >= 30) {
                            n2 = 30;
                        }
                        this.mGoogleAssistantSelfTimer = n2 * 1000;
                        this.mIsGoogleAssistantLaunchOnly = false;
                    }
                    else if (!this.isGoogleAssistantLaunchOnly()) {
                        this.mGoogleAssistantSelfTimer = 3000;
                    }
                    SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN.setDurationInMillisecond(this.getGoogleAssistantSelfTimer());
                }
                if (b3 && PlatformCapability.isFrontCameraSupported()) {
                    if ("MANUAL_MODE".equals(anObject)) {
                        this.setCapturingMode(CapturingMode.FRONT_PHOTO, OneShotMode.NONE);
                        break;
                    }
                    this.setCapturingMode(CapturingMode.SUPERIOR_FRONT, OneShotMode.NONE);
                    break;
                }
                else {
                    if ("MANUAL_MODE".equals(anObject)) {
                        this.setCapturingMode(CapturingMode.NORMAL, OneShotMode.NONE);
                        break;
                    }
                    break;
                }
                break;
            }
            case 0: {
                if ("HDR_WITH_4K_RESOLUTION".equals(anObject)) {
                    if (PlatformCapability.isVideoHdrSupported(CameraInfo.CameraId.BACK)) {
                        this.setLaunchCameraMode(LaunchCameraMode.FOUR_K_HDR);
                        break;
                    }
                    break;
                }
                else {
                    if ("SLOW_MOTION".equals(anObject)) {
                        this.setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                        this.setLaunchCameraMode(LaunchCameraMode.SLOW_MOTION);
                        break;
                    }
                    if ("SUPER_SLOW_MOTION".toString().equals(anObject)) {
                        if (PlatformCapability.isSuperSlowMotionSupported(CameraInfo.CameraId.BACK)) {
                            this.setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                            this.setLaunchCameraMode(LaunchCameraMode.SUPER_SLOW_MOTION);
                            break;
                        }
                        break;
                    }
                    else {
                        if (b3 && PlatformCapability.isFrontCameraSupported()) {
                            this.setCapturingMode(CapturingMode.FRONT_VIDEO, OneShotMode.NONE);
                            break;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private void clearLaunchTrigger() {
        if (this.isTimeIntervalBeyondThreshold() && this.mExtraOperation == ExtraOperation.NONE) {
            this.setLaunchTrigger(LaunchTrigger.OTHER);
        }
    }
    
    private CapturingMode getOneShotCapturingMode(final CapturingMode capturingMode) {
        CapturingMode capturingMode2 = CapturingMode.SCENE_RECOGNITION;
        if (capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.FRONT_VIDEO) {
            capturingMode2 = CapturingMode.SUPERIOR_FRONT;
        }
        return capturingMode2;
    }
    
    private boolean isLaunchedByLiftTrigger(final Intent intent) {
        return "lift_to_launch_ml".equals(intent.getStringExtra("com.android.systemui.camera_launch_source"));
    }
    
    private static boolean isLaunchedByLockScreen(final Intent intent) {
        return "lockscreen_affordance".equals(intent.getStringExtra("com.android.systemui.camera_launch_source"));
    }
    
    private boolean isLaunchedByPowerKeyDoubleTap(final Intent intent) {
        return "power_double_tap".equals(intent.getStringExtra("com.android.systemui.camera_launch_source"));
    }
    
    private boolean isResetCapturingMode(final boolean b) {
        if (!b && !this.mIsLaunchedByActivityResult) {
            if (this.mAccessor == null) {
                this.mAccessor = new SharedPreferencesAccessor(CameraApplication.getContext(), "com.sonyericsson.android.camera.shared_preferences");
            }
            final long long1 = this.mAccessor.readLong("KEY_TIME_APP_PAUSED", 0L);
            final long currentTimeMillis = System.currentTimeMillis();
            if (long1 != 0L && (currentTimeMillis < long1 || currentTimeMillis - long1 > 30000L)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isTimeIntervalBeyondThreshold() {
        return SystemClock.elapsedRealtime() - this.mCheckStartTimeInMillis > 2000L;
    }
    
    private void readExtra(final Intent intent) {
        if (intent.getBooleanExtra("com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogopen", false)) {
            this.mExtraOperation = ExtraOperation.OPEN_SETTINGS_MENU;
            this.mUserSettingKeyName = intent.getStringExtra("com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogkey");
        }
        else if (this.getExtraOperation() == ExtraOperation.OPEN_SETTINGS_MENU) {
            this.clearExtraOperation();
        }
    }
    
    private void setAddToMediaStore(final boolean mAddToMediaStore) {
        this.mAddToMediaStore = mAddToMediaStore;
    }
    
    private void setExtraOutput(final Uri mExtraOutput) {
        this.mExtraOutput = mExtraOutput;
    }
    
    private void setIsSecurePhotoLaunchedByIntent(final boolean mIsSecurePhotoLaunchedByIntent) {
        this.mIsSecurePhotoLaunchedByIntent = mIsSecurePhotoLaunchedByIntent;
    }
    
    private void setLaunchCameraMode(final LaunchCameraMode mLaunchCameraMode) {
        this.mLaunchCameraMode = mLaunchCameraMode;
    }
    
    private void setLaunchTrigger(final LaunchTrigger mLaunchTrigger) {
        this.mLaunchTrigger = mLaunchTrigger;
    }
    
    private void updateCheckStartTime() {
        this.mCheckStartTimeInMillis = SystemClock.elapsedRealtime();
    }
    
    public void clearExtraOperation() {
        this.mExtraOperation = ExtraOperation.NONE;
        this.mUserSettingKeyName = null;
    }
    
    public void clearLaunchCameraMode() {
        this.setLaunchCameraMode(LaunchCameraMode.NONE);
    }
    
    public void clearLaunchInternalCallingCapturingMode() {
        this.mLaunchInternlCallingCapturingModeValue = -1;
    }
    
    public void clearLaunchInternalMode() {
        this.mInternalModeValue = -1;
    }
    
    public boolean getAddToMediaStore() {
        return this.mAddToMediaStore;
    }
    
    public CapturingMode getCapturingMode() {
        return this.mCapturingMode;
    }
    
    public ExtraOperation getExtraOperation() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getExtraOperation: ");
            sb.append(this.mExtraOperation);
            CamLog.d(sb.toString());
        }
        return this.mExtraOperation;
    }
    
    public Uri getExtraOutput() {
        return this.mExtraOutput;
    }
    
    public ExtraSettings getExtraSettings() {
        return this.mExtraSettings;
    }
    
    public int getGoogleAssistantSelfTimer() {
        return this.mGoogleAssistantSelfTimer;
    }
    
    public LaunchCameraMode getLaunchCameraMode() {
        return this.mLaunchCameraMode;
    }
    
    public int getLaunchInternalCallingCapturingMode() {
        return this.mLaunchInternlCallingCapturingModeValue;
    }
    
    public int getLaunchInternalMode() {
        return this.mInternalModeValue;
    }
    
    public LaunchTrigger getLaunchTrigger() {
        return this.mLaunchTrigger;
    }
    
    public OneShotMode getOneShotMode() {
        return this.mOneShot;
    }
    
    public Storage.StorageType getStorageTypeForOneshot() {
        return this.mStorageTypeForOneshot;
    }
    
    public String getUserSettingKeyName() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getUserSettingKeyName: ");
            sb.append(this.mUserSettingKeyName);
            CamLog.d(sb.toString());
        }
        return this.mUserSettingKeyName;
    }
    
    public IntentReader.VideoQualityConfigurations getVideoQualityConfigurations() {
        return this.mVideoQualityConfigurations;
    }
    
    public boolean isCorrectExtraOutputPath() {
        final Uri mExtraOutput = this.mExtraOutput;
        boolean b = false;
        if (mExtraOutput != null) {
            if (!DocumentsContract.isDocumentUri(CameraApplication.getContext(), this.mExtraOutput)) {
                if (StorageUtil.getStorageTypeFromUri(this.mExtraOutput, CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD && !"content".equalsIgnoreCase(this.mExtraOutput.getScheme())) {
                    return b;
                }
            }
            else if (!StorageUtil.exists(CameraApplication.getContext(), this.mExtraOutput)) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    public boolean isGoogleAssistantLaunchOnly() {
        return this.mIsGoogleAssistantLaunchOnly;
    }
    
    public boolean isLaunchInternalMode() {
        return this.mInternalModeValue != -1;
    }
    
    public boolean isSecurePhotoLaunchedByIntent() {
        return this.mIsSecurePhotoLaunchedByIntent;
    }
    
    public void onActivityResult(final int n, final Intent intent) {
        Label_0174: {
            if (n != 14) {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                break Label_0174;
                            }
                            case 19: {
                                this.setLaunchTrigger(LaunchTrigger.ADDONS);
                                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                                this.updateCheckStartTime();
                                break Label_0174;
                            }
                            case 18: {
                                this.setLaunchTrigger(LaunchTrigger.PORTRAIT_SELFIE);
                                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                                this.updateCheckStartTime();
                                break Label_0174;
                            }
                            case 16:
                            case 17: {
                                this.setLaunchTrigger(LaunchTrigger.DUAL_CAMERA_EFFECT);
                                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                                this.updateCheckStartTime();
                                break Label_0174;
                            }
                        }
                        break;
                    }
                    case 9: {
                        this.setLaunchTrigger(LaunchTrigger.VIEWER);
                        this.updateCheckStartTime();
                        break;
                    }
                    case 8: {
                        this.setLaunchTrigger(LaunchTrigger.VIEWER);
                        this.updateCheckStartTime();
                        break;
                    }
                }
            }
            else {
                this.setLaunchTrigger(LaunchTrigger.VIDEO_EDITOR);
                this.updateCheckStartTime();
            }
        }
        this.mIsLaunchedByActivityResult = true;
    }
    
    public void onPause() {
        this.mIsLaunchedByIntent = false;
        this.mIsLaunchedByActivityResult = false;
        this.mGoogleAssistantSelfTimer = 0;
        this.mIsGoogleAssistantLaunchOnly = true;
        this.mExtraSettings.clearAll();
    }
    
    public void onRestart(final boolean b, final boolean b2) {
        this.clearLaunchTrigger();
        if (!this.mIsLaunchedByIntent && !this.mIsLaunchedByActivityResult && (!b || !b2) && this.isResetCapturingMode(this.mOneShot.isEnabled())) {
            this.setCapturingMode(CapturingMode.SCENE_RECOGNITION);
        }
    }
    
    public void onResume() {
        this.clearLaunchTrigger();
    }
    
    public void setCapturingMode(final CapturingMode mCapturingMode) {
        this.mCapturingMode = mCapturingMode;
    }
    
    public void setCapturingMode(final CapturingMode mCapturingMode, final OneShotMode mOneShot) {
        this.mCapturingMode = mCapturingMode;
        this.mOneShot = mOneShot;
    }
    
    public void setup(final Intent obj, CapturingMode scene_RECOGNITION, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setup: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mIsLaunchedByIntent = true;
        String action;
        if ((action = obj.getAction()) == null) {
            action = "android.intent.action.MAIN";
            obj.setAction("android.intent.action.MAIN");
        }
        this.setLaunchTrigger(LaunchTrigger.OTHER);
        if (this.isResetCapturingMode(action == "android.media.action.IMAGE_CAPTURE" || action == "android.media.action.IMAGE_CAPTURE_SECURE" || action == "android.media.action.VIDEO_CAPTURE")) {
            scene_RECOGNITION = CapturingMode.SCENE_RECOGNITION;
        }
        this.setIsSecurePhotoLaunchedByIntent(false);
        this.clearLaunchCameraMode();
        this.mGoogleAssistantSelfTimer = 0;
        this.mIsGoogleAssistantLaunchOnly = true;
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("setLaunchMode: action: ");
            sb2.append(action);
            CamLog.d(sb2.toString());
        }
        switch (action) {
            default: {
                if (action.equals(CapturingMode.NORMAL.getValue())) {
                    this.setCapturingMode(CapturingMode.NORMAL, OneShotMode.NONE);
                    break;
                }
                if (action.equals(CapturingMode.FRONT_PHOTO.getValue())) {
                    this.setCapturingMode(CapturingMode.FRONT_PHOTO, OneShotMode.NONE);
                    break;
                }
                if (action.equals(CapturingMode.SLOW_MOTION.getValue())) {
                    this.setCapturingMode(CapturingMode.SLOW_MOTION, OneShotMode.NONE);
                    break;
                }
                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                break;
            }
            case "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE": {
                this.mExtraOperation = ExtraOperation.LAUNCH_AND_CAPTURE;
                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                break;
            }
            case "com.sonyericsson.android.camera.action.FRONT_VIDEO_CAMERA": {
                this.setCapturingMode(CapturingMode.FRONT_VIDEO, OneShotMode.NONE);
                break;
            }
            case "com.sonyericsson.android.camera.action.FRONT_STILL_IMAGE_CAMERA": {
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.setCapturingMode(CapturingMode.SUPERIOR_FRONT, OneShotMode.NONE);
                    break;
                }
                break;
            }
            case "android.media.action.VIDEO_CAMERA": {
                this.setCapturingMode(CapturingMode.VIDEO, OneShotMode.NONE);
                this.checkLaunchCameraModeFromGoogleAssistant(action, obj, b);
                break;
            }
            case "android.media.action.STILL_IMAGE_CAMERA_SECURE": {
                this.setIsSecurePhotoLaunchedByIntent(true);
                if (!this.isLaunchedByPowerKeyDoubleTap(obj) && !isLaunchedByLockScreen(obj)) {
                    this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                }
                if (scene_RECOGNITION == CapturingMode.UNKNOWN) {
                    this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                }
                this.setCapturingMode(scene_RECOGNITION, OneShotMode.NONE);
                break;
            }
            case "android.media.action.STILL_IMAGE_CAMERA": {
                if (!this.isLaunchedByPowerKeyDoubleTap(obj) && !isLaunchedByLockScreen(obj)) {
                    this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    this.checkLaunchCameraModeFromGoogleAssistant(action, obj, b);
                    break;
                }
                if (scene_RECOGNITION == CapturingMode.UNKNOWN) {
                    this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                }
                this.setCapturingMode(scene_RECOGNITION, OneShotMode.NONE);
                break;
            }
            case "android.media.action.VIDEO_CAPTURE": {
                CapturingMode capturingMode = CapturingMode.VIDEO;
                if (this.getOneShotCapturingMode(scene_RECOGNITION) == CapturingMode.SUPERIOR_FRONT) {
                    capturingMode = CapturingMode.FRONT_VIDEO;
                }
                this.setCapturingMode(capturingMode, OneShotMode.VIDEO);
                break;
            }
            case "android.media.action.IMAGE_CAPTURE_SECURE": {
                this.setIsSecurePhotoLaunchedByIntent(true);
                this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.PHOTO);
                break;
            }
            case "android.media.action.IMAGE_CAPTURE": {
                this.setCapturingMode(this.getOneShotCapturingMode(scene_RECOGNITION), OneShotMode.PHOTO);
                break;
            }
            case "android.intent.action.MAIN":
            case "com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH": {
                if (scene_RECOGNITION == CapturingMode.UNKNOWN) {
                    this.setCapturingMode(CapturingMode.SCENE_RECOGNITION, OneShotMode.NONE);
                    break;
                }
                this.setCapturingMode(scene_RECOGNITION, OneShotMode.NONE);
                break;
            }
        }
        final Bundle extras = obj.getExtras();
        if (extras == null) {
            this.setExtraOutput(null);
            this.setAddToMediaStore(true);
        }
        else if (this.getOneShotMode().isEnabled()) {
            for (final String str : extras.keySet()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("#### intent extra key: ");
                    sb3.append(str);
                    CamLog.d(sb3.toString());
                }
            }
            this.setExtraOutput((Uri)extras.getParcelable("output"));
            this.setAddToMediaStore(extras.getBoolean("addToMediaStore"));
        }
        else {
            this.setExtraOutput(null);
            this.setAddToMediaStore(true);
        }
        if (this.getOneShotMode().isEnabled()) {
            this.mStorageTypeForOneshot = Storage.StorageType.INTERNAL;
            if (StorageUtil.getStorageTypeFromUri(this.getExtraOutput(), CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD) {
                this.mStorageTypeForOneshot = Storage.StorageType.EXTERNAL_CARD;
            }
        }
        this.readExtra(obj);
        final String stringExtra = obj.getStringExtra("com.sonyericsson.android.camera.extra.launchTrigger");
        if (stringExtra != null) {
            if (LaunchTrigger.HW_CAMERA_KEY.toString().equals(stringExtra)) {
                this.setLaunchTrigger(LaunchTrigger.HW_CAMERA_KEY);
            }
            else if (LaunchTrigger.HW_CAMERA_KEY_LOCK.toString().equals(stringExtra)) {
                this.setLaunchTrigger(LaunchTrigger.HW_CAMERA_KEY_LOCK);
            }
            else if (LaunchTrigger.LOCK_SCREEN.toString().equals(stringExtra)) {
                this.setLaunchTrigger(LaunchTrigger.LOCK_SCREEN);
            }
            else if (LaunchTrigger.ADDONS.toString().equals(stringExtra)) {
                this.setLaunchTrigger(LaunchTrigger.ADDONS);
            }
        }
        if (this.getExtraOperation() == ExtraOperation.OPEN_SETTINGS_MENU) {
            this.setLaunchTrigger(LaunchTrigger.SETTINGS_SECURE_LOCK);
        }
        if (isLaunchedByLockScreen(obj)) {
            this.setLaunchTrigger(LaunchTrigger.LOCK_SCREEN);
        }
        else if (this.isLaunchedByPowerKeyDoubleTap(obj)) {
            this.setLaunchTrigger(LaunchTrigger.POWER_KEY_DOUBLE_TAP);
        }
        else if (this.isLaunchedByLiftTrigger(obj)) {
            this.setLaunchTrigger(LaunchTrigger.LIFT_TRIGGER);
        }
        if (this.getLaunchTrigger() == LaunchTrigger.OTHER) {
            if (this.getOneShotMode().isEnabled()) {
                this.setLaunchTrigger(LaunchTrigger.ONE_SHOT_APP);
            }
            else {
                this.setLaunchTrigger(LaunchTrigger.HOME);
            }
        }
        if (obj.hasExtra("internal_mode")) {
            this.mInternalModeValue = obj.getIntExtra("internal_mode", ModeSelectorInternalMode.MANUAL.ordinal());
            obj.removeExtra("internal_mode");
        }
        if (obj.hasExtra("capturing_mode")) {
            this.mLaunchInternlCallingCapturingModeValue = obj.getIntExtra("capturing_mode", CapturingMode.NORMAL.ordinal());
            obj.removeExtra("capturing_mode");
        }
        this.updateCheckStartTime();
    }
    
    public enum ExtraOperation
    {
        private static final ExtraOperation[] $VALUES;
        
        LAUNCH_AND_CAPTURE, 
        NONE, 
        OPEN_SETTINGS_MENU;
        
        static {
            $VALUES = new ExtraOperation[] { ExtraOperation.NONE, ExtraOperation.OPEN_SETTINGS_MENU, ExtraOperation.LAUNCH_AND_CAPTURE };
        }
    }
    
    public enum LaunchCameraMode
    {
        private static final LaunchCameraMode[] $VALUES;
        
        FOUR_K_HDR, 
        NONE, 
        SLOW_MOTION, 
        SUPER_SLOW_MOTION;
        
        static {
            $VALUES = new LaunchCameraMode[] { LaunchCameraMode.NONE, LaunchCameraMode.FOUR_K_HDR, LaunchCameraMode.SLOW_MOTION, LaunchCameraMode.SUPER_SLOW_MOTION };
        }
        
        public boolean isLaunchedByGoogleAssistant() {
            return this != LaunchCameraMode.NONE;
        }
        
        public boolean isSlowMotion() {
            return this == LaunchCameraMode.SLOW_MOTION || this == LaunchCameraMode.SUPER_SLOW_MOTION;
        }
    }
    
    public enum LaunchTrigger
    {
        private static final LaunchTrigger[] $VALUES;
        
        ADDONS, 
        APP_SHORTCUT, 
        DUAL_CAMERA_EFFECT, 
        GOOGLE_ASSISTANT, 
        HISTORY, 
        HOME, 
        HW_CAMERA_KEY, 
        HW_CAMERA_KEY_LOCK, 
        LIFT_TRIGGER, 
        LOCK_SCREEN, 
        MODE_SELECTOR, 
        MRU_SHORTCUT, 
        ONE_SHOT_APP, 
        OTHER, 
        PORTRAIT_SELFIE, 
        POWER_KEY_DOUBLE_TAP, 
        SAME_ACTIVITY, 
        SETTINGS_SECURE_LOCK, 
        VIDEO_EDITOR, 
        VIEWER;
        
        static {
            $VALUES = new LaunchTrigger[] { LaunchTrigger.LOCK_SCREEN, LaunchTrigger.HW_CAMERA_KEY, LaunchTrigger.HW_CAMERA_KEY_LOCK, LaunchTrigger.HOME, LaunchTrigger.ONE_SHOT_APP, LaunchTrigger.SETTINGS_SECURE_LOCK, LaunchTrigger.POWER_KEY_DOUBLE_TAP, LaunchTrigger.ADDONS, LaunchTrigger.LIFT_TRIGGER, LaunchTrigger.GOOGLE_ASSISTANT, LaunchTrigger.APP_SHORTCUT, LaunchTrigger.VIEWER, LaunchTrigger.VIDEO_EDITOR, LaunchTrigger.DUAL_CAMERA_EFFECT, LaunchTrigger.PORTRAIT_SELFIE, LaunchTrigger.OTHER, LaunchTrigger.HISTORY, LaunchTrigger.SAME_ACTIVITY, LaunchTrigger.MODE_SELECTOR, LaunchTrigger.MRU_SHORTCUT };
        }
    }
    
    public enum OneShotMode
    {
        private static final OneShotMode[] $VALUES;
        
        NONE, 
        PHOTO, 
        VIDEO;
        
        static {
            $VALUES = new OneShotMode[] { OneShotMode.NONE, OneShotMode.PHOTO, OneShotMode.VIDEO };
        }
        
        public boolean isEnabled() {
            return this != OneShotMode.NONE;
        }
        
        public boolean isPhoto() {
            return this == OneShotMode.PHOTO;
        }
        
        public boolean isVideo() {
            return this == OneShotMode.VIDEO;
        }
    }
}
